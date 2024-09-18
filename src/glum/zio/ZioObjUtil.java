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
package glum.zio;

import java.io.IOException;
import java.util.*;

/**
 * Collection of utility methods for working with {@link ZioObj} objects.
 *
 * @author lopeznr1
 */
public class ZioObjUtil
{
	/**
	 * Utility method to read a list of {@link ZioObj} items. The objects are assumed to be of the same type.
	 * <p>
	 * Format: &lt;numItems> (&lt;ZioObj>)&ast;
	 */
	public static <G1 extends ZioObj> ArrayList<G1> readList(ZinStream aStream, Class<G1> aClass) throws IOException
	{
		// Read the item count
		int numItems = aStream.readInt();

		// Read the actual objects
		ArrayList<G1> retItemL = new ArrayList<G1>();
		for (int c1 = 0; c1 < numItems; c1++)
		{
			// Serialize the class
			try
			{
				G1 aItem = aClass.getDeclaredConstructor().newInstance();
				aItem.zioRead(aStream);
				retItemL.add(aItem);
			}
			catch(Exception aException)
			{
				throw new IOException("Failed to instantiate: " + aClass, aException);
			}
		}

		return retItemL;
	}

	/**
	 * Utility method to read a preloaded list of ZioObj items. The passed in list must contain the exact number of items
	 * as that stored on disk and in the correct order.
	 * <p>
	 * Format: &lt;numItems> (&lt;ZioObj>)&ast;
	 */
	public static void readList(ZinStream aStream, Collection<? extends ZioObj> aItemL) throws IOException
	{
		// Read the item count
		int numItems = aStream.readInt();
		if (numItems != aItemL.size())
			throw new IOException("Items stored: " + numItems + ". Expected: " + aItemL.size());

		// Read the actual BinRaw items
		for (ZioObj aItem : aItemL)
			aItem.zioRead(aStream);
	}

	/**
	 * Utility method to write out a list of ZioObj items.
	 * <p>
	 * Format: &lt;numItems> (&lt;ZioObj>)&ast;
	 */
	public static void writeList(ZoutStream aStream, Collection<? extends ZioObj> aItemL) throws IOException
	{
		// Write the item count
		aStream.writeInt(aItemL.size());

		// Write the actual objects
		for (ZioObj aItem : aItemL)
			aItem.zioWrite(aStream);
	}

	/**
	 * Utility method to read a map of binary objects. The ZioObj items are assumed to be of the same type.
	 * <p>
	 * Format: &lt;numItems> (&lt;String, ZioObj>)&ast;
	 */
	public static <G1 extends ZioObj> Map<String, G1> readMap(ZinStream aStream, Class<G1> aClass) throws IOException
	{
		// Read the item count
		int numItems = aStream.readInt();

		// Read the actual objects
		Map<String, G1> retItemM = new LinkedHashMap<String, G1>();
		for (int c1 = 0; c1 < numItems; c1++)
		{
			// Read the key
			String aKey = aStream.readString();

			// Read the value
			G1 aItem = read(aStream, aClass);
			retItemM.put(aKey, aItem);
		}

		return retItemM;
	}

	/**
	 * Utility method to read a map of ZioObj items. The items are assumed to be preloaded. Thus the passed in map must
	 * contain as many items (and in the order) as that which will be read in from the disk. It is therefore advisable
	 * that only LinkedHashMaps be used with this method.
	 */
	public static void readMap(ZinStream aStream, Map<String, ? extends ZioObj> aItemM) throws IOException
	{
		// Read the item count
		int numItems = aStream.readInt();
		if (numItems != aItemM.size())
			throw new IOException("Items stored: " + numItems + ". Expected: " + aItemM.size());

		// Read the actual key,value pairings
		String[] keyArr = aItemM.keySet().toArray(new String[0]);
		for (int c1 = 0; c1 < numItems; c1++)
		{
			// Read the key and ensure the proper key was read
			String aKey = aStream.readString();
			if (aKey.equals(keyArr[c1]) == false)
				throw new IOException("Key read: " + aKey + ". Expected: " + keyArr[c1]);

			// Read the value
			ZioObj aItem = aItemM.get(aKey);
			aItem.zioRead(aStream);
		}
	}

	/**
	 * Utility method to write out a map of ZioObj items.
	 * <p>
	 * Format: &lt;numItems> (&lt;String, ZioObj>)&ast;
	 */
	public static void writeMap(ZoutStream aStream, Map<String, ? extends ZioObj> aItemM) throws IOException
	{
		// Write the item count
		aStream.writeInt(aItemM.size());

		// Write the actual objects
		for (String aKey : aItemM.keySet())
		{
			// Write the key
			aStream.writeString(aKey);

			// Write the value
			ZioObj aZioObj = aItemM.get(aKey);
			aZioObj.zioWrite(aStream);
		}
	}

	public static <G1 extends ZioObj> G1 read(ZinStream aStream, Class<G1> aClass) throws IOException
	{
		// Serialize the class
		G1 aItem;
		try
		{
			aItem = aClass.getDeclaredConstructor().newInstance();
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
			aItem = aClass.getDeclaredConstructor().newInstance();
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
