package glum.logic.dock;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Set;
import javax.swing.JMenu;
import javax.swing.JMenuItem;

import glum.logic.LogicChunk;
import glum.logic.SubMenuChunk;
import glum.registry.Registry;

import bibliothek.gui.DockFrontend;
import bibliothek.gui.Dockable;
import bibliothek.gui.dock.event.DockFrontendListener;

public class FrontendLoadMI implements LogicChunk, SubMenuChunk, DockFrontendListener, ActionListener
{
	protected DockFrontend refFrontend;
	protected JMenu refMenu;

	public FrontendLoadMI(Registry aRegistry, String aLabel)
	{
		refFrontend = aRegistry.getSingleton(DockFrontend.class, DockFrontend.class);
		refFrontend.addFrontendListener(this);
	}

	@Override
	public void actionPerformed(ActionEvent aEvent)
	{
		Object aSource;
		String aName;

		aSource = aEvent.getSource();
		if (aSource instanceof JMenuItem)
		{
			aName = ((JMenuItem)aSource).getText();
			refFrontend.load(aName);
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
	public String getName()
	{
		return "DockFrontend Configuration Loader";
	}

	@Override
	public String getVersion()
	{
		return "0.1";
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
	 * Utility method to keep the refMenu in sync with the available dock
	 * configurations
	 */
	protected void updateGui()
	{
		Set<String> currSet;
		JMenuItem tmpMI;

		// Remove the old items
		refMenu.removeAll();

		// Add all of the current configurations
		currSet = refFrontend.getSettings();
		for (String aStr : currSet)
		{
			// Do not add hidden configurations
			if (aStr.charAt(0) != '.')
			{
				tmpMI = new JMenuItem(aStr);
				tmpMI.addActionListener(this);
				refMenu.add(tmpMI);
			}
		}

		// Ensure we have items (to be enabled)
		refMenu.setEnabled(currSet.size() > 0);
	}

}
