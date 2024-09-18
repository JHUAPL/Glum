// Copyright (C) 2024 The Johns Hopkins University Applied Physics Laboratory LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package glum.gui.panel.itemList;

import java.util.ArrayList;
import java.util.Collection;

import javax.swing.JTable;
import javax.swing.table.AbstractTableModel;

/**
 * TableModel that provides access to a collection of items handled by the provided {@link ItemHandler}.
 *
 * @param <G1>
 * @param <G2>
 *
 * @author lopeznr1
 */
public class ItemListTableModel<G1, G2 extends Enum<?>> extends AbstractTableModel
{
	// Ref vars
	private final ItemHandler<G1, G2> refItemHandler;

	// State vars
	private final TableColumnHandler<G2> workTableColumnHandler;
	private final ArrayList<G1> myItemL;

	/**
	 * Standard Constructor
	 *
	 * @param aItemHandler
	 * @param aTableColumnHandler
	 */
	public ItemListTableModel(ItemHandler<G1, G2> aItemHandler, TableColumnHandler<G2> aTableColumnHandler)
	{
		refItemHandler = aItemHandler;
		workTableColumnHandler = aTableColumnHandler;

		myItemL = new ArrayList<>();
	}

	@Override
	public int getColumnCount()
	{
		return workTableColumnHandler.getColumnCount();
	}

	@Override
	public int getRowCount()
	{
		return myItemL.size();
	}

	@Override
	public String getColumnName(int aCol)
	{
		return workTableColumnHandler.getColumnLabel(aCol);
	}

	@Override
	public Object getValueAt(int aRow, int aCol)
	{
		// Locate the lookup corresponding to the specified column
		var tmpEnum = workTableColumnHandler.getEnum(aCol);
		if (tmpEnum == null)
			return null;

		// Retrieve the appropriate data field of the appropriate item
		return refItemHandler.getValue(myItemL.get(aRow), tmpEnum);
	}

	@Override
	public Class<?> getColumnClass(int aCol)
	{
		return workTableColumnHandler.getColumnClass(aCol);
	}

	@Override
	public boolean isCellEditable(int aRow, int aCol)
	{
		return workTableColumnHandler.isColumnEditable(aCol);
	}

	@Override
	public void setValueAt(Object aValue, int aRow, int aCol)
	{
		// Locate the lookup corresponding to the specified column
		var tmpEnum = workTableColumnHandler.getEnum(aCol);
		if (tmpEnum == null)
			return;

		// Update the appropriate data field of the appropriate item
		refItemHandler.setValue(myItemL.get(aRow), tmpEnum, aValue);
	}

	/**
	 * Removes all values from the TableModel
	 */
	public void clear()
	{
		if (myItemL.isEmpty() == true)
			return;

		int endIdx = myItemL.size() - 1;
		myItemL.clear();

		fireTableRowsDeleted(0, endIdx);
	}

	/**
	 * Returns the row index associated with aItem
	 */
	public int getRowIndex(G1 aItem)
	{
		if (aItem == null)
			return -1;

		int tmpIdx = 0;
		for (G1 aObj : myItemL)
		{
			if (aObj.equals(aItem) == true)
				return tmpIdx;

			tmpIdx++;
		}

		return -1;
	}

	/**
	 * Returns the item associated with the row
	 */
	public G1 getRowItem(int aRow)
	{
		if (aRow < 0 || aRow >= myItemL.size())
			return null;

		return myItemL.get(aRow);
	}

	/**
	 * Adds the collection to our TableModel
	 */
	public void addItems(Collection<? extends G1> aItemC)
	{
		if (aItemC == null)
			return;

		if (aItemC.isEmpty() == true)
			return;

		var startIndex = myItemL.size();
		var endIndex = startIndex + aItemC.size();

		myItemL.addAll(aItemC);

		fireTableRowsInserted(startIndex, endIndex);
	}

	/**
	 * Notifies this {@link TableColumnHandler} of the associated {@link JTable}.
	 */
	public void initialize(JTable aTable)
	{
		// Delegate
		workTableColumnHandler.initialize(aTable);
	}

}
