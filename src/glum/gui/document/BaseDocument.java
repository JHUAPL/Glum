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
package glum.gui.document;

import java.awt.event.*;

import javax.swing.JTextField;
import javax.swing.text.PlainDocument;

public abstract class BaseDocument extends PlainDocument implements ActionListener, FocusListener
{
	// State vars
	protected JTextField ownerTF;

	/** Standard Constructor */
	public BaseDocument(JTextField aOwnerTF)
	{
		ownerTF = aOwnerTF;
	}

	/**
	 * Get the owner of this Document model
	 * <p>
	 * TODO: This method should no longer be needed.
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
