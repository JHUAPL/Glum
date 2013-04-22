package glum.gui.panel;

import java.awt.Component;
import java.awt.Container;
import java.awt.FocusTraversalPolicy;
import java.util.ArrayList;

import com.google.common.collect.Lists;

public class CustomFocusTraversalPolicy extends FocusTraversalPolicy
{
	protected ArrayList<Component> itemList;
	
	public CustomFocusTraversalPolicy()
	{
		itemList = Lists.newArrayList();
	}
	
	/**
	 * Method to add an item to the end of the FocusTraversalPolicy
	 */
	public void addComponent(Component aItem)
	{
		itemList.add(aItem);
	}

	@Override
	public Component getComponentAfter(Container focusCycleRoot, Component aComponent)
	{
		Component aComp;
		int cIndex, tIndex;

		// Bail if the component is not in our list
		cIndex = itemList.indexOf(aComponent);
		if (cIndex < 0)
			return getFirstComponent(focusCycleRoot);
		
		tIndex = cIndex;
		while (true)
		{
			// Iterate through the circular loop
			tIndex++;
			if (tIndex == itemList.size())
				tIndex = 0;
			
			// Ensure the item is focusable
			aComp = itemList.get(tIndex);
			if (checkFocusability(aComp) == true)
				return aComp;
			
			// Bail if we have made an full loop
			if (tIndex == cIndex)
				break;
		}
		
		return itemList.get(cIndex);
	}

	@Override
	public Component getComponentBefore(Container focusCycleRoot, Component aComponent)
	{
		Component aComp;
		int cIndex, tIndex;

		// Bail if the component is not in our list
		cIndex = itemList.indexOf(aComponent);
		if (cIndex < 0)
			return getLastComponent(focusCycleRoot);
		
		tIndex = cIndex;
		while (true)
		{
			// Iterate through the circular loop
			tIndex--;
			if (tIndex == -1)
				tIndex = itemList.size() - 1;
			
			// Ensure the item is focusable
			aComp = itemList.get(tIndex);
			if (checkFocusability(aComp) == true)
				return aComp;
			
			// Bail if we have made an full loop
			if (tIndex == cIndex)
				break;
		}
		
		return itemList.get(cIndex);
	}

	@Override
	public Component getDefaultComponent(Container focusCycleRoot)
	{
		return getFirstComponent(focusCycleRoot);
	}

	@Override
	public Component getFirstComponent(Container focusCycleRoot)
	{
		Component aComp;
		
		// Bail if no components to traverse
		if (itemList.isEmpty() == true)
			return null;
			
		aComp = itemList.get(0);
		if (checkFocusability(aComp) == true)
			return aComp;
		
		return getComponentAfter(focusCycleRoot, aComp);
	}

	@Override
	public Component getLastComponent(Container focusCycleRoot)
	{
		Component aComp;
		
		// Bail if no components to traverse
		if (itemList.isEmpty() == true)
			return null;
			
		aComp = itemList.get(itemList.size()-1);
		if (checkFocusability(aComp) == true)
			return aComp;
		
		return getComponentBefore(focusCycleRoot, aComp);
	}
	

	/**
	* Utility method to test to see if a Component can grab the focus.
	* To grab the focus a component must be:
	* - focusable
	* - enabled
	* - visible
	* - be in a container that is being shown.
	*/
	protected boolean checkFocusability(Component aComp)
	{
		if (aComp.isFocusable() == false)
			return false;
		
		if (aComp.isEnabled() == false)
			return false;
		
		if (aComp.isVisible() == false)
			return false;
		
		if (aComp.isShowing() == false)
			return false;
		
		return true;
	}

}
