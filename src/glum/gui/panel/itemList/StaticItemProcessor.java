package glum.gui.panel.itemList;

import java.util.*;

public class StaticItemProcessor<G1> extends BasicItemProcessor<G1>
{
	private ArrayList<G1> itemList;

	public StaticItemProcessor()
	{
		super();

		itemList = new ArrayList<G1>();
	}

	public StaticItemProcessor(List<G1> aList)
	{
		super();

		itemList = new ArrayList<G1>(aList);
	}

	/**
	 * Return the index of the specified item
	 * 
	 * @see ArrayList#indexOf
	 */
	public int indexOf(G1 aItem)
	{
		return itemList.indexOf(aItem);
	}

	/**
	 * Replaces the static list of items stored with aItemList
	 */
	public void setItems(Collection<G1> aItemList)
	{
		itemList = new ArrayList<G1>(aItemList);

		// Notify our listeners
		notifyListeners();
	}

	@Override
	public synchronized ArrayList<G1> getItems()
	{
		return new ArrayList<G1>(itemList);
	}

	@Override
	public synchronized int getNumItems()
	{
		return itemList.size();
	}

}
