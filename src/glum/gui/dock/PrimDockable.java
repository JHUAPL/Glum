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

import bibliothek.gui.dock.DefaultDockable;

/**
 * Base class for Dockables which would like to store their configuration via the PrimConfig mechanism. Note if the
 * child class will be loaded with the PrimDockableFactory, then you should have a constructor with one of the following
 * properties:.
 * <ul>
 * <li>1 arguments: Registry
 * <li>0 arguments: Empty Constructor
 * </ul>
 *
 * @author lopeznr1
 */
public abstract class PrimDockable extends DefaultDockable implements PrimDock
{
	@Override
	public abstract PrimConfig getConfiguration();

	@Override
	public abstract void setConfiguration(PrimConfig aConfig);

	@Override
	public String getFactoryID()
	{
		return PrimDockFactory.ID;
	}

}
