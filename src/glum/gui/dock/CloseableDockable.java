package glum.gui.dock;

import glum.gui.dock.action.Closeable;

import javax.swing.Icon;
import javax.swing.JComponent;

import bibliothek.gui.DockFrontend;

/**
 * Dockable class to wrap aComp into a DefaultDockable using the specified title and icon. This Dockable will be
 * automatically installed into aFrontend and support proper closing via the {@link Closeable} interface.
 */
public class CloseableDockable extends BaseDockable implements Closeable
{
	protected DockFrontend refFrontend;

	public CloseableDockable(DockFrontend aFrontend, String idName, JComponent aComp, String aTitle, Icon aIcon)
	{
		super(aComp, aTitle, aIcon);
		setTitleIcon(aIcon);

		// Register ourselves with the refFrontend
		refFrontend = aFrontend;
		refFrontend.addDockable(idName, this);
	}

	@Override
	public void close()
	{
		refFrontend.hide(this);
	}

}
