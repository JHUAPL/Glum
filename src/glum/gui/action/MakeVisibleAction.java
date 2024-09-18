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
package glum.gui.action;

import java.awt.Component;
import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;

/**
 * Action used to change the target {@link Component} to be visible.
 *
 * @author lopeznr1
 */
public class MakeVisibleAction extends AbstractAction
{
	// Reference vars
	private final Component refTarget;

	/**
	 * Standard Constructor
	 *
	 * @param aTarget
	 */
	public MakeVisibleAction(Component aTarget)
	{
		refTarget = aTarget;
	}

	/**
	 * Returns the reference target.
	 */
	public Component getTarget()
	{
		return refTarget;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		refTarget.setVisible(true);
	}

}
