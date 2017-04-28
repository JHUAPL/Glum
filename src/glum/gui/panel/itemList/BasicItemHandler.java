package glum.gui.panel.itemList;

import glum.gui.panel.itemList.query.QueryAttribute;
import glum.gui.panel.itemList.query.QueryComposer;
import glum.gui.panel.itemList.query.QueryTableCellRenderer;
import glum.unit.Unit;
import glum.unit.UnitListener;
import glum.unit.UnitProvider;
import glum.zio.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

import javax.swing.JTable;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public abstract class BasicItemHandler<G1> implements ZioObj, ItemHandler<G1>, UnitListener
{
	protected JTable myOwner;
	protected ArrayList<QueryAttribute> fullAttributeList;
	protected ArrayList<QueryAttribute> sortedAttributeList;

	/**
	* Constructor
	*/
	public BasicItemHandler(QueryComposer<?> aComposer)
	{
		this(aComposer.getItems());
	}
	
	public BasicItemHandler(Collection<QueryAttribute> aQueryAttrList)
	{
		int evalIndex;
		
		myOwner = null;

		fullAttributeList = new ArrayList<QueryAttribute>();
		sortedAttributeList = new ArrayList<QueryAttribute>();

		if (aQueryAttrList != null && aQueryAttrList.isEmpty() == false)
		{
			evalIndex = 0;
			for (QueryAttribute aAttr : aQueryAttrList)
			{
				// Ensure the model index is appropriately initialized
				if (evalIndex != aAttr.modelIndex)
					throw new RuntimeException("Improper initialization. Expected Index: " + evalIndex + "  Received Index: " + aAttr.modelIndex);
				
				fullAttributeList.add(aAttr);
				sortedAttributeList.add(aAttr);
				evalIndex++;

				// Register for the appropriate unit events
				aAttr.refUnitProvider.addListener(this);
			}
		}
		
		fullAttributeList.trimToSize();
		sortedAttributeList.trimToSize();
	}

	@Override
	public void zioRead(ZinStream aStream) throws IOException
	{
		ArrayList<QueryAttribute> newSortedList;
		int numItems, index;
		
		// Header
		aStream.readVersion(0);
		
		// Payload
		ZioObjUtil.readList(aStream, fullAttributeList);

		// Reorder the sortedAttributeList based on the serialization
		numItems = aStream.readInt();
		newSortedList = Lists.newArrayListWithCapacity(numItems);
		for (int c1 = 0; c1 < numItems; c1++)
		{
			index = aStream.readInt();
			newSortedList.add(fullAttributeList.get(index));
		}
		
		sortedAttributeList = newSortedList;
		sortedAttributeList.trimToSize();
		
		// Initialize the table columns
		rebuildTableColumns();
	}

	@Override
	public void zioWrite(ZoutStream aStream) throws IOException
	{
		int numItems;
		
		// Header
		aStream.writeVersion(0);
		
		// Payload
		ZioObjUtil.writeList(aStream, fullAttributeList);
		
		// Output the order of the sortedAttributeList
		numItems = sortedAttributeList.size();
		aStream.writeInt(numItems);
		for (QueryAttribute aAttr : sortedAttributeList)
			aStream.writeInt(aAttr.modelIndex);
	}

	@Override
	public void initialize(JTable aOwner)
	{
		JTableHeader aTableHeader;
		TableColumnModel aTableColumnModel;
		TableColumn aTableColumn;

		// This method is only allowed to be called once!
		if (myOwner != null)
			throw new RuntimeException("QueryItemHandler already initialized!");

		myOwner = aOwner;

		aTableHeader = myOwner.getTableHeader();
		aTableColumnModel = aTableHeader.getColumnModel();

		// Customize overall settings
		aTableHeader.setReorderingAllowed(false);

		// Grab all of the precomputed columns from the table
		// and store with their associated queryAttributes
		for (int c1 = 0; c1 < fullAttributeList.size(); c1++)
		{
			aTableColumn = aTableColumnModel.getColumn(c1);
			fullAttributeList.get(c1).assocTableColumn = aTableColumn;
		}

		// Rebuild the table columns; Needed so that only the
		// visible ones are displayed. Do this only after you
		// have grabbed the table columns as aTableColumnModel
		// will strictly contain the "visible" ones
		rebuildTableColumns();
	}

	@Override
	public int getColumnCount()
	{
		return fullAttributeList.size();
	}

	@Override
	public Class<?> getColumnClass(int colNum)
	{
		// Insanity check
		if (colNum < 0 && colNum >= fullAttributeList.size())
			return String.class;

		return fullAttributeList.get(colNum).refClass;
	}

	public int getColumnDefaultWidth(int colNum)
	{
		int defaultSize, minSize;

		// Insanity check
		if (colNum < 0 && colNum >= fullAttributeList.size())
			return -1;

		// Get the default and min size
		defaultSize = fullAttributeList.get(colNum).defaultSize;
		minSize = fullAttributeList.get(colNum).minSize;

		// Ensure size makes sense
		if (defaultSize < minSize)
			return minSize;

		return defaultSize;
	}

	/**
	* getColumnMinWidth
	*/
/*	public int getColumnMinWidth(int colNum)
	{
		// Insanity check
		if (queryAttributes == null)
			return -1;

		if (colNum < 0 && colNum >= queryAttributes.length)
			return -1;

		return queryAttributes[colNum].minSize;
	}
*/
	/**
	* getColumnMaxWidth
	*/
	public int getColumnMaxWidth(int colNum)
	{
		int defaultSize, maxSize;

		// Insanity check
		if (colNum < 0 && colNum >= fullAttributeList.size())
			return -1;

		// Get the default and max size
		defaultSize = fullAttributeList.get(colNum).defaultSize;
		maxSize = fullAttributeList.get(colNum).maxSize;

		// Ensure size makes sense
		if (defaultSize > maxSize && maxSize != -1)
			return defaultSize;

		return maxSize;
	}

	@Override
	public String getColumnLabel(int colNum)
	{
		QueryAttribute aAttribute;
		Unit aUnit;
		String aStr;

		// Insanity check
		if (colNum < 0 && colNum >= fullAttributeList.size())
			return "";

		aAttribute = fullAttributeList.get(colNum);

		// Retrieve the associated unit
		aUnit = aAttribute.refUnitProvider.getUnit();

		// Retrieve the base column label
		aStr = aAttribute.label;

		// Append the unit name to the column label
		if (aUnit != null && "".equals(aUnit.getLabel(false)) == false)
			return aStr + " [" + aUnit.getLabel(false) + "]";
		else
			return aStr;
	}

	@Override
	public Collection<String> getColumnLabels()
	{
		Collection<String> retSet;

		if (fullAttributeList == null)
			return new ArrayList<String>();

		retSet = new ArrayList<String>();
		for (QueryAttribute aAttribute : fullAttributeList)
		{
			if (aAttribute != null && aAttribute.label != null)
				retSet.add(aAttribute.label);
			else
				retSet.add("");
		}

		return retSet;
	}

	@Override
	public boolean isCellEditable(int colNum)
	{
		// Insanity check
		if (colNum < 0 && colNum >= fullAttributeList.size())
			return false;

		if (fullAttributeList.get(colNum).editor == null)
			return false;

		return true;
	}

	@Override
	public boolean isColumnVisible(int colNum)
	{
		// Insanity check
		if (colNum < 0 && colNum >= fullAttributeList.size())
			return false;

		return fullAttributeList.get(colNum).isVisible;
	}

	/**
	* update
	*/
	public void update()
	{
		TableColumn aTableColumn;
		QueryTableCellRenderer aRenderer;
		Object aObject;
		Unit aUnit;

		// Update all the TableColumn renderers with the appropriate unit
		for (QueryAttribute aAttribute : fullAttributeList)
		{
			if (aAttribute.assocTableColumn != null)
			{
				aTableColumn = aAttribute.assocTableColumn;
				aObject = aTableColumn.getCellRenderer();
				if (aObject instanceof QueryTableCellRenderer)
				{
					aRenderer = (QueryTableCellRenderer)aObject;
					
					aAttribute.assocTableColumn.setHeaderValue( getColumnLabel(aAttribute.modelIndex) );
					
					aUnit = aAttribute.refUnitProvider.getUnit();
					aRenderer.setUnit(aUnit);
				}
			}
		}
	}

	@Override
	public void unitChanged(UnitProvider aManager, String aKey)
	{
		JTableHeader aTableHeader;

		update();

		myOwner.repaint();

		aTableHeader = myOwner.getTableHeader();
		if (aTableHeader != null)
			aTableHeader.repaint();
/*
		for (QueryAttribute aAttribute : queryAttributes)
		{
			if (aKey.equals(aAttribute.unitKey) == true)
		}

		Tile aTile;

		// Update our listPanel to by sync with the active tile
		if (aKey.equals("tile.active") == true)
		{
			aTile = refRegistry.getSingleton(aKey, Tile.class);

			listPanel.removeListSelectionListener(this);
			listPanel.selectItem(aTile);
			listPanel.addListSelectionListener(this);
		}

		updateGui();
*/
	}



public Unit getUnit(int colNum)
{
	QueryAttribute aAttribute;

	// Insanity check
	if (colNum < 0 && colNum >= fullAttributeList.size())
		return null;

	aAttribute = fullAttributeList.get(colNum);
	return aAttribute.refUnitProvider.getUnit();
}


public void setColumnAlignment(int colNum, int aAlignment)
{
	// Insanity check
	if (colNum < 0 && colNum >= fullAttributeList.size())
		return;

	fullAttributeList.get(colNum).alignment = aAlignment;
}

public void setColumnLabel(int colNum, String aLabel)
{
	// Insanity check
	if (colNum < 0 && colNum >= fullAttributeList.size())
		return;

	fullAttributeList.get(colNum).label = aLabel;
}

public void setColumnPosition(int colNum, int aPosition)
{
	// Insanity check
	if (colNum < 0 || aPosition < 0
		|| colNum >= fullAttributeList.size()
		|| aPosition >= fullAttributeList.size())
		return;

	sortedAttributeList.remove(fullAttributeList.get(colNum));
	sortedAttributeList.add(aPosition, fullAttributeList.get(colNum));
}


public void setColumnSize(int colNum, int aSize)
{
	// Insanity check
	if (colNum < 0 && colNum >= fullAttributeList.size())
		return;

System.out.println("[QueryItemHandler.java] Changing size of colNum: " + aSize);	
	fullAttributeList.get(colNum).defaultSize = aSize;
}


public void setColumnSortDir(int colNum, int aSortDir)
{
	// Insanity check
	if (colNum < 0 && colNum >= fullAttributeList.size())
		return;

	fullAttributeList.get(colNum).sortDir = aSortDir;
}


public void setColumnVisible(int colNum, boolean isVisible)
{
	// Insanity check
	if (colNum < 0 && colNum >= fullAttributeList.size())
		return;

	fullAttributeList.get(colNum).isVisible = isVisible;
	
	// Update our table
	if (myOwner != null)
		rebuildTableColumns();
}









	/**
	* getSortedAttributes
	*/
	public ArrayList<QueryAttribute> getSortedAttributes()
	{
		return new ArrayList<QueryAttribute>(sortedAttributeList);
	}


	/**
	* moveSortedAttribute
	*/
	public void moveSortedAttribute(int currIndex, int newIndex)
	{
		QueryAttribute aItem;

		aItem = sortedAttributeList.get(currIndex);
		sortedAttributeList.remove(currIndex);
		sortedAttributeList.add(newIndex, aItem);
	}

	/**
	 * Method to reconfigure the columns of the table. This will update the
	 * display order of the columns as well as the individual relevant 
	 * attributes of the column. Currently supported relevant attributes are
	 * those defined in the method {@link QueryAttribute#setConfig}.
	 * 
	 * Note any non specified columns will appear last according to the previous order.
	 *   
	 * @param orderSet: Ordered set of QueryAttributes with matching modelIndexes
	 */
	public void setOrderAndConfig(Collection<QueryAttribute> orderArr)
	{
		Map<Integer, QueryAttribute> itemMap;
		QueryAttribute workItem;
		
		// Form a lookup map (modelIndex to attribute)
		itemMap = Maps.newLinkedHashMap();
		for (QueryAttribute aItem : sortedAttributeList)
			itemMap.put(aItem.modelIndex, aItem);
				
		// Rebuild the sortedQueryAttribute list to conform with
		// - the specified order of orderList
		// - synch up relevant attributes
		sortedAttributeList.clear();
		for (QueryAttribute  aItem: orderArr)
		{
			workItem = itemMap.remove(aItem.modelIndex);
			if (workItem != null)
			{
				workItem.setConfig(aItem);
				sortedAttributeList.add(workItem);
			}
		}
		
		sortedAttributeList.addAll(itemMap.values());
		itemMap.clear();
	}

	/**
	* initializeTableColumn - Helper method to initialize aTableColumn
	* with the actual QueryAttribute properties.
	* 
	* @param colNum: Index into column model
	*/
	protected void initializeTableColumn(int colNum)
	{
		TableCellRenderer aRenderer;
		QueryAttribute aAttribute;
		TableColumn aTableColumn;
		String aLabel;
		int defaultWidth, maxWidth, minWidth;

		// Insanity check
		if (colNum < 0 && colNum >= fullAttributeList.size())
			return;

		// Get the associated table column
		aAttribute = fullAttributeList.get(colNum);
		aTableColumn = aAttribute.assocTableColumn;
		if (aTableColumn == null)
			return;

		// Retrieve settings of interest
		aLabel = getColumnLabel(colNum);
		defaultWidth = getColumnDefaultWidth(colNum);
		maxWidth = getColumnMaxWidth(colNum);
		minWidth = aAttribute.minSize;

		// Set up the column's renderer
		aRenderer = aAttribute.renderer;
		if (aRenderer == null)
			aRenderer = new QueryTableCellRenderer(aAttribute);
		aTableColumn.setCellRenderer(aRenderer);

		// Set up the column's editor
		aTableColumn.setCellEditor(aAttribute.editor);

		// Set up the column's size attributes
		aTableColumn.setMinWidth(minWidth);
		aTableColumn.setMaxWidth(maxWidth);
		aTableColumn.setPreferredWidth(defaultWidth);
		
		// Set up the column header
		aTableColumn.setHeaderValue(aLabel);
	}
	
// TODO -> This should probably be protected
	public void rebuildTableColumns()
	{
		// Enforce the constraints (QueryAttribute) on the associated columns
		for (int c1 = 0; c1 < fullAttributeList.size(); c1++)
			initializeTableColumn(c1);

		// Remove all of the columns from the table
		for (QueryAttribute aAttribute : fullAttributeList)
			myOwner.removeColumn(aAttribute.assocTableColumn);
		
		// Add in only the columns that are visible
		for (QueryAttribute aAttribute : sortedAttributeList)	
		{
			if (aAttribute.isVisible == true)
				myOwner.addColumn(aAttribute.assocTableColumn);
		}
	}

}
