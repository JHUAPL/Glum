package glum.registry;

import java.util.*;

/**
 * The Registry class allows for various types of entities to be linked together by a common key. Outside operators can:
 * <UL>
 * <LI>1. Add items associated with the key
 * <LI>2. Retrieve a collection of all items associated with the key
 * <LI>3. Register to receive notification of any changes associated with a key
 * <LI>4. Send notification with regard to a key
 * </UL>
 */
public class Registry
{
	// State var
	private Map<Object, Collection<Object>> mySetMap;
	private Map<Object, Collection<ResourceListener>> mySetListeners;
	private Map<Object, Object> mySingletonMap;
	private Map<Object, Collection<ResourceListener>> mySingletonListeners;

	public Registry()
	{
		mySetMap = new LinkedHashMap<Object, Collection<Object>>();
		mySetListeners = new LinkedHashMap<Object, Collection<ResourceListener>>();

		mySingletonMap = new LinkedHashMap<Object, Object>();
		mySingletonListeners = new LinkedHashMap<Object, Collection<ResourceListener>>();
	}

	/**
	 * Manual Destructor
	 */
	public synchronized void dispose()
	{
		if (mySetMap != null)
		{
			mySetMap.clear();
			mySetMap = null;
		}
		if (mySetListeners != null)
		{
			mySetListeners.clear();
			mySetListeners = null;
		}

		if (mySingletonMap != null)
		{
			mySingletonMap.clear();
			mySingletonMap = null;
		}
		if (mySingletonListeners != null)
		{
			mySingletonListeners.clear();
			mySingletonListeners = null;
		}
	}

	/**
	 * Adds in the collection of objects w.r.t aKey
	 */
	public void addAllResourceItems(Object aKey, Collection<? extends Object> aList)
	{
		Collection<Object> aCollection;

		synchronized(this)
		{
			aCollection = mySetMap.get(aKey);
			if (aCollection == null)
			{
				aCollection = new LinkedHashSet<Object>();
				mySetMap.put(aKey, aCollection);
			}
			aCollection.addAll(aList);
		}

		// Notify the listeners
		notifyResourceListeners(aKey);
	}

	/**
	 * Adds aObject to the appropriate Collection w.r.t aKey
	 */
	public void addResourceItem(Object aKey, Object aObject)
	{
		Collection<Object> aCollection;

		synchronized(this)
		{
			aCollection = mySetMap.get(aKey);
			if (aCollection == null)
			{
				aCollection = new LinkedHashSet<Object>();
				mySetMap.put(aKey, aCollection);
			}
			aCollection.add(aObject);
		}

		// Notify the listeners
		notifyResourceListeners(aKey);
	}

	/**
	 * Adds Listener associated w.r.t aKey
	 */
	public void addResourceListener(Object aKey, ResourceListener aListener)
	{
		Collection<ResourceListener> aSet;

		synchronized(this)
		{
			aSet = mySetListeners.get(aKey);
			if (aSet == null)
			{
				aSet = new ArrayList<ResourceListener>();
				mySetListeners.put(aKey, aSet);
			}
			aSet.add(aListener);
		}
	}

	/**
	 * Adds Listener associated w.r.t aKey
	 */
	public void addSingletonListener(Object aKey, ResourceListener aListener)
	{
		Collection<ResourceListener> aSet;

		synchronized(this)
		{
			aSet = mySingletonListeners.get(aKey);
			if (aSet == null)
			{
				aSet = new ArrayList<ResourceListener>();
				mySingletonListeners.put(aKey, aSet);
			}
			aSet.add(aListener);
		}
	}

	/**
	 * Returns the appropriate collection w.r.t aKey
	 */
	public synchronized List<Object> getResourceItems(Object aKey)
	{
		Collection<Object> aCollection;

		aCollection = mySetMap.get(aKey);
		if (aCollection == null)
			return new ArrayList<Object>();

		return new ArrayList<Object>(aCollection);
	}

	public synchronized <G1> List<G1> getResourceItems(Object aKey, Class<G1> retType)
	{
		Collection<Object> aCollection;
		List<G1> retList;

		retList = new ArrayList<G1>();

		aCollection = mySetMap.get(aKey);
		if (aCollection == null)
			return retList;

		for (Object aObj : aCollection)
		{
			if (retType.isInstance(aObj) == true)
				retList.add(retType.cast(aObj));
		}

		return retList;
	}

