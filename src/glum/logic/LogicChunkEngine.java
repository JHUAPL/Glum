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
package glum.logic;

import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.URL;
import java.util.*;

import javax.swing.*;

import glum.io.token.MatchTokenizer;
import glum.reflect.ReflectUtil;
import glum.registry.Registry;

/**
 * Mechanism for loading {@link LogicChunk}s into an application.
 *
 * @author lopeznr1
 */
public class LogicChunkEngine implements ActionListener
{
	// Ref vars
	private final Registry refRegistry;

	// State vars
	private JMenuBar menuBar;
	private Map<Object, LogicChunk> menuMap;
	private ShutdownHook shutdownHook;

	/** Standard Constructor */
	public LogicChunkEngine(Registry aRegistry, URL aURL, String aAppName)
	{
		refRegistry = aRegistry;
		menuMap = null;
		menuBar = null;

		// Are we headless
		var isHeadless = GraphicsEnvironment.isHeadless();

		// Install our custom shutdown logic
		shutdownHook = new ShutdownHook(this, aAppName);
		Runtime.getRuntime().addShutdownHook(shutdownHook);

		// Load up the LogicChunks
		loadLogicChunks(aURL, isHeadless);
	}

	/**
	 * Notifies all of the LogicChunks to perform the dispose operation.
	 */
	public void dispose()
	{
		for (LogicChunk aLogicChunk : menuMap.values())
		{
			try
			{
				if (aLogicChunk != null)
					aLogicChunk.dispose();
			}
			catch (Exception aExp)
			{
				System.out.println("Failed to dispose LogicChunk. Exception:");
				aExp.printStackTrace();
			}
		}

		menuBar = null;
		menuMap = null;
	}

	/**
	 * Returns the MenuBar associated with the LogicChunkEngine
	 */
	public JMenuBar getMenuBar()
	{
		return menuBar;
	}

	/**
	 * Returns all of the LogicChunks in this engine
	 */
	public Collection<LogicChunk> getLogicChunks()
	{
		return menuMap.values();
	}

	/**
	 * Configures the shutdown hook to exit quickly by not waiting for daemon threads.
	 */
	public void setQuickExit(boolean aBool)
	{
		shutdownHook.setQuickExit(aBool);
	}

	@Override
	public void actionPerformed(ActionEvent aEvent)
	{
		// Insanity check
		if (aEvent == null || menuMap == null)
			return;

		// Find the associated LogicChunk
		var tmpLogicChunk = menuMap.get(aEvent.getSource());

		// Activate the MenuItem
		if (tmpLogicChunk != null)
			tmpLogicChunk.activate();
	}

