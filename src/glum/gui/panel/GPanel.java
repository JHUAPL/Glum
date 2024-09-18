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
package glum.gui.panel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.LinkedHashSet;
import java.util.Set;

import javax.swing.JPanel;

import glum.gui.panel.generic.GenericCodes;

/**
 * JPanel that supports registration of {@link ActionListener}s. Derived classes will be responsible for determining
 * when an {@link ActionEvent} should be fired.
 *
 * @author lopeznr1
 */
public class GPanel extends JPanel implements GenericCodes
{
	// State vars
	protected Set<ActionListener> myListenerS;

	/** Standard Constructor */
	public GPanel()
	{
		myListenerS = new LinkedHashSet<>();
	}

	/**
	 * Add an {@link ActionListener} from this panel.
	 */
	public synchronized void addActionListener(ActionListener aListener)
	{
		// Insanity check
		if (aListener == null)
			throw new RuntimeException("Listener should not be null.");

		myListenerS.add(aListener);
	}

	/**
	 * Remove an {@link ActionListener} from this panel.
	 */
	public synchronized void delActionListener(ActionListener aListener)
	{
		myListenerS.remove(aListener);
	}

	/**
	 * Send out notification to all of the {@link ActionListener}s.
	 */
	public void notifyListeners(Object aSource, int aId, String aCommand)
	{
		// Get a copy of the current set of listeners
		Set<ActionListener> tmpListenerS;
		synchronized (this)
		{
			tmpListenerS = new LinkedHashSet<>(myListenerS);
		}

		// Notify our listeners
		for (ActionListener aListener : tmpListenerS)
			aListener.actionPerformed(new ActionEvent(aSource, aId, aCommand));
	}

	/**
	 * Send out notification to all of the {@link ActionListener}s.
	 */
	public void notifyListeners(Object aSource, int aId)
	{
		// Delegate
		notifyListeners(aSource, aId, "");
	}

	/**
	 * Send out notification to all of the {@link ActionListener}s.
	 */
	public void notifyListeners(Object aSource)
	{
		// Delegate
		notifyListeners(aSource, 0, "");
	}
}
