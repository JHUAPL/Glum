package glum.coord;

/** Simple class for Lat/Lon values. */
public class LatLon
{
	public double lat;
	public double lon;

	public LatLon()
	{
	}

	public LatLon(LatLon latlon)
	{
		if (latlon != null)
		{
			lat = latlon.lat;
			lon = latlon.lon;
		}
	}

	public LatLon(double lat, double lon)
	{
		this.lat = lat;
		this.lon = lon;
	}

	public LatLon(String lat_string, String lon_string)
	{
		set(lat_string, lon_string);
	}

	public void set(double lat, double lon)
	{
		this.lat = lat;
		this.lon = lon;
	}

	public void set(LatLon latlon)
	{
		if (latlon != null)
		{
			lat = latlon.lat;
			lon = latlon.lon;
		}
	}

	public void set(String lat_string, String lon_string)
	{
		lat = CoordUtil.StringToLat(lat_string);
		lon = CoordUtil.StringToLon(lon_string);
	}

	public void normalize()
	{
		if (lat > 90)
			lat = 90;
		else if (lat < -90)
			lat = -90;

		if (lon > 180)
			lon -= 360;
		else if (lon < -180)
			lon += 360;
	}

	/**
	 * Tests to see if the given object is the same lat/lon as this position.
	 * "Same" really means "very, very close," as defined by {@link Epsilon}.
	 * 
	 * @return True if obj is a LatLon and is very close to our lat/lon position.
	 *         False otherwise.
	 */
	@Override
	public boolean equals(Object obj)
	{
		return (obj instanceof LatLon) && Epsilon.close(lat, ((LatLon)obj).lat) && Epsilon.close(lon, ((LatLon)obj).lon);
	}

	@Override
	public String toString()
	{
		return CoordUtil.LatLonToString(this);
	}

	/**
	 * Returns the change in latitude
	 */
	static public double computeDeltaLat(double lat1, double lat2)
	{
		return lat2 - lat1;
	}

	/**
	 * Returns the change in longitude
	 */
	static public double computeDeltaLon(double lon1, double lon2)
	{
		double dLon;

		dLon = lon2 - lon1;
		if (Math.abs(dLon) < 180)
			return dLon;

		if (dLon > 180)
			return dLon - 360;
		else
			return dLon + 360;
	}

}
