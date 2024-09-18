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

import javax.swing.JPanel;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * {@link TableCellRenderer} used to render a color.
 *
 * @author lopeznr1
 */
public class ColorCellRenderer extends JPanel implements TableCellRenderer
{
	// State vars
	protected Color activeColor;

	/** Standard Constructor */
	public ColorCellRenderer()
	{
		activeColor = null;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column)
	{
		activeColor = null;
		if (value instanceof Color)
			activeColor = (Color) value;

		if (activeColor != null)
			setBackground(activeColor);
		else
			setBackground(Color.LIGHT_GRAY);

		return this;
	}

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);
		var g2d = (Graphics2D) g;

		// Bail if we have a valid color
		if (activeColor != null)
			return;

		// Draw a red x if no valid color
		g2d.setColor(Color.RED);
		g2d.drawLine(0, 0, getWidth(), getHeight());
		g2d.drawLine(getWidth(), 0, 0, getHeight());
	}

}
