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
package glum.gui.panel.generic;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.*;

import glum.gui.FocusUtil;
import glum.gui.GuiUtil;
import glum.gui.action.ClickAction;
import glum.gui.component.GTextField;
import glum.gui.panel.GlassPanel;
import glum.io.Loader;
import glum.io.LoaderInfo;
import glum.unit.ByteUnit;
import glum.zio.ZinStream;
import glum.zio.ZoutStream;
import net.miginfocom.swing.MigLayout;

/**
 * {@link GlassPanel} used to prompt the user for a location on the disk.
 * <p>
 * The title and (prompt) message can be customized.
 *
 * @author lopeznr1
 */
public class LocationPanel extends GlassPanel implements ActionListener, GenericCodes
{
	// Constants
	public static final Color ColorWarn = new Color(128, 0, 0);

	// GUI vars
	private JLabel titleL, locationL, infoL, warnL;
	private JButton cancelB, acceptB, fileB;
	private JTextArea instrTA;
	private GTextField locationTF;

	// State vars
	private LoaderInfo loaderInfo;
	private long minFreeSpace;
	private boolean isAccepted;

	/** Standard Constructor */
	public LocationPanel(Component aParent)
	{
		super(aParent);

		loaderInfo = new LoaderInfo();
		minFreeSpace = 0;
		isAccepted = false;

		// Build the actual GUI
		buildGuiArea();
		setPreferredSize(new Dimension(300, getPreferredSize().height));

		// Set up keyboard short cuts
		FocusUtil.addAncestorKeyBinding(this, "ESCAPE", new ClickAction(cancelB));
		FocusUtil.addAncestorKeyBinding(this, "ENTER", new ClickAction(acceptB));
		FocusUtil.addFocusKeyBinding(locationTF, "ENTER", new ClickAction(acceptB));
	}

	/**
	 * Returns the file as specified in the location gui.
	 * <p>
	 * Returns null if the input is empty.
	 */
	public File getPath()
	{
		var inputStr = locationTF.getText();
		if (inputStr.isEmpty() == true)
			return null;

		return new File(inputStr);
	}

	/**
	 * Returns true if this panel was accepted.
	 */
	public boolean isAccepted()
	{
		return isAccepted;
	}

	/**
	 * Sets the location gui to reflect the specified file.
	 */
	public void setPath(File aFile)
	{
		var inputStr = "";
		if (aFile != null)
			inputStr = aFile.getAbsolutePath();

		locationTF.setText(inputStr);
		updateGui();
	}

	/**
	 * Sets in the instruction
	 */
	public void setInstruction(String aMsg)
	{
		instrTA.setText(aMsg);
	}

	/**
	 * Sets in the minimum free space required
	 */
	public void setMinFreeSpace(long aSize)
	{
		minFreeSpace = aSize;
	}

	/**
	 * Sets in the title
	 */
	public void setTitle(String aTitle)
	{
		titleL.setText(aTitle);
	}

	@Override
	public void actionPerformed(ActionEvent aEvent)
	{
		var source = aEvent.getSource();
		if (source == acceptB)
			doActionAccept();
		else if (source == cancelB)
			doActionCancel();
		else if (source == fileB)
			doActionDestPath();

		updateGui();
	}

	@Override
	public void setVisible(boolean aBool)
	{
		// Reset relevant state vars
		if (aBool == true)
		{
			isAccepted = false;
			updateGui();
		}

		super.setVisible(aBool);
	}

	@Override
	public void zioRead(ZinStream aStream) throws IOException
	{
		super.zioRead(aStream);

		aStream.readVersion(0);

		loaderInfo.zioRead(aStream);
	}

	@Override
	public void zioWrite(ZoutStream aStream) throws IOException
	{
		super.zioWrite(aStream);

		aStream.writeVersion(0);

		loaderInfo.zioWrite(aStream);
	}

