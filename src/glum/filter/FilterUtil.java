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
package glum.filter;

import java.util.ArrayList;
import java.util.List;

import javax.swing.JCheckBox;

import glum.gui.component.GList;
import glum.gui.component.GNumberField;

/**
 * Collection of utility methods used to aide with working with {@link Filter}s and (swing) UI components.
 *
 * @author lopeznr1
 */
public class FilterUtil
{
	/**
	 * Utility method to return a sublist of itemList based on aFilter.
	 */
	public static <G1> List<G1> applyFilter(List<G1> itemList, Filter<G1> aFilter)
	{
		var retItemL = new ArrayList<G1>();
		for (G1 aItem : itemList)
		{
			if (aFilter.isValid(aItem) == true)
				retItemL.add(aItem);
		}

		return retItemL;
	}

	/**
	 * Utility method to synchronize the specified filter with the associated GUI controls.
	 */
	public static void getEnumFilter(EnumFilter<?, Enum<?>> aFilter, JCheckBox mainCB, GList<Enum<?>> mainList)
	{
		aFilter.setIsEnabled(mainCB.isSelected());
		aFilter.setSetSelectedItems(mainList.getSelectedItems());
	}

	/**
	 * Utility method to synchronize the associated GUI controls with the specified filter.
	 */
	public static void setEnumFilter(EnumFilter<?, Enum<?>> aFilter, JCheckBox mainCB, GList<Enum<?>> mainList)
	{
		mainCB.setSelected(aFilter.getIsEnabled());
		mainList.setSelectedItems(aFilter.getSelectedItems());
	}

	/**
	 * Utility method to synchronize the specified filter with the associated GUI controls.
	 */
	public static void getRangeFilter(RangeFilter<?> aFilter, JCheckBox mainCB, JCheckBox minCB, JCheckBox maxCB,
			GNumberField minNF, GNumberField maxNF)
	{
		aFilter.setIsEnabled(mainCB.isSelected());
		aFilter.setUseMin(minCB.isSelected());
		aFilter.setUseMax(maxCB.isSelected());
		aFilter.setMinValue(minNF.getValue());
		aFilter.setMaxValue(maxNF.getValue());
	}

	/**
	 * Utility method to synchronize the associated GUI controls with the specified filter.
	 */
	public static void setRangeGui(RangeFilter<?> aFilter, JCheckBox mainCB, JCheckBox minCB, JCheckBox maxCB,
			GNumberField minNF, GNumberField maxNF)
	{
		mainCB.setSelected(aFilter.getIsEnabled());
		minCB.setSelected(aFilter.getUseMin());
		maxCB.setSelected(aFilter.getUseMax());
		minNF.setValue(aFilter.getMinValue());
		maxNF.setValue(aFilter.getMaxValue());
	}

	/**
	 * Utility method to keep the various GUI components associated with an EnumFilter synchronized. The mainList will be
	 * enabled/disabled based on the selection state of mainCB.
	 */
	public static void syncEnumGui(JCheckBox mainCB, GList<Enum<?>> mainList)
	{
		var isEnabled = mainCB.isSelected();
		mainList.setEnabled(isEnabled);
	}

	/**
	 * Utility method to keep the various GUI components associated with an RangeFilter synchronized. Gui components will
	 * be enabled/disabled based on the various check boxes.
	 */
	public static void syncRangeGui(JCheckBox mainCB, JCheckBox minCB, JCheckBox maxCB, GNumberField minNF,
			GNumberField maxNF)
	{
		var isEnabled = mainCB.isSelected();
		minCB.setEnabled(isEnabled);
		maxCB.setEnabled(isEnabled);
		minNF.setEnabled(isEnabled & minCB.isSelected());
		maxNF.setEnabled(isEnabled & maxCB.isSelected());
	}

}
