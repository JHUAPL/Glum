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

import glum.gui.dock.action.Closeable;

import javax.swing.Icon;
import javax.swing.JComponent;

import bibliothek.gui.DockFrontend;

/**
 * Dockable class to wrap aComp into a DefaultDockable using the specified title and icon. This Dockable will be
 * automatically installed into aFrontend and support proper closing via the {@link Closeable} interface.
 */
public class CloseableDockable extends BaseDockable implements Closeable
{
	protected DockFrontend refFrontend;

	public CloseableDockable(DockFrontend aFrontend, String idName, JComponent aComp, String aTitle, Icon aIcon)
	{
		super(aComp, aTitle, aIcon);
		setTitleIcon(aIcon);

		// Register ourselves with the refFrontend
		refFrontend = aFrontend;
		refFrontend.addDockable(idName, this);
	}

	@Override
	public void close()
	{
		refFrontend.hide(this);
	}

}
