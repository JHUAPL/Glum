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
package glum.gui.panel.itemList.config;

import glum.gui.panel.itemList.ItemHandler;
import glum.gui.panel.itemList.ItemListPanel;
import glum.gui.panel.itemList.query.QueryAttribute;

/**
 * Implementation of {@link ItemHandler} that allows for tabular access of columns shown in a {@link ItemListPanel}.
 *
 * @author lopeznr1
 */
public class ConfigHandler<G1 extends Enum<?>> implements ItemHandler<QueryAttribute<G1>, ConfigLookUp>
{
	@Override
	public Object getValue(QueryAttribute<G1> aItem, ConfigLookUp aEnum)
	{
		switch (aEnum)
		{
			case IsVisible:
				return aItem.isVisible;

			case Name:
				return aItem.refKey;

			case Label:
				return aItem.label;

			default:
				break;
		}

		throw new RuntimeException("Unsupported enum:" + aEnum);
	}

	@Override
	public void setValue(QueryAttribute<G1> aItem, ConfigLookUp aEnum, Object aValue)
	{
		if (aEnum == ConfigLookUp.IsVisible)
		{
			aItem.isVisible = (Boolean) aValue;
			return;
		}

		throw new RuntimeException("Unsupported enum:" + aEnum);
	}

}
