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
package glum.gui.table;

import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.event.TableModelListener;
import javax.swing.table.TableModel;

/**
 * Base class used to define a TableModel. This class provides for handling of each columns type and title.
 *
 * @author lopeznr1
 */
public abstract class BaseTableModel implements TableModel
{
	// Attributes
	private final Class<?>[] classArr;
	private final String[] nameArr;

	// State vars
	private List<TableModelListener> listenerL;

	/**
	 * Standard Constructor
	 */
	public BaseTableModel(Class<?>[] aClassArr, String[] aNameArr)
	{
		classArr = aClassArr;
		nameArr = aNameArr;
		if (classArr.length != nameArr.length)
			throw new RuntimeException("Parameters aClassArr and aNameArr must be the same length!");

		listenerL = new ArrayList<>();
	}

	@Override
	public void addTableModelListener(TableModelListener aListener)
	{
		listenerL.add(aListener);
	}

	@Override
	public void removeTableModelListener(TableModelListener aListener)
	{
		listenerL.remove(aListener);
	}

	@Override
	public Class<?> getColumnClass(int aColIndex)
	{
		return classArr[aColIndex];
	}

	@Override
	public int getColumnCount()
	{
		return classArr.length;
	}

	@Override
	public String getColumnName(int aColIndex)
	{
		return nameArr[aColIndex];
	}

	/**
	 * Helper method to send out notification to the registered listeners.
	 */
	protected void notifyListeners()
	{
		TableModelEvent tmpEvent = new TableModelEvent(this);
		for (TableModelListener aListener : listenerL)
			aListener.tableChanged(tmpEvent);
	}

}
