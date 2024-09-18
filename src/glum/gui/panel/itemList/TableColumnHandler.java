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

import java.util.*;

import javax.swing.JTable;

import com.google.common.collect.ImmutableList;

import glum.gui.panel.itemList.query.QueryAttribute;
import glum.gui.panel.itemList.query.QueryTableCellRenderer;
import glum.gui.table.SortDir;
import glum.gui.table.TableSorter;
import glum.unit.*;

/**
 * Support class for {@link ItemListTableModel}.
 *
 * @author lopeznr1
 */
public class TableColumnHandler<G2 extends Enum<?>> implements UnitListener
{
	// Attributes
	private final ArrayList<QueryAttribute<G2>> fullAttributeL;

	// State vars
	private JTable myOwner;
	private ArrayList<QueryAttribute<G2>> orderAttributeL;

	/** Standard Constructor */
	public TableColumnHandler(Collection<QueryAttribute<G2>> aQueryAttrC)
	{
		myOwner = null;

		fullAttributeL = new ArrayList<>();
		orderAttributeL = new ArrayList<>();

		int evalIndex = 0;
		for (QueryAttribute<G2> aAttr : aQueryAttrC)
		{
			// Ensure the model index is appropriately initialized
			if (evalIndex != aAttr.modelIndex)
				throw new RuntimeException(
						"Improper initialization. Expected Index: " + evalIndex + "  Received Index: " + aAttr.modelIndex);

			fullAttributeL.add(aAttr);
			orderAttributeL.add(aAttr);
			evalIndex++;

			// Register for the appropriate unit events
			aAttr.refUnitProvider.addListener(this);
		}

		fullAttributeL.trimToSize();
		orderAttributeL.trimToSize();
	}

	/**
	 * Returns the (enum) lookup associated with the specified column index.
	 * <p>
	 * Returns null if the column index is out of range.
	 */
	public G2 getEnum(int aColIdx)
	{
		// Insanity check
		if (aColIdx < 0 && aColIdx >= fullAttributeL.size())
			return null;

		return fullAttributeL.get(aColIdx).refKey;
	}

	/**
	 * Notifies the {@link TableColumnHandler} of its associated JTable. This table should be updated or painted whenever
	 * the internals of this {@link TableColumnHandler} change.
	 */
	public void initialize(JTable aOwner)
	{
		// This method is only allowed to be called once!
		if (myOwner != null)
			throw new RuntimeException("TableColumnHandler already initialized!");

		myOwner = aOwner;

		var tmpTableHeader = myOwner.getTableHeader();
		var tmpTableColumnModel = tmpTableHeader.getColumnModel();

		// Customize overall settings
		tmpTableHeader.setReorderingAllowed(false);

		// Grab all of the precomputed columns from the table
		// and store with their associated queryAttributes
		for (int c1 = 0; c1 < fullAttributeL.size(); c1++)
		{
			var tmpTableColumn = tmpTableColumnModel.getColumn(c1);
			fullAttributeL.get(c1).assocTableColumn = tmpTableColumn;
		}

		// Rebuild the table columns; Needed so that only the
		// visible ones are displayed. Do this only after you
		// have grabbed the table columns as aTableColumnModel
		// will strictly contain the "visible" ones
		rebuildTableColumns();
	}

	/**
	 * Returns the number of columns associated with the tabular data.
	 */
	public int getColumnCount()
	{
		return fullAttributeL.size();
	}

	/**
	 * Returns the class of data type for each column in the tabular data.
	 */
	public Class<?> getColumnClass(int colNum)
	{
		// Insanity check
		if (colNum < 0 && colNum >= fullAttributeL.size())
			return String.class;

		return fullAttributeL.get(colNum).refClass;
	}

	public int getColumnDefaultWidth(int colNum)
	{
		// Insanity check
		if (colNum < 0 && colNum >= fullAttributeL.size())
			return -1;

		// Get the default and min size
		var defaultSize = fullAttributeL.get(colNum).defaultSize;
		var minSize = fullAttributeL.get(colNum).minSize;

		// Ensure size makes sense
		if (defaultSize < minSize)
			return minSize;

		return defaultSize;
	}

	/**
	 * getColumnMinWidth
	 */
//	public int getColumnMinWidth(int colNum)
//	{
//		// Insanity check
//		if (queryAttributes == null)
//			return -1;
//
//		if (colNum < 0 && colNum >= queryAttributes.length)
//			return -1;
//
//		return queryAttributes[colNum].minSize;
//	}

	/**
	 * getColumnMaxWidth
	 */
	public int getColumnMaxWidth(int colNum)
	{
		// Insanity check
		if (colNum < 0 && colNum >= fullAttributeL.size())
			return -1;

		// Get the default and max size
		var defaultSize = fullAttributeL.get(colNum).defaultSize;
		var maxSize = fullAttributeL.get(colNum).maxSize;

		// Ensure size makes sense
		if (defaultSize > maxSize && maxSize != -1)
			return defaultSize;

		return maxSize;
	}

