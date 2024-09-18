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

import java.awt.*;

import javax.swing.JComponent;
import javax.swing.RootPaneContainer;

import glum.gui.GuiUtil;
import net.miginfocom.swing.MigLayout;

/**
 * UI panel for displaying an overlayed {@link Component}.
 *
 * @author lopeznr1
 */
public class StandardPane extends JComponent
{
	// Communicator vars
	private final Component refParent;
	private final JComponent displayPane;

	private RootPaneContainer rootPane;

	// State vars
	private Color fillColor;
//	private CustomFocusTraversalPolicy myFocusPolicy;

	/** Standard Constructor */
	public StandardPane(Component aRefParent, JComponent aDisplayPane)
	{
		// Communicator vars
		refParent = aRefParent;
		displayPane = aDisplayPane;

		// Build the GUI
		setLayout(new MigLayout("", "0[center,fill]0", "0[center,fill]0"));
		add(aDisplayPane, "span 1");

		rootPane = null;
		fillColor = new Color(96, 96, 96, 96);
	}

	/**
	 * Set in an alternative shade color.
	 */
	public void setFillColor(Color aColor)
	{
		fillColor = aColor;
	}

	@Override
	public void setVisible(boolean isVisible)
	{
		// Initialize the GUI if it has not been initialed
		// Once initialized rootPane != null
		if (rootPane == null)
			initializeGui();

		((Component)rootPane).setVisible(isVisible);
	}

	/**
	 * Sets up the GlassPane
	 */
	protected void initializeGui()
	{
		ComponentTracker aTracker;

		// Ensure this method is not called twice
		if (rootPane != null)
			throw new RuntimeException("GlassPane.initializeGui() has been already called.");

		// Retrieve the associated rootPane and layeredPane
		if (refParent == null)
			rootPane = GuiUtil.getRootPaneContainer(displayPane);
		else
			rootPane = GuiUtil.getRootPaneContainer(refParent);

		// Set up a ComponentTracker to keep this ShadePane linked to the rootPane
		aTracker = new ComponentTracker(this);
		aTracker.setResizedTracker((Component) rootPane);
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		g.setColor(fillColor);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	/**
	 * Utility method to help with debugging
	 */
	protected void debugMsg(Component aComp, String aStr)
	{
		if (aComp == displayPane)
			System.out.println("displayPane being " + aStr + "...");
		else if (aComp == rootPane)
			System.out.println("rootPane being " + aStr + "...");
		else
			System.out.println("Undefined object being " + aStr + "...");
	}

}
