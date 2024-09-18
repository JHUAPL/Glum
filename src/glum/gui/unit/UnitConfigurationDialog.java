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
package glum.gui.unit;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import glum.gui.FocusUtil;
import glum.gui.GuiUtil;
import glum.gui.action.ClickAction;
import glum.gui.panel.CardPanel;
import glum.gui.panel.itemList.*;
import glum.gui.panel.itemList.query.QueryComposer;
import glum.unit.UnitListener;
import glum.unit.UnitProvider;
import net.miginfocom.swing.MigLayout;

/**
 * JDialog that allows for the configuration of UnitProviders.
 *
 * @author lopeznr1
 */
public class UnitConfigurationDialog extends JDialog implements ActionListener, ListSelectionListener, UnitListener
{
	// Gui Components
	private ItemListPanel<UnitProvider, LookUp> itemLP;
	private CardPanel<EditorPanel> editorPanel;
	private JLabel titleL;
	private JButton closeB;

	// State vars
	private StaticItemProcessor<UnitProvider> itemProcessor;

	/** Standard Constructor */
	public UnitConfigurationDialog(JFrame aParentFrame)
	{
		// Make sure we call the parent
		super(aParentFrame);

		// Set the characteristics for this dialog
		setTitle("Edit Unit");

		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setLocationRelativeTo(aParentFrame);
		setModal(false);

		// Build the actual GUI
		buildGuiArea();
		updateGui();
		pack();

		// Set up keyboard short cuts
		FocusUtil.addWindowKeyBinding(getRootPane(), "ESCAPE", new ClickAction(closeB));
		FocusUtil.addWindowKeyBinding(getRootPane(), "ENTER", new ClickAction(closeB));
//		FocusUtil.addFocusKeyBinding(itemLP.getTable(), "ENTER", new ClickAction(closeB));
	}

	/**
	 * Adds an EditorPanel and associateds with aClass
	 */
	public void addEditorPanel(Class<?> aClass, EditorPanel aPanel)
	{
		// Insanity check
		if (aClass == null || aPanel == null)
			return;

		// Add the editorPanel and associate with the specified class
		editorPanel.addCard("" + aClass, aPanel);
		editorPanel.switchToCard("null");
		updateGui();

		var tmpDim = editorPanel.getMinimumSize();
		for (EditorPanel evalPanel : editorPanel.getAllCards())
		{
			if (evalPanel.getMinimumSize().height > tmpDim.height)
				tmpDim.height = evalPanel.getMinimumSize().height;
		}

		// Set the dialog to the best initial size
		setSize(getPreferredSize().width, itemLP.getHeight() + closeB.getHeight() + titleL.getHeight() + tmpDim.height);
	}

	/**
	 * Adds a UnitProvider to our set of UnitProviders
	 */
	public void addUnitProvider(UnitProvider aUnitProvider)
	{
		// Insanity check
		if (aUnitProvider == null)
			return;

		// Register for state changes on the UnitProvider
		aUnitProvider.addListener(this);

		// Update the table processor
		var itemL = new ArrayList<>(itemProcessor.getAllItems());
		itemL.add(aUnitProvider);
		itemProcessor.setItems(itemL);

		// Update the dialog
		updateTable();
	}

	@Override
	public void actionPerformed(ActionEvent aEvent)
	{
		var source = aEvent.getSource();
		if (source == closeB)
		{
			setVisible(false);
		}
	}

	@Override
	public void unitChanged(UnitProvider aProvider, String aKey)
	{
		itemLP.repaint();
	}

	@Override
	public void valueChanged(ListSelectionEvent aEvent)
	{
		var source = aEvent.getSource();
		if (source == itemLP)
		{
			if (aEvent.getValueIsAdjusting() == true)
				return;

			updateGui();
		}
	}

	/**
	 * Form the actual GUI
	 */
	private void buildGuiArea()
	{
		setLayout(new MigLayout("", "[center,grow]", "2[]3[grow][fill,growprio 200][]"));

		// Build the title label
		titleL = new JLabel("Units", JLabel.CENTER);
		add("span,wrap", titleL);

		// UnitProvider table
		var tmpComposer = new QueryComposer<LookUp>();
		tmpComposer.addAttribute(LookUp.Key, String.class, "Type", null);
		tmpComposer.addAttribute(LookUp.Value, String.class, "Value", null);

		var tmpIH = new UnitProviderHandler();
		itemProcessor = new StaticItemProcessor<>();

		itemLP = new ItemListPanel<>(tmpIH, itemProcessor, tmpComposer, false);
		itemLP.addListSelectionListener(this);
		itemLP.setSortingEnabled(false);
		add("growx,growy,h 70::,span,wrap", itemLP);

		// Form the editor area
		var nullPanel = new EditorPanel();
		nullPanel.setPreferredSize(new Dimension(1, 1));

		editorPanel = new CardPanel<EditorPanel>();
		editorPanel.addCard("null", nullPanel);
		add("growx,growy,wrap", editorPanel);

		// Action area
		closeB = GuiUtil.createJButton("Close", this);
		add("align right", closeB);
	}

	/**
	 * updateGui
	 */
	private void updateGui()
	{
		// Get the selected UnitProvider
		var tmpUnitProvider = itemLP.getSelectedItem();

		// Switch to the appropriate Editor
		var cardName = "null";
		if (tmpUnitProvider != null)
			cardName = "" + tmpUnitProvider.getClass();
		editorPanel.switchToCard(cardName);

		// Resize the editorPanel to be as compact as the active card
		var tmpPanel = editorPanel.getActiveCard();
		tmpPanel.setUnitProvider(tmpUnitProvider);
		var tmpDim = tmpPanel.getPreferredSize();
//		System.out.println("minHeight: " + aDim.getHeight() + "   hmm: " + aPanel);

		editorPanel.setMaximumSize(new Dimension(5000, tmpDim.height));
		// Hack to get the editorPanel resize properly. Not sure why invalidate(),
		// validate() do not work
		int aHeight = getHeight();
		SwingUtilities.invokeLater(() -> setSize(getWidth(), aHeight - 1));
		SwingUtilities.invokeLater(() -> setSize(getWidth(), aHeight));
//		SwingUtilities.invokeLater(() -> editorPanel.invalidate());
//		SwingUtilities.invokeLater(() -> editorPanel.validate());
//		invalidate();
//		validate();
	}

	/**
	 * updateTable
	 */
	private void updateTable()
	{
		// Update myTable column[0] width
		var aWidth = 10;
		var tmpLabel = new JLabel("");
		var itemL = itemProcessor.getAllItems();
		for (UnitProvider aUnitProvider : itemL)
		{
			tmpLabel.setText(aUnitProvider.getDisplayName());
			var tmpWidth = tmpLabel.getPreferredSize().width + 5;
			if (aWidth < tmpWidth)
				aWidth = tmpWidth;
		}

		// Set sizing attributes of the column 1
		var tmpTableHeader = itemLP.getTable().getTableHeader();
		var tmpTableColumnModel = tmpTableHeader.getColumnModel();
		var tmpTableColumn = tmpTableColumnModel.getColumn(0);
		tmpTableColumn.setResizable(false);
		tmpTableColumn.setMinWidth(aWidth);
		tmpTableColumn.setMaxWidth(aWidth);
		tmpTableColumn.setPreferredWidth(aWidth);
	}

	/**
	 * Enum which defines the types used to show a listing of configurable units.
	 *
	 * @author lopeznr1
	 */
	enum LookUp
	{
		Key,

		Value,
	};

	/**
	 * Implementation of {@link ItemHandler} that allows for tabular access of {@link UnitProvider}s.
	 *
	 * @author lopeznr1
	 */
	public class UnitProviderHandler implements ItemHandler<UnitProvider, LookUp>
	{
		@Override
		public Object getValue(UnitProvider aItem, LookUp aEnum)
		{
			switch (aEnum)
			{
				case Key:
					return aItem.getDisplayName();

				case Value:
					return getConfigName(aItem);

				default:
					break;
			}

			throw new RuntimeException("Unsupported enum:" + aEnum);
		}

		@Override
		public void setValue(UnitProvider aItem, LookUp aEnum, Object aValue)
		{
			throw new RuntimeException("Unsupported enum:" + aEnum);
		}

		/**
		 * Helper method that returns the configuration name to utilize.
		 */
		public String getConfigName(UnitProvider aUnitProvider)
		{
			var tmpUnit = aUnitProvider.getUnit();
			if (tmpUnit == null)
				return "None";

			return tmpUnit.getConfigName();
		}

	}

}
