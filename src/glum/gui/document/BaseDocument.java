package glum.gui.document;

import java.awt.event.*;
import javax.swing.text.*;
import javax.swing.JTextField;

public abstract class BaseDocument extends PlainDocument implements ActionListener, FocusListener
{
	// State vars
	protected JTextField ownerTF;

	public BaseDocument(JTextField aOwnerTF)
	{
		super();

		ownerTF = aOwnerTF;
	}

	/**
	 * Get the owner of this Document model
	 * Todo: This method should no longer be needed.
	 */
	public JTextField getOwner()
	{
		return ownerTF;
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		formalizeInput();
	}

	@Override
	public void focusGained(FocusEvent e)
	{
	}

	@Override
	public void focusLost(FocusEvent e)
	{
		formalizeInput();
	}

	/**
	 * Updates the text to reflect a formal output
	 */
	public abstract void formalizeInput();

}
