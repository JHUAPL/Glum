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
import javax.swing.plaf.metal.*;

public class MultiStateIcon implements Icon
{
	@Override
	public int getIconWidth()
	{
		return getIconSize();
	}

	@Override
	public int getIconHeight()
	{
		return getIconSize();
	}

	@Override
	public void paintIcon(Component c, Graphics g, int x, int y)
	{
		MultiStateModel model;
		MultiState aState;
		int iconSize;

		// Insanity check
		if (c instanceof MultiStateCheckBox == false)
			return;

		model = (MultiStateModel)((JCheckBox)c).getModel();
		iconSize = getIconSize();

		if (model.isEnabled())
		{
			if (model.isPressed() && model.isArmed())
			{
				g.setColor(MetalLookAndFeel.getControlShadow());
				g.fillRect(x, y, iconSize - 1, iconSize - 1);
				drawPressed3DBorder(g, x, y, iconSize, iconSize);
			}
			else
			{
				if (model.isArmed() == true)
				{
					g.setColor(MetalLookAndFeel.getControlShadow());
					drawPressed3DBorder(g, x, y, iconSize, iconSize);
				}
				else
				{
					drawFlush3DBorder(g, x, y, iconSize, iconSize);
				}
			}

			g.setColor(MetalLookAndFeel.getControlInfo());
		}
		else
		{
			g.setColor(MetalLookAndFeel.getControlShadow());
			g.drawRect(x, y, iconSize - 1, iconSize - 1);
		}

		// Render the appropriate symbol
		aState = model.getState();
		aState.render(g, x, y, iconSize);
	}

	/**
	 * Utility method
	 */
	protected void drawFlush3DBorder(Graphics g, int x, int y, int w, int h)
	{
		g.translate(x, y);
		g.setColor(MetalLookAndFeel.getControlDarkShadow());
		g.drawRect(0, 0, w - 2, h - 2);
		g.setColor(MetalLookAndFeel.getControlHighlight());
		g.drawRect(1, 1, w - 2, h - 2);
		g.setColor(MetalLookAndFeel.getControl());
		g.drawLine(0, h - 1, 1, h - 2);
		g.drawLine(w - 1, 0, w - 2, 1);
		g.translate(-x, -y);
	}

	/**
	 * Utility method
	 */
	protected void drawPressed3DBorder(Graphics g, int x, int y, int w, int h)
	{
		g.translate(x, y);
		drawFlush3DBorder(g, 0, 0, w, h);
		g.setColor(MetalLookAndFeel.getControlShadow());
		g.drawLine(1, 1, 1, h - 2);
		g.drawLine(1, 1, w - 2, 1);
		g.drawLine(2, 1, 2, h - 2);
		g.drawLine(1, 2, w - 2, 2);

		g.drawLine(w - 2, 1, w - 2, h - 2);
		g.drawLine(1, h - 2, w - 2, h - 2);
		g.drawLine(w - 3, 1, w - 3, h - 2);
		g.drawLine(1, h - 3, w - 2, h - 3);

		g.translate(-x, -y);
	}

	/**
	 * Returns the square dimensions of this GUI
	 */
	protected int getIconSize()
	{
		return 13;
	}

}
