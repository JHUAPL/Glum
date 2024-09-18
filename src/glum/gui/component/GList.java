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
package glum.gui.component;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import glum.gui.component.model.GListModel;
import glum.gui.panel.generic.GenericCodes;

public class GList<G1> extends JComponent implements GenericCodes, ListSelectionListener
{
	// Gui vars
	private JList<G1> refList;
	private GListModel<G1> refModel;

	// State vars
	private ActionListener refActionListener;
	private ListSelectionListener refListSelectionListener;

	public GList(Object aListener, List<G1> aItemList)
	{
		refActionListener = null;
		refListSelectionListener = null;

		refModel = new GListModel<>(aItemList);
		refList = new JList<>(refModel);
		buildGui(aListener);
	}

//	public GList(Object aListener, G1... aItemArr)
//	{
//		refActionListener = null;
//		refListSelectionListener = null;
//
//		refModel = new GListModel<G1>(aItemArr);
//		refList = new JList<>(refModel);
//		buildGui(aListener);
//	}
//
	/**
	 * Returns the (first) selected item
	 */
	public G1 getSelectedItem()
	{
		var selectedIndex = refList.getSelectedIndex();
		if (selectedIndex == -1)
			return null;

		return refModel.getElementAt(selectedIndex);
	}

	/**
	 * Returns all of the selected items
	 */
	public List<G1> getSelectedItems()
	{
		var indexArr = refList.getSelectedIndices();

		var retItemL = new ArrayList<G1>();
		for (int aIndex : indexArr)
			retItemL.add(refModel.getElementAt(aIndex));

		retItemL.trimToSize();
		return retItemL;
	}

	/**
	 * Replaces all of the items in the list with aItemList.
	 */
	public void setItems(Collection<G1> aItemList)
	{
		refList.removeListSelectionListener(this);
		refList.setModel(new GListModel<G1>(aItemList));
		refList.addListSelectionListener(this);
	}

	/**
	 * Sets aItem as the selected item.
	 */
	public void setSelectedItem(G1 aItem)
	{
		refList.removeListSelectionListener(this);
		refList.setSelectedIndex(refModel.indexOf(aItem));
		refList.addListSelectionListener(this);
	}

	/**
	 * Sets all of the items in aItemList as selected
	 */
	public void setSelectedItems(List<G1> aItemList)
	{
		// Ensure we are executed only on the proper thread
		if (SwingUtilities.isEventDispatchThread() == false)
			throw new RuntimeException("GList.selectItems() not executed on the AWT event dispatch thread.");

		refList.removeListSelectionListener(this);

		var c1 = 0;
		var idArr = new int[aItemList.size()];
		for (G1 aItem : aItemList)
		{
			idArr[c1] = refModel.indexOf(aItem);
			c1++;
		}

		refList.setSelectedIndices(idArr);

		refList.addListSelectionListener(this);
	}

	@Override
	public void setEnabled(boolean aBool)
	{
		refList.setEnabled(aBool);
	}

	@Override
	public void valueChanged(ListSelectionEvent aEvent)
	{
		if (refListSelectionListener != null)
			refListSelectionListener.valueChanged(new ListSelectionEvent(this, aEvent.getFirstIndex(), aEvent.getLastIndex(), aEvent.getValueIsAdjusting()));

		if (refActionListener != null)
			refActionListener.actionPerformed(new ActionEvent(this, ID_UPDATE, "update"));
	}

	/**
	 * Helper method used to build the GUI
	 */
	private void buildGui(Object aListener)
	{
		if (aListener instanceof ActionListener)
			refActionListener = (ActionListener)aListener;
		if (aListener instanceof ListSelectionListener)
			refListSelectionListener = (ListSelectionListener)aListener;

		setLayout(new BorderLayout());
		add(refList);
	}

}
