package glum.gui.misc;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.LinkedList;
import javax.swing.AbstractCellEditor;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class MultiStateCheckBoxCellEditor extends AbstractCellEditor implements ActionListener, TableCellEditor
{
	// State vars
	protected Collection<ActionListener> myListeners;
	protected MultiStateCheckBox refMultiStateCheckBox;

	/**
	 * Constructor
	 */
	public MultiStateCheckBoxCellEditor()
	{
		myListeners = new LinkedList<ActionListener>();
		refMultiStateCheckBox = new MultiStateCheckBox("", false);
		refMultiStateCheckBox.addActionListener(this);
	}

	public void addActionListener(ActionListener aListener)
	{
		myListeners.add(aListener);
	}

	public void removeActionListener(ActionListener aListener)
	{
		myListeners.remove(aListener);
	}

	@Override
	public void actionPerformed(ActionEvent aEvent)
	{
		fireEditingStopped();

		aEvent = new ActionEvent(this, aEvent.getID(), "MultiStateCheckBoxCell edited.");
		for (ActionListener aListener : myListeners)
			aListener.actionPerformed(aEvent);
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		// Update our checkbox with the appropriate state
		refMultiStateCheckBox.removeActionListener(this);
		if (value instanceof MultiState)
			refMultiStateCheckBox.setState((MultiState)value);
		else if (value instanceof Boolean)
			refMultiStateCheckBox.setSelected((Boolean)value);
		refMultiStateCheckBox.addActionListener(this);

		return refMultiStateCheckBox;
	}

	@Override
	public Object getCellEditorValue()
	{
		return refMultiStateCheckBox.getState();
	}

}
