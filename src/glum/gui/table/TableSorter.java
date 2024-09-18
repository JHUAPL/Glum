/*
 * Copyright (c) 1995, 2008, Oracle and/or its affiliates. All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions
 * are met:
 *
 *   - Redistributions of source code must retain the above copyright
 *     notice, this list of conditions and the following disclaimer.
 *
 *   - Redistributions in binary form must reproduce the above copyright
 *     notice, this list of conditions and the following disclaimer in the
 *     documentation and/or other materials provided with the distribution.
 *
 *   - Neither the name of Oracle or the names of its
 *     contributors may be used to endorse or promote products derived
 *     from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS
 * IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED.  IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR
 * PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF
 * LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 * NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS
 * SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package glum.gui.table;

import java.awt.Component;
import java.awt.event.*;
import java.util.*;

import javax.swing.*;
import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.*;

import com.google.common.collect.ImmutableList;

import glum.gui.table.sort.DefaultSortIconProvider;
import glum.gui.table.sort.SortIconProvider;

/**
 * {@link TableSorter} is a decorator for TableModels; adding sorting functionality to a supplied TableModel.
 * TableSorter does not store or copy the data in its TableModel; instead it maintains a map from the row indexes of the
 * view to the row indexes of the model. As requests are made of the sorter (like getValueAt(row, col)) they are passed
 * to the underlying model after the row numbers have been translated via the internal mapping array. This way, the
 * TableSorter appears to hold another copy of the table with the rows in a different order.
 * <p>
 * TableSorter registers itself as a listener to the underlying model, just as the JTable itself would. Events received
 * from the model are examined, sometimes manipulated (typically widened), and then passed on to the TableSorter's
 * listeners (typically the JTable). If a change to the model has invalidated the order of TableSorter's rows, a note of
 * this is made and the sorter will resort the rows the next time a value is requested.
 * <p>
 * When the tableHeader property is set, either by using the setTableHeader() method or the two argument constructor,
 * the table header may be used as a complete UI for TableSorter. The default renderer of the tableHeader is decorated
 * with a renderer that indicates the sorting status of each column. In addition, a mouse listener is installed with the
 * following behavior:
 * <ul>
 * <li>Mouse-Click: Clears the sorting status of all other columns and sets the current column as the primary sort
 * column.
 * <li>CONTROL-Mouse-Click: Adds a secondary (or lower rank) column to sort upon.
 * <li>SHIFT-Mouse-Click: Clears the sort status of the selected column. The column will no longer influence the sort.
 * </ul>
 * Please note that mouse-clicking on the columns will toggle the sort directive of that column from ascending to
 * descending.
 * <p>
 * Column headers that have been selected for sorting provide the following (default) visual indicators:
 * <ul>
 * <li>The 1st (primary) sort column is displayed with a bright red icon.
 * <li>The 2nd sort column is displayed with a dark red icon.
 * <li>The 3rd sort column is displayed with a black icon.
 * <li>The 4th (and all lower level) sort column is displayed with a gray icon.
 * </ul>
 * Please note that the icons used for painting the sort indicators can be customized via the method
 * {@link #setSortIconProvider(SortIconProvider)}.
 * <p>
 * This class is an incompatible rewrite of the TableSorter class (~2004Feb27). The original class can be currently be
 * sourced (as of date 2019Aug20) from: </br>
 * https://docs.oracle.com/javase/tutorial/uiswing/examples/components/TableSorterDemoProject/src/components/TableSorter.java</br>
 * or</br>
 * https://web.archive.org/web/20171019235101/http://docs.oracle.com/javase/tutorial/uiswing/examples/components/TableSorterDemoProject/src/components/TableSorter.java
 *
 * @author Philip Milne
 * @author Brendon McLean
 * @author Dan van Enckevort
 * @author Parwinder Sekhon
 * @author lopeznr1 (Rewrite author as used in Glum library)
 */
public class TableSorter extends AbstractTableModel
{
	// Constants
	private static final Comparator<?> LEXICAL_COMPARATOR = Comparator.comparing(Object::toString);

	// Ref vars
	private final TableModel refTableModel;
	private JTableHeader refTableHeader;

