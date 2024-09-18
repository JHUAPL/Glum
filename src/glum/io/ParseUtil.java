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
package glum.io;

/**
 * Collection of utility methods for parsing values from text input.
 *
 * @author lopeznr1
 */
public class ParseUtil
{
	/**
	 * Reads a boolean from a string with out throwing a exception.
	 */
	public static boolean readBoolean(String aStr, boolean aVal)
	{
		if (aStr == null)
			return aVal;

		// Special case for 1 char strings
		if (aStr.length() == 1)
		{
			char aChar;

			aChar = aStr.charAt(0);
			if (aChar == 'T' || aChar == 't' || aChar == '1')
				return true;

			return false;
		}

		try
		{
			return Boolean.valueOf(aStr).booleanValue();
		}
		catch (Exception aExp)
		{
			return aVal;
		}
	}

	/**
	 * Reads a double from a string with out throwing a exception. Note aStr can have an number of separators: comma
	 * chars
	 */
	public static double readDouble(String aStr, double aVal)
	{
		try
		{
			aStr = aStr.replace(",", "");
			return Double.parseDouble(aStr);
		}
		catch (Exception aExp)
		{
			return aVal;
		}
	}

	/**
	 * Reads a float from a string with out throwing a exception. Note aStr can have an number of separators: comma chars
	 */
	public static float readFloat(String aStr, float aVal)
	{
		try
		{
			aStr = aStr.replace(",", "");
			return Float.parseFloat(aStr);
		}
		catch (Exception aExp)
		{
			return aVal;
		}
	}

	/**
	 * Reads an int from a string without throwing a exception. Note aStr can have an number of separators: comma chars
	 */
	public static int readInt(String aStr, int aVal)
	{
		try
		{
			aStr = aStr.replace(",", "");
			return Integer.parseInt(aStr);
		}
		catch (Exception aExp)
		{
			return aVal;
		}
	}

	/**
	 * Reads a long from a string without throwing a exception. Note aStr can have an number of separators: comma chars
	 */
	public static long readLong(String aStr, long aVal)
	{
		try
		{
			aStr = aStr.replace(",", "");
			return Long.parseLong(aStr);
		}
		catch (Exception aExp)
		{
			return aVal;
		}
	}

	/**
	 * Reads an int (forced to fit within a range) from a string with out throwing a exception.
	 */
	public static int readRangeInt(String aStr, int aMinVal, int aMaxVal, int aVal)
	{
		try
		{
			int tmpInt = Integer.parseInt(aStr);
			if (tmpInt < aMinVal)
				tmpInt = aMinVal;
			else if (tmpInt > aMaxVal)
				tmpInt = aMaxVal;

			return tmpInt;
		}
		catch (Exception aExp)
		{
			return aVal;
		}
	}

	/**
	 * Utility method to strip the white space from an array of tokens.
	 */
	public static void cleanTokens(String[] aTokenArr)
	{
		for (int c1 = 0; c1 < aTokenArr.length; c1++)
			aTokenArr[c1] = aTokenArr[c1].strip();
	}

}
