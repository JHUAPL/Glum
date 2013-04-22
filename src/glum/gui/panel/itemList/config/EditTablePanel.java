package glum.gui.panel.itemList.config;

import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import com.google.common.collect.Sets;

import glum.gui.*;
import glum.gui.action.*;
import glum.gui.component.GComboBox;
import glum.gui.component.GTextField;
import glum.gui.icon.ArrowNorthIcon;
import glum.gui.icon.ArrowSouthIcon;
import glum.gui.icon.DeleteIcon;
import glum.gui.misc.*;
import glum.gui.panel.*;
import glum.gui.panel.itemList.BasicItemHandler;
import glum.gui.panel.itemList.ItemHandler;
import glum.gui.panel.itemList.ItemListPanel;
import glum.gui.panel.itemList.StaticItemProcessor;
import glum.gui.panel.itemList.query.*;
import glum.zio.ZinStream;
import glum.zio.ZoutStream;
import glum.zio.raw.ZioRaw;

import net.miginfocom.swing.MigLayout;

public class EditTablePanel extends GlassPanel implements ActionListener, ZioRaw, ListSelectionListener
{
	// GUI vars
	protected JLabel titleL;
	protected JRadioButton profileRB, customRB;
	protected GComboBox<ProfileConfig> profileBox;
	protected ItemListPanel<QueryAttribute> listPanel;
	protected BooleanCellEditor col0Editor;
	protected BooleanCellRenderer col0Renderer;
	protected DefaultTableCellRenderer col1Renderer;
	protected JButton closeB, saveB, upB, downB, deleteB;
	protected JLabel labelL;
	protected GTextField labelTF;
	protected Font smallFont;
	protected AddProfilePanel profilePanel;

	// State vars
	protected BasicItemHandler<?> refItemHandler;
	protected StaticItemProcessor<QueryAttribute> myItemProcessor;

	public EditTablePanel(Component aParent, BasicItemHandler<?> aItemHandler)
	{
		super(aParent);

		// State vars
		refItemHandler = aItemHandler;

		// Build the actual GUI
		smallFont = (new JTextField()).getFont();
		buildGuiArea();
		setPreferredSize(new Dimension(250, getPreferredSize().height));
		
		profilePanel = new AddProfilePanel(this);
		profilePanel.setSize(375, 140);
		profilePanel.addActionListener(this);
		
		syncGui();
		updateGui();
		
		// Set up some keyboard shortcuts
		FocusUtil.addAncestorKeyBinding(this, "ESCAPE", new ClickAction(closeB));
	}

	/**
	 * Adds a predefined profile (as specified in aConfig) to the
	 * list of available profiles.
	 * @param aConfig
	 */
	public void addConfig(ProfileConfig aConfig)
	{
		profileBox.addItem(aConfig);
	}
	
	public ArrayList<ProfileConfig> getAllConfig()
	{
		return profileBox.getAllItems();
	}
	
	public BasicItemHandler<?> getItemHandler()
	{
		return refItemHandler;
	}
	
	/**
	 * Synchronizes the gui to match the model 
	 */
	public void syncGui()
	{
		myItemProcessor.setItems(refItemHandler.getSortedAttributes());
	}
	
	@Override
	public void actionPerformed(ActionEvent aEvent)
	{
		Object source;
		QueryAttribute aItem;
		int index;

		aItem = listPanel.getSelectedItem();

		source = aEvent.getSource();
		if (source == labelTF)
		{
			updateLayerAttribute();
			return;
		}
		else if (source == deleteB)
		{
			profileBox.removeItem(profileBox.getChosenItem());
			actionPerformed(new ActionEvent(profileBox, ID_UPDATE, null));
		}
		else if (source == closeB)
		{
			setVisible(false);
			notifyListeners(this, 0, "Close");
		}
		else if (source == saveB)
		{
			Set<String> nameSet;
			
			nameSet = Sets.newHashSet();
			for (ProfileConfig aProfile : profileBox.getAllItems())
				nameSet.add(aProfile.getName());
			
			profilePanel.resetGui();
			profilePanel.setReservedNames(nameSet);
			profilePanel.setVisible(true);
		}
		else if (source == upB)
		{
			index = myItemProcessor.indexOf(aItem);
			refItemHandler.moveSortedAttribute(index, index - 1);
			refItemHandler.rebuildTableColumns();
			
			myItemProcessor.setItems(refItemHandler.getSortedAttributes());
		}
		else if (source == downB)
		{
			index = myItemProcessor.indexOf(aItem);
			refItemHandler.moveSortedAttribute(index, index + 1);
			refItemHandler.rebuildTableColumns();
			
			myItemProcessor.setItems(refItemHandler.getSortedAttributes());
		}
		else if (source == col0Editor)
		{
			refItemHandler.rebuildTableColumns();
		}
		else if (source == profileBox || source == profileRB)
		{
			ProfileConfig aConfig;
			
			aConfig = profileBox.getChosenItem();
			refItemHandler.setOrderAndConfig(aConfig.getItems());
			refItemHandler.rebuildTableColumns();
			
			myItemProcessor.setItems(refItemHandler.getSortedAttributes());
		}
		else if (source == profilePanel && aEvent.getID() == AddProfilePanel.ID_ACCEPT)
		{
			ProfileConfig aProfile;
			Collection<QueryAttribute> aItemList;
			String aName;
			
			aName = profilePanel.getInput();
			aItemList = myItemProcessor.getItems();
			for (QueryAttribute aAttribute : aItemList)
				aAttribute.synchronizeAttribute();
			
			aProfile = new ProfileConfig(aName, aItemList);
			profileBox.addItem(aProfile);
		}

		updateGui();
	}
	
