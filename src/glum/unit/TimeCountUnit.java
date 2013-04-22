package glum.unit;

import static glum.util.TimeConst.*;
import glum.util.WallTimer;

/**
 * Unit used to display a total count of time. This Unit is not configurable and only numerical values should be passed
 * in where each increment represents 1 millisecond. If a WallTimer is passed in then this Unit will display it's total
 * time.
 */
public class TimeCountUnit extends HeuristicUnit
{
	// Constants
	private static final long MAX_SEC_BEFORE_FMT_DAY = 2 * MS_IN_DAY;
	private static final long MAX_SEC_BEFORE_FMT_HOUR = 2 * MS_IN_HOUR;
	private static final long MAX_SEC_BEFORE_FMT_MIN = 2 * MS_IN_MIN;

	// State vars
	private String nanStr;

	public TimeCountUnit(int numDecimalPlaces)
	{
		super("Heuristic", numDecimalPlaces);
		nanStr = "---";
	}

	@Override
	public String getString(Object aObj)
	{
		double numMS;
		String aStr;

		// Transform WallTimers to their total count
		if (aObj instanceof WallTimer)
			aObj = ((WallTimer)aObj).getTotal();

		// We need a number
		if (aObj instanceof Number == false)
			return "N/A";

		numMS = ((Number)aObj).doubleValue();
		if (Double.isNaN(numMS) == true)
			return nanStr;

		if (numMS > MAX_SEC_BEFORE_FMT_DAY)
			aStr = format.format(numMS / MS_IN_DAY) + " days";
		else if (numMS > MAX_SEC_BEFORE_FMT_HOUR)
			aStr = format.format(numMS / MS_IN_HOUR) + " hrs";
		else if (numMS > MAX_SEC_BEFORE_FMT_MIN)
			aStr = format.format(numMS / MS_IN_MIN) + " min";
		else
			aStr = format.format(numMS / MS_IN_SEC) + " sec";

		return aStr;
	}

	@Override
	public HeuristicUnit spawnClone(int numDecimalPlaces)
	{
		return new TimeCountUnit(numDecimalPlaces);
	}

}
