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

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Sets;

/**
 * Base implementation of the ItemManager interface.
 *
 * @author lopeznr1
 */
public class BaseItemManager<G1> implements ItemManager<G1>
{
	// State vars
	private List<ItemEventListener> listenerL;

	private ImmutableList<G1> fullItemL;
	private ImmutableSet<G1> pickItemS;

	/**
	 * Standard Constructor
	 */
	public BaseItemManager()
	{
		listenerL = new ArrayList<>();

		fullItemL = ImmutableList.of();
		pickItemS = ImmutableSet.of();
	}

	@Override
	public void addListener(ItemEventListener aListener)
	{
		listenerL.add(aListener);
	}

	@Override
	public void delListener(ItemEventListener aListener)
	{
		listenerL.remove(aListener);
	}

	@Override
	public ImmutableList<G1> getAllItems()
	{
		return fullItemL;
	}

	@Override
	public int getNumItems()
	{
		return fullItemL.size();
	}

	@Override
	public ImmutableSet<G1> getSelectedItems()
	{
		return pickItemS;
	}

	@Override
	public void removeItems(Collection<G1> aItemC)
	{
		List<G1> tmpTrackL = new ArrayList<>(fullItemL);
		tmpTrackL.removeAll(aItemC);
		fullItemL = ImmutableList.copyOf(tmpTrackL);

		Set<G1> tmpTrackS = new LinkedHashSet<>(pickItemS);
		tmpTrackS.removeAll(aItemC);
		pickItemS = ImmutableSet.copyOf(tmpTrackS);

		// Send out the appropriate notifications
		notifyListeners(this, ItemEventType.ItemsChanged);
		notifyListeners(this, ItemEventType.ItemsSelected);
	}

	@Override
	public void setAllItems(Collection<G1> aItemC)
	{
		fullItemL = ImmutableList.copyOf(aItemC);

		// Update the picked items to contain items only in fullItemL
		Set<G1> tmpS = new LinkedHashSet<>(fullItemL);
		tmpS = Sets.intersection(pickItemS, tmpS);
		pickItemS = ImmutableSet.copyOf(tmpS);

		notifyListeners(this, ItemEventType.ItemsChanged);
		notifyListeners(this, ItemEventType.ItemsSelected);
	}

	@Override
	public void setSelectedItems(Collection<G1> aItemC)
	{
		// Bail if the selection has not changed
		if (aItemC.equals(pickItemS.asList()) == true)
			return;

		// Update our selection
		pickItemS = ImmutableSet.copyOf(aItemC);

		// Send out the appropriate notifications
		notifyListeners(this, ItemEventType.ItemsSelected);
	}

	/**
	 * Sends out notification to all the listeners of the specified event.
	 */
	protected void notifyListeners(Object aSource, ItemEventType aEventType)
	{
		for (ItemEventListener aListener : listenerL)
			aListener.handleItemEvent(aSource, aEventType);
	}

}
