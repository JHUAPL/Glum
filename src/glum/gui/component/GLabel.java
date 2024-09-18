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

import glum.unit.*;

public class GLabel extends JLabel implements UnitListener
{
	// State vars
	protected UnitProvider refUnitProvider;
	protected Object refValue;
	protected boolean showLabel;

	public GLabel(Font aFont)
	{
		this(null, aFont, false);
	}

	public GLabel(Unit aUnit, Font aFont)
	{
		this(new ConstUnitProvider(aUnit), aFont, false);
	}

	public GLabel(UnitProvider aUnitProvider, Font aFont)
	{
		this(aUnitProvider, aFont, false);
	}

	public GLabel(UnitProvider aUnitProvider, Font aFont, boolean aShowLabel)
	{
		refUnitProvider = aUnitProvider;
		if (refUnitProvider != null)
			refUnitProvider.addListener(this);
		refValue = null;

		if (aFont != null)
			setFont(aFont);

		showLabel = aShowLabel;

		setMinimumSize(new Dimension(0, 0));
	}

	@Override
	public void unitChanged(UnitProvider aProvider, String aKey)
	{
		setValue(refValue);
	}

	/**
	 * Method to set in the value which will be formatted with the associated unit.
	 */
	public void setValue(Object aValue)
	{
		String aStr;
		Unit aUnit;

		refValue = aValue;

		aUnit = null;
		if (refUnitProvider != null)
			aUnit = refUnitProvider.getUnit();

		if (aUnit == null)
			aStr = "" + aValue;
		else if (showLabel == false)
			aStr = aUnit.getString(aValue);
		else
			aStr = aUnit.getString(aValue, true);

		setText(aStr);
	}

}
