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

import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.google.common.collect.Range;

/**
 * User interface input used to capture an individual numerical input (type: double).
 * <p>
 * Unlike JSlider, users of this class should not use getValue() / setValue() but rather getModelValue() /
 * setModelValue() methods. Also it should not be necessary to register ChangeListeners - rather an ActionListener
 * should be sufficient.
 *
 * @author lopeznr1
 */
public class GSlider extends JSlider implements ChangeListener
{
	// Attributes
	private final ActionListener refListener;

	// State vars
	private Range<Double> minMaxRange;
	private int maxSteps;

	/**
	 * Standard Constructor
	 *
	 * @param aListener
	 *        An ActionListener that will be notified when ever the user makes any input changes.
	 * @param aMinMaxRange
	 *        The range of values to accept.
	 * @param aMaxSteps
	 *        The number of steps associated with the slider.
	 */
	public GSlider(ActionListener aListener, Range<Double> aMinMaxRange, int aMaxSteps)
	{
		super(0, aMaxSteps);
		addChangeListener(this);

		refListener = aListener;

		minMaxRange = aMinMaxRange;
		maxSteps = aMaxSteps;
	}

	/**
	 * Simplified Constructor
	 *
	 * @param aListener
	 *        An ActionListener that will be notified when ever the user makes any input changes.
	 * @param aMinMaxRange
	 *        The range of values to accept.
	 */
	public GSlider(ActionListener aListener, Range<Double> aMinMaxRange)
	{
		this(aListener, aMinMaxRange, 1000);
	}

	/**
	 * Returns the model value for which this slider is currently set to.
	 * <p>
	 * Use this method over {@link JSlider#getValue()}
	 */
	public double getModelValue()
	{
		double minVal = minMaxRange.lowerEndpoint();
		double maxVal = minMaxRange.upperEndpoint();
		double rngVal = maxVal - minVal;

		double retVal = minVal + ((super.getValue() / (double) maxSteps) * rngVal);
		return retVal;
	}

	/**
	 * Takes in the model's minVal and maxVal range. The current chosen model value will be adjusted to be in the middle
	 * of the range.
	 */
	public void setModelRange(Range<Double> aMinMaxRange)
	{
		minMaxRange = aMinMaxRange;

		double minVal = minMaxRange.lowerEndpoint();
		double maxVal = minMaxRange.upperEndpoint();
		double rngVal = maxVal - minVal;

		setModelValue(minVal + rngVal / 2);
	}

	/**
	 * Takes in a model value and will adjust the slider to display the value. Note this method will not trigger an
	 * ActionEvent.
	 * <p>
	 * Use this method over {@link JSlider#setValue}
	 */
	public void setModelValue(double aVal)
	{
		double minVal = minMaxRange.lowerEndpoint();
		double maxVal = minMaxRange.upperEndpoint();
		double rngVal = maxVal - minVal;

		removeChangeListener(this);
		double guiVal = ((aVal - minVal) / rngVal) * maxSteps;
		setValue((int) guiVal);
		addChangeListener(this);
	}

	/**
	 * Sets in the number of steps associated with the GSlider.
	 * <p>
	 * Values will be uniformly distributed over the range / numSteps
	 *
	 * @param aNumSteps
	 *        The number of steps the slider should have.
	 */
	public void setNumSteps(int aNumSteps)
	{
		removeChangeListener(this);

		setMinimum(0);
		setMaximum(aNumSteps);
		maxSteps = aNumSteps;

		addChangeListener(this);
	}

	@Override
	public void stateChanged(ChangeEvent aEvent)
	{
		notifyLisener();
	}

	@Override
	@Deprecated
	public int getValue()
	{
		return super.getValue();
	}

	@Override
	@Deprecated
	public void setValue(int n)
	{
		super.setValue(n);
	}

	/**
	 * Helper method to notify our listener
	 */
	private void notifyLisener()
	{
		refListener.actionPerformed(new ActionEvent(this, 0, "update"));
	}

}
