package glum.zio.raw;

import glum.zio.ZinStream;
import glum.zio.ZoutStream;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;

public class ZioRawUtil
{
	/**
	 * Utility method to read a list of BinRaw items. The objects are assumed to be of the same type.
	 * <P>
	 * Format: <numItems> (<BinRaw>)*
	 */
	public static <G1 extends ZioRaw> ArrayList<G1> readRawList(ZinStream aStream, Class<G1> binClass) throws IOException
	{
		ArrayList<G1> itemList;
		G1 aItem;
		int numItems;

		// Read the item count
		numItems = aStream.readInt();

		// Read the actual objects
		itemList = new ArrayList<G1>();
		for (int c1 = 0; c1 < numItems; c1++)
		{
			// Serialize the class
			try
			{
				aItem = binClass.newInstance();
				aItem.zioReadRaw(aStream);
				itemList.add(aItem);
			}
			catch (Exception aException)
			{
				throw new IOException("Failed to instantiate: " + binClass, aException);
			}
		}

		return itemList;
	}

	/**
	 * Utility method to read a preloaded list of BinRaw items. The passed in list must contain the exact number of items
	 * as that stored on disk and in the correct order.
	 * <P>
	 * Format: <numItems> (<BinRaw>)*
	 */
	public static void readRawList(ZinStream aStream, Collection<? extends ZioRaw> itemList) throws IOException
	{
		int numItems;

		// Read the item count
		numItems = aStream.readInt();
		if (numItems != itemList.size())
			throw new IOException("Items stored: " + numItems + ". Expected: " + itemList.size());

		// Read the actual BinRaw items
		for (ZioRaw aItem : itemList)
			aItem.zioReadRaw(aStream);
	}

	/**
	 * Utility method to write out a list of BinRaw items.
	 * <P>
	 * Format: <numItems> (<BinRaw>)*
	 */
	public static void writeRawList(ZoutStream aStream, Collection<? extends ZioRaw> itemList) throws IOException
	{
		// Write the item count
		aStream.writeInt(itemList.size());

		// Write the actual objects
		for (ZioRaw aItem : itemList)
			aItem.zioWriteRaw(aStream);
	}

	/**
	 * Utility method to read a map of binary objects. The BinRaw items are assumed to be of the same type.
	 * <P>
	 * Format: <numItems> (<String, BinRaw>)*
	 */
	public static <G1 extends ZioRaw> Map<String, G1> readRawMap(ZinStream aStream, Class<G1> binClass) throws IOException
	{
		Map<String, G1> itemMap;
		String aKey;
		G1 aItem;
		int numItems;

		// Read the item count
		numItems = aStream.readInt();

		// Read the actual objects
		itemMap = new LinkedHashMap<String, G1>();
		for (int c1 = 0; c1 < numItems; c1++)
		{
			// Read the key
			aKey = aStream.readString();

			// Read the value
			aItem = readRaw(aStream, binClass);
			itemMap.put(aKey, aItem);
		}

		return itemMap;
	}

	/**
	 * Utility method to read a map of raw binary items. The items are assumed to be preloaded. Thus the passed in map
	 * must contain as many items (and in the order) as that which will be read in from the disk. It is therefore
	 * advisable that only LinkedHashMaps be used with this method.
	 */
	public static void readRawMap(ZinStream aStream, Map<String, ? extends ZioRaw> itemMap) throws IOException
	{
		String[] keyArr;
		String aKey;
		ZioRaw aItem;
		int numItems;

		// Read the item count
		numItems = aStream.readInt();
		if (numItems != itemMap.size())
			throw new IOException("Items stored: " + numItems + ". Expected: " + itemMap.size());

		// Read the actual key,value pairings
		keyArr = itemMap.keySet().toArray(new String[0]);
		for (int c1 = 0; c1 < numItems; c1++)
		{
			// Read the key and ensure the proper key was read
			aKey = aStream.readString();
			if (aKey.equals(keyArr[c1]) == false)
				throw new IOException("Key read: " + aKey + ". Expected: " + keyArr[c1]);

			// Read the value
			aItem = itemMap.get(aKey);
			aItem.zioReadRaw(aStream);
		}
	}

	/**
	 * Utility method to write out a map of binary raw items.
	 * <P>
	 * Format: <numItems> (<String, BinRaw>)*
	 */
	public static void writeRawMap(ZoutStream aStream, Map<String, ? extends ZioRaw> itemMap) throws IOException
	{
		ZioRaw aBinObj;

		// Write the item count
		aStream.writeInt(itemMap.size());

		// Write the actual objects
		for (String aKey : itemMap.keySet())
		{
			// Write the key
			aStream.writeString(aKey);

			// Write the value
			aBinObj = itemMap.get(aKey);
			aBinObj.zioWriteRaw(aStream);
		}
	}

	public static <G1 extends ZioRaw> G1 readRaw(ZinStream aStream, Class<G1> binClass) throws IOException
	{
		G1 aItem;

		// Serialize the class
		try
		{
			aItem = binClass.newInstance();
			aItem.zioReadRaw(aStream);
		}
		catch (Exception aException)
		{
			throw new IOException("Failed to instantiate: " + binClass, aException);
		}

		return aItem;
	}

	public static <G1 extends ZioRaw> G1 readNullableRaw(ZinStream aStream, Class<G1> binClass) throws IOException
	{
		boolean aBool;
		G1 aItem;

		aBool = aStream.readBool();
		if (aBool == false)
			return null;

		// Serialize the class
		try
		{
			aItem = binClass.newInstance();
			aItem.zioReadRaw(aStream);
		}
		catch (Exception aException)
		{
			throw new IOException("Failed to instantiate: " + binClass, aException);
		}

		return aItem;
	}

	public static void writeNullableRaw(ZoutStream aStream, ZioRaw aItem) throws IOException
	{
		if (aItem == null)
		{
			aStream.writeBool(false);
			return;
		}

		aStream.writeBool(true);
		aItem.zioWriteRaw(aStream);
	}

}
