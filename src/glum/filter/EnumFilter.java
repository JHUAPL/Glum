package glum.filter;

import glum.zio.*;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public abstract class EnumFilter<G1, G2 extends Enum<?>> implements ZioObj, Filter<G1>
{
	// Static config vars
	private Map<Integer, Enum<?>> fullMap;
	
	// State vars
	private Set<Enum<?>> validSet;
	private boolean isEnabled;
	
	public EnumFilter(Class<? extends Enum<?>> enumClass)
	{
		fullMap = Maps.newLinkedHashMap();
		for (Enum<?> aEnum : enumClass.getEnumConstants())
			fullMap.put(aEnum.ordinal(), aEnum);
		
		validSet = Sets.newLinkedHashSet();
		isEnabled = false;
	}
	
	/**
	 * Returns true if the filter is active.
	 */
	public boolean getIsEnabled()
	{
		return isEnabled;
	}

	/**
	 * Returns the list of valid enums for this filter.
	 */
	public List<Enum<?>> getSelectedItems()
	{
		return Lists.newArrayList(validSet);
	}

	/**
	 * Sets this filter to match aFilter
	 */
	public void set(EnumFilter<G1, G2> aFilter)
	{
		validSet = Sets.newLinkedHashSet(aFilter.validSet);
		isEnabled = aFilter.getIsEnabled();
	}
	
	/**
	 * Sets whether the filter is active.
	 */
	public void setIsEnabled(boolean aBool)
	{
		isEnabled = aBool;
	}
	
	/**
	 * Sets the list of valid enums for this filter.
	 */
	public void setSetSelectedItems(List<Enum<?>> selectedItems)
	{
		validSet.clear();
		validSet.addAll(selectedItems);
	}
	
	@Override
	public void zioRead(ZinStream aStream) throws IOException
	{
		int numItems;
		
		aStream.readVersion(0);
		
		// Read the payload
		isEnabled = aStream.readBool();
		
		validSet.clear();
		numItems = aStream.readInt();
		for (int c1 = 0; c1 < numItems; c1++)
			validSet.add(fullMap.get(aStream.readInt()));
	}

	@Override
	public void zioWrite(ZoutStream aStream) throws IOException
	{
		int numItems;
		
		aStream.writeVersion(0);
		
		aStream.writeBool(isEnabled);

		numItems = validSet.size();
		aStream.writeInt(numItems);
		for (Enum<?> aEnum : validSet)
			aStream.writeInt(aEnum.ordinal());
	}

	/**
	 * Utility method that returns whether aValue is within the constraints
	 * specified by this filter.
	 */
	protected boolean testIsValid(G2 aEnum)
	{
		if (isEnabled == false)
			return true;
		
		return validSet.contains(aEnum);
	}

}
