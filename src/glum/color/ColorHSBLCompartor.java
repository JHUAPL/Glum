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
package glum.color;

import java.util.Comparator;
import java.util.List;

import com.google.common.collect.ImmutableList;

/**
 * Flexible Color Comparator that provides color comparisons using any combination of the attributes HLSB (hue, lumance,
 * saturation, brightness) rather than RGB (red,green, blue).
 *
 * @author lopeznr1
 */
public class ColorHSBLCompartor implements Comparator<ColorHSBL>
{
	// Constants
	private static ImmutableList<KeyAttr> SortKey_HLB = ImmutableList.of(KeyAttr.Hue, KeyAttr.Luminance,
			KeyAttr.Brightness);

	// Attributes
	private final List<KeyAttr> keyL;
	private final int numGroups;

	/**
	 * Standard Constructor
	 *
	 * @param aKeyL
	 *        Defines an ordered list of {@link KeyAttr}s that will define how colors are sorted.
	 * @param aNumGroups
	 *        Defines the number of groups for which the various color attributes will be grouped to.
	 */
	public ColorHSBLCompartor(List<KeyAttr> aKeyL, int aNumGroups)
	{
		keyL = ImmutableList.copyOf(aKeyL);
		numGroups = aNumGroups;
	}

	/**
	 * Simplified Constructor
	 *
	 * Colors will be sorted by the following ordered attributes: hue, lumance, and brightness.
	 *
	 * @param aNumGroups
	 *        Defines the number of groups for which the various color attributes will be grouped to.
	 */
	public ColorHSBLCompartor(int aNumGroups)
	{
		this(SortKey_HLB, aNumGroups);
	}

	@Override
	public int compare(ColorHSBL aHsbl1, ColorHSBL aHsbl2)
	{
		int prevStep = 0;

		// Iterate through all of the keys
		int tmpIdx = -1;
		for (KeyAttr aKey : keyL)
		{
			tmpIdx++;
			double fVal1, fVal2;
			switch (aKey)
			{
				case Hue:
					fVal1 = aHsbl1.getHue();
					fVal2 = aHsbl2.getHue();
					break;
				case Saturation:
					fVal1 = aHsbl1.getSaturation();
					fVal2 = aHsbl2.getSaturation();
					break;
				case Brightness:
					fVal1 = aHsbl1.getBrightness();
					fVal2 = aHsbl2.getBrightness();
					break;
				case Luminance:
					fVal1 = aHsbl1.getLuminance();
					fVal2 = aHsbl2.getLuminance();
					break;

				default:
					throw new UnsupportedOperationException("Unrecognized key: " + aKey);
			}

			// Compare the corresponding normalized (group) values
			int iVal1 = (int) (fVal1 * numGroups);
			int iVal2 = (int) (fVal2 * numGroups);
			int cmpVal = Integer.compare(iVal1, iVal2);

			// If we are at the last key - do not use the normalize (group) values
			// but rather utilize the non-normalized raw float values so as to
			// reduce (eliminate?) ambiguities due to values being in the same bin.
			if (tmpIdx == keyL.size() - 1)
				cmpVal = Double.compare(fVal1, fVal2);

			// Note we flip the comparison if the previous (key) comparison
			// resulted in an odd value. We do this so that rather than having
			// a number of step function we get more of an oscillating smooth
			// transition between the comparison keys.
			if (prevStep % 2 == 1)
				cmpVal = -cmpVal;

			if (cmpVal != 0)
				return cmpVal;

			prevStep = iVal1;
		}

		return 0;
	}

}
