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

import java.io.IOException;
import java.util.*;

import glum.zio.*;

/**
 * A {@link Filter} which performs filtering based on a enam values.
 *
 * @author lopeznr1
 *
 * @param <G1>
 */
public abstract class EnumFilter<G1, G2 extends Enum<?>> implements ZioObj, Filter<G1>
{
	// Static config vars
	private Map<Integer, Enum<?>> fullMap;

	// State vars
	private Set<Enum<?>> validS;
	private boolean isEnabled;

	/** Standard Constructor */
	public EnumFilter(Class<? extends Enum<?>> enumClass)
	{
		fullMap = new LinkedHashMap<>();
		for (Enum<?> aEnum : enumClass.getEnumConstants())
			fullMap.put(aEnum.ordinal(), aEnum);

		validS = new LinkedHashSet<>();
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
		return new ArrayList<>(validS);
	}

	/**
	 * Sets this filter to match aFilter
	 */
	public void set(EnumFilter<G1, G2> aFilter)
	{
		validS = new LinkedHashSet<>(aFilter.validS);
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
	public void setSetSelectedItems(List<Enum<?>> aItemL)
	{
		validS.clear();
		validS.addAll(aItemL);
	}

	@Override
	public void zioRead(ZinStream aStream) throws IOException
	{
		aStream.readVersion(0);

		// Read the payload
		isEnabled = aStream.readBool();

		validS.clear();
		var numItems = aStream.readInt();
		for (int c1 = 0; c1 < numItems; c1++)
			validS.add(fullMap.get(aStream.readInt()));
	}

	@Override
	public void zioWrite(ZoutStream aStream) throws IOException
	{
		aStream.writeVersion(0);

		aStream.writeBool(isEnabled);

		var numItems = validS.size();
		aStream.writeInt(numItems);
		for (Enum<?> aEnum : validS)
			aStream.writeInt(aEnum.ordinal());
	}

	/**
	 * Utility method that returns whether aValue is within the constraints specified by this filter.
	 */
	protected boolean testIsValid(G2 aEnum)
	{
		if (isEnabled == false)
			return true;

		return validS.contains(aEnum);
	}

}
