package glum.zio;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ZioObjUtil
{
	/**
	 * Utility method to read a list of ZioObj items. The objects are assumed to be of the same type.
	 * <P>
	 * Format: <numItems> (<ZioObj>)*
	 */
	public static <G1 extends ZioObj> ArrayList<G1> readList(ZinStream aStream, Class<G1> aClass) throws IOException
	{
		// Read the item count
		int numItems = aStream.readInt();

		// Read the actual objects
		ArrayList<G1> itemList = new ArrayList<G1>();
		for (int c1 = 0; c1 < numItems; c1++)
		{
			// Serialize the class
			try
			{
				G1 aItem = aClass.newInstance();
				aItem.zioRead(aStream);
				itemList.add(aItem);
			}
			catch(Exception aException)
			{
				throw new IOException("Failed to instantiate: " + aClass, aException);
			}
		}

		return itemList;
	}

	/**
	 * Utility method to read a preloaded list of ZioObj items. The passed in list must contain the exact number of items as that stored on disk and in the
	 * correct order.
	 * <P>
	 * Format: <numItems> (<ZioObj>)*
	 */
	public static void readList(ZinStream aStream, Collection<? extends ZioObj> aItemList) throws IOException
	{
		// Read the item count
		int numItems = aStream.readInt();
		if (numItems != aItemList.size())
			throw new IOException("Items stored: " + numItems + ". Expected: " + aItemList.size());

		// Read the actual BinRaw items
		for (ZioObj aItem : aItemList)
			aItem.zioRead(aStream);
	}

	/**
	 * Utility method to write out a list of ZioObj items.
	 * <P>
	 * Format: <numItems> (<ZioObj>)*
	 */
	public static void writeList(ZoutStream aStream, Collection<? extends ZioObj> aItemList) throws IOException
	{
		// Write the item count
		aStream.writeInt(aItemList.size());

		// Write the actual objects
		for (ZioObj aItem : aItemList)
			aItem.zioWrite(aStream);
	}

	/**
	 * Utility method to read a map of binary objects. The ZioObj items are assumed to be of the same type.
	 * <P>
	 * Format: <numItems> (<String, ZioObj>)*
	 */
	public static <G1 extends ZioObj> Map<String, G1> readMap(ZinStream aStream, Class<G1> aClass) throws IOException
	{
		// Read the item count
		int numItems = aStream.readInt();

		// Read the actual objects
		Map<String, G1> itemMap = new LinkedHashMap<String, G1>();
		for (int c1 = 0; c1 < numItems; c1++)
		{
			// Read the key
			String aKey = aStream.readString();

			// Read the value
			G1 aItem = read(aStream, aClass);
			itemMap.put(aKey, aItem);
		}

		return itemMap;
	}

	/**
	 * Utility method to read a map of ZioObj items. The items are assumed to be preloaded. Thus the passed in map must contain as many items (and in the order)
	 * as that which will be read in from the disk. It is therefore advisable that only LinkedHashMaps be used with this method.
	 */
	public static void readMap(ZinStream aStream, Map<String, ? extends ZioObj> aItemMap) throws IOException
	{
		// Read the item count
		int numItems = aStream.readInt();
		if (numItems != aItemMap.size())
			throw new IOException("Items stored: " + numItems + ". Expected: " + aItemMap.size());

		// Read the actual key,value pairings
		String[] keyArr = aItemMap.keySet().toArray(new String[0]);
		for (int c1 = 0; c1 < numItems; c1++)
		{
			// Read the key and ensure the proper key was read
			String aKey = aStream.readString();
			if (aKey.equals(keyArr[c1]) == false)
				throw new IOException("Key read: " + aKey + ". Expected: " + keyArr[c1]);

			// Read the value
			ZioObj aItem = aItemMap.get(aKey);
			aItem.zioRead(aStream);
		}
	}

	/**
	 * Utility method to write out a map of ZioObj items.
	 * <P>
	 * Format: <numItems> (<String, ZioObj>)*
	 */
	public static void writeMap(ZoutStream aStream, Map<String, ? extends ZioObj> aItemMap) throws IOException
	{
		// Write the item count
		aStream.writeInt(aItemMap.size());

		// Write the actual objects
		for (String aKey : aItemMap.keySet())
		{
			// Write the key
			aStream.writeString(aKey);

			// Write the value
			ZioObj aZioObj = aItemMap.get(aKey);
			aZioObj.zioWrite(aStream);
		}
	}

	public static <G1 extends ZioObj> G1 read(ZinStream aStream, Class<G1> aClass) throws IOException
	{
		G1 aItem;

		// Serialize the class
		try
		{
			aItem = aClass.newInstance();
			aItem.zioRead(aStream);
		}
		catch(Exception aException)
		{
			throw new IOException("Failed to instantiate: " + aClass, aException);
		}

		return aItem;
	}

	public static <G1 extends ZioObj> G1 readNullable(ZinStream aStream, Class<G1> aClass) throws IOException
	{
		boolean aBool = aStream.readBool();
		if (aBool == false)
			return null;

		// Serialize the class
		G1 aItem;
		try
		{
			aItem = aClass.newInstance();
			aItem.zioRead(aStream);
		}
		catch(Exception aException)
		{
			throw new IOException("Failed to instantiate: " + aClass, aException);
		}

		return aItem;
	}

	public static void writeNullable(ZoutStream aStream, ZioObj aItem) throws IOException
	{
		if (aItem == null)
		{
			aStream.writeBool(false);
			return;
		}

		aStream.writeBool(true);
		aItem.zioWrite(aStream);
	}

}
