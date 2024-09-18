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
import java.util.*;

import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import glum.item.ItemManager;

/**
 * UI component that allows a custom popup menu to be built.
 *
 * @author lopeznr1
 */
public class PopupMenu<G1> extends JPopupMenu
{
	// Reference vars
	private ItemManager<G1> refManager;

	// State vars
	private Map<JMenuItem, PopAction<G1>> actionM;

	/**
	 * Standard Constructor
	 *
	 * @param aManager
	 */
	public PopupMenu(ItemManager<G1> aManager)
	{
		refManager = aManager;

		actionM = new HashMap<>();
	}

	/**
	 * Registers the specified {@link PopAction} into this {@link PopupMenu}.
	 * <p>
	 * A simple menu item will be created and associated with the specified action.
	 */
	public void installPopAction(PopAction<G1> aAction, String aTitle)
	{
		JMenuItem tmpMI = new JMenuItem(aAction);
		tmpMI.setText(aTitle);

		// Delegate
		installPopAction(aAction, tmpMI);
	}

	/**
	 * Registers the specified {@link PopAction} into this {@link PopupMenu}.
	 * <p>
	 * The action will be associated with the specified menu item.
	 */
	public void installPopAction(PopAction<G1> aAction, JMenuItem aTargMI)
	{
		add(aTargMI);
		actionM.put(aTargMI, aAction);
	}

	@Override
	public void show(Component aParent, int aX, int aY)
	{
		// Bail if we do not have selected items
		Set<G1> tmpS = refManager.getSelectedItems();
		if (tmpS.size() == 0)
			return;

		// Update our PopActions
		for (JMenuItem aMI : actionM.keySet())
		{
			PopAction<G1> tmpPA = actionM.get(aMI);
			tmpPA.setChosenItems(tmpS, aMI);
		}

		// Delegate
		super.show(aParent, aX, aY);
	}

}