	// State vars
	private Row[] viewToModel;
	private int[] modelToView;

	private final MouseListener mouseListener;
	private final TableModelListener tableModelListener;
	private final Map<Class<?>, Comparator<?>> columnComparatorClassM;
	private final Map<Integer, Comparator<?>> columnComparatorIndexM;
	private final List<Directive> sortingColumnL;
	private boolean isSortEnabled;

	// Render vars
	private SortIconProvider refSortIconProvider;
	private ImmutableList<Icon> sortIconAsceL;
	private ImmutableList<Icon> sortIconDescL;
	private int cSize;

	/**
	 * Standard Constructor
	 *
	 * @param aTableModel
	 *        The table model which will be used as the backing table model.
	 * @param aTableHeader
	 *        The table header asociated with the JTable.
	 */
	public TableSorter(TableModel aTableModel, JTableHeader aTableHeader)
	{
		refTableModel = aTableModel;

		viewToModel = null;
		modelToView = null;

		mouseListener = new MouseHandler();
		tableModelListener = new TableModelHandler();
		columnComparatorClassM = new HashMap<>();
		columnComparatorIndexM = new HashMap<>();
		sortingColumnL = new ArrayList<>();
		isSortEnabled = true;

		refSortIconProvider = DefaultSortIconProvider.Default;
		sortIconAsceL = ImmutableList.of();
		sortIconDescL = ImmutableList.of();
		cSize = -1;

		if (aTableHeader != null)
			setTableHeader(aTableHeader);

		// Register for events of interest
		refTableModel.addTableModelListener(tableModelListener);
		fireTableStructureChanged();
	}

	/** Simplified Constructor */
	public TableSorter(TableModel aTableModel)
	{
		this(aTableModel, null);
	}

	/**
	 * Returns the {@link SortDir} of the specified column.
	 */
	public SortDir getSortDir(int aColNum)
	{
		var tmpDirective = getDirective(aColNum);
		if (tmpDirective != null)
			return tmpDirective.sortDir;

		return SortDir.NotSorted;
	}

	/**
	 * Returns an ordered map of column model index to the {@link SortDir}.
	 * <p>
	 * The returned map is a snapshot of the columns that are sorted and their ordered priority.
	 */
	public Map<Integer, SortDir> getSortState()
	{
		var retSortStateM = new LinkedHashMap<Integer, SortDir>();
		for (var aDirective : sortingColumnL)
			retSortStateM.put(aDirective.column, aDirective.sortDir);

		return retSortStateM;
	}

	public JTableHeader getTableHeader()
	{
		return refTableHeader;
	}

	public TableModel getTableModel()
	{
		return refTableModel;
	}

	/**
	 * Returns true if any columns have a sort directive set.
	 */
	public boolean isSorting()
	{
		return sortingColumnL.size() != 0;
	}

	/**
	 * Sets in the Comparator that will be associated with columns of data type, aType.
	 * <p>
	 * This Comparator will only be used if the column does not have a specific Comparator associated with it.
	 *
	 * @param aType
	 * @param aComparator
	 */
	public void setColumnClassComparator(Class<?> aType, Comparator<?> aComparator)
	{
		if (aComparator == null)
			columnComparatorClassM.remove(aType);
		else
			columnComparatorClassM.put(aType, aComparator);
	}

	/**
	 * Sets in the Comparator that will be associated with the specified column.
	 * <p>
	 * This Comparator will be used before other (criteria matching) Comparators.
	 *
	 * @param aColNum
	 * @param aComparator
	 */
	public void setColumnIndexComparator(int aColNum, Comparator<?> aComparator)
	{
		if (aComparator == null)
			columnComparatorIndexM.remove(aColNum);
		else
			columnComparatorIndexM.put(aColNum, aComparator);
	}

	/**
	 * Sets the {@link SortIconProvider}.
	 */
	public void setSortIconProvider(SortIconProvider aSortIconProvider)
	{
		refSortIconProvider = aSortIconProvider;

		// Invalidate the sort icons cache
		sortIconAsceL = ImmutableList.of();
		sortIconDescL = ImmutableList.of();
		cSize = -1;

		sortingStatusChanged();
	}

