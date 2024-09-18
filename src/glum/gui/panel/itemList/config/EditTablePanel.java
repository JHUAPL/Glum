// Copyright (C) 2024 The Johns Hopkins University Applied Physics Laboratory LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package glum.gui.panel.itemList.config;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.*;
import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.DefaultTableCellRenderer;

import glum.gui.FocusUtil;
import glum.gui.GuiUtil;
import glum.gui.action.ClickAction;
import glum.gui.component.GComboBox;
import glum.gui.component.GTextField;
import glum.gui.icon.*;
import glum.gui.misc.BooleanCellEditor;
import glum.gui.misc.BooleanCellRenderer;
import glum.gui.panel.GlassPanel;
import glum.gui.panel.generic.MessagePanel;
import glum.gui.panel.itemList.*;
import glum.gui.panel.itemList.query.QueryAttribute;
import glum.gui.panel.itemList.query.QueryComposer;
import glum.zio.*;
import net.miginfocom.swing.MigLayout;

/**
 * UI component that allows for the configuration of the table contained by an {@link ItemListPanel}.
 * <p>
 * Construction of this panel involves passing a reference to the {@link ItemListPanel}'s {@link TableColumnHandler}.
 * This handler is retrieved via {@link ItemListPanel#getTableColumnHandler()}.
 *
 * @author lopeznr1
 */
