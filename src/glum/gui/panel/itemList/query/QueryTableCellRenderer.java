package glum.gui.panel.itemList.query;

import java.awt.Component;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.*;

import glum.unit.Unit;

public class QueryTableCellRenderer extends DefaultTableCellRenderer
{
	// State vars
	protected Unit myUnit;

	public QueryTableCellRenderer(QueryAttribute aAttribute)
	{
		super();

		myUnit = null;
		if (aAttribute != null)
		{
			myUnit = aAttribute.refUnitProvider.getUnit();
			setHorizontalAlignment(aAttribute.alignment);
		}
	}

	public QueryTableCellRenderer()
	{
		this(null);
	}

	/**
	 * Sets in the Unit used to retrieve the actual textual values of the table cell data
	 */
	public void setUnit(Unit aUnit)
	{
		myUnit = aUnit;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		JLabel retLabel;

		// No special processing is needed if no unit specified
		retLabel = (JLabel)super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (myUnit == null)
			return retLabel;

		retLabel.setText(myUnit.getString(value));
		return retLabel;
	}

}
