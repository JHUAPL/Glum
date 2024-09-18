// Copyright (C) 2024 The Johns Hopkins University Applied Physics Laboratory LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package glum.gui.component;

import java.awt.event.ActionListener;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.Document;
import javax.swing.text.PlainDocument;

/**
 * User interface input used to capture an input string.
 * <p>
 * Unlike JTextField, users of this class should not use getText() / setText() but rather getValue() / setValue()
 * methods. Also it should not be necessary to register DocumentListeners - rather an ActionListener should be
 * sufficient.
 *
 * @author lopeznr1
 */
public class GTextField extends GBaseTextField implements DocumentListener
{
	/**
	 * Standard Constructor
	 *
	 * @param aListener
	 *        An ActionListener that will be notified when ever the user makes any input changes.
	 * @param aLabel
	 *        The text value to use as the initial input.
	 * @param aHint
	 *        The hint to show when no input has been entered.
	 */
	public GTextField(ActionListener aListener, String aLabel, String aHint)
	{
		super(aLabel, 0);

		if (aListener != null)
			addActionListener(aListener);

		// Form the appropriate Document
		Document doc;
		doc = new PlainDocument();
		super.setDocument(doc);

		// Register for events of interest
		doc.addDocumentListener(this);

		if (aHint != null)
			setHint(aHint);

		forceTF(aLabel);
	}

	/**
	 * Simplified Constructor
	 *
	 * @param aListener
	 *        An ActionListener that will be notified when ever the user makes any input changes.
	 * @param aLabel
	 *        The text value to use as the initial input.
	 */
	public GTextField(ActionListener aListener, String aLabel)
	{
		this(aListener, aLabel, null);
	}

	/**
	 * Simplified Constructor
	 *
	 * @param aListener
	 *        An ActionListener that will be notified when ever the user makes any input changes.
	 */
	public GTextField(ActionListener aListener)
	{
		this(aListener, "", null);
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
