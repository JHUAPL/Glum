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
package glum.gui.dock;

import java.awt.Component;
import java.awt.event.ActionEvent;

import bibliothek.gui.DockFrontend;
import glum.gui.panel.generic.BaseTextInputPanel;

/**
 * UI component that allows the creation of a docking configuration.
 *
 * @author lopeznr1
 */
public class FrontendAddConfigPanel extends BaseTextInputPanel
{
	// Constants
	public static final String DEFAULT_NAME = "Default";

	// Ref vars
	private final DockFrontend refFrontend;

	/** Standard Constructor */
	public FrontendAddConfigPanel(Component aParent, DockFrontend aFrontend)
	{
		super(aParent);

		refFrontend = aFrontend;

		// Set in a more specific title and input label
		titleL.setText("Save configuration as:");
		inputL.setText("Name:");
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		// Save of the configuration
		var source = e.getSource();
		if (source == acceptB)
		{
			var configName = getInput();
			refFrontend.save(configName);
		}

		// Call parent behavior
		super.actionPerformed(e);
	}

	@Override
	protected void updateGui()
	{
		// Assume the GUI is invalid
		var isEnabled = false;

		// Retrieve the name
		var inputStr = inputTF.getText();

		// Determine the validity of specified name
		String infoMsg;
		if (inputStr.equals("") == true)
		{
			infoMsg = "Please enter a valid configuration name.";
		}
		else if (inputStr.startsWith(".") == true)
		{
			infoMsg = "Invalid name. Name can not start with the char '.'";
		}
		else if (refFrontend.getSetting(inputStr) != null)
		{
			infoMsg = "Name in use. Configuration will be overwritten.";
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
