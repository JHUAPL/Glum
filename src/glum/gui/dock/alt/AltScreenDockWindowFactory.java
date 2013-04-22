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
