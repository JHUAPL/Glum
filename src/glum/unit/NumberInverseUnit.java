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

		if (format == null)
			return "" + ((1.0/((Number)aVal).doubleValue()) * conversionFactor);

		synchronized (format)
		{
			return format.format((1.0/((Number)aVal).doubleValue()) * conversionFactor);
		}
	}

	@Override
	public String getString(Object aVal, boolean isDetailed)
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
