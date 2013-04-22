package glum.logic.misc;

import glum.gui.dialog.MemoryUtilDialog;
import glum.logic.LogicChunk;
import glum.registry.Registry;

import javax.swing.JFrame;

public class MemoryDialogMI implements LogicChunk
{
	private JFrame refMainFrame;
	private MemoryUtilDialog myDialog;
	
	public MemoryDialogMI(Registry aRegistry, String aLabel)
	{
		refMainFrame = aRegistry.getSingleton("root.window", JFrame.class);
		myDialog = null;
	}

	@Override
	public void activate()
	{
		// Lazy initialization
		if (myDialog == null)
			myDialog = new MemoryUtilDialog(refMainFrame);
		
		myDialog.setVisible(true);
	}

	@Override
	public void dispose()
	{
	}

	@Override
	public String getName()
	{
		return "Memory Util Dialog";
	}

	@Override
	public String getVersion()
	{
		return "0.1";
	}

}
