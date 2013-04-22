package glum.logic.dock;

import javax.swing.JFrame;


import bibliothek.gui.DockFrontend;

import glum.gui.dock.FrontendManageConfigPanel;
import glum.logic.LogicChunk;
import glum.registry.Registry;

public class FrontendManageMI implements LogicChunk
{
	private FrontendManageConfigPanel myPanel;
	
	public FrontendManageMI(Registry aRegistry, String aLabel)
	{
		JFrame aFrame;
		DockFrontend aFrontend;
		
		aFrame = aRegistry.getSingleton("root.window", JFrame.class);
		aFrontend = aRegistry.getSingleton(DockFrontend.class, DockFrontend.class);
		
		myPanel = new FrontendManageConfigPanel(aFrame, aFrontend);
	}

	@Override
	public void activate()
	{
		myPanel.setVisible(true);
	}

	@Override
	public void dispose()
	{
	}

	@Override
	public String getName()
	{
		return "DockFrontend Configuration Manager";
	}

	@Override
	public String getVersion()
	{
		return "0.1";
	}

}
