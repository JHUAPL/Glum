package glum.gui.panel;

import java.awt.Component;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.util.List;

import com.google.common.collect.Lists;

/**
 * Utility class to allow a component to track other Component so it can keep it's
 * properties synchronized with the tracked Components.
 */
public class ComponentTracker implements ComponentListener
{
	protected Component targComp;
	protected Component trkHiddenComp;
	protected Component trkMovedComp;
	protected Component trkResizedComp;
	protected Component trkShownComp;
	
	public ComponentTracker(Component aTargComp)
	{
		targComp = aTargComp;
		trkHiddenComp = null;
		trkMovedComp = null;
		trkResizedComp = null;
		trkShownComp = null;
	}
	
	/**
	 * Track aComp so that if it is hidden, then the reference
	 * targetComponent will be hidden.
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
	 * Track aComp so that if it is moved, then the reference
	 * targetComponent will be moved.
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
	 * Track aComp so that if it is resized, then the reference
	 * targetComponent will be resized.
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
	 * Track aComp so that if it is shown, then the reference
	 * targetComponent will be shown.
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
	 * Utility method to ensure that we are registered (Component events) for
	 * all component which are being tracked. Note that at most we will register
	 * only only once for each unique Component.
	 */
	protected void updateListenersForTrackedComponents()
	{
		List<ComponentListener> listenerList;

		if (trkHiddenComp != null)
		{
			listenerList = Lists.newArrayList(trkHiddenComp.getComponentListeners());
			if (listenerList.contains(this) == false)
				trkHiddenComp.addComponentListener(this);
		}
		
		if (trkMovedComp != null)
		{
			listenerList = Lists.newArrayList(trkMovedComp.getComponentListeners());
			if (listenerList.contains(this) == false)
				trkMovedComp.addComponentListener(this);
		}
		
		if (trkResizedComp != null)
		{
			listenerList = Lists.newArrayList(trkResizedComp.getComponentListeners());
			if (listenerList.contains(this) == false)
				trkResizedComp.addComponentListener(this);
		}
		
		if (trkShownComp != null)
		{
			listenerList = Lists.newArrayList(trkShownComp.getComponentListeners());
			if (listenerList.contains(this) == false)
				trkShownComp.addComponentListener(this);
		}
	}
	
}
