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
package glum.item;

import java.util.Collection;

import com.google.common.collect.ImmutableSet;

import glum.gui.panel.itemList.ItemProcessor;

/**
 * Interface that defines a collection of methods used to manage and handle notification of a set of items.
 *
 * @author lopeznr1
 */
public interface ItemManager<G1> extends ItemProcessor<G1>
{
	/**
	 * Returns the set of selected items.
	 */
	public ImmutableSet<G1> getSelectedItems();

	/**
	 * Removes the specified lists of items from this {@link ItemManager}.
	 *
	 * @param aItemC
	 *        The list of items to be removed.
	 */
	public void removeItems(Collection<G1> aItemC);

	/**
	 * Installs the specified items into this {@link ItemManager}.
	 * <p>
	 * All prior items will be cleared out.
	 *
	 * @param aItemC
	 *        The list of items to be installed.
	 */
	public void setAllItems(Collection<G1> aItemC);

	/**
	 * Method that sets in the list of selected items.
	 *
	 * @param aItemC
	 *        The list of items to be selected.
	 */
	public void setSelectedItems(Collection<G1> aItemC);

}
