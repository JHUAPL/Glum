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
package glum.gui.panel.itemList;

import java.util.*;

import glum.item.*;

/**
 * Base implementation of the interface {@link ItemProcessor}.
 * 
 * @author lopeznr1
 */
public abstract class BasicItemProcessor<G1> implements ItemProcessor<G1>
{
	private final List<ItemEventListener> listenerL;

	public BasicItemProcessor()
	{
		listenerL = new ArrayList<>();
	}

	@Override
	public synchronized void addListener(ItemEventListener aListener)
	{
		listenerL.add(aListener);
	}

	@Override
	public synchronized void delListener(ItemEventListener aListener)
	{
		listenerL.remove(aListener);
	}

	/**
	 * Helper method
	 */
	protected void notifyListeners()
	{
		// Get the listeners
		Collection<ItemEventListener> tmpListenerL;
		synchronized (this)
		{
			tmpListenerL = new ArrayList<>(listenerL);
		}

		// Send out the notifications
		for (ItemEventListener aListener : tmpListenerL)
			aListener.handleItemEvent(this, ItemEventType.ItemsChanged);
	}

}
