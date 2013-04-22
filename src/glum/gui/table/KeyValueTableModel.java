package glum.gui.table;

import java.util.*;
import javax.swing.table.AbstractTableModel;

import com.google.common.collect.Lists;

public class KeyValueTableModel extends AbstractTableModel
{
	private String[] columnNames;
	private List<Map.Entry<String, String>> myList;

	/**
	 * Constructor
	 */
	public KeyValueTableModel()
	{
		this(null, null);
	}

	public KeyValueTableModel(String keyHeader, String valueHeader)
	{
		columnNames = new String[2];
		columnNames[0] = "";
		columnNames[1] = "";

		if (keyHeader != null)
			columnNames[0] = keyHeader;

		if (valueHeader != null)
			columnNames[1] = valueHeader;

		myList = Lists.newArrayList();
	}

	/**
	 * Adds the collection to our TableModel
	 */
	public void addItems(Map<String, String> aMap)
	{
		int startIndex, endIndex;

		if (aMap.isEmpty() == true)
			return;

		startIndex = myList.size();
		endIndex = startIndex + aMap.size();

		myList.addAll(aMap.entrySet());

		fireTableRowsInserted(startIndex, endIndex);
	}

	/**
	 * Removes all values from the TableModel
	 */
	public void clear()
	{
		int endIndex;

		if (myList.isEmpty() == true)
			return;

		endIndex = myList.size() - 1;
		myList.clear();

		fireTableRowsDeleted(0, endIndex);
	}

	@Override
	public int getColumnCount()
	{
		return columnNames.length;
	}

	@Override
	public int getRowCount()
	{
		return myList.size();
	}

	@Override
	public String getColumnName(int col)
	{
		return columnNames[col];
	}

	@Override
	public Object getValueAt(int row, int col)
	{
		if (row < 0 || row >= myList.size())
			return null;

		// DataProviderShell Enabled
		if (col == 0)
			return myList.get(row).getKey();
		else if (col == 1)
			return myList.get(row).getValue();

		return null;
	}

	@Override
	public Class<?> getColumnClass(int col)
	{
		return String.class;
	}

	@Override
	public boolean isCellEditable(int row, int col)
	{
		// Note that the data/cell address is constant,
		// no matter where the cell appears on screen.
		return false;
	}

}
