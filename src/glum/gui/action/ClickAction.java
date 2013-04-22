package glum.gui.action;

import java.awt.event.*;
import javax.swing.*;

public class ClickAction extends AbstractAction
{
	// State vars
	protected AbstractButton target;

	public ClickAction(AbstractButton aTarget)
	{
		target = aTarget;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		target.doClick();
	}

}
