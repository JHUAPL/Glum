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

import javax.swing.JFrame;

import bibliothek.gui.DockFrontend;
import glum.gui.dock.FrontendManageConfigPanel;
import glum.logic.LogicChunk;
import glum.registry.Registry;

/**
 * {@link LogicChunk} used to display the various dockable configurations and allow the user to switch to a specific
 * configuration..
 *
 * @author lopeznr1
 */
public class FrontendManageMI implements LogicChunk
{
	// State vars
	private final FrontendManageConfigPanel myPanel;

	/** Standard Constructor */
	public FrontendManageMI(Registry aRegistry, String aLabel)
	{
		var tmpFrame = aRegistry.getSingleton("root.window", JFrame.class);
		var tmpFrontend = aRegistry.getSingleton(DockFrontend.class, DockFrontend.class);

		myPanel = new FrontendManageConfigPanel(tmpFrame, tmpFrontend);
	}

	@Override
	public void activate()
	{
		myPanel.setVisible(true);
	}

	@Override
	public void dispose()
	{
	}

}
