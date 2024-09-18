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

import javax.swing.*;

import glum.gui.FocusUtil;
import glum.gui.GuiUtil;
import glum.gui.action.ClickAction;
import glum.gui.component.GTextField;
import glum.gui.panel.GlassPanel;
import net.miginfocom.swing.MigLayout;

/**
 * {@link GlassPanel} used to prompt the user for a text inupt.
 * <p>
 * The title and (prompt) message can be customized.
 *
 * @author lopeznr1
 */
public abstract class BaseTextInputPanel extends GlassPanel implements ActionListener, GenericCodes
{
	// Constants
	public static final Color warnColor = new Color(128, 0, 0);

	// GUI vars
	protected JLabel titleL, inputL, infoL;
	protected JButton cancelB, acceptB;
	protected GTextField inputTF;

	// State vars
	protected boolean isAccepted;

	/** Standard Constructor */
	public BaseTextInputPanel(Component aParent)
	{
		super(aParent);

		isAccepted = false;

		// Build the actual GUI
		buildGuiArea();
		setPreferredSize(new Dimension(300, getPreferredSize().height));

		// Set up keyboard short cuts
		FocusUtil.addAncestorKeyBinding(this, "ESCAPE", new ClickAction(cancelB));
		FocusUtil.addAncestorKeyBinding(this, "ENTER", new ClickAction(acceptB));
		FocusUtil.addFocusKeyBinding(inputTF, "ENTER", new ClickAction(acceptB));
	}

	/**
	 * Returns the input of the user.
	 */
	public String getInput()
	{
		if (isAccepted == false)
			return null;

		return inputTF.getText();
	}

	/**
	 * Sets in aStr as the default input
	 */
	public void setInput(String aStr)
	{
		inputTF.setValue(aStr);
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
		else if (source == inputTF)
		{
			updateGui();
		}
	}

	@Override
	public void setVisible(boolean aBool)
	{
		if (aBool == true)
			isAccepted = false;
//			resetGui();

		super.setVisible(aBool);
	}

	/**
	 * Forms the actual dialog GUI
	 */
	protected void buildGuiArea()
	{
		setLayout(new MigLayout("", "[right][grow][][]", "[][][20!][]"));
		var tmpFont = (new JTextField()).getFont();

		// Title Area
		titleL = new JLabel("Title", JLabel.CENTER);
		add(titleL, "growx,span 4,wrap");

		// Source area
		inputL = new JLabel("Symbol:");
		inputTF = new GTextField(this);
		add(inputL);
		add(inputTF, "growx,span 3,wrap");

		// Warn area
		var tmpStr = "Please enter text input.";
		infoL = GuiUtil.createJLabel(tmpStr, tmpFont);
		infoL.setForeground(warnColor);
		add(infoL, "growx,span 4,wrap");

		// Control area
		cancelB = GuiUtil.createJButton("Cancel", this);
		acceptB = GuiUtil.createJButton("Accept", this);
		add(cancelB, "skip 2");
		add(acceptB, "");
	}

	/**
	 * Sets the Gui and associated components to the initial state
	 */
	public void resetGui()
	{
		isAccepted = false;
		inputTF.setText("");
		updateGui();
	}

	/**
	 * Utility method to update the various GUI components (most likely infoL, acceptB) based on the current inputTF.
	 */
	protected abstract void updateGui();

}
