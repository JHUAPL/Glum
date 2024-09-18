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
package glum.gui.panel;

import java.awt.*;
import java.util.ArrayList;

/**
 * Implementation of {@link FocusTraversalPolicy}.
 * <p>
 * Components will be traversed based on the order they have been added to this object.
 *
 * @author lopeznr1
 */
public class CustomFocusTraversalPolicy extends FocusTraversalPolicy
{
	// State vars
	private ArrayList<Component> itemL;

	/** Standard Constructor */
	public CustomFocusTraversalPolicy()
	{
		itemL = new ArrayList<>();
	}

	/**
	 * Method to add an item to the end of the FocusTraversalPolicy
	 */
	public void addComponent(Component aItem)
	{
		itemL.add(aItem);
	}

	@Override
	public Component getComponentAfter(Container focusCycleRoot, Component aComponent)
	{
		// Bail if the component is not in our list
		var cIndex = itemL.indexOf(aComponent);
		if (cIndex < 0)
			return getFirstComponent(focusCycleRoot);

		var tIndex = cIndex;
		while (true)
		{
			// Iterate through the circular loop
			tIndex++;
			if (tIndex == itemL.size())
				tIndex = 0;

			// Ensure the item is focusable
			var tmpComp = itemL.get(tIndex);
			if (checkFocusability(tmpComp) == true)
				return tmpComp;

			// Bail if we have made an full loop
			if (tIndex == cIndex)
				break;
		}

		return itemL.get(cIndex);
	}

	@Override
	public Component getComponentBefore(Container focusCycleRoot, Component aComponent)
	{
		// Bail if the component is not in our list
		var cIndex = itemL.indexOf(aComponent);
		if (cIndex < 0)
			return getLastComponent(focusCycleRoot);

		var tIndex = cIndex;
		while (true)
		{
			// Iterate through the circular loop
			tIndex--;
			if (tIndex == -1)
				tIndex = itemL.size() - 1;

			// Ensure the item is focusable
			var tmpComp = itemL.get(tIndex);
			if (checkFocusability(tmpComp) == true)
				return tmpComp;

			// Bail if we have made an full loop
			if (tIndex == cIndex)
				break;
		}

		return itemL.get(cIndex);
	}

	@Override
	public Component getDefaultComponent(Container focusCycleRoot)
	{
		return getFirstComponent(focusCycleRoot);
	}

	@Override
	public Component getFirstComponent(Container focusCycleRoot)
	{
		// Bail if no components to traverse
		if (itemL.isEmpty() == true)
			return null;

		var tmpComp = itemL.get(0);
		if (checkFocusability(tmpComp) == true)
			return tmpComp;

		return getComponentAfter(focusCycleRoot, tmpComp);
	}

	@Override
	public Component getLastComponent(Container focusCycleRoot)
	{
		// Bail if no components to traverse
		if (itemL.isEmpty() == true)
			return null;

		var tmpComp = itemL.get(itemL.size() - 1);
		if (checkFocusability(tmpComp) == true)
			return tmpComp;

		return getComponentBefore(focusCycleRoot, tmpComp);
	}

	/**
	 * Utility method to test to see if a Component can grab the focus. To grab the focus a component must be: -
	 * focusable - enabled - visible - be in a container that is being shown.
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
