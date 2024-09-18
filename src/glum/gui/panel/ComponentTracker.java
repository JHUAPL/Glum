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

import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.Arrays;
import java.util.List;

/**
 * Worker class to allow a component to track other Component so it can keep it's properties synchronized with the
 * tracked Components.
 *
 * @author lopeznr1
 */
public class ComponentTracker implements ComponentListener
{
	private Component targComp;
	private Component trkHiddenComp;
	private Component trkMovedComp;
	private Component trkResizedComp;
	private Component trkShownComp;

	public ComponentTracker(Component aTargComp)
	{
		targComp = aTargComp;
		trkHiddenComp = null;
		trkMovedComp = null;
		trkResizedComp = null;
		trkShownComp = null;
	}

	/**
	 * Track aComp so that if it is hidden, then the reference targetComponent will be hidden.
	 */
	public void setHiddenTracker(Component aComp)
	{
		// Deregister from the old trkShownComp
		if (trkHiddenComp != null)
			trkHiddenComp.removeComponentListener(this);

		trkHiddenComp = aComp;
		updateListenersForTrackedComponents();
	}

	/**
	 * Track aComp so that if it is moved, then the reference targetComponent will be moved.
	 */
	public void setMovedTracker(Component aComp)
	{
		// Deregister from the old trkShownComp
		if (trkMovedComp != null)
			trkMovedComp.removeComponentListener(this);

		trkMovedComp = aComp;
		updateListenersForTrackedComponents();
	}

	/**
	 * Track aComp so that if it is resized, then the reference targetComponent will be resized.
	 */
	public void setResizedTracker(Component aComp)
	{
		// Deregister from the old trkShownComp
		if (trkResizedComp != null)
			trkResizedComp.removeComponentListener(this);

		trkResizedComp = aComp;
		updateListenersForTrackedComponents();
	}

	/**
	 * Track aComp so that if it is shown, then the reference targetComponent will be shown.
	 */
	public void setShownTracker(Component aComp)
	{
		// Deregister from the old trkShownComp
		if (trkShownComp != null)
			trkShownComp.removeComponentListener(this);

		trkShownComp = aComp;
		updateListenersForTrackedComponents();
	}

	@Override
	public void componentHidden(ComponentEvent aEvent)
	{
		if (aEvent.getComponent() == trkHiddenComp)
			targComp.setVisible(false);
	}

	@Override
	public void componentMoved(ComponentEvent aEvent)
	{
		if (aEvent.getComponent() == trkMovedComp)
			targComp.setLocation(trkMovedComp.getLocation());
	}

	@Override
	public void componentResized(ComponentEvent aEvent)
	{
		if (aEvent.getComponent() == trkResizedComp)
		{
			targComp.setSize(trkResizedComp.getSize());
			targComp.validate();
		}
	}

	@Override
	public void componentShown(ComponentEvent aEvent)
	{
		if (aEvent.getComponent() == trkShownComp)
			targComp.setVisible(true);
	}

	/**
	 * Utility method to ensure that we are registered (Component events) for all component which are being tracked. Note
	 * that at most we will register only only once for each unique Component.
	 */
	protected void updateListenersForTrackedComponents()
	{
		List<ComponentListener> listenerL;

		if (trkHiddenComp != null)
		{
			listenerL = Arrays.asList(trkHiddenComp.getComponentListeners());
			if (listenerL.contains(this) == false)
				trkHiddenComp.addComponentListener(this);
		}

		if (trkMovedComp != null)
		{
			listenerL = Arrays.asList(trkMovedComp.getComponentListeners());
			if (listenerL.contains(this) == false)
				trkMovedComp.addComponentListener(this);
		}

		if (trkResizedComp != null)
		{
			listenerL = Arrays.asList(trkResizedComp.getComponentListeners());
			if (listenerL.contains(this) == false)
				trkResizedComp.addComponentListener(this);
		}

		if (trkShownComp != null)
		{
			listenerL = Arrays.asList(trkShownComp.getComponentListeners());
			if (listenerL.contains(this) == false)
				trkShownComp.addComponentListener(this);
		}
	}

}
