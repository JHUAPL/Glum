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
import java.text.Format;

import glum.io.ParseUtil;

/**
 * Implementation of {@link Unit} for displaying decimal values.
 *
 * @author lopeznr1
 */
public class NumberUnit implements Unit
{
	// State vars
	protected DecimalFormat dispFormat;
	protected String nanStr;
	protected String fullLabel;
	protected String shortLabel;
	protected double conversionFactor;

	/** Standard Constructor */
	public NumberUnit(String aFullLabel, String aShortLabel, double aConversionFactor, DecimalFormat aDispFormat)
	{
		nanStr = "---";
		fullLabel = aFullLabel;
		shortLabel = aShortLabel;
		conversionFactor = aConversionFactor;
		dispFormat = aDispFormat;
	}

	public NumberUnit(String aFullLabel, String aShortLabel, double aConversionFactor, String aFormatStr)
	{
		this(aFullLabel, aShortLabel, aConversionFactor, new DecimalFormat(aFormatStr));
	}

	public NumberUnit(String aFullLabel, String aShortLabel, double aConversionFactor)
	{
		this(aFullLabel, aShortLabel, aConversionFactor, (DecimalFormat) null);
	}

	public NumberUnit(String aFullLabel, String aShortLabel, double aConversionFactor, int aNumDecimalPlaces)
	{
		this(aFullLabel, aShortLabel, aConversionFactor, UnitUtil.formFormatWithNumDecimalPlaces(aNumDecimalPlaces));
	}

	/**
	 * Returns whether this unit supports floating point numbers
	 */
	public boolean isFloating()
	{
		if (dispFormat != null && dispFormat.getMaximumFractionDigits() == 0)
			return false;

		return true;

//System.out.println("NumFracDigits:" + format.getMaximumFractionDigits());
//		return format.isParseIntegerOnly();
	}

	/**
	 * Sets in the string representation for NaN
	 */
	public void setNaNString(String aStr)
	{
		nanStr = aStr;
	}

	@Override
	public Format getFormat()
	{
		if (dispFormat == null)
			return null;

		return (Format) dispFormat.clone();
	}

	@Override
	public String getConfigName()
	{
		return fullLabel;
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
	public String getString(Object aVal)
	{
		if (aVal instanceof Number == false)
			return nanStr;

		double doubleVal = ((Number) aVal).doubleValue();
		if (Double.isNaN(doubleVal) == true)
			return nanStr;

		if (dispFormat == null)
			return "" + doubleVal * conversionFactor;

		synchronized (dispFormat)
		{
			return dispFormat.format(doubleVal * conversionFactor);
		}
	}

	@Override
	public String getString(Object aVal, boolean aIsDetailed)
	{
		// Delegate
		String retStr = getString(aVal);

		// Add the label component
		if (aIsDetailed == true)
			retStr += " " + fullLabel;
		else
			retStr += " " + shortLabel;

		return retStr;
	}

	@Override
	public double parseString(String aStr, double eVal)
	{
		double aVal;

		aVal = ParseUtil.readDouble(aStr, Double.NaN);
		return toModel(aVal);
	}

	@Override
	public double toModel(double aVal)
	{
		if (conversionFactor == 0)
			return Double.NaN;

		return aVal / conversionFactor;
	}

	@Override
	public double toUnit(double aVal)
	{
		return aVal * conversionFactor;
	}

}
