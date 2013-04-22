package glum.gui.component;

import java.awt.Color;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

import glum.gui.document.NumberDocument;
import glum.unit.ConstUnitProvider;
import glum.unit.NumberUnit;
import glum.unit.Unit;
import glum.unit.UnitListener;
import glum.unit.UnitProvider;

public class GNumberField extends JTextField implements DocumentListener, UnitListener
{
	// State vars
	protected UnitProvider refUnitProvider;
	protected double currValue, minValue, maxValue;
	protected boolean isMutating;

	// Gui vars
	protected Color failColor, passColor;
	protected NumberDocument myDocument;

	/**
	 * Constructor
	 * 
	 * @param aListener
	 *           : Default ActionListener
	 * @param aUnit
	 *           : Object used to format programatic entered values. Note aUnitProvider will also be used to determine if
	 *           Floating or only Integral input is allowed.
	 * @param inputType
	 *           : Type of input to accept (Integer, Double, etc...)
	 * @param aMinVal
	 *           : Minimum value to accept
	 * @param aMaxVal
	 *           : Maximum value to accept
	 */
	public GNumberField(ActionListener aListener, UnitProvider aUnitProvider, double aMinVal, double aMaxVal)
	{
		super("", 0);

		refUnitProvider = aUnitProvider;
		currValue = 0;
		minValue = aMinVal;
		maxValue = aMaxVal;
		isMutating = false;

		failColor = Color.RED.darker();
		passColor = getForeground();

		// Register the ActionListener
		if (aListener != null)
			addActionListener(aListener);

		// Form the appropriate Document and initialize
		myDocument = new NumberDocument(this, false);
		super.setDocument(myDocument);

		// Register for events of interest
		myDocument.addDocumentListener(this);
		refUnitProvider.addListener(this);

		// Install our unit into the Document. Note this should be done last as the method installUnit() assumes that
		// this GNumberField is already registered with myDocument (via the method call forceTF()).
		installUnit();
	}

	public GNumberField(ActionListener aListener, Unit aUnit, double aMinVal, double aMaxVal)
	{
		this(aListener, new ConstUnitProvider(aUnit), aMinVal, aMaxVal);
	}

	/**
	 * Returns whether the current input is valid
	 */
	public boolean isValidInput()
	{
		Unit aUnit;
		double modelVal;

		// Ensure we have valid input
		aUnit = refUnitProvider.getUnit();

		modelVal = aUnit.parseString(this.getText(), Double.NaN);
		if (Double.isNaN(modelVal) == true)
			return false;

		// Ensure the value is within range
		if (modelVal < minValue || modelVal > maxValue)
			return false;

		return true;
	}

	/**
	 * Sets the currently stored model value to Double.NaN. Also clears out the input area. This method will not trigger
	 * an ActionEvent.
	 */
	public void clearValue()
	{
		currValue = Double.NaN;

		myDocument.removeDocumentListener(this);
		setText("");
		setCaretPosition(0);
		myDocument.addDocumentListener(this);
	}

	/**
	 * Returns the currently stored model value
	 */
	public double getValue()
	{
		return currValue;
	}

	/**
	 * Returns the currently stored model value as an integer. If the modelValue is NaN, then errorVal will be returned.
	 * The values MaxInt, MinInt are returned for Infinity.
	 */
	public int getValueAsInt(int errorVal)
	{
		if (Double.isNaN(currValue) == true)
			return errorVal;

		return (int)currValue;
	}

	/**
	 * Takes in a model value and will display it with respect to the active unit. This method will not trigger an
	 * ActionEvent.
	 * <P>
	 * Note this method will do nothing if the UI is being "mutated" when this method is called.
	 */
	public void setValue(final double aValue)
	{
		// Bail if we are being mutated. The alternative is to throw an exception like:
		// throw new IllegalStateException("Attempt to mutate in notification");
		if (isMutating == true)
			return;

		// Simple edit if we are not currently being mutated
		forceTF(aValue);
		updateGui();
	}

