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
import javax.swing.JComponent;

import bibliothek.gui.Dockable;
import bibliothek.gui.dock.action.actions.SimpleButtonAction;

/**
 * A DockAction that will cause the target component to become visible. This action is useful to bring up a
 * configuration panel related to the Dockable.
 */
public class MakeVisibleAction extends SimpleButtonAction
{
	private JComponent targComp;

	public MakeVisibleAction(JComponent aTargetComp, String aText, Icon aIcon)
	{
		targComp = aTargetComp;

		setText(aText);
		setIcon(aIcon);
	}

	@Override
	public void action(Dockable aDockable)
	{
		super.action(aDockable);

		// Make the component visible
		targComp.setVisible(true);
	}
}
