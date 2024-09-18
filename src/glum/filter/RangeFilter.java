// Copyright (C) 2024 The Johns Hopkins University Applied Physics Laboratory LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package glum.filter;

import java.io.IOException;

import glum.zio.*;

/**
 * Abstract {@link Filter} which is used to filter a single value between the specified min/max ranges. The only code to
 * write is the isValid() method. In the isValid() method, you should delegate filter logic to the method testIsValid()
 * with the quantity of interest, and return the result from the method call.
 *
 * @author lopeznr1
 *
 * @param <G1>
 */
public abstract class RangeFilter<G1> implements Filter<G1>, ZioObj
{
	// State vars
	private boolean isEnabled;
	private boolean useMin, useMax;
	private double minValue, maxValue;

	/** Standard Constructor */
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
	public void zioRead(ZinStream aStream) throws IOException
	{
		aStream.readVersion(0);

		byte bSwitch = aStream.readByte();
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
	public void zioWrite(ZoutStream aStream) throws IOException
	{
		aStream.writeVersion(0);

		byte bSwitch = 0;
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
