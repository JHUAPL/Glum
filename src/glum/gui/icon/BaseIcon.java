package glum.gui.icon;

import javax.swing.Icon;

public abstract class BaseIcon implements Icon
{
	protected int mDim;

	/**
	 * Constructor
	 */
	public BaseIcon(int aDim)
	{
		mDim = aDim;
	}
	
	@Override
	public int getIconWidth()
	{
		return mDim;
	}

	@Override
	public int getIconHeight()
	{
		return mDim;
	}

}
