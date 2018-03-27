package glum.gui.unit;

import java.awt.Component;
import java.util.TimeZone;

import javax.swing.DefaultListCellRenderer;
import javax.swing.JLabel;
import javax.swing.JList;

public class TimeZoneCellRenderer extends DefaultListCellRenderer
{
	public TimeZoneCellRenderer()
	{
		super();
	}

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object aObj, int index, boolean isSelected, boolean hasFocus)
	{
		JLabel retL;
		String aStr;

		retL = (JLabel)super.getListCellRendererComponent(list, aObj, index, isSelected, hasFocus);
		if (aObj instanceof TimeZone)
		{
			aStr = ((TimeZone)aObj).getID();
			retL.setText(aStr);
		}

		return retL;
	}

}
