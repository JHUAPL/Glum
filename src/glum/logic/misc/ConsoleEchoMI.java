package glum.logic.misc;

import javax.swing.JMenuItem;

import glum.logic.LogicChunk;
import glum.logic.MenuItemChunk;
import glum.registry.Registry;

/**
 * LogicChunk used to echo a message to the console.
 */
public class ConsoleEchoMI implements LogicChunk, MenuItemChunk
{
	private String message;
	private String title;

	public ConsoleEchoMI(Registry aRegistry, String aLabel)
	{
		String[] strArr;

		title = aLabel;
		message = "No message specified.";

		// Customize the message and MenuItem title
		strArr = aLabel.split(":");
		if (strArr.length == 2)
		{
			title = strArr[0];
			message = strArr[1];
		}
	}

	@Override
	public void activate()
	{
		System.out.println(message);
	}

	@Override
	public void dispose()
	{
		; // Nothing to do
	}

	@Override
	public String getName()
	{
		return "Console Echo MI";
	}

	@Override
	public String getVersion()
	{
		return "0.1";
	}

	@Override
	public void setMenuItem(JMenuItem aMI)
	{
		aMI.setText(title);
	}

}
