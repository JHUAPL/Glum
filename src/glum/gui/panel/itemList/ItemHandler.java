package glum.gui.panel.itemList;

import java.util.*;

import javax.swing.*;

public interface ItemHandler<G1>
{
	/**
	* ItemHandler interface methods
	*/
	public int getColumnCount();
	public Class<?> getColumnClass(int colNum);
	public String getColumnLabel(int colNum);
	public Collection<String> getColumnLabels();
	public Object getColumnValue(G1 aItem, int colNum);
	public void setColumnValue(G1 aItem, int colNum, Object aValue);
	public boolean isCellEditable(int colNum);
	public boolean isColumnVisible(int colNum);

	/**
	 * Notifies the ItemHandler of its associated JTable This table should be updated or painted whenever
	 * the internals of this ItemHandler change
	 */
	public void initialize(JTable aOwner);
	
	/**
	 * Notifies the ItemHandler to synchronize the items to match the state of the associated Columns
	 */
//	public void synchItems();
}
