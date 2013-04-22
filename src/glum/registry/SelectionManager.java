package glum.registry;

import glum.reflect.FunctionRunnable;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import javax.swing.SwingUtilities;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

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
	public <G1 extends Object> void addItem(Class<G1> aClass, G1 aItem, SelectionListener skipListener)
	{
		if (registerSet.contains(aClass) == false)
			throw new RuntimeException("Unregistered selection class: " + aClass);

		// Replace the old selections with the new item list
		synchronized(this)
		{
			selectionMap.put(aClass, aItem);
		}

		notifyListeners(aClass, skipListener);
	}

	/**
	 * Add to the list of selected items associated with aClass
	 */
	public <G1 extends Object> void addItems(Class<G1> aClass, List<G1> itemList)
	{
		addItems(aClass, itemList, null);
	}

	/**
	 * Adds to the list of selected items and notifies all listeners but the specified skipListener
	 */
	public <G1 extends Object> void addItems(Class<G1> aClass, List<G1> itemList, SelectionListener skipListener)
	{
		if (registerSet.contains(aClass) == false)
			throw new RuntimeException("Unregistered selection class: " + aClass);

		// Replace the old selections with the new item list
		synchronized(this)
		{
			selectionMap.putAll(aClass, itemList);
		}

		notifyListeners(aClass, skipListener);
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
	public <G1 extends Object> void removeItems(Class<G1> aClass, List<G1> itemList)
	{
		removeItems(aClass, itemList, null);
	}

	/**
	 * Removes from the list of selected items and notifies all listeners but the specified skipListener
	 */
	public <G1 extends Object> void removeItems(Class<G1> aClass, List<G1> itemList, SelectionListener skipListener)
	{
		if (registerSet.contains(aClass) == false)
			throw new RuntimeException("Unregistered selection class: " + aClass);

		// Replace the old selections with the new item list
		synchronized(this)
		{
			Set<G1> replaceSet;

			replaceSet = new LinkedHashSet<G1>(getSelectedItems(aClass));
			replaceSet.removeAll(itemList);
			selectionMap.replaceValues(aClass, replaceSet);
		}

		notifyListeners(aClass, skipListener);
	}

	/**
	 * Sets in the selected items and notifies all listeners
	 */
	public <G1 extends Object> void setItems(Class<G1> aClass, List<G1> itemList)
	{
		setItems(aClass, itemList, null);
	}

	/**
	 * Sets in the selected items and notifies all listeners but the specified skipListener
	 */
	public <G1 extends Object> void setItems(Class<G1> aClass, List<G1> itemList, SelectionListener skipListener)
	{
		if (registerSet.contains(aClass) == false)
			throw new RuntimeException("Unregistered selection class: " + aClass);

		// Replace the old selections with the new item list
		synchronized(this)
		{
			selectionMap.replaceValues(aClass, itemList);
		}

		notifyListeners(aClass, skipListener);
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
	private void notifyListeners(Class<?> aClass, SelectionListener skipListener)
	{
		List<SelectionListener> listenerList;

		// Ensure this logic is always executed on the AWT thread (if notifyViaAwtThread == true)
		if (notifyViaAwtThread == true && SwingUtilities.isEventDispatchThread() == false)
		{
			SwingUtilities.invokeLater(new FunctionRunnable(this, "notifyListeners", aClass, skipListener));
			return;
		}

		synchronized(this)
		{
			listenerList = Lists.newArrayList(listenerMap.get(aClass));
			listenerList.remove(skipListener);
		}

		for (SelectionListener aListener : listenerList)
			aListener.selectionChanged(this, aClass);
	}

}
