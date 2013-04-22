package glum.gui.misc;

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class BooleanCellRenderer extends JCheckBox implements TableCellRenderer
{
	public BooleanCellRenderer()
	{
		super("", false);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		if (value instanceof Boolean)
		{
			setSelected((Boolean)value);
			return this;
		}

		return null;
	}

}