	/**
	 * Returns the text label that should be used for the title of the column.
	 */
	public String getColumnLabel(int colNum)
	{
		// Insanity check
		if (colNum < 0 && colNum >= fullAttributeL.size())
			return "";

		var tmpAttribute = fullAttributeL.get(colNum);

		// Retrieve the associated unit
		var tmpUnit = tmpAttribute.refUnitProvider.getUnit();

		// Retrieve the base column label
		var tmpStr = tmpAttribute.label;

		// Append the unit name to the column label
		if (tmpUnit != null && "".equals(tmpUnit.getLabel(false)) == false)
			return tmpStr + " [" + tmpUnit.getLabel(false) + "]";
		else
			return tmpStr;
	}

	/**
	 * Returns an ordered list of the {@link QueryAttribute}s.
	 */
	public ArrayList<QueryAttribute<G2>> getOrderedAttributes()
	{
		if (myOwner.getModel() instanceof TableSorter aTableSorter)
		{
			for (var aAttr : orderAttributeL)
			{
				var tmpIdx = aAttr.modelIndex;
				aAttr.sortDir = aTableSorter.getSortDir(tmpIdx);
			}
		}

		return new ArrayList<QueryAttribute<G2>>(orderAttributeL);
	}

	/**
	 * Returns a list of model indexes which define the priority of the sorted columns.
	 */
	public List<Integer> getSortPriorityList()
	{
		var tmpModel = myOwner.getModel();
		if (tmpModel instanceof TableSorter aTableSorter)
		{
			var retSortPriorityL = new ArrayList<Integer>();
			var tmpSortStateM = aTableSorter.getSortState();
			for (var aModelIdx : tmpSortStateM.keySet())
				retSortPriorityL.add(aModelIdx);

			return retSortPriorityL;
		}

		return ImmutableList.of();
	}

	/**
	 * Returns true if the data associated at the specified column can be edited.
	 */
	public boolean isColumnEditable(int colNum)
	{
		// Insanity check
		if (colNum < 0 && colNum >= fullAttributeL.size())
			return false;

		if (fullAttributeL.get(colNum).editor == null)
			return false;

		return true;
	}

	@Override
	public void unitChanged(UnitProvider aManager, String aKey)
	{
		update();

		myOwner.repaint();

		var tmpTableHeader = myOwner.getTableHeader();
		if (tmpTableHeader != null)
			tmpTableHeader.repaint();

//		for (QueryAttribute aAttribute : queryAttributes)
//		{
//			if (aKey.equals(aAttribute.unitKey) == true)
//		}
//
//		Tile aTile;
//
//		// Update our listPanel to by sync with the active tile
//		if (aKey.equals("tile.active") == true)
//		{
//			aTile = refRegistry.getSingleton(aKey, Tile.class);
//
//			listPanel.removeListSelectionListener(this);
//			listPanel.selectItem(aTile);
//			listPanel.addListSelectionListener(this);
//		}
//
//		updateGui();
	}

	public Unit getUnit(int aColNum)
	{
		// Insanity check
		if (aColNum < 0 && aColNum >= fullAttributeL.size())
			return null;

		var tmpAttribute = fullAttributeL.get(aColNum);
		return tmpAttribute.refUnitProvider.getUnit();
	}

	public void setColumnAlignment(int colNum, int aAlignment)
	{
		// Insanity check
		if (colNum < 0 && colNum >= fullAttributeL.size())
			return;

		fullAttributeL.get(colNum).alignment = aAlignment;
	}

	public void setColumnLabel(int colNum, String aLabel)
	{
		// Insanity check
		if (colNum < 0 && colNum >= fullAttributeL.size())
			return;

		fullAttributeL.get(colNum).label = aLabel;
	}

	public void setColumnPosition(int colNum, int aPosition)
	{
		// Insanity check
		if (colNum < 0 || aPosition < 0 || colNum >= fullAttributeL.size() || aPosition >= fullAttributeL.size())
			return;

		orderAttributeL.remove(fullAttributeL.get(colNum));
		orderAttributeL.add(aPosition, fullAttributeL.get(colNum));
	}

	public void setColumnSize(int colNum, int aSize)
	{
		// Insanity check
		if (colNum < 0 && colNum >= fullAttributeL.size())
			return;

		System.out.println("[QueryItemHandler.java] Changing size of colNum: " + aSize);
		fullAttributeL.get(colNum).defaultSize = aSize;
	}

	public void setColumnSortDir(int colNum, SortDir aSortDir)
	{
		// Insanity check
		if (colNum < 0 && colNum >= fullAttributeL.size())
			return;

		fullAttributeL.get(colNum).sortDir = aSortDir;
	}

	public void setColumnVisible(int colNum, boolean isVisible)
	{
		// Insanity check
		if (colNum < 0 && colNum >= fullAttributeL.size())
			return;

		fullAttributeL.get(colNum).isVisible = isVisible;

		// Update our table
		if (myOwner != null)
			rebuildTableColumns();
	}

	/**
	 * moveSortedAttribute
	 */
	public void moveSortedAttribute(int currIndex, int newIndex)
	{
		var tmpItem = orderAttributeL.get(currIndex);
		orderAttributeL.remove(currIndex);
		orderAttributeL.add(newIndex, tmpItem);
	}

