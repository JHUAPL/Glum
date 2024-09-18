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

import java.util.*;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.google.common.base.Preconditions;

import glum.gui.panel.itemList.ItemListPanel;
import glum.unit.UnitProvider;

/**
 * Object which allows a collection of QueryAttributes to be programmatically constructed. This object provides for a
 * simplified and uniform method for construction the {@link QueryAttribute}s that will be associated with a
 * {@link ItemListPanel}.
 *
 * @param <G1>
 * 
 * @author lopeznr1
 */
public class QueryComposer<G1 extends Enum<?>>
{
	// State vars
	protected ArrayList<QueryAttribute<G1>> itemL;
//	protected Map<G1, QueryAttribute<G1>> itemM;

	public QueryComposer()
	{
		itemL = new ArrayList<>();
//		itemM = new LinkedHashMap<>();
	}

	/**
	 * Return the QueryAttribute located at aIndex
	 */
	public QueryAttribute<G1> get(int aIndex)
	{
		return itemL.get(aIndex);
	}

	/**
	 * Return the QueryAttribute associated with aRefKey
	 */
	public QueryAttribute<G1> getItem(G1 aRefKey)
	{
		for (QueryAttribute<G1> aItem : itemL)
		{
			if (aItem.refKey == aRefKey)
				return aItem;
		}

		return null;
	}

	/**
	 * Returns a listing of all the QueryAttributes that were composed
	 */
	public Collection<QueryAttribute<G1>> getItems()
	{
		return new ArrayList<>(itemL);
	}

	/**
	 * Returns a listing of the items found between sIndex, eIndex (inclusive)
	 */
	public Collection<QueryAttribute<G1>> getItemsFrom(int sIndex, int eIndex)
	{
		List<QueryAttribute<G1>> retL;

		retL = new ArrayList<>((eIndex - sIndex) + 1);
		for (int c1 = sIndex; c1 <= eIndex; c1++)
			retL.add(itemL.get(c1));

		return retL;
	}

	/**
	 * Returns a listing of the items found between sKey, eKey (inclusive)
	 */
	public Collection<QueryAttribute<G1>> getItemsFrom(G1 sKey, G1 eKey)
	{
		int sIndex, eIndex;

		// Locate the indexes for the corresponding elements
		sIndex = eIndex = -1;
		for (int c1 = 0; c1 < itemL.size(); c1++)
		{
			if (itemL.get(c1).refKey == sKey)
				sIndex = c1;
			if (itemL.get(c1).refKey == eKey)
				eIndex = c1;
		}

		// Insanity checks
		if (sIndex == -1)
			throw new RuntimeException("Failure. Key is not in composer: sKey: " + sKey);
		if (eIndex == -1)
			throw new RuntimeException("Failure. Key is not in composer: eKey: " + eKey);
		if (sIndex > eIndex)
			throw new RuntimeException("Failure: eKey: (" + eKey + ") appears before for sKey: (" + sKey + ")");

		return getItemsFrom(sIndex, eIndex);
	}

	/**
	 * Returns the num of items in the composition
	 */
	public int size()
	{
		return itemL.size();
	}

	/**
	 * Method to add a QueryAttribute to this container
	 */
	public QueryAttribute<G1> addAttribute(G1 aRefKey, UnitProvider aUnitProvider, String aName, String maxValue)
	{
		return addAttribute(aRefKey, aUnitProvider, aName, maxValue, true);
	}

	public QueryAttribute<G1> addAttribute(G1 aRefKey, UnitProvider aUnitProvider, String aName, String maxValue,
			boolean isVisible)
	{
		QueryAttribute<G1> retAttribute;

		// Insanity check
		Preconditions.checkNotNull(aUnitProvider);

		retAttribute = addAttribute(aRefKey, Double.class, aName, maxValue, isVisible);
		retAttribute.refUnitProvider = aUnitProvider;
		return retAttribute;
	}

	public QueryAttribute<G1> addAttribute(G1 aRefKey, Class<?> aClass, String aName, String maxValue)
	{
		return addAttribute(aRefKey, aClass, aName, maxValue, true);
	}

