package glum.gui.panel.itemList.query;

import java.util.*;

import glum.database.QueryItem;
import glum.gui.panel.itemList.BasicItemHandler;

public class QueryItemHandler<G1 extends QueryItem<?>> extends BasicItemHandler<G1>
{
	public QueryItemHandler(QueryComposer<?> aComposer)
	{
		super(aComposer.getItems());
	}

	public QueryItemHandler(Collection<QueryAttribute> aQueryAttrList)
	{
		super(aQueryAttrList);
	}

	@Override
	public Object getColumnValue(G1 aObj, int colNum)
	{
		QueryItem<Enum<?>> aItem;

		// Insanity check
		if (colNum < 0 && colNum >= fullAttributeList.size())
			return null;

//		return aObj.getValue(fullAttributeList.get(colNum).refKey);
		aItem = null;
		if (aObj instanceof QueryItem)
		{
			aItem = (QueryItem<Enum<?>>)aObj;
			return aItem.getValue(fullAttributeList.get(colNum).refKey);
		}

		return null;
	}

	@Override
	public void setColumnValue(G1 aObj, int colNum, Object aValue)
	{
		QueryItem<Enum<?>> aItem;

		// Insanity check
		if (colNum < 0 && colNum >= fullAttributeList.size())
			return;

//		aObj.setValue(fullAttributeList.get(colNum).refKey, aValue);
		aItem = null;
		if (aObj instanceof QueryItem)
		{
			aItem = (QueryItem<Enum<?>>)aObj;
			aItem.setValue(fullAttributeList.get(colNum).refKey, aValue);
		}
	}

}
