package glum.gui.dock;

import java.awt.*;
import java.awt.event.ActionEvent;

import bibliothek.gui.DockFrontend;

import glum.gui.panel.generic.TextInputPanel;

public class FrontendAddConfigPanel extends TextInputPanel
{
	// Constants
	public static final String DEFAULT_NAME = "Default";

	// State vars
	protected DockFrontend refFrontend;

	/**
	 * Constructor
	 */
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
		Object source;
		String configName;

		// Save of the configuration
		source = e.getSource();
		if (source == acceptB)
		{
			configName = getInput();
			refFrontend.save(configName);
		}

		// Call parent behavior
		super.actionPerformed(e);
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