public class EditTablePanel<G2 extends Enum<?>> extends GlassPanel
		implements ActionListener, ItemListener, ListSelectionListener, ZioObj
{
	// Attributes
	private final TableColumnHandler<G2> refTableColumnHandler;

	// GUI vars
	private JLabel titleL;
	private JRadioButton profileRB, customRB;
	private GComboBox<ProfileConfig<G2>> profileBox;
	private ItemListPanel<QueryAttribute<G2>, ConfigLookUp> listPanel;
	private BooleanCellEditor col0Editor;
	private BooleanCellRenderer col0Renderer;
	private DefaultTableCellRenderer col1Renderer;
	private JButton closeB, saveB, upB, downB, deleteB;
	private JLabel labelL;
	private GTextField labelTF;
	private AddProfilePanel profilePanel;

	// State vars
	protected StaticItemProcessor<QueryAttribute<G2>> myItemProcessor;

	/** Standard Constructor */
	public EditTablePanel(Component aParent, TableColumnHandler<G2> aTableColumnHandler)
	{
		super(aParent);

		// State vars
		refTableColumnHandler = aTableColumnHandler;

		// Build the actual GUI
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
	 * Adds a predefined profile (as specified in aConfig) to the list of available profiles.
	 *
	 * @param aConfig
	 */
	public void addConfig(ProfileConfig<G2> aConfig)
	{
		profileBox.addItem(aConfig);
	}

	public List<ProfileConfig<G2>> getAllConfig()
	{
		return profileBox.getAllItems();
	}

	/**
	 * Synchronizes the gui to match the model
	 */
	public void syncGui()
	{
		myItemProcessor.setItems(refTableColumnHandler.getOrderedAttributes());
	}

	@Override
	public void actionPerformed(ActionEvent aEvent)
	{
		var tmpItem = listPanel.getSelectedItem();

		var source = aEvent.getSource();
		if (source == labelTF)
		{
			updateLayerAttribute();
			return;
		}
		else if (source == deleteB)
		{
			doActionDel();
		}
		else if (source == closeB)
		{
			setVisible(false);
			notifyListeners(this, 0, "Close");
		}
		else if (source == saveB)
		{
			var nameS = new HashSet<String>();
			for (ProfileConfig<G2> aProfile : profileBox.getAllItems())
				nameS.add(aProfile.name());

			profilePanel.resetGui();
			profilePanel.setReservedNames(nameS);
			profilePanel.setVisible(true);
		}
		else if (source == upB)
		{
			var index = myItemProcessor.indexOf(tmpItem);
			refTableColumnHandler.moveSortedAttribute(index, index - 1);
			refTableColumnHandler.rebuildTableColumns();

			myItemProcessor.setItems(refTableColumnHandler.getOrderedAttributes());
		}
		else if (source == downB)
		{
			var index = myItemProcessor.indexOf(tmpItem);
			refTableColumnHandler.moveSortedAttribute(index, index + 1);
			refTableColumnHandler.rebuildTableColumns();

			myItemProcessor.setItems(refTableColumnHandler.getOrderedAttributes());
		}
		else if (source == col0Editor)
		{
			refTableColumnHandler.rebuildTableColumns();
		}
		else if (source == profileBox || source == profileRB)
		{
			var tmpConfig = profileBox.getChosenItem();
			refTableColumnHandler.setOrderAndConfig(tmpConfig.getItems());
			refTableColumnHandler.rebuildTableColumns();

			myItemProcessor.setItems(refTableColumnHandler.getOrderedAttributes());
		}
		else if (source == profilePanel && aEvent.getID() == AddProfilePanel.ID_ACCEPT)
		{
			var tmpName = profilePanel.getInput();
			var tmpItemL = myItemProcessor.getAllItems();
			for (var aAttribute : myItemProcessor.getAllItems())
				aAttribute.synchronizeAttribute();

			// Determine if this will result in a Profile being replaced
			ProfileConfig<G2> repProfile = null;
			for (ProfileConfig<G2> aProfile : profileBox.getAllItems())
			{
				if (aProfile.name().equals(tmpName) == true)
					repProfile = aProfile;
			}

			var tmpProfile = new ProfileConfig<>(tmpName, tmpItemL);
			if (repProfile == null)
				profileBox.addItem(tmpProfile);
			else
				profileBox.replaceItem(repProfile, tmpProfile);
		}

		updateGui();
	}

	@Override
	public void itemStateChanged(ItemEvent aEvent)
	{
		var source = aEvent.getSource();
		if (source == profileRB)
		{
			var tmpConfig = profileBox.getChosenItem();
			refTableColumnHandler.setOrderAndConfig(tmpConfig.getItems());
			refTableColumnHandler.rebuildTableColumns();

			myItemProcessor.setItems(refTableColumnHandler.getOrderedAttributes());
		}

		updateGui();
	}

	@Override
	public void valueChanged(ListSelectionEvent aEvent)
	{
		// Update only after the user has released the mouse
		if (aEvent.getValueIsAdjusting() == true)
			return;

		updateGui();
	}

	@Override
	public void zioRead(ZinStream aStream) throws IOException
	{
		super.zioRead(aStream);

		aStream.readVersion(0);

		var tmpBool = aStream.readBool();
		customRB.setSelected(tmpBool);
		profileRB.setSelected(!tmpBool);

		var numItems = aStream.readInt();
		profileBox.removeAllItems();
		for (int c1 = 0; c1 < numItems; c1++)
		{
			var tmpProfileConfig = ProfileConfig.zioRead(aStream, myItemProcessor.getAllItems());
			profileBox.addItem(tmpProfileConfig);
		}

		var profileIndex = aStream.readInt();
		profileBox.removeActionListener(this);
		if (profileIndex >= 0)
			profileBox.setSelectedIndex(profileIndex);
		profileBox.addActionListener(this);

		var tmpProfileConfig = ProfileConfig.zioRead(aStream, myItemProcessor.getAllItems());
		refTableColumnHandler.setOrderAndConfig(tmpProfileConfig.getItems());
		refTableColumnHandler.rebuildTableColumns();
		refTableColumnHandler.setSortPriorityList(tmpProfileConfig.sortPriorityList());

		myItemProcessor.setItems(refTableColumnHandler.getOrderedAttributes());

		updateGui();
	}

	@Override
	public void zioWrite(ZoutStream aStream) throws IOException
	{
		super.zioWrite(aStream);

		aStream.writeVersion(0);

		var tmpBool = customRB.isSelected();
		aStream.writeBool(tmpBool);

		var numItems = profileBox.getAllItems().size();
		aStream.writeInt(numItems);

		for (var aProfile : profileBox.getAllItems())
			ProfileConfig.zioWrite(aStream, aProfile);

		var profileIndex = profileBox.getSelectedIndex();
		aStream.writeInt(profileIndex);

		var tmpProfileConfig = new ProfileConfig<>("", refTableColumnHandler.getOrderedAttributes(),
				refTableColumnHandler.getSortPriorityList());
		ProfileConfig.zioWrite(aStream, tmpProfileConfig);
	}

	/**
	 * Helper method that builds the main GUI area
	 */
	private void buildGuiArea()
	{
		// Form the layout
		setLayout(new MigLayout("", "[left][grow][]", "[][][]3[grow][]"));

		// Title Area
		titleL = new JLabel("Table Config", JLabel.CENTER);
		add(titleL, "growx,span 2,wrap");

		// Profile Area
		profileRB = GuiUtil.createJRadioButton(this, "Profile:");
		add(profileRB, "span 1");

		profileBox = new GComboBox<>();
		profileBox.addActionListener(this);
		add(profileBox, "growx,span 1");

		deleteB = GuiUtil.createJButton(new DeleteIcon(14), this);
		add(deleteB, "align right,span 1,w 20!,h 20!,wrap");

		// Custom Area
		customRB = GuiUtil.createJRadioButton(this, "Custom:");
		add(customRB, "");

		// saveB = GuiUtil.createJButton(new ArrowNorthIcon(14), this);
		// add(saveB, "align right,span 1,w 18!, h 18!");
		saveB = GuiUtil.createJButton("Save", this);
		add(saveB, "align right,span 2,wrap");

		var tmpPanel = buildItemListTablePanel();
		tmpPanel.setBorder(new EmptyBorder(0, 15, 0, 0));
		add(tmpPanel, "growx,growy,span,wrap");

		// Link the radio buttons
		GuiUtil.linkRadioButtons(profileRB, customRB);

		// Build the config area
		tmpPanel = buildConfigPanel();
		add(tmpPanel, "growx,span,wrap");

		// Action area
		closeB = GuiUtil.createJButton("Close", this);
		add(closeB, "align right,span");

		// Add in the default profile
		var tmpConfig = new ProfileConfig<>(ProfileConfig.DEFAULT_NAME, refTableColumnHandler.getOrderedAttributes());
		profileBox.addItem(tmpConfig);

		customRB.setSelected(true);
	}

	/**
	 * Helper method to build the configuration area for the individual attributes
	 */
	private JPanel buildConfigPanel()
	{
		// Form the layout
		var tmpPanel = new JPanel();
		tmpPanel.setLayout(new MigLayout("", "0[grow][][]0", "0[][]10"));

		// Title Area
		labelL = new JLabel("Label:", JLabel.LEADING);
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
	 * Helper method to build the query item list table
	 */
	private JPanel buildItemListTablePanel()
	{
		var tmpComposer = new QueryComposer<ConfigLookUp>();
		tmpComposer.addAttribute(ConfigLookUp.IsVisible, Boolean.class, "", "");
		tmpComposer.addAttribute(ConfigLookUp.Name, String.class, "Name", null);
		tmpComposer.addAttribute(ConfigLookUp.Label, String.class, "Label", null);

		col0Editor = new BooleanCellEditor();
		col0Editor.addActionListener(this);
		col0Renderer = new BooleanCellRenderer();
		col1Renderer = new DefaultTableCellRenderer();
		tmpComposer.setEditor(ConfigLookUp.IsVisible, col0Editor);
		tmpComposer.setRenderer(ConfigLookUp.IsVisible, col0Renderer);
		tmpComposer.setRenderer(ConfigLookUp.Name, col1Renderer);
		tmpComposer.setRenderer(ConfigLookUp.Label, col1Renderer);

		var tmpIH = new ConfigHandler<G2>();
		myItemProcessor = new StaticItemProcessor<>();
		myItemProcessor.setItems(refTableColumnHandler.getOrderedAttributes());

		listPanel = new ItemListPanel<>(tmpIH, myItemProcessor, tmpComposer, false);
		listPanel.setSortingEnabled(false);
		listPanel.addListSelectionListener(this);
		return listPanel;
	}

	/**
	 * Helper method that executes the delete profile action.
	 */
	private void doActionDel()
	{
		var tmpPick = profileBox.getChosenItem();
		if (tmpPick.name() == ProfileConfig.DEFAULT_NAME)
		{
			var msgPanel = new MessagePanel(this, "Reserved Profile");
			msgPanel.setInfo("The Default profile can not be deleted.");
			msgPanel.setSize(400, 140);
			msgPanel.setVisibleAsModal();
			return;
		}

		profileBox.removeItem(profileBox.getChosenItem());
		actionPerformed(new ActionEvent(profileBox, ID_UPDATE, null));
	}

	/**
	 * Synchronizes the model to match the label gui
	 */
	private void updateLayerAttribute()
	{
		var chosenItem = listPanel.getSelectedItem();
		chosenItem.label = labelTF.getText();

		chosenItem.assocTableColumn.setHeaderValue(chosenItem.label);

		listPanel.repaint();
//getParent().repaint();
	}

	/**
	 * Synchronizes our GUI vars
	 */
	private void updateGui()
	{
		// Update the profile area
		var isEnabled = profileRB.isSelected();
		profileBox.setEnabled(isEnabled);

		isEnabled = isEnabled & (profileBox.getAllItems().size() > 1);
		deleteB.setEnabled(isEnabled);

		// Update the custom area
		isEnabled = customRB.isSelected();
		saveB.setEnabled(isEnabled);
		GuiUtil.setEnabled(listPanel, isEnabled);
		col0Renderer.setEnabled(isEnabled);
		col1Renderer.setEnabled(isEnabled);

		var chosenItem = listPanel.getSelectedItem();
		var chosenIndex = myItemProcessor.indexOf(chosenItem);
		if (chosenItem != null)
			labelTF.setText(chosenItem.label);

		isEnabled = isEnabled & (chosenItem != null);
		labelL.setEnabled(isEnabled);
		labelTF.setEnabled(isEnabled);

		upB.setEnabled(isEnabled & (chosenIndex > 0));
		downB.setEnabled(isEnabled & (chosenIndex + 1 < myItemProcessor.getNumItems()));

		listPanel.repaint();
	}

}
