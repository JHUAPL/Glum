package glum.logic.dock;

import javax.swing.JMenuItem;

import glum.logic.LogicChunk;
import glum.logic.MenuItemChunk;
import glum.registry.Registry;

import bibliothek.gui.DockFrontend;
import bibliothek.gui.Dockable;

public class FrontendShowMI implements LogicChunk, MenuItemChunk
{
	// State vars
	protected DockFrontend refFrontend;
	protected String labelStr;
	protected String refName;
	
	public FrontendShowMI(Registry aRegistry, String aLabel)
	{
		String[] tokens;
		
		refFrontend = aRegistry.getSingleton(DockFrontend.class, DockFrontend.class);
		
		tokens = aLabel.split(":");
		if (tokens.length != 2)
			throw new RuntimeException("Invalid label specification for LogicChunk.\n Label: " + aLabel);
		
		labelStr = tokens[0];
		refName = tokens[1];
	}

	@Override
	public void activate()
	{
		Dockable aDockable;
		
		aDockable = refFrontend.getDockable(refName);
		if (aDockable == null)
		{
			System.out.println("Failed to locate a dockable with the name: " + refName);
			return;
		}
		
//		refFrontend.hide(aDockable);
		refFrontend.show(aDockable);
		aDockable.getDockParent().setFrontDockable(aDockable);
	}

	@Override
	public void dispose()
	{
	}

	@Override
	public String getName()
	{
		return "DockFrontend Dock Shower";
	}

	@Override
	public String getVersion()
	{
		return "0.1";
	}

	@Override
	public void setMenuItem(JMenuItem aMI)
	{
		aMI.setText(labelStr);
	}

}
