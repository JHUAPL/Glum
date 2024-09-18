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
package glum.logic.dock;

import javax.swing.JMenuItem;

import bibliothek.gui.DockFrontend;
import glum.logic.LogicChunk;
import glum.logic.MenuItemChunk;
import glum.registry.Registry;

/**
 * {@link LogicChunk} used to allow an end user to display a dockable.
 *
 * @author lopeznr1
 */
public class FrontendShowMI implements LogicChunk, MenuItemChunk
{
	// Ref vars
	private final DockFrontend refFrontend;

	// State vars
	private String labelStr;
	private String refName;

	/** Standard Constructor */
	public FrontendShowMI(Registry aRegistry, String aLabel)
	{
		refFrontend = aRegistry.getSingleton(DockFrontend.class, DockFrontend.class);

		var tokenArr = aLabel.split(":");
		if (tokenArr.length != 2)
			throw new RuntimeException("Invalid label specification for LogicChunk.\n Label: " + aLabel);

		labelStr = tokenArr[0];
		refName = tokenArr[1];
	}

	@Override
	public void activate()
	{
		var tmpDockable = refFrontend.getDockable(refName);
		if (tmpDockable == null)
		{
			System.out.println("Failed to locate a dockable with the name: " + refName);
			return;
		}

//		refFrontend.hide(aDockable);
		refFrontend.show(tmpDockable);
		tmpDockable.getDockParent().setFrontDockable(tmpDockable);
	}

	@Override
	public void dispose()
	{
	}

	@Override
	public void setMenuItem(JMenuItem aMI)
	{
		aMI.setText(labelStr);
	}

}
