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

import java.text.DecimalFormat;

/**
 * {@link HeuristicUnit} used to display a total count of bytes.
 *
 * @author lopeznr1
 */
public class ByteUnit extends HeuristicUnit
{
	// Constants
	public static final double Kilobyte = 1024;
	public static final double Megabyte = 1024 * 1024;
	public static final double Gigabyte = 1024 * 1024 * 1024;
	public static final double Terabyte = 1024 * 1024 * 1024L * 1024L;

	// Attributes
	private final String nanStr;

	/** Standard Constructor */
	public ByteUnit(DecimalFormat aDispFormat, String aNaNStr)
	{
		super("Heuristic", aDispFormat);
		nanStr = aNaNStr;
	}

	/** Simplified Constructor */
	public ByteUnit(DecimalFormat aDispFormat)
	{
		this(aDispFormat, "---");
	}

	/** Alternative Constructor */
	public ByteUnit(int aNumDecimalPlaces, String aNaNStr)
	{
		this(UnitUtil.formFormatWithNumDecimalPlaces(aNumDecimalPlaces), aNaNStr);
	}

	/** Alternative Constructor */
	public ByteUnit(int aNumDecimalPlaces)
	{
		this(aNumDecimalPlaces, "---");
	}

	@Override
	public String getString(Object aVal)
	{
		if (aVal instanceof Number == false)
			return nanStr;

		var numBytes = ((Number) aVal).longValue();

		String unitStr;
		double dVal;
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

		synchronized (dispFormat)
		{
			return dispFormat.format(dVal) + " " + unitStr;
		}
	}

	@Override
	public HeuristicUnit spawnClone(int numDecimalPlaces)
	{
		return new ByteUnit(numDecimalPlaces);
	}

}
