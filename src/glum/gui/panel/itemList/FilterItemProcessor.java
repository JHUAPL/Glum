package glum.gui.panel.itemList;

import java.util.ArrayList;
import java.util.Collection;

import com.google.common.collect.Lists;

import glum.filter.Filter;
import glum.filter.NullFilter;

public class FilterItemProcessor<G1> extends BasicItemProcessor<G1>
{
	// State vars
	private ArrayList<G1> fullList;
	private ArrayList<G1> passList;
	private Filter<G1> activeFilter;

	public FilterItemProcessor()
	{
		super();

		fullList = Lists.newArrayList();
		passList = Lists.newArrayList();
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
	public void setItems(Collection<G1> aItemList)
	{
		fullList = new ArrayList<G1>(aItemList);
		rebuildPassList();

		// Notify our listeners
		notifyListeners();
	}

	@Override
	public int getNumItems()
	{
		return passList.size();
	}

	@Override
	public Collection<? extends G1> getItems()
	{
		return Lists.newArrayList(passList);
	}

	/**
	 * Helper method to determine all of the items that are visible by this ItemProcessor. This is accomplished by
	 * passing them through activeFilter.
	 */
	private void rebuildPassList()
	{
		passList = Lists.newArrayList();

		for (G1 aItem : fullList)
		{
			if (activeFilter == null || activeFilter.isValid(aItem) == true)
				passList.add(aItem);
		}

		passList.trimToSize();
	}

}