	/**
	 * Installs an ordered map of column model index to the {@link SortDir}.
	 * <p>
	 * The installed map will set the columns that are sorted and their ordered sort priority.
	 */
	public void setSortState(Map<Integer, SortDir> aSortDirM)
	{
		sortingColumnL.clear();
		for (var aCol : aSortDirM.keySet())
			sortingColumnL.add(new Directive(aCol, aSortDirM.get(aCol)));

		sortingStatusChanged();
	}

	/**
	 * Sets whether sorting is enabled or disabled.
	 */
	public void setSortingEnabled(boolean aBool)
	{
		isSortEnabled = aBool;

		clearSortingState();
	}

	/**
	 * Sets in the table header associated with the backing TableModel. A mouse listener will be installed that provides
	 * a fully functional UI for configuring the rank and sort directive of the various columns.
	 *
	 * @param aTableHeader
	 */
	public void setTableHeader(JTableHeader aTableHeader)
	{
		if (refTableHeader != null)
		{
			refTableHeader.removeMouseListener(mouseListener);
			TableCellRenderer defaultRenderer = refTableHeader.getDefaultRenderer();
			if (defaultRenderer instanceof SortableHeaderRenderer)
			{
				refTableHeader.setDefaultRenderer(((SortableHeaderRenderer) defaultRenderer).tableCellRenderer);
			}
		}
		refTableHeader = aTableHeader;
		if (refTableHeader != null)
		{
			refTableHeader.addMouseListener(mouseListener);
			refTableHeader.setDefaultRenderer(new SortableHeaderRenderer(refTableHeader.getDefaultRenderer()));
		}
	}

	public int modelIndex(int viewIndex)
	{
		return getViewToModel()[viewIndex].modelIndex;
	}

	public int viewIndex(int modelIndex)
	{
		return getModelToView()[modelIndex];
	}

	// TableModel interface methods
	@Override
	public int getRowCount()
	{
		return (refTableModel == null) ? 0 : refTableModel.getRowCount();
	}

	@Override
	public int getColumnCount()
	{
		return (refTableModel == null) ? 0 : refTableModel.getColumnCount();
	}

	@Override
	public String getColumnName(int column)
	{
		return refTableModel.getColumnName(column);
	}

	@Override
	public Class<?> getColumnClass(int column)
	{
		return refTableModel.getColumnClass(column);
	}

	@Override
	public boolean isCellEditable(int row, int column)
	{
		return refTableModel.isCellEditable(modelIndex(row), column);
	}

	@Override
	public Object getValueAt(int row, int column)
	{
		return refTableModel.getValueAt(modelIndex(row), column);
	}

	@Override
	public void setValueAt(Object aValue, int row, int column)
	{
		refTableModel.setValueAt(aValue, modelIndex(row), column);
	}

	/**
	 * Helper method that clears all sorting directives and sends out the proper notification.
	 */
	private void cancelSorting()
	{
		sortingColumnL.clear();
		sortingStatusChanged();
	}

	private void clearSortingState()
	{
		viewToModel = null;
		modelToView = null;
	}

	/**
	 * Helper method that returns the appropriate Comparator for the specified column.
	 */
	private Comparator<?> getComparator(int aColNum)
	{
		Comparator<?> retComparator;

		// Utilize the column index Comparator (if specified)
		retComparator = columnComparatorIndexM.get(aColNum);
		if (retComparator != null)
			return retComparator;

		// Utilize the class type Comparator (if specified)
		Class<?> columnType = refTableModel.getColumnClass(aColNum);
		retComparator = columnComparatorClassM.get(columnType);
		if (retComparator != null)
			return retComparator;

		if (Comparable.class.isAssignableFrom(columnType))
			return Comparator.naturalOrder();

		return LEXICAL_COMPARATOR;
	}

	/**
	 * Helper method that returns the sort directive of the specified column.
	 * <p>
	 * Returns null if the column is not sorted.
	 */
	private Directive getDirective(int aColNum)
	{
		for (Directive aDirective : sortingColumnL)
		{
			if (aDirective.column == aColNum)
				return aDirective;
		}
		return null;
	}

