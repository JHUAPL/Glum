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
package glum.gui;

import java.util.*;

import javax.swing.JTable;
import javax.swing.event.*;

import com.google.common.primitives.Ints;

import glum.gui.table.TableSorter;
import glum.item.ItemManager;

/**
 * Collection of utilities associated with JTables.
 * 
 * @author lopeznr1
 */
public class TableUtil
{

	/**
	 * Utility method to invert the selection on the specified table. The selection will be inverted with the specified
	 * ListSelectionListener being ignored.
	 * 
	 * @param aTable
	 *        The table for which the selection should be inverted.
	 * @param aIgnoreListener
	 *        An ListSelectionListener that will not receive any intermediate events. It is important that this listener
	 *        be registered with the table's selection model as it will be deregistered and then later registered. If
	 *        null then this argument will be ignored.
	 */
	public static void invertSelection(JTable aTable, ListSelectionListener aIgnoreListener)
	{
		Set<Integer> oldSet = new HashSet<>();
		for (int aId : aTable.getSelectedRows())
			oldSet.add(aId);

		int numRows = aTable.getRowCount();
		int[] tmpArr = new int[numRows - oldSet.size()];

		// Determine the rows that are to be selected
		int idx = 0;
		for (int aId = 0; aId < numRows; aId++)
		{
			// Skip to next if row was previously selected
			if (oldSet.contains(aId) == true)
				continue;

			tmpArr[idx] = aId;
			idx++;
		}

		// Delegate
		setSortedSelection(aTable, aIgnoreListener, tmpArr);

		// Send out a single event of the change
		if (aIgnoreListener != null)
			aIgnoreListener.valueChanged(new ListSelectionEvent(aTable, 0, aTable.getRowCount() - 1, false));
	}

	/**
	 * Utility method to select the rows at the specified indexes. The selection will be updated with the specified
	 * ListSelectionListener being ignored.
	 * 
	 * @param aTable
	 *        The table for which the selection should be updated.
	 * @param aIgnoreListener
	 *        A ListSelectionListener that will not receive any intermediate events. It is important that this listener
	 *        be registered with the table's selection model as it will be deregistered and then later registered. If
	 *        null then this argument will be ignored.
	 * @param aRowL
	 *        A list of indexes corresponding to the rows that are to be selected. All other rows will be unselected.
	 */
	public static void setSelection(JTable aTable, ListSelectionListener aIgnoreListener, List<Integer> aRowL)
	{
		// Transform to a sorted array
		int[] rowArr = Ints.toArray(aRowL);
		Arrays.parallelSort(rowArr);

		// Delegate
		setSortedSelection(aTable, aIgnoreListener, rowArr);
	}

	/**
	 * Utility method that will synchronize the table selection to match the selected items in the ItemManager. If new
	 * items were selected then the table will be scrolled (to the first newly selected row).
	 */
	public static <G1> void updateTableSelection(ListSelectionListener aIgnoreListener, ItemManager<G1> aManager,
			JTable aTable, TableSorter aSortTableModel)
	{
		// Form a reverse lookup map of item to (view) index
		List<G1> fullItemL = aManager.getAllItems();
		Map<G1, Integer> revLookM = new HashMap<>();
		for (int aIdx = 0; aIdx < fullItemL.size(); aIdx++)
		{
			int tmpIdx = aSortTableModel.viewIndex(aIdx);
			revLookM.put(fullItemL.get(aIdx), tmpIdx);
		}

		int[] idxArr = aTable.getSelectedRows();
		List<Integer> oldL = Ints.asList(idxArr);
		Set<Integer> oldS = new LinkedHashSet<>(oldL);

		List<Integer> newL = new ArrayList<>();
		for (G1 aItem : aManager.getSelectedItems())
			newL.add(revLookM.get(aItem));
		Set<Integer> newS = new LinkedHashSet<>(newL);

		// Bail if nothing has changed
		if (newS.equals(oldS) == true)
			return;

		// Update the table's selection
		setSelection(aTable, aIgnoreListener, newL);
		aTable.repaint();
	}

	/**
	 * Utility helper method that selects the specified rows.
	 * <p>
	 * The rows must be in sorted order.
	 * 
	 * @param aTable
	 *        The table for which the selection should be updated.
	 * @param aIgnoreListener
	 *        An ListSelectionListener that will not receive any intermediate events. It is important that this listener
	 *        be registered with the table's selection model as it will be deregistered and then later registered. If
	 *        null then this argument will be ignored.
	 * @param aSortedRowArr
	 *        An array of indexes corresponding to the rows that are to be selected. All other rows will be unselected.
	 */
	private static void setSortedSelection(JTable aTable, ListSelectionListener aIgnoreListener, int[] aSortedRowArr)
	{
		// Initial range
		int begIdx = -1;
		int endIdx = -1;
		if (aSortedRowArr.length >= 1)
		{
			begIdx = aSortedRowArr[0];
			endIdx = begIdx;
		}

		if (aIgnoreListener != null)
			aTable.getSelectionModel().removeListSelectionListener(aIgnoreListener);

		aTable.clearSelection();

		for (int aRow : aSortedRowArr)
		{
			// Expand the range by: +1
			if (aRow == endIdx + 1)
			{
				endIdx++;
				continue;
			}

			// Add the current interval
			aTable.addRowSelectionInterval(begIdx, endIdx);

			// Start a new range
			begIdx = endIdx = aRow;
		}

		// Ensure the last interval gets added
		if (begIdx != -1 && endIdx != -1)
			aTable.addRowSelectionInterval(begIdx, endIdx);

		if (aIgnoreListener != null)
			aTable.getSelectionModel().addListSelectionListener(aIgnoreListener);
	}

}
