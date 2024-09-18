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

import java.util.ArrayList;
import java.util.Collection;

import com.google.common.collect.ImmutableList;

import glum.filter.Filter;
import glum.filter.NullFilter;

/**
 * Implementation of {@link ItemProcessor} which provides automatic filtering.
 * <p>
 * Only items that pass the specified activeFilter will be returned.
 *
 * @author lopeznr1
 */
public class FilterItemProcessor<G1> extends BasicItemProcessor<G1>
{
	// State vars
	private ImmutableList<G1> fullItemL;
	private ImmutableList<G1> passItemL;
	private Filter<G1> activeFilter;

	/** Standard Constructor */
	public FilterItemProcessor()
	{
		fullItemL = ImmutableList.of();
		passItemL = ImmutableList.of();
		activeFilter = new NullFilter<G1>();
	}

	/**
	 * Returns the current active filter
	 */
	public Filter<G1> getFilter()
	{
		return activeFilter;
	}

	/**
	 * Sets in the filter used to determine the subset of the items available to this ItemProcessor.
	 */
	public void setFilter(Filter<G1> aFilter)
	{
		activeFilter = aFilter;
		if (activeFilter == null)
			activeFilter = new NullFilter<G1>();

		rebuildPassList();

		// Notify our listeners
		notifyListeners();
	}

	/**
	 * Replaces the current full list of items stored with aItemList. Note that the number of items available by this
	 * processor may be less than the number of items in aItemList due to the active filter.
	 */
	public void setItems(Collection<G1> aItemC)
	{
		fullItemL = ImmutableList.copyOf(aItemC);
		rebuildPassList();

		// Notify our listeners
		notifyListeners();
	}

	@Override
	public int getNumItems()
	{
		return passItemL.size();
	}

	@Override
	public ImmutableList<G1> getAllItems()
	{
		return passItemL;
	}

	/**
	 * Helper method to determine all of the items that are visible by this ItemProcessor. This is accomplished by
	 * passing them through activeFilter.
	 */
	private void rebuildPassList()
	{
		var tmpItemL = new ArrayList<G1>();
		for (var aItem : fullItemL)
		{
			if (activeFilter == null || activeFilter.isValid(aItem) == true)
				tmpItemL.add(aItem);
		}

		passItemL = ImmutableList.copyOf(tmpItemL);
	}

}
