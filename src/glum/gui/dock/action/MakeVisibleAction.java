package glum.gui.dock.action;

import javax.swing.Icon;
import javax.swing.JComponent;

import bibliothek.gui.Dockable;
import bibliothek.gui.dock.action.actions.SimpleButtonAction;

/**
 * A DockAction that will cause the target component to become visible. This action is useful to bring up a
 * configuration panel related to the Dockable.
 */
public class MakeVisibleAction extends SimpleButtonAction
{
	private JComponent targComp;

	public MakeVisibleAction(JComponent aTargetComp, String aText, Icon aIcon)
	{
		targComp = aTargetComp;

		setText(aText);
		setIcon(aIcon);
	}

	@Override
	public void action(Dockable aDockable)
	{
		super.action(aDockable);

		// Make the component visible
		targComp.setVisible(true);
	}
}
