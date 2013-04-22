package glum.unit;

import glum.gui.GuiUtil;

import java.text.*;

public class NumberUnit implements Unit
{
	// State vars
	protected DecimalFormat format;
	protected String nanStr;
	protected String fullLabel;
	protected String shortLabel;
	protected double conversionFactor;

	/**
	* Constructor
	*/
	public NumberUnit(String aFullLabel, String aShortLabel, double aConversionFactor)
	{
		this(aFullLabel, aShortLabel, aConversionFactor, (DecimalFormat)null);
	}

	public NumberUnit(String aFullLabel, String aShortLabel, double aConversionFactor, String aDecimalFormatStr)
	{
		this(aFullLabel, aShortLabel, aConversionFactor, new DecimalFormat(aDecimalFormatStr));
	}

	public NumberUnit(String aFullLabel, String aShortLabel, double aConversionFactor, DecimalFormat aFormat)
	{
		nanStr = "---";
		fullLabel = aFullLabel;
		shortLabel = aShortLabel;
		conversionFactor = aConversionFactor;
		format = aFormat;
	}

	public NumberUnit(String aFullLabel, String aShortLabel, double aConversionFactor, int numDecimalPlaces)
	{
		String aStr;

		nanStr = "---";
		fullLabel = aFullLabel;
		shortLabel = aShortLabel;
		conversionFactor = aConversionFactor;

		aStr = "#0";
		if (numDecimalPlaces > 0)
		{
			aStr = "#0.";
			for (int c1 = 0; c1 < numDecimalPlaces; c1++)
				aStr += "0";
		}
		format = new DecimalFormat(aStr);
	}	

	/**
	 * Returns whether this unit supports floating point numbers
	 */
	public boolean isFloating()
	{
		if (format != null && format.getMaximumFractionDigits() == 0)
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
		if (format == null)
			return null;

		return (Format)format.clone();
	}
	
	@Override
	public String getConfigName()
	{
		return fullLabel;
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
	public String getString(Object aVal)
	{
		String aStr;

		aStr = "N/A";
		if (aVal instanceof Number == false)
			return aStr;

		if (format == null)
			return "" + (((Number)aVal).doubleValue() * conversionFactor);

		synchronized (format)
		{
			return format.format(((Number)aVal).doubleValue() * conversionFactor);
		}
	}

	@Override
	public String getString(Object aVal, boolean isDetailed)
	{
		String aStr;
		double aDouble;

		if (aVal instanceof Number == false)
			return nanStr;
		
		aDouble = ((Number)aVal).doubleValue();
		if (Double.isNaN(aDouble) == true)
			return nanStr;

		// Format the number
		if (format == null)
		{
			aStr = "" + aDouble * conversionFactor;
		}
		else
		{
			synchronized (format)
			{
				aStr = format.format(aDouble * conversionFactor);
			}
		}

		// Add the label component
		if (isDetailed == true)
			aStr += " " + fullLabel;
		else
			aStr += " " + shortLabel;

		return aStr;
	}

	@Override
	public double parseString(String aStr, double eVal)
	{
		double aVal;
		
		aVal = GuiUtil.readDouble(aStr, Double.NaN);
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
