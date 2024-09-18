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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.List;

import javax.swing.JCheckBox;
import javax.swing.JLabel;

import com.google.common.collect.Range;

import glum.gui.GuiUtil;
import glum.gui.component.GComboBox;
import glum.gui.component.GNumberField;
import glum.unit.*;
import net.miginfocom.swing.MigLayout;

/**
 * User input component that allows the user to specify a {@link DecimalUnitProvider}.
 *
 * @author lopeznr1
 */
public class DecimalUnitPanel extends EditorPanel implements ActionListener
{
	// Gui components
	private GComboBox<Unit> unitBox;
	private GNumberField decimalPlacesNF;
	private JCheckBox forceLongUnitsCB;
	private JLabel unitL, decimalPlacesL;

	// State vars
	private DecimalUnitProvider myUnitProvider;

	/** Standard Constructor */
	public DecimalUnitPanel()
	{
		myUnitProvider = null;

		buildGuiArea();
		updateGui();
	}

	@Override
	public void actionPerformed(ActionEvent aEvent)
	{
		updateGui();
		updateModel();

		notifyListeners(this, ID_UPDATE, "unit.update");
	}

	@Override
	public void setUnitProvider(UnitProvider aUnitProvider)
	{
		// Update our UnitProvider
		myUnitProvider = null;
		if (aUnitProvider instanceof DecimalUnitProvider)
			myUnitProvider = (DecimalUnitProvider) aUnitProvider;

		// Sync the GUI to the state of the aEditable
		var decimalPlaces = 0;
		var forceFullLabel = false;
		List<Unit> unitL = null;
		Unit protoUnit = null;
		if (myUnitProvider != null)
		{
			decimalPlaces = myUnitProvider.getDecimalPlaces();
			forceFullLabel = myUnitProvider.getForceFullLabel();
			unitL = myUnitProvider.getProtoUnitList();
			protoUnit = myUnitProvider.getProtoUnit();
		}

		// Synch the GUI with the UnitProvider
		decimalPlacesNF.setValue(decimalPlaces);
		forceLongUnitsCB.setSelected(forceFullLabel);

		Unit chosenUnit = null;
		unitBox.removeAllItems();
		for (Unit aUnit : unitL)
		{
			unitBox.addItem(aUnit);
			if (aUnit == protoUnit)
				chosenUnit = aUnit;
		}

		unitBox.removeActionListener(this);
		unitBox.setSelectedItem(chosenUnit);
		unitBox.addActionListener(this);
		updateGui();
	}

	/**
	 * Forms the GUI
	 */
	private void buildGuiArea()
	{
		setLayout(new MigLayout("", "0[right][][grow]0", "0[][]0[]0"));
		var countUP = new ConstUnitProvider(new NumberUnit("", "", 1.0, new DecimalFormat("###,###,###,###,##0")));

		// Unit area
		unitL = new JLabel("Unit:");
		unitBox = new GComboBox<Unit>(this, new UnitLabelRenderer());
		add("span 1", unitL);
		add("growx,span,wrap", unitBox);

		// DecimalPlaces area
		var tmpRange = Range.closed(0.0, 9.0);
		decimalPlacesL = new JLabel("Decimal Places:");
		decimalPlacesNF = new GNumberField(this, countUP, tmpRange);
		add("span 2", decimalPlacesL);
		add("growx,span 1,wrap", decimalPlacesNF);

		// ForceFullLabel
		forceLongUnitsCB = GuiUtil.createJCheckBox("Force long units", this);
		add("align left,growx,span", forceLongUnitsCB);
	}

	/**
	 * Updates the gui
	 */
	private void updateGui()
	{
		// Need a valid UnitProvider
		if (myUnitProvider == null)
			return;

		// Synch the gui components
		var protoUnit = unitBox.getChosenItem();
		var isEnabled = (protoUnit instanceof NumberUnit);
		forceLongUnitsCB.setEnabled(isEnabled);
	}

	/**
	 * Updates the associated UnitProvider
	 */
	private void updateModel()
	{
		// Get the gui configuration
		var protoUnit = unitBox.getChosenItem();
		var decimalPlaces = decimalPlacesNF.getValueAsInt(0);
		var forceLongUnits = forceLongUnitsCB.isSelected();

		// Update the UnitProvider
		myUnitProvider.activate(protoUnit, decimalPlaces, forceLongUnits);
	}

}
