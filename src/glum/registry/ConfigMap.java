package glum.registry;

import java.util.*;

/**
 * ConfigMap is a general purpose mapping allowing objects to be linked together by a key. On retrievals of objects from
 * the ConfigMap - a default can be specified should no mapping be found.
 */
public class ConfigMap
{
	// State var
	private Map<Object, Collection<Object>> mySetMap;
	private Map<Object, Object> mySingletonMap;

	public ConfigMap()
	{
		mySetMap = new LinkedHashMap<Object, Collection<Object>>();
		mySingletonMap = new LinkedHashMap<Object, Object>();
	}

	/**
	 * Adds aObject to the appropriate collection w.r.t aKey
	 */
	public synchronized void addItem(Object aKey, Object aObject)
	{
		Collection<Object> aCollection;

		aCollection = mySetMap.get(aKey);
		if (aCollection == null)
		{
			aCollection = new LinkedHashSet<Object>();
			mySetMap.put(aKey, aCollection);
		}
		aCollection.add(aObject);
	}

	/**
	 * Adds a collection of objects to the appropriate collection w.r.t aKey
	 */
	public synchronized void addItems(Object aKey, Collection<? extends Object> aObjectList)
	{
		Collection<Object> aCollection;

		aCollection = mySetMap.get(aKey);
		if (aCollection == null)
		{
			aCollection = new LinkedHashSet<Object>();
			mySetMap.put(aKey, aCollection);
		}
		aCollection.addAll(aObjectList);
	}

	/**
	 * Returns the appropriate collection w.r.t aKey
	 */
	public synchronized Collection<Object> getItems(Object aKey)
	{
		Collection<Object> aCollection;

		aCollection = mySetMap.get(aKey);
		if (aCollection == null)
			return new ArrayList<Object>();

		return new ArrayList<Object>(aCollection);
	}

	public synchronized <G1> Collection<G1> getItems(Object aKey, Class<G1> retType)
	{
		Collection<Object> aCollection;
		Collection<G1> retList;

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
	public synchronized Object get(Object aKey)
	{
		Object aSingleton;

		// Insanity check
		if (aKey == null)
			return null;

		aSingleton = mySingletonMap.get(aKey);
		return aSingleton;
	}

	@SuppressWarnings("unchecked")
	public synchronized <G1> G1 get(Object aKey, G1 defaultVal)
	{
		Object aSingleton;
		Class<?> aType;

		// Insanity check
		if (aKey == null)
			return null;

		if (defaultVal == null)
			throw new IllegalArgumentException();

		// If no value associated with the key then return defaultVal
		aSingleton = mySingletonMap.get(aKey);
		if (aSingleton == null)
			return defaultVal;

		// Ensure the value is of the same type as defaultVal
		aType = defaultVal.getClass();
		if (aType.isAssignableFrom(aSingleton.getClass()) == true)
			return (G1)(aType.cast(aSingleton));

		// !if (G1.class.isAssignableFrom(aSingleton.getClass()) == true)

		return defaultVal;
	}

	public synchronized <G1> G1 get(Object aKey, Class<G1> retType, G1 defaultVal)
	{
		Object aSingleton;

		// Insanity check
		if (aKey == null)
			return null;

		// If no value associated with the key then return defaultVal
		aSingleton = mySingletonMap.get(aKey);
		if (aSingleton == null)
			return defaultVal;

		if (retType.isInstance(aSingleton) == true)
			return retType.cast(aSingleton);

		return defaultVal;
	}

	/**
	 * Returns all of the keys for the singleton map
	 */
	public synchronized Set<Object> keySetForSingletons()
	{
		return new LinkedHashSet<Object>(mySingletonMap.keySet());
	}

	/**
	 * Returns all of the keys for the multiple map
	 */
	public synchronized Set<Object> keySetForMultiples()
	{
		return new LinkedHashSet<Object>(mySetMap.keySet());
	}

	/**
	 * Sets aObject as the appropriate singleton w.r.t aKey
	 */
	public synchronized void put(Object aKey, Object aObject)
	{
		// Remove the entry from the hashtable if null
		if (aObject == null)
			mySingletonMap.remove(aKey);

		// Set in the entry for the corresponding aKey
		else
			mySingletonMap.put(aKey, aObject);
	}

	/**
	 * Removes all the objects from the appropriate collection
	 */
	public synchronized void removeAllItems(Object aKey)
	{
		Collection<Object> aCollection;

		// Remove the item
		// Get the associated collection
		aCollection = mySetMap.get(aKey);
		if (aCollection == null)
			return;

		// Remove the item from the collection
		aCollection.clear();
	}

	/**
	 * Removes aObject from the appropriate Collection w.r.t aKey
	 */
	public synchronized void removeItem(Object aKey, Object aObject)
	{
		Collection<Object> aCollection;

		// Get the associated collection
		aCollection = mySetMap.get(aKey);
		if (aCollection == null)
			return;

		// Remove the item from the collection
		aCollection.remove(aObject);
	}

}
