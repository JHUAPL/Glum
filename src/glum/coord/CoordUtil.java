package glum.coord;


/** Provides a few useful functions on coordinates, such as converting
* to a user-presentable string.
*/
public class CoordUtil
{
	/** Convert a Lat/Lon to a pair of DEG:MM:SS H strings.  H is the
	* hemisphere, N or S for lat, E or W for lon.  The default separator
	* string LL_SEP is used.
	*/
	public static String LatLonToString (LatLon ll)
	{
		return ll == null ? "" : LatLonToString (ll, LL_SEP);
	}

	/** Same as the other LatLonToString, excepts this one uses the
	* given <code>sep</code> string to separate the Lat and Lon.
	*/
	public static String LatLonToString (LatLon ll, String sep)
	{
		return ll == null ? "" :
			LatToString (ll.lat) + LL_SEP + LonToString (ll.lon);
	}

	/** Converts the given <code>lat</code> to DD:MM:SS H. */

	public static String LatToString (double lat)
	{
		return LatToString (lat, true);
	}

	/** Converts the given <code>lat</code> to DD:MM:SS H if
	* <code>include_seconds</code> is true.  If it's false, then the
	* :SS part is left off.
	*/

	public static String LatToString (double lat, boolean include_seconds)
	{
		DMS		dms = new DMS (lat);
		StringBuffer	s = new StringBuffer();

		if ( dms.degrees < 10 )
			s.append ("0");
		s.append (dms.degrees);
		s.append (":");
		if ( dms.minutes < 10 )
			s.append ("0");
		s.append (dms.minutes);
		if ( include_seconds )
		{
			s.append (":");
			if ( dms.seconds < 10 )
				s.append ("0");
			s.append (dms.seconds);
		}
		s.append (lat >= 0 ? " N" : " S");
		return s.toString();
	}

	/** Similar to <code>LatToString</code> except that the degrees
	* part is DDD instead of DD. */

	public static String LonToString (double lon)
	{
		return LonToString (lon, true);
	}

	/** Similar to <code>LatToString</code> except that the degrees
	* part is DDD instead of DD. */

	public static String LonToString (double lon, boolean include_seconds)
	{
		DMS		dms = new DMS (lon);
		StringBuffer	s = new StringBuffer();

		if ( dms.degrees < 100 )
			s.append ("0");
		if ( dms.degrees < 10 )
			s.append ("0");
		s.append (dms.degrees);
		s.append (":");
		if ( dms.minutes < 10 )
			s.append ("0");
		s.append (dms.minutes);
		if ( include_seconds )
		{
			s.append (":");
			if ( dms.seconds < 10 )
				s.append ("0");
			s.append (dms.seconds);
		}
		s.append (lon >= 0 ? " E" : " W");
		return s.toString();
	}

	/** Converts <code>dmsh_string</code> to a double value.
	* The string format should match the output of the
	* LatToString formats, including hemisphere.
	* If a hemisphere character is not part of the string, the
	* returned value will be non-negative.
	*/
	public static double StringToLat (String dmsh_string)
	{
		if ( dmsh_string == null || dmsh_string.length() == 0 )
			return 0.0;

		int	dms [] = StringToDMS (dmsh_string);

		if ( dms.length == 3 )
			return new Degrees (dms[0], dms[1], dms[2]).degrees;
		else	return 0.0;
	}

	/** {@see StringToLat} */

	public static double StringToLon (String dmsh_string)
	{
		// Because we aren't doing any range or hemisphere error
		// checking, a lon value is identical to a lat value.
		return StringToLat (dmsh_string);
	}

	/** Converts <code>dmsh_string</code> to a an array of
	* 3 ints representing degrees, minutes, and seconds.
	* if a hemisphere character is present (one of NSEW or
	* nsew), and it represents a souther or western hemisphere,
	* then the degrees value, in index 0 of the returned array,
	* will be a non-positive number.
	*/
	public static int [] StringToDMS (String dmsh_string)
	{
		if ( dmsh_string == null || dmsh_string.length() == 0 )
			return null;

		char	chars [] = dmsh_string.toCharArray();
		int	dms [] = new int [ 3 ];

		dms[0] = 0;
		for ( int i = 0,  j = 0;  i < chars.length;  i++ )
		{
			char	c = chars[i];

			if ( c == ' '  ||  c == '	' ) // Space or tab.
				continue;
			else if ( c >= '0'  &&  c <= '9'  &&  j < 3 )
				dms[j] = dms[j] * 10 + c - '0';
			else if ( c == ':' )
			{
				j++;
				dms[j] = 0;
			}
			else if ( c == 'S' || c == 's' || c == 'W' || c == 'w' )
				dms[0] = -dms[0];
		}

		return dms;
	}

	public static class DMS
	{
		public DMS (double deg)
		{
			if ( deg < 0 ) deg = -deg;
			degrees = (int) deg;
			minutes = (int) (deg * 60) % 60;
			seconds = (int) (deg * 3600) % 60;
		}
		public int degrees, minutes, seconds;
	}

	public static class Degrees
	{
		public Degrees (int deg, int min, int sec)
		{
			degrees = Math.abs (deg) +
				Math.abs(min) / 60.0 +
				Math.abs(sec) / 3600.0;
			if ( deg < 0 || min < 0 || sec < 0 )
				degrees = -degrees;
		}
		public Degrees (int deg, int min, int sec, char hemisphere)
		{
			this (deg, min, sec);
			if ( hemisphere == 'N' || hemisphere == 'n' ||
				hemisphere == 'E' || hemisphere == 'e' )
				degrees = Math.abs (degrees);
			else if ( hemisphere == 'S' || hemisphere == 's' ||
				hemisphere == 'W' || hemisphere == 'w' )
				degrees = -Math.abs (degrees);
		}
		public double degrees;
	}

	public static String	LL_SEP = " / ";
}
