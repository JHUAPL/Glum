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

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.NumberFormat;

import javax.swing.JPanel;

import com.google.common.collect.Range;

import glum.gui.GuiUtil;
import net.miginfocom.swing.MigLayout;

/**
 * User interface component that combines a {@link GNumberField} and a {@link GSlider} into a single unified component.
 *
 * @author lopeznr1
 */
public class GNumberFieldSlider extends JPanel implements ActionListener
{
	// Ref vars
	private final ActionListener refLister;

	// Gui vars
	private GNumberField valueNF;
	private GSlider valueS;

	/**
	 * Standard Constructor
	 *
	 * @param aListener
	 *        An ActionListener that will be notified when ever the user makes any input changes.
	 * @param aFormat
	 *        NumberFormat used to transform the numerical value to a string.
	 * @param aMinMaxRange
	 *        The range of values to accept.
	 * @param aNumColumns
	 *        The number of columns associated with the {@link GNumberField}.
	 */
	public GNumberFieldSlider(ActionListener aListener, NumberFormat aFormat, Range<Double> aMinMaxRange,
			int aNumColumns)
	{
		refLister = aListener;

		buildGui(aFormat, aMinMaxRange);

		setNumColumns(aNumColumns);
		setNumSteps(100);
	}

	/**
	 * Simplified Constructor
	 *
	 * @param aListener
	 *        An ActionListener that will be notified when ever the user makes any input changes.
	 * @param aFormat
	 *        NumberFormat used to transform the numerical value to a string.
	 * @param aMinMaxRange
	 *        The range of values to accept.
	 */
	public GNumberFieldSlider(ActionListener aListener, NumberFormat aFormat, Range<Double> aMinMaxRange)
	{
		this(aListener, aFormat, aMinMaxRange, 4);
	}

	/**
	 * Returns whether the current input is valid
	 */
	public boolean isValidInput()
	{
		// Delegate
		return valueNF.isValidInput();
	}

	/**
	 * Returns the selected value.
	 */
	public double getValue()
	{
		return valueNF.getValue();
	}

	/**
	 * Returns the currently stored model value as an integer.
	 * <p>
	 * See {@link GNumberField#getValueAsInt(int)}
	 */
	public int getValueAsInt(int aErrorVal)
	{
		return valueNF.getValueAsInt(aErrorVal);
	}

	/**
	 * Returns true if the value is being actively adjusted.
	 * <p>
	 * See also {@link GSlider#getValueIsAdjusting()}
	 */
	public boolean getValueIsAdjusting()
	{
		// Delegate
		return valueS.getValueIsAdjusting();
	}

	/**
	 * Sets in the selected value. Note no events will be fired.
	 */
	public void setValue(double aVal)
	{
		valueNF.setValue(aVal);
		valueS.setModelValue(aVal);
	}

	/**
	 * Set the editable state of the UI component.
	 */
	public void setEditable(boolean aBool)
	{
		valueNF.setEditable(aBool);
		valueS.setEnabled(aBool);
	}

	/**
	 * Set the enable state of the UI component.
	 */
	@Override
	public void setEnabled(boolean aBool)
	{
		GuiUtil.setEnabled(this, aBool);
	}

	/**
	 * Sets in the steps to be an integral value.
	 * <p>
	 * The minVal and maxVal must be integers otherwise this method will throw an exception. The number of steps will be:
	 * (maxVal - minVal).
	 */
	public void setIntegralSteps()
	{
		var tmpMinMaxRange = valueNF.getMinMaxRange();
		double tmpMinVal = tmpMinMaxRange.lowerEndpoint();
		double tmpMaxVal = tmpMinMaxRange.upperEndpoint();

		int intMaxVal = (int) tmpMaxVal;
		int intMinVal = (int) tmpMinVal;

		var isIntegral = true;
		isIntegral &= tmpMinVal - intMinVal == 0;
		isIntegral &= tmpMaxVal - intMaxVal == 0;
		if (isIntegral == false)
			throw new RuntimeException("Min,Max values are not integral: [" + tmpMinVal + ", " + tmpMaxVal + "]");

		// Delegate
		int numSteps = intMaxVal - intMinVal;
		setNumSteps(numSteps);
	}

	/**
	 * Sets in the number of columns for the associated GTextField.
	 */
	public void setNumColumns(int aNumColumns)
	{
		valueNF.setColumns(aNumColumns);
	}

	/**
	 * Sets in the number of steps associated with the slider.
	 */
	public void setNumSteps(int aNumSteps)
	{
		valueS.setNumSteps(aNumSteps);
	}

	@Override
	public void actionPerformed(ActionEvent aEvent)
	{
		var source = aEvent.getSource();
		if (source == valueNF)
		{
			double tmpVal = valueNF.getValue();
			valueS.setModelValue(tmpVal);
		}
		else if (source == valueS)
		{
			double tmpVal = valueS.getModelValue();
			valueNF.setValue(tmpVal);
		}

		var tmpEvent = new ActionEvent(this, 0, "");
		refLister.actionPerformed(tmpEvent);
	}

	/**
	 * Helper method that builds the unified GUI
	 */
	private void buildGui(NumberFormat aNumberFormat, Range<Double> aMinMaxRange)
	{
		valueNF = new GNumberField(this, aNumberFormat, aMinMaxRange);
		valueS = new GSlider(this, aMinMaxRange);

		setLayout(new MigLayout("", "0[]0", "0[]0"));
		add(valueNF, "w 40:");
		add(valueS, "growx,pushx");
	}

}
