package glum.gui.dock.alt;

import glum.gui.dock.BaseDockable;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import bibliothek.gui.Dockable;
import bibliothek.gui.dock.SplitDockStation;
import bibliothek.gui.dock.action.DefaultDockActionSource;
import bibliothek.gui.dock.action.DockAction;
import bibliothek.gui.dock.action.DockActionSource;
import bibliothek.gui.dock.action.LocationHint;

public class AltSplitDockStation extends SplitDockStation
{
	// Action vars
	private List<DockAction> directDockActionList;
	private List<DockAction> localDockActionList;
	private List<DockAction> indirectDockActionList;

	// Lock vars
	private Set<Dockable> lockSet;
	private boolean isLocked;

	public AltSplitDockStation()
	{
		super();

		directDockActionList = Lists.newArrayList();
		localDockActionList = Lists.newArrayList();
		indirectDockActionList = Lists.newArrayList();
		
		lockSet = Sets.newHashSet();
		isLocked = false;
	}

	/**
	 * Registers a DockAction to always be available for direct Dockables
	 */
	public void addDirectActionOffer(DockAction aDockAction)
	{
		directDockActionList.add(aDockAction);
	}

	/**
	 * Registers a DockAction to always be available for local Dockables
	 */
	public void addLocalActionOffer(DockAction aDockAction)
	{
		localDockActionList.add(aDockAction);
	}

	/**
	 * Registers a DockAction to always be available for indirect Dockables
	 */
	public void addIndirectActionOffer(DockAction aDockAction)
	{
		indirectDockActionList.add(aDockAction);
	}

	/**
	 * Utility method to force this station to accept no further Dockables. Only dockables that are currently children
	 * will be accepted.
	 */
	public void lockStation(boolean aBool)
	{
		isLocked = aBool;

		if (isLocked == false)
		{
			lockSet.clear();
			return;
		}

		// Record all of the valid children when the lock is triggered
		for (int c1 = 0; c1 < getDockableCount(); c1++)
		{
			lockSet.add(getDockable(c1));

		}
	}

	@Override
	public boolean accept(Dockable aChild)
	{
		// If we are locked then never accept any Dockable, which was not recorded as valid when the lock happened
		if (isLocked == true && lockSet.contains(aChild) == false)
			return false;

		// Never accept any Dockable that has been marked as nontransferable
		if (aChild instanceof BaseDockable)
			return ((BaseDockable)aChild).isTransferable(this);

		// Default behavior for non BaseDockables
		return super.accept(aChild);
	}

//	
//	@Override
//	protected boolean acceptable(Dockable old, Dockable next)
//	{
//		return false;
////		// Never accept any Dockable that has been marked as nontransferable
////		if (old instanceof BaseDockable || next instanceof BaseDockable)
////		{
////			if (((BaseDockable)old).isTransferable() == false)
////				return false;
////
////			if (((BaseDockable)next).isTransferable() == false)
////				return false;
////		}
////		
////		
////		// TODO Auto-generated method stub
////		return super.acceptable(old, next);
//	}

//	@Override
//	public boolean canDrag(Dockable aDockable)
//	{
//		if (lockSet.contains(aDockable) == true)
//			return false;
//
//		return super.canDrag(aDockable);
//	}
//
//	
//	@Override
//	public boolean canReplace(Dockable oldDockable, Dockable nextDockable)
//	{
//		if (lockSet.contains(oldDockable) == true)
//			return false;
//
//		if (lockSet.contains(nextDockable) == true)
//			return false;
//
//		return super.canReplace(oldDockable, nextDockable);
//	}

	@Override
	public DefaultDockActionSource getDirectActionOffers(Dockable dockable)
	{
		DefaultDockActionSource source;

		source = new DefaultDockActionSource(new LocationHint(LocationHint.DIRECT_ACTION, LocationHint.VERY_RIGHT));
		source.add(directDockActionList.toArray(new DockAction[0]));

		return source;
	}

	@Override
	public DockActionSource getLocalActionOffers()
	{
		DefaultDockActionSource source;

		source = new DefaultDockActionSource(new LocationHint(LocationHint.DOCKABLE, LocationHint.RIGHT));
		source.add(localDockActionList.toArray(new DockAction[0]));

		return source;
	}

	@Override
	public DockActionSource getIndirectActionOffers(Dockable dockable)
	{
		DefaultDockActionSource source;

		source = new DefaultDockActionSource(new LocationHint(LocationHint.INDIRECT_ACTION, LocationHint.VERY_RIGHT));
		source.add(indirectDockActionList.toArray(new DockAction[0]));

		return source;
	}

}
