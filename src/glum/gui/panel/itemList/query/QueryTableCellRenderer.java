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
package glum.gui.panel.itemList.query;

import java.awt.Component;

import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

import glum.unit.Unit;

/**
 * TableCellRenderer used to render a QueryAttribute with the appropriate unit.
 *
 * @author lopeznr1
 */
public class QueryTableCellRenderer extends DefaultTableCellRenderer
{
	// State vars
	private Unit myUnit;

	public QueryTableCellRenderer(QueryAttribute<?> aAttribute)
	{
		myUnit = null;
		if (aAttribute != null)
		{
			myUnit = aAttribute.refUnitProvider.getUnit();
			setHorizontalAlignment(aAttribute.alignment);
		}
	}

	public QueryTableCellRenderer()
	{
		this(null);
	}

	/**
	 * Sets in the Unit used to retrieve the actual textual values of the table cell data
	 */
	public void setUnit(Unit aUnit)
	{
		myUnit = aUnit;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column)
	{
		// No special processing is needed if no unit specified
		var retLabel = (JLabel) super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
		if (myUnit == null)
			return retLabel;

		retLabel.setText(myUnit.getString(value));
		return retLabel;
	}

}
