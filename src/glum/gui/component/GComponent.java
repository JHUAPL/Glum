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
package glum.gui.component;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import glum.gui.panel.generic.GenericCodes;

/**
 * Base component that provides support for {@link ActionListener} event mechanism.
 *
 * @author lopeznr1
 */
public class GComponent extends JComponent implements GenericCodes
{
	// State vars
	protected List<ActionListener> listenerL;

	/** Standard Constructor */
	public GComponent()
	{
		listenerL = new ArrayList<>();
	}

	/**
	 * Add an ActionListener to this GPanel
	 */
	public void addActionListener(ActionListener aListener)
	{
		listenerL.add(aListener);
	}

	/**
	 * Remove an ActionListener to this GPanel
	 */
	public void removeActionListener(ActionListener aListener)
	{
		listenerL.remove(aListener);
	}

	/**
	 * Send out notification to all of the ActionListeners
	 */
	public void notifyListeners(Object aSource, int aId, String aCommand)
	{
		for (var aListener : listenerL)
			aListener.actionPerformed(new ActionEvent(aSource, aId, aCommand));
	}

}
