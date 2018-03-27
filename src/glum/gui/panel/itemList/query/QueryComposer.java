package glum.gui.panel.itemList.query;

import java.util.*;

import javax.swing.JLabel;
import javax.swing.JTextField;
import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

import com.google.common.base.Preconditions;

import glum.unit.UnitProvider;

public class QueryComposer<G1 extends Enum<?>>
{
	// State vars
	protected ArrayList<QueryAttribute> itemList;
//	protected Map<G1, QueryAttribute> itemMap;

	public QueryComposer()
	{
		itemList = new ArrayList<>();
//		itemMap = Maps.newLinkedHashMap();
	}

	/**
	 * Return the QueryAttribute located at aIndex
	 */
	public QueryAttribute get(int aIndex)
	{
		return itemList.get(aIndex);
	}

	/**
	 * Return the QueryAttribute associated with aRefKey
	 */
	public QueryAttribute getItem(G1 aRefKey)
	{
		for (QueryAttribute aItem : itemList)
		{
			if (aItem.refKey == aRefKey)
				return aItem;
		}

		return null;
	}

	/**
	 * Returns a listing of all the QueryAttributes that were composed
	 */
	public Collection<QueryAttribute> getItems()
	{
		return new ArrayList<>(itemList);
	}

	/**
	 * Returns a listing of the items found between sIndex, eIndex (inclusive)
	 */
	public Collection<QueryAttribute> getItemsFrom(int sIndex, int eIndex)
	{
		List<QueryAttribute> rList;

		rList = new ArrayList<>((eIndex - sIndex) + 1);
		for (int c1 = sIndex; c1 <= eIndex; c1++)
			rList.add(itemList.get(c1));

		return rList;
	}

	/**
	 * Returns a listing of the items found between sKey, eKey (inclusive)
	 */
	public Collection<QueryAttribute> getItemsFrom(G1 sKey, G1 eKey)
	{
		int sIndex, eIndex;

		// Locate the indexes for the coresponding elements
		sIndex = eIndex = -1;
		for (int c1 = 0; c1 < itemList.size(); c1++)
		{
			if (itemList.get(c1).refKey == sKey)
				sIndex = c1;
			if (itemList.get(c1).refKey == eKey)
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
		return itemList.size();
	}

	/**
	 * Method to add a QueryAttribute to this container
	 */
	public QueryAttribute addAttribute(G1 aRefKey, UnitProvider aUnitProvider, String aName, String maxValue)
	{
		return addAttribute(aRefKey, aUnitProvider, aName, maxValue, true);
	}

	public QueryAttribute addAttribute(G1 aRefKey, UnitProvider aUnitProvider, String aName, String maxValue, boolean isVisible)
	{
		QueryAttribute aAttribute;

		// Insanity check
		Preconditions.checkNotNull(aUnitProvider);

		aAttribute = addAttribute(aRefKey, Double.class, aName, maxValue, isVisible);
		aAttribute.refUnitProvider = aUnitProvider;
		return aAttribute;
	}

	public QueryAttribute addAttribute(G1 aRefKey, Class<?> aClass, String aName, String maxValue)
	{
		return addAttribute(aRefKey, aClass, aName, maxValue, true);
	}

	public QueryAttribute addAttribute(G1 aRefKey, Class<?> aClass, String aName, String maxValue, boolean isVisible)
	{
		int maxSize;

		// Compute the maxSize
		maxSize = Integer.MAX_VALUE;
		if (maxValue != null)
			maxSize = computeStringWidth(maxValue) + 15;

		return addAttribute(aRefKey, aClass, aName, maxSize, isVisible);
	}

	public QueryAttribute addAttribute(G1 aRefKey, Class<?> aClass, String aName, int aMaxSize)
	{
		return addAttribute(aRefKey, aClass, aName, aMaxSize, true);
	}

	public QueryAttribute addAttribute(G1 aRefKey, Class<?> aClass, String aLabel, int aMaxSize, boolean isVisible)
	{
		QueryAttribute aAttribute;
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
		aAttribute = new QueryAttribute(itemList.size());
		aAttribute.refKey = aRefKey;
		aAttribute.refClass = aClass;
		aAttribute.label = aLabel;
		aAttribute.defaultSize = defaultSize;
		aAttribute.maxSize = maxSize;
		aAttribute.minSize = minSize;
		aAttribute.isVisible = isVisible;

		itemList.add(aAttribute);
		return aAttribute;
	}

	public QueryAttribute addAttribute(G1 aRefKey, Enum<?> enumSet[], String aName)
	{
		return addAttribute(aRefKey, enumSet, aName, true);
	}

	public QueryAttribute addAttribute(G1 aRefKey, Enum<?> enumSet[], String aLabel, boolean isVisible)
	{
		QueryAttribute aAttribute;
		int defaultSize, aSize;

		// Get the defaultSize
		defaultSize = 10;
		if (aLabel != null)
			defaultSize = computeStringWidth(aLabel);

		for (Enum<?> aEnum : enumSet)
		{
			aSize = computeStringWidth(aEnum.toString());
			if (aSize > defaultSize)
				defaultSize = aSize;
		}

		// Form the attribute
		aAttribute = new QueryAttribute(itemList.size());
		aAttribute.refKey = aRefKey;
		aAttribute.refClass = Enum.class;
		aAttribute.label = aLabel;
		aAttribute.defaultSize = defaultSize;
		aAttribute.maxSize = defaultSize;
		aAttribute.minSize = 15;
		aAttribute.isVisible = isVisible;

		itemList.add(aAttribute);
		return aAttribute;
	}

	/**
	 * Method to set in a custom Editor for the QueryAttribute associated with aRefKey
	 */
	public void setEditor(G1 aRefKey, TableCellEditor aEditor)
	{
		for (QueryAttribute aItem : itemList)
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
		for (QueryAttribute aItem : itemList)
		{
			if (aItem.refKey == aRefKey)
			{
				aItem.renderer = aRenderer;
				return;
			}
		}

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
