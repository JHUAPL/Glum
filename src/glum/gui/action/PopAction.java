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
import java.util.Collection;
import java.util.List;

import javax.swing.AbstractAction;
import javax.swing.JMenuItem;

import com.google.common.collect.ImmutableList;

/**
 * Base action specific to popup menus.
 * <p>
 * Whenever the list of selected objects changes this PopAction will be notified.
 *
 * @author lopeznr1
 */
public abstract class PopAction<G1> extends AbstractAction
{
	// State vars
	private ImmutableList<G1> itemL;

	/**
	 * Standard Constructor
	 */
	public PopAction()
	{
		itemL = ImmutableList.of();
	}

	/**
	 * Notification that the {@link PopAction} should be executed on the specified items.
	 *
	 * @param aItemL
	 */
	public abstract void executeAction(List<G1> aItemL);

	/**
	 * Sets in the items that are currently selected.
	 */
	public void setChosenItems(Collection<G1> aItemC, JMenuItem aAssocMI)
	{
		itemL = ImmutableList.copyOf(aItemC);
	}

	@Override
	public void actionPerformed(ActionEvent aAction)
	{
		executeAction(itemL);
	}

}