	/**
	 * Method to reconfigure the columns of the table. This will update the display order of the columns as well as the
	 * individual relevant attributes of the column. Currently supported relevant attributes are those defined in the
	 * method {@link QueryAttribute#setConfig}.
	 *
	 * Note any non specified columns will appear last according to the previous order.
	 *
	 * @param orderSet:
	 *        Ordered set of QueryAttributes with matching modelIndexes
	 */
	public void setOrderAndConfig(Collection<QueryAttribute<G2>> aOrderArr)
	{
		// Form a lookup map (modelIndex to attribute)
		var tmpItemM = new LinkedHashMap<Integer, QueryAttribute<G2>>();
		for (var aItem : orderAttributeL)
			tmpItemM.put(aItem.modelIndex, aItem);

		// Rebuild the sortedQueryAttribute list to conform with
		// - the specified order of orderList
		// - synch up relevant attributes
		orderAttributeL.clear();
		for (QueryAttribute<G2> aItem : aOrderArr)
		{
			var workItem = tmpItemM.remove(aItem.modelIndex);
			if (workItem != null)
			{
				workItem.setConfig(aItem);
				orderAttributeL.add(workItem);
			}
		}

		orderAttributeL.addAll(tmpItemM.values());
		tmpItemM.clear();
	}

	/**
	 * Returns a list of model indexes which define the priority of the sorted columns.
	 */
	public void setSortPriorityList(List<Integer> aSortPriorityL)
	{
		var tmpModel = myOwner.getModel();
		if (tmpModel instanceof TableSorter aTableSorter)
		{
			var tmpAttributeM = new HashMap<Integer, QueryAttribute<?>>();
			for (var aAttribute: fullAttributeL)
				tmpAttributeM.put(aAttribute.modelIndex, aAttribute);

			var tmpSortStateM = new LinkedHashMap<Integer, SortDir>();
			for (var aModelIdx : aSortPriorityL)
			{
				var tmpAttribute = tmpAttributeM.get(aModelIdx);
				if (tmpAttribute == null)
					continue;

				tmpSortStateM.put(aModelIdx, tmpAttribute.sortDir);
			}

			aTableSorter.setSortState(tmpSortStateM);
		}
	}

	/**
	 * Helper method to initialize a TableColumn with the actual QueryAttribute properties.
	 *
	 * @param colNum:
	 *        Index into column model
	 */
	protected void initializeTableColumn(int aColNum)
	{
		// Insanity check
		if (aColNum < 0 && aColNum >= fullAttributeL.size())
			return;

		// Get the associated table column
		var tmpAttribute = fullAttributeL.get(aColNum);
		var tmpTableColumn = tmpAttribute.assocTableColumn;
		if (tmpTableColumn == null)
			return;

		// Retrieve settings of interest
		var label = getColumnLabel(aColNum);
		var defaultWidth = getColumnDefaultWidth(aColNum);
		var maxWidth = getColumnMaxWidth(aColNum);
		var minWidth = tmpAttribute.minSize;

		// Set up the column's renderer
		var tmpRenderer = tmpAttribute.renderer;
		if (tmpRenderer == null)
			tmpRenderer = new QueryTableCellRenderer(tmpAttribute);
		tmpTableColumn.setCellRenderer(tmpRenderer);

		// Set up the column's editor
		tmpTableColumn.setCellEditor(tmpAttribute.editor);

		// Set up the column's size attributes
		tmpTableColumn.setMinWidth(minWidth);
		tmpTableColumn.setMaxWidth(maxWidth);
		tmpTableColumn.setPreferredWidth(defaultWidth);
		tmpTableColumn.setWidth(defaultWidth);

		// Set up the column header
		tmpTableColumn.setHeaderValue(label);
	}

// TODO -> This should probably be protected
	public void rebuildTableColumns()
	{
		// Enforce the constraints (QueryAttribute) on the associated columns
		for (int c1 = 0; c1 < fullAttributeL.size(); c1++)
			initializeTableColumn(c1);

		// Remove all of the columns from the table
		for (var aAttribute : fullAttributeL)
			myOwner.removeColumn(aAttribute.assocTableColumn);

		// Add in only the columns that are visible
		for (var aAttribute : orderAttributeL)
		{
			if (aAttribute.isVisible == false)
				continue;
			myOwner.addColumn(aAttribute.assocTableColumn);
		}
	}

	/**
	 * Helper method that updates all the TableColumn renders with the appropriate unit.
	 */
	private void update()
	{
		for (var aAttribute : fullAttributeL)
		{
			if (aAttribute.assocTableColumn != null)
			{
				var tmpTableColumn = aAttribute.assocTableColumn;
				var tmpObject = tmpTableColumn.getCellRenderer();
				if (tmpObject instanceof QueryTableCellRenderer aRenderer)
				{
					aAttribute.assocTableColumn.setHeaderValue(getColumnLabel(aAttribute.modelIndex));

					var tmpUnit = aAttribute.refUnitProvider.getUnit();
					aRenderer.setUnit(tmpUnit);
				}
			}
		}
	}

}