	/**
	 * Helper method that returns the icon that should be used for the specified column.
	 * <p>
	 * If the column is not sorted then null will be returned.
	 *
	 * @param aColumn
	 * @param aSize
	 * @return
	 */
	private Icon getHeaderRendererIcon(int aColumn, int aSize)
	{
		// Bail if this column is not sorted
		Directive directive = getDirective(aColumn);
		if (directive == null)
			return null;

		// Synthesize new icons if the size differs from the cache
		if (aSize != cSize)
		{
			sortIconAsceL = ImmutableList.copyOf(refSortIconProvider.getIconsForSortAsce(aSize));
			sortIconDescL = ImmutableList.copyOf(refSortIconProvider.getIconsForSortDesc(aSize));
		}
		cSize = aSize;

		// Locate the proper sort icon list
		List<Icon> sortIconL = sortIconAsceL;
		if (directive.sortDir == SortDir.Descending)
			sortIconL = sortIconDescL;

		// Bail if there are no available sort icons
		if (sortIconL.size() == 0)
			return null;

		// Retrieve the priority
		int priority = sortingColumnL.indexOf(directive);
		if (priority < 0)
			priority = 0;
		else if (priority >= sortIconL.size())
			priority = sortIconL.size() - 1;

		// Return the proper icon corresponding to the priority
		return sortIconL.get(priority);
	}

	private int[] getModelToView()
	{
		if (modelToView == null)
		{
			int n = getViewToModel().length;
			modelToView = new int[n];
			for (int i = 0; i < n; i++)
			{
				modelToView[modelIndex(i)] = i;
			}
		}
		return modelToView;
	}

	private Row[] getViewToModel()
	{
		if (viewToModel == null)
		{
			int tableModelRowCount = refTableModel.getRowCount();
			viewToModel = new Row[tableModelRowCount];
			for (int row = 0; row < tableModelRowCount; row++)
			{
				viewToModel[row] = new Row(row);
			}

			if (isSorting())
			{
				Arrays.sort(viewToModel);
			}
		}
		return viewToModel;
	}

	/**
	 * Sets the {@link SortDir} for a specific column.
	 * <p>
	 * Note that this column will be sorted after any previously sorted column.
	 */
	private void setSortDir(int aColNum, SortDir aSortDir)
	{
		var directive = getDirective(aColNum);
		if (directive != null)
			sortingColumnL.remove(directive);

		if (aSortDir != SortDir.NotSorted)
			sortingColumnL.add(new Directive(aColNum, aSortDir));

		sortingStatusChanged();
	}

	/**
	 * Helper method that is triggered whenever any of the sort state changes.
	 * <p>
	 * Event notification will be sent out.
	 */
	private void sortingStatusChanged()
	{
		clearSortingState();
		fireTableDataChanged();
		if (refTableHeader != null)
			refTableHeader.repaint();
	}

	// Helper classes

	private class Row implements Comparable<Row>
	{
		// Attributes
		private final int modelIndex;

		/** Standard Constructor */
		public Row(int aIndex)
		{
			modelIndex = aIndex;
		}

		@Override
		@SuppressWarnings("unchecked")
		public int compareTo(Row aRow)
		{
			int row1 = modelIndex;
			int row2 = aRow.modelIndex;

			for (Directive aDirective : sortingColumnL)
			{
				int column = aDirective.column;
				Object o1 = refTableModel.getValueAt(row1, column);
				Object o2 = refTableModel.getValueAt(row2, column);

				int comparison = 0;
				// Define null less than everything, except null.
				if (o1 == null && o2 == null)
					comparison = 0;
				else if (o1 == null)
					comparison = -1;
				else if (o2 == null)
					comparison = 1;
				else
					comparison = ((Comparator<Object>) getComparator(column)).compare(o1, o2);

				if (comparison != 0)
					return aDirective.sortDir == SortDir.Descending ? -comparison : comparison;
			}
			return 0;
		}
	}

