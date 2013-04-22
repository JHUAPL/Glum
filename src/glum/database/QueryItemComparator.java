package glum.database;

import java.util.Comparator;

public class QueryItemComparator<G1 extends QueryItem<G2>, G2 extends Enum<?>> implements Comparator<G1>
{
	private G2 sortKey;

	public QueryItemComparator(G2 aSortKey)
	{
		sortKey = aSortKey;
	}

	@SuppressWarnings("unchecked")
	@Override
	public int compare(G1 item1, G1 item2)
	{
		Comparable<Object> value1, value2;

		value1 = (Comparable<Object>)item1.getValue(sortKey);
		value2 = (Comparable<Object>)item2.getValue(sortKey);

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
	 * <P>
	 * This logic is here due to Java's horrible implementation off generics.
	 */
	public static <G3 extends QueryItem<G4>, G4 extends Enum<?>> Comparator<G3> spawn(Class<G3> aClass, G4 aEnum)
	{
		QueryItemComparator<G3, G4> retComparator;

		retComparator = new QueryItemComparator<G3, G4>(aEnum);
		return retComparator;
	}

	/**
	 * Utility method to create a QueryItemComparator by specifying just the Enum. Note this method can not be used in a
	 * argument to another method; instead use: {@link #spawn(Class, Enum)}
	 * <P>
	 * This logic is here due to Java's horrible implementation off generics.
	 */
	public static <G3 extends QueryItem<G4>, G4 extends Enum<?>> Comparator<G3> spawn(G4 aEnum)
	{
		QueryItemComparator<G3, G4> retComparator;

		retComparator = new QueryItemComparator<G3, G4>(aEnum);
		return retComparator;
	}

}
