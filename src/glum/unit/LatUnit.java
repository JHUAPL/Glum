package glum.unit;

import java.text.*;

import glum.coord.*;

public class LatUnit implements Unit
{
	// State vars
	private boolean isSecondsShown;

	/**
	* Constructor
	*/
	public LatUnit(boolean aIsSecondsShown)
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
		return "Lat";
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
			aVal = ((LatLon)aVal).lat;
*/		else
			return "N/A";

		return CoordUtil.LatToString(aVal, isSecondsShown);
	}

	@Override
	public String getString(Object aObj, boolean isDetailed)
	{
		Double aVal;

		if (aObj instanceof Number)
			aVal = ((Number)aObj).doubleValue();
/*		else if (aObj instanceof LatLon)
			aVal = ((LatLon)aVal).lat;
*/		else
			return "N/A";

		return CoordUtil.LatToString(aVal, isSecondsShown);
	}

	@Override
	public double parseString(String aStr, double eVal)
	{
		return CoordUtil.StringToLat(aStr);
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
