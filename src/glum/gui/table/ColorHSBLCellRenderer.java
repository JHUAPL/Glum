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
package glum.gui.table;

import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

import glum.color.ColorHSBL;

/**
 * Class that used to display a filled color rectangle for a table cell where the data model is a {@link Color}
 */
public class ColorHSBLCellRenderer extends JLabel implements TableCellRenderer
{
	// Constants
	private static final long serialVersionUID = 1L;

	// State vars
	private boolean showToolTips;

	// Cache vars
	private Border cUnselectedBorder = null;
	private Border cSelectedBorder = null;

	public ColorHSBLCellRenderer(boolean aShowToolTips)
	{
		showToolTips = aShowToolTips;

		setOpaque(true); // MUST do this for background to show up.
	}

	@Override
	public Component getTableCellRendererComponent(JTable aTable, Object aObj, boolean isSelected, boolean hasFocus,
			int row, int column)
	{
		Color tmpColor = ((ColorHSBL) aObj).getColor();
		setBackground(tmpColor);

		if (isSelected)
		{
			if (cSelectedBorder == null)
				cSelectedBorder = BorderFactory.createMatteBorder(2, 5, 2, 5, aTable.getSelectionBackground());
			setBorder(cSelectedBorder);
		}
		else
		{
			if (cUnselectedBorder == null)
				cUnselectedBorder = BorderFactory.createMatteBorder(2, 5, 2, 5, aTable.getBackground());
			setBorder(cUnselectedBorder);
		}

		String toolTipStr = null;
		if (showToolTips == true && tmpColor != null)
			toolTipStr = "RGB value: " + tmpColor.getRed() + ", " + tmpColor.getGreen() + ", " + tmpColor.getBlue();
		setToolTipText(toolTipStr);

		return this;
	}
}
