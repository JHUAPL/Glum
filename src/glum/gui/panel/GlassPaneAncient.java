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
package glum.gui.panel;

import glum.gui.GuiUtil;

import java.awt.*;
import javax.swing.*;

import net.miginfocom.swing.MigLayout;

public class GlassPaneAncient extends JComponent
{
	// Communicator vars
	protected Component refParent;
	protected JComponent displayPane;

	/**
	 * Constructor
	 */
	public GlassPaneAncient(Component aRefParent, JComponent aDisplayPane)
	{
		// Communicator vars
		refParent = aRefParent;
		displayPane = aDisplayPane;

		// Build the GUI
		setLayout(new MigLayout("", "0[center,grow]0", "0[center,grow]0"));
		add(aDisplayPane, "span 1");

		GlassPaneListener aListener = new GlassPaneListener(this, aDisplayPane);
		addMouseListener(aListener);
		addMouseMotionListener(aListener);
	}

	@Override
	public void setVisible(boolean isVisible)
	{
		RootPaneContainer rootPane;

		// Update the RootPane to use this GlassPane
		rootPane = GuiUtil.getRootPaneContainer(refParent);
		if (isVisible == true)
			rootPane.setGlassPane(this);
//		else if (isVisible == false && rootPane.getGlassPane() == this)
//			rootPane.setGlassPane(null);

		super.setVisible(isVisible);
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		g.setColor(new Color(192, 192, 192, 192));
		g.fillRect(0, 0, getWidth(), getHeight());
	}

}
