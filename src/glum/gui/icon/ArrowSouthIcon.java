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
package glum.gui.icon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Polygon;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class ArrowSouthIcon extends BaseIcon
{
	/**
	 * Constructor
	 */
	public ArrowSouthIcon(int aDim)
	{
		super(aDim);
	}
	
	@Override
	public void paintIcon(Component aComp, Graphics g, int x, int y)
	{
		Polygon aPolygon;
		int w, h;
		int hW, hH;
		
		w = getIconWidth();
		h = getIconHeight();
		hW = w / 2;
		hH = h / 2;
		
		g.setColor(Color.BLACK);
		if (aComp.isEnabled() == false)
			g.setColor(MetalLookAndFeel.getControlDisabled());
		
		if (IconUtil.isPressed(aComp) == true)
			y += 2;
		
		g.translate(x + hW, y + hH);
		
		aPolygon = new Polygon();
		aPolygon.addPoint(0, +hH);
		aPolygon.addPoint(+hW, 0);
		
		aPolygon.addPoint(+hW/4, 0);
		aPolygon.addPoint(+hW/4, -hH);
		aPolygon.addPoint(-hW/4, -hH);
		aPolygon.addPoint(-hW/4, 0);
		
		aPolygon.addPoint(-hW, 0);
		
		g.fillPolygon(aPolygon);
		
		g.translate(-(x + hW), -(y + hH));
	}

}
