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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.*;

import glum.gui.FocusUtil;
import glum.gui.GuiUtil;
import glum.gui.action.ClickAction;
import glum.gui.component.GComboBox;
import glum.gui.panel.GlassPanel;
import glum.gui.panel.itemList.TableColumnHandler;
import glum.gui.panel.itemList.query.QueryAttribute;
import glum.gui.panel.itemList.query.QueryItemHandler;
import net.miginfocom.swing.MigLayout;

/**
 * UI component that allows for the configuration of ItemListPanels that utilize the {@link QueryAttribute} and
 * {@link QueryItemHandler} interfaces.
 * <p>
 * New code should make use of {@link EditTablePanel} rather than {@link EditTablePanelClassic}.
 *
 * @author lopeznr1
 */
public class EditTablePanelClassic<G2 extends Enum<?>> extends GlassPanel implements ActionListener
{
	// Attributes
	private final TableColumnHandler<G2> refTableColumnHandler;

	// GUI vars
	private JLabel titleL;
	private JRadioButton profileRB, customRB;
	private GComboBox<ProfileConfig<G2>> profileBox;
	private JButton closeB;

	// State vars
	private Map<JCheckBox, QueryAttribute<G2>> actionMap;

	/** Standard Constructor */
	public EditTablePanelClassic(Component aParent, TableColumnHandler<G2> aTableColumnHandler)
	{
		super(aParent);

		// State vars
		refTableColumnHandler = aTableColumnHandler;
		actionMap = new HashMap<>();

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
		var source = e.getSource();
		if (source == closeB)
		{
			setVisible(false);
			notifyListeners(this, 0, "Close");
		}
		else if (source == profileBox || source == profileRB)
		{
			var tmpConfig = profileBox.getChosenItem();
			refTableColumnHandler.setOrderAndConfig(tmpConfig.getItems());
			refTableColumnHandler.rebuildTableColumns();

			for (JCheckBox itemCB : actionMap.keySet())
			{
				var tmpAttribute = actionMap.get(itemCB);
				itemCB.setSelected(tmpAttribute.isVisible);
			}
		}
		else if (source instanceof JCheckBox)
		{
			var tmpCB = (JCheckBox) source;
			var tmpAttribute = actionMap.get(tmpCB);
			tmpAttribute.isVisible = tmpCB.isSelected();

			refTableColumnHandler.rebuildTableColumns();
		}

		updateGui();
	}

	public void addConfig(ProfileConfig<G2> aConfig)
	{
		profileBox.addItem(aConfig);
	}

	/**
	 * Builds the main GUI area
	 */
	protected void buildGuiArea()
	{
		// Form the grid bag constraints
		setLayout(new MigLayout("", "[left][grow]", "[]"));

		// Title Area
		titleL = new JLabel("Table Config", JLabel.CENTER);
		add(titleL, "growx,span 2,wrap");

		// Profile Area
		profileRB = new JRadioButton("Profile:");
		profileRB.addActionListener(this);
		add(profileRB, "span 1");

		profileBox = new GComboBox<>();
		profileBox.addActionListener(this);
		add(profileBox, "growx,span 1,wrap");

		// Custom Area
		customRB = new JRadioButton("Custom:", true);
		customRB.addActionListener(this);
		add(customRB, "span 1,wrap");

		var tmpPanel = buildQueryItemPanel();
		var border1 = new EmptyBorder(0, 10, 0, 10);
		var border2 = new BevelBorder(BevelBorder.RAISED);
		tmpPanel.setBorder(new CompoundBorder(border2, border1));
		tmpPanel.setEnabled(false);
		add(tmpPanel, "growx,span 2,wrap");

		// Link the radio buttons
		GuiUtil.linkRadioButtons(profileRB, customRB);

		// Build the default profile box
		var tmpConfig = new ProfileConfig<>(ProfileConfig.DEFAULT_NAME, refTableColumnHandler.getOrderedAttributes());
		addConfig(tmpConfig);

		// Action area
		closeB = GuiUtil.createJButton("Close", this);
		add(closeB, "ax right,span,split");
	}

	protected JPanel buildQueryItemPanel()
	{
		var retPanel = new JPanel();
		retPanel.setLayout(new BoxLayout(retPanel, BoxLayout.Y_AXIS));

		var tmpAttrL = refTableColumnHandler.getOrderedAttributes();
		for (QueryAttribute<G2> aAttr : tmpAttrL)
		{
			var tmpCB = new JCheckBox(aAttr.label, aAttr.isVisible);
			tmpCB.addActionListener(this);
			retPanel.add(tmpCB);

			actionMap.put(tmpCB, aAttr);
		}

		return retPanel;
	}

	protected void updateGui()
	{
		// Update the profile area
		var isEnabled = profileRB.isSelected();
		profileBox.setEnabled(isEnabled);

		// Update the custom area
		isEnabled = customRB.isSelected();
		for (JCheckBox aItemCB : actionMap.keySet())
			aItemCB.setEnabled(isEnabled);
	}

}
