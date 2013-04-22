package glum.gui.panel.itemList;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.table.*;

import glum.gui.component.GComboBox;
import glum.gui.table.*;

import com.google.common.collect.*;
import net.miginfocom.swing.MigLayout;

public class ItemListPanel<G1> extends JPanel implements ActionListener, ListSelectionListener, ItemChangeListener
{
	// Gui components
	protected JTable myTable;
	protected JScrollPane tableScrollPane;
	protected ItemListTableModel<G1> myTableModel;
	protected TableSorter sortTableModel;
	protected GComboBox<TableColumn> searchBox;
	protected JTextField searchTF;

	// State vars
	protected ItemHandler<G1> myItemHandler;
	protected ItemProcessor<G1> myItemProcessor;
	protected boolean updateNeeded;

	// Communicator vars
	protected List<ListSelectionListener> myListeners;

	public ItemListPanel(ItemHandler<G1> aItemHandler, ItemProcessor<G1> aItemProcessor, boolean hasSearchBox, boolean supportsMultipleSelection)
	{
		// State vars
		myItemHandler = aItemHandler;
		myItemProcessor = aItemProcessor;
		updateNeeded = true;

		// Communicator vars
		myListeners = Lists.newLinkedList();

		// Build the actual GUI
		buildGuiArea(hasSearchBox, supportsMultipleSelection);

		// Register for DataChange events and trigger the initial one
		myItemProcessor.addItemChangeListener(this);
	}

	/**
	 * addListSelectionListener
	 */
	public synchronized void addListSelectionListener(ListSelectionListener aListener)
	{
		myListeners.add(aListener);
	}

	/**
	 * removeListSelectionListener
	 */
	public synchronized void removeListSelectionListener(ListSelectionListener aListener)
	{
		myListeners.remove(aListener);
	}

