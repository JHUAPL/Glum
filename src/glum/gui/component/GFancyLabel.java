package glum.gui.component;

import java.awt.Dimension;
import java.awt.Font;

import glum.unit.UnitListener;
import glum.unit.UnitProvider;

import javax.swing.JLabel;

public class GFancyLabel extends JLabel implements UnitListener
{
	// State vars
	protected UnitProvider[] unitProviderArr;
	protected String[] formatStrArr;
	protected Object[] refValueArr;

	/**
	 * Fancy JLabel which provides auto formatting of objects. The constructor
	 * is provided with a formatStr which has unit place holders specified by "%u"
	 * There must be a corresponding UnitProvider for each occurrence of "%u". This 
	 * is provided via the variable arguments of UnitProvider. 
	 */
	public GFancyLabel(String formatStr, UnitProvider... aUnitProviderArr)
	{
		this(null, formatStr, aUnitProviderArr);
	}

	/**
	 * Fancy JLabel which provides auto formatting of objects. The constructor
	 * is provided with a formatStr which has unit place holders specified by "%u"
	 * There must be a corresponding UnitProvider for each occurrence of "%u". This 
	 * is provided via the variable arguments of UnitProvider. 
	 */
	public GFancyLabel(Font aFont, String aFormatStr, UnitProvider... aUnitProviderArr)
	{
		super();

		if (aFont != null)
			setFont(aFont);
		
		formatStrArr = aFormatStr.split("%u", -1);
		
		unitProviderArr = aUnitProviderArr;
		for (UnitProvider aUnitProvider : unitProviderArr)
			aUnitProvider.addListener(this);
		
		// Insanity check
		if (unitProviderArr.length != formatStrArr.length - 1)
			throw new RuntimeException("Num place holders: " + (formatStrArr.length - 1) + "    Num units: " + unitProviderArr.length);

		refValueArr = new Object[unitProviderArr.length];
		for (int c1 = 0; c1 < unitProviderArr.length; c1++)
			refValueArr[c1] = null;

		setMinimumSize(new Dimension(0, 0));
	}

	@Override
	public void unitChanged(UnitProvider aProvider, String aKey)
	{
		setValues(refValueArr);
	}

	/**
	 * Method to set in the set of values which will be formatted with the associated UnitProviders
	 * which were specified via the constructor.
	 */
	public void setValues(Object... aValueArr)
	{
		String aStr;
		
		// Ensure the number of objects matches the number of units
		if (unitProviderArr.length != aValueArr.length)
			throw new RuntimeException("Inproper number of arguments. Expected: " + unitProviderArr.length + " Recieved:" + aValueArr.length);
		
		for (int c1 = 0; c1 < aValueArr.length; c1++)
			refValueArr[c1] = aValueArr[c1];
		
		aStr = "";
		for (int c1 = 0; c1 < aValueArr.length; c1++)
		{
			aStr += formatStrArr[c1];
			aStr += unitProviderArr[c1].getUnit().getString(aValueArr[c1], false);
		}
		
		if (formatStrArr.length > aValueArr.length)
			aStr += formatStrArr[formatStrArr.length - 1];

		setText(aStr);
	}

}
