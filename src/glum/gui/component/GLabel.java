package glum.gui.component;

import java.awt.Dimension;
import java.awt.Font;

import glum.unit.ConstUnitProvider;
import glum.unit.Unit;
import glum.unit.UnitListener;
import glum.unit.UnitProvider;

import javax.swing.JLabel;

public class GLabel extends JLabel implements UnitListener
{
	// State vars
	protected UnitProvider refUnitProvider;
	protected Object refValue;
	protected boolean showLabel;

	public GLabel(Font aFont)
	{
		this(null, aFont, false);
	}

	public GLabel(Unit aUnit, Font aFont)
	{
		this(new ConstUnitProvider(aUnit), aFont, false);
	}

	public GLabel(UnitProvider aUnitProvider, Font aFont)
	{
		this(aUnitProvider, aFont, false);
	}

	public GLabel(UnitProvider aUnitProvider, Font aFont, boolean aShowLabel)
	{
		super();

		refUnitProvider = aUnitProvider;
		if (refUnitProvider != null)
			refUnitProvider.addListener(this);
		refValue = null;

		if (aFont != null)
			setFont(aFont);

		showLabel = aShowLabel;

		setMinimumSize(new Dimension(0, 0));
	}

	@Override
	public void unitChanged(UnitProvider aProvider, String aKey)
	{
		setValue(refValue);
	}

	/**
	 * Method to set in the value which will be formatted with the associated unit.
	 */
	public void setValue(Object aValue)
	{
		String aStr;
		Unit aUnit;
		
		refValue = aValue;
		
		aUnit = null;
		if (refUnitProvider != null)
			aUnit = refUnitProvider.getUnit();

		if (aUnit == null)
			aStr = "" + aValue;
		else if (showLabel == false)
			aStr = aUnit.getString(aValue);
		else
			aStr = aUnit.getString(aValue, true);

		setText(aStr);
	}

}
