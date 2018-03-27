package glum.gui.document;

import javax.swing.text.*;
import javax.swing.JTextField;

import glum.gui.GuiUtil;

public class NumberDocument extends BaseNumberDocument
{
	protected boolean allowFloats;
//	protected NumberUnit myUnit;	

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

		// Ensure we do not exceed number of columns
		if (numAvailCols > 0)
		{
			if (offs + str.length() >= numAvailCols)
				throw new BadLocationException("Too many characters to insert.", offs);
		}

		// Form the resultant string
		bStr = "";
		eStr = "";
		if (offs > 0)
			bStr = getText(0, offs);
		eStr = getText(offs, getLength() - offs);
		resultStr = bStr + str + eStr;

		// Ensure the resultant is sensical
		aVal = GuiUtil.readDouble(resultStr, Double.NaN);
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
