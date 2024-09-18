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
import javax.swing.JTable;
import javax.swing.table.TableCellRenderer;

public class MultiStateCheckBoxCellRenderer extends MultiStateCheckBox implements TableCellRenderer
{
	// State vars
	protected MultiStateCheckBox refMultiStateCheckBox;

	/**
	 * Constructor
	 */
	public MultiStateCheckBoxCellRenderer()
	{
		refMultiStateCheckBox = new MultiStateCheckBox("", false);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column)
	{
		if (value instanceof MultiState)
		{
			refMultiStateCheckBox.setState((MultiState)value);
			return refMultiStateCheckBox;
		}
		else if (value instanceof Boolean)
		{
			if ((Boolean)value == true)
				refMultiStateCheckBox.setState(MultiState.Checked);
			else
				refMultiStateCheckBox.setState(MultiState.None);
			return refMultiStateCheckBox;
		}

		return null;
	}

}
