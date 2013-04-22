package glum.gui.action;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

public class MakeVisibleAction extends AbstractAction
{
	// State vars
	protected Component target;

	public MakeVisibleAction(Component aTarget)
	{
		target = aTarget;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		target.setVisible(true);
	}

}
