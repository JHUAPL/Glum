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
package glum.gui.misc;

import java.awt.*;
import javax.swing.*;
import javax.swing.text.*;
import javax.swing.border.*;

public class SimpleTitledBorder extends TitledBorder
{
	// Class var used to strip the disabled color
	private static JTextComponent testComponent = null;

	/**
	 * Constuctor
	 */
	public SimpleTitledBorder(Border border)
	{
		super(border);
	}

	public SimpleTitledBorder(Border border, String title)
	{
		super(border, title);
	}

	public SimpleTitledBorder(Border border, String title, int titleJustification, int titlePosition)
	{
		super(border, title, titleJustification, titlePosition);
	}

	public SimpleTitledBorder(Border border, String title, int titleJustification, int titlePosition, Font titleFont)
	{
		super(border, title, titleJustification, titlePosition, titleFont);
	}

	public SimpleTitledBorder(Border border, String title, int titleJustification, int titlePosition, Font titleFont, Color titleColor)
	{
		super(border, title, titleJustification, titlePosition, titleFont, titleColor);
	}

	public SimpleTitledBorder(String title)
	{
		super(title);
	}

	/**
	 * setEnabled
	 */
	public void setEnabled(boolean aBool)
	{
		if (testComponent == null)
			testComponent = new JTextField();

		// ! TODO: Color should be based on system settings
		setTitleColor(Color.BLACK);
		if (aBool == false)
			setTitleColor(testComponent.getDisabledTextColor());
	}

}
