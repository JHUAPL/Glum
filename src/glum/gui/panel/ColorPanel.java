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
package glum.gui.panel;

import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;
import javax.swing.border.BevelBorder;
import javax.swing.border.LineBorder;

public class ColorPanel extends JPanel
{
	// State vars
	private Color dispColor;

	public ColorPanel()
	{
		dispColor = Color.BLACK;
		updateGui();
	}

	public ColorPanel(int sizeX, int sizeY)
	{
		this();

		setMinimumSize(new Dimension(sizeX, sizeY));
		setPreferredSize(new Dimension(sizeX, sizeY));
	}

	/**
	 * Sets in the color that is displayed by this component
	 */
	public void setColor(Color aColor)
	{
		dispColor = aColor;
		updateGui();
	}

	@Override
	public void setEnabled(boolean aBool)
	{
		super.setEnabled(aBool);
		updateGui();
	}

	/**
	 * Updates the GUI to reflect the chosen color
	 */
	protected void updateGui()
	{
		var isEnabled = isEnabled();
		if (isEnabled == false)
		{
			setBackground(Color.LIGHT_GRAY);
			setBorder(new LineBorder(Color.GRAY));
			return;
		}

		setBackground(dispColor);
		setBorder(new BevelBorder(BevelBorder.RAISED));
	}

}
