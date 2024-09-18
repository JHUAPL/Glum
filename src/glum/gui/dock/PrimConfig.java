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
package glum.gui.dock;

import java.io.*;
import java.util.LinkedHashMap;
import java.util.Map;

import glum.io.IoUtil;

/**
 * Container which holds a mapping of keys (String) to a bunch of primitive objects.
 * <p>
 * Serialization of content is supported via {@link #readBin(DataInputStream)} and {@link #writeBin(DataOutputStream)}
 *
 * @author lopeznr1
 */
public class PrimConfig
{
	// Constants
	public static final byte CODE_BOOL = 1;
	public static final byte CODE_INT = 10;
	public static final byte CODE_LONG = 11;
	public static final byte CODE_SHORT = 12;
	public static final byte CODE_FLOAT = 13;
	public static final byte CODE_DOUBLE = 14;
	public static final byte CODE_STRING = 20;

	// State vars
	protected Map<String, Object> mySingletonM;

	/** Standard Constructor */
	public PrimConfig()
	{
		mySingletonM = new LinkedHashMap<>();
	}

	/**
	 * Returns the boolean associated with aKey If no such value is found then defaultVal is returned.
	 */
	public boolean getBoolean(String aKey, boolean aDefaultVal)
	{
		// If no value associated with the key then return defaultVal
		var tmpVal = mySingletonM.get(aKey);
		if (tmpVal == null || tmpVal.getClass() != Boolean.class)
			return aDefaultVal;

		return (Boolean) tmpVal;
	}

	/**
	 * Returns the int associated with aKey If no such value is found then defaultVal is returned.
	 */
	public int getInt(String aKey, int aDefaultVal)
	{
		// If no value associated with the key then return defaultVal
		var tmpVal = mySingletonM.get(aKey);
		if (tmpVal == null || tmpVal.getClass() != Integer.class)
			return aDefaultVal;

		return (Integer) tmpVal;
	}

	/**
	 * Returns the long associated with aKey If no such value is found then defaultVal is returned.
	 */
	public long getLong(String aKey, long aDefaultVal)
	{
		// If no value associated with the key then return defaultVal
		var tmpVal = mySingletonM.get(aKey);
		if (tmpVal == null || tmpVal.getClass() != Long.class)
			return aDefaultVal;

		return (Long) tmpVal;
	}

	/**
	 * Returns the short associated with aKey If no such value is found then defaultVal is returned.
	 */
	public short getShort(String aKey, short aDefaultVal)
	{
		// If no value associated with the key then return defaultVal
		var tmpVal = mySingletonM.get(aKey);
		if (tmpVal == null || tmpVal.getClass() != Short.class)
			return aDefaultVal;

		return (Short) tmpVal;
	}

	/**
	 * Returns the float associated with aKey If no such value is found then defaultVal is returned.
	 */
	public float getFloat(String aKey, float aDefaultVal)
	{
		// If no value associated with the key then return defaultVal
		var tmpVal = mySingletonM.get(aKey);
		if (tmpVal == null || tmpVal.getClass() != Float.class)
			return aDefaultVal;

		return (Float) tmpVal;
	}

	/**
	 * Returns the double associated with aKey If no such value is found then defaultVal is returned.
	 */
	public double getDouble(String aKey, double aDefaultVal)
	{
		// If no value associated with the key then return defaultVal
		var tmpVal = mySingletonM.get(aKey);
		if (tmpVal == null || tmpVal.getClass() != Double.class)
			return aDefaultVal;

		return (Double) tmpVal;
	}

	/**
	 * Returns the String associated with aKey If no such value is found then defaultVal is returned.
	 */
	public String getString(String aKey, String aDefaultVal)
	{
		// If no value associated with the key then return defaultVal
		var tmpVal = mySingletonM.get(aKey);
		if (tmpVal == null || tmpVal.getClass() != String.class)
			return aDefaultVal;

		return (String) tmpVal;
	}

	/**
	 * Associates aVal with aKey. Note this will overwrite any previous association.
	 */
	public void setBoolean(String aKey, boolean aValue)
	{
		mySingletonM.put(aKey, aValue);
	}

