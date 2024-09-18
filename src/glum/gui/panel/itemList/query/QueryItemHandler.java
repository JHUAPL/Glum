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
package glum.gui.panel.itemList.query;

import glum.database.QueryItem;
import glum.gui.panel.itemList.ItemHandler;

/**
 * Implementation of {@link ItemHandler} that allows for tabular access of items that implement the {@link QueryItem}
 * interface.
 *
 * @author lopeznr1
 */
public class QueryItemHandler<G1 extends QueryItem<G2>, G2 extends Enum<?>> implements ItemHandler<G1, G2>
{
	@Override
	public Object getValue(G1 aItem, G2 aEnum)
	{
		// Delegate
		return aItem.getValue(aEnum);
	}

	@Override
	public void setValue(G1 aItem, G2 aEnum, Object aValue)
	{
		// Delegate
		aItem.setValue(aEnum, aValue);
	}

}
