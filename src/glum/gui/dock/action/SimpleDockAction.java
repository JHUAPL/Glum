package glum.gui.dock.action;

import java.awt.event.ActionEvent;

import javax.swing.Action;
import javax.swing.Icon;

import bibliothek.gui.Dockable;
import bibliothek.gui.dock.action.actions.SimpleButtonAction;

/**
 * An DockAction that will fire trigger an embedded java.awt Action
 */
public class SimpleDockAction extends SimpleButtonAction
{
	protected Action refAction;
	
	public SimpleDockAction(Action aAction, String aText, Icon aIcon)
	{
		refAction = aAction;
		
		setText(aText);
		setIcon(aIcon);
	}
	
	@Override
	public void action(Dockable dockable)
	{
		super.action(dockable);
		
		refAction.actionPerformed(new ActionEvent(this, 0, "SimpleDockAction"));
	}
}
