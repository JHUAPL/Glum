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
package glum.gui.table.sort;

import java.awt.Color;
import java.util.*;

import javax.swing.Icon;

import com.google.common.collect.ImmutableList;

/**
 * Default implementation of the SortIconProvider interface.
 * <p>
 * This SortIconProvider provides a flexible number of "priorities" based on the list of colors provided at construction
 * time.
 * 
 * @author lopeznr1
 */
public class DefaultSortIconProvider implements SortIconProvider
{
	// Constants
	private static final List<Color> DefaultColorL = ImmutableList.of(Color.RED, Color.RED.darker(), Color.BLACK,
			Color.LIGHT_GRAY);
	public static final DefaultSortIconProvider Default = new DefaultSortIconProvider(DefaultColorL);

	// Attributes
	private final ImmutableList<Color> refColorL;

	/**
	 * Standard Constructor
	 * 
	 * @param aColorL
	 *        The list of colors for each priority. Colors at the front of the list are associated with a higher
	 *        priority.
	 */
	public DefaultSortIconProvider(List<Color> aColorL)
	{
		refColorL = ImmutableList.copyOf(aColorL);
	}

	@Override
	public List<Icon> getIconsForSortAsce(int aSize)
	{
		List<Icon> retL = new ArrayList<>();

		for (Color aColor : refColorL)
			retL.add(new SortArrow(false, aSize, aColor));

		return retL;
	}

	@Override
	public List<Icon> getIconsForSortDesc(int aSize)
	{
		List<Icon> retL = new ArrayList<>();

		for (Color aColor : refColorL)
			retL.add(new SortArrow(true, aSize, aColor));

		return retL;
	}

}
