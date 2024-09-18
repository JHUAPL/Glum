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
package glum.item;

import java.util.Set;

import com.google.common.collect.*;

/**
 * Collection of utility methods for working with ItemManagers.
 * 
 * @author lopeznr1
 */
public class ItemManagerUtil
{
	/**
	 * Utility method that will select all of the items in the specified ItemManager.
	 */
	public static <G1> void selectAll(ItemManager<G1> aManager)
	{
		aManager.setSelectedItems(aManager.getAllItems());
	}

	/**
	 * Utility method that will invert the selection of the specified ItemManager.
	 */
	public static <G1> void selectInvert(ItemManager<G1> aManager)
	{
		Set<G1> fullS = ImmutableSet.copyOf(aManager.getAllItems());
		Set<G1> pickS = ImmutableSet.copyOf(aManager.getSelectedItems());

		Set<G1> tmpS = Sets.difference(fullS, pickS);
		aManager.setSelectedItems(ImmutableList.copyOf(tmpS));
	}

	/**
	 * Utility method that will clear the selection of the specified ItemManager.
	 */
	public static <G1> void selectNone(ItemManager<G1> aManager)
	{
		aManager.setSelectedItems(ImmutableList.of());
	}

}
