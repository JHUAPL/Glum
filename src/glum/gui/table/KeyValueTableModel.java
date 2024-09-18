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
package glum.gui.table;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.swing.table.AbstractTableModel;

/**
 * Implementation of {@link AbstractTableModel} for displaying key / value pairs.
 *
 * @author lopeznr1
 */
public class KeyValueTableModel extends AbstractTableModel
{
	private String[] columnNameArr;
	private List<Map.Entry<String, String>> workL;

	/**
	 * Standard Constructor
	 */
	public KeyValueTableModel(String aKeyHeader, String aValueHeader)
	{
		columnNameArr = new String[2];
		columnNameArr[0] = "";
		columnNameArr[1] = "";

		if (aKeyHeader != null)
			columnNameArr[0] = aKeyHeader;

		if (aValueHeader != null)
			columnNameArr[1] = aValueHeader;

		workL = new ArrayList<>();
	}

	/**
	 * Adds the collection to our TableModel
	 */
	public void addItems(Map<String, String> aMap)
	{
		int startIndex, endIndex;

		if (aMap.isEmpty() == true)
			return;

		startIndex = workL.size();
		endIndex = startIndex + aMap.size();

		workL.addAll(aMap.entrySet());

		fireTableRowsInserted(startIndex, endIndex);
	}

	/**
	 * Removes all values from the TableModel
	 */
	public void clear()
	{
		int endIndex;

		if (workL.isEmpty() == true)
			return;

		endIndex = workL.size() - 1;
		workL.clear();

		fireTableRowsDeleted(0, endIndex);
	}

	@Override
	public int getColumnCount()
	{
		return columnNameArr.length;
	}

	@Override
	public int getRowCount()
	{
		return workL.size();
	}

	@Override
	public String getColumnName(int aCol)
	{
		return columnNameArr[aCol];
	}

	@Override
	public Object getValueAt(int aRow, int aCol)
	{
		if (aRow < 0 || aRow >= workL.size())
			return null;

		// DataProviderShell Enabled
		if (aCol == 0)
			return workL.get(aRow).getKey();
		else if (aCol == 1)
			return workL.get(aRow).getValue();

		return null;
	}

	@Override
	public Class<?> getColumnClass(int aCol)
	{
		return String.class;
	}

	@Override
	public boolean isCellEditable(int aRow, int aCol)
	{
		// Note that the data/cell address is constant,
		// no matter where the cell appears on screen.
		return false;
	}

}
