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
package glum.gui;

import java.awt.Component;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

/**
 * Collection of execution utilities used to execute behavior at a specific time frame associated with the life cycle of
 * AWT/Swing components.
 * 
 * @author lopeznr1
 */
public class GuiExeUtil
{
	/**
	 * Utility method to execute the Runnable once the specified Component is "showing" on the screen.
	 */
	public static void executeOnceWhenShowing(Component aComp, Runnable aRunnable)
	{
		aComp.addHierarchyListener(new HierarchyListener() {

			@Override
			public void hierarchyChanged(HierarchyEvent aEvent)
			{
				if (aComp.isShowing() == false)
					return;

				aRunnable.run();
				aComp.removeHierarchyListener(this);
			}
		});

	}

}
