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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.TableColumn;

import glum.gui.GuiExeUtil;
import glum.gui.TableUtil;
import glum.gui.component.GComboBox;
import glum.gui.panel.itemList.query.QueryComposer;
import glum.gui.table.JTableScrolling;
import glum.gui.table.TableSorter;
import glum.item.*;
import net.miginfocom.swing.MigLayout;

/**
 * UI component that will display a list of items as provided by the specified {@link ItemHandler} and
 * {@link ItemProcessor}.
 *
 * @author lopeznr1
 */
public class ItemListPanel<G1, G2 extends Enum<?>> extends JPanel
		implements ActionListener, ItemEventListener, ListSelectionListener
{
	// Ref vars
	private final ItemProcessor<G1> refItemProcessor;

	// Gui vars
	private JTable myTable;
	private JScrollPane tableScrollPane;
	private TableSorter sortTableModel;
	private TableColumnHandler<G2> workTableColumnHandler;
	private ItemListTableModel<G1, G2> viewTableModel;
	private GComboBox<TableColumn> searchBox;
	private JTextField searchTF;

	// State vars
	private List<ListSelectionListener> listenerL;
	private boolean updateNeeded;

	/** Standard Constructor */
	public ItemListPanel(ItemHandler<G1, G2> aItemHandler, ItemProcessor<G1> aItemProcessor, QueryComposer<G2> aComposer,
			boolean aSupportsMultipleSelection)
	{
		// Delegate
		this(aItemHandler, aItemProcessor, aComposer, false, aSupportsMultipleSelection);
	}

	private ItemListPanel(ItemHandler<G1, G2> aItemHandler, ItemProcessor<G1> aItemProcessor,
			QueryComposer<G2> aComposer, boolean aHasSearchBox, boolean aSupportsMultipleSelection)
	{
		refItemProcessor = aItemProcessor;

		listenerL = new ArrayList<>();
		updateNeeded = true;

		// Form the gui
		buildGuiArea(aItemHandler, aComposer, aHasSearchBox, aSupportsMultipleSelection);
		GuiExeUtil.executeOnceWhenShowing(myTable, () -> updateTable());

		// Register for events of interest
		refItemProcessor.addListener(this);
	}

	/**
	 * Registers a ListSelectionListener with this ItemListPanel.
	 */
	public synchronized void addListSelectionListener(ListSelectionListener aListener)
	{
		listenerL.add(aListener);
	}

	/**
	 * Deregisters a ListSelectionListener with this ItemListPanel.
	 */
	public synchronized void delListSelectionListener(ListSelectionListener aListener)
	{
		listenerL.remove(aListener);
	}

	/**
	 * Returns the backing {@link TableColumnHandler}.
	 */
	public TableColumnHandler<G2> getTableColumnHandler()
	{
		return workTableColumnHandler;
	}

	/**
	 * Method to insert/replace an action for a specific keyboard shortcut
	 */
	public synchronized void setTableAction(KeyStroke aKeyStroke, Action aAction)
	{
		myTable.getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(aKeyStroke, aAction);
		myTable.getActionMap().put(aAction, aAction);
	}

	/**
	 * Returns the object located at the specified (view) row
	 */
	public synchronized G1 getItem(int aRow)
	{
		aRow = sortTableModel.modelIndex(aRow);
		if (aRow == -1)
			return null;

		var retObj = viewTableModel.getRowItem(aRow);
		return retObj;
	}

	/**
	 * Returns the first item that is selected from the table
	 */
	public synchronized G1 getSelectedItem()
	{
		// Ensure the table is up to date
		updateTable();

		int selectedRow = myTable.getSelectedRow();
		if (selectedRow == -1)
			return null;

		selectedRow = sortTableModel.modelIndex(selectedRow);
		if (selectedRow == -1)
			return null;

		G1 selectedObj = viewTableModel.getRowItem(selectedRow);
		return selectedObj;
	}

	/**
	 * Returns the list of selected items from the table
	 */
	public synchronized List<G1> getSelectedItems()
	{
		// Ensure the table is up to date
		updateTable();

		// Transform from rows to items
		var retItemL = new ArrayList<G1>();
		int[] idxArr = myTable.getSelectedRows();
		for (int aIdx : idxArr)
		{
			int tmpIdx = sortTableModel.modelIndex(aIdx);
			if (tmpIdx != -1)
			{
				G1 selectedObj = viewTableModel.getRowItem(tmpIdx);
				retItemL.add(selectedObj);
			}
		}

		return retItemL;
	}

	/**
	 * Returns the associated SortTableModel
	 */
	public TableSorter getSortTableModel()
	{
		return sortTableModel;
	}

	/**
	 * Method to set the selected item. This method will first ensure that myItemProcessor and myTable are synchronized
	 * via the method to updateTable. It is mandatory that this method is executed in the gui swing thread. Note that
	 * this method will not trigger an event for selection listeners.
	 */
	public synchronized void selectItem(G1 aObj)
	{
		// Ensure we are executed only on the proper thread
		if (SwingUtilities.isEventDispatchThread() == false)
			throw new RuntimeException("ItemListPanel.selectItem() not executed on the AWT event dispatch thread.");

		// Ensure the table is synchronized
		updateTable();

		// Stop listening to events
		myTable.getSelectionModel().removeListSelectionListener(this);

		// Select the object
		if (aObj == null)
		{
			myTable.getSelectionModel().clearSelection();
		}
		else
		{
			int chosenRow = viewTableModel.getRowIndex(aObj);
			if (chosenRow != -1)
			{
				chosenRow = sortTableModel.viewIndex(chosenRow);
				myTable.getSelectionModel().addSelectionInterval(chosenRow, chosenRow);

				// Ensure the row is visible
				if (JTableScrolling.isRowVisible(myTable, chosenRow) == false)
					JTableScrolling.centerRow(myTable, chosenRow);
			}
		}

		// Resume listening to events
		myTable.getSelectionModel().addListSelectionListener(this);

		// Time for a repaint
		myTable.repaint();
	}

	/**
	 * Ensures that the specified item is within the view.
	 */
	public synchronized void scrollToItem(G1 aItem)
	{
		// Transform from item to row index (view)
		int tmpRow = viewTableModel.getRowIndex(aItem);
		if (tmpRow == -1)
			return;

		tmpRow = sortTableModel.viewIndex(tmpRow);

		// Ensure the row is in the view
		if (JTableScrolling.isRowVisible(myTable, tmpRow) == false)
			JTableScrolling.centerRow(myTable, tmpRow);
	}

	/**
	 * Sets the table bodies color to aColor
	 */
	public synchronized void setTableBodyColor(Color aColor)
	{
		tableScrollPane.getViewport().setBackground(aColor);
	}

	/**
	 * Sets in the Comparator that will be used to sort the specified column.
	 */
	public void setSortComparator(int aColNum, Comparator<?> aComparator)
	{
		sortTableModel.setColumnIndexComparator(aColNum, aComparator);
	}

	/**
	 * Sets in the Comparator that will be used to sort items of type aType.
	 */
	public void setSortComparator(Class<?> aType, Comparator<?> aComparator)
	{
		sortTableModel.setColumnClassComparator(aType, aComparator);
	}

	/**
	 * Sets whether the table can be sorted
	 */
	public void setSortingEnabled(boolean aBool)
	{
		sortTableModel.setSortingEnabled(aBool);
	}

	@Override
	public void actionPerformed(ActionEvent aEvent)
	{
		// Determine the source
		var source = aEvent.getSource();
		if (source == searchTF || source == searchBox)
		{
			TableColumn selectedItem = searchBox.getChosenItem();
			selectNextItem(selectedItem, searchTF.getText());
		}
	}

	@Override
	public void handleItemEvent(Object aSource, ItemEventType aEventType)
	{
		// Time to update our selected items
		if (aEventType == ItemEventType.ItemsSelected)
			updateTableSelection();
		// Nothing to do just a repaint is needed
		else if (aEventType == ItemEventType.ItemsMutated)
			myTable.repaint();
		// Mark the table as being outdated
		else if (aEventType == ItemEventType.ItemsChanged)
			updateNeeded = true;

		// The advantage to the code below (as opposed to SwingUtilities.invokeLater()
		// style) is that it is only updated when it absolutely is necessary. Thus if
		// multiple updates come in before it is repainted they will be ignored. In
		// the future this method may have an argument called isLazy which allow both
		// styles of updating.
		repaint();
	}

	/**
	 * This may be triggered indirectly via a network call after the method repaint() has been called. Do not call this
	 * method from a non gui thread
	 */
	@Override
	public void paint(Graphics g)
	{
		// Ensure the table is up to date
		updateTable();

		// Do the actual paint
		if (g != null)
			super.paint(g);
	}

	@Override
	public void setEnabled(boolean aBool)
	{
		myTable.setEnabled(aBool);
	}

	@Override
	public void valueChanged(ListSelectionEvent aEvent)
	{
		if (refItemProcessor instanceof ItemManager == false)
		{
			notifyListeners(aEvent);
			return;
		}

		// TODO: All ItemProcessor may go away and we interface with ItemManager
		var tmpManager = (ItemManager<G1>) refItemProcessor;

		// Update the ItemManager's selection
		tmpManager.delListener(this);
		tmpManager.setSelectedItems(getSelectedItems());
		tmpManager.addListener(this);

		notifyListeners(aEvent);
	}

	/**
	 * Method to provide raw access to the underlying JTable
	 */
	public JTable getTable()
	{
		return myTable;
	}

	/**
	 * Forms the actual panel GUI
	 */
	private void buildGuiArea(ItemHandler<G1, G2> aItemHandler, QueryComposer<G2> aComposer, boolean aHasSearchBox,
			boolean aSupportsMultipleSelection)
	{
		if (aHasSearchBox == true)
			setLayout(new MigLayout("", "0[][][grow]0", "0[grow][]0"));
		else
			setLayout(new MigLayout("", "0[][][grow]0", "0[grow]0"));

		// Form the table
		workTableColumnHandler = new TableColumnHandler<>(aComposer.getItems());
		viewTableModel = new ItemListTableModel<>(aItemHandler, workTableColumnHandler);
		sortTableModel = new TableSorter(viewTableModel);
		myTable = new JTable(sortTableModel);
		sortTableModel.setTableHeader(myTable.getTableHeader());

		myTable.setBackground(null);
		myTable.getSelectionModel().addListSelectionListener(this);
		if (aSupportsMultipleSelection == false)
			myTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		else
			myTable.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		// Notify the TableModel of the associated table (and initialize the table)
		viewTableModel.initialize(myTable);

		// Create the scroll pane and add the table to it.
		tableScrollPane = new JScrollPane(myTable);
		add(tableScrollPane, "growx,growy,span");

		// The search section
		searchBox = null;
		searchTF = null;
		if (aHasSearchBox == true)
		{
			var tmpL = new JLabel("Find:");
			add(tmpL, "newline");

			searchBox = new GComboBox<TableColumn>(this, new SearchBoxRenderer());
			searchBox.setMaximumSize(searchBox.getPreferredSize());
			add(searchBox, "");

			searchTF = new JTextField("");
			searchTF.addActionListener(this);
			add(searchTF, "growx,span");

			// Set in the preferred font
			tmpL.setFont(searchTF.getFont());
			searchBox.setFont(searchTF.getFont());
		}

		// Set the default preferred size of the ListPanel
		setPreferredSize(new Dimension(myTable.getPreferredSize().width, 150));
	}

	/**
	 * notifyListeners
	 */
	protected void notifyListeners(ListSelectionEvent aEvent)
	{
		List<ListSelectionListener> tmpL;
		synchronized (this)
		{
			tmpL = new ArrayList<>(listenerL);
		}

		// Notify our listeners
		var tmpEvent = new ListSelectionEvent(this, aEvent.getFirstIndex(), aEvent.getLastIndex(),
				aEvent.getValueIsAdjusting());
		for (ListSelectionListener aListener : tmpL)
			aListener.valueChanged(tmpEvent);
	}

	/**
	 * rebuildItemList
	 */
	protected synchronized void rebuildItemList()
	{
		// Insanity check
		if (refItemProcessor == null)
			return;

		// Get the old selected items
		var selectedL = new ArrayList<G1>();
		int[] idxArr = myTable.getSelectedRows();
		for (int aInt : idxArr)
		{
			int tmpIdx = sortTableModel.modelIndex(aInt);
			selectedL.add(viewTableModel.getRowItem(tmpIdx));
		}

		// Suspend listening to selection change events
		myTable.getSelectionModel().removeListSelectionListener(this);

		// Update our table with the new set of items
		var itemL = refItemProcessor.getAllItems();
		viewTableModel.clear();
		viewTableModel.addItems(itemL);

		// Determine the row indexes to be selected
		var tmpRowL = new ArrayList<Integer>();
		for (G1 aObj : selectedL)
		{
			int tmpRow = viewTableModel.getRowIndex(aObj);
			if (tmpRow == -1)
				continue;

			tmpRow = sortTableModel.viewIndex(tmpRow);
			tmpRowL.add(tmpRow);
		}

		// Select the appropriate rows
		TableUtil.setSelection(myTable, null, tmpRowL);

		// Restore listening to selection change events
		myTable.getSelectionModel().addListSelectionListener(this);
	}

	/**
	 * Rebuild the searchBox so that it contains all of the table columns.
	 */
	protected synchronized void rebuildSearchBox()
	{
		// Insanity check
		if (searchBox == null)
			return;

		// Save off the currently selected object
		var selectedItem = searchBox.getChosenItem();

		// Reconstitude searchBox
		var numCols = myTable.getColumnCount();
		searchBox.removeAllItems();
		for (int c1 = 0; c1 < numCols; c1++)
		{
			TableColumn tmpTC = myTable.getTableHeader().getColumnModel().getColumn(c1);

			// Allow items to be searched if derived from JLabel
			// TODO: In the future this should have a searchable flag rather
			// this hack that checks if this is a child of JLabel
			var tmpTCR = tmpTC.getCellRenderer();
			if (tmpTCR instanceof JLabel)
				searchBox.addItem(tmpTC);
		}

		// Set up the searchBox to appropriate state
		searchBox.removeActionListener(this);
		if (selectedItem != null)
			searchBox.setSelectedItem(selectedItem);
		searchBox.addActionListener(this);

		searchBox.setMaximumSize(searchBox.getPreferredSize());
	}

	/**
	 * Utility to locate the next item to be selected, and change the section to that such item
	 */
	protected synchronized void selectNextItem(TableColumn aTableColumn, String aSearchStr)
	{
		if (aTableColumn == null || aSearchStr == null)
			return;

		// Retrieve the model index and table renderer
		var aRenderer = aTableColumn.getCellRenderer();
		var colNum = aTableColumn.getModelIndex();

		// Is a hard match required
		var hardMatchRequired = false;
		if (aSearchStr.endsWith(" ") == true)
		{
			hardMatchRequired = true;
			aSearchStr = aSearchStr.substring(0, aSearchStr.length() - 1);
		}

		var startRow = myTable.getSelectedRow();
		if (startRow == -1)
			startRow = 0;
		else
			startRow++;

		var numRows = sortTableModel.getRowCount();
		if (numRows == 0)
			return;

		// Search lower half of table
		var chosenRow = -1;
		for (var cX = startRow; cX < numRows; cX++)
		{
			var tmpObj = sortTableModel.getValueAt(cX, colNum);
			if (tmpObj == null)
				continue;

			var tmpL = (JLabel) aRenderer.getTableCellRendererComponent(myTable, tmpObj, false, false, cX, colNum);
			var currStr = tmpL.getText();
			if (currStr == null)
				continue;

			if (hardMatchRequired == true)
			{
				if (currStr.equals(aSearchStr) == true)
				{
					chosenRow = cX;
					break;
				}
			}
			else if (currStr.contains(aSearchStr) == true)
			{
				chosenRow = cX;
				break;
			}
		}

		// Search upper half of table
		if (chosenRow == -1)
		{
			for (var cX = 0; cX < startRow; cX++)
			{
				var tmpObj = sortTableModel.getValueAt(cX, colNum);
				if (tmpObj == null)
					continue;

				var tmpL = (JLabel) aRenderer.getTableCellRendererComponent(myTable, tmpObj, false, false, cX, colNum);
				var currStr = tmpL.getText();
				if (currStr == null)
					continue;

				if (hardMatchRequired == true)
				{
					if (currStr.equals(aSearchStr) == true)
					{
						chosenRow = cX;
						break;
					}
				}
				else if (currStr.contains(aSearchStr) == true)
				{
					chosenRow = cX;
					break;
				}
			}
		}

		if (chosenRow != -1)
		{
			myTable.getSelectionModel().setSelectionInterval(chosenRow, chosenRow);

			// Ensure the row is visible
			if (JTableScrolling.isRowVisible(myTable, chosenRow) == false)
			{
				JTableScrolling.centerRow(myTable, chosenRow);
			}
		}
	}

	/**
	 * Utility method to execute the actual synchronization of the JTable with myItemProcessor. Note that this will
	 * coalesce multiple update requests into one (via the var updateNeeded).
	 */
	protected synchronized void updateTable()
	{
		// Bail if an update is no longer needed.
		if (updateNeeded == false)
			return;
//System.out.println("ItemListPanel.updateTable() Addr:" + this.hashCode());

//		// Ensure we are executed only on the proper thread
//		if (SwingUtilities.isEventDispatchThread() == false)
//			throw new RuntimeException("ItemListPanel.updateTable() not executed on the AWT event dispatch thread.");

		// Perform the actual synchronization
		rebuildItemList();
		rebuildSearchBox();

		// Mark any future (already scheduled) requests as filled.
		updateNeeded = false;
	}

	/**
	 * Helper method to update the table selection to match the state of the ItemManager.
	 * <p>
	 * TODO: In the future this method may go away ItemProcessor and ItemManager are merged.
	 */
	private void updateTableSelection()
	{
		// Ensure the table is up to date
		updateTable();

		if (refItemProcessor instanceof ItemManager)
			TableUtil.updateTableSelection(this, (ItemManager<?>) refItemProcessor, myTable, sortTableModel);
	}

}
