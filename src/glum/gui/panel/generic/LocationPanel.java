package glum.gui.panel.generic;

import glum.gui.FocusUtil;
import glum.gui.GuiUtil;
import glum.gui.action.ClickAction;
import glum.gui.component.GTextField;
import glum.gui.panel.GlassPanel;
import glum.io.Loader;
import glum.io.LoaderInfo;
import glum.unit.ByteUnit;
import glum.unit.Unit;
import glum.zio.ZinStream;
import glum.zio.ZoutStream;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import net.miginfocom.swing.MigLayout;

public class LocationPanel extends GlassPanel implements ActionListener, GenericCodes
{
	// Constants
	public static final Color warnColor = new Color(128, 0, 0);

	// GUI vars
	protected JLabel titleL, locationL, infoL, warnL;
	protected JButton cancelB, acceptB, fileB;
	protected JTextArea instrTA;
	protected GTextField locationTF;

	// State vars
	protected LoaderInfo loaderInfo;
	protected long minFreeSpace;
	protected boolean isAccepted;

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
	 * Returns the input of the user.
	 */
	public File getInput()
	{
		if (isAccepted == false)
			return null;

		return new File(locationTF.getText());
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
		Object source;

		source = aEvent.getSource();
		if (source == cancelB)
		{
			isAccepted = false;
			setVisible(false);
			notifyListeners(this, ID_CANCEL, "Cancel");
		}
		else if (source == acceptB)
		{
			isAccepted = true;
			setVisible(false);
			notifyListeners(this, ID_ACCEPT, "Accept");
		}
		else if (source == locationTF)
		{
			updateGui();
		}
		else if (source == fileB)
		{
			File aFile;

			// Retrieve the path to load
			aFile = Loader.queryUserForPath(loaderInfo, getParent(), "Select target folder", true);
			if (aFile != null)
				locationTF.setValue(aFile.getAbsolutePath());

			updateGui();
		}

	}

	@Override
	public void setVisible(boolean aBool)
	{
		// Reset the GUI
		if (aBool == true)
		{
			isAccepted = false;
			locationTF.setText("");
			updateGui();
		}

		super.setVisible(aBool);
	}

	@Override
	public void zioReadRaw(ZinStream aStream) throws IOException
	{
		super.zioReadRaw(aStream);

		aStream.readVersion(0);

		loaderInfo.zioReadRaw(aStream);
	}

	@Override
	public void zioWriteRaw(ZoutStream aStream) throws IOException
	{
		super.zioWriteRaw(aStream);

		aStream.writeVersion(0);

		loaderInfo.zioWriteRaw(aStream);
	}

	/**
	 * Forms the actual dialog GUI
	 */
	protected void buildGuiArea()
	{
		JScrollPane tmpPane;
		Font aFont;
		String aStr;

		setLayout(new MigLayout("", "[][][grow]", "[][grow,50::][]"));
		aFont = (new JTextField()).getFont();

		// Title Area
		titleL = new JLabel("Title", JLabel.CENTER);
		add(titleL, "growx,span,wrap");

		// Instruction area
		instrTA = GuiUtil.createUneditableTextArea(2, 0);
		tmpPane = new JScrollPane(instrTA);
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
		aStr = "Please specify the disk location where the catalog should be constructed..";
		infoL = GuiUtil.createJLabel(aStr, aFont);
		add(infoL, "growx,span,wrap");

		// Warn area
		aStr = "";
		warnL = GuiUtil.createJLabel(aStr, aFont);
		warnL.setForeground(warnColor);
		add(warnL, "growx,h 20!,span,wrap");

		// Control area
		cancelB = GuiUtil.createJButton("Cancel", this, aFont);
		acceptB = GuiUtil.createJButton("Accept", this, aFont);
		add(cancelB, "align right,span,split 2");
		add(acceptB, "");

		setBorder(new BevelBorder(BevelBorder.RAISED));
	}

	/**
	 * Utility method to update the various GUI components (most likely infoL, acceptB) based on the current inputTF.
	 */
	protected void updateGui()
	{
		File destPath, rootPath;
		String infoStr, warnStr;
		Unit diskUnit;
		boolean isValid;
		long freeBytes;

		// Retrieve the folder
		destPath = null;
		if (locationTF.getText().isEmpty() == false)
			destPath = new File(locationTF.getText());

		// Retrieve the root folder
		rootPath = destPath;
		while (rootPath != null)
		{
			if (rootPath.isDirectory() == true)
				break;

			rootPath = rootPath.getParentFile();
		}

		// Test the validity of the location
		isValid = false;
		if (rootPath == null)
		{
			infoStr = "Free space: ---";
			warnStr = "Invalid location.";
		}
		else
		{
			diskUnit = new ByteUnit(2);
			freeBytes = rootPath.getFreeSpace();
			infoStr = "Free space: " + diskUnit.getString(freeBytes);
			if (minFreeSpace > 0)
				infoStr += " Required space: " + diskUnit.getString(minFreeSpace);

			warnStr = "";
			if (rootPath.canWrite() == false)
				warnStr = "No write permission at location.";
			else if (freeBytes < minFreeSpace && minFreeSpace > 0)
				warnStr = "Not enough free space on disk. Minimun required: " + diskUnit.getString(minFreeSpace);
			else
				isValid = true;
		}

		// Update the components
		infoL.setText(infoStr);
		warnL.setText(warnStr);
		acceptB.setEnabled(isValid);
	}

}
