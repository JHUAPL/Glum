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

import java.text.*;
import java.util.*;

public class DateUnit implements Unit
{
	// State vars
	private String configName;
	private SimpleDateFormat format;

	private String nullStr;
	private boolean markZeroAsNull;

	/**
	 * Constructor
	 */
	public DateUnit(String aConfigName, String aFmtStr, TimeZone aTimeZone)
	{
		configName = aConfigName;
		format = new SimpleDateFormat(aFmtStr);
		if (aTimeZone != null)
			format.setTimeZone(aTimeZone);

		nullStr = "None";
		markZeroAsNull = false;
	}

	public DateUnit(String aConfigName, String aFmtStr)
	{
		this(aConfigName, aFmtStr, null);
	}

	public DateUnit(String aConfigName)
	{
		this(aConfigName, "yyyy-MM-dd", null);
	}

	/**
	 * Returns the TimeZone associated with this unit
	 */
	public TimeZone getTimeZone()
	{
		if (format == null)
			return TimeZone.getDefault();

		return format.getTimeZone();
	}

	/**
	 * Method to cause long values of 0 (UTC == 0) to be displayed in the same fashion as null rather than as the
	 * formated date of 1969Dec31...
	 */
	public void setMarkZeroAsNull(boolean aBool)
	{
		markZeroAsNull = aBool;
	}

	/**
	 * Sets in the string representation for null (or invalid) values
	 */
	public void setNaNString(String aStr)
	{
		nullStr = aStr;
	}

	/**
	 * Method to parse the UTC time using this DateUnit's configuration to interpret aStr
	 */
	public long parseString(String aTimeStr, long eTime)
	{
		Date aDate;
		long aTime;

		try
		{
			aDate = format.parse(aTimeStr);
			aTime = aDate.getTime();
		}
		catch (ParseException aExp)
		{
			aTime = eTime;
			aExp.printStackTrace();
		}

		return aTime;
	}

	@Override
	public String getConfigName()
	{
		return configName;
	}

	@Override
	public SimpleDateFormat getFormat()
	{
		if (format == null)
			return null;

		return (SimpleDateFormat)format.clone();
	}

	@Override
	public String getLabel(boolean aIsDetailed)
	{
		return "";
	}

	@Override
	public String getString(Object aVal)
	{
		Date aDate;

		// Retrieve the appropriate Date object
		aDate = null;
		if (aVal instanceof Calendar)
			aDate = ((Calendar)aVal).getTime();
		else if (aVal instanceof Date)
			aDate = (Date)aVal;
		else if (aVal instanceof Long)
		{
			Long aLong;

			aLong = (Long)aVal;
			if (aLong == 0 && markZeroAsNull == true)
				return nullStr;

			Calendar aCalendar;
			aCalendar = new GregorianCalendar();
			aCalendar.setTimeInMillis(aLong);
			aDate = aCalendar.getTime();
		}
		else
			return nullStr;

		return format.format(aDate);
	}

	@Override
	public String getString(Object aVal, boolean aIsDetailed)
	{
		return getString(aVal);
	}

	@Override
	public double parseString(String aStr, double eVal)
	{
		throw new UnsupportedOperationException();
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