	/**
	 * Changes the range of acceptable values (in model units). Note the current value will be force to fit this range.
	 */
	public void setMinMaxValue(double aMinValue, double aMaxValue)
	{
		Unit aUnit;

		minValue = aMinValue;
		maxValue = aMaxValue;
		if (currValue < minValue || currValue > maxValue)
			currValue = minValue;

		// Update our document
		aUnit = refUnitProvider.getUnit();
		myDocument.setMinMaxValue(aUnit.toUnit(minValue), aUnit.toUnit(maxValue));
	}

	@Override
	public void setDocument(Document aDoc)
	{
//		throw new UnsupportedOperationException();
		if (aDoc != null)
			aDoc.addDocumentListener(this);

		super.setDocument(aDoc);
	}

	@Override
	public void changedUpdate(DocumentEvent aEvent)
	{
		syncValue(aEvent);
	}

	@Override
	public void insertUpdate(DocumentEvent aEvent)
	{
		syncValue(aEvent);
	}

	@Override
	public void removeUpdate(DocumentEvent aEvent)
	{
		syncValue(aEvent);
	}

	@Override
	public void unitChanged(UnitProvider aProvider, String aKey)
	{
		installUnit();
	}

	/**
	 * Updates the internal model value and will update the display wrt to the active unit.
	 */
	protected void forceTF(double aValue)
	{
		Unit aUnit;
		String aStr;

		// Save off the new model value, and check the validity
		currValue = aValue;
		if (currValue < minValue || currValue > maxValue)
			currValue = Double.NaN;
//			throw new RuntimeException("Programatic input is invalid. Is unit compatible? Input: " + aValue);

		// Invalid values shall just clear the text field and bail
		if (Double.isNaN(currValue) == true)
		{
			clearValue();
			return;
		}

		// Convert from model value to (unit) textual format
		aUnit = refUnitProvider.getUnit();
		aStr = aUnit.getString(currValue);

		// Update the GUI internals
		myDocument.removeDocumentListener(this);
		setText(aStr);
		setCaretPosition(0);
		myDocument.addDocumentListener(this);
	}

	/**
	 * Helper method to update the associated Document whenever a Unit is changed
	 */
	protected void installUnit()
	{
		Unit aUnit;
		boolean aBool;

		// Ensure that we have a valid Unit
		aUnit = refUnitProvider.getUnit();
		if (aUnit instanceof NumberUnit == false)
			throw new RuntimeException("refUnitProvider must return a Unit of type NumberUnit. Unit: " + aUnit);

		// Update our Document to reflect whether this Unit supports floating point numbers
		aBool = (aUnit instanceof NumberUnit) && (((NumberUnit)aUnit).isFloating() == true);
		myDocument.setAllowFloats(aBool);

		// Update the Document's MinMax values reflect the new Unit
		setMinMaxValue(minValue, maxValue);

		// Force myDocument's text to match the new unit
		forceTF(currValue);
	}

	/**
	 * Keeps the "model" value conceptually linked to the GUI component. It will also trigger the actionEventListeners.
	 */
	protected void syncValue(DocumentEvent e)
	{
		Unit aUnit;

		// Mark ourself as mutating
		isMutating = true;

		// Convert the textual (unit) value to the model value
		aUnit = refUnitProvider.getUnit();
		currValue = aUnit.parseString(this.getText(), Double.NaN);

		// If the value is not in range then, it is invalid
		if (currValue < minValue || currValue > maxValue)
			currValue = Double.NaN;

		// Notify our listeners and update the GUI
		updateGui();
		fireActionPerformed();

		// We are no longer mutating
		isMutating = false;
	}

	/**
	 * Helper method to update the GUI to reflect the current state of the NumberField
	 */
	protected void updateGui()
	{
		Color aColor;

		aColor = passColor;
		if (isValidInput() == false)
			aColor = failColor;

		setForeground(aColor);
	}

}
