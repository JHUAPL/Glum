package glum.gui.panel.itemList;

import java.awt.*;
import javax.swing.*;
import javax.swing.table.*;

public class SearchBoxRenderer extends DefaultListCellRenderer
{
	/**
	 * Constructor
	 */
	public SearchBoxRenderer()
	{
		super();
	}

	@Override
	public Component getListCellRendererComponent(JList list, Object aObj, int index, boolean isSelected, boolean hasFocus)
	{
		JLabel retL;
		String aStr;

		retL = (JLabel)super.getListCellRendererComponent(list, aObj, index, isSelected, hasFocus);
		if (aObj instanceof TableColumn)
		{
			aStr = "" + ((TableColumn)aObj).getHeaderValue();
			if (aStr.equals("null") == true || aStr.equals("") == true)
				aStr = "" + ((TableColumn)aObj).getIdentifier();

			retL.setText(aStr);
		}

		return retL;
	}

}
