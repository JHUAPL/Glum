package glum.gui.dock;

import bibliothek.gui.DockStation;
import bibliothek.gui.Dockable;
import bibliothek.gui.dock.station.support.PlaceholderStrategy;
import bibliothek.gui.dock.station.support.PlaceholderStrategyListener;
import bibliothek.util.Path;

/*
 * This is our very simple PlaceholderStrategy. It only recognizes our custom
 * PrimDockable and returns the associated place holder.
 * 
 * TODO: This class is incomplete. It might be deleted in the future.
 */
public class CustomPlaceholderStrategy implements PlaceholderStrategy
{
	@Override
	public void addListener(PlaceholderStrategyListener listener)
	{
		// ignore
	}

	@Override
	public Path getPlaceholderFor(Dockable aDockable)
	{
		// We handle only PrimDockables
		if (aDockable instanceof PrimDock == false)
			return null;
			
//System.out.println("Attempting to add placeholder for dockable: " + aDockable.getTitleText());
//		return new Path("Prim:" + ((PrimDockable)aDockable).getUniqueId());
		return null;
	}

	@Override
	public void install(DockStation station)
	{
		// ignore
	}

	@Override
	public boolean isValidPlaceholder(Path placeholder)
	{
		System.out.println("isValidPlaceholder()-> Path:" + placeholder.toString());		
		
		return true;
	}

	@Override
	public void removeListener(PlaceholderStrategyListener listener)
	{
		// ignore
	}

	@Override
	public void uninstall(DockStation station)
	{
		// ignore
	}
	
}
