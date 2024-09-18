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

import java.util.List;

import javax.swing.Icon;

/**
 * Interface that allows customization of the sort icons.
 * 
 * @author lopeznr1
 */
public interface SortIconProvider
{
	/**
	 * Returns a list of icons that will be utilized as the sort icon indicators (when the items are sorted ascending).
	 * Icons at the front of the list will be associated with columns of data that have a higher sort priority.
	 * 
	 * @param aSize
	 *        The size of the sort icons in pixels.
	 * @return
	 */
	public List<Icon> getIconsForSortAsce(int aSize);

	/**
	 * Returns a list of icons that will be utilized as the sort icon indicators (when the items are sorted descending).
	 * Icons at the front of the list will be associated with columns of data that have a higher sort priority.
	 * 
	 * @param aSize
	 *        The size of the sort icons in pixels.
	 * @return
	 */
	public List<Icon> getIconsForSortDesc(int aSize);
}
