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

import java.awt.*;

import javax.swing.JTextField;

/**
 * User interface input used to capture an input string.
 * <p>
 * This object provides the following additional features:
 * <ul>
 * <li>Display of passive hint text
 * </ul>
 *
 * @author lopeznr1
 */
public class GBaseTextField extends JTextField
{
	// State vars
	private String mHint;

	/** Standard Constructor */
	public GBaseTextField(String aText, int aNumColumns)
	{
		super(aText, aNumColumns);

		mHint = null;
	}

	/**
	 * Sets a hint that will be shown whenever the text field is empty.
	 */
	public void setHint(String aHint)
	{
		mHint = aHint;
		repaint();
	}

	@Override
	public void paint(Graphics g)
	{
		super.paint(g);

		// Bail if there is already input
		if (getText().length() != 0)
			return;

		// Bail if there is no hint
		if (mHint == null || mHint.length() == 0)
			return;

		// Draw the textual hint
		// Source:
		// https://stackoverflow.com/questions/1738966/java-jtextfield-with-input-hint
		int h = getHeight();
		((Graphics2D) g).setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
		Insets ins = getInsets();
		FontMetrics fm = g.getFontMetrics();
		int c0 = getBackground().getRGB();
		int c1 = getForeground().getRGB();
		int m = 0xfefefefe;
		int c2 = ((c0 & m) >>> 1) + ((c1 & m) >>> 1);
		g.setColor(new Color(c2, true));
		g.drawString(mHint, ins.left, h / 2 + fm.getAscent() / 2 - 2);
	}

}
