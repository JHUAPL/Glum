package glum.zio;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;

public class ZioRawUtil
{
	/**
	 * Utility method to read a list of ZioRaw items. The following requirements must be met:
	 * <UL>
	 * <LI>The objects are assumed to be of the same type.
	 * <LI>The class must define a Constructor that takes one argument - a ZinStream.
	 * </UL>
	 * <P>
	 * Format: <numItems> (<ZioRaw>)*
	 */
	public static <G1 extends ZioRaw> ArrayList<G1> readList(ZinStream aStream, Class<G1> aClass) throws IOException
	{
		// Locate an appropriate Constructor
		Constructor<G1> tmpConstructor = null;
		try
		{
			tmpConstructor = aClass.getConstructor(ZinStream.class);
		}
		catch(NoSuchMethodException | SecurityException aExp)
		{
			throw new IOException("Failed to locate a proper constructor. Constuctor requires a single argument - ZioStream", aExp);
		}

		// Read the item count
		int numItems = aStream.readInt();

		// Read the actual objects
		ArrayList<G1> itemList = new ArrayList<G1>();
		for (int c1 = 0; c1 < numItems; c1++)
		{
			// Serialize the class
			try
			{
				G1 aItem = tmpConstructor.newInstance(aStream);
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
	 * Utility method to write out a list of ZioRaw items.
	 * <P>
	 * Format: <numItems> (<ZioObj>)*
	 */
	public static void writeList(ZoutStream aStream, Collection<? extends ZioRaw> aItemList) throws IOException
	{
		// Write the item count
		aStream.writeInt(aItemList.size());

		// Write the actual objects
		for (ZioRaw aItem : aItemList)
			aItem.zioWrite(aStream);
	}

}
