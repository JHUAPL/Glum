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
package glum.database;

import java.util.Comparator;

/**
 * {@link Comparable} to sort {@link QueryItem}s based on the provided key.
 *
 * @author lopeznr1
 */
public class QueryItemComparator<G1 extends QueryItem<G2>, G2 extends Enum<?>> implements Comparator<G1>
{
	// Attributes
	private final G2 sortKey;

	/** Standard Constructor */
	public QueryItemComparator(G2 aSortKey)
	{
		sortKey = aSortKey;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compare(G1 item1, G1 item2)
	{
		var value1 = (Comparable<Object>) item1.getValue(sortKey);
		var value2 = (Comparable<Object>) item2.getValue(sortKey);

		if (value1 == null && value2 == null)
			return 0;

		if (value1 == null)
			return -1;

		if (value2 == null)
			return 1;

		return value1.compareTo(value2);
	}

	/**
	 * Utility method to create a QueryItemComparator by specifying the class and sort Enum.
	 * <p>
	 * This logic is here due to Java's horrible implementation off generics.
	 */
	public static <G3 extends QueryItem<G4>, G4 extends Enum<?>> Comparator<G3> spawn(Class<G3> aClass, G4 aEnum)
	{
		return new QueryItemComparator<G3, G4>(aEnum);
	}

	/**
	 * Utility method to create a QueryItemComparator by specifying just the Enum. Note this method can not be used in a
	 * argument to another method; instead use: {@link #spawn(Class, Enum)}
	 * <p>
	 * This logic is here due to Java's horrible implementation off generics.
	 */
	public static <G3 extends QueryItem<G4>, G4 extends Enum<?>> Comparator<G3> spawn(G4 aEnum)
	{
		return new QueryItemComparator<G3, G4>(aEnum);
	}

}
