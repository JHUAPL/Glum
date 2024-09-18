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

import javax.swing.*;
import javax.swing.table.TableCellEditor;

/**
 * Cell editor suitable for editing boolean values.
 *
 * @author lopeznr1
 */
public class BooleanCellEditor extends AbstractCellEditor implements ActionListener, TableCellEditor
{
	// State vars
	private Collection<ActionListener> listenerL;
	private JCheckBox refCheckBox;

	/** Standard Constructor */
	public BooleanCellEditor(ActionListener aListener)
	{
		listenerL = new LinkedList<>();
		if (aListener != null)
			listenerL.add(aListener);

		refCheckBox = new JCheckBox("", false);
		refCheckBox.addActionListener(this);
		refCheckBox.setHorizontalAlignment(JCheckBox.CENTER);
	}

	/** Simplified Constructor */
	public BooleanCellEditor()
	{
		this(null);
	}

	public void addActionListener(ActionListener aListener)
	{
		listenerL.add(aListener);
	}

	public void removeActionListener(ActionListener aListener)
	{
		listenerL.remove(aListener);
	}

	@Override
	public void actionPerformed(ActionEvent aEvent)
	{
		fireEditingStopped();

		aEvent = new ActionEvent(this, aEvent.getID(), "BooleanCell edited.");
		for (ActionListener aListener : listenerL)
			aListener.actionPerformed(aEvent);
	}

	@Override
	public Component getTableCellEditorComponent(JTable table, Object value, boolean isSelected, int row, int column)
	{
		// Update our checkbox with the appropriate state
		refCheckBox.removeActionListener(this);
		if (value instanceof Boolean)
			refCheckBox.setSelected((Boolean) value);
		refCheckBox.addActionListener(this);

		return refCheckBox;
	}

	@Override
	public Object getCellEditorValue()
	{
		return refCheckBox.isSelected();
	}

}
