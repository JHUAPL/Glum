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
package glum.gui.component;

import java.awt.Color;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;

import com.google.common.collect.Range;

import glum.gui.document.NumberDocument;
import glum.unit.*;

/**
 * User interface input used to capture an individual numerical input (type: double).
 * <p>
 * Unlike JTextField, users of this class should not use getText() / setText() but rather getValue() / setValue()
 * methods. Also it should not be necessary to register DocumentListeners - rather an ActionListener should be
 * sufficient.
 * <p>
 * This class provides two modes of converting model values to textual input:
 * <ul>
 * <li>{@link NumberFormat} mechanism
 * <li>{@link UnitProvider} mechanism
 * </ul>
 *
 * @author lopeznr1
 */
public class GNumberField extends GBaseTextField implements DocumentListener, UnitListener
{
	// Attributes
	private final NumberFormat refFormat;
	private final UnitProvider refUnitProvider;

	// State vars
	private Range<Double> minMaxRange;
	private double currValue;
	private boolean isMutating;

	// Gui vars
	private Color colorFail, colorPass;
	private NumberDocument myDocument;

	/**
	 * Standard Constructor
	 *
	 * @param aListener
	 *        An ActionListener that will be notified when ever the user makes any input changes.
	 * @param aUnit
	 *        Object used to format programmatic entered values. Note aUnitProvider will also be used to determine if
	 *        Floating or only Integral input is allowed.
	 * @param aMinMaxRange
	 *        The range of values to accept.
	 */
	public GNumberField(ActionListener aListener, UnitProvider aUnitProvider, Range<Double> aMinMaxRange)
	{
		super("", 0);

		refFormat = null;
		refUnitProvider = aUnitProvider;

		minMaxRange = aMinMaxRange;
		currValue = Double.NaN;
		isMutating = false;

		colorFail = Color.RED.darker();
		colorPass = getForeground();

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

	/**
	 * Standard Constructor
	 *
	 * @param aListener
	 *        An ActionListener that will be notified when ever the user makes any input changes.
	 * @param aFormat
	 *        NumberFormat used to transform the numerical value to a string.
	 * @param aMinMaxRange
	 *        The range of values to accept.
	 */
	public GNumberField(ActionListener aListener, NumberFormat aFormat, Range<Double> aMinMaxRange)
	{
		super("", 0);

		refFormat = aFormat;
		refUnitProvider = null;

		minMaxRange = aMinMaxRange;
		currValue = Double.NaN;
		isMutating = false;

		colorFail = Color.RED.darker();
		colorPass = getForeground();

		// Form the appropriate Document and initialize
		myDocument = new NumberDocument(this, false);
		super.setDocument(myDocument);

		// Register the ActionListener
		if (aListener != null)
			addActionListener(aListener);

		// Register for events of interest
		myDocument.addDocumentListener(this);

		// Force the UI component to reflect the currValue. Note this is done last since this method
		// assumes the GNumberField is already registered with myDocument.
		forceTF(currValue);
	}

	/**
	 * Simplified Constructor
	 *
	 * @param aListener
	 *        An ActionListener that will be notified when ever the user makes any input changes.
	 * @param aUnit
	 * @param aMinMaxRange
	 *        The range of values to accept.
	 */
	public GNumberField(ActionListener aListener, Unit aUnit, Range<Double> aMinMaxRange)
	{
		this(aListener, new ConstUnitProvider(aUnit), aMinMaxRange);
	}

	/**
	 * Simplified Constructor
	 *
	 * @param aListener
	 *        An ActionListener that will be notified when ever the user makes any input changes.
	 * @param aFormat
	 *        NumberFormat used to transform the numerical value to a string.
	 */
	public GNumberField(ActionListener aListener, NumberFormat aFormat)
	{
		this(aListener, aFormat, Range.closed(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
	}

	/**
	 * Simplified Constructor
	 *
	 * @param aListener
	 *        An ActionListener that will be notified when ever the user makes any input changes.
	 */
	public GNumberField(ActionListener aListener)
	{
		this(aListener, new DecimalFormat("#.###"), Range.closed(Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY));
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
	 * Returns the {@link Color} used to change foreground text whenever invalid input is entered.
	 */
	public Color getColorFail()
	{
		return colorFail;
	}

	/**
	 * Returns the {@link Color} used to change foreground text whenever valid input is entered.
	 */
	public Color getColorPass()
	{
		return colorPass;
	}

	/**
	 * Returns the range of valid values.
	 */
	public Range<Double> getMinMaxRange()
	{
		return minMaxRange;
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
	 * The values MAX_VALUE will be returned for +Infinity. The value MIN_VALUE will be returned for -Infinity.
	 */
	public long getValueAsLong(long aErrorVal)
	{
		if (Double.isNaN(currValue) == true)
			return aErrorVal;

		return (long) currValue;
	}

	/**
	 * Returns the currently stored model value as an integer. If the modelValue is NaN, then errorVal will be returned.
	 * The values MAX_VALUE will be returned for +Infinity. The value MIN_VALUE will be returned for -Infinity.
	 */
	public int getValueAsInt(int aErrorVal)
	{
		if (Double.isNaN(currValue) == true)
			return aErrorVal;

		return (int) currValue;
	}

	/**
	 * Returns whether the current input is valid
	 */
	public boolean isValidInput()
	{
		// Ensure we have valid input
		double modelVal = transformToModel(this.getText());
		if (Double.isNaN(modelVal) == true)
			return false;

		// Ensure the value is within range
		if (minMaxRange.contains(modelVal) == false)
			return false;

		return true;
	}

	/**
	 * Sets the {@link Color} used to indicate invalid input is entered.
	 */
	public void setColorFail(Color aColor)
	{
		colorFail = aColor;
	}

	/**
	 * Sets the {@link Color} used to indicate valid input is entered.
	 */
	public void setColorPass(Color aColor)
	{
		colorPass = aColor;
	}

	/**
	 * Takes in a model value and will display it with respect to the active unit. This method will not trigger an
	 * ActionEvent.
	 * <p>
	 * Note this method will do nothing if the UI is being "mutated" when this method is called.
	 */
	public void setValue(final double aValue)
	{
		// Bail if we are being mutated. The alternative is to throw an exception like:
		// throw new IllegalStateException("Attempt to mutate in notification");
		if (isMutating == true)
			return;

		// Bail if the value has not changed. We do this so that user
		// entered input will not change if the model value has not changed.
		double ulp = Math.ulp(aValue);
		boolean ignoreInput = true;
		ignoreInput &= Double.isNaN(ulp) == false;
		ignoreInput &= Double.isFinite(ulp) == true;
		ignoreInput &= Math.abs(currValue - aValue) < ulp;
		if (ignoreInput == true)
			return;

		// Simple edit if we are not currently being mutated
		forceTF(aValue);
		updateGui();
	}

	/**
	 * Changes the range of acceptable values (in model units). Note the current value will be force to fit this range.
	 */
	public void setMinMaxRange(Range<Double> aMinMaxRange)
	{
		minMaxRange = aMinMaxRange;
		if (minMaxRange.hasLowerBound() == true && currValue < minMaxRange.lowerEndpoint())
			currValue = minMaxRange.lowerEndpoint();
		else if (minMaxRange.hasUpperBound() == true && currValue > minMaxRange.upperEndpoint())
			currValue = minMaxRange.upperEndpoint();

		// Update our document
		double minValue = minMaxRange.lowerEndpoint();
		double maxValue = minMaxRange.upperEndpoint();
		if (refUnitProvider != null)
		{
			Unit tmpUnit = refUnitProvider.getUnit();
			myDocument.setMinMaxValue(tmpUnit.toUnit(minValue), tmpUnit.toUnit(maxValue));
		}
		else
		{
			myDocument.setMinMaxValue(minValue, maxValue);
		}
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
	public void setDocument(Document aDoc)
	{
//		throw new UnsupportedOperationException();
		if (aDoc != null)
			aDoc.addDocumentListener(this);

		super.setDocument(aDoc);
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
		// Save off the new model value, and check the validity
		currValue = aValue;
		if (minMaxRange.contains(currValue) == false)
			currValue = Double.NaN;
//			throw new RuntimeException("Programmatic input is invalid. Is unit compatible? Input: " + aValue);

		// Invalid values shall just clear the text field and bail
		if (Double.isNaN(currValue) == true)
		{
			clearValue();
			return;
		}

		// Convert from model value to text
		String tmpStr = transformToString(currValue);

		// Update the GUI internals
		myDocument.removeDocumentListener(this);
		setText(tmpStr);
		setCaretPosition(0);
		myDocument.addDocumentListener(this);
	}

	/**
	 * Helper method to update the associated Document whenever a Unit is changed
	 */
	protected void installUnit()
	{
		// Ensure that we have a valid Unit
		Unit tmpUnit = refUnitProvider.getUnit();
		if (tmpUnit instanceof NumberUnit == false)
			throw new RuntimeException("refUnitProvider must return a Unit of type NumberUnit. Unit: " + tmpUnit);

		// Update our Document to reflect whether this Unit supports floating point numbers
		boolean tmpBool = (tmpUnit instanceof NumberUnit) && (((NumberUnit) tmpUnit).isFloating() == true);
		myDocument.setAllowFloats(tmpBool);

		// Update the Document's MinMax values reflect the new Unit
		setMinMaxRange(minMaxRange);

		// Force myDocument's text to match the new unit
		forceTF(currValue);
	}

	/**
	 * Keeps the "model" value conceptually linked to the GUI component. It will also trigger the actionEventListeners.
	 */
	protected void syncValue(DocumentEvent aEvent)
	{
		// Mark ourself as mutating
		isMutating = true;

		// Convert the string to the model value
		currValue = transformToModel(this.getText());

		// If the value is not in range then, it is invalid
		if (minMaxRange.contains(currValue) == false)
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
		Color tmpColor = colorPass;
		if (isValidInput() == false)
			tmpColor = colorFail;

		setForeground(tmpColor);
	}

	/**
	 * Helper method that will take a given value and convert it to a string.
	 *
	 * @param aValue
	 */
	private String transformToString(double aValue)
	{
		// Convert from model value to (unit) textual format
		if (refUnitProvider != null)
		{
			Unit tmpUnit = refUnitProvider.getUnit();
			String tmpStr = tmpUnit.getString(aValue);
			return tmpStr;
		}

		return refFormat.format(aValue);
	}

	/**
	 * Helper method that will take a String and convert it to the equivalent numerical value. On failure Double.NaN will
	 * be returned.
	 *
	 * @param aValue
	 */
	private double transformToModel(String aStr)
	{
		// Convert the textual (unit) value to the model value
		if (refUnitProvider != null)
		{
			Unit tmpUnit = refUnitProvider.getUnit();
			double retValue = tmpUnit.parseString(aStr, Double.NaN);
			return retValue;
		}

		try
		{
			return Double.parseDouble(aStr);
		}
		catch (NumberFormatException aExp)
		{
			return Double.NaN;
		}
	}

}
