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
package glum.registry;

import java.util.*;

/**
 * The Registry class allows for various types of entities to be linked together by a common key. Outside operators can:
 * <ul>
 * <li>1. Add items associated with the key
 * <li>2. Retrieve a collection of all items associated with the key
 * <li>3. Register to receive notification of any changes associated with a key
 * <li>4. Send notification with regard to a key
 * </ul>
 *
 * @author lopeznr1
 */
public class Registry
{
	// State var
	private Map<Object, Collection<Object>> myGroupM;
	private Map<Object, Collection<ResourceListener>> myGroupListenerM;
	private Map<Object, Object> mySingletonM;
	private Map<Object, Collection<ResourceListener>> mySingletonListenerM;

	/** Standard Constructor */
	public Registry()
	{
		myGroupM = new LinkedHashMap<Object, Collection<Object>>();
		myGroupListenerM = new LinkedHashMap<Object, Collection<ResourceListener>>();

		mySingletonM = new LinkedHashMap<Object, Object>();
		mySingletonListenerM = new LinkedHashMap<Object, Collection<ResourceListener>>();
	}

	/**
	 * Manual Destructor
	 */
	public synchronized void dispose()
	{
		if (myGroupM != null)
		{
			myGroupM.clear();
			myGroupM = null;
		}
		if (myGroupListenerM != null)
		{
			myGroupListenerM.clear();
			myGroupListenerM = null;
		}

		if (mySingletonM != null)
		{
			mySingletonM.clear();
			mySingletonM = null;
		}
		if (mySingletonListenerM != null)
		{
			mySingletonListenerM.clear();
			mySingletonListenerM = null;
		}
	}

	/**
	 * Adds in the collection of objects w.r.t aKey
	 */
	public void addAllResourceItems(Object aKey, Collection<? extends Object> aList)
	{
		Collection<Object> tmpGroupC;

		synchronized (this)
		{
			tmpGroupC = myGroupM.get(aKey);
			if (tmpGroupC == null)
			{
				tmpGroupC = new LinkedHashSet<Object>();
				myGroupM.put(aKey, tmpGroupC);
			}
			tmpGroupC.addAll(aList);
		}

		// Notify the listeners
		notifyResourceListeners(aKey);
	}

	/**
	 * Adds aObject to the appropriate Collection w.r.t aKey
	 */
	public void addResourceItem(Object aKey, Object aObject)
	{
		Collection<Object> tmpGroupC;

		synchronized (this)
		{
			tmpGroupC = myGroupM.get(aKey);
			if (tmpGroupC == null)
			{
				tmpGroupC = new LinkedHashSet<Object>();
				myGroupM.put(aKey, tmpGroupC);
			}
			tmpGroupC.add(aObject);
		}

		// Notify the listeners
		notifyResourceListeners(aKey);
	}

	/**
	 * Adds Listener associated w.r.t aKey
	 */
	public void addResourceListener(Object aKey, ResourceListener aListener)
	{
		Collection<ResourceListener> tmpListenerC;

		synchronized (this)
		{
			tmpListenerC = myGroupListenerM.get(aKey);
			if (tmpListenerC == null)
			{
				tmpListenerC = new ArrayList<ResourceListener>();
				myGroupListenerM.put(aKey, tmpListenerC);
			}
			tmpListenerC.add(aListener);
		}
	}

	/**
	 * Adds Listener associated w.r.t aKey
	 */
	public void addSingletonListener(Object aKey, ResourceListener aListener)
	{
		Collection<ResourceListener> tmpListenerC;

		synchronized (this)
		{
			tmpListenerC = mySingletonListenerM.get(aKey);
			if (tmpListenerC == null)
			{
				tmpListenerC = new ArrayList<ResourceListener>();
				mySingletonListenerM.put(aKey, tmpListenerC);
			}
			tmpListenerC.add(aListener);
		}
	}

	/**
	 * Returns the appropriate collection w.r.t aKey
	 */
	public synchronized List<Object> getResourceItems(Object aKey)
	{
		Collection<Object> tmpGroupC;

		tmpGroupC = myGroupM.get(aKey);
		if (tmpGroupC == null)
			return new ArrayList<Object>();

		return new ArrayList<Object>(tmpGroupC);
	}

	public synchronized <G1> List<G1> getResourceItems(Object aKey, Class<G1> aRetType)
	{
		Collection<Object> tmpGroupC;
		List<G1> retList;

		retList = new ArrayList<G1>();

		tmpGroupC = myGroupM.get(aKey);
		if (tmpGroupC == null)
			return retList;

		for (Object aObj : tmpGroupC)
		{
			if (aRetType.isInstance(aObj) == true)
				retList.add(aRetType.cast(aObj));
		}

		return retList;
	}

