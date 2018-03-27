package glum.gui.unit;

import java.awt.Component;
import javax.swing.*;

import glum.unit.Unit;

public class UnitLabelRenderer extends DefaultListCellRenderer
{
	public UnitLabelRenderer()
	{
		super();
	}

	@Override
	public Component getListCellRendererComponent(JList<?> list, Object aObj, int index, boolean isSelected, boolean hasFocus)
	{
		JLabel retL;
		String aStr;

		retL = (JLabel)super.getListCellRendererComponent(list, aObj, index, isSelected, hasFocus);
		if (aObj instanceof Unit)
		{
			aStr = ((Unit)aObj).getConfigName();
			retL.setText(aStr);
		}

		return retL;
	}

}
