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
package glum.logic.dock;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JMenu;
import javax.swing.JMenuItem;

import bibliothek.gui.DockFrontend;
import bibliothek.gui.Dockable;
import bibliothek.gui.dock.event.DockFrontendListener;
import glum.logic.LogicChunk;
import glum.logic.SubMenuChunk;
import glum.registry.Registry;

/**
 * {@link LogicChunk} used to allow an end user to load a dockable configuration.
 *
 * @author lopeznr1
 */
public class FrontendLoadMI implements LogicChunk, SubMenuChunk, DockFrontendListener, ActionListener
{
	// Ref vars
	private final DockFrontend refFrontend;

	// State vars
	private JMenu refMenu;

	/** Standard Constructor */
	public FrontendLoadMI(Registry aRegistry, String aLabel)
	{
		refFrontend = aRegistry.getSingleton(DockFrontend.class, DockFrontend.class);
		refFrontend.addFrontendListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent aEvent)
	{
		var source = aEvent.getSource();
		if (source instanceof JMenuItem)
		{
			var name = ((JMenuItem) source).getText();
			refFrontend.load(name);
		}
	}

	@Override
	public void activate()
	{
		; // Nothing to do
	}

	@Override
	public void dispose()
	{
	}

	@Override
	public void setMenu(JMenu aMenu)
	{
		refMenu = aMenu;
		updateGui();
	}

	@Override
	public void hidden(DockFrontend frontend, Dockable dockable)
	{
		; // Nothing to do
	}

	@Override
	public void shown(DockFrontend frontend, Dockable dockable)
	{
		; // Nothing to do
	}

	@Override
	public void added(DockFrontend frontend, Dockable dockable)
	{
		; // Nothing to do
	}

	@Override
	public void removed(DockFrontend frontend, Dockable dockable)
	{
		; // Nothing to do
	}

	@Override
	public void hideable(DockFrontend frontend, Dockable dockable, boolean hideable)
	{
		; // Nothing to do
	}

	@Override
	public void loaded(DockFrontend frontend, String name)
	{
		; // Nothing to do
	}

	@Override
	public void read(DockFrontend frontend, String name)
	{
		updateGui();
	}

	@Override
	public void saved(DockFrontend frontend, String name)
	{
		updateGui();
	}

	@Override
	public void deleted(DockFrontend frontend, String name)
	{
		updateGui();
	}

	/**
	 * Helper method that keeps various UI components synchronized.
	 */
	protected void updateGui()
	{
		// Remove the old items
		refMenu.removeAll();

		// Add all of the current configurations
		var currS = refFrontend.getSettings();
		for (String aStr : currS)
		{
			// Do not add hidden configurations
			if (aStr.charAt(0) != '.')
			{
				var tmpMI = new JMenuItem(aStr);
				tmpMI.addActionListener(this);
				refMenu.add(tmpMI);
			}
		}

		// Ensure we have items (to be enabled)
		refMenu.setEnabled(currS.size() > 0);
	}

}
