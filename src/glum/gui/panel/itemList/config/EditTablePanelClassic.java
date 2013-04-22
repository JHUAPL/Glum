package glum.gui.panel.itemList.config;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.border.*;

import glum.gui.*;
import glum.gui.action.*;
import glum.gui.component.GComboBox;
import glum.gui.panel.*;
import glum.gui.panel.itemList.query.*;

import net.miginfocom.swing.MigLayout;

public class EditTablePanelClassic extends GlassPanel implements ActionListener
{
	// GUI vars
	protected JLabel titleL;
	protected JRadioButton profileRB, customRB;
	protected GComboBox<ProfileConfig> profileBox;
	protected JButton closeB;

	// State vars
	protected QueryItemHandler<?> refItemHandler;
	protected Map<JCheckBox, QueryAttribute> actionMap;

	public EditTablePanelClassic(Component aParent, QueryItemHandler<?> aItemHandler)
	{
		super(aParent);

		// State vars
		refItemHandler = aItemHandler;
		actionMap = new HashMap<JCheckBox, QueryAttribute>();

		// Build the actual GUI
		buildGuiArea();
		setPreferredSize(new Dimension(200, getPreferredSize().height));
		updateGui();
		
		// Set up some keyboard shortcuts
		FocusUtil.addAncestorKeyBinding(this, "ESCAPE", new ClickAction(closeB));
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object source;

		source = e.getSource();

		if (source == closeB)
		{
			setVisible(false);
			notifyListeners(this, 0, "Close");
		}
		else if (source == profileBox || source == profileRB)
		{
			ProfileConfig aConfig;
			QueryAttribute aAttribute;
			
			aConfig = profileBox.getChosenItem();
			refItemHandler.setOrderAndConfig(aConfig.getItems());
			refItemHandler.rebuildTableColumns();
			
			for (JCheckBox itemCB : actionMap.keySet())
			{
				aAttribute = actionMap.get(itemCB);
				itemCB.setSelected(aAttribute.isVisible);
			}
		}
		else if (source instanceof JCheckBox)
		{
			QueryAttribute aAttribute;
			JCheckBox tmpCB;
			
			tmpCB = (JCheckBox)source;
			aAttribute = actionMap.get(tmpCB);
			aAttribute.isVisible = tmpCB.isSelected();
			
			refItemHandler.rebuildTableColumns();
		}

		updateGui();
	}

	public void addConfig(ProfileConfig aConfig)
	{
		profileBox.addItem(aConfig);
	}

	/**
	 * Builds the main GUI area
	 */
	protected void buildGuiArea()
	{
		JPanel tmpPanel;
		Border border1, border2;

		// Form the grid bag constraints
		setLayout(new MigLayout("", "[left][grow]", "[]"));

		// Title Area
		titleL = new JLabel("Table Config", JLabel.CENTER);
		add(titleL, "growx,span 2,wrap");

		// Profile Area
		profileRB = new JRadioButton("Profile:");
		profileRB.addActionListener(this);
		add(profileRB, "span 1");

		profileBox = new GComboBox<ProfileConfig>();
		profileBox.addActionListener(this);
		add(profileBox, "growx,span 1,wrap");

		// Custom Area
		customRB = new JRadioButton("Custom:", true);
		customRB.addActionListener(this);
		add(customRB, "span 1,wrap");

		tmpPanel = buildQueryItemPanel();
		border1 = new EmptyBorder(0, 10, 0, 10);
		border2 = new BevelBorder(BevelBorder.RAISED);
		tmpPanel.setBorder(new CompoundBorder(border2, border1));
		tmpPanel.setEnabled(false);
		add(tmpPanel, "growx,span 2,wrap");

		// Link the radio buttons
		GuiUtil.linkRadioButtons(profileRB, customRB);

		// Build the default profile box
		ProfileConfig aConfig;
		aConfig = new ProfileConfig(AddProfilePanel.DEFAULT_NAME, refItemHandler.getSortedAttributes());
		addConfig(aConfig);

		// Action area
		closeB = GuiUtil.createJButton("Close", this);
		add(closeB, "align right,span 2");

		setBorder(new BevelBorder(BevelBorder.RAISED));
	}

	protected JPanel buildQueryItemPanel()
	{
		Collection<QueryAttribute> attrList;
		JPanel aPanel;
		JCheckBox tmpCB;

		aPanel = new JPanel();
		aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.Y_AXIS));

		attrList = refItemHandler.getSortedAttributes();
		for (QueryAttribute aAttr : attrList)
		{
			tmpCB = new JCheckBox(aAttr.label, aAttr.isVisible);
			tmpCB.addActionListener(this);
			aPanel.add(tmpCB);

			actionMap.put(tmpCB, aAttr);
		}

		return aPanel;
	}

	protected void updateGui()
	{
		boolean isEnabled;

		// Update the profile area
		isEnabled = profileRB.isSelected();
		profileBox.setEnabled(isEnabled);

		// Update the custom area
		isEnabled = customRB.isSelected();
		for (JCheckBox aItemCB : actionMap.keySet())
			aItemCB.setEnabled(isEnabled);
	}

}
