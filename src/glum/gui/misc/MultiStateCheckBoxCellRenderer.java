package glum.gui.misc;

import java.awt.Component;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class MultiStateCheckBoxCellRenderer extends MultiStateCheckBox implements TableCellRenderer
{
	// State vars
	protected MultiStateCheckBox refMultiStateCheckBox;

	/**
	 * Constructor
	 */
	public MultiStateCheckBoxCellRenderer()
	{
		refMultiStateCheckBox = new MultiStateCheckBox("", false);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		if (value instanceof MultiState)
		{
			refMultiStateCheckBox.setState((MultiState)value);
			return refMultiStateCheckBox;
		}
		else if (value instanceof Boolean)
		{
			if ((Boolean)value == true)
				refMultiStateCheckBox.setState(MultiState.Checked);
			else
				refMultiStateCheckBox.setState(MultiState.None);
			return refMultiStateCheckBox;
		}

		return null;
	}

}
