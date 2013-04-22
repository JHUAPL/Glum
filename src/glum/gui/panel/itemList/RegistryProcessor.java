package glum.gui.panel.itemList;

import java.util.*;

import glum.registry.*;

public class RegistryProcessor<G1> extends BasicItemProcessor<G1> implements ResourceListener
{
	private Registry refRegistry;
	private Class<G1> resourceClass;
	private Object resourceKey;

	private Collection<G1> itemList;

	/**
	 * Constructor
	 */
	public RegistryProcessor(Registry aRegistry, Object aResourceKey, Class<G1> aResourceClass)
	{
		super();

		refRegistry = aRegistry;
		resourceClass = aResourceClass;
		resourceKey = aResourceKey;

		// Initialize our state vars
		itemList = new ArrayList<G1>();

		// Register for events of interest
		refRegistry.addResourceListener(resourceKey, this);
	}

	@Override
	public synchronized Collection<G1> getItems()
	{
		return new ArrayList<G1>(itemList);
	}

	@Override
	public synchronized int getNumItems()
	{
		return itemList.size();
	}

	@Override
	public void resourceChanged(Registry aRegistry, Object aKey)
	{
		// Retrieve the list of tiles
		itemList = refRegistry.getResourceItems(resourceKey, resourceClass);

		// Notify our listeners
		notifyListeners();
	}

}
