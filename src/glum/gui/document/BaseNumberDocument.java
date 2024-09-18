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

import javax.swing.JTextField;

/**
 * Implementation of {@link BaseDocument} specific for numerical input.
 *
 * @author lopeznr1
 */
public abstract class BaseNumberDocument extends BaseDocument
{
	// State vars
	protected double minVal, maxVal;
	protected boolean formalizeDoc;

	/** Standard Constructor */
	public BaseNumberDocument(JTextField aOwner, double aMinVal, double aMaxVal)
	{
		super(aOwner);

		setMinMaxValue(aMinVal, aMaxVal);
		formalizeDoc = false;
	}

	/**
	 * Updates the new range of valid numbers.
	 */
	public void setMinMaxValue(double aMinVal, double aMaxVal)
	{
		minVal = aMinVal;
		maxVal = aMaxVal;

		// Insanity check
		if (minVal >= maxVal)
			throw new RuntimeException("Illogical range. Range: [" + minVal + "," + maxVal + "]");
	}

	@Override
	public void formalizeInput()
	{
		String currStr;

		// Insanity check
		if (ownerTF == null)
			return;

		// Is formalization required
		if (formalizeDoc == false)
			return;

		// Disassociate ourselves from event handling
		ownerTF.removeActionListener(this);
		ownerTF.removeFocusListener(this);

		currStr = ownerTF.getText();
		ownerTF.setText(currStr);

		// Reassociate ourselves from event handling
		ownerTF.addActionListener(this);
		ownerTF.addFocusListener(this);
	}

}
