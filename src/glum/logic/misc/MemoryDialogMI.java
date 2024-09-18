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
package glum.logic.misc;

import javax.swing.JFrame;

import glum.gui.memory.MemoryUtilDialog;
import glum.logic.LogicChunk;
import glum.registry.Registry;

/**
 * {@link LogicChunk} used to show a UI component of the memory state of the application.
 *
 * @author lopeznr1
 */
public class MemoryDialogMI implements LogicChunk
{
	// Ref vars
	private final JFrame refMainFrame;

	// State vars
	private MemoryUtilDialog myDialog;

	/** Standard Constructor */
	public MemoryDialogMI(Registry aRegistry, String aLabel)
	{
		refMainFrame = aRegistry.getSingleton("root.window", JFrame.class);
		myDialog = null;
	}

	@Override
	public void activate()
	{
		// Lazy initialization
		if (myDialog == null)
			myDialog = new MemoryUtilDialog(refMainFrame);

		myDialog.setVisible(true);
	}

	@Override
	public void dispose()
	{
	}

}
