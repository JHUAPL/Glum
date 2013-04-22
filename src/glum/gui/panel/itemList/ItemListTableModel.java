package glum.gui.panel.itemList;

import java.util.*;
import javax.swing.table.AbstractTableModel;

public class ItemListTableModel<G1> extends AbstractTableModel
{
	private ItemHandler<G1> myHandler;
	private ArrayList<G1> myVector;

	/**
	 * Constructor
	 */
	public ItemListTableModel(ItemHandler<G1> aHandler)
	{
		myHandler = aHandler;

		myVector = new ArrayList<G1>();
	}

	@Override
	public int getColumnCount()
	{
		if (myHandler == null)
			return 0;

		return myHandler.getColumnCount();
	}

	@Override
	public int getRowCount()
	{
		return myVector.size();
	}

	@Override
	public String getColumnName(int col)
	{
		if (myHandler == null)
			return null;

		return myHandler.getColumnLabel(col);
	}

	@Override
	public Object getValueAt(int row, int col)
	{
		if (myHandler == null)
			return null;

		return myHandler.getColumnValue(myVector.get(row), col);
	}

	@Override
	public Class<?> getColumnClass(int col)
	{
		return myHandler.getColumnClass(col);
	}

	@Override
	public boolean isCellEditable(int row, int col)
	{
		if (myHandler == null)
			return false;

		return myHandler.isCellEditable(col);
	}

	@Override
	public void setValueAt(Object value, int row, int col)
	{
		if (myHandler == null)
			return;

		myHandler.setColumnValue(myVector.get(row), col, value);
	}

	/**
	 * Removes all values from the TableModel
	 */
	public void clear()
	{
		int endIndex;

		if (myVector.isEmpty() == true)
			return;

		endIndex = myVector.size() - 1;
		myVector.clear();

		fireTableRowsDeleted(0, endIndex);
	}

	/**
	 * Returns the row index associated with aItem
	 */
	public int getRowIndex(G1 aItem)
	{
		int aIndex;

		if (aItem == null)
			return -1;

		aIndex = 0;
		for (G1 aObj : myVector)
		{
			if (aObj.equals(aItem) == true)
				return aIndex;

			aIndex++;
		}

		return -1;
	}

	/**
	 * Returns the item associated with the row
	 */
	public G1 getRowItem(int row)
	{
		if (row < 0 || row >= myVector.size())
			return null;

		return myVector.get(row);
	}

	/**
	 * Adds the collection to our TableModel
	 */
	public void addItems(Collection<? extends G1> aCollection)
	{
		int startIndex, endIndex;

		if (aCollection == null)
			return;

		if (aCollection.isEmpty() == true)
			return;

		startIndex = myVector.size();
		endIndex = startIndex + aCollection.size();

		myVector.addAll(aCollection);

		fireTableRowsInserted(startIndex, endIndex);
	}

}
