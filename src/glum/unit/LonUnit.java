package glum.unit;

import java.text.*;

import glum.coord.*;

public class LonUnit implements Unit
{
	// State vars
	private boolean isSecondsShown;

	/**
	* Constructor
	*/
	public LonUnit(boolean aIsSecondsShown)
	{
		isSecondsShown = aIsSecondsShown;
	}

	/**
	* isSecondsShown
	*/
	public boolean isSecondsShown()
	{
		return isSecondsShown;
	}

	@Override
	public String getConfigName()
	{
		return "Lon";
	}

	@Override
	public Format getFormat()
	{
		return null;
	}

	@Override
	public String getLabel(boolean isDetailed)
	{
		return "";
	}

	@Override
	public String getString(Object aObj)
	{
		Double aVal;

		if (aObj instanceof Number)
			aVal = ((Number)aObj).doubleValue();
/*		else if (aObj instanceof LatLon)
			aVal = ((LatLon)aVal).lon;
*/		else
			return "N/A";

		return CoordUtil.LonToString(aVal, isSecondsShown);
	}

	@Override
	public String getString(Object aObj, boolean isDetailed)
	{
		Double aVal;

		if (aObj instanceof Number)
			aVal = ((Number)aObj).doubleValue();
/*		else if (aObj instanceof LatLon)
			aVal = ((LatLon)aVal).lon;
*/		else
			return "N/A";

		return CoordUtil.LonToString(aVal, isSecondsShown);
	}

	@Override
	public double parseString(String aStr, double eVal)
	{
		return CoordUtil.StringToLon(aStr);
	}

	@Override
	public double toModel(double aVal)
	{
		return aVal;
	}

	@Override
	public double toUnit(double aVal)
	{
		return aVal;
	}

}
