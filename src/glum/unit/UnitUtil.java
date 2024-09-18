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
package glum.unit;

import java.text.DecimalFormat;

/**
 * Collection of utility methods to support working with {@link Unit}s.
 *
 * @author lopeznr1
 */
public class UnitUtil
{
	/**
	 * Utility method to create a {@link DecimalFormat} that has the specified number of decimal places
	 */
	public static DecimalFormat formFormatWithNumDecimalPlaces(int aNumDecimalPlaces)
	{
		String tmpStr;

		tmpStr = "#0";
		if (aNumDecimalPlaces > 0)
		{
			tmpStr = "#0.";
			for (int c1 = 0; c1 < aNumDecimalPlaces; c1++)
				tmpStr += "0";
		}

		return new DecimalFormat(tmpStr);
	}

}
