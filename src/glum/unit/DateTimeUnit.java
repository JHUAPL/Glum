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

import java.time.format.DateTimeFormatter;
import java.time.temporal.TemporalAccessor;

/**
 * Implementation of {@link Unit} for displaying temporal objects such as date, time, etc...
 *
 * @author lopeznr1
 */
public class DateTimeUnit implements Unit
{
	// Attributes
	private final String configName;
	private final DateTimeFormatter formatter;

	// State vars
	private String nullStr;

	/** Standard Constructor */
	public DateTimeUnit(String aConfigName, DateTimeFormatter aFormatter)
	{
		configName = aConfigName;
		formatter = aFormatter;

		nullStr = "---";
	}

	/** Alternative Constructor */
	public DateTimeUnit(String aConfigName, String aFmtStr)
	{
		configName = aConfigName;
		formatter = DateTimeFormatter.ofPattern(aFmtStr);

		nullStr = "---";
	}

	/**
	 * Sets in the string representation for null (or invalid) values
	 */
	public void setNaNString(String aStr)
	{
		nullStr = aStr;
	}

	@Override
	public String getConfigName()
	{
		return configName;
	}

	@Override
	public String getLabel(boolean aIsDetailed)
	{
		return "";
	}

	@Override
	public String getString(Object aVal)
	{
		TemporalAccessor tmpAccessor;

		// Note we format a TemporalAccessor rather than to LocalDate,LocalTime,...
		// since this is much more generic
		tmpAccessor = null;
		if (aVal instanceof TemporalAccessor)
			tmpAccessor = (TemporalAccessor) aVal;

		// Bail if unrecognized object
		if (tmpAccessor == null)
			return nullStr;

		return formatter.format(tmpAccessor);
	}

	@Override
	public String getString(Object aVal, boolean aIsDetailed)
	{
		return getString(aVal);
	}

	@Override
	public double parseString(String aDateStr, double aErrorVal)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public double toModel(double aVal)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public double toUnit(double aVal)
	{
		throw new UnsupportedOperationException();
	}

}
