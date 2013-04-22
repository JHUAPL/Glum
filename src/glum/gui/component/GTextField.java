package glum.gui.component;

import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.event.*;
import javax.swing.text.*;

public class GTextField extends JTextField implements DocumentListener
{
	/**
	 * Constructor
	 * 
	 * @param aListener
	 *           : Default ActionListener
	 */
	public GTextField(ActionListener aListener)
	{
		super("", 0);

		if (aListener != null)
			addActionListener(aListener);

		// Form the appropriate Document
		Document doc;
		doc = new PlainDocument();
		super.setDocument(doc);

		// Register for events of interest
		doc.addDocumentListener(this);
	}

	/**
	 * Returns whether the current input is valid
	 */
	public boolean isValidInput()
	{
		if (getText().isEmpty() == true)
			return false;

		return true;
	}

	/**
	 * Returns the currently stored model value
	 */
	public String getValue()
	{
		return getText();
	}

	/**
	 * Sets in the value for the GTextField. Note this method will not trigger an ActionEvent.
	 */
	public void setValue(String aText)
	{
		forceTF(aText);
	}

	@Override
	public void setDocument(Document aDoc)
	{
		// throw new UnsupportedOperationException();
		if (aDoc != null)
			aDoc.addDocumentListener(this);

		super.setDocument(aDoc);
	}

	@Override
	public void changedUpdate(DocumentEvent aEvent)
	{
		fireActionPerformed();
	}

	@Override
	public void insertUpdate(DocumentEvent aEvent)
	{
		fireActionPerformed();
	}

	@Override
	public void removeUpdate(DocumentEvent aEvent)
	{
		fireActionPerformed();
	}

	/**
	 * Updates the internal model value and will update the display wrt tho active unit.
	 */
	protected void forceTF(String aStr)
	{
		Document aDocument;

		// Update the GUI internals
		aDocument = this.getDocument();
		if (aDocument != null)
			aDocument.removeDocumentListener(this);

		setText(aStr);
		setCaretPosition(0);

		if (aDocument != null)
			aDocument.addDocumentListener(this);
	}

	/**
	 * Keeps the "model" value conceptually linked to the GUI component. It will also trigger the actionEventListeners.
	 */
	protected void syncValue(DocumentEvent aEvent)
	{
		fireActionPerformed();
	}

}