	@Override
	public void zioReadRaw(ZinStream aStream) throws IOException
	{
		ProfileConfig aProfile;
		int numItems, profileIndex;
		boolean aBool;
		
		super.zioReadRaw(aStream);
		
		aStream.readVersion(0);
		
		aBool = aStream.readBool();
		customRB.setSelected(aBool);
		profileRB.setSelected(!aBool);
		
		numItems = aStream.readInt();
		profileBox.removeAllItems();
		for (int c1 = 0; c1 < numItems; c1++)
		{
			aProfile = new ProfileConfig("unnamed", myItemProcessor.getItems());
			aProfile.zioReadRaw(aStream);
			
			profileBox.addItem(aProfile);
		}
		
		profileIndex = aStream.readInt();
		profileBox.removeActionListener(this);
		if (profileIndex >= 0)		
			profileBox.setSelectedIndex(profileIndex);
		profileBox.addActionListener(this);
		
		refItemHandler.zioReadRaw(aStream);
		
		updateGui();
	}

	@Override
	public void zioWriteRaw(ZoutStream aStream) throws IOException
	{
		int numItems, profileIndex;
		boolean aBool;
		
		super.zioWriteRaw(aStream);

		aStream.writeVersion(0);
		
		aBool = customRB.isSelected();
		aStream.writeBool(aBool);
		
		numItems = profileBox.getAllItems().size();
		aStream.writeInt(numItems);
		
		for (ProfileConfig aProfile : profileBox.getAllItems())
		{
			aProfile.zioWriteRaw(aStream);
		}
		
		profileIndex = profileBox.getSelectedIndex();
		aStream.writeInt(profileIndex);
		
		refItemHandler.zioWriteRaw(aStream);
	}

	@Override
	public void valueChanged(ListSelectionEvent e)
	{
		// Update only after the user has released the mouse
		if (e.getValueIsAdjusting() == true)
			return;

		updateGui();
	}

	/**
	 * Builds the main GUI area
	 */
	protected void buildGuiArea()
	{
		JPanel tmpPanel;
		ProfileConfig aConfig;

		// Form the layout
		setLayout(new MigLayout("", "[left][grow][]", "[][][]3[grow][]"));

		// Title Area
		titleL = new JLabel("Table Config", JLabel.CENTER);
		add(titleL, "growx,span 2,wrap");

		// Profile Area
		profileRB = GuiUtil.createJRadioButton("Profile:", this, smallFont);
		add(profileRB, "span 1");

		profileBox = new GComboBox<ProfileConfig>();
		profileBox.addActionListener(this);
		profileBox.setFont(smallFont);
		add(profileBox, "growx,span 1");
		
		deleteB = GuiUtil.createJButton(new DeleteIcon(14), this);
		add(deleteB, "align right,span 1,w 20!, h 20!,wrap");
		
		// Custom Area
		customRB = GuiUtil.createJRadioButton("Custom:", this, smallFont);
		customRB.setSelected(true);
		add(customRB, "span 1");

		//saveB = GuiUtil.createJButton(new ArrowNorthIcon(14), this);
		//add(saveB, "align right,span 1,w 18!, h 18!");
		saveB = GuiUtil.createJButton("Save", this, smallFont);
		add(saveB, "align right,span 2,wrap");
		
		tmpPanel = buildItemListTablePanel();
		tmpPanel.setBorder(new EmptyBorder(0, 15, 0, 0));
		add(tmpPanel, "growx,growy,span,wrap");

		// Link the radio buttons
		GuiUtil.linkRadioButtons(profileRB, customRB);
		
		// Build the config area
		tmpPanel = buildConfigPanel();
		add(tmpPanel, "growx,span,wrap");

		// Action area
		closeB = GuiUtil.createJButton("Close", this, smallFont);
		add(closeB, "align right,span");

		// Add in the default profile
		aConfig = new ProfileConfig(AddProfilePanel.DEFAULT_NAME, refItemHandler.getSortedAttributes());
		profileBox.addItem(aConfig);

		setBorder(new BevelBorder(BevelBorder.RAISED));
	}

