package glum.gui.panel.itemList.config;

import glum.gui.panel.itemList.query.QueryAttribute;
import glum.gui.panel.itemList.query.QueryComposer;
import glum.zio.*;

import java.io.IOException;
import java.util.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 *
 * Class that stores a list of QuerryAttributes used to describe a specific configuration
 * of ItemListPanel and associated table.
 *
 */
public class ProfileConfig implements ZioObj
{
	// State vars
	protected String myName;
	protected Map<Integer, QueryAttribute> itemMap;

	/**
	* Constructor. Form a deep copy of the list of QueryAttributes. This is to
	* ensure that this profile will not share QueryAttributes with other profiles
	* leading to inadvertent tampering.
	* 
	* @param aName: Name of the profile
	* @param aItemSet: Set of all QuerryAttributes associated with this profile
	*/
	public ProfileConfig(String aName, Collection<QueryAttribute> aItemList)
	{
		myName = aName;
		
		itemMap = Maps.newLinkedHashMap();
		for (QueryAttribute aItem : aItemList)
			itemMap.put(aItem.modelIndex, new QueryAttribute(aItem));
	}
	
	/**
	* Constructor. All of the QueryAttribute will be turned off by default.
	* It is imperative that you call setVisibleItems() to allow this profile
	* to display columns when activated.
	* 
	* @param aName: Name of the profile
	* @param aComposer: The composer which provides all QuerryAttributes
	*  associated with this profile
	*/
	public ProfileConfig(String aName, QueryComposer<?> aComposer)
	{
		this(aName, aComposer.getItems());

		for (QueryAttribute aItem : itemMap.values())
			aItem.isVisible = false;
	}
	
	/**
	 * Sets the specified QueryAttributes as visible for this ProfileConfig
	 */
	public void setVisibleItems(Collection<QueryAttribute> aItemList)
	{
		for (QueryAttribute aItem : aItemList)
			itemMap.get(aItem.modelIndex).isVisible = true;
	}
	
	/**
	 * Sets the specified QueryAttributes as visible for this ProfileConfig
	 */
	public void setVisibleItems(QueryAttribute... aItemList)
	{
		for (QueryAttribute aItem : aItemList)
			itemMap.get(aItem.modelIndex).isVisible = true;
	}

	/**
	 * Returns the (display) name of this ProfileConfig
	 */
	public String getName()
	{
		return myName;
	}

	/**
	 * Returns the QueryAttributes associated with this ProfileConfig
	 */
	public ArrayList<QueryAttribute> getItems()
	{
		return Lists.newArrayList(itemMap.values());
	}

	@Override
	public void zioRead(ZinStream aStream) throws IOException
	{
		Map<Integer, QueryAttribute> newMap;
		QueryAttribute queryAttr;
		int numItems, modelId;
		
		aStream.readVersion(0);

		myName = aStream.readString();

		numItems = aStream.readInt();
		if (numItems != itemMap.size())
			throw new IOException("Mismatched attribute count. Expected: " + itemMap.size() + " Read: " + numItems);
		
		newMap = Maps.newLinkedHashMap();
		for (int c1 = 0; c1 < numItems; c1++)
		{
			modelId = aStream.readInt();
			
			queryAttr = itemMap.get(modelId);
			queryAttr.zioRead(aStream);
			
			newMap.put(modelId, queryAttr);
		}
		
		itemMap = newMap;
	}

	@Override
	public void zioWrite(ZoutStream aStream) throws IOException
	{
		QueryAttribute queryAttr;
		int numItems;
		
		aStream.writeVersion(0);

		aStream.writeString(myName);
		
		numItems = itemMap.size();
		aStream.writeInt(numItems);
		
		for (int aModelId : itemMap.keySet())
		{
			queryAttr = itemMap.get(aModelId);
			
			aStream.writeInt(aModelId);
			queryAttr.zioWrite(aStream);
		}
	}

	@Override
	public String toString()
	{
		return myName;
	}

}
