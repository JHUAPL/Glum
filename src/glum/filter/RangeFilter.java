package glum.filter;

import glum.zio.ZinStream;
import glum.zio.ZoutStream;
import glum.zio.raw.ZioRaw;

import java.io.IOException;

/**
 * Abstract filter which is used to filter a single value between the specified min/max ranges. The only code to write
 * is the isValid() method and to call the appropriate Constructor. In the isValid() method, you should delegate filter
 * logic to the method testIsValid() with the quantity of interest, and return the result from the method call.
 */
public abstract class RangeFilter<G1> implements ZioRaw, Filter<G1>
{
	private boolean isEnabled;
	private boolean useMin, useMax;
	private double minValue, maxValue;

	/**
	 * @param aBinCode
	 *           Unique identifier used during serialization. The value specified here should not collide with any other
	 *           codes for which there is serialization.
	 */
	public RangeFilter()
	{
		isEnabled = false;
		useMin = false;
		useMax = false;
		minValue = 0;
		maxValue = 0;
	}

	/**
	 * Accessor methods
	 */
	// @formatter:off
	public boolean getIsEnabled() { return isEnabled; }
	public boolean getUseMin() { return useMin; }
	public boolean getUseMax() { return useMax; }
	public double getMinValue() { return minValue; }
	public double getMaxValue() { return maxValue; }
	
	public void setIsEnabled(boolean aBool) { isEnabled = aBool; }
	public void setUseMin(boolean aBool) { useMin = aBool; }
	public void setUseMax(boolean aBool) { useMax = aBool; }
	public void setMinValue(double aValue) { minValue = aValue; }
	public void setMaxValue(double aValue) { maxValue = aValue; }
	// @formatter:on

	/**
	 * Sets this filter to match aFilter
	 */
	public void set(RangeFilter<G1> aFilter)
	{
		isEnabled = aFilter.getIsEnabled();
		useMin = aFilter.getUseMin();
		useMax = aFilter.getUseMax();
		minValue = aFilter.getMinValue();
		maxValue = aFilter.getMaxValue();
	}

	@Override
	public void zioReadRaw(ZinStream aStream) throws IOException
	{
		byte bSwitch;

		aStream.readVersion(0);

		bSwitch = aStream.readByte();
		isEnabled = (bSwitch & 0x1) != 0;
		useMin = (bSwitch & 0x2) != 0;
		useMax = (bSwitch & 0x4) != 0;

//		isEnabled = aStream.readBoolean();
//		useMin = aStream.readBoolean();
//		useMax = aStream.readBoolean();
		minValue = aStream.readDouble();
		maxValue = aStream.readDouble();
	}

	@Override
	public void zioWriteRaw(ZoutStream aStream) throws IOException
	{
		byte bSwitch;

		aStream.writeVersion(0);

		bSwitch = 0;
		if (isEnabled == true)
			bSwitch |= 0x1;
		if (useMin == true)
			bSwitch |= 0x2;
		if (useMax == true)
			bSwitch |= 0x4;
		aStream.writeByte(bSwitch);

//		aStream.writeBoolean(isEnabled);
//		aStream.writeBoolean(useMin);
//		aStream.writeBoolean(useMax);
		aStream.writeDouble(minValue);
		aStream.writeDouble(maxValue);
	}

	/**
	 * Utility method that returns whether aValue is within the constraints specified by this filter.
	 */
	protected boolean testIsValid(double aValue)
	{
		if (isEnabled == false)
			return true;

		if (useMin == true && aValue < minValue)
			return false;

		if (useMax == true && aValue > maxValue)
			return false;

		return true;
	}

}
