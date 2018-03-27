package glum.registry;

import java.util.*;

import javax.swing.SwingUtilities;

import com.google.common.collect.*;

/**
 * Class that support arbitrary item selection. All valid types of selected items must be registered in the Constructor.
 */
public class SelectionManager
{
	// State vars
	private Multimap<Class<?>, Object> selectionMap;
	private Multimap<Class<?>, SelectionListener> listenerMap;
	private Set<Class<?>> registerSet;
	private boolean notifyViaAwtThread;

	public SelectionManager(Class<?>... classArr)
	{
		selectionMap = ArrayListMultimap.create();
		listenerMap = HashMultimap.create();

		registerSet = Sets.newHashSet();
		for (Class<?> aClass : classArr)
			registerSet.add(aClass);

		notifyViaAwtThread = false;
	}

	/**
	 * Adds in the specified Listener as a Listener of the specified item selection changes. Throws RuntimeException if
	 * aClass was not previously registered in the constructor.
	 */
	public synchronized void addListener(SelectionListener aListener, Class<?> aClass)
	{
		if (registerSet.contains(aClass) == true)
			listenerMap.put(aClass, aListener);
		else
			throw new RuntimeException("Unregistered selection class: " + aClass);
	}

	/**
	 * Add to the list of selected items associated with aClass
	 */
	public <G1 extends Object> void addItem(Class<G1> aClass, G1 aItem)
	{
		addItem(aClass, aItem, null);
	}

	/**
	 * Adds to the list of selected items and notifies all listeners but the specified skipListener
	 */
	public <G1 extends Object> void addItem(Class<G1> aClass, G1 aItem, SelectionListener aSkipListener)
	{
		if (registerSet.contains(aClass) == false)
			throw new RuntimeException("Unregistered selection class: " + aClass);

		// Replace the old selections with the new item list
		synchronized(this)
		{
			selectionMap.put(aClass, aItem);
		}

		notifyListeners(aClass, aSkipListener);
	}

	/**
	 * Add to the list of selected items associated with aClass
	 */
	public <G1 extends Object> void addItems(Class<G1> aClass, List<G1> aItemList)
	{
		addItems(aClass, aItemList, null);
	}

	/**
	 * Adds to the list of selected items and notifies all listeners but the specified skipListener
	 */
	public <G1 extends Object> void addItems(Class<G1> aClass, List<G1> aItemList, SelectionListener aSkipListener)
	{
		if (registerSet.contains(aClass) == false)
			throw new RuntimeException("Unregistered selection class: " + aClass);

		// Replace the old selections with the new item list
		synchronized(this)
		{
			selectionMap.putAll(aClass, aItemList);
		}

		notifyListeners(aClass, aSkipListener);
	}

	/**
	 * Returns the list of selected items associated with aClass
	 */
	@SuppressWarnings("unchecked")
	public synchronized <G1 extends Object> List<G1> getSelectedItems(Class<G1> aClass)
	{
		if (registerSet.contains(aClass) == false)
			throw new RuntimeException("Unregistered selection class: " + aClass);

		return (List<G1>)Lists.newArrayList(selectionMap.get(aClass));
	}

	/**
	 * Removes from the list of selected items associated with aClass
	 */
	public <G1 extends Object> void removeItems(Class<G1> aClass, List<G1> aItemList)
	{
		removeItems(aClass, aItemList, null);
	}

	/**
	 * Removes from the list of selected items and notifies all listeners but the specified skipListener
	 */
	public <G1 extends Object> void removeItems(Class<G1> aClass, List<G1> aItemList, SelectionListener skipListener)
	{
		if (registerSet.contains(aClass) == false)
			throw new RuntimeException("Unregistered selection class: " + aClass);

		// Replace the old selections with the new item list
		synchronized(this)
		{
			Set<G1> replaceSet;

			replaceSet = new LinkedHashSet<G1>(getSelectedItems(aClass));
			replaceSet.removeAll(aItemList);
			selectionMap.replaceValues(aClass, replaceSet);
		}

		notifyListeners(aClass, skipListener);
	}

	/**
	 * Sets in the selected items and notifies all listeners
	 */
	public <G1 extends Object> void setItems(Class<G1> aClass, List<G1> aItemList)
	{
		setItems(aClass, aItemList, null);
	}

	/**
	 * Sets in the selected items and notifies all listeners but the specified skipListener
	 */
	public <G1 extends Object> void setItems(Class<G1> aClass, List<G1> aItemList, SelectionListener aSkipListener)
	{
		if (registerSet.contains(aClass) == false)
			throw new RuntimeException("Unregistered selection class: " + aClass);

		// Replace the old selections with the new item list
		synchronized(this)
		{
			selectionMap.replaceValues(aClass, aItemList);
		}

		notifyListeners(aClass, aSkipListener);
	}

	/**
	 * Method to force all notifications to be sent via the AWT thread. Note if there is some time sensitive code,
	 * setting this flag may cause it to run slightly slower.
	 */
	public void setNotificationViaAwtThread(boolean aBool)
	{
		notifyViaAwtThread = aBool;
	}

	/**
	 * Helper method to notify the listeners associated with the specified class.
	 */
	private void notifyListeners(Class<?> aClass, SelectionListener aSkipListener)
	{
		// Ensure this logic is always executed on the AWT thread (if notifyViaAwtThread == true)
		if (notifyViaAwtThread == true && SwingUtilities.isEventDispatchThread() == false)
		{
			Runnable tmpRunnable = () -> notifyListeners(aClass, aSkipListener);
			SwingUtilities.invokeLater(tmpRunnable);
			return;
		}

		List<SelectionListener> listenerList;
		synchronized(this)
		{
			listenerList = new ArrayList<>(listenerMap.get(aClass));
			listenerList.remove(aSkipListener);
		}

		for (SelectionListener aListener : listenerList)
			aListener.selectionChanged(this, aClass);
	}

}
