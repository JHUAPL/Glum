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

import java.awt.event.ActionListener;
import java.util.Collection;
import java.util.List;

import javax.swing.JComboBox;
import javax.swing.ListCellRenderer;

import com.google.common.collect.ImmutableList;

import glum.gui.component.model.GComboBoxModel;

/**
 * Enhanced implementation of {@link JComboBox}. This implementation provides for better generics support. Please
 * utilize the {@link #getChosenItem()}, {@link #setChosenItem(Object)}, etc...
 *
 * @author lopeznr1
 */
public class GComboBox<G1> extends JComboBox<G1>
{
	// State vars
	private GComboBoxModel<G1> itemModel;

	public GComboBox()
	{
		itemModel = new GComboBoxModel<G1>(ImmutableList.of());
		setModel(itemModel);
	}

	public GComboBox(ActionListener aListener, ListCellRenderer<? super G1> aRenderer)
	{
		this();

		addActionListener(aListener);
		setRenderer(aRenderer);
	}

	public GComboBox(ActionListener aListener)
	{
		this();

		addActionListener(aListener);
	}

	public GComboBox(ActionListener aListener, Collection<G1> aItemC)
	{
		itemModel = new GComboBoxModel<>(aItemC);
		setModel(itemModel);

		addActionListener(aListener);
	}

	public GComboBox(ActionListener aListener, G1... aItemArr)
	{
		itemModel = new GComboBoxModel<>(aItemArr);
		setModel(itemModel);

		addActionListener(aListener);
	}

	/**
	 * Returns the list of all items stored in the GComboBox
	 */
	public List<G1> getAllItems()
	{
		return itemModel.getAllItems();
	}

	/**
	 * Returns the items that is currently selected.
	 */
	public G1 getChosenItem()
	{
		return itemModel.getSelectedItem();
	}

	/**
	 * Sets in the currently selected item. This method will not trigger an ActionEvent.
	 *
	 * @see JComboBox#setSelectedItem
	 */
	public void setChosenItem(G1 aItem)
	{
		var listenerArr = getActionListeners();
		for (ActionListener aListener : listenerArr)
			removeActionListener(aListener);

		itemModel.setSelectedItem(aItem);
		super.setSelectedItem(aItem);

		for (ActionListener aListener : listenerArr)
			addActionListener(aListener);

		// We must force a repaint since any ActionListener responsible will never get the update
		repaint();
	}

	/**
	 * Note aItem must be of the generified type. This method will not trigger an ActionEvent.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void addItem(Object aItem)
	{
		ActionListener[] listenerArr;

		listenerArr = getActionListeners();
		for (ActionListener aListener : listenerArr)
			removeActionListener(aListener);

		itemModel.addItem((G1) aItem);

		for (ActionListener aListener : listenerArr)
			addActionListener(aListener);
	}

	/**
	 * Note aItem must be of the generified type. This method will not trigger an ActionEvent.
	 */
	public void addItems(G1... aItemArr)
	{
		var listenerArr = getActionListeners();
		for (ActionListener aListener : listenerArr)
			removeActionListener(aListener);

		for (G1 aItem : aItemArr)
			itemModel.addItem(aItem);

		for (ActionListener aListener : listenerArr)
			addActionListener(aListener);
	}

	/**
	 * Adds all of the specified items to the model. This method will not trigger an ActionEvent.
	 */
	public void addItems(Collection<G1> aItemC)
	{
		var listenerArr = getActionListeners();
		for (ActionListener aListener : listenerArr)
			removeActionListener(aListener);

		for (G1 aItem : aItemC)
			itemModel.addItem(aItem);

		for (ActionListener aListener : listenerArr)
			addActionListener(aListener);
	}

	/**
	 * Note aItem must be of the generified type. This method will not trigger an ActionEvent.
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void removeItem(Object aItem)
	{
		var listenerArr = getActionListeners();
		for (ActionListener aListener : listenerArr)
			removeActionListener(aListener);

		itemModel.removeItem((G1) aItem);

		for (ActionListener aListener : listenerArr)
			addActionListener(aListener);
	}

	/**
	 * Method to replace all of the items with the specified list of items.
	 * <p>
	 * If the current selection is in the replacement list then the selection will be maintained otherwise it will be
	 * replaced with the first item in the list.
	 * <p>
	 * This method will not trigger an ActionEvent.
	 */
	public void replaceAllItems(Collection<G1> aItemC)
	{
		G1 pickItem = getChosenItem();

		var listenerArr = getActionListeners();
		for (ActionListener aListener : listenerArr)
			removeActionListener(aListener);

		// Replace the items
		itemModel.removeAllItems();

		for (G1 aItem : aItemC)
			itemModel.addItem(aItem);

		// Update the selection after the replacement
		if (aItemC.contains(pickItem) == false)
			pickItem = null;
		if (pickItem == null && aItemC.size() > 0)
			pickItem = aItemC.iterator().next();

		setChosenItem(pickItem);

		for (ActionListener aListener : listenerArr)
			addActionListener(aListener);
	}

	/**
	 * Method to replace the original item with the provided replacement.
	 * <p>
	 * This method will not trigger an ActionEvent.
	 */
	public void replaceItem(G1 aOrigItem, G1 aReplItem)
	{
		var listenerArr = getActionListeners();
		for (ActionListener aListener : listenerArr)
			removeActionListener(aListener);

		// Delegate
		itemModel.replaceItem(aOrigItem, aReplItem);

		for (ActionListener aListener : listenerArr)
			addActionListener(aListener);
	}

	/**
	 * Removes all of the items from the model. This method will not trigger an ActionEvent.
	 */
	@Override
	public void removeAllItems()
	{
		var listenerArr = getActionListeners();
		for (ActionListener aListener : listenerArr)
			removeActionListener(aListener);

		itemModel.removeAllItems();

		for (ActionListener aListener : listenerArr)
			addActionListener(aListener);
	}

// Note you cannot do the below as internal methods within JComboBox make calls to
// the methods below.
//	@Override
//	public Object getSelectedItem()
//	{
//		throw new RuntimeException("Unsupported operation. Call getChosenItem()");
//	}
//
//	@Override
//	public void setSelectedItem(Object aObj)
//	{
//		throw new RuntimeException("Unsupported operation. Call setChosenItem()");
//	}
}
