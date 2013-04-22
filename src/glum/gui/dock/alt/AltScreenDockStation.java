package glum.gui.dock.alt;

import glum.gui.dock.BaseDockable;

import java.awt.Window;
import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

import bibliothek.gui.Dockable;
import bibliothek.gui.dock.ScreenDockStation;
import bibliothek.gui.dock.action.DefaultDockActionSource;
import bibliothek.gui.dock.action.DockAction;
import bibliothek.gui.dock.action.DockActionSource;
import bibliothek.gui.dock.action.LocationHint;

/**
 * Alternative ScreenDockStation which provides no default direct/indirect action offers.
 */
public class AltScreenDockStation extends ScreenDockStation
{
	// Action vars
	private List<DockAction> directDockActionList;
	private List<DockAction> indirectDockActionList;

	// Lock vars
	private Set<Dockable> lockSet;
	private boolean isLocked;

	public AltScreenDockStation(Window owner)
	{
		super(owner);

		directDockActionList = Lists.newArrayList();
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

		return super.accept(aChild);
	}

	@Override
	public DefaultDockActionSource getDirectActionOffers(Dockable dockable)
	{
		DefaultDockActionSource source;

		source = new DefaultDockActionSource(new LocationHint(LocationHint.DIRECT_ACTION, LocationHint.VERY_RIGHT));
		source.add(directDockActionList.toArray(new DockAction[0]));

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
