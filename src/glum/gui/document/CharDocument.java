package glum.gui.document;

import java.util.Set;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

import com.google.common.collect.Sets;

/**
 * Specialized Document designed to accept only the specified input chars
 */
public class CharDocument extends BaseDocument
{
	private Set<Character> validSet;

	public CharDocument(JTextField aOwner, String validCharStr)
	{
		this(aOwner, validCharStr, true);
	}

	public CharDocument(JTextField aOwner, String validCharStr, boolean isCaseSensitive)
	{
		super(aOwner);
	
		validSet = Sets.newHashSet();
		for (int c1 = 0; c1 < validCharStr.length(); c1++)
		{
			validSet.add(validCharStr.charAt(c1));
			if (isCaseSensitive == false)
			{
				validSet.add(Character.toLowerCase(validCharStr.charAt(c1)));
				validSet.add(Character.toUpperCase(validCharStr.charAt(c1)));
			}
		}
	}

	@Override
	public void formalizeInput()
	{
		; // Nothing to do
	}
	
	@Override
	public void insertString(int offs, String str, AttributeSet a) throws BadLocationException
	{
		char aChar;

		// Insanity check
		if (str == null)
			return;
		
		// Ensure all of the characters in str are in the valid set
		for (int c1 = 0; c1 < str.length(); c1++)
		{
			aChar = str.charAt(c1);
			if (validSet.contains(aChar) == false)
				throw new BadLocationException("Invalid character: " + aChar, offs);
		}
		
		super.insertString(offs, str, a);
	}

}
