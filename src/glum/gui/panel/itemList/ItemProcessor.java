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

import com.google.common.collect.ImmutableList;

import glum.item.ItemEventListener;

/**
 * Interface that defines methods for accessing a collection of items. Support is provided to allow notification of when
 * the items change via the ItemEventListener mechanism.
 * 
 * @author lopeznr1
 */
public interface ItemProcessor<G1>
{
	/**
	 * Adds a {@link ItemEventListener} to this ItemProcessor.
	 */
	public void addListener(ItemEventListener aListener);

	/**
	 * Removes a {@link ItemEventListener} from this ItemProcessor.
	 */
	public void delListener(ItemEventListener aListener);

	/**
	 * Returns a list of all items in this ItemProcessor
	 */
	public ImmutableList<G1> getAllItems();

	/**
	 * Returns the number of items in this ItemProcessor
	 */
	public int getNumItems();

}
