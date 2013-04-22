package glum.gui.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JSlider;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

public class GSlider extends JSlider implements ChangeListener
{
	private ActionListener myListener;
	private double minVal, maxVal, rngVal;
	private int maxSteps;

	public GSlider(ActionListener aListener, int aMaxSteps, double aMinVal, double aMaxVal)
	{
		super(0, aMaxSteps);
		addChangeListener(this);

		myListener = aListener;

		maxSteps = aMaxSteps;

		minVal = aMinVal;
		maxVal = aMaxVal;
		rngVal = maxVal - minVal;
	}

	public GSlider(ActionListener aListener, double aMinVal, double aMaxVal)
	{
		this(aListener, 1000, aMinVal, aMaxVal);
	}

	/**
	 * Returns the model value for which this slider is currently set to.
	 * <P>
	 * Use this method over {@link JSlider#getValue()}
	 */
	public double getModelValue()
	{
		double retVal;

		retVal = minVal + ((super.getValue() / (double)maxSteps) * rngVal);
		return retVal;
	}

	/**
	 * Takes in the model's minVal and maxVal range. The current chosen model value will be adjusted to be in the middle
	 * of the range.
	 */
	public void setModelRange(double aMinVal, double aMaxVal)
	{
		minVal = aMinVal;
		maxVal = aMaxVal;
		rngVal = maxVal - minVal;

		setModelValue(minVal + rngVal / 2);
	}

	/**
	 * Takes in a model value and will adjust the slider to display the value. Note this method will not trigger an
	 * ActionEvent.
	 * <P>
	 * Use this method over {@link JSlider#setValue}
	 */
	public void setModelValue(double aVal)
	{
		double guiVal;

		guiVal = ((aVal - minVal) / rngVal) * maxSteps;

		removeChangeListener(this);
		setValue((int)guiVal);
		addChangeListener(this);
	}

	@Override
	public void stateChanged(ChangeEvent aEvent)
	{
		notifyLisener();
	}

	/**
	 * Helper method to notify our listener
	 */
	private void notifyLisener()
	{
		myListener.actionPerformed(new ActionEvent(this, 0, "update"));
	}

}
