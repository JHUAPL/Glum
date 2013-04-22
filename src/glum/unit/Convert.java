package glum.unit;

/** Contains conversion multipliers to/from feet, yards, meters, data miles,
* and nautical miles, as well as angular values to/from degrees and radians.
* To convert a value <code>X</code> in units of <code>U</code> to units of
* <code>V</code>, use <code>X * Convert.U_TO_V</code>.
*/
public class Convert
{
	public static final double   FEET_TO_METERS = 0.3048;
	public static final double     DM_TO_METERS = 1828.8;
	public static final double     NM_TO_METERS = 1852.0;
	public static final double  MILES_TO_METERS = 1609.344;
	public static final double  YARDS_TO_METERS = 0.9144; // 3 * FEET_TO_METERS

	public static final double   METERS_TO_FEET = 1.0 / FEET_TO_METERS;
	public static final double       DM_TO_FEET = 6000.0;
	public static final double       NM_TO_FEET = NM_TO_METERS * METERS_TO_FEET;
	public static final double    MILES_TO_FEET = 5280.0;
	public static final double    YARDS_TO_FEET = 3.0;

	public static final double     METERS_TO_DM = 1.0 / DM_TO_METERS;
	public static final double       FEET_TO_DM = FEET_TO_METERS * METERS_TO_DM;
	public static final double         NM_TO_DM = NM_TO_METERS * METERS_TO_DM;
	public static final double      MILES_TO_DM = MILES_TO_METERS * METERS_TO_DM;
	public static final double      YARDS_TO_DM = YARDS_TO_METERS * METERS_TO_DM;

	public static final double     METERS_TO_NM = 1.0 / NM_TO_METERS;
	public static final double       FEET_TO_NM = FEET_TO_METERS * METERS_TO_NM;
	public static final double         DM_TO_NM = DM_TO_METERS * METERS_TO_NM;
	public static final double      MILES_TO_NM = MILES_TO_METERS * METERS_TO_NM;
	public static final double      YARDS_TO_NM = YARDS_TO_METERS * NM_TO_METERS;

	public static final double  METERS_TO_MILES = 1.0 / MILES_TO_METERS;
	public static final double    FEET_TO_MILES = FEET_TO_METERS * METERS_TO_MILES;
	public static final double      DM_TO_MILES = DM_TO_METERS * METERS_TO_MILES;
	public static final double      NM_TO_MILES = NM_TO_METERS * METERS_TO_MILES;
	public static final double   YARDS_TO_MILES = YARDS_TO_METERS * METERS_TO_MILES;

	public static final double  METERS_TO_YARDS = 1.0 / YARDS_TO_METERS;
	public static final double    FEET_TO_YARDS = 1.0 / 3.0;
	public static final double      DM_TO_YARDS = 2000.0;
	public static final double      NM_TO_YARDS = NM_TO_METERS * METERS_TO_YARDS;
	public static final double   MILES_TO_YARDS = 1760.0;

	public static final double   RAD_TO_DEG = 180.0 / Math.PI;
	public static final double   DEG_TO_RAD = Math.PI / 180.0;

	public static final double    SECS_TO_MSECS = 1000.0;
	public static final double    MSECS_TO_SECS = 1.0 / SECS_TO_MSECS;

	public static final int        MINS_TO_SECS = 60;
	public static final int       HOURS_TO_MINS = 60;
	public static final int       HOURS_TO_SECS = HOURS_TO_MINS * MINS_TO_SECS;

	public static final double     SECS_TO_MINS = 1 / MINS_TO_SECS;
	public static final double    MINS_TO_HOURS = 1 / HOURS_TO_MINS;
	public static final double    SECS_TO_HOURS = 1 / HOURS_TO_SECS;


	/**
	* angleToBearing - Converts an angle to a bearing
	*/
	public static double angleToBearing(double aAngle)
	{
		double bearing;

		bearing = 180 - (aAngle + 90);
		if (bearing < 0)
			bearing += 360;

		return bearing;
	}


	/**
	* beairngToAngle - Converts a bearing to an angle
	*/
	public static double bearingToAngle(double aBearing)
	{
		double angle;

		angle = 180 - (aBearing + 90);
		if (angle < 0)
			angle += 360;

		return angle;
	}

}