	/**
	 * Method to insert/replace an action for a specific keyboard shortcut
	 */
	public synchronized void setTableAction(KeyStroke aKeyStroke, Action aAction)
	{
		myTable.getInputMap(WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(aKeyStroke, aAction);
		myTable.getActionMap().put(aAction, aAction);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object source;
		TableColumn selectedItem;

		// Determine the source
		source = e.getSource();

		if (source == searchTF || source == searchBox)
		{
			selectedItem = searchBox.getChosenItem();
			selectNextItem(selectedItem, searchTF.getText());
		}
	}

	/**
	 * Returns the object located at the specified (view) row
	 */
	public synchronized G1 getItem(int aRow)
	{
		G1 aObj;

		aRow = sortTableModel.modelIndex(aRow);
		if (aRow == -1)
			return null;

		aObj = myTableModel.getRowItem(aRow);
		return aObj;
	}

	/**
	 * Returns the first item that is selected from the table
	 */
	public synchronized G1 getSelectedItem()
	{
		G1 selectedObj;
		int selectedRow;

		// Ensure the table is up to date
		updateTable();

		selectedRow = myTable.getSelectedRow();
		if (selectedRow == -1)
			return null;

		selectedRow = sortTableModel.modelIndex(selectedRow);
		if (selectedRow == -1)
			return null;

		selectedObj = myTableModel.getRowItem(selectedRow);
		return selectedObj;
	}

	/**
	 * getSelectedItems - Returns the list of selected items from the table
	 */
	public synchronized List<G1> getSelectedItems()
	{
		List<G1> aList;
		G1 selectedObj;
		int[] selectedRows;
		int selectedRow;

		// Ensure the table is up to date
		updateTable();

		aList = Lists.newLinkedList();
		selectedRows = myTable.getSelectedRows();
		if (selectedRows != null)
		{
			for (int aInt : selectedRows)
			{
				selectedRow = sortTableModel.modelIndex(aInt);
				if (selectedRow != -1)
				{
					selectedObj = myTableModel.getRowItem(selectedRow);
					aList.add(selectedObj);
				}
			}
		}

		return aList;
	}

	@Override
	public void itemChanged()
	{
		// Mark the table as being outdated
		updateNeeded = true;

		// The advantage to the code below (as opposed to SwingUtilities.invokeLater() style) is
		// that it is only updated when it absolutely is necessary. Thus if multiple updates come
		// in before it is repainted they will be ignored. In the future this method may have an
		// argument called isLazy which allow both styles of updating.
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

	/**
	 * Method to set the selected item. This method will first ensure that myItemProcessor and myTable are synchronized
	 * via the method to updateTable. It is mandatory that this method is executed in the gui swing thread. Note that
	 * this method will not trigger an event for selection listeners.
	 */
	public synchronized void selectItem(G1 aObj)
	{
		int chosenRow;

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
			chosenRow = myTableModel.getRowIndex(aObj);
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
	 * Sets the table bodies color to aColor
	 */
	public synchronized void setTableBodyColor(Color aColor)
	{
		tableScrollPane.getViewport().setBackground(aColor);
	}

	/**
	 * Sets whether the table can be sorted
	 */
	public void setSortingEnabled(boolean aBool)
	{
		sortTableModel.setSortingEnabled(aBool);
	}

	@Override
	public void setEnabled(boolean aBool)
	{
		myTable.setEnabled(aBool);
	}

	@Override
	public void valueChanged(ListSelectionEvent aEvent)
	{
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
	private void buildGuiArea(boolean hasSearchBox, boolean supportsMultipleSelection)
	{
		JLabel tmpL;

		if (hasSearchBox == true)
			setLayout(new MigLayout("", "0[][][grow]0", "0[grow][]0"));
		else
			setLayout(new MigLayout("", "0[][][grow]0", "0[grow]0"));

		// Form the table
		myTableModel = new ItemListTableModel<G1>(myItemHandler);
		sortTableModel = new TableSorter(myTableModel);
		myTable = new JTable(sortTableModel);
		sortTableModel.setTableHeader(myTable.getTableHeader());
//!		sortTableModel.setSortingStatus(0, 1);

		myTable.setBackground(null);
		myTable.getSelectionModel().addListSelectionListener(this);
		if (supportsMultipleSelection == false)
			myTable.getSelectionModel().setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		else
			myTable.getSelectionModel().setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);

		// Notify the ItemHandler of its associated table and initialize the table
		myItemHandler.initialize(myTable);

		// Create the scroll pane and add the table to it.
		tableScrollPane = new JScrollPane(myTable);
		add(tableScrollPane, "growx,growy,span 3");

		// The search section
		searchBox = null;
		searchTF = null;
		if (hasSearchBox == true)
		{
			tmpL = new JLabel("Find:");
			add(tmpL, "newline,span 1");

			searchBox = new GComboBox<TableColumn>(this, new SearchBoxRenderer());
			searchBox.setMaximumSize(searchBox.getPreferredSize());
			add(searchBox, "span 1");

			searchTF = new JTextField("");
			searchTF.addActionListener(this);
			add(searchTF, "growx,span 1");

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
		List<ListSelectionListener> tmpList;
		ListSelectionEvent tmpEvent;

		synchronized(this)
		{
			tmpList = Lists.newArrayList(myListeners);
		}

		// Notify our listeners
		tmpEvent = new ListSelectionEvent(this, aEvent.getFirstIndex(), aEvent.getLastIndex(), aEvent.getValueIsAdjusting());
		for (ListSelectionListener aListener : tmpList)
			aListener.valueChanged(tmpEvent);
	}

	/**
	 * rebuildItemList
	 */
	protected synchronized void rebuildItemList()
	{
		Collection<? extends G1> itemList;
		Collection<G1> selectedObjSet;
		int[] selectedRows;
		int aRow;

		// Insanity check
		if (myItemProcessor == null)
			return;

		// Get the old selected items
		selectedObjSet = new LinkedList<G1>();
		selectedRows = myTable.getSelectedRows();
		if (selectedRows != null)
		{
			for (int aInt : selectedRows)
			{
				aRow = sortTableModel.modelIndex(aInt);
				selectedObjSet.add(myTableModel.getRowItem(aRow));
			}
		}

		// Suspend listening to selection change events
		myTable.getSelectionModel().removeListSelectionListener(this);

		// Update our table with the new set of items
		itemList = myItemProcessor.getItems();
		myTableModel.clear();
		myTableModel.addItems(itemList);

		// Reselect the old selected items
		myTable.getSelectionModel().clearSelection();
		for (G1 aObj : selectedObjSet)
		{
			aRow = myTableModel.getRowIndex(aObj);
			if (aRow != -1)
			{
				aRow = sortTableModel.viewIndex(aRow);
				myTable.getSelectionModel().addSelectionInterval(aRow, aRow);
			}
		}

		// Restore listening to selection change events
		myTable.getSelectionModel().addListSelectionListener(this);
	}

	/**
	 * Rebuild the searchBox so that it contains all of the table columns.
	 */
	protected synchronized void rebuildSearchBox()
	{
		TableColumn selectedItem;
		int numCols;

		// Insanity check
		if (searchBox == null)
			return;

		// Save off the currently selected object
		selectedItem = searchBox.getChosenItem();

		// Reconstitude searchBox
		numCols = myTable.getColumnCount();
		searchBox.removeAllItems();
		for (int c1 = 0; c1 < numCols; c1++)
			searchBox.addItem(myTable.getTableHeader().getColumnModel().getColumn(c1));

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
	protected synchronized void selectNextItem(TableColumn aTableColumn, String searchStr)
	{
		TableCellRenderer aRenderer;
		JLabel tmpL;
		Object aObj;
		String currStr;
		int colNum, chosenRow, startRow, numRows;
		int cX;
		boolean hardMatchRequired;

		if (aTableColumn == null || searchStr == null)
			return;

		// Retrieve the model index and table renderer
		aRenderer = aTableColumn.getCellRenderer();
		colNum = aTableColumn.getModelIndex();

		// Is a hard match required
		hardMatchRequired = false;
		if (searchStr.endsWith(" ") == true)
		{
			hardMatchRequired = true;
			searchStr = searchStr.substring(0, searchStr.length() - 1);
		}

		startRow = myTable.getSelectedRow();
		if (startRow == -1)
			startRow = 0;
		else
			startRow++;

		numRows = sortTableModel.getRowCount();
		if (numRows == 0)
			return;

		// Search lower half of table
		chosenRow = -1;
		for (cX = startRow; cX < numRows; cX++)
		{
			aObj = sortTableModel.getValueAt(cX, colNum);
			if (aObj != null)
			{

				tmpL = (JLabel)aRenderer.getTableCellRendererComponent(myTable, aObj, false, false, cX, colNum);
				currStr = tmpL.getText();
				if (currStr != null)
				{
					if (hardMatchRequired == true)
					{
						if (currStr.equals(searchStr) == true)
						{
							chosenRow = cX;
							break;
						}
					}
					else if (currStr.startsWith(searchStr) == true)
					{
						chosenRow = cX;
						break;
					}
				}
			}
		}

		// Search upper half of table
		if (chosenRow == -1)
		{
			for (cX = 0; cX < startRow; cX++)
			{
				aObj = sortTableModel.getValueAt(cX, colNum);
				if (aObj != null)
				{
					tmpL = (JLabel)aRenderer.getTableCellRendererComponent(myTable, aObj, false, false, cX, colNum);
					currStr = tmpL.getText();
					if (currStr != null)
					{
						if (hardMatchRequired == true)
						{
							if (currStr.equals(searchStr) == true)
							{
								chosenRow = cX;
								break;
							}
						}
						else if (currStr.startsWith(searchStr) == true)
						{
							chosenRow = cX;
							break;
						}
					}
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
		// If an update was originally scheduled, this will be false
		// due to coalesion.
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

}
