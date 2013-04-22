package glum.gui.misc;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.LinkedList;
import javax.swing.AbstractCellEditor;
import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellEditor;

public class BooleanCellEditor extends AbstractCellEditor implements ActionListener, TableCellEditor
{
	// State vars
	protected Collection<ActionListener> myListeners;
	protected JCheckBox refCheckBox;

	/**
	 * Constructor
	 */
	public BooleanCellEditor()
	{
		this(null);
	}

	public BooleanCellEditor(ActionListener aListener)
	{
		myListeners = new LinkedList<ActionListener>();
		if (aListener != null)
			myListeners.add(aListener);

		refCheckBox = new JCheckBox("", false);
		refCheckBox.addActionListener(this);
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

		aEvent = new ActionEvent(this, aEvent.getID(), "BooleanCell edited.");
		for (ActionListener aListener : myListeners)
			aListener.actionPerformed(aEvent);
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		// Update our checkbox with the appropriate state
		refCheckBox.removeActionListener(this);
		if (value instanceof Boolean)
			refCheckBox.setSelected((Boolean)value);
		refCheckBox.addActionListener(this);

		return refCheckBox;
	}

	@Override
	public Object getCellEditorValue()
	{
		return refCheckBox.isSelected();
	}

}
