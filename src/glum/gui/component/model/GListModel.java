package glum.gui.component.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractListModel;

import com.google.common.collect.Lists;

/**
 * Generified mutable ListModel
 */
public class GListModel<G1> extends AbstractListModel
{
	protected List<G1> itemList;

	public GListModel(Collection<G1> aItemList)
	{
		itemList = Lists.newArrayList(aItemList);
	}

	public GListModel(G1... aItemArr)
	{
		itemList = Lists.newArrayList(aItemArr);
	}

	/**
	 * Adds aItem to this model
	 */
	public void addItem(G1 aItem)
	{
		int index;
		
		itemList.add(aItem);
		
		index = itemList.size() - 1;
		fireIntervalAdded(this, index, index);
	}
	
	/**
	 * Removes aItem from this model
	 */
	public void removeItem(G1 aItem)
	{
		int index;
		
		index = itemList.indexOf(aItem);
		itemList.remove(aItem);
		
		fireIntervalRemoved(this, index, index);
	}
	
	/**
	 * Removes all the items from this model
	 */
	public void removeAllItems()
	{
		int lastIndex;
		
		// Bail if the list is empty
		if (itemList.isEmpty() == true)
			return;
		
		lastIndex = itemList.size() - 1;
		itemList.clear();
		
		fireIntervalRemoved(this, 0, lastIndex);
	}
	
	/**
	 * Returns a list of all the items
	 */
	public ArrayList<G1> getAllItems()
	{
		return Lists.newArrayList(itemList);
	}

	/**
	 * Returns the index of the item located in this model.
	 */
	public int indexOf(G1 aItem)
	{
		return itemList.indexOf(aItem);
	}

	@Override
	public int getSize()
	{
		return itemList.size();
	}

	@Override
	public G1 getElementAt(int index)
	{
		return itemList.get(index);
	}
}