	public QueryAttribute<G1> addAttribute(G1 aRefKey, Class<?> aClass, String aName, String maxValue, boolean isVisible)
	{
		int maxSize;

		// Compute the maxSize
		maxSize = Integer.MAX_VALUE;
		if (maxValue != null)
			maxSize = computeStringWidth(maxValue) + 15;

		return addAttribute(aRefKey, aClass, aName, maxSize, isVisible);
	}

	public QueryAttribute<G1> addAttribute(G1 aRefKey, Class<?> aClass, String aName, int aMaxSize)
	{
		return addAttribute(aRefKey, aClass, aName, aMaxSize, true);
	}

	public QueryAttribute<G1> addAttribute(G1 aRefKey, Class<?> aClass, String aLabel, int aMaxSize, boolean isVisible)
	{
		QueryAttribute<G1> retAttribute;
		int defaultSize, minSize, maxSize;

		// Get the defaultSize
		defaultSize = 15;
		if (aLabel != null)
			defaultSize = computeStringWidth(aLabel);

		minSize = 15;
		maxSize = aMaxSize;
		if (defaultSize < minSize)
			defaultSize = minSize;
		if (maxSize < defaultSize)
			maxSize = defaultSize;

		// Set the defaultSize to be maxSize (unless it is ~infinite)
		if (maxSize != Integer.MAX_VALUE)
			defaultSize = maxSize;

		// Form the attribute
		retAttribute = new QueryAttribute<>(aRefKey, aClass, itemL.size());
		retAttribute.label = aLabel;
		retAttribute.defaultSize = defaultSize;
		retAttribute.maxSize = maxSize;
		retAttribute.minSize = minSize;
		retAttribute.isVisible = isVisible;

		itemL.add(retAttribute);
		return retAttribute;
	}

	public QueryAttribute<G1> addAttribute(G1 aRefKey, Enum<?> enumSet[], String aName)
	{
		return addAttribute(aRefKey, enumSet, aName, true);
	}

	public QueryAttribute<G1> addAttribute(G1 aRefKey, Enum<?> enumSet[], String aLabel, boolean isVisible)
	{
		QueryAttribute<G1> retAttribute;
		int defaultSize, tmpSize;

		// Get the defaultSize
		defaultSize = 10;
		if (aLabel != null)
			defaultSize = computeStringWidth(aLabel);

		for (Enum<?> aEnum : enumSet)
		{
			tmpSize = computeStringWidth(aEnum.toString());
			if (tmpSize > defaultSize)
				defaultSize = tmpSize;
		}

		// Form the attribute
		retAttribute = new QueryAttribute<>(aRefKey, Enum.class, itemL.size());
		retAttribute.label = aLabel;
		retAttribute.defaultSize = defaultSize;
		retAttribute.maxSize = defaultSize;
		retAttribute.minSize = 15;
		retAttribute.isVisible = isVisible;

		itemL.add(retAttribute);
		return retAttribute;
	}

	/**
	 * Method to set in a custom Editor for the QueryAttribute associated with aRefKey
	 */
	public void setEditor(G1 aRefKey, TableCellEditor aEditor)
	{
		for (QueryAttribute<G1> aItem : itemL)
		{
			if (aItem.refKey == aRefKey)
			{
				aItem.editor = aEditor;
				return;
			}
		}

		throw new RuntimeException("No item found with the key:" + aRefKey);
	}

	/**
	 * Method to set in a custom Renderer for the QueryAttribute associated with aRefKey
	 */
	public void setRenderer(G1 aRefKey, TableCellRenderer aRenderer)
	{
		boolean isPass = false;
		for (QueryAttribute<G1> aItem : itemL)
		{
			if (aItem.refKey == aRefKey)
			{
				aItem.renderer = aRenderer;
				isPass = true;
			}
		}

		if (isPass == false)
			throw new RuntimeException("No item found with the key:" + aRefKey);
	}

	/**
	 * Utility method to compute the width of a string with the standard font
	 */
	protected int computeStringWidth(String aStr)
	{
		JLabel tmpL;

		tmpL = new JLabel(aStr);
		tmpL.setFont((new JTextField()).getFont());

		return tmpL.getPreferredSize().width + 5;
	}

}
