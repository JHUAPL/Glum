package glum.gui.panel.itemList.config;

import glum.gui.panel.itemList.BasicItemHandler;
import glum.gui.panel.itemList.query.QueryAttribute;
import glum.gui.panel.itemList.query.QueryComposer;

public class ConfigHandler extends BasicItemHandler<QueryAttribute>
{
	public ConfigHandler(QueryComposer<?> aComposer)
	{
		super(aComposer);
	}
	
	@Override
	public Object getColumnValue(QueryAttribute aObj, int colNum)
	{
		Enum<?> refKey;

		// Insanity check
		if (colNum < 0 && colNum >= fullAttributeList.size())
			return null;

		refKey = fullAttributeList.get(colNum).refKey;
		return getColumnValue(aObj, refKey);
	}

	@Override
	public void setColumnValue(QueryAttribute aObj, int colNum, Object aValue)
	{
		Enum<?> refKey;

		// Insanity check
		if (colNum < 0 && colNum >= fullAttributeList.size())
			return;

		refKey = fullAttributeList.get(colNum).refKey;
		setColumnValue(aObj, refKey, aValue);
	}

	/**
	 * Method to get the value from aObj described by aRefKey
	 */
	public Object getColumnValue(QueryAttribute aItem, Enum<?> aRefKey)
	{
		switch ((ConfigLookUp)aRefKey)
		{
			case IsVisible:
			return aItem.isVisible;

			case Name:
			return aItem.refKey;

			case Label:
			return aItem.label;

			default:
			break;
		}

		return null;
	}

	/**
	 * Method to get the value from aObj described by aRefKey
	 */
	public void setColumnValue(QueryAttribute aItem, Enum<?> aRefKey, Object aValue)
	{
		if (aRefKey == ConfigLookUp.IsVisible)
		{
			aItem.isVisible = (Boolean)aValue;
			return;
		}

		throw new RuntimeException("Unsupported Operation.");
	}

}
