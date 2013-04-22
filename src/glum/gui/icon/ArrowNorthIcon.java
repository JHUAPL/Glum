package glum.gui.icon;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Polygon;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class ArrowNorthIcon extends BaseIcon
{
	/**
	 * Constructor
	 */
	public ArrowNorthIcon(int aDim)
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
		aPolygon.addPoint(0, -hH);
		aPolygon.addPoint(-hW, 0);
		
		aPolygon.addPoint(-hW/4, 0);
		aPolygon.addPoint(-hW/4, hH);
		aPolygon.addPoint(+hW/4, hH);
		aPolygon.addPoint(+hW/4, 0);
		
		aPolygon.addPoint(+hW, 0);
		
		g.fillPolygon(aPolygon);
		
		g.translate(-(x + hW), -(y + hH));
	}

}
