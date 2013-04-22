package glum.unit;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * Base UnitProvider class that provides the functionality of listener registration and notification.
 */
public abstract class BaseUnitProvider implements UnitProvider
{
	private List<UnitListener> myListeners;
	private String refName;

	public BaseUnitProvider(String aRefName)
	{
		myListeners = Lists.newLinkedList();
		refName = aRefName;
	}

	@Override
	public String getDisplayName()
	{
		return refName;
	}

	@Override
	public void addListener(UnitListener aListener)
	{
		myListeners.add(aListener);
	}

	@Override
	public void removeListener(UnitListener aListener)
	{
		myListeners.remove(aListener);
	}

	/**
	 * Utility method to notify our listeners
	 */
	protected void notifyListeners()
	{
		for (UnitListener aListener : myListeners)
			aListener.unitChanged(this, refName);
	}

}
