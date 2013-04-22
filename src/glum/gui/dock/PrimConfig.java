package glum.gui.dock;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Map;
import java.util.Set;

import com.google.common.collect.Maps;

import glum.io.IoUtil;

public class PrimConfig
{
	// Constants
	public static final byte CODE_BOOL   = 1;
	public static final byte CODE_INT    = 10;
	public static final byte CODE_LONG   = 11;
	public static final byte CODE_SHORT  = 12;	
	public static final byte CODE_FLOAT  = 13;
	public static final byte CODE_DOUBLE = 14;
	public static final byte CODE_STRING = 20;

	// State vars
	protected Map<String, Object> mySingletonMap;
	
	/**
	 * Container which holds a mapping of keys (String) to a bunch
	 * of primitive objects. 
	 */
	public PrimConfig()
	{
		mySingletonMap = Maps.newLinkedHashMap();
	}

	/**
	 * Returns the boolean associated with aKey
	 * If no such value is found then defaultVal is returned.
	 */
	public boolean getBoolean(String aKey, boolean defaultVal)
	{
		Object aValue;

		// If no value associated with the key then return defaultVal
		aValue = mySingletonMap.get(aKey);
		if (aValue == null || aValue.getClass() != Boolean.class)
			return defaultVal;

		return (Boolean)aValue;
	}
	
	/**
	 * Returns the int associated with aKey
	 * If no such value is found then defaultVal is returned.
	 */
	public int getInt(String aKey, int defaultVal)
	{
		Object aValue;

		// If no value associated with the key then return defaultVal
		aValue = mySingletonMap.get(aKey);
		if (aValue == null || aValue.getClass() != Integer.class)
			return defaultVal;

		return (Integer)aValue;
	}
	
	/**
	 * Returns the long associated with aKey
	 * If no such value is found then defaultVal is returned.
	 */
	public long getLong(String aKey, long defaultVal)
	{
		Object aValue;

		// If no value associated with the key then return defaultVal
		aValue = mySingletonMap.get(aKey);
		if (aValue == null || aValue.getClass() != Long.class)
			return defaultVal;

		return (Long)aValue;
	}
	
	/**
	 * Returns the short associated with aKey
	 * If no such value is found then defaultVal is returned.
	 */
	public short getShort(String aKey, short defaultVal)
	{
		Object aValue;

		// If no value associated with the key then return defaultVal
		aValue = mySingletonMap.get(aKey);
		if (aValue == null || aValue.getClass() != Short.class)
			return defaultVal;

		return (Short)aValue;
	}

	/**
	 * Returns the float associated with aKey
	 * If no such value is found then defaultVal is returned.
	 */
	public float getFloat(String aKey, float defaultVal)
	{
		Object aValue;

		// If no value associated with the key then return defaultVal
		aValue = mySingletonMap.get(aKey);
		if (aValue == null || aValue.getClass() != Float.class)
			return defaultVal;

		return (Float)aValue;
	}

	/**
	 * Returns the double associated with aKey
	 * If no such value is found then defaultVal is returned.
	 */
	public double getDouble(String aKey, double defaultVal)
	{
		Object aValue;

		// If no value associated with the key then return defaultVal
		aValue = mySingletonMap.get(aKey);
		if (aValue == null || aValue.getClass() != Double.class)
			return defaultVal;

		return (Double)aValue;
	}

	/**
	 * Returns the String associated with aKey
	 * If no such value is found then defaultVal is returned.
	 */
	public String getString(String aKey, String defaultVal)
	{
		Object aValue;

		// If no value associated with the key then return defaultVal
		aValue = mySingletonMap.get(aKey);
		if (aValue == null || aValue.getClass() != String.class)
			return defaultVal;

		return (String)aValue;
	}

	/**
	 * Associates aVal with aKey. Note this will overwrite any
	 * previous association.
	 */
	public void setBoolean(String aKey, boolean aValue)
	{
		mySingletonMap.put(aKey, aValue);
	}

	/**
	 * Associates aVal with aKey. Note this will overwrite any
	 * previous association.
	 */
	public void setInt(String aKey, int aValue)
	{
		mySingletonMap.put(aKey, aValue);
	}

