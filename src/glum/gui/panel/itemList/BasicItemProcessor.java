package glum.gui.panel.itemList;

import java.util.*;

public abstract class BasicItemProcessor<G1> implements ItemProcessor<G1>
{
	private Collection<ItemChangeListener> myListeners;

	public BasicItemProcessor()
	{
		myListeners = new ArrayList<ItemChangeListener>();
	}

	@Override
	public synchronized void addItemChangeListener(ItemChangeListener aListener)
	{
		myListeners.add(aListener);
	}

	@Override
	public synchronized void removeItemChangeListener(ItemChangeListener aListener)
	{
		myListeners.remove(aListener);
	}

	/**
	 * Helper method
	 */
	protected void notifyListeners()
	{
		Collection<ItemChangeListener> notifySet;

		// Get the listeners
		synchronized(this)
		{
			notifySet = new ArrayList<ItemChangeListener>(myListeners);
		}

		// Send out the notifications
		for (ItemChangeListener aListener : notifySet)
			aListener.itemChanged();
	}

}
