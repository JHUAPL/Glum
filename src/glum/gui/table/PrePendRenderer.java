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

import java.awt.Component;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * TableCellRenderer used to display values prepended with a text string.
 *
 * @author lopeznr1
 */
public class PrePendRenderer extends DefaultTableCellRenderer
{
	// Constants
	private static final long serialVersionUID = 1L;

	// Attributes
	private final String prependLabel;

	/**
	 * Standard Constructor
	 *
	 * @param aPrependLabel:
	 *        String that will be prepended to the table cell's value.
	 */
	public PrePendRenderer(String aPrependLabel)
	{
		prependLabel = aPrependLabel;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column)
	{
		String tmpStr = prependLabel + value;
		return super.getTableCellRendererComponent(table, tmpStr, isSelected, hasFocus, row, column);
	}

}
