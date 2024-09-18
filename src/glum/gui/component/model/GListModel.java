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
package glum.gui.component.model;

import java.util.*;

import javax.swing.AbstractListModel;

/**
 * Generified mutable ListModel
 *
 * @author lopeznr1
 */
public class GListModel<G1> extends AbstractListModel<G1>
{
	protected final List<G1> itemL;

	/**
	 * Standard Constructor
	 */
	public GListModel(Collection<G1> aItemC)
	{
		itemL = new ArrayList<>(aItemC);
	}

	/**
	 * Adds aItem to this model
	 */
	public void addItem(G1 aItem)
	{
		itemL.add(aItem);

		var index = itemL.size() - 1;
		fireIntervalAdded(this, index, index);
	}

	/**
	 * Removes aItem from this model
	 */
	public void removeItem(G1 aItem)
	{
		var index = itemL.indexOf(aItem);
		itemL.remove(aItem);

		fireIntervalRemoved(this, index, index);
	}

	/**
	 * Removes all the items from this model
	 */
	public void removeAllItems()
	{
		// Bail if the list is empty
		if (itemL.isEmpty() == true)
			return;

		var lastIndex = itemL.size() - 1;
		itemL.clear();

		fireIntervalRemoved(this, 0, lastIndex);
	}

	/**
	 * Replaces the original item with the provided replacement.
	 * <p>
	 * Throws an exception if the original item is not installed.
	 */
	public void replaceItem(G1 aOrigItem, G1 aReplItem)
	{
		int tmpIdx = itemL.indexOf(aOrigItem);
		if (tmpIdx == -1)
			throw new RuntimeException("The original item is not in the list");

		itemL.set(tmpIdx, aReplItem);

		fireContentsChanged(this, tmpIdx, tmpIdx);
	}

	/**
	 * Returns a list of all the items
	 */
	public List<G1> getAllItems()
	{
		return new ArrayList<>(itemL);
	}

	/**
	 * Returns the index of the item located in this model.
	 */
	public int indexOf(G1 aItem)
	{
		return itemL.indexOf(aItem);
	}

	@Override
	public int getSize()
	{
		return itemL.size();
	}

	@Override
	public G1 getElementAt(int index)
	{
		return itemL.get(index);
	}
}
