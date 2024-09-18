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
