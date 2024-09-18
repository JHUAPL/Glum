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
package glum.gui.component;

import java.awt.Dimension;
import java.awt.Font;

import javax.swing.JLabel;

import glum.unit.UnitListener;
import glum.unit.UnitProvider;

public class GFancyLabel extends JLabel implements UnitListener
{
	// State vars
	private UnitProvider[] unitProviderArr;
	private String[] formatStrArr;
	private Object[] refValueArr;

	/**
	 * Fancy JLabel which provides auto formatting of objects. The constructor is provided with a formatStr which has
	 * unit place holders specified by "%u" There must be a corresponding UnitProvider for each occurrence of "%u". This
	 * is provided via the variable arguments of UnitProvider.
	 */
	public GFancyLabel(String formatStr, UnitProvider... aUnitProviderArr)
	{
		this(null, formatStr, aUnitProviderArr);
	}

	/**
	 * Fancy JLabel which provides auto formatting of objects. The constructor is provided with a formatStr which has
	 * unit place holders specified by "%u" There must be a corresponding UnitProvider for each occurrence of "%u". This
	 * is provided via the variable arguments of UnitProvider.
	 */
	public GFancyLabel(Font aFont, String aFormatStr, UnitProvider... aUnitProviderArr)
	{
		if (aFont != null)
			setFont(aFont);

		formatStrArr = aFormatStr.split("%u", -1);

		unitProviderArr = aUnitProviderArr;
		for (UnitProvider aUnitProvider : unitProviderArr)
			aUnitProvider.addListener(this);

		// Insanity check
		if (unitProviderArr.length != formatStrArr.length - 1)
			throw new RuntimeException(
					"Num place holders: " + (formatStrArr.length - 1) + "    Num units: " + unitProviderArr.length);

		refValueArr = new Object[unitProviderArr.length];
		for (int c1 = 0; c1 < unitProviderArr.length; c1++)
			refValueArr[c1] = null;

		setMinimumSize(new Dimension(0, 0));
	}

	@Override
	public void unitChanged(UnitProvider aProvider, String aKey)
	{
		setValues(refValueArr);
	}

	/**
	 * Method to set in the set of values which will be formatted with the associated UnitProviders which were specified
	 * via the constructor.
	 */
	public void setValues(Object... aValueArr)
	{
		// Ensure the number of objects matches the number of units
		if (unitProviderArr.length != aValueArr.length)
			throw new RuntimeException("Inproper number of arguments. Expected: " + unitProviderArr.length //
					+ " Recieved:" + aValueArr.length);

		for (int c1 = 0; c1 < aValueArr.length; c1++)
			refValueArr[c1] = aValueArr[c1];

		var tmpStr = "";
		for (int c1 = 0; c1 < aValueArr.length; c1++)
		{
			tmpStr += formatStrArr[c1];
			tmpStr += unitProviderArr[c1].getUnit().getString(aValueArr[c1], false);
		}

		if (formatStrArr.length > aValueArr.length)
			tmpStr += formatStrArr[formatStrArr.length - 1];

		setText(tmpStr);
	}

}
