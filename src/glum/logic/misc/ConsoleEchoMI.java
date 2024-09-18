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
package glum.logic.misc;

import javax.swing.JMenuItem;

import glum.logic.LogicChunk;
import glum.logic.MenuItemChunk;
import glum.registry.Registry;

/**
 * {@link LogicChunk} used to echo a message to the console.
 *
 * @author lopeznr1
 */
public class ConsoleEchoMI implements LogicChunk, MenuItemChunk
{
	// Attributes
	private final String message;
	private final String title;

	/** Standard Constructor */
	public ConsoleEchoMI(Registry aRegistry, String aLabel)
	{
		// Customize the message and MenuItem title
		var strArr = aLabel.split(":");
		if (strArr.length == 2)
		{
			title = strArr[0];
			message = strArr[1];
		}
		else
		{
			title = aLabel;
			message = "No message specified.";
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
	public void setMenuItem(JMenuItem aMI)
	{
		aMI.setText(title);
	}

}
