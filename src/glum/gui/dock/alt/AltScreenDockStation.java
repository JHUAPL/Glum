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

import java.awt.Window;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import bibliothek.gui.Dockable;
import bibliothek.gui.dock.ScreenDockStation;
import bibliothek.gui.dock.action.DefaultDockActionSource;
import bibliothek.gui.dock.action.DockAction;
import bibliothek.gui.dock.action.DockActionSource;
import bibliothek.gui.dock.action.LocationHint;
import glum.gui.dock.BaseDockable;

/**
 * Alternative ScreenDockStation which provides no default direct/indirect action offers.
 *
 * @author lopeznr1
 */
public class AltScreenDockStation extends ScreenDockStation
{
	// Action vars
	private List<DockAction> directDockActionL;
	private List<DockAction> indirectDockActionL;

	// Lock vars
	private Set<Dockable> lockS;
	private boolean isLocked;

	/**
	 * Standard Constructor
	 */
	public AltScreenDockStation(Window aOwner)
	{
		super(aOwner);

		directDockActionL = new ArrayList<>();
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

		return super.accept(aChild);
	}

	@Override
	public DefaultDockActionSource getDirectActionOffers(Dockable aDockable)
	{
		DefaultDockActionSource source;

		source = new DefaultDockActionSource(new LocationHint(LocationHint.DIRECT_ACTION, LocationHint.VERY_RIGHT));
		source.add(directDockActionL.toArray(new DockAction[0]));

		return source;
	}

	@Override
	public DockActionSource getIndirectActionOffers(Dockable aDockable)
	{
		DefaultDockActionSource source;

		source = new DefaultDockActionSource(new LocationHint(LocationHint.INDIRECT_ACTION, LocationHint.VERY_RIGHT));
		source.add(indirectDockActionL.toArray(new DockAction[0]));

		return source;
	}

}
