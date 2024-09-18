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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import glum.gui.FocusUtil;
import glum.gui.GuiUtil;
import glum.gui.action.ClickAction;
import glum.gui.panel.GlassPanel;
import net.miginfocom.swing.MigLayout;

/**
 * {@link GlassPanel} used to prompt the user of an action to be taken. A message will be provided via a {@link JLabel}.
 * <p>
 * The title and (prompt) message can be customized.
 *
 * @author lopeznr1
 */
public class SimplePromptPanel extends GlassPanel implements ActionListener, GenericCodes
{
	// GUI vars
	protected JLabel titleL, messageL;
	protected JButton cancelB, acceptB;

	// State vars
	protected boolean isAccepted;

	/** Standard Constructor */
	public SimplePromptPanel(Component aParent)
	{
		super(aParent);

		isAccepted = false;

		// Build the actual GUI
		buildGuiArea();
		setPreferredSize(new Dimension(300, getPreferredSize().height));

		// Set up keyboard short cuts
		FocusUtil.addAncestorKeyBinding(this, "ESCAPE", new ClickAction(cancelB));
		FocusUtil.addAncestorKeyBinding(this, "ENTER", new ClickAction(acceptB));
	}

	/**
	 * Returns true if the user accepted the prompt
	 */
	public boolean isAccepted()
	{
		return isAccepted;
	}

	/**
	 * Sets in the title of this PromptPanel
	 */
	public void setTitle(String aStr)
	{
		titleL.setText(aStr);
	}

	/**
	 * Sets in the informational message of this PromptPanel
	 */
	public void setMessage(String aStr)
	{
		messageL.setText(aStr);
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
	public void setVisible(boolean aBool)
	{
		isAccepted = false;
		super.setVisible(aBool);
	}

	/**
	 * Forms the actual GUI
	 */
	protected void buildGuiArea()
	{
		setLayout(new MigLayout("", "[right][grow][][]", "[][][20!][]"));
		var tmpFont = (new JTextField()).getFont();

		// Title Area
		titleL = new JLabel("Title", JLabel.CENTER);
		add(titleL, "growx,span,wrap");

		// Message area
		messageL = GuiUtil.createJLabel("Message", tmpFont);
		add(messageL, "growx,span,wrap");

		// Control area
		cancelB = GuiUtil.createJButton("Cancel", this);
		acceptB = GuiUtil.createJButton("Accept", this);
		add(cancelB, "skip 2");
		add(acceptB, "");
	}

}
