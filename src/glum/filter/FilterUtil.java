package glum.filter;

import java.util.List;

import javax.swing.JCheckBox;

import com.google.common.collect.Lists;

import glum.gui.component.GList;
import glum.gui.component.GNumberField;

public class FilterUtil
{
	/**
	 * Utility method to return a sublist of itemList based on aFilter.
	 */
	public static <G1> List<G1> applyFilter(List<G1> itemList, Filter<G1> aFilter)
	{
		List<G1> retList;
		
		retList = Lists.newArrayList();
		for (G1 aItem : itemList)
		{
			if (aFilter.isValid(aItem) == true)
				retList.add(aItem);
		}

		return retList;
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
	public static void getRangeFilter(RangeFilter<?> aFilter, JCheckBox mainCB, JCheckBox minCB, JCheckBox maxCB, GNumberField minNF, GNumberField maxNF)
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
	public static void setRangeGui(RangeFilter<?> aFilter, JCheckBox mainCB, JCheckBox minCB, JCheckBox maxCB, GNumberField minNF, GNumberField maxNF)
	{
		mainCB.setSelected(aFilter.getIsEnabled());
		minCB.setSelected(aFilter.getUseMin());
		maxCB.setSelected(aFilter.getUseMax());
		minNF.setValue(aFilter.getMinValue());
		maxNF.setValue(aFilter.getMaxValue());
	}

	/**
	 * Utility method to keep the various GUI components associated with an EnumFilter synchronized.
	 * The mainList will be enabled/disabled based on the selection state of mainCB.
	 */
	public static void syncEnumGui(JCheckBox mainCB, GList<Enum<?>> mainList)
	{
		boolean isEnabled;
		
		isEnabled = mainCB.isSelected();
		mainList.setEnabled(isEnabled);
	}

	/**
	 * Utility method to keep the various GUI components associated with an RangeFilter synchronized.
	 * Gui components will be enabled/disabled based on the various check boxes.
	 */
	public static void syncRangeGui(JCheckBox mainCB, JCheckBox minCB, JCheckBox maxCB, GNumberField minNF, GNumberField maxNF)
	{
		boolean isEnabled;
		
		isEnabled = mainCB.isSelected();
		minCB.setEnabled(isEnabled);
		maxCB.setEnabled(isEnabled);
		minNF.setEnabled(isEnabled & minCB.isSelected());
		maxNF.setEnabled(isEnabled & maxCB.isSelected());
	}

}
