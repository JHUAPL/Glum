package glum.gui.panel.itemList.config;

import glum.gui.panel.generic.TextInputPanel;

import java.awt.*;
import java.util.*;
import com.google.common.collect.Sets;

public class AddProfilePanel extends TextInputPanel
{
	// Constants
	public static final String DEFAULT_NAME = "Default";

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
		else if (inputStr.equals(DEFAULT_NAME) == true)
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
