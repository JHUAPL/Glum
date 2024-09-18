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
import java.util.HashMap;
import java.util.Map;

import javax.swing.*;

/**
 * {@link ListCellRenderer} used to render custom labels corresponding to the provided items.
 * <p>
 * If an item is not registered via {@link #addMapping(Object, String)}, then the string returned by
 * {@link Object#toString()} will be utilized instead.
 *
 * @author lopeznr1
 */
public class CustomLCR extends DefaultListCellRenderer
{
	// State vars
	private final Map<Object, String> labelM;

	/** Standard Constructor */
	public CustomLCR()
	{
		labelM = new HashMap<>();
	}

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object aObj, int index, boolean isSelected,
			boolean hasFocus)
	{
		JLabel retL = (JLabel) super.getListCellRendererComponent(list, aObj, index, isSelected, hasFocus);

		String tmpLabel = labelM.get(aObj);
		if (tmpLabel == null)
			tmpLabel = "" + aObj;

		retL.setText(tmpLabel);
		return retL;
	}

	/**
	 * Registers the label that will be shown when the specified item is selected.
	 */
	public void addMapping(Object aItem, String aLabel)
	{
		labelM.put(aItem, aLabel);
	}

	/**
	 * Deregisters the label that will be shown when the specified item is selected.
	 */
	public void delMapping(Object aItem)
	{
		labelM.remove(aItem);
	}

	/**
	 * Returns the label associated with the specified mapping.
	 */
	public String getLabel(Object aItem)
	{
		return labelM.get(aItem);
	}

}