	/**
	 * Helper method to load up the LogicChunk described at aURL
	 */
	private void loadLogicChunks(URL aURL, boolean aIsHeadless)
	{
		var aLoc = aURL.toString();

		var currMenuL = new LinkedList<JMenu>();
		menuMap = new LinkedHashMap<Object, LogicChunk>();

		// Build our tokenizer
		var regEx = "(#.*)|([a-zA-Z0-9\\.]+)|(\"[^\"\\r\\n]*\")|([a-zA-Z0-9\\.]+)";
		var tmpTokenizer = new MatchTokenizer(regEx);

		// Create the MenuBar only if we are not headless
		var tmpMenuBar = (JMenuBar) null;
		if (aIsHeadless == false)
			tmpMenuBar = new JMenuBar();

		// Process our input
		JMenu currMenu;
		try (var br = new BufferedReader(new InputStreamReader(aURL.openStream())))
		{
			// Read the lines
			while (true)
			{
				var strLine = br.readLine();
				if (strLine == null)
				{
					br.close();
					break;
				}

				// Get the tokens out of strLine
				var tokenL = tmpTokenizer.getTokens(strLine);

				var tokenArr = tokenL.toArray(new String[0]);
				var numTokens = tokenArr.length;

				// Skip to the next line if the line is empty
				if (numTokens == 0)
					continue;

				// Process the tokens
				if ((aIsHeadless == true) && (tokenArr[0].equals("Menu") == true || tokenArr[0].equals("MenuItem") == true
						|| tokenArr[0].equals("SubMenu") || tokenArr[0].equals("EndSubMenu")))
				{
					System.out.println("Ignoring:" + tokenArr[0] + " command. Running in headless environment.");
					System.out.println("\tTokens: " + tokenArr);
				}
				else if (tokenArr[0].equals("Menu") == true && numTokens == 2)
				{
					// Create a new menu
					var tmpMenu = new JMenu(tokenArr[1]);
					tmpMenuBar.add(tmpMenu);

					// Reset the menu list
					currMenuL.clear();
					currMenuL.addLast(tmpMenu);
				}
				else if (tokenArr[0].equals("SubMenu") == true && numTokens == 2)
				{
					// Get the current menu
					if (currMenuL.isEmpty() == true)
						currMenu = null;
					else
						currMenu = currMenuL.getLast();

					if (currMenu == null)
					{
						System.out.println("[" + aLoc + "]: Warning no parent menu found for the SubMenu.\n");
					}
					else
					{
						// Create and add into the current working menu
						var tmpMenu = new JMenu(tokenArr[1]);
						currMenu.add(tmpMenu);

						currMenuL.addLast(tmpMenu);
					}
				}
				else if (tokenArr[0].equals("EndSubMenu") == true && numTokens == 1)
				{
					if (currMenuL.isEmpty() == true)
						System.out.println("[" + aLoc + "]: Warning no parent sub menu found. Instr: EndSubMenu\n");
					else
						currMenuL.removeLast();
				}

				// Process the various types of MenuItems
				else if (tokenArr[0].equals("AutoItem") == true && numTokens == 3)
				{
					// Build the auto item and add it only into our MenuMap
					var logicChunk = loadLogicChunkInstance(refRegistry, tokenArr[2], tokenArr[1], aLoc);
					if (logicChunk != null)
						menuMap.put(logicChunk, logicChunk);
				}
				else if (tokenArr[0].equals("MenuItem") == true && (numTokens == 2 || numTokens == 3))
				{
					// Get the current menu
					if (currMenuL.isEmpty() == true)
						currMenu = null;
					else
						currMenu = currMenuL.getLast();

					if (currMenu == null)
					{
						System.out.println("[" + aLoc + "]: Warning no parent sub menu found. Instr: MenuItem.\n");
					}
					else if (tokenArr[1].equals("Break") == true)
					{
						currMenu.addSeparator();
					}
					else
					{
						JMenuItem tmpMenuItem;

						// Build the menu item
						if (numTokens == 2)
						{
							tmpMenuItem = new JMenuItem(tokenArr[1]);
							tmpMenuItem.setEnabled(false);
						}
						else
						{
							// Try to build the LogicChunk and load it into our MenuMap
							var logicChunk = loadLogicChunkInstance(refRegistry, tokenArr[2], tokenArr[1], aLoc);

							// Form the MenuItem or Menu
							if (logicChunk instanceof SubMenuChunk)
								tmpMenuItem = new JMenu(tokenArr[1]);
							else
								tmpMenuItem = new JMenuItem(tokenArr[1]);
							tmpMenuItem.addActionListener(this);

							// Associate the MenuItem with the LogicChunk
							if (logicChunk != null)
								menuMap.put(tmpMenuItem, logicChunk);

							// Notify MenuItemChunk/SubMenuChunk of the associated menu item
							if (logicChunk instanceof MenuItemChunk)
								((MenuItemChunk) logicChunk).setMenuItem(tmpMenuItem);
							if (logicChunk instanceof SubMenuChunk)
								((SubMenuChunk) logicChunk).setMenu((JMenu) tmpMenuItem);
						}
						currMenu.add(tmpMenuItem);
					}
				}
				else
				{
					System.err.println("Unreconized line. Instruction: -" + tokenArr[0] + "-");
					System.err.println("\tTokens: " + tokenArr);
				}
			}

		}
		catch (FileNotFoundException aExp)
		{
			System.err.println("File not found: " + aLoc);
			return;
		}
		catch (IOException aExp)
		{
			System.err.println("Ioexception occured in: LogicChunkEngine.loadLogicChunks()");
			return;
		}

		menuBar = tmpMenuBar;
	}

	/**
	 * Attempts to load up the logicChunk with the specified aLabel. If that fails then will attempt to construct the
	 * LogicChunk using the default constructor.
	 */
	private static LogicChunk loadLogicChunkInstance(Registry aRegistry, String aFullClassPath, String aLabel,
			String aLoc)
	{
		// Insanity check
		if (aFullClassPath == null)
			return null;

		try
		{
			// Retrieve the class and ensure it is a LogicChunk
			var rawClass = Class.forName(aFullClassPath);
			if (LogicChunk.class.isAssignableFrom(rawClass) == false)
			{
				System.out.println("Failure: " + aFullClassPath + " is not a LogicChunk!");
				System.out.println("\tLocation: " + aLoc + "\n");
				return null;
			}

			// Try the 1st preferred constructor
			Class<?> parmTypes1[] = { Registry.class, String.class };
			var rawConstructor = ReflectUtil.getConstructorSafe(rawClass, parmTypes1);
			if (rawConstructor != null)
			{
				var parmValues = new Object[2];
				parmValues[0] = aRegistry;
				parmValues[1] = aLabel;

				return (LogicChunk) rawConstructor.newInstance(parmValues);
			}

			// Try the 2nd preferred constructor
			Class<?> parmTypes2[] = { String.class };
			rawConstructor = ReflectUtil.getConstructorSafe(rawClass, parmTypes2);
			if (rawConstructor != null)
			{
				var parmValues = new Object[1];
				parmValues[0] = aLabel;

				return (LogicChunk) rawConstructor.newInstance(parmValues);
			}

			// Just use the default constructor
			else
			{
				return (LogicChunk) rawClass.getDeclaredConstructor().newInstance();
			}
		}
		catch (ClassNotFoundException aExp)
		{
			System.err.println("Failure: " + aFullClassPath + " not found.");
			System.err.println("\tLocation: " + aLoc + "\n");
		}
		catch (Exception aExp)
		{
			// Unknown Exception
			aExp.printStackTrace();
		}

		return null;
	}

}
