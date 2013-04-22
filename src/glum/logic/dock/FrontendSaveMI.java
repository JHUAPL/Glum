package glum.logic.dock;

import javax.swing.JFrame;

import glum.gui.dock.FrontendAddConfigPanel;
import glum.logic.LogicChunk;
import glum.registry.Registry;

import bibliothek.gui.DockFrontend;

public class FrontendSaveMI implements LogicChunk
{
	protected FrontendAddConfigPanel myPanel;
	
	public FrontendSaveMI(Registry aRegistry, String aLabel)
	{
		JFrame aFrame;
		DockFrontend aFrontend;
		
		aFrame = aRegistry.getSingleton("root.window", JFrame.class);
		aFrontend = aRegistry.getSingleton(DockFrontend.class, DockFrontend.class);

		myPanel = new FrontendAddConfigPanel(aFrame, aFrontend);
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
		return "DockFrontend Configuration Saver";
	}

	@Override
	public String getVersion()
	{
		return "0.1";
	}

}
