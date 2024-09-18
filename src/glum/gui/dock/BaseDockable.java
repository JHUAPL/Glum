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

import javax.swing.Icon;
import javax.swing.JComponent;

import bibliothek.gui.DockStation;
import bibliothek.gui.dock.DefaultDockable;

public class BaseDockable extends DefaultDockable
{
	// Tells whether this Dockable can be dragged and dropped to another station
	private DockStation homeStation;
	private boolean isTransferable;

	public BaseDockable()
	{
		isTransferable = true;
	}

	public BaseDockable(JComponent aComp, String aTitle, Icon aIcon)
	{
		super(aComp, aTitle, aIcon);

		isTransferable = true;
	}

	public boolean isTransferable(DockStation aStation)
	{
		if (isTransferable == true)
			return true;

		// We can only be transfered to our homeStation when we are not transferable
		return aStation == homeStation;
	}

	public void setTransferable(boolean aBool)
	{
		homeStation = null;
		isTransferable = aBool;

		// Record our parent when we become non transferable
		if (isTransferable == false)
			homeStation = getDockParent();
	}

}
