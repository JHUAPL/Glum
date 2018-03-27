package glum.gui.panel;

import glum.gui.panel.generic.GenericCodes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.*;

import javax.swing.JPanel;

/**
 * JPanel that supports registration of ActionListeners. Derived classes will be responsible for determining when an ActionEvent should be fired.
 */
public class GPanel extends JPanel implements GenericCodes
{
	// State vars
	protected Set<ActionListener> myListeners;

	public GPanel()
	{
		super();

		myListeners = new LinkedHashSet<>();
	}

	/**
	 * Add an ActionListener to this GPanel
	 */
	public synchronized void addActionListener(ActionListener aListener)
	{
		// Insanity check
		if (aListener == null)
			throw new RuntimeException("Listener should not be null.");

		myListeners.add(aListener);
	}

	/**
	 * Remove an ActionListener to this GPanel
	 */
	public synchronized void removeActionListener(ActionListener aListener)
	{
		myListeners.remove(aListener);
	}

	/**
	 * Send out notification to all of the ActionListeners
	 */
	public void notifyListeners(Object aSource, int aId, String aCommand)
	{
		Set<ActionListener> tmpListeners;

		// Get a copy of the current set of listeners
		synchronized (this)
		{
			tmpListeners = new LinkedHashSet<>(myListeners);
		}

		// Notify our listeners
		for (ActionListener aListener : tmpListeners)
			aListener.actionPerformed(new ActionEvent(aSource, aId, aCommand));
	}

	/**
	 * Send out notification to all of the ActionListeners
	 */
	public void notifyListeners(Object aSource, int aId)
	{
		notifyListeners(aSource, aId, "");
	}
}
