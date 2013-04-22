package glum.gui.misc;

import java.awt.*;

public enum MultiState
{
	Checked("Checked", "MultiState.Checked")
	{
		@Override
		public void render(Graphics g, int x, int y, int iconSize)
		{
			g.fillRect(x + 3, y + 5, 2, iconSize - 8);
			g.drawLine(x + (iconSize - 4), y + 3, x + 5, y + (iconSize - 6));
			g.drawLine(x + (iconSize - 4), y + 4, x + 5, y + (iconSize - 5));
		}
	},

	Crossed("Crossed", "MultiState.Crossed")
	{
		@Override
		public void render(Graphics g, int x, int y, int iconSize)
		{
			g.drawLine(x + (iconSize - 4), y + 2, x + 3, y + (iconSize - 5));
			g.drawLine(x + (iconSize - 4), y + 3, x + 3, y + (iconSize - 4));
			g.drawLine(x + 3, y + 2, x + (iconSize - 4), y + (iconSize - 5));
			g.drawLine(x + 3, y + 3, x + (iconSize - 4), y + (iconSize - 4));
		}
	},

	Mixed("Mixed", "MultiState.Mixed")
	{
		@Override
		public void render(Graphics g, int x, int y, int iconSize)
		{
			int cX, cY;

			cX = x + iconSize / 2;
			cY = y + iconSize / 2;

			g.drawOval(cX - 2, cY - 2, 4, 4);
			g.drawOval(cX - 3, cY - 3, 6, 6);
		}
	},

	None("None", "MultiState.None")
	{
		@Override
		public void render(Graphics g, int x, int y, int iconSize)
		{
			; // Nothing to render
		}
	};

	// Vars
	private final String userStr;
	private final String referenceName;

	/**
	 * Constructor
	 */
	MultiState(String aUserStr, String aReferenceName)
	{
		userStr = aUserStr;
		referenceName = aReferenceName;
	}

	/**
	 * getReferenceName
	 */
	public String getReferenceName()
	{
		return referenceName;
	}

	/**
	 * Method to draw a representative icon of the state
	 */
	public void render(Graphics g, int x, int y, int iconSize)
	{
		this.render(g, x, y, iconSize);
	}

	@Override
	public String toString()
	{
		return userStr;
	}

}
