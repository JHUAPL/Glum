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
package glum.text;

import java.math.BigDecimal;
import java.math.MathContext;
import java.text.*;

/**
 * NumberFormat used to display values with a desired number of significant figures.
 * <p>
 * TODO: This class is incomplete and was rushed just to support simple display of values with a requested number of
 * significant digits.
 *
 * @author lopeznr1
 */
public class SigFigNumberFormat extends NumberFormat
{
	// Attributes
	private final String nanStr;
	private final int numSigFigs;

	/**
	 * Standard Constructor
	 *
	 * @param aNumSigFigs
	 *        The number of significant figures to display
	 * @param aNaNStr
	 *        The string to show for values of NaN.
	 */
	public SigFigNumberFormat(int aNumSigFigs, String aNaNStr)
	{
		nanStr = aNaNStr;
		numSigFigs = aNumSigFigs;
	}

	/**
	 * Simplified Constructor
	 *
	 * @param aNumSigFigs
	 *        The number of significant figures to display
	 */
	public SigFigNumberFormat(int aNumSigFigs)
	{
		this(aNumSigFigs, null);
	}

	@Override
	public StringBuffer format(double number, StringBuffer result, FieldPosition fieldPosition)
	{
		// Special handling for infinite
		if (Double.isInfinite(number) == true)
			return result.append("" + number);

		// Special handling for NaN
		if (Double.isNaN(number) == true)
		{
			if (nanStr != null)
				return result.append(nanStr);

			result.append("-.");
			for (int c1 = 0; c1 < numSigFigs - 1; c1++)
				result.append("-");

			return result;
		}

		BigDecimal tmpBD = getValueRoundedToSigFigs(number, numSigFigs);
		return result.append("" + tmpBD);
	}

	@Override
	public StringBuffer format(long number, StringBuffer toAppendTo, FieldPosition pos)
	{
		throw new RuntimeException("NOT IMPLEMENTED");
	}

	@Override
	public Number parse(String source, ParsePosition parsePosition)
	{
		throw new RuntimeException("NOT IMPLEMENTED");
	}

	/**
	 * Utility method that returns a BigDecimal rounded to the specified number of significant figures.
	 * <p>
	 * Source: https://stackoverflow.com/questions/7548841
	 * <p>
	 * TODO: Consider promoting this method to a utility class.
	 *
	 * @param aValue
	 *        The double value to be rounded
	 * @param aNumSigFigs
	 *        The number of significant figures of interest.
	 */
	private static BigDecimal getValueRoundedToSigFigs(double aValue, int aNumSigFigs)
	{
		BigDecimal retBD = new BigDecimal(aValue);
		retBD = retBD.round(new MathContext(aNumSigFigs));

		return retBD;
	}

}