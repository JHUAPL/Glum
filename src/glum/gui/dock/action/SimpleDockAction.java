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

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.Icon;

import bibliothek.gui.Dockable;
import bibliothek.gui.dock.action.actions.SimpleButtonAction;

/**
 * An DockAction that will fire trigger an embedded java.awt Action
 */
public class SimpleDockAction extends SimpleButtonAction
{
	protected Action refAction;
	
	public SimpleDockAction(Action aAction, String aText, Icon aIcon)
	{
		refAction = aAction;
		
		setText(aText);
		setIcon(aIcon);
	}
	
	@Override
	public void action(Dockable dockable)
	{
		super.action(dockable);
		
		refAction.actionPerformed(new ActionEvent(this, 0, "SimpleDockAction"));
	}
}
