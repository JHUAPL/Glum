package glum.gui.dock.action;

import javax.swing.Icon;

import bibliothek.gui.DockStation;
import bibliothek.gui.Dockable;
import bibliothek.gui.dock.action.actions.SimpleButtonAction;

/**
 * A DockAction that will dismiss the targeted Dockable. If the targeted Dockable is a Destroyable, then is's destroy
 * method will be called. If the targeted Dockable is a Closeable then it's close method is called. As a last resort the
 * Dockable will just be hidden by removing it from it's parent. This should effectively hide the Dockable.
 */
public class DismissAction extends SimpleButtonAction
{
	public DismissAction(String aText, Icon aIcon)
	{
		setText(aText);
		setIcon(aIcon);
	}

	@Override
	public void action(Dockable aDockable)
	{
		DockStation aStation;

		super.action(aDockable);

		// Destroy the Destroyable
		if (aDockable instanceof Destroyable)
		{
			((Destroyable)aDockable).destroy();
		}
		// Close the Closable
		else if (aDockable instanceof Closeable)
		{
			((Closeable)aDockable).close();
		}
		// Remove the Dockable from it's parent (last resort)
		else
		{
			aStation = aDockable.getDockParent();
			aStation.drag(aDockable);
		}
	}
}
