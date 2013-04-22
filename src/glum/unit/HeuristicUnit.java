package glum.unit;

import java.text.DecimalFormat;
import java.text.Format;

/**
 * A special kind of Unit that converts the value to the most human readable format. Thus, this Unit does not support
 * any internal model, and calling associated model conversion routines will throw an
 * {@link UnsupportedOperationException}. <BR>
 * <BR>
 * The primary method to override is getString(Object aVal)
 */
public abstract class HeuristicUnit implements Unit
{
	// State vars
	private String configName;
	protected DecimalFormat format;

	public HeuristicUnit(String aConfigName, int numDecimalPlaces)
	{
		String aStr;

		configName = aConfigName;

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
	 * Spawns a near exact copy of this HeuristicUnit (only different number of decimal places)
	 */
	public abstract HeuristicUnit spawnClone(int numDecimalPlaces);

	@Override
	public String getConfigName()
	{
		return configName;
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
		return "";
	}

	@Override
	public String getString(Object aVal, boolean isDetailed)
	{
		return getString(aVal);
	}

	@Override
	public double parseString(String aStr, double eVal)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public double toModel(double aVal)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public double toUnit(double aVal)
	{
		throw new UnsupportedOperationException();
	}

}
