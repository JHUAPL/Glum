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

import javax.swing.SwingUtilities;

import com.google.common.collect.*;

/**
 * Class that support arbitrary item selection. All valid types of selected items must be registered in the Constructor.
 *
 * @author lopeznr1
 */
public class SelectionManager
{
	// State vars
	private Multimap<Class<?>, Object> selectionM;
	private Multimap<Class<?>, SelectionListener> listenerM;
	private Set<Class<?>> registerS;
	private boolean notifyViaAwtThread;

	/**
	 * Standard Constructor
	 */
	public SelectionManager(Class<?>... aClassArr)
	{
		selectionM = ArrayListMultimap.create();
		listenerM = HashMultimap.create();

		registerS = new HashSet<>();
		for (Class<?> aClass : aClassArr)
			registerS.add(aClass);

		notifyViaAwtThread = false;
	}

	/**
	 * Adds in the specified Listener as a Listener of the specified item selection changes. Throws RuntimeException if
	 * aClass was not previously registered in the constructor.
	 */
	public synchronized void addListener(SelectionListener aListener, Class<?> aClass)
	{
		if (registerS.contains(aClass) == true)
			listenerM.put(aClass, aListener);
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
		if (registerS.contains(aClass) == false)
			throw new RuntimeException("Unregistered selection class: " + aClass);

		// Replace the old selections with the new item list
		synchronized (this)
		{
			selectionM.put(aClass, aItem);
		}

		notifyListeners(aClass, aSkipListener);
	}

	/**
	 * Add to the list of selected items associated with aClass
	 */
	public <G1 extends Object> void addItems(Class<G1> aClass, List<G1> aItemL)
	{
		addItems(aClass, aItemL, null);
	}

	/**
	 * Adds to the list of selected items and notifies all listeners but the specified skipListener
	 */
	public <G1 extends Object> void addItems(Class<G1> aClass, List<G1> aItemL, SelectionListener aSkipListener)
	{
		if (registerS.contains(aClass) == false)
			throw new RuntimeException("Unregistered selection class: " + aClass);

		// Replace the old selections with the new item list
		synchronized (this)
		{
			selectionM.putAll(aClass, aItemL);
		}

		notifyListeners(aClass, aSkipListener);
	}

	/**
	 * Returns the list of selected items associated with aClass
	 */
	@SuppressWarnings("unchecked")
	public synchronized <G1 extends Object> List<G1> getSelectedItems(Class<G1> aClass)
	{
		if (registerS.contains(aClass) == false)
			throw new RuntimeException("Unregistered selection class: " + aClass);

		return (List<G1>) new ArrayList<>(selectionM.get(aClass));
	}

	/**
	 * Removes from the list of selected items associated with aClass
	 */
	public <G1 extends Object> void removeItems(Class<G1> aClass, List<G1> aItemL)
	{
		removeItems(aClass, aItemL, null);
	}

	/**
	 * Removes from the list of selected items and notifies all listeners but the specified skipListener
	 */
	public <G1 extends Object> void removeItems(Class<G1> aClass, List<G1> aItemL, SelectionListener skipListener)
	{
		if (registerS.contains(aClass) == false)
			throw new RuntimeException("Unregistered selection class: " + aClass);

		// Replace the old selections with the new item list
		synchronized (this)
		{
			Set<G1> replaceSet;

			replaceSet = new LinkedHashSet<G1>(getSelectedItems(aClass));
			replaceSet.removeAll(aItemL);
			selectionM.replaceValues(aClass, replaceSet);
		}

		notifyListeners(aClass, skipListener);
	}

	/**
	 * Sets in the selected items and notifies all listeners
	 */
	public <G1 extends Object> void setItems(Class<G1> aClass, List<G1> aItemL)
	{
		setItems(aClass, aItemL, null);
	}

	/**
	 * Sets in the selected items and notifies all listeners but the specified skipListener
	 */
	public <G1 extends Object> void setItems(Class<G1> aClass, List<G1> aItemL, SelectionListener aSkipListener)
	{
		if (registerS.contains(aClass) == false)
			throw new RuntimeException("Unregistered selection class: " + aClass);

		// Replace the old selections with the new item list
		synchronized (this)
		{
			selectionM.replaceValues(aClass, aItemL);
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
			SwingUtilities.invokeLater(() -> notifyListeners(aClass, aSkipListener));
			return;
		}

		List<SelectionListener> listenerL;
		synchronized (this)
		{
			listenerL = new ArrayList<>(listenerM.get(aClass));
			listenerL.remove(aSkipListener);
		}

		for (SelectionListener aListener : listenerL)
			aListener.selectionChanged(this, aClass);
	}

}
