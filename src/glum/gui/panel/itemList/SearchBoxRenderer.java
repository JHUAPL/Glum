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
package glum.gui.panel.itemList;

import java.awt.Component;

import javax.swing.*;
import javax.swing.table.TableColumn;

public class SearchBoxRenderer extends DefaultListCellRenderer
{
	/*** Standard Constructor */
	public SearchBoxRenderer()
	{
		; // Nothing to do
	}

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object aObj, int index, boolean isSelected,
			boolean hasFocus)
	{
		var retL = (JLabel) super.getListCellRendererComponent(list, aObj, index, isSelected, hasFocus);
		if (aObj instanceof TableColumn)
		{
			var tmpStr = "" + ((TableColumn) aObj).getHeaderValue();
			if (tmpStr.equals("null") == true || tmpStr.equals("") == true)
				tmpStr = "" + ((TableColumn) aObj).getIdentifier();

			retL.setText(tmpStr);
		}

		return retL;
	}

}
