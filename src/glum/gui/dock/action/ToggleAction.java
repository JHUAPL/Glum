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
package glum.gui.dock.action;

import javax.swing.Icon;

import bibliothek.gui.Dockable;
import bibliothek.gui.dock.action.actions.SimpleButtonAction;

/**
 * An DockAction that will fire trigger an embedded java.awt Action
 *
 * @author lopeznr1
 */
public class ToggleAction extends SimpleButtonAction
{
	// State vars
	private boolean isActive;

	// Gui vars
	private final Icon falseIcon, trueIcon;

	/** Standard Constructor */
	public ToggleAction(String aText, Icon aFalseIcon, Icon aTrueIcon, boolean aIsActive)
	{
		isActive = aIsActive;

		falseIcon = aFalseIcon;
		trueIcon = aTrueIcon;

		setText(aText);
		updateGui();
	}

	/**
	 * Accessor methods
	 */
	public boolean getIsActive()
	{
		return isActive;
	}

	public void setIsActive(boolean aBool)
	{
		isActive = aBool;
		updateGui();
	}

	@Override
	public void action(Dockable aDockable)
	{
		isActive = !isActive;
		updateGui();

		super.action(aDockable);
	}

	/**
	 * Utility method
	 */
	private void updateGui()
	{
		if (isActive == true)
			setIcon(trueIcon);
		else
			setIcon(falseIcon);
	}

}
