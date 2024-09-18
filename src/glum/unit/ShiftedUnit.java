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

import java.text.Format;

/**
 * Implementation of {@link Unit} for displaying values shifted by a constant value.
 *
 * @author lopeznr1
 */
public class ShiftedUnit implements Unit
{
	// State vars
	private Format format;
	private String nanStr;
	private String fullLabel;
	private String shortLabel;
	private double deltaValue;

	/** Standard Constructor */
	public ShiftedUnit(String aFullLabel, String aShortLabel, double aDeltaValue, Format aFormat)
	{
		nanStr = "---";
		fullLabel = aFullLabel;
		shortLabel = aShortLabel;
		deltaValue = aDeltaValue;
		format = aFormat;
	}
	public ShiftedUnit(String aFullLabel, String aShortLabel, double aDeltaValue, int aNumDecimalPlaces)
	{
		this(aFullLabel, aShortLabel, aDeltaValue, UnitUtil.formFormatWithNumDecimalPlaces(aNumDecimalPlaces));
	}

	@Override
	public String getConfigName()
	{
		return "shifted";
	}

	@Override
	public Format getFormat()
	{
		if (format == null)
			return null;

		return (Format)format.clone();
	}

	@Override
	public String getLabel(boolean aIsDetailed)
	{
		if (aIsDetailed == true)
			return fullLabel;
		else
			return shortLabel;
	}

	@Override
	public String getString(Object aObj)
	{
		double aVal;

		// We need a number
		if (aObj instanceof Number == false)
			return "N/A";

		aVal = ((Number)aObj).doubleValue();
		if (Double.isNaN(aVal) == true)
			return nanStr;

		if (format == null)
			return "" + (aVal + deltaValue);

		synchronized (format)
		{
			return format.format(Double.valueOf(aVal + deltaValue));
		}
	}

	@Override
	public String getString(Object aObj, boolean aIsDetailed)
	{
		double aVal;

		// We need a number
		if (aObj instanceof Number == false)
			return "N/A";

		aVal = ((Number)aObj).doubleValue();
		if (Double.isNaN(aVal) == true)
			return nanStr;

		if (format == null)
		{
			if (aIsDetailed == true)
				return "" + (aVal + deltaValue) + " " + fullLabel;
			else
				return "" + (aVal + deltaValue) + " " + shortLabel;
		}

		synchronized (format)
		{
			if (aIsDetailed == true)
				return format.format(Double.valueOf(aVal + deltaValue)) + " " + fullLabel;
			else
				return format.format(Double.valueOf(aVal + deltaValue)) + " " + shortLabel;
		}
	}

	@Override
	public double parseString(String aStr, double eVal)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public double toModel(double aVal)
	{
		return aVal - deltaValue;
	}

	@Override
	public double toUnit(double aVal)
	{
		return aVal + deltaValue;
	}

}
