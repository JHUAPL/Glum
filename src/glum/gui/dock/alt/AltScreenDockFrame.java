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
package glum.gui.dock.alt;

import glum.gui.dock.action.Destroyable;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import javax.swing.JDialog;
import javax.swing.JFrame;

import bibliothek.gui.DockFrontend;
import bibliothek.gui.DockStation;
import bibliothek.gui.Dockable;
import bibliothek.gui.dock.ScreenDockStation;
import bibliothek.gui.dock.station.screen.ScreenDockFrame;

public class AltScreenDockFrame extends ScreenDockFrame implements WindowListener
{
	private DockFrontend refFrontend;
	private JFrame frame;

	/**
	 * Creates a new ScroonDockWindow with an associated JFrame.
	 * 
	 * @param aStation
	 *           the station to which this dialog is responsible
	 * @param isUndecorated
	 *           whether the dialog should suppress the default decorations
	 */
	public AltScreenDockFrame(DockFrontend aFrontend, ScreenDockStation aStation, boolean isUndecorated)
	{
		super(aStation, isUndecorated);

		refFrontend = aFrontend;

		// Set up the Jframe
		frame = getFrame();
		frame.addWindowListener(this);
		frame.setDefaultCloseOperation(JDialog.DO_NOTHING_ON_CLOSE);
	}

	@Override
	public void windowOpened(WindowEvent e)
	{
		; // Nothing to do
	}

	@Override
	public void windowClosing(WindowEvent e)
	{
		Dockable dockable;

		System.out.println("Window should be closed....");
		dockable = getDockable();

		// Destroy the associated Destroyable
		if (dockable instanceof Destroyable)
		{
			((Destroyable)dockable).destroy();
		}
		// Attempt to hide the (if it is registered) Dockable
		else if (refFrontend != null && refFrontend.listDockables().contains(dockable) == true)
		{
			refFrontend.hide(dockable);
		}
		// Just hide the dockable
		else
		{
			DockStation aStation;

			aStation = dockable.getDockParent();
			aStation.drag(dockable);
		}
	}

	@Override
	public void windowClosed(WindowEvent e)
	{
		; // Nothing to do
	}

	@Override
	public void windowIconified(WindowEvent e)
	{
		; // Nothing to do
	}

	@Override
	public void windowDeiconified(WindowEvent e)
	{
		; // Nothing to do
	}

	@Override
	public void windowActivated(WindowEvent e)
	{
		; // Nothing to do
	}

	@Override
	public void windowDeactivated(WindowEvent e)
	{
		; // Nothing to do
	}

}
