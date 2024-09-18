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

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.AbstractButton;

/**
 * Action used to "click" on the target {@link AbstractButton}.
 * <p>
 * Clicking will be done via the {@link AbstractButton#doClick()} method.
 *
 * @author lopeznr1
 */
public class ClickAction extends AbstractAction
{
	// Reference vars
	private final AbstractButton refTarget;

	public ClickAction(AbstractButton aTarget)
	{
		refTarget = aTarget;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		refTarget.doClick();
	}

}
