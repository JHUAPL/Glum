package glum.gui.panel;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

public class ColorPanel extends JPanel
{
	// State vars
	protected Color dispColor;

	public ColorPanel()
	{
		super();

		dispColor = Color.BLACK;
		updateGui();
	}

	public ColorPanel(int sizeX, int sizeY)
	{
		this();

		setMinimumSize(new Dimension(sizeX, sizeY));
		setPreferredSize(new Dimension(sizeX, sizeY));
	}

	/**
	 * Sets in the color that is displayed by this component
	 */
	public void setColor(Color aColor)
	{
		dispColor = aColor;
		updateGui();
	}

	@Override
	public void setEnabled(boolean aBool)
	{
		super.setEnabled(aBool);
		updateGui();
	}

	/**
	 * Updates the GUI to reflect the chosen color
	 */
	protected void updateGui()
	{
		boolean isEnabled;

		isEnabled = isEnabled();
		if (isEnabled == false)
		{
			setBackground(Color.LIGHT_GRAY);
			setBorder(new LineBorder(Color.GRAY));
			return;
		}

		setBackground(dispColor);
		setBorder(new BevelBorder(BevelBorder.RAISED));
	}

}
