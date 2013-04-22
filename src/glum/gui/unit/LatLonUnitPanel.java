package glum.gui.unit;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.*;

import net.miginfocom.swing.MigLayout;

import glum.gui.GuiUtil;
import glum.gui.component.GComboBox;
import glum.gui.component.GNumberField;
import glum.gui.panel.CardPanel;
import glum.unit.ConstUnitProvider;
import glum.unit.LatLonUnitProvider;
import glum.unit.NumberUnit;
import glum.unit.UnitProvider;

public class LatLonUnitPanel extends EditorPanel implements ActionListener
{
	// Gui components
	private GComboBox<String> unitBox;
	private CardPanel<JPanel> editPanel;
	private GNumberField decimalPlacesNF;
	private JCheckBox isZeroCenteredCB;
	private JCheckBox isSecondsShownCB;

	// State vars
	private LatLonUnitProvider myUnitProvider;

	public LatLonUnitPanel()
	{
		super();

		myUnitProvider = null;
		buildGuiArea();
		updateGui();
	}

	@Override
	public void actionPerformed(ActionEvent aEvent)
	{
		Object source;

		source = aEvent.getSource();
		if (source == unitBox)
			updateGui();

		updateEditable();
		notifyListeners(this, ID_UPDATE, "unit.update");
	}

	@Override
	public void setUnitProvider(UnitProvider aUnitProvider)
	{
		String unitTypeStr;
		int decimalPlaces;
		boolean isSecondsShown;
		boolean isZeroCentered;

		// Update our UnitProvider
		myUnitProvider = null;
		if (aUnitProvider instanceof LatLonUnitProvider)
			myUnitProvider = (LatLonUnitProvider)aUnitProvider;

		// Sync the GUI to the state of the UnitProvider
		decimalPlaces = decimalPlacesNF.getValueAsInt(0);
		isSecondsShown = isSecondsShownCB.isSelected();
		isZeroCentered = isZeroCenteredCB.isSelected();
		unitTypeStr = "Raw";
		if (myUnitProvider != null)
		{
			if (myUnitProvider.isRawUnitActive() == true)
			{
				decimalPlaces = myUnitProvider.getDecimalPlaces();
				isZeroCentered = myUnitProvider.isZeroCentered();
				unitTypeStr = "Raw";
			}
			else
			{
				isSecondsShown = myUnitProvider.isSecondsShown();
				unitTypeStr = "Standard";
			}
		}

		// Synch the GUI with the UnitProvider
		decimalPlacesNF.setValue(decimalPlaces);
		isSecondsShownCB.setSelected(isSecondsShown);
		isZeroCenteredCB.setSelected(isZeroCentered);

		unitBox.removeActionListener(this);
		unitBox.setSelectedItem(unitTypeStr);
		unitBox.addActionListener(this);

		updateGui();
	}

	/**
	 * Forms the GUI
	 */
	private void buildGuiArea()
	{
		JLabel unitL;

		setLayout(new MigLayout("", "0[right][][grow]0", "0[][grow]0"));

		// Unit area
		unitL = new JLabel("Unit:", JLabel.CENTER);
		unitBox = new GComboBox<String>(this, "Raw", "Standard");
		add("span 1", unitL);
		add("growx,span,wrap", unitBox);

		// Edit area
		editPanel = new CardPanel<JPanel>();
		editPanel.addCard("Raw", formRawPanel());
		editPanel.addCard("Standard", formStandardPanel());
		add("growx,growy,span", editPanel);
	}

	/**
	 * Forms the panel to configure the raw unit for lat/lon
	 */
	private JPanel formRawPanel()
	{
		JPanel tmpPanel;
		JLabel tmpL;
		UnitProvider countUP;

		tmpPanel = new JPanel(new MigLayout("", "0[right][][grow]0", "0[]0"));
		countUP = new ConstUnitProvider(new NumberUnit("", "", 1.0, new DecimalFormat("###,###,###,###,##0")));

		// DecimalPlaces area
		tmpL = new JLabel("Decimal Places:", JLabel.CENTER);
		decimalPlacesNF = new GNumberField(this, countUP, 0, 9);
		tmpPanel.add("span 2", tmpL);
		tmpPanel.add("growx,span 1,wrap", decimalPlacesNF);

		// Zero centered area
		isZeroCenteredCB = GuiUtil.createJCheckBox("Zero Centered", this);
		tmpPanel.add("align left,span", isZeroCenteredCB);

		return tmpPanel;
	}

	/**
	 * Forms the panel to configure the standard unit for lat/lon
	 */
	private JPanel formStandardPanel()
	{
		JPanel tmpPanel;

		tmpPanel = new JPanel(new MigLayout("", "0[grow]0", "0[]0"));

		isSecondsShownCB = GuiUtil.createJCheckBox("Show Seconds", this);
		tmpPanel.add("span 1", isSecondsShownCB);

		return tmpPanel;
	}

	/**
	 * Updates the GUI
	 */
	private void updateGui()
	{
		String unitTypeStr;

		unitTypeStr = unitBox.getChosenItem();
		editPanel.switchToCard(unitTypeStr);
	}

	/**
	 * Updates the UnitProvider
	 */
	private void updateEditable()
	{
		String unitTypeStr;
		boolean isSecondsShown;
		boolean isZeroCentered;
		int decimalPlaces;

		// Bail if no UnitProvider
		if (myUnitProvider == null)
			return;

		isSecondsShown = isSecondsShownCB.isSelected();
		isZeroCentered = isZeroCenteredCB.isSelected();
		decimalPlaces = decimalPlacesNF.getValueAsInt(0);

		unitTypeStr = unitBox.getChosenItem();
		if (unitTypeStr.equals("Raw") == true)
			myUnitProvider.activateRaw(decimalPlaces, isZeroCentered);
		else
			myUnitProvider.activateStandard(isSecondsShown);
	}

}
