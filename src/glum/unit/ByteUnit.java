package glum.unit;

public class ByteUnit extends HeuristicUnit
{
	// Constants
	public static final double Kilobyte = 1024;
	public static final double Megabyte = 1024 * 1024;
	public static final double Gigabyte = 1024 * 1024 * 1024;
	public static final double Terabyte = 1024 * 1024 * 1024L * 1024L;

	public ByteUnit(int numDecimalPlaces)
	{
		super("Heuristic", numDecimalPlaces);
	}

	@Override
	public String getString(Object aVal)
	{
		String aStr, unitStr;
		long numBytes;
		double dVal;

		aStr = "N/A";
		if (aVal instanceof Number == false)
			return aStr;
		
		numBytes = ((Number)aVal).longValue();

		if (numBytes < Kilobyte)
		{
			unitStr = "B";
			dVal = numBytes;
		}
		else if (numBytes < Megabyte)
		{
			unitStr = "KB";
			dVal = (numBytes + 0.0) / Kilobyte;
		}
		else if (numBytes < Gigabyte)
		{
			unitStr = "MB";
			dVal = (numBytes + 0.0) / Megabyte;
		}
		else if (numBytes < Terabyte)
		{
			unitStr = "GB";
			dVal = (numBytes + 0.0) / Gigabyte;
		}
		else
		{
			unitStr = "TB";
			dVal = (numBytes + 0.0) / Terabyte;
		}

		synchronized (format)
		{
			return format.format(dVal) + " " + unitStr;
		}
	}

	@Override
	public HeuristicUnit spawnClone(int numDecimalPlaces)
	{
		return new ByteUnit(numDecimalPlaces);
	}

}
