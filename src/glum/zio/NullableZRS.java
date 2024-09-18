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
import java.lang.reflect.Constructor;

/**
 * Generic implementation of {@link ZioSpawner} that supports "null" items.
 * <p>
 * This implementation will support serialization of null (or invalid) items. When the deserialization process occurs a
 * previously serialized null item will result in the corresponding null (or invalid) item being returned.
 *
 * @author lopeznr1
 */
public class NullableZRS<G1 extends ZioRaw> implements ZioSpawner<G1>
{
	// Attributes
	private final Class<G1> refClass;
	private final G1 refInvalidItem;

	/**
	 * Standard Constructor
	 *
	 * @param aClass
	 *        Reference class associated with the items to be serialized by this {@link ZioSpawner}.
	 *
	 * @param aInvalidItem
	 *        Null equivalent object. This may be null or an (immutable) object that stands for "invalid" values.
	 */
	public NullableZRS(Class<G1> aClass, G1 aInvalidItem)
	{
		refClass = aClass;
		refInvalidItem = aInvalidItem;
	}

	/**
	 * Simplified Constructor
	 * <p>
	 * Serialized values of null (or a previously invalid item) will result in null being returned.
	 */
	public NullableZRS(Class<G1> aClass)
	{
		this(aClass, null);
	}

	@Override
	public G1 readInstance(ZinStream aStream) throws IOException
	{
		aStream.readVersion(0);

		byte type = aStream.readByte();
		if (type == 0)
			return refInvalidItem;

		// Locate an appropriate Constructor
		Constructor<G1> tmpConstructor = null;
		try
		{
			tmpConstructor = refClass.getConstructor(ZinStream.class);
		}
		catch (NoSuchMethodException | SecurityException aExp)
		{
			throw new IOException("Failed to locate a proper constructor. Constuctor requires " //
					+ "a single argument - ZioStream", aExp);
		}

		// Serialize the class
		try
		{
			G1 retItem = tmpConstructor.newInstance(aStream);
			return retItem;
		}
		catch (Exception aExp)
		{
			throw new IOException("Failed to instantiate: " + refClass, aExp);
		}

	}

	@Override
	public void writeInstance(ZoutStream aStream, G1 aItem) throws IOException
	{
		aStream.writeVersion(0);

		byte type = 0;
		if (aItem != null && aItem != refInvalidItem)
			type = 1;

		aStream.writeByte(type);
		if (type == 0)
			return;

		aItem.zioWrite(aStream);
	}

}
