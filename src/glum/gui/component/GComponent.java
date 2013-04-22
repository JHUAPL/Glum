package glum.gui.component;

import glum.gui.panel.generic.GenericCodes;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;
import javax.swing.JComponent;

import com.google.common.collect.Lists;

public class GComponent extends JComponent implements GenericCodes
{
	// State vars
	protected List<ActionListener> myListeners;

	public GComponent()
	{
		super();
		
		myListeners = Lists.newLinkedList();
	}
	
	/**
	 * Add an ActionListener to this GPanel
	 */
	public void addActionListener(ActionListener aListener)
	{
		myListeners.add(aListener);
	}

	/**
	 * Remove an ActionListener to this GPanel
	 */
	public void removeActionListener(ActionListener aListener)
	{
		myListeners.remove(aListener);
	}
	
	/**
	 * Send out notification to all of the ActionListeners
	 */
	public void notifyListeners(Object aSource, int aId, String aCommand)
	{
		for (ActionListener aListener : myListeners)
			aListener.actionPerformed(new ActionEvent(aSource, aId, aCommand));
	}
	
}