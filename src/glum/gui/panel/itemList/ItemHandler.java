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

/**
 * Interface which allows standardized mechanism for getting and setting of values associated with an item.
 *
 * @param <G1>
 *        Generic associated with the item.
 * @param <G2>
 *        Generic enum that defines the "names" of values to be accessed (get / set).
 *
 * @author lopeznr1
 */
public interface ItemHandler<G1, G2 extends Enum<?>>
{
	/**
	 * Returns the appropriate data field within the item for the specified lookup enum.
	 *
	 * @param aItem
	 * @param aEnum
	 */
	public Object getValue(G1 aItem, G2 aEnum);

	/**
	 * Updates the appropriate data field within the item for the specified lookup enum (with the provided value).
	 *
	 * @param aItem
	 * @param aEnum
	 * @param aValue
	 */
	public void setValue(G1 aItem, G2 aEnum, Object aValue);

}
