package glum.gui.icon;

import java.awt.Component;
import java.awt.Graphics;

import javax.swing.Icon;

public class EmptyIcon implements Icon
{
	protected int width, height;
	
	EmptyIcon(int aWidth, int aHeight)
	{
		width = aWidth;
		height = aHeight;
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		; // Nothing to do
	}

	@Override
	public int getIconWidth()
	{
		return width;
	}

	@Override
	public int getIconHeight()
	{
		return height;
	}

}