	/**
	 * Associates aVal with aKey. Note this will overwrite any previous association.
	 */
	public void setInt(String aKey, int aValue)
	{
		mySingletonM.put(aKey, aValue);
	}

	/**
	 * Associates aVal with aKey. Note this will overwrite any previous association.
	 */
	public void setLong(String aKey, long aValue)
	{
		mySingletonM.put(aKey, aValue);
	}

	/**
	 * Associates aVal with aKey. Note this will overwrite any previous association.
	 */
	public void setShort(String aKey, short aValue)
	{
		mySingletonM.put(aKey, aValue);
	}

	/**
	 * Associates aVal with aKey. Note this will overwrite any previous association.
	 */
	public void setFloat(String aKey, float aValue)
	{
		mySingletonM.put(aKey, aValue);
	}

	/**
	 * Associates aVal with aKey. Note this will overwrite any previous association.
	 */
	public void setDouble(String aKey, double aValue)
	{
		mySingletonM.put(aKey, aValue);
	}

	/**
	 * Associates aVal with aKey. Note this will overwrite any previous association.
	 */
	public void setString(String aKey, String aValue)
	{
		mySingletonM.put(aKey, aValue);
	}

	/**
	 * Utility method to read the configuration from a DataInputStream
	 */
	public void readBin(DataInputStream aStream) throws IOException
	{
		mySingletonM.clear();

		var numItems = aStream.readInt();
		for (int c1 = 0; c1 < numItems; c1++)
		{
			var tmpKey = IoUtil.readString(aStream);
			var tmpType = aStream.readByte();

			switch (tmpType)
			{
				case CODE_BOOL:
					mySingletonM.put(tmpKey, aStream.readBoolean());
					break;

				case CODE_INT:
					mySingletonM.put(tmpKey, aStream.readInt());
					break;

				case CODE_LONG:
					mySingletonM.put(tmpKey, aStream.readLong());
					break;

				case CODE_SHORT:
					mySingletonM.put(tmpKey, aStream.readShort());
					break;

				case CODE_FLOAT:
					mySingletonM.put(tmpKey, aStream.readFloat());
					break;

				case CODE_DOUBLE:
					mySingletonM.put(tmpKey, aStream.readDouble());
					break;

				case CODE_STRING:
					mySingletonM.put(tmpKey, IoUtil.readString(aStream));
					break;

				default:
					throw new RuntimeException("Unreconnized type: " + tmpType);
			}
		}
	}

	/**
	 * Utility method to write the configuration to a DataOutputStream
	 */
	public void writeBin(DataOutputStream aStream) throws IOException
	{
		var keyS = mySingletonM.keySet();

		aStream.writeInt(keyS.size());
		for (String aKey : keyS)
		{
			IoUtil.writeString(aStream, aKey);

			var tmpVal = mySingletonM.get(aKey);
			if (tmpVal instanceof Boolean)
			{
				aStream.writeByte(CODE_BOOL);
				aStream.writeBoolean((Boolean) tmpVal);
			}
			else if (tmpVal instanceof Integer)
			{
				aStream.writeByte(CODE_INT);
				aStream.writeInt((Integer) tmpVal);
			}
			else if (tmpVal instanceof Long)
			{
				aStream.writeByte(CODE_LONG);
				aStream.writeLong((Long) tmpVal);
			}
			else if (tmpVal instanceof Short)
			{
				aStream.writeByte(CODE_SHORT);
				aStream.writeLong((Short) tmpVal);
			}
			else if (tmpVal instanceof Float)
			{
				aStream.writeByte(CODE_FLOAT);
				aStream.writeFloat((Float) tmpVal);
			}
			else if (tmpVal instanceof Double)
			{
				aStream.writeByte(CODE_DOUBLE);
				aStream.writeDouble((Double) tmpVal);
			}
			else if (tmpVal instanceof String)
			{
				aStream.writeByte(CODE_STRING);
				IoUtil.writeString(aStream, (String) tmpVal);
			}
			else
			{
				throw new RuntimeException("Unsupported Object: " + tmpVal);
			}
		}
	}

}
