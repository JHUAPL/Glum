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
package glum.gui.panel.itemList;

import java.util.List;

import com.google.common.collect.ImmutableList;

import glum.registry.Registry;
import glum.registry.ResourceListener;

/**
 * Implementation of {@link ItemProcessor} backed by the specified Registry and resource key.
 * <p>
 * Notification will be sent out whenever the registry changes.
 *
 * @author lopeznr1
 */
public class RegistryProcessor<G1> extends BasicItemProcessor<G1> implements ResourceListener
{
	// Ref vars
	private final Registry refRegistry;
	private final Class<G1> resourceClass;
	private final Object resourceKey;

	// State vars
	private ImmutableList<G1> itemL;

	/** Standard Constructor */
	public RegistryProcessor(Registry aRegistry, Object aResourceKey, Class<G1> aResourceClass)
	{
		refRegistry = aRegistry;
		resourceClass = aResourceClass;
		resourceKey = aResourceKey;

		// Initialize our state vars
		itemL = ImmutableList.of();

		// Register for events of interest
		refRegistry.addResourceListener(resourceKey, this);
	}

	@Override
	public ImmutableList<G1> getAllItems()
	{
		return itemL;
	}

	@Override
	public synchronized int getNumItems()
	{
		return itemL.size();
	}

	@Override
	public void resourceChanged(Registry aRegistry, Object aKey)
	{
		// Retrieve the list of tiles
		List<G1> tmpL = refRegistry.getResourceItems(resourceKey, resourceClass);
		itemL = ImmutableList.copyOf(tmpL);

		// Notify our listeners
		notifyListeners();
	}

}
