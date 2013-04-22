package glum.unit;

import java.text.*;

public class ShiftedUnit implements Unit
{
	// State vars
	private Format format;
	private String nanStr;
	private String fullLabel;
	private String shortLabel;
	private double deltaValue;

	/**
	* Constructor
	*/
	public ShiftedUnit(String aFullLabel, String aShortLabel, double aDeltaValue, Format aFormat)
	{
		nanStr = "---";
		fullLabel = aFullLabel;
		shortLabel = aShortLabel;
		deltaValue = aDeltaValue;
		format = aFormat;
	}
	public ShiftedUnit(String aFullLabel, String aShortLabel, double aDeltaValue, int numDecimalPlaces)
	{
		String aStr;

		aStr = "#0";
		if (numDecimalPlaces > 0)
		{
			aStr = "#0.";
			for (int c1 = 0; c1 < numDecimalPlaces; c1++)
				aStr += "0";
		}

		nanStr = "---";
		fullLabel = aFullLabel;
		shortLabel = aShortLabel;
		deltaValue = aDeltaValue;
		format = new DecimalFormat(aStr);
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
	public String getLabel(boolean isDetailed)
	{
		if (isDetailed == true)
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
	public String getString(Object aObj, boolean isDetailed)
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
			if (isDetailed == true)
				return "" + (aVal + deltaValue) + " " + fullLabel;
			else
				return "" + (aVal + deltaValue) + " " + shortLabel;
		}

		synchronized (format)
		{
			if (isDetailed == true)
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
