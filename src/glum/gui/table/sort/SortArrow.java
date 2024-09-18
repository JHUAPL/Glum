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
package glum.gui.table.sort;

import java.awt.*;

import javax.swing.Icon;

/**
 * Icon used to render the sort indicator.
 * 
 * @author lopeznr1
 */
public class SortArrow implements Icon
{
	// Attributes
	private final boolean descending;
	private final int size;
	private final Color fillColor;

	/**
	 * Standard Constructor
	 * 
	 * @param aDescending
	 * @param aSize
	 * @param aPriority
	 */
	public SortArrow(boolean aDescending, int aSize, Color aColor)
	{
		descending = aDescending;
		size = aSize;
		fillColor = aColor;
	}

	@Override
	public void paintIcon(Component aComp, Graphics g, int x, int y)
	{
		int dx = (int) (size / 2.0);
		int dy = descending ? dx : -dx;

		// Align icon (roughly) with font baseline.
		y = y + 5 * size / 6 + (descending ? -dy : 0);
		int shift = descending ? 1 : -1;
		g.translate(x, y);

		int[] xArr = { dx / 2, 0, dx };
		int[] yArr = { dy + shift, shift, 0 };

		// Draw the solid area
		g.setColor(fillColor);
		g.fillPolygon(xArr, yArr, 3);

		// Draw the border area
		Color tmpColor = aComp == null ? Color.GRAY : aComp.getBackground();

		// Right diagonal.
		g.setColor(tmpColor.darker());
		g.drawLine(dx / 2, dy, 0, 0);
		g.drawLine(dx / 2, dy + shift, 0, shift);

		// Left diagonal.
		g.setColor(tmpColor.brighter());
		g.drawLine(dx / 2, dy, dx, 0);
		g.drawLine(dx / 2, dy + shift, dx, shift);

		// Horizontal line.
		if (descending)
			g.setColor(tmpColor.darker().darker());
		else
			g.setColor(tmpColor.brighter().brighter());
		g.drawLine(dx, 0, 0, 0);

		g.setColor(tmpColor);
		g.translate(-x, -y);
	}

	@Override
	public int getIconWidth()
	{
		return size;
	}

	@Override
	public int getIconHeight()
	{
		return size;
	}

}
