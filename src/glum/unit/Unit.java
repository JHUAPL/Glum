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

import java.text.Format;

/**
 * Interface that provides a mechanism of transforming model values to a user oriented string.
 * <p>
 * Transforming a user oriented string back to a (numerical) model value is supported via
 * {@link #parseString(String, double)}.
 *
 * @author lopeznr1
 */
public interface Unit
{
	/**
	 * Returns the formal name associated with the unit
	 */
	public String getConfigName();

	/**
	 * Returns Format object associated with the unit.
	 */
	public default Format getFormat()
	{
		return null;
	}

	/**
	 * Returns the label associated with the unit.
	 */
	public String getLabel(boolean aIsDetailed);

	/**
	 * Returns string representation of aVal w.r.t this unit without the associated label.
	 */
	public String getString(Object aVal);

	/**
	 * Returns string representation of aVal w.r.t this unit with the associated (detailed if isDetailed == true) label.
	 */
	public String getString(Object aVal, boolean aIsDetailed);

	/**
	 * Returns the model value which corresponds to the specified input string. The input string should be in this unit.
	 * If no value can be parsed then eVal is returned.
	 */
	public double parseString(String aStr, double eVal);

	/**
	 * Assume aVal is in units of this and returns it as model units.
	 */
	public double toModel(double aVal);

	/**
	 * Returns aVal in terms of this unit. Note aVal is assumed to be in model units.
	 */
	public double toUnit(double aVal);

}
