package glum.gui.component;

import javax.swing.Icon;
import javax.swing.JToggleButton;

public class GToggle extends JToggleButton
{
	// State vars
	protected boolean isActive;

	// Gui vars
	protected Icon falseIcon, trueIcon;

	public GToggle(Icon aFalseIcon, Icon aTrueIcon, boolean aIsActive)
	{
		super();

		falseIcon = aFalseIcon;
		trueIcon = aTrueIcon;
		setSelected(aIsActive);

		setModel(new GToggleButtonModel());
	}

	@Override
	public void setSelected(boolean b)
	{
		super.setSelected(b);
		updateGui();
	}

	@Override
	public void doClick(int pressTime)
	{
		super.doClick(pressTime);
		updateGui();
	}

	/**
	 * Utility method
	 */
	private void updateGui()
	{
		if (isSelected() == true)
			setIcon(trueIcon);
		else
			setIcon(falseIcon);
	}

	/**
	 * The ToggleButton model
	 * <p>
	 * <strong>Warning:</strong> Serialized objects of this class will not be compatible with future Swing releases. The
	 * current serialization support is appropriate for short term storage or RMI between applications running the same
	 * version of Swing. As of 1.4, support for long term storage of all JavaBeans<sup><font size="-2">TM</font></sup>
	 * has been added to the <code>java.beans</code> package. Please see {@link java.beans.XMLEncoder}.
	 */
	public class GToggleButtonModel extends ToggleButtonModel
	{
		/**
		 * Creates a new ToggleButton Model
		 */
		public GToggleButtonModel()
		{
		}

		@Override
		public void setSelected(boolean b)
		{
			super.setSelected(b);

			updateGui();
		}
	}

}
