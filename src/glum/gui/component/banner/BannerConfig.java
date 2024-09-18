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
