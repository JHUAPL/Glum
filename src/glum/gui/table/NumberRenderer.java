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
import java.text.DecimalFormat;
import java.text.NumberFormat;

import javax.swing.JTable;
import javax.swing.table.DefaultTableCellRenderer;

/**
 * TableCellRenderer used to display numerical values.
 *
 * @author lopeznr1
 */
public class NumberRenderer extends DefaultTableCellRenderer
{
	// Constants
	private static final long serialVersionUID = 1L;

	// Attributes
	private final NumberFormat numberFormat;
	private final String nanStr;

	/**
	 * Standard Constructor
	 *
	 * @param aPrependLabel:
	 *        String that will be prepended to the table cell's value.
	 */
	public NumberRenderer(NumberFormat aNumberFormat, String aNanStr)
	{
		numberFormat = aNumberFormat;
		nanStr = aNanStr;
	}

	/**
	 * Simplified Constructor
	 *
	 * @param aNumberFmtStr
	 *        The number string format to be sent to a DecimalFormat.
	 * @param aNanStr
	 *        The String to be used as NaN.
	 */
	public NumberRenderer(String aNumberFmtStr, String aNanStr)
	{
		this(new DecimalFormat(aNumberFmtStr), aNanStr);
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus,
			int row, int column)
	{
		String tmpStr = nanStr;
		if (value instanceof Number)
		{
			double tmpVal = ((Number) value).doubleValue();
			if (Double.isNaN(tmpVal) == false)
				tmpStr = numberFormat.format(value);
		}

		return super.getTableCellRendererComponent(table, tmpStr, isSelected, hasFocus, row, column);
	}

}
