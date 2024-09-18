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

import java.util.HashSet;
import java.util.Set;

import javax.swing.JTextField;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;

/**
 * Specialized Document designed to accept only the specified input chars
 *
 * @author lopeznr1
 */
public class CharDocument extends BaseDocument
{
	private Set<Character> validS;

	/**
	 * Standard Constructor
	 */
	public CharDocument(JTextField aOwner, String aValidCharStr, boolean aIsCaseSensitive)
	{
		super(aOwner);

		validS = new HashSet<>();
		for (int c1 = 0; c1 < aValidCharStr.length(); c1++)
		{
			validS.add(aValidCharStr.charAt(c1));
			if (aIsCaseSensitive == false)
			{
				validS.add(Character.toLowerCase(aValidCharStr.charAt(c1)));
				validS.add(Character.toUpperCase(aValidCharStr.charAt(c1)));
			}
		}
	}

	@Override
	public void formalizeInput()
	{
		; // Nothing to do
	}

	@Override
	public void insertString(int aOffs, String aStr, AttributeSet aAS) throws BadLocationException
	{
		// Insanity check
		if (aStr == null)
			return;

		// Ensure all of the characters in str are in the valid set
		for (int c1 = 0; c1 < aStr.length(); c1++)
		{
			char tmpChar = aStr.charAt(c1);
			if (validS.contains(tmpChar) == false)
				throw new BadLocationException("Invalid character: " + tmpChar, aOffs);
		}

		super.insertString(aOffs, aStr, aAS);
	}

}