	/**
	 * Utility method to build the configuration area for the individual attributes
	 */
	protected JPanel buildConfigPanel()
	{
		JPanel tmpPanel;
		
		// Form the layout
		tmpPanel = new JPanel();
		tmpPanel.setLayout(new MigLayout("", "0[grow][][]0", "0[][]10"));

		// Title Area
		labelL = GuiUtil.createJLabel("Label:", smallFont);
		tmpPanel.add(labelL, "growx,span 1");
		
		upB = GuiUtil.createJButton(new ArrowNorthIcon(14), this);
		tmpPanel.add(upB, "align right,span 1,w 18!, h 18!");
		
		downB = GuiUtil.createJButton(new ArrowSouthIcon(14), this);
		tmpPanel.add(downB, "align right,span 1,w 18!, h 18!,wrap");
		
		labelTF = new GTextField(this); 
		tmpPanel.add(labelTF, "growx,span 3,wrap");
		
		return tmpPanel;
	}

	/**
	 * Utility method to build the query item list table
	 */
	protected JPanel buildItemListTablePanel()
	{
		QueryComposer<ConfigLookUp> aComposer;
		ItemHandler<QueryAttribute> aItemHandler;
		
		aComposer = new QueryComposer<ConfigLookUp>();
		aComposer.addAttribute(ConfigLookUp.IsVisible, Boolean.class, "", "");
		aComposer.addAttribute(ConfigLookUp.Name, String.class, "Name", null);
		aComposer.addAttribute(ConfigLookUp.Label, String.class, "Label", null);
		
		col0Editor = new BooleanCellEditor();
		col0Editor.addActionListener(this);
		col0Renderer = new BooleanCellRenderer();
		col1Renderer = new DefaultTableCellRenderer();
		aComposer.setEditor(ConfigLookUp.IsVisible, col0Editor);
		aComposer.setRenderer(ConfigLookUp.IsVisible, col0Renderer);
		aComposer.setRenderer(ConfigLookUp.Name, col1Renderer);
		aComposer.setRenderer(ConfigLookUp.Label, col1Renderer);
		
		aItemHandler = new ConfigHandler(aComposer);
		
		myItemProcessor = new StaticItemProcessor<QueryAttribute>();
		myItemProcessor.setItems(refItemHandler.getSortedAttributes());
		
		listPanel = new ItemListPanel<QueryAttribute>(aItemHandler, myItemProcessor, false, false);
		listPanel.setSortingEnabled(false);
		listPanel.addListSelectionListener(this);
		return listPanel;
	}
	
	/**
	 * Synchronizes the model to match the label gui
	 */
	protected void updateLayerAttribute()
	{
		QueryAttribute chosenItem;
		
		chosenItem = listPanel.getSelectedItem();
		chosenItem.label = labelTF.getText();
		
		chosenItem.assocTableColumn.setHeaderValue(chosenItem.label);
		
		listPanel.repaint();
//getParent().repaint();
	}

	/**
	 * Synchronizes our GUI vars
	 */
	protected void updateGui()
	{
		QueryAttribute chosenItem;
		boolean isEnabled;
		int chosenIndex;

		// Update the profile area
		isEnabled = profileRB.isSelected();
		profileBox.setEnabled(isEnabled);
		
		isEnabled = isEnabled & (profileBox.getAllItems().size() > 1);
		deleteB.setEnabled(isEnabled);

		// Update the custom area
		isEnabled = customRB.isSelected();
		saveB.setEnabled(isEnabled);
		GuiUtil.setEnabled(listPanel, isEnabled);
		col0Renderer.setEnabled(isEnabled);
		col1Renderer.setEnabled(isEnabled);
		
		chosenItem = listPanel.getSelectedItem();
		chosenIndex = myItemProcessor.indexOf(chosenItem);
		if (chosenItem != null)
			labelTF.setText(chosenItem.label);

		isEnabled = isEnabled & (chosenItem != null);
		labelL.setEnabled(isEnabled);
		labelTF.setEnabled(isEnabled);
		
		upB.setEnabled(isEnabled & (chosenIndex > 0));
		downB.setEnabled(isEnabled & (chosenIndex+1 < myItemProcessor.getNumItems()));

		listPanel.repaint();			
	}

}
