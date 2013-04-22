package glum.gui.dock.action;

import javax.swing.Icon;

import bibliothek.gui.Dockable;
import bibliothek.gui.dock.action.actions.SimpleButtonAction;

/**
 * An DockAction that will fire trigger an embedded java.awt Action
 */
public class ToggleAction extends SimpleButtonAction
{
	// State vars
	protected boolean isActive;
	
	// Gui vars
	protected Icon falseIcon, trueIcon;
	
	public ToggleAction(String aText, Icon aFalseIcon, Icon aTrueIcon, boolean aIsActive)
	{
		super();
		
		isActive = aIsActive;
		
		falseIcon = aFalseIcon;
		trueIcon = aTrueIcon;

		setText(aText);
		updateGui();
	}

	/**
	 * Accessor methods
	 */
	public boolean getIsActive()
	{
		return isActive;
	}
	
	public void setIsActive(boolean aBool)
	{
		isActive = aBool;
		updateGui();
	}
	
	@Override
	public void action(Dockable aDockable)
	{
		isActive = !isActive;
		updateGui();
		
		super.action(aDockable);
	}

	/**
	 * Utility method 
	 */
	private void updateGui()
	{
		if (isActive == true)
			setIcon(trueIcon);
		else
			setIcon(falseIcon);
	}

}
