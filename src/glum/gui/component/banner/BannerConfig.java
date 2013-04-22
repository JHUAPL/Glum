package glum.gui.component.banner;

import java.awt.*;

public class BannerConfig
{
	// State vars
	public String refName;

	public Color bgColor, fgColor;
	public Color borderColor;
	public int borderWidth, borderPad;

	public Font font;
	public int numRepeats;
	public String bannerMsg;

	/**
	 * Constructor
	 */
	public BannerConfig()
	{
		refName = "DEFAULT";

		bgColor = Color.BLACK;
		fgColor = Color.WHITE;
		borderColor = null;
		borderWidth = 0;
		borderPad = 0;

		font = new Font("Serif", Font.PLAIN, 12);
		numRepeats = 1;
		bannerMsg = "Banner";
	}

}
