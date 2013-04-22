package glum.gui.misc;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class ColorCellRenderer extends JPanel implements TableCellRenderer
{
	// State vars
	protected Color activeColor;

	/**
	 * Constructor
	 */
	public ColorCellRenderer()
	{
		super();

		activeColor = null;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		activeColor = null;
		if (value instanceof Color)
			activeColor = (Color)value;

		if (activeColor != null)
			setBackground(activeColor);
		else
			setBackground(Color.LIGHT_GRAY);

		return this;
	}

	@Override
	public void paint(Graphics g)
	{
		Graphics2D g2d;

		super.paint(g);
		g2d = (Graphics2D)g;

		// Bail if we have a valid color
		if (activeColor != null)
			return;

		// Draw a red x if no valid color
		g2d.setColor(Color.RED);
		g2d.drawLine(0, 0, getWidth(), getHeight());
		g2d.drawLine(getWidth(), 0, 0, getHeight());
	}

}