	/**
	 * Returns the appropriate singleton w.r.t aKey
	 */
	public synchronized Object getSingleton(Object aKey)
	{
		Object tmpSingleton;

		// Insanity check
		if (aKey == null)
			return null;

		tmpSingleton = mySingletonM.get(aKey);
		return tmpSingleton;
	}

	public synchronized <G1> G1 getSingleton(Object aKey, Class<G1> aRetType)
	{
		Object tmpSingleton;

		// Insanity check
		if (aKey == null)
			return null;

		tmpSingleton = mySingletonM.get(aKey);
		if (aRetType.isInstance(tmpSingleton) == true)
			return aRetType.cast(tmpSingleton);

		return null;
	}

	/**
	 * Removes all the objects from the appropriate collection
	 */
	public void removeAllResourceItems(Object aKey)
	{
		Collection<Object> tmpGroupC;

		// Remove the item
		synchronized (this)
		{
			// Get the associated collection
			tmpGroupC = myGroupM.get(aKey);
			if (tmpGroupC == null)
				return;

			// Remove all the items from the collection
			tmpGroupC.clear();
		}

		// Notify the listeners
		notifyResourceListeners(aKey);
	}

	/**
	 * Removes aObject from the appropriate Collection w.r.t aKey
	 */
	public void removeResourceItem(Object aKey, Object aObject)
	{
		Collection<Object> tmpGroupC;

		// Remove the item
		synchronized (this)
		{
			// Get the associated collection
			tmpGroupC = myGroupM.get(aKey);
			if (tmpGroupC == null)
				return;

			// Remove the item from the collection
			tmpGroupC.remove(aObject);
		}

		// Notify the listeners
		notifyResourceListeners(aKey);
	}

	/**
	 * Replaces the current collection with aList w.r.t aKey
	 */
	public void replaceResourceItems(Object aKey, Collection<? extends Object> aList)
	{
		Collection<Object> tmpGroupC;

		synchronized (this)
		{
			tmpGroupC = myGroupM.get(aKey);
			if (tmpGroupC == null)
			{
				tmpGroupC = new LinkedHashSet<Object>();
				myGroupM.put(aKey, tmpGroupC);
			}
			tmpGroupC.clear();
			tmpGroupC.addAll(aList);
		}

		// Notify the listeners
		notifyResourceListeners(aKey);
	}

	/**
	 * Removes aListener associated w.r.t aKey
	 */
	public void removeResourceListener(Object aKey, ResourceListener aListener)
	{
		Collection<ResourceListener> tmpListenerC;

		// Remove the listenr
		synchronized (this)
		{
			if (myGroupListenerM == null)
				return;

			tmpListenerC = myGroupListenerM.get(aKey);
			if (tmpListenerC == null)
				return;

			tmpListenerC.remove(aListener);
		}
	}

	/**
	 * Removes aListener associated w.r.t aKey
	 */
	public void removeSingletonListener(Object aKey, ResourceListener aListener)
	{
		Collection<ResourceListener> tmpListenerC;

		// Remove the listenr
		synchronized (this)
		{
			if (mySingletonListenerM == null)
				return;

			tmpListenerC = mySingletonListenerM.get(aKey);
			if (tmpListenerC == null)
				return;

			tmpListenerC.remove(aListener);
		}
	}

	/**
	 * Sets aObject as the appropriate singleton w.r.t aKey
	 */
	public void setSingleton(Object aKey, Object aObject)
	{
		synchronized (this)
		{
			// Remove the entry from the hashtable if null
			if (aObject == null)
				mySingletonM.remove(aKey);
			// Set in the entry for the corresponding aKey
			else
				mySingletonM.put(aKey, aObject);
		}

		// Notify the listeners
		notifySingletonListeners(aKey);
	}

	/**
	 * Notifies the listeners that some action has occurred w.r.t aKey
	 */
	public void notifyResourceListeners(Object aKey)
	{
		Collection<ResourceListener> tmpListenerC;

		// Get the listeners
		synchronized (this)
		{
			tmpListenerC = myGroupListenerM.get(aKey);
			if (tmpListenerC == null)
				return;

			tmpListenerC = new ArrayList<ResourceListener>(tmpListenerC);
		}

		// Send out the notifications
		for (ResourceListener aListener : tmpListenerC)
			aListener.resourceChanged(this, aKey);
	}

	/**
	 * Notifies the listeners that some action has occurred w.r.t aKey
	 */
	public void notifySingletonListeners(Object aKey)
	{
		Collection<ResourceListener> tmpListenerC;

		// Get the listeners
		synchronized (this)
		{
			tmpListenerC = mySingletonListenerM.get(aKey);
			if (tmpListenerC == null)
				return;

			tmpListenerC = new ArrayList<ResourceListener>(tmpListenerC);
		}

		// Send out the notifications
		for (ResourceListener aListener : tmpListenerC)
			aListener.resourceChanged(this, aKey);
	}

}
