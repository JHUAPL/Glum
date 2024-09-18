// Copyright (C) 2024 The Johns Hopkins University Applied Physics Laboratory LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package glum.gui.panel.itemList.config;

import java.io.IOException;
import java.util.*;

import com.google.common.collect.ImmutableList;

import glum.gui.panel.itemList.query.QueryAttribute;
import glum.zio.ZinStream;
import glum.zio.ZoutStream;

/**
 * Class that stores an ordered list of {@link QueryAttribute}s and the corresponding sort priorities.
 *
 * @author lopeznr1
 */
public class ProfileConfig<G1 extends Enum<?>>
{
	// Constants
	/** String constant that defines the reserved profile name. */
	public static final String DEFAULT_NAME = "Default";

	// Attributes
	private final String name;
	private Map<Integer, QueryAttribute<G1>> itemM;
	private final ImmutableList<Integer> sortPriorityL;

	/**
	 * Standard Constructor. Form a deep copy of the list of {@link QueryAttribute}s. This is to ensure that this profile
	 * will not share {@link QueryAttribute}s with other profiles leading to inadvertent tampering.
	 *
	 * @param aName:
	 *        Name of the profile
	 * @param aItemSet:
	 *        Set of all QuerryAttributes associated with this profile
	 */
	public ProfileConfig(String aName, Collection<QueryAttribute<G1>> aItemC, Collection<Integer> aSortPriorityC)
	{
		name = aName;

		itemM = new LinkedHashMap<>();
		for (QueryAttribute<G1> aItem : aItemC)
			itemM.put(aItem.modelIndex, new QueryAttribute<>(aItem));

		sortPriorityL = ImmutableList.copyOf(aSortPriorityC);

		// TODO: Make this object fully immutable
	}

	/**
	 * Simplified Constructor. Form a deep copy of the list of {@link QueryAttribute}s. This is to ensure that this
	 * profile will not share {@link QueryAttribute}s with other profiles leading to inadvertent tampering.
	 *
	 * @param aName:
	 *        Name of the profile
	 * @param aItemSet:
	 *        Set of all QuerryAttributes associated with this profile
	 */
	public ProfileConfig(String aName, Collection<QueryAttribute<G1>> aItemL)
	{
		this(aName, aItemL, ImmutableList.of());
	}

	/**
	 * Returns the (display) name of this ProfileConfig
	 */
	public String name()
	{
		return name;
	}

	/**
	 * Returns the {@link QueryAttribute}s associated with this ProfileConfig
	 */
	public ArrayList<QueryAttribute<G1>> getItems()
	{
		return new ArrayList<>(itemM.values());
	}

	/**
	 * Returns a list of model indexes which define the priority of the associated {@link QueryAttribute}s.
	 */
	public ImmutableList<Integer> sortPriorityList()
	{
		return sortPriorityL;
	}

	@Override
	public String toString()
	{
		return name;
	}

	/**
	 * Utility method to read the {@link ProfileConfig} from the specified stream.
	 */
	public static <G extends Enum<?>> ProfileConfig<G> zioRead(ZinStream aStream, Collection<QueryAttribute<G>> aItemC)
			throws IOException
	{
		var itemM = new LinkedHashMap<Integer, QueryAttribute<G>>();
		for (QueryAttribute<G> aItem : aItemC)
			itemM.put(aItem.modelIndex, new QueryAttribute<>(aItem));

		aStream.readVersion(1);

		var name = aStream.readString();

		var doRestore = true;
		var numItems = aStream.readInt();
		if (numItems != itemM.size())
		{
			var tmpMsg = "Mismatched attribute count. Expected: " + itemM.size() + " Read: " + numItems;
			tmpMsg += "\n\tTable configuration, " + name + ", will not be restored...";
			System.err.println(tmpMsg);
			doRestore = false;
		}

		var newItemL = new ArrayList<QueryAttribute<G>>();
		for (int c1 = 0; c1 < numItems; c1++)
		{
			var modelId = aStream.readInt();

			var queryAttr = itemM.get(modelId);
			if (queryAttr == null || doRestore == false)
			{
				// Skip over unsupported QueryAttributes
				var tmpAttr = new QueryAttribute<G>(null, null, -1);
				tmpAttr.zioRead(aStream);
				continue;
			}
			queryAttr.zioRead(aStream);

			newItemL.add(queryAttr);
		}

		var priCnt = aStream.readInt();
		var tmpSortPriL = new ArrayList<Integer>(priCnt);
		for (var c1 = 0; c1 < priCnt; c1++)
			tmpSortPriL.add(aStream.readInt());

		if (doRestore == false)
		{
			newItemL = new ArrayList<QueryAttribute<G>>(aItemC);
			tmpSortPriL = new ArrayList<Integer>(priCnt);
		}

		return new ProfileConfig<>(name, newItemL, tmpSortPriL);
	}

	/**
	 * Utility method to write the {@link ProfileConfig} to the specified stream.
	 */
	public static void zioWrite(ZoutStream aStream, ProfileConfig<?> aItem) throws IOException
	{
		aStream.writeVersion(1);

		aStream.writeString(aItem.name);

		aStream.writeInt(aItem.itemM.size());
		for (int aModelId : aItem.itemM.keySet())
		{
			var queryAttr = aItem.itemM.get(aModelId);

			aStream.writeInt(aModelId);
			queryAttr.zioWrite(aStream);
		}

		aStream.writeInt(aItem.sortPriorityL.size());
		for (var aModelId : aItem.sortPriorityL)
			aStream.writeInt(aModelId);
	}

}
