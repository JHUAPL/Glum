package glum.gui.dock;

import javax.swing.Icon;
import javax.swing.JComponent;

import bibliothek.gui.DockStation;
import bibliothek.gui.dock.DefaultDockable;

public class BaseDockable extends DefaultDockable
{
	// Tells whether this Dockable can be dragged and dropped to another station
	private DockStation homeStation;
	private boolean isTransferable;
	
	public BaseDockable()
	{
		super();
		
		isTransferable = true;
	}
	
	public BaseDockable(JComponent aComp, String aTitle, Icon aIcon)
	{
		super(aComp, aTitle, aIcon);
		
		isTransferable = true;
	}

	public boolean isTransferable(DockStation aStation)
	{
		if (isTransferable == true)
			return true;
	
		// We can only be transfered to our homeStation when we are not transferable
		return aStation == homeStation;
	}
	
	public void setTransferable(boolean aBool)
	{
		homeStation = null;
		isTransferable = aBool;
		
		
		// Record our parent when we become non transferable
		if (isTransferable == false)
			homeStation = getDockParent();
	}
	

}
