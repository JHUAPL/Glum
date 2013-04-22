package glum.gui.misc;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.border.*;

public class SimpleTitledBorder extends TitledBorder
{
	// Class var used to strip the disabled color
	private static JTextComponent testComponent = null;

	/**
	 * Constuctor
	 */
	public SimpleTitledBorder(Border border)
	{
		super(border);
	}

	public SimpleTitledBorder(Border border, String title)
	{
		super(border, title);
	}

	public SimpleTitledBorder(Border border, String title, int titleJustification, int titlePosition)
	{
		super(border, title, titleJustification, titlePosition);
	}

	public SimpleTitledBorder(Border border, String title, int titleJustification, int titlePosition, Font titleFont)
	{
		super(border, title, titleJustification, titlePosition, titleFont);
	}

	public SimpleTitledBorder(Border border, String title, int titleJustification, int titlePosition, Font titleFont, Color titleColor)
	{
		super(border, title, titleJustification, titlePosition, titleFont, titleColor);
	}

	public SimpleTitledBorder(String title)
	{
		super(title);
	}

	/**
	 * setEnabled
	 */
	public void setEnabled(boolean aBool)
	{
		if (testComponent == null)
			testComponent = new JTextField();

		// ! TODO: Color should be based on system settings
		setTitleColor(Color.BLACK);
		if (aBool == false)
			setTitleColor(testComponent.getDisabledTextColor());
	}

}
