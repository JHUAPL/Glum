package glum.gui.unit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.util.List;
import javax.swing.*;

import glum.gui.GuiUtil;
import glum.gui.component.GComboBox;
import glum.gui.component.GNumberField;
import glum.unit.ConstUnitProvider;
import glum.unit.DecimalUnitProvider;
import glum.unit.NumberUnit;
import glum.unit.Unit;
import glum.unit.UnitProvider;

import net.miginfocom.swing.MigLayout;

public class DecimalUnitPanel extends EditorPanel implements ActionListener
{
	// Gui components
	private GComboBox<Unit> unitBox;
	private GNumberField decimalPlacesNF;
	private JCheckBox forceLongUnitsCB;
	private JLabel unitL, decimalPlacesL;

	// State vars
	private DecimalUnitProvider myUnitProvider;

	public DecimalUnitPanel()
	{
		super();

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
		int decimalPlaces;
		boolean forceFullLabel;
		List<Unit> unitList;
		Unit protoUnit, chosenUnit;

		// Update our UnitProvider
		myUnitProvider = null;
		if (aUnitProvider instanceof DecimalUnitProvider)
			myUnitProvider = (DecimalUnitProvider)aUnitProvider;

		// Sync the GUI to the state of the aEditable
		decimalPlaces = 0;
		forceFullLabel = false;
		unitList = null;
		protoUnit = null;
		if (myUnitProvider != null)
		{
			decimalPlaces = myUnitProvider.getDecimalPlaces();
			forceFullLabel = myUnitProvider.getForceFullLabel();
			unitList = myUnitProvider.getProtoUnitList();
			protoUnit = myUnitProvider.getProtoUnit();
		}

		// Synch the GUI with the UnitProvider
		decimalPlacesNF.setValue(decimalPlaces);
		forceLongUnitsCB.setSelected(forceFullLabel);

		chosenUnit = null;
		unitBox.removeAllItems();
		for (Unit aUnit : unitList)
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
		UnitProvider countUP;

		setLayout(new MigLayout("", "0[right][][grow]0", "0[][]0[]0"));
		countUP = new ConstUnitProvider(new NumberUnit("", "", 1.0, new DecimalFormat("###,###,###,###,##0")));

		// Unit area
		unitL = new JLabel("Unit:");
		unitBox = new GComboBox<Unit>(this, new UnitLabelRenderer());
		add("span 1", unitL);
		add("growx,span,wrap", unitBox);

		// DecimalPlaces area
		decimalPlacesL = new JLabel("Decimal Places:");
		decimalPlacesNF = new GNumberField(this, countUP, 0, 9);
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
		Unit protoUnit;
		boolean isEnabled;

		// Need a valid UnitProvider
		if (myUnitProvider == null)
			return;

		// Synch the gui components
		protoUnit = unitBox.getChosenItem();
		isEnabled = (protoUnit instanceof NumberUnit);
		forceLongUnitsCB.setEnabled(isEnabled);
	}

	/**
	 * Updates the associated UnitProvider
	 */
	private void updateModel()
	{
		Unit protoUnit;
		int decimalPlaces;
		boolean forceLongUnits;

		// Get the gui configuration
		protoUnit = unitBox.getChosenItem();
		decimalPlaces = decimalPlacesNF.getValueAsInt(0);
		forceLongUnits = forceLongUnitsCB.isSelected();

		// Update the UnitProvider
		myUnitProvider.activate(protoUnit, decimalPlaces, forceLongUnits);
	}

}
