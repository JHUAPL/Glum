package glum.logic;

import glum.io.token.MatchTokenizer;
import glum.reflect.ReflectUtil;
import glum.registry.Registry;

import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.*;

import javax.swing.*;

public class LogicChunkEngine implements ActionListener
{
	// State vars
	private Registry refRegistry;
	private JMenuBar menuBar;
	private Map<Object, LogicChunk> menuMap;
	private ShutdownHook shutdownHook;

	public LogicChunkEngine(Registry aRegistry, URL aURL, String aAppName)
	{
		boolean isHeadless;

		refRegistry = aRegistry;
		menuMap = null;
		menuBar = null;

		// Are we headless
		isHeadless = GraphicsEnvironment.isHeadless();

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
			catch(Exception aExp)
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
		LogicChunk aLogicChunk;

		// Insanity check
		if (aEvent == null || menuMap == null)
			return;

		// Find the associated LogicChunk
		aLogicChunk = (LogicChunk)menuMap.get(aEvent.getSource());

		// Activate the MenuItem
		if (aLogicChunk != null)
			aLogicChunk.activate();
	}

	/**
	 * Helper method to load up the LogicChunk described at aURL
	 */
	private void loadLogicChunks(URL aURL, boolean isHeadless)
	{
		InputStream inStream;
		BufferedReader br;
		MatchTokenizer aTokenizer;
		String regEx;
		ArrayList<String> tokenList;
		String[] tokens;
		int numTokens;
		LinkedList<JMenu> currMenuList;
		JMenuBar aMenuBar;
		JMenu currMenu;
		JMenuItem aMenuItem;
		String strLine;
		String aLoc;
		LogicChunk logicChunk;

		aLoc = aURL.toString();

		currMenuList = new LinkedList<JMenu>();
		aMenuItem = null;
		menuMap = new LinkedHashMap<Object, LogicChunk>();

		// Build our tokenizer
		regEx = "(#.*)|([a-zA-Z0-9\\.]+)|(\"[^\"\\r\\n]*\")|([a-zA-Z0-9\\.]+)";
		aTokenizer = new MatchTokenizer(regEx);

		// Create the MenuBar only if we are not headless
		aMenuBar = null;
		if (isHeadless == false)
			aMenuBar = new JMenuBar();

		// Process our input
		try
		{
			inStream = aURL.openStream();
			br = new BufferedReader(new InputStreamReader(inStream));

			// Read the lines
			while (true)
			{
				strLine = br.readLine();
				if (strLine == null)
				{
					br.close();
					inStream.close();
					break;
				}

				// Get the tokens out of strLine
				tokenList = aTokenizer.getTokens(strLine);

				tokens = tokenList.toArray(new String[0]);
				numTokens = tokens.length;

				// Process the tokens
				if (numTokens == 0)
				{
					; // Empty line
				}
				else if ((isHeadless == true) && (tokens[0].equals("Menu") == true || tokens[0].equals("MenuItem") == true || tokens[0].equals("SubMenu") || tokens[0].equals("EndSubMenu")))
				{
					System.out.println("Ignoring:" + tokens[0] + " command. Running in headless environment.");
					System.out.println("\tTokens: " + tokens);
				}
				else if (tokens[0].equals("Menu") == true && numTokens == 2)
				{
					JMenu aMenu;

					// Create a new menu
					aMenu = new JMenu(tokens[1]);
					aMenuBar.add(aMenu);

					// Reset the menu list
					currMenuList.clear();
					currMenuList.addLast(aMenu);
				}
				else if (tokens[0].equals("SubMenu") == true && numTokens == 2)
				{
					JMenu aMenu;

					// Get the current menu
					if (currMenuList.isEmpty() == true)
						currMenu = null;
					else
						currMenu = currMenuList.getLast();

					if (currMenu == null)
					{
						System.out.println("[" + aLoc + "]: Warning no parent menu found for the SubMenu.\n");
					}
					else
					{
						// Create and add into the current working menu
						aMenu = new JMenu(tokens[1]);
						currMenu.add(aMenu);

						currMenuList.addLast(aMenu);
					}
				}
				else if (tokens[0].equals("EndSubMenu") == true && numTokens == 1)
				{
					if (currMenuList.isEmpty() == true)
						System.out.println("[" + aLoc + "]: Warning no parent sub menu found. Instr: EndSubMenu\n");
					else
						currMenuList.removeLast();
				}

				// Process the various types of MenuItems
				else if (tokens[0].equals("AutoItem") == true && numTokens == 3)
				{
					// Build the auto item and add it only into our MenuMap
					logicChunk = loadLogicChunkInstance(refRegistry, tokens[2], tokens[1], aLoc);
					if (logicChunk != null)
					{
						menuMap.put(logicChunk, logicChunk);
					}
				}
				else if (tokens[0].equals("MenuItem") == true && (numTokens == 2 || numTokens == 3))
				{
					// Get the current menu
					if (currMenuList.isEmpty() == true)
						currMenu = null;
					else
						currMenu = currMenuList.getLast();

					if (currMenu == null)
					{
						System.out.println("[" + aLoc + "]: Warning no parent sub menu found. Instr: MenuItem.\n");
					}
					else if (tokens[1].equals("Break") == true)
					{
						currMenu.addSeparator();
					}
					else
					{
						// Build the menu item
						if (numTokens == 2)
						{
							aMenuItem = new JMenuItem(tokens[1]);
							aMenuItem.setEnabled(false);
						}
						else
						{
							// Try to build the LogicChunk and load it into our MenuMap
							logicChunk = loadLogicChunkInstance(refRegistry, tokens[2], tokens[1], aLoc);

							// Form the MenuItem or Menu
							if (logicChunk instanceof SubMenuChunk)
								aMenuItem = new JMenu(tokens[1]);
							else
								aMenuItem = new JMenuItem(tokens[1]);
							aMenuItem.addActionListener(this);

							// Associate the MenuItem with the LogicChunk
							if (logicChunk != null)
								menuMap.put(aMenuItem, logicChunk);

							// Notify MenuItemChunk/SubMenuChunk of the associated menu item
							if (logicChunk instanceof MenuItemChunk)
								((MenuItemChunk)logicChunk).setMenuItem(aMenuItem);
							if (logicChunk instanceof SubMenuChunk)
								((SubMenuChunk)logicChunk).setMenu((JMenu)aMenuItem);
						}
						currMenu.add(aMenuItem);
					}
				}
				else
				{
					System.out.println("Unreconized line. Instruction: -" + tokens[0] + "-");
					System.out.println("\tTokens: " + tokens);
				}
			}

		}
		catch(FileNotFoundException e)
		{
			System.out.println("File not found: " + aLoc);
			return;
		}
		catch(IOException e)
		{
			System.out.println("Ioexception occured in: LogicChunkEngine.loadLogicChunks()");
			return;
		}

		menuBar = aMenuBar;
	}

