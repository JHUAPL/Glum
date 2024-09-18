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
package glum.version;

/**
 * Utility class that allows for comparing Versions.
 * <p>
 * Eventually when Java allows operator overloading then this class can go away since the standard mathematical
 * comparison symbols would be much clearer.
 *
 * @author lopeznr1
 */
public class VersionUtils
{
	/**
	 * Utility method that returns true if aVerA occurs after aVerB
	 */
	public static boolean isAfter(Version aVerA, Version aVerB)
	{
		int majorA = aVerA.major();
		int minorA = aVerA.minor();
		int patchA = aVerA.patch();
		int majorB = aVerB.major();
		int minorB = aVerB.minor();
		int patchB = aVerB.patch();

		if (majorA > majorB)
			return true;
		if (majorA == majorB && minorA > minorB)
			return true;
		if (majorA == majorB && minorA == minorB && patchA > patchB)
			return true;

		return false;
	}

	/**
	 * Utility method that returns true if aVerA occurs after aVerB
	 */
	public static boolean isAfterOrEqual(Version aVerA, Version aVerB)
	{
		// Delegate to isAfter
		return isAfter(aVerB, aVerA) == false;
	}

	/**
	 * Utility method that returns true if the following statement is true:
	 * <p>
	 * aVerEval >= aVerMin && aVerEval <= aVerMax
	 * <p>
	 * A LogicError will be thrown if the aVerMin and aVerMax are inverted (aVerMin > aVerMax)
	 */
	public static boolean isInRange(Version aVerEval, Version aVerMin, Version aVerMax)
	{
		// Ensure the endpoints are not inverted
		if (isAfter(aVerMin, aVerMax) == true)
			throw new RuntimeException("Min/Max versions appear to be swapped. min: " + aVerMin + " max: " + aVerMax);

		// Decompose and delegate
		if (isAfter(aVerMin, aVerEval) == true)
			return false;
		if (isAfter(aVerEval, aVerMax) == true)
			return false;

		return true;
	}

	/**
	 * Utility method to allow the comparison of two versions.
	 *
	 * @param aVerA
	 * @param aVerB
	 * @return
	 */
	public static int compare(Version aVerA, Version aVerB)
	{

		int majorA = aVerA.major();
		int minorA = aVerA.minor();
		int patchA = aVerA.patch();
		int majorB = aVerB.major();
		int minorB = aVerB.minor();
		int patchB = aVerB.patch();

		int cmpVal;
		cmpVal = majorA - majorB;
		if (cmpVal != 0)
			return cmpVal;
		cmpVal = minorA - minorB;
		if (cmpVal != 0)
			return cmpVal;
		cmpVal = patchA - patchB;
		if (cmpVal != 0)
			return cmpVal;

		return 0;
	}
}
