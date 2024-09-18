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
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

import glum.io.ParseUtil;

/**
 * Implementation of {@link BaseNumberDocument}.
 *
 * @author lopeznr1
 */
public class NumberDocument extends BaseNumberDocument
{
	// Constants
	private final String ValidPosDigitStr = "0123456789";
	private final String ValidNegDigitStr = "+-0123456789";
	private final String ValidFractStr = "+-0123456789e.";

	protected boolean allowFloats;
//	protected NumberUnit myUnit;

	/** Standard Constructor */
	public NumberDocument(JTextField aOwnerTF, boolean aFormalizeDoc)
	{
		super(aOwnerTF, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY);

		allowFloats = true;

		formalizeDoc = aFormalizeDoc;
	}

	/**
	 * Sets in whether floating point numbers are allowed
	 */
	public void setAllowFloats(boolean aBool)
	{
		allowFloats = aBool;
	}

	@Override
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
	{
		String bStr, eStr, resultStr;
		double aVal;
		char aChar;

		// Insanity check
		if (str == null)
			return;

		// Change to allow user to enter any valid single character to be input. Note that the
		// NumberDocument is used primary by GNumberField and as such invalid input will be
		// colored with the "fail" color.
		if (str.length() == 1)
		{
			if (allowFloats == false && minVal >= 0 && ValidPosDigitStr.contains(str) == true)
			{
				super.insertString(offs, str, a);
				return;
			}
			else if (allowFloats == false && minVal < 0 && ValidNegDigitStr.contains(str) == true)
			{
				super.insertString(offs, str, a);
				return;
			}
			else if (allowFloats == true && ValidFractStr.contains(str) == true)
			{
				super.insertString(offs, str, a);
				return;
			}
		}

		// Special cases
		aChar = str.charAt(0);
		if (offs == 0)
		{
			// Reject if we need a positive number
			if (aChar == '-' && minVal >= 0)
			{
				throw new BadLocationException("Negative values are not allowed.", offs);
			}

			// Potential valid string that starts off an int
			if ((aChar == '-') && str.length() == 1)
			{
				super.insertString(offs, str, a);
				return;
			}

			// Potential valid string that starts off a double
			if ((aChar == '.') && str.length() == 1 && allowFloats == true)
			{
				super.insertString(offs, str, a);
				return;
			}
		}
		else if (offs == 1 && str.length() == 1 && aChar == '.')
		{
			// Potential valid string that starts off a double
			if ("-".equals(getText(0, offs)) == true && allowFloats == true)
			{
				super.insertString(offs, str, a);
				return;
			}
		}

		// Reject if we detect a floating point, but it is not allowed
		if (str.contains(".") == true && allowFloats == false)
			throw new BadLocationException("Only integers are allowed.", offs);

		// Form the resultant string
		bStr = "";
		eStr = "";
		if (offs > 0)
			bStr = getText(0, offs);
		eStr = getText(offs, getLength() - offs);
		resultStr = bStr + str + eStr;

		// Ensure the resultant is sensical
		aVal = ParseUtil.readDouble(resultStr, Double.NaN);
		if (Double.isNaN(aVal) == true)
			throw new BadLocationException("Nonsensical number.", offs);

		// Ensure that the number is in range
		if (aVal > maxVal)
			throw new BadLocationException("Out of numerical range.", offs);
		else if (aVal < minVal && resultStr.length() >= ("" + maxVal).length())
			throw new BadLocationException("Out of numerical range.", offs);

		// Insert the string
		super.insertString(offs, str, a);
	}

}
