package glum.gui.component.banner;

import java.awt.*;
import java.awt.geom.*;
import java.util.*;
import javax.swing.*;

import glum.gui.*;
import glum.io.token.MatchTokenizer;
import glum.io.token.Tokenizer;

public class Banner extends JComponent
{
	// Our configuration
	protected BannerConfig myConfig;

	// Current message to display
	protected String displayMsg;

	/**
	 * Constructor
	 */
	public Banner()
	{
		super();

		myConfig = new BannerConfig();
		setConfig(myConfig);
	}

	@Override
	public void paintComponent(Graphics g)
	{
		Graphics2D g2d;
		Rectangle2D aRect;
		double winW, winH, msgW, msgH;
		double sX, sY, offSetY;

		super.paintComponent(g);
		g2d = (Graphics2D)g;

		// Determine the window boundaries
		winW = getWidth();
		winH = getHeight();

		// Compute the displayMsg boundaries
		aRect = myConfig.font.getStringBounds(displayMsg, g2d.getFontRenderContext());
		msgW = aRect.getWidth();
		msgH = aRect.getHeight();
		offSetY = msgH - aRect.getMaxY();

		// Form our rectangle
		aRect = new Rectangle2D.Double(0, 0, winW, winH);

		// Draw the background
		if (myConfig.bgColor != null)
		{
			g2d.setColor(myConfig.bgColor);
			g2d.fill(aRect);
		}

		// Draw the border
		if (myConfig.borderColor != null && myConfig.borderWidth > 0)
		{
			g2d.setColor(myConfig.borderColor);
			g2d.setStroke(new BasicStroke(myConfig.borderWidth));
			g2d.draw(aRect);
		}

		// Draw the displayMsg
		sX = winW / 2.0 - msgW / 2.0;
		sY = winH / 2.0 - msgH / 2.0;
		g2d.setFont(myConfig.font);
		g2d.setColor(myConfig.fgColor);
		g2d.drawString(displayMsg, (int)sX, (int)(sY + offSetY));
	}

	/**
	 * setConfig - Sets in the new banner attributes
	 */
	public void setConfig(BannerConfig aConfig)
	{
		// Insanity check
		if (aConfig == null)
			return;
		myConfig = aConfig;

		// Build the actual displayMsg
		if (myConfig.numRepeats > 1)
		{
			displayMsg = "";
			for (int c1 = 0; c1 < myConfig.numRepeats; c1++)
				displayMsg += myConfig.bannerMsg;
		}
		else
		{
			displayMsg = myConfig.bannerMsg;
		}

		// Time for a repaint
		repaint();
	}

	/**
	 * Utility method that converts a string to a BannerConfig. Eventually this method class should be
	 * moved to a utility class of sorts.
	 */
	public static BannerConfig readBannerConfig(String strLine)
	{
		BannerConfig aConfig;
		Collection<String> instrSet, parmSet;
		Tokenizer instrTokenizer, parmTokenizer;
		String parms[];

		// Insanity check
		if (strLine == null)
			return null;

		// Build our tokenizers
		instrTokenizer = new MatchTokenizer("[^/]+");
		parmTokenizer = new MatchTokenizer("[^\\,]+");

		// Get the set of embedded instructions
		instrSet = instrTokenizer.getTokens(strLine);

		// Process the instruction
		aConfig = new BannerConfig();
		for (String aInstr : instrSet)
		{
			parmSet = parmTokenizer.getTokens(aInstr);
			parms = parmSet.toArray(new String[] {""});

			if (parms.length == 0)
			{
				; // Nothing to process
			}
			else if (parms[0].equalsIgnoreCase("refName") == true && parms.length == 2)
			{
				aConfig.refName = parms[1];
			}
			else if (parms[0].equalsIgnoreCase("label") == true && parms.length == 2)
			{
				aConfig.bannerMsg = parms[1];
			}
			else if (parms[0].equalsIgnoreCase("font") == true && parms.length >= 2)
			{
				String face;
				int style, size;
				boolean bold, italic;

				face = parms[1];

				size = 12;
				if (parms.length > 2)
					size = GuiUtil.readInt(parms[2], 12);

				bold = false;
				if (parms.length > 3)
					bold = GuiUtil.readBoolean(parms[3], false);

				italic = false;
				if (parms.length > 4)
					italic = GuiUtil.readBoolean(parms[4], false);

				style = 0;
				if (bold == false && italic == false)
					style = Font.PLAIN;
				if (bold == true)
					style = Font.BOLD;
				if (italic == true)
					style |= Font.ITALIC;

				aConfig.font = new Font(face, style, size);
			}
			else if (parms[0].equalsIgnoreCase("fgColor") == true && parms.length == 4)
			{
				int r, g, b;

				r = GuiUtil.readRangeInt(parms[1], 0, 255, 255);
				g = GuiUtil.readRangeInt(parms[2], 0, 255, 255);
				b = GuiUtil.readRangeInt(parms[3], 0, 255, 255);
				aConfig.fgColor = new Color(r, g, b);
			}
			else if (parms[0].equalsIgnoreCase("bgColor") == true && parms.length >= 4)
			{
				int r, g, b, a;

				r = GuiUtil.readRangeInt(parms[1], 0, 255, 255);
				g = GuiUtil.readRangeInt(parms[2], 0, 255, 255);
				b = GuiUtil.readRangeInt(parms[3], 0, 255, 255);

				a = 255;
				if (parms.length > 4)
					a = GuiUtil.readRangeInt(parms[4], 0, 255, 255);

				aConfig.bgColor = new Color(r, g, b, a);
			}
			else if (parms[0].equalsIgnoreCase("border") == true && parms.length >= 4)
			{
				int r, g, b;

				r = GuiUtil.readRangeInt(parms[1], 0, 255, 255);
				g = GuiUtil.readRangeInt(parms[2], 0, 255, 255);
				b = GuiUtil.readRangeInt(parms[3], 0, 255, 255);
				aConfig.borderColor = new Color(r, g, b);

				if (parms.length > 4)
					aConfig.borderWidth = GuiUtil.readRangeInt(parms[4], 0, 10, 0);

				if (parms.length > 5)
					aConfig.borderPad = GuiUtil.readRangeInt(parms[5], -20, 20, 0);
			}
			else if (parms[0].equalsIgnoreCase("repeatMsg") == true && parms.length == 2)
			{
				aConfig.numRepeats = GuiUtil.readRangeInt(parms[1], -1, 100, 0);
			}
		}

		return aConfig;
	}

}
