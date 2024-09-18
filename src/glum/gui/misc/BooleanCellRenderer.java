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

import java.awt.Component;

import javax.swing.JCheckBox;
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

/**
 * {@link TableCellRenderer} used to render boolean values.
 *
 * @author lopeznr1
 */
public class BooleanCellRenderer extends JCheckBox implements TableCellRenderer
{
	/** Standard Constructor */
	public BooleanCellRenderer()
	{
		super("", false);
		setHorizontalAlignment(JCheckBox.CENTER);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column)
	{
		if (value instanceof Boolean)
		{
			setSelected((Boolean) value);
			return this;
		}

		return null;
	}

}
