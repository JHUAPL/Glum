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

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import glum.gui.FocusUtil;
import glum.gui.GuiUtil;
import glum.gui.action.ClickAction;
import glum.gui.panel.GlassPanel;
import net.miginfocom.swing.MigLayout;

/**
 * {@link GlassPanel} used to prompt the user of an action to be taken. A message will be provided via a
 * {@link JTextArea}.
 * <p>
 * The title and (prompt) message can be customized.
 *
 * @author lopeznr1
 */
public class PromptPanel extends GlassPanel implements ActionListener, GenericCodes
{
	// GUI vars
	private JLabel titleL;
	private JTextArea infoTA;
	private JButton cancelB, acceptB;

	// State vars
	private boolean isAccepted;

	/** Standard Constructor */
	public PromptPanel(Component aParent, String aTitle, int sizeX, int sizeY)
	{
		super(aParent);

		isAccepted = false;

		buildGuiArea();
		setSize(sizeX, sizeY);
		setTitle(aTitle);

		// Set up keyboard short cuts
		FocusUtil.addAncestorKeyBinding(this, "ESCAPE", new ClickAction(cancelB));
		FocusUtil.addAncestorKeyBinding(this, "ENTER", new ClickAction(acceptB));
	}

	/** Simplified Constructor */
	public PromptPanel(Component aParent, String aTitle)
	{
		this(aParent, aTitle, 275, 350);
	}

	/** Simplified Constructor */
	public PromptPanel(Component aParent)
	{
		this(aParent, "Untitled", 275, 350);
	}

	/**
	 * Returns true if the prompt was accepted
	 */
	public boolean isAccepted()
	{
		return isAccepted;
	}

	/**
	 * Sets the info message and adjusts the caret position.
	 */
	public void setInfo(String aStr, int aCaretPos)
	{
		infoTA.setText(aStr);
		infoTA.setCaretPosition(aCaretPos);
	}

	/**
	 * Sets the info message.
	 */
	public void setInfo(String aStr)
	{
		infoTA.setText(aStr);
	}

	/**
	 * Sets in the tabs size of the info text area.
	 */
	public void setTabSize(int aNumSpaces)
	{
		infoTA.setTabSize(aNumSpaces);
	}

	/**
	 * Sets the title of this panel.
	 */
	public void setTitle(String aTitle)
	{
		titleL.setText(aTitle);
	}

	@Override
	public void actionPerformed(ActionEvent aEvent)
	{
		var source = aEvent.getSource();
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
	}

	@Override
	public void setVisible(boolean isVisible)
	{
		// Reset the panel
		if (isVisible == true)
			isAccepted = false;

		super.setVisible(isVisible);
	}

	/**
	 * Forms the actual GUI
	 */
	protected void buildGuiArea()
	{
		setLayout(new MigLayout("", "[right][grow][][]", "[][grow][]"));

		// Title Area
		titleL = new JLabel("Title", JLabel.CENTER);
		add(titleL, "growx,span,wrap");

		// Info area
		infoTA = new JTextArea("No status", 3, 0);
		infoTA.setEditable(false);
//		infoTA.setOpaque(false);
		infoTA.setLineWrap(true);
		infoTA.setWrapStyleWord(true);

		var tmpScrollPane = new JScrollPane(infoTA);
//		tmpScrollPane.setBorder(null);
		add(tmpScrollPane, "growx,growy,span,wrap");

		// Control area
		cancelB = GuiUtil.createJButton("Cancel", this);
		acceptB = GuiUtil.createJButton("Accept", this);
		add(cancelB, "skip 2");
		add(acceptB, "");
	}

}
