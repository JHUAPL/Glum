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

import java.text.DecimalFormat;;

public class NumberInverseUnit extends NumberUnit
{
	public NumberInverseUnit(String aFullLabel, String aShortLabel, double aConversionFactor)
	{
		super(aFullLabel, aShortLabel, aConversionFactor, (DecimalFormat)null);
	}

	public NumberInverseUnit(String aFullLabel, String aShortLabel, double aConversionFactor, int numDecimalPlaces)
	{
		super(aFullLabel, aShortLabel, aConversionFactor, numDecimalPlaces);
	}

	public NumberInverseUnit(String aFullLabel, String aShortLabel, double aConversionFactor, DecimalFormat aFormat)
	{
		super(aFullLabel, aShortLabel, aConversionFactor, aFormat);
	}

	@Override
	public String getString(Object aVal)
	{
		String aStr;

		aStr = "N/A";
		if (aVal instanceof Number == false)
			return aStr;

		if (dispFormat == null)
			return "" + ((1.0/((Number)aVal).doubleValue()) * conversionFactor);

		synchronized (dispFormat)
		{
			return dispFormat.format((1.0/((Number)aVal).doubleValue()) * conversionFactor);
		}
	}

	@Override
	public String getString(Object aVal, boolean aIsDetailed)
	{
		if (aVal instanceof Number == false)
			return "N/A";

		return "" + ((1.0/((Number)aVal).doubleValue()) * conversionFactor);
	}

	@Override
	public double toModel(double aVal)
	{
		if (conversionFactor == 0)
			return Double.NaN;

		return 1.0/(aVal / conversionFactor);
	}

	@Override
	public double toUnit(double aVal)
	{
		return (1.0/aVal) * conversionFactor;
	}

}