	/**
	 * Associates aVal with aKey. Note this will overwrite any
	 * previous association.
	 */
	public void setLong(String aKey, long aValue)
	{
		mySingletonMap.put(aKey, aValue);
	}

	/**
	 * Associates aVal with aKey. Note this will overwrite any
	 * previous association.
	 */
	public void setShort(String aKey, short aValue)
	{
		mySingletonMap.put(aKey, aValue);
	}

	/**
	 * Associates aVal with aKey. Note this will overwrite any
	 * previous association.
	 */
	public void setFloat(String aKey, float aValue)
	{
		mySingletonMap.put(aKey, aValue);
	}

	/**
	 * Associates aVal with aKey. Note this will overwrite any
	 * previous association.
	 */
	public void setDouble(String aKey, double aValue)
	{
		mySingletonMap.put(aKey, aValue);
	}

	/**
	 * Associates aVal with aKey. Note this will overwrite any
	 * previous association.
	 */
	public void setString(String aKey, String aValue)
	{
		mySingletonMap.put(aKey, aValue);
	}

	/**
	 * Utility method to read the configuration from a DataInputStream
	 */
	public void readBin(DataInputStream aStream) throws IOException
	{
		int numItems;
		String aKey;
		byte aType;

		mySingletonMap.clear();

		numItems = aStream.readInt();
		for (int c1 = 0; c1 < numItems; c1++)
		{
			aKey = IoUtil.readString(aStream);
			aType = aStream.readByte();
			
			switch(aType)
			{
				case CODE_BOOL:
					mySingletonMap.put(aKey, aStream.readBoolean());
					break;
					
				case CODE_INT:
					mySingletonMap.put(aKey, aStream.readInt());
					break;
				
				case CODE_LONG:
					mySingletonMap.put(aKey, aStream.readLong());
					break;
					
				case CODE_SHORT:
					mySingletonMap.put(aKey, aStream.readShort());
					break;
				
				case CODE_FLOAT:
					mySingletonMap.put(aKey, aStream.readFloat());
					break;
					
				case CODE_DOUBLE:
					mySingletonMap.put(aKey, aStream.readDouble());
					break;
				
				case CODE_STRING:
					mySingletonMap.put(aKey, IoUtil.readString(aStream));
					break;
					
				default:
					throw new RuntimeException("Unreconnized type: " + aType);
			}
		}
	}

	/**
	 * Utility method to write the configuration to a DataOutputStream
	 */
	public void writeBin(DataOutputStream aStream) throws IOException
	{
		Set<String> keySet;
		Object aVal;
		
		keySet = mySingletonMap.keySet();
		
		aStream.writeInt(keySet.size());
		for (String aKey : keySet)
		{
			IoUtil.writeString(aStream, aKey);
			
			aVal = mySingletonMap.get(aKey);
			if (aVal instanceof Boolean)
			{
				aStream.writeByte(CODE_BOOL);
				aStream.writeBoolean((Boolean)aVal);
			}
			else if (aVal instanceof Integer)
			{
				aStream.writeByte(CODE_INT);
				aStream.writeInt((Integer)aVal);
			}
			else if (aVal instanceof Long)
			{
				aStream.writeByte(CODE_LONG);
				aStream.writeLong((Long)aVal);
			}
			else if (aVal instanceof Short)
			{
				aStream.writeByte(CODE_SHORT);
				aStream.writeLong((Short)aVal);
			}
			else if (aVal instanceof Float)
			{
				aStream.writeByte(CODE_FLOAT);
				aStream.writeFloat((Float)aVal);
			}
			else if (aVal instanceof Double)
			{
				aStream.writeByte(CODE_DOUBLE);
				aStream.writeDouble((Double)aVal);
			}
			else if (aVal instanceof String)
			{
				aStream.writeByte(CODE_STRING);
				IoUtil.writeString(aStream, (String)aVal);
			}
			else
			{
				throw new RuntimeException("Unsupported Object: " + aVal);
			}
		}
	}

}