	/**
	 * Forms the actual dialog GUI
	 */
	protected void buildGuiArea()
	{
		setLayout(new MigLayout("", "[][][grow]", "[][grow,50::][]"));
		var tmpFont = (new JTextField()).getFont();

		// Title Area
		titleL = new JLabel("Title", JLabel.CENTER);
		add(titleL, "growx,span,wrap");

		// Instruction area
		instrTA = GuiUtil.createUneditableTextArea(2, 0);
		var tmpPane = new JScrollPane(instrTA);
		tmpPane.setBorder(null);
		add(tmpPane, "growx,growy,span,wrap");

		// Location area
		fileB = GuiUtil.createJButton("...", this);
		locationL = new JLabel("Location:");
		locationTF = new GTextField(this);
		add(fileB, "w 20!,h 20!");
		add(locationL);
		add(locationTF, "growx,span,wrap");

		// Info area
		var tmpStr = "Please specify the disk location where the catalog should be constructed..";
		infoL = GuiUtil.createJLabel(tmpStr, tmpFont);
		add(infoL, "growx,span,wrap");

		// Warn area
		tmpStr = "";
		warnL = GuiUtil.createJLabel(tmpStr, tmpFont);
		warnL.setForeground(ColorWarn);
		add(warnL, "growx,h 20!,span,wrap");

		// Control area
		cancelB = GuiUtil.createJButton("Cancel", this);
		acceptB = GuiUtil.createJButton("Accept", this);
		add(cancelB, "ax right,span,split");
		add(acceptB, "");
	}

	/**
	 * Helper method to process the "accept" action.
	 */
	private void doActionAccept()
	{
		isAccepted = true;
		setVisible(false);
		notifyListeners(this, ID_ACCEPT, "Cancel");
	}

	/**
	 * Helper method to process the "cancel" action.
	 */
	private void doActionCancel()
	{
		isAccepted = false;
		setVisible(false);
		notifyListeners(this, ID_CANCEL, "Cancel");
	}

	/**
	 * Helper method to process the "specify destination path" action.
	 */
	private void doActionDestPath()
	{
		// Retrieve the path to load
		File tmpFile = Loader.queryUserForPath(loaderInfo, getParent(), "Select target folder", true);
		if (tmpFile != null)
			locationTF.setValue(tmpFile.getAbsolutePath());
	}

	/**
	 * Helper method that keeps various UI components synchronized.
	 */
	private void updateGui()
	{
		// Retrieve the folder
		File destPath = null;
		String locationStr = locationTF.getText();
		if (locationStr.isEmpty() == false)
		{
			if (locationStr.equals("~") == true)
				locationStr = System.getProperty("user.home");
			if (locationStr.startsWith("~/") == true)
				locationStr = new File(System.getProperty("user.home"), locationStr.substring(2)).getAbsolutePath();

			destPath = new File(locationStr);
		}

		// Retrieve the root folder
		var rootPath = destPath;
		while (rootPath != null)
		{
			if (rootPath.isDirectory() == true)
				break;

			rootPath = rootPath.getParentFile();
		}

		// Test the validity of the location
		String infoStr, warnStr;
		var isValid = false;
		if (rootPath == null)
		{
			infoStr = "Free space: ---";
			warnStr = "Invalid location.";
		}
		else
		{
			var diskBU = new ByteUnit(2);
			var freeBytes = rootPath.getFreeSpace();
			infoStr = "Free space: " + diskBU.getString(freeBytes);
			if (minFreeSpace > 0)
				infoStr += " Required space: " + diskBU.getString(minFreeSpace);

			warnStr = "";
			if (rootPath.canWrite() == false)
				warnStr = "No write permission at location.";
			else if (freeBytes < minFreeSpace && minFreeSpace > 0)
				warnStr = "Not enough free space on disk. Minimun required: " + diskBU.getString(minFreeSpace);
			else
				isValid = true;

			// Update the loaderInfo to reflect the user input
			if (destPath != null)
				loaderInfo.setPath(destPath);
		}

		// Update the components
		infoL.setText(infoStr);
		warnL.setText(warnStr);
		acceptB.setEnabled(isValid);
	}

}
