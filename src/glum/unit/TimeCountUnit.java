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
package glum.unit;

import static glum.util.TimeConst.MS_IN_DAY;
import static glum.util.TimeConst.MS_IN_HOUR;
import static glum.util.TimeConst.MS_IN_MIN;
import static glum.util.TimeConst.MS_IN_SEC;

import java.text.DecimalFormat;
import java.time.Duration;

import glum.util.WallTimer;

/**
 * {@link HeuristicUnit} used to display a total count of time.
 * <p>
 * This unit supports three types of inputs:
 * <ul>
 * <li>Numerical values - where each increment represents 1 millisecond.
 * <li>{@link WallTimer} - where this unit will display it's total time.
 * <li>{@link Duration}
 * </ul>
 *
 * @author lopeznr1
 */
public class TimeCountUnit extends HeuristicUnit
{
	// Constants
	private static final long MAX_SEC_BEFORE_FMT_DAY = 2 * MS_IN_DAY;
	private static final long MAX_SEC_BEFORE_FMT_HOUR = 2 * MS_IN_HOUR;
	private static final long MAX_SEC_BEFORE_FMT_MIN = 2 * MS_IN_MIN;

	// Attributes
	private final String nanStr;

	/** Standard Constructor */
	public TimeCountUnit(DecimalFormat aDispFormat, String aNanStr)
	{
		super("Heuristic", aDispFormat);
		nanStr = aNanStr;
	}

	/** Alternative Constructor */
	public TimeCountUnit(int aNumDecimalPlaces, String aNanStr)
	{
		this(UnitUtil.formFormatWithNumDecimalPlaces(aNumDecimalPlaces), aNanStr);
	}

	/** Simplified Constructor */
	public TimeCountUnit(int aNumDecimalPlaces)
	{
		this(UnitUtil.formFormatWithNumDecimalPlaces(aNumDecimalPlaces), "---");
	}

	@Override
	public String getString(Object aObj)
	{
		// Transform WallTimers to their total count
		if (aObj instanceof WallTimer)
			aObj = ((WallTimer) aObj).getTotal();

		// We need a number
		if (aObj instanceof Duration)
			aObj = ((Duration) aObj).toMillis();
		else if (aObj instanceof Number == false)
			return nanStr;

		var numMS = ((Number) aObj).doubleValue();
		if (Double.isNaN(numMS) == true)
			return nanStr;

		if (numMS > MAX_SEC_BEFORE_FMT_DAY)
			return dispFormat.format(numMS / MS_IN_DAY) + " days";
		else if (numMS > MAX_SEC_BEFORE_FMT_HOUR)
			return dispFormat.format(numMS / MS_IN_HOUR) + " hrs";
		else if (numMS > MAX_SEC_BEFORE_FMT_MIN)
			return dispFormat.format(numMS / MS_IN_MIN) + " min";
		else
			return dispFormat.format(numMS / MS_IN_SEC) + " sec";
	}

	@Override
	public HeuristicUnit spawnClone(int numDecimalPlaces)
	{
		return new TimeCountUnit(numDecimalPlaces);
	}

}
