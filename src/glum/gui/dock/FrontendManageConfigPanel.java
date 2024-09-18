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
package glum.gui.dock;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import bibliothek.gui.DockFrontend;
import glum.database.QueryItem;
import glum.gui.FocusUtil;
import glum.gui.GuiUtil;
import glum.gui.action.ClickAction;
import glum.gui.panel.GlassPanel;
import glum.gui.panel.itemList.ItemListPanel;
import glum.gui.panel.itemList.StaticItemProcessor;
import glum.gui.panel.itemList.query.QueryComposer;
import glum.gui.panel.itemList.query.QueryItemHandler;
import net.miginfocom.swing.MigLayout;

/**
 * UI component that provides a listing of all docking configurations.
 *
 * @author lopeznr1
 */
public class FrontendManageConfigPanel extends GlassPanel implements ActionListener, ListSelectionListener
{
	// GUI vars
	private ItemListPanel<ConfigItem, LookUp> listPanel;
	private JButton deleteB, closeB;

	// State vars
	private DockFrontend refFrontend;
	private StaticItemProcessor<ConfigItem> myItemProcessor;

	/**
	 * Standard Constructor
	 *
	 * @param aFrontend
	 */
	public FrontendManageConfigPanel(Component aParent, DockFrontend aFrontend)
	{
		super(aParent);

		// State vars
		refFrontend = aFrontend;

		// Build the actual GUI
		buildGuiArea();
		setPreferredSize(new Dimension(250, 300));// TODO:getPreferredSize().height));

		// Set up keyboard short cuts
		FocusUtil.addAncestorKeyBinding(this, "ESCAPE", new ClickAction(closeB));
		FocusUtil.addAncestorKeyBinding(this, "ENTER", new ClickAction(closeB));
		FocusUtil.addFocusKeyBinding(listPanel.getTable(), "ENTER", new ClickAction(closeB));
	}

	@Override
	public void setVisible(boolean aBool)
	{
		resetGui();

		super.setVisible(aBool);
	}

	@Override
	public void actionPerformed(ActionEvent aEvent)
	{
		var chosenItem = listPanel.getSelectedItem();

		var source = aEvent.getSource();
		if (source == deleteB)
		{
			refFrontend.delete(chosenItem.getName());
			resetGui();
		}
		else if (source == closeB)
		{
			setVisible(false);
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent aEvent)
	{
		if (aEvent.getValueIsAdjusting() == true)
			return;

		updateGui();
	}

	/**
	 * buildGuiArea - Forms the actual dialog GUI
	 */
	private void buildGuiArea()
	{
		setLayout(new MigLayout("", "[grow][][]", "[][grow][]"));

		var tmpL = new JLabel("Select configuration:", JLabel.CENTER);
		add(tmpL, "growx,span 4,wrap");

		// Construct the actual table panel
		var tmpComposer = new QueryComposer<LookUp>();
		tmpComposer.addAttribute(LookUp.Name, String.class, "Configuration", null);

		myItemProcessor = new StaticItemProcessor<>();
		var tmpIH = new QueryItemHandler<ConfigItem, LookUp>();
		listPanel = new ItemListPanel<>(tmpIH, myItemProcessor, tmpComposer, true);
		listPanel.addListSelectionListener(this);
		add(listPanel, "growx,growy,span 4,wrap");

		// Control area
		deleteB = GuiUtil.createJButton("Delete", this);
		closeB = GuiUtil.createJButton("Close", this);

		add(deleteB, "skip 1");
		add(closeB, "");
	}

	/**
	 * Utility method to update item list
	 */
	private void resetGui()
	{
		var itemL = new ArrayList<ConfigItem>();
		var strL = refFrontend.getSettings();
		for (String aStr : strL)
		{
			// Add only non reserved items
			if (aStr.charAt(0) != '.')
				itemL.add(new ConfigItem(aStr));
		}

		myItemProcessor.setItems(itemL);

		// TODO: Should be able to just call updateGui but can not
		SwingUtilities.invokeLater(() -> updateGui());
	}

	/**
	 * Utility method to update the individual gui components
	 */
	private void updateGui()
	{
		var chosenItem = listPanel.getSelectedItem();
		if (chosenItem != null)
			refFrontend.load(chosenItem.getName());

		var isEnabled = chosenItem != null;
		deleteB.setEnabled(isEnabled);

		// Ensure we have the focus.
//listPanel.requestFocusInWindow();
		repaint();

		// TODO: Ugly code: Not sure why need to request focus multiple times to ensure we regrab
		// the focus. This is particularly true when there are multiple DockStations located on different
		// windows
		SwingUtilities.invokeLater(new Runnable() {

			@Override
			public void run()
			{
				listPanel.getTable().requestFocus();

				SwingUtilities.invokeLater(() -> listPanel.getTable().requestFocus());

//				listPanel.getTable().getFocusCycleRootAncestor().setFocusCycleRoot(true);
				listPanel.getTable().requestFocusInWindow();
				repaint();
			}
		});
	}

	/**
	 * Internal class only used to wrap named (string) settings into a QueryItem.
	 *
	 * @author lopeznr1
	 */
	class ConfigItem implements QueryItem<LookUp>
	{
		// Attributes
		private final String refName;

		public ConfigItem(String aName)
		{
			refName = aName;
		}

		/**
		 * Accessor method
		 */
		public String getName()
		{
			return refName;
		}

		@Override
		public Object getValue(LookUp aLookUp)
		{
			return refName;
		}

		@Override
		public void setValue(LookUp aLookUp, Object aObj)
		{
			throw new UnsupportedOperationException();
		}

	}

}
