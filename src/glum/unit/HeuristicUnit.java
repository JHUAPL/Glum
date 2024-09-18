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
import java.text.Format;

/**
 * Implementation of {@link Unit} that converts the value to the most human readable format.
 * <p>
 * This Unit does not support any internal model, and calling associated model conversion routines will throw an
 * {@link UnsupportedOperationException}.
 * <p>
 * The primary method to override is getString(Object aVal)
 *
 * @author lopeznr1
 */
public abstract class HeuristicUnit implements Unit
{
	// Attributes
	private final String configName;
	protected final DecimalFormat dispFormat;

	/** Standard Constructor */
	public HeuristicUnit(String aConfigName, DecimalFormat aDispFormat)
	{
		configName = aConfigName;
		dispFormat = aDispFormat;
	}

	/**
	 * Spawns a near exact copy of this HeuristicUnit (only different number of decimal places)
	 */
	public abstract HeuristicUnit spawnClone(int aNumDecimalPlaces);

	@Override
	public String getConfigName()
	{
		return configName;
	}

	@Override
	public Format getFormat()
	{
		if (dispFormat == null)
			return null;

		return (Format) dispFormat.clone();
	}

	@Override
	public String getLabel(boolean aIsDetailed)
	{
		return "";
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
		throw new UnsupportedOperationException();
	}

	@Override
	public double toUnit(double aVal)
	{
		throw new UnsupportedOperationException();
	}

}
