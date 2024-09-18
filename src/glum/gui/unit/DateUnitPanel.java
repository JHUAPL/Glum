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
import java.awt.event.*;
import java.util.*;

import javax.swing.JLabel;
import javax.swing.JRadioButton;
import javax.swing.table.DefaultTableCellRenderer;

import glum.database.QueryItem;
import glum.gui.GuiUtil;
import glum.gui.component.*;
import glum.gui.document.CharDocument;
import glum.gui.panel.itemList.ItemListPanel;
import glum.gui.panel.itemList.StaticItemProcessor;
import glum.gui.panel.itemList.query.QueryComposer;
import glum.gui.panel.itemList.query.QueryItemHandler;
import glum.unit.*;
import net.miginfocom.swing.MigLayout;

/**
 * Panel that allows the user to view and configure a {@link DateUnitProvider}.
 *
 * @author lopeznr1
 */
public class DateUnitPanel extends EditorPanel implements ActionListener, ItemListener
{
	// Constants
	// @formatter:off
	public static final String[][] DescriptionArr = {
		{"G", "Era", "AD"},
		{"y", "Year", "1996"},
		{"M", "Month in year", "Jul; 07"},
		{"w", "Week in year", "27"},
		{"W", "Week in month", "2"},
		{"D", "Day in year", "189"},
		{"d", "Day in month", "10"},
		{"E", "Day in week", "Tue"},
		{"a", "AM/PM marker", "PM"},
		{"H", "Hour in day", "0"},
		{"h", "Hour in am/pm", "12"},
		{"m", "Minute in hour", "30"},
		{"s", "Second in minute", "55"},
		{"S", "Millisecond", "978"},
		{"z", "Time Zone: General", "Eastern; EST"},
		{"Z", "Time Zone: RFC 822", "-800"}};
	// @formatter:on

	// Gui components
	private JRadioButton custRB, typeRB;
	private GComboBox<String> protoBox;
	private GTextField custTF;
	private GLabel exampleL;
	private ItemListPanel<PlainRow, Lookup> ruleLP;
	private DefaultTableCellRenderer plainRenderer;
	private JLabel timeZoneL;
	private GComboBox<TimeZone> timeZoneBox;

	// State vars
	private DateUnitProvider myUnitProvider;

	/** Standard Constructor */
	public DateUnitPanel()
	{
		myUnitProvider = null;

		buildGuiArea();
		updateGui();
	}

	/**
	 * Sets in the available TimeZones the user can select
	 */
	public void setAvailableTimeZones(List<TimeZone> itemList)
	{
		timeZoneBox.removeAllItems();
		for (TimeZone aTimeZone : itemList)
			timeZoneBox.addItem(aTimeZone);
	}

	@Override
	public void actionPerformed(ActionEvent aEvent)
	{
		updateModel();
		updateGui();

		notifyListeners(this, ID_UPDATE, "unit.update");
	}

	@Override
	public void itemStateChanged(ItemEvent aEvent)
	{
		updateModel();
		updateGui();

		notifyListeners(this, ID_UPDATE, "unit.update");
	}

	@Override
	public void setUnitProvider(UnitProvider aUnitProvider)
	{
		// Update our UnitProvider
		myUnitProvider = null;
		if (aUnitProvider instanceof DateUnitProvider)
			myUnitProvider = (DateUnitProvider) aUnitProvider;

		// Sync the GUI to the state of the aEditable
		var protoNameL = myUnitProvider.getProtoNameList();

		var activeUnit = myUnitProvider.getUnit();
		var activeTimeZone = activeUnit.getTimeZone();

		// Synch the GUI with the UnitProvider
		custTF.setValue(myUnitProvider.getCustomPattern());

		protoBox.removeAllItems();
		for (String aName : protoNameL)
			protoBox.addItem(aName);

		protoBox.setChosenItem(myUnitProvider.getProtoUnit().getConfigName());

		if (myUnitProvider.getIsCustom() == true)
			custRB.setSelected(true);
		else
			typeRB.setSelected(true);

		for (TimeZone aTimeZone : timeZoneBox.getAllItems())
		{
			if (activeTimeZone != null && aTimeZone.getID() == activeTimeZone.getID())
			{
				timeZoneBox.setChosenItem(aTimeZone);
				break;
			}
		}

		updateGui();
	}

