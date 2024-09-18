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
package glum.gui.panel.itemList.config;

import glum.gui.panel.generic.BaseTextInputPanel;

import java.awt.*;
import java.util.*;
import com.google.common.collect.Sets;

public class AddProfilePanel extends BaseTextInputPanel
{
	// State vars
	protected Set<String> reservedSet;

	public AddProfilePanel(Component aParent)
	{
		super(aParent);
		
		// Set in a more specific title and input label
		titleL.setText("Save Profile As");
		inputL.setText("Name:");
		
		reservedSet = Sets.newHashSet();
	}
	
	/**
	 * Sets in all of the names currently used. 
	 */
	public void setReservedNames(Collection<String> aReservedSet)
	{
		reservedSet.clear();
		reservedSet.addAll(aReservedSet);
	}

	@Override
	protected void updateGui()
	{
		String inputStr, infoMsg;
		boolean isEnabled;

		// Assume the GUI is invalid
		isEnabled = false;

		// Retrieve the name
		inputStr = inputTF.getText();

		// Determine the validity of specified name
		if (inputStr.equals("") == true)
		{
			infoMsg = "Please enter a valid profile name.";
		}
		else if (inputStr.equals(ProfileConfig.DEFAULT_NAME) == true)
		{
			infoMsg = "Name is reserved. Please pick another.";
		}
		else if (reservedSet.contains(inputStr) == true)
		{
			infoMsg = "Name in use. Profile will be overwritten.";
			isEnabled = true;
		}
		else
		{
			infoMsg = "";
			isEnabled = true;
		}

		infoL.setText(infoMsg);
		acceptB.setEnabled(isEnabled);
	}

}