	/**
	 * Attempts to load up the logicChunk with the specified aLabel. If that fails then will attempt to construct the
	 * LogicChunk using the default constructor.
	 */
	private static LogicChunk loadLogicChunkInstance(Registry aRegistry, String aFullClassPath, String aLabel, String aLoc)
	{
		Class<?> rawClass;
		Constructor<?> rawConstructor;
		Class<?> parmTypes1[] = {Registry.class, String.class};
		Class<?> parmTypes2[] = {String.class};
		Object parmValues[];

		// Insanity check
		if (aFullClassPath == null)
			return null;

		try
		{
			// Retrieve the class and ensure it is a LogicChunk
			rawClass = Class.forName(aFullClassPath);
			if (LogicChunk.class.isAssignableFrom(rawClass) == false)
			{
				System.out.println("Failure: " + aFullClassPath + " is not a LogicChunk!");
				System.out.println("\tLocation: " + aLoc + "\n");
				return null;
			}

			// Try the 1st preferred constructor
			rawConstructor = ReflectUtil.getConstructorSafe(rawClass, parmTypes1);
			if (rawConstructor != null)
			{
				parmValues = new Object[2];
				parmValues[0] = aRegistry;
				parmValues[1] = aLabel;

				return (LogicChunk)rawConstructor.newInstance(parmValues);
			}

			// Try the 2nd preferred constructor
			rawConstructor = ReflectUtil.getConstructorSafe(rawClass, parmTypes2);
			if (rawConstructor != null)
			{
				parmValues = new Object[1];
				parmValues[0] = aLabel;

				return (LogicChunk)rawConstructor.newInstance(parmValues);
			}

			// Just use the default constructor
			else
			{
				return (LogicChunk)rawClass.getDeclaredConstructor().newInstance();
			}
		}
		catch(ClassNotFoundException aExp)
		{
			System.out.println("Failure: " + aFullClassPath + " not found.");
			System.out.println("\tLocation: " + aLoc + "\n");
		}
		catch(Exception aExp)
		{
			// Unknown Exception
			aExp.printStackTrace();
		}

		return null;
	}

}
