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
