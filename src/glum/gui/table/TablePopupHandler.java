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
package glum.gui.table;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

import javax.swing.JPopupMenu;

import glum.item.ItemManager;

/**
 * Object that provides the logic used to determine when a {@link JPopupMenu} should be displayed.
 * <p>
 * In order for a popup menu to be shown at least one item must be selected in provided refManager. When a popup menu is
 * triggered the current selection will not be changed.
 *
 * @author lopeznr1
 */
public class TablePopupHandler extends MouseAdapter
{
	// Ref vars
	private final ItemManager<?> refManager;
	private final JPopupMenu refPopupMenu;

	/**
	 * Standard Constructor
	 */
	public TablePopupHandler(ItemManager<?> aManager, JPopupMenu aPopupMenu)
	{
		refManager = aManager;
		refPopupMenu = aPopupMenu;
	}

	@Override
	public void mousePressed(MouseEvent aEvent)
	{
		maybeShowPopup(aEvent);
	}

	@Override
	public void mouseReleased(MouseEvent aEvent)
	{
		maybeShowPopup(aEvent);
	}

	/**
	 * Helper method to handle the showing of the table popup menu.
	 */
	private void maybeShowPopup(MouseEvent aEvent)
	{
		// Bail if this is not a valid popup action
		if (aEvent.isPopupTrigger() == false)
			return;

		// Bail if no items are selected
		if (refManager.getSelectedItems().isEmpty() == true)
			return;

		refPopupMenu.show(aEvent.getComponent(), aEvent.getX(), aEvent.getY());
	}

}
