// Copyright (C) 2024 The Johns Hopkins University Applied Physics Laboratory LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package glum.gui.dock.alt;

import java.util.*;

import bibliothek.gui.Dockable;
import bibliothek.gui.dock.SplitDockStation;
import bibliothek.gui.dock.action.*;
import glum.gui.dock.BaseDockable;

/**
 * Alternative implementation of SplitDockStation.
 *
 * @author lopeznr1
 */
public class AltSplitDockStation extends SplitDockStation
{
	// Action vars
	private List<DockAction> directDockActionL;
	private List<DockAction> localDockActionL;
	private List<DockAction> indirectDockActionL;

	// Lock vars
	private Set<Dockable> lockS;
	private boolean isLocked;

	/** Standard Constructor */
	public AltSplitDockStation()
	{
		directDockActionL = new ArrayList<>();
		localDockActionL = new ArrayList<>();
		indirectDockActionL = new ArrayList<>();

		lockS = new HashSet<>();
		isLocked = false;
	}

	/**
	 * Registers a DockAction to always be available for direct Dockables
	 */
	public void addDirectActionOffer(DockAction aDockAction)
	{
		directDockActionL.add(aDockAction);
	}

	/**
	 * Registers a DockAction to always be available for local Dockables
	 */
	public void addLocalActionOffer(DockAction aDockAction)
	{
		localDockActionL.add(aDockAction);
	}

	/**
	 * Registers a DockAction to always be available for indirect Dockables
	 */
	public void addIndirectActionOffer(DockAction aDockAction)
	{
		indirectDockActionL.add(aDockAction);
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
			lockS.clear();
			return;
		}

		// Record all of the valid children when the lock is triggered
		for (int c1 = 0; c1 < getDockableCount(); c1++)
		{
			lockS.add(getDockable(c1));

		}
	}

	@Override
	public boolean accept(Dockable aChild)
	{
		// If we are locked then never accept any Dockable, which was not recorded as valid when the lock happened
		if (isLocked == true && lockS.contains(aChild) == false)
			return false;

		// Never accept any Dockable that has been marked as nontransferable
		if (aChild instanceof BaseDockable)
			return ((BaseDockable) aChild).isTransferable(this);

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
	public DefaultDockActionSource getDirectActionOffers(Dockable aDockable)
	{
		var source = new DefaultDockActionSource(new LocationHint(LocationHint.DIRECT_ACTION, LocationHint.VERY_RIGHT));
		source.add(directDockActionL.toArray(new DockAction[0]));

		return source;
	}

	@Override
	public DockActionSource getLocalActionOffers()
	{
		var source = new DefaultDockActionSource(new LocationHint(LocationHint.DOCKABLE, LocationHint.RIGHT));
		source.add(localDockActionL.toArray(new DockAction[0]));

		return source;
	}

	@Override
	public DockActionSource getIndirectActionOffers(Dockable aDockable)
	{
		var source = new DefaultDockActionSource(new LocationHint(LocationHint.INDIRECT_ACTION, LocationHint.VERY_RIGHT));
		source.add(indirectDockActionL.toArray(new DockAction[0]));

		return source;
	}

}
