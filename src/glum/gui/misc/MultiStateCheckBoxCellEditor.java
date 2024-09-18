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
