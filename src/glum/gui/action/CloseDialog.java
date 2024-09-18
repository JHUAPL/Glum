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
package glum.gui.action;

import java.awt.Component;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

import javax.swing.*;

import glum.gui.GuiUtil;
import net.miginfocom.swing.MigLayout;

/**
 * JDialog that holds a single {@link Component} and a close button.
 *
 * @author lopeznr1
 */
public class CloseDialog extends JDialog implements ActionListener
{
	// Gui vars
	private final JButton closeB;

	/**
	 * Standard Constructor
	 */
	public CloseDialog(Frame aParent, Component aMainComp)
	{
		super(aParent);

		var tmpPanel = new JPanel();
		tmpPanel.setLayout(new MigLayout("", "[]", "0[]0[]"));

		tmpPanel.add(aMainComp, "growx,growy,pushx,pushy,span,wrap");

		// Form a unified list of buttons
		var tmpActionCompL = new ArrayList<Component>();
		if (aMainComp instanceof ActionComponentProvider)
			tmpActionCompL.addAll(((ActionComponentProvider) aMainComp).getActionButtons());
		closeB = GuiUtil.formButton(this, "Close");
		tmpActionCompL.add(closeB);

		// Add the components in
		var isFirst = true;
		for (Component aComp : tmpActionCompL)
		{
			if (isFirst == true)
				tmpPanel.add(aComp, "span,split,ax right");
			else
				tmpPanel.add(aComp, "");

			isFirst = false;
		}

		setContentPane(tmpPanel);
		setDefaultCloseOperation(JDialog.HIDE_ON_CLOSE);
		setModal(false);
		pack();
	}

	@Override
	public void actionPerformed(ActionEvent aEvent)
	{
		var source = aEvent.getSource();
		if (source == closeB)
			setVisible(false);
	}

}