	/**
	 * Forms the GUI
	 */
	private void buildGuiArea()
	{
		setLayout(new MigLayout("", "0[left][grow]0", "0[][][][][][]0[grow]0"));

		// Example area
		exampleL = new GLabel(null);
		exampleL.setHorizontalAlignment(GLabel.CENTER);
		add("growx,h 18!,span,wrap", exampleL);

		add(GuiUtil.createDivider(), "growx,h 4!,span,wrap");

		// Specification area
		typeRB = GuiUtil.createJRadioButton(this, "Named:");
		protoBox = new GComboBox<String>(this);
		add("span 1", typeRB);
		add("growx,span,wrap", protoBox);

		// Custom area
		custRB = GuiUtil.createJRadioButton(this, "Custom:");
		custTF = new GTextField(this);
		var charDoc = new CharDocument(custTF, "GyMwWDdEaHhmsSzZ :|\\/-,[](){}<>;.", true);
		custTF.setDocument(charDoc);
		add("span 1", custRB);
		add("growx,span,wrap", custTF);

		// Link the radio buttons
		GuiUtil.linkRadioButtons(custRB, typeRB);

		// TimeZone area
		timeZoneL = new JLabel("TimeZone:");
		timeZoneBox = new GComboBox<TimeZone>(this, new TimeZoneCellRenderer());
		timeZoneBox.addItem(TimeZone.getDefault());
		for (String aID : TimeZone.getAvailableIDs())
			timeZoneBox.addItem(TimeZone.getTimeZone(aID));
		add("span 1", timeZoneL);
		add("growx,span,w 0:100:,wrap", timeZoneBox);

		// Rules table
		var tmpItemL = new ArrayList<PlainRow>();
		for (String[] aRow : DescriptionArr)
			tmpItemL.add(new PlainRow(aRow[0], aRow[1], aRow[2]));

		var tmpComposer = new QueryComposer<Lookup>();
		tmpComposer.addAttribute(Lookup.Key, String.class, "Key", "Key");
		tmpComposer.addAttribute(Lookup.Comp, String.class, "Date Comp.", "Time Zone: General");
		tmpComposer.addAttribute(Lookup.Example, String.class, "Example", "Eastern; EST");
		tmpComposer.getItem(Lookup.Example).maxSize = 5000;
		plainRenderer = new DefaultTableCellRenderer();
		for (Lookup aEnum : Lookup.values())
			tmpComposer.setRenderer(aEnum, plainRenderer);

		var tmpIH = new QueryItemHandler<PlainRow, Lookup>();
		var tmpIP = new StaticItemProcessor<>(tmpItemL);

		var targH = ((custTF.getPreferredSize().height - 2) * DescriptionArr.length) + 2;
		ruleLP = new ItemListPanel<>(tmpIH, tmpIP, tmpComposer, false);
		ruleLP.setPreferredSize(new Dimension(ruleLP.getPreferredSize().width, targH));
		ruleLP.setSortingEnabled(false);
		ruleLP.setEnabled(false);
		add("growx,h 70::,span", ruleLP);
	}

	/**
	 * Updates the UnitProvider to be synch with the GUI
	 */
	private void updateModel()
	{
		// Need a valid UnitProvider
		if (myUnitProvider == null)
			return;

		var timeZone = timeZoneBox.getChosenItem();
		if (typeRB.isSelected() == true)
			myUnitProvider.activateProto(timeZone, protoBox.getChosenItem());
		else
			myUnitProvider.activateCustom(timeZone, custTF.getText());
	}

	/**
	 * Updates the GUI
	 */
	private void updateGui()
	{
		var isEnabled = custRB.isSelected();
		custTF.setEnabled(isEnabled);
		protoBox.setEnabled(!isEnabled);
//		itemLP.repaint();

		// Retrieve the activeUnit
		var activeUnit = (DateUnit) null;
		if (myUnitProvider != null)
			activeUnit = myUnitProvider.getUnit();

		// Update the example area
		var currTime = System.currentTimeMillis();
		var exampleStr = "";
		if (activeUnit != null)
			exampleStr = activeUnit.getString(currTime);

		exampleL.setValue(exampleStr);
	}

	/**
	 * Helper classes to aide with setting up the info table
	 *
	 * @author lopeznr1
	 */
	enum Lookup
	{
		Key, Comp, Example
	};

	/**
	 * Helper classes to aide with setting up the info table
	 *
	 * @author lopeznr1
	 */
	class PlainRow implements QueryItem<Lookup>
	{
		private final String key, comp, example;

		public PlainRow(String aKey, String aComp, String aExample)
		{
			key = aKey;
			comp = aComp;
			example = aExample;
		}

		@Override
		public Object getValue(Lookup aEnum)
		{
			switch (aEnum)
			{
				case Key:
					return key;

				case Comp:
					return comp;

				case Example:
					return example;

				default:
					return null;
			}

		}

		@Override
		public void setValue(Lookup aEnum, Object aObj)
		{
			throw new UnsupportedOperationException();

		}
	}

}
