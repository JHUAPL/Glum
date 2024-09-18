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
 * {@link GlassPanel} used to provide the user with a notification.
 * <p>
 * The title and (notification) message can be customized.
 *
 * @author lopeznr1
 */
public class MessagePanel extends GlassPanel implements ActionListener, GenericCodes
{
	// GUI vars
	private JLabel titleL;
	private JTextArea infoTA;
	private JButton closeB;

	/** Standard Constructor */
	public MessagePanel(Component aParent, String aTitle, int sizeX, int sizeY)
	{
		super(aParent);

		buildGuiArea();
		setSize(sizeX, sizeY);
		setTitle(aTitle);

		// Set up keyboard short cuts
		FocusUtil.addAncestorKeyBinding(this, "ESCAPE", new ClickAction(closeB));
		FocusUtil.addAncestorKeyBinding(this, "ENTER", new ClickAction(closeB));
	}

	/** Simplified Constructor */
	public MessagePanel(Component aParent, String aTitle)
	{
		this(aParent, aTitle, 275, 350);
	}

	/** Simplified Constructor */
	public MessagePanel(Component aParent)
	{
		this(aParent, "Untitled", 275, 350);
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
		infoTA.setCaretPosition(0);
	}

	/**
	 * Sets the tab size associated with the info area.
	 */
	public void setTabSize(int aSize)
	{
		infoTA.setTabSize(aSize);
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
		if (source == closeB)
		{
			setVisible(false);
			notifyListeners(this, ID_CANCEL, "Close");
		}
	}

	/**
	 * Forms the actual GUI
	 */
	private void buildGuiArea()
	{
		setLayout(new MigLayout("", "[right][grow][]", "[][grow][]"));

		// Title Area
		titleL = new JLabel("Title", JLabel.CENTER);
		add(titleL, "growx,span,wrap");

		// Info area
		infoTA = new JTextArea("No status", 3, 0);
		infoTA.setEditable(false);
//		infoTA.setOpaque(false);
		infoTA.setLineWrap(true);
		infoTA.setTabSize(3);
		infoTA.setWrapStyleWord(true);

		JScrollPane tmpScrollPane = new JScrollPane(infoTA);
//		tmpScrollPane.setBorder(null);
		add(tmpScrollPane, "growx,growy,span,wrap");

		// Control area
		closeB = GuiUtil.createJButton("Close", this);
		add(closeB, "skip 2");
	}

}
