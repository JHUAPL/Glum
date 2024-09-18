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
package glum.zio.util;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;

import glum.zio.*;

/**
 * Collection of utility methods for working with {@link ZioRaw} objects.
 *
 * @author lopeznr1
 */
public class ZioRawUtil
{
	/**
	 * Utility method to read a list of {@link ZioRaw} items. The following requirements must be met:
	 * <ul>
	 * <li>The objects are assumed to be of the same type.
	 * <li>The class must define a Constructor that takes one argument - a {@link ZinStream}.
	 * </ul>
	 * <p>
	 * Format: &lt;numItems> (&lt;ZioRaw>)&ast;
	 */
	public static <G1> ArrayList<G1> readList(ZinStream aStream, Class<G1> aClass) throws IOException
	{
		// Locate an appropriate Constructor
		Constructor<G1> tmpConstructor = null;
		try
		{
			tmpConstructor = aClass.getConstructor(ZinStream.class);
		}
		catch (NoSuchMethodException | SecurityException aExp)
		{
			throw new IOException("Failed to locate a proper constructor. Constuctor requires " //
					+ "a single argument - ZioStream", aExp);
		}

		// Read the item count
		int numItems = aStream.readInt();

		// Read the actual objects
		ArrayList<G1> retItemL = new ArrayList<G1>();
		for (int c1 = 0; c1 < numItems; c1++)
		{
			// Serialize the class
			try
			{
				G1 aItem = tmpConstructor.newInstance(aStream);
				retItemL.add(aItem);
			}
			catch (Exception aException)
			{
				throw new IOException("Failed to instantiate: " + aClass, aException);
			}
		}

		return retItemL;
	}

	/**
	 * Utility method to write out a list of {@link ZioRaw} items.
	 * <p>
	 * Format: &lt;numItems> (&lt;ZioObj>)&ast;
	 */
	public static void writeList(ZoutStream aStream, Collection<? extends ZioRaw> aItemC) throws IOException
	{
		// Write the item count
		aStream.writeInt(aItemC.size());

		// Write the actual objects
		for (ZioRaw aItem : aItemC)
			aItem.zioWrite(aStream);
	}

	/**
	 * Utility method to write an individual {@link ZioRaw} item.
	 */
	public static <G1> G1 readRaw(ZinStream aStream, ZioSpawner<G1> aSpawner) throws IOException
	{
		aStream.readVersion(0);
		return aSpawner.readInstance(aStream);
	}

	/**
	 * Utility method to write an individual {@link ZioRaw} item.
	 */
	public static <G1> void writeRaw(ZoutStream aStream, ZioSpawner<G1> aSpawner, G1 aItem) throws IOException
	{
		aStream.writeVersion(0);
		aSpawner.writeInstance(aStream, aItem);
	}

	/**
	 * Utility method to read a list of {@link ZioRaw} items.
	 * <p>
	 * Format: <numItems> (&lt;ZioRaw>)&ast;
	 */
	public static <G1> ArrayList<G1> readRawList(ZinStream aStream, ZioSpawner<G1> aSpawner) throws IOException
	{
		ArrayList<G1> retItemL;
		G1 aItem;
		int numItems;

		// Read the item count
		numItems = aStream.readInt();

		// Read the actual objects
		retItemL = new ArrayList<>(numItems + 1);
		for (int c1 = 0; c1 < numItems; c1++)
		{
			// Serialize the object
			try
			{
				aItem = aSpawner.readInstance(aStream);
				retItemL.add(aItem);
			}
			catch (Exception aException)
			{
				throw new IOException("Failed to instantiate: " + aSpawner, aException);
			}
		}

		return retItemL;
	}

	/**
	 * Utility method to write out a list of {@link ZioRaw} items.
	 * <p>
	 * Format: <numItems> (&lt;ZioRaw>)&ast;
	 */
	public static <G1> void writeRawList(ZoutStream aStream, ZioSpawner<G1> aSpawner, Collection<G1> aItemC)
			throws IOException
	{
		// Write the item count
		aStream.writeInt(aItemC.size());

		// Write the actual objects
		for (G1 aItem : aItemC)
			aSpawner.writeInstance(aStream, aItem);
	}

}
