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

import java.awt.Dialog;
import java.awt.Frame;
import java.awt.Window;

import javax.swing.Icon;

import bibliothek.gui.DockFrontend;
import bibliothek.gui.dock.ScreenDockStation;
import bibliothek.gui.dock.station.screen.AbstractScreenDockWindow;
import bibliothek.gui.dock.station.screen.DefaultScreenDockWindowFactory;
import bibliothek.gui.dock.station.screen.ScreenDockDialog;
import bibliothek.gui.dock.station.screen.ScreenDockWindow;

/**
 * Alternative ScreenDockWindowFactory that return AltScreenDockFrame instead of DefaultScreenDockFrame. Also the
 * returned windows will by default have the typical OS decorations.
 */
public class AltScreenDockWindowFactory extends DefaultScreenDockWindowFactory
{
	private DockFrontend refFrontend;

	public AltScreenDockWindowFactory(DockFrontend aFrontend)
	{
		refFrontend = aFrontend;

		setKind(Kind.FRAME);
		setUndecorated(false);
	}

	@Override
	public ScreenDockWindow createWindow(ScreenDockStation station)
	{
		Kind kind;
		boolean undecorated;
		boolean showDockTitle;
		Icon titleIcon;
		String titleText;

		kind = getKind();
		undecorated = isUndecorated();
		showDockTitle = isShowDockTitle();
		titleIcon = getTitleIcon();
		titleText = getTitleText();

		AbstractScreenDockWindow window;

		if (kind == Kind.FRAME)
		{
			window = new AltScreenDockFrame(refFrontend, station, undecorated);
		}
		else
		{
			Window owner = station.getOwner();
			if (owner instanceof Frame)
				window = new ScreenDockDialog(station, (Frame)owner, undecorated);
			else if (owner instanceof Dialog)
				window = new ScreenDockDialog(station, (Dialog)owner, undecorated);
			else
				window = new ScreenDockDialog(station, undecorated);
		}

		window.setShowTitle(showDockTitle);
		window.setTitleIcon(titleIcon);
		window.setTitleText(titleText);
		return window;
	}
}
