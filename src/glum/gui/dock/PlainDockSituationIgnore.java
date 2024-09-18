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

import java.util.LinkedHashSet;
import java.util.Set;

import bibliothek.gui.DockStation;
import bibliothek.gui.dock.DockElement;
import bibliothek.gui.dock.layout.DockSituationIgnore;
import bibliothek.gui.dock.perspective.PerspectiveElement;
import bibliothek.gui.dock.perspective.PerspectiveStation;

public class PlainDockSituationIgnore implements DockSituationIgnore
{
	// State vars
	private Set<Class<?>> ignoreClassS;

	/** Standard Constructor */
	public PlainDockSituationIgnore()
	{
		ignoreClassS = new LinkedHashSet<>();
	}

	/**
	 * Method to register a new type of Dockable/Station to ignore
	 */
	public void addIgnoreClass(Class<?> aClass)
	{
		ignoreClassS.add(aClass);
	}

	@Override
	public boolean ignoreElement(PerspectiveElement aElement)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean ignoreElement(DockElement aElement)
	{
		// Check to see if aElement is of one of the types in ignoreClassSet
		for (var aClass : ignoreClassS)
		{
			if (aClass.isAssignableFrom(aElement.getClass()) == true)
				return true;
		}

		return false;
	}

	@Override
	public boolean ignoreChildren(PerspectiveStation aStation)
	{
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean ignoreChildren(DockStation aStation)
	{
		// TODO Auto-generated method stub
		return false;
	}

}