	private class TableModelHandler implements TableModelListener
	{
		@Override
		public void tableChanged(TableModelEvent e)
		{
			// If we're not sorting by anything, just pass the event along.
			if (!isSorting())
			{
				clearSortingState();
				fireTableChanged(e);
				return;
			}

			// If the table structure has changed, cancel the sorting; the
			// sorting columns may have been either moved or deleted from
			// the model.
			if (e.getFirstRow() == TableModelEvent.HEADER_ROW)
			{
				cancelSorting();
				fireTableChanged(e);
				return;
			}

			// We can map a cell event through to the view without widening
			// when the following conditions apply:
			//
			// a) all the changes are on one row (e.getFirstRow() ==
			// e.getLastRow()) and,
			// b) all the changes are in one column (column !=
			// TableModelEvent.ALL_COLUMNS) and,
			// c) we are not sorting on that column (getSortingStatus(column) ==
			// NOT_SORTED) and,
			// d) a reverse lookup will not trigger a sort (modelToView != null)
			//
			// Note: INSERT and DELETE events fail this test as they have column ==
			// ALL_COLUMNS.
			//
			// The last check, for (modelToView != null) is to see if modelToView
			// is already allocated. If we don't do this check; sorting can become
			// a performance bottleneck for applications where cells
			// change rapidly in different parts of the table. If cells
			// change alternately in the sorting column and then outside of
			// it this class can end up re-sorting on alternate cell updates -
			// which can be a performance problem for large tables. The last
			// clause avoids this problem.
			int column = e.getColumn();
			if (e.getFirstRow() == e.getLastRow() && column != TableModelEvent.ALL_COLUMNS
					&& getSortDir(column) == SortDir.NotSorted && modelToView != null)
			{
				int viewIndex = getModelToView()[e.getFirstRow()];
				fireTableChanged(new TableModelEvent(TableSorter.this, viewIndex, viewIndex, column, e.getType()));
				return;
			}

			// Something has happened to the data that may have invalidated the row
			// order.
			clearSortingState();
			fireTableDataChanged();
			return;
		}
	}

	private class MouseHandler extends MouseAdapter
	{
		@Override
		public void mouseClicked(MouseEvent aEvent)
		{
			// Bail if sorting is disabled
			if (isSortEnabled == false)
				return;

			// Bail if no view column
			JTableHeader h = (JTableHeader) aEvent.getSource();
			TableColumnModel columnModel = h.getColumnModel();
			int viewColumn = columnModel.getColumnIndexAtX(aEvent.getX());
			if (viewColumn == -1)
				return;

			// Bail if no model column
			int column = columnModel.getColumn(viewColumn).getModelIndex();
			if (column == -1)
				return;

			// Save off the current sort direction
			var prevSortDir = getSortDir(column);

			// Clear all sort status if CTRL or SHIFT are not pressed
			if (aEvent.isControlDown() == false && aEvent.isShiftDown() == false)
				cancelSorting();

			// Alternate between Ascending, Descending sorting
			var nextSortDir = SortDir.Ascending;
			if (prevSortDir == SortDir.Ascending)
				nextSortDir = SortDir.Descending;

			// If SHIFT is pressed then clear the sorting status
			if (aEvent.isShiftDown() == true)
				nextSortDir = SortDir.NotSorted;

			setSortDir(column, nextSortDir);
		}
	}

	private class SortableHeaderRenderer implements TableCellRenderer
	{
		// Attributes
		private final TableCellRenderer tableCellRenderer;

		/** Standard Constructor */
		public SortableHeaderRenderer(TableCellRenderer aTableCellRenderer)
		{
			tableCellRenderer = aTableCellRenderer;
		}

		@Override
		public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
				int row, int column)
		{
			var tmpComp = tableCellRenderer.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
			if (tmpComp instanceof JLabel aLabel)
			{
				aLabel.setHorizontalTextPosition(JLabel.LEFT);
				int modelColumn = table.convertColumnIndexToModel(column);
				aLabel.setIcon(getHeaderRendererIcon(modelColumn, aLabel.getFont().getSize()));
			}
			return tmpComp;
		}

	}

	/**
	 * Record that holds the state of a specific column and it's associated sort direction.
	 */
	private static record Directive(int column, SortDir sortDir)
	{
	}

}
