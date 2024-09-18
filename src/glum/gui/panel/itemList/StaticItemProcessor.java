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
package glum.gui.panel.itemList;

import java.util.*;

import com.google.common.collect.ImmutableList;

/**
 * Implementation of {@link ItemProcessor} which maintains a static list of items.
 * 
 * @author lopeznr1
 */
public class StaticItemProcessor<G1> extends BasicItemProcessor<G1>
{
	private ImmutableList<G1> itemL;

	/**
	 * Standard Constructor
	 */
	public StaticItemProcessor(List<G1> aItemL)
	{
		itemL = ImmutableList.copyOf(aItemL);
	}

	/**
	 * Empty Constructor
	 */
	public StaticItemProcessor()
	{
		this(ImmutableList.of());
	}

	/**
	 * Return the index of the specified item
	 * 
	 * @see ArrayList#indexOf
	 */
	public int indexOf(G1 aItem)
	{
		return itemL.indexOf(aItem);
	}

	/**
	 * Replaces the static list of items stored with aItemList
	 */
	public void setItems(Collection<G1> aItemC)
	{
		itemL = ImmutableList.copyOf(aItemC);

		// Notify our listeners
		notifyListeners();
	}

	@Override
	public synchronized ImmutableList<G1> getAllItems()
	{
		return itemL;
	}

	@Override
	public synchronized int getNumItems()
	{
		return itemL.size();
	}

}
