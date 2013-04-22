package glum.gui.icon;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.swing.plaf.metal.MetalLookAndFeel;

public class DeleteIcon extends BaseIcon
{
	public DeleteIcon(int aDim)
	{
		super(aDim);
	}

	@Override
	public void paintIcon(Component aComp, Graphics g, int x, int y)
	{
		Graphics2D g2d;
		BasicStroke aStroke;
		int w, h;
		int hW, hH, dX, dY;
		int shrinkSize;
		float strokeW;
		
		w = getIconWidth();
		h = getIconHeight();
		hW = w / 2;
		hH = h / 2;
		
		g2d = (Graphics2D)g;

		g2d.setColor(Color.RED.darker());
		if (aComp.isEnabled() == false)
			g2d.setColor(MetalLookAndFeel.getControlDisabled());
	
		shrinkSize = 2;
		if (IconUtil.isPressed(aComp) == true)
			shrinkSize += 2;
		
		g2d.translate(x + hW, y + hH);
		
		
		strokeW = getIconWidth() / 7.0f;
		aStroke = new BasicStroke(strokeW, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND);

		g2d.setStroke(aStroke);
		
		dX = hW - shrinkSize;
		dY = hH - shrinkSize;
		g2d.drawLine(-dX,-dY, +dX,+dY);
		g2d.drawLine(-dX,+dY, +dX,-dY);
		
		g2d.translate(-(x + hW), -(y + hH));
	}

}