	/**
	 * Returns the appropriate singleton w.r.t aKey
	 */
	public synchronized Object getSingleton(Object aKey)
	{
		Object aSingleton;

		// Insanity check
		if (aKey == null)
			return null;

		aSingleton = mySingletonMap.get(aKey);
		return aSingleton;
	}

	public synchronized <G1> G1 getSingleton(Object aKey, Class<G1> retType)
	{
		Object aSingleton;

		// Insanity check
		if (aKey == null)
			return null;

		aSingleton = mySingletonMap.get(aKey);
		if (retType.isInstance(aSingleton) == true)
			return retType.cast(aSingleton);

		return null;
	}

	/**
	 * Removes all the objects from the appropriate collection
	 */
	public void removeAllResourceItems(Object aKey)
	{
		Collection<Object> aCollection;

		// Remove the item
		synchronized(this)
		{
			// Get the associated collection
			aCollection = mySetMap.get(aKey);
			if (aCollection == null)
				return;

			// Remove all the items from the collection
			aCollection.clear();
		}

		// Notify the listeners
		notifyResourceListeners(aKey);
	}

	/**
	 * Removes aObject from the appropriate Collection w.r.t aKey
	 */
	public void removeResourceItem(Object aKey, Object aObject)
	{
		Collection<Object> aCollection;

		// Remove the item
		synchronized(this)
		{
			// Get the associated collection
			aCollection = mySetMap.get(aKey);
			if (aCollection == null)
				return;

			// Remove the item from the collection
			aCollection.remove(aObject);
		}

		// Notify the listeners
		notifyResourceListeners(aKey);
	}

	/**
	 * Replaces the current collection with aList w.r.t aKey
	 */
	public void replaceResourceItems(Object aKey, Collection<? extends Object> aList)
	{
		Collection<Object> aCollection;

		synchronized(this)
		{
			aCollection = mySetMap.get(aKey);
			if (aCollection == null)
			{
				aCollection = new LinkedHashSet<Object>();
				mySetMap.put(aKey, aCollection);
			}
			aCollection.clear();
			aCollection.addAll(aList);
		}

		// Notify the listeners
		notifyResourceListeners(aKey);
	}

	/**
	 * Removes aListener associated w.r.t aKey
	 */
	public void removeResourceListener(Object aKey, ResourceListener aListener)
	{
		Collection<ResourceListener> aSet;

		// Remove the listenr
		synchronized(this)
		{
			if (mySetListeners == null)
				return;

			aSet = mySetListeners.get(aKey);
			if (aSet == null)
				return;

			aSet.remove(aListener);
		}
	}

	/**
	 * Removes aListener associated w.r.t aKey
	 */
	public void removeSingletonListener(Object aKey, ResourceListener aListener)
	{
		Collection<ResourceListener> aSet;

		// Remove the listenr
		synchronized(this)
		{
			if (mySingletonListeners == null)
				return;

			aSet = mySingletonListeners.get(aKey);
			if (aSet == null)
				return;

			aSet.remove(aListener);
		}
	}

	/**
	 * Sets aObject as the appropriate singleton w.r.t aKey
	 */
	public void setSingleton(Object aKey, Object aObject)
	{
		synchronized(this)
		{
			// Remove the entry from the hashtable if null
			if (aObject == null)
				mySingletonMap.remove(aKey);
			// Set in the entry for the corresponding aKey
			else
				mySingletonMap.put(aKey, aObject);
		}

		// Notify the listeners
		notifySingletonListeners(aKey);
	}

	/**
	 * Notifies the listeners that some action has occurred w.r.t aKey
	 */
	public void notifyResourceListeners(Object aKey)
	{
		Collection<ResourceListener> aSet, notifySet;

		// Get the listeners
		synchronized(this)
		{
			aSet = mySetListeners.get(aKey);
			if (aSet == null)
				return;

			notifySet = new ArrayList<ResourceListener>(aSet);
		}

		// Send out the notifications
		for (ResourceListener aListener : notifySet)
			aListener.resourceChanged(this, aKey);
	}

	/**
	 * Notifies the listeners that some action has occurred w.r.t aKey
	 */
	public void notifySingletonListeners(Object aKey)
	{
		Collection<ResourceListener> aSet, notifySet;

		// Get the listeners
		synchronized(this)
		{
			aSet = mySingletonListeners.get(aKey);
			if (aSet == null)
				return;

			notifySet = new ArrayList<ResourceListener>(aSet);
		}

		// Send out the notifications
		for (ResourceListener aListener : notifySet)
			aListener.resourceChanged(this, aKey);
	}

}
