package glum.gui.dock;

import glum.reflect.ReflectUtil;
import glum.registry.Registry;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.lang.reflect.Constructor;
import java.util.Map;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;
import bibliothek.gui.Dockable;
import bibliothek.gui.dock.DockFactory;
import bibliothek.gui.dock.dockable.DefaultDockablePerspective;
import bibliothek.gui.dock.layout.LocationEstimationMap;
import bibliothek.gui.dock.perspective.PerspectiveDockable;
import bibliothek.gui.dock.station.support.PlaceholderStrategy;
import bibliothek.util.xml.XElement;

/**
 * Generic DockableFactory for creating PrimDocks.
 * <P>
 * Note that before this factory is used all PrimDockable class types must first be associated with a spawnName. This is
 * used during serialization configuration associated with PrimDock. See method {@link PrimDockFactory#addSpawnMapping}
 */
public class PrimDockFactory implements DockFactory<PrimDock, DefaultDockablePerspective, PrimConfig>
{
	// Constants
	public static final String ID = "PrimDockFactory";
	public static final String SpawnNameKey = "factory.spawnName"; 		
	
	// State var
	protected Registry refRegistry;
	protected BiMap<String, Class<? extends PrimDock>> spawnMap;
	
	public PrimDockFactory(Registry aRegistry)
	{
		refRegistry = aRegistry;
		spawnMap = HashBiMap.create();
	}
	
	/**
	 * Add a mapping for a PrimDockable to a the associated spawnName. It is mandatory
	 * that this mapping is always the same regardless of application executions, as
	 * this value will be serialized to the disk. 
	 */
	public void addSpawnMapping(String spawnName, Class<? extends PrimDock> spawnClass)
	{
		// Ensure the spawnName is not already reserved
		if (spawnMap.containsKey(spawnName) == true)
			throw new RuntimeException("Previous mapping stored for spawnName:" + spawnName);
		
		// Ensure the spawnClass is not already stored
		if (spawnMap.inverse().containsKey(spawnClass) == true)
			throw new RuntimeException("Previous mapping stored for spawnClass:" + spawnClass);
		
		spawnMap.put(spawnName, spawnClass);
	}
	

	@Override
	public String getID()
	{
		return ID;
	}

	@Override
	public PrimConfig getLayout(PrimDock aDockable, Map<Dockable, Integer> children)
	{
		PrimConfig rConfig;
		String spawnName;
		
		rConfig = aDockable.getConfiguration();
		
		// Store the associated spawnName used to instantiate the Dockable
		spawnName = spawnMap.inverse().get(aDockable.getClass());
		if (spawnName == null)
			throw new RuntimeException("Factory is not configured properly. Failed to locate associated spawnName for class:" + aDockable.getClass());
		
		// Ensure that the SpawnNameKey is not already reserved.
		; // TODO
		
		rConfig.setString(SpawnNameKey, spawnName);		
		return rConfig;
	}

	@Override
	public PrimConfig getPerspectiveLayout(DefaultDockablePerspective element, Map<PerspectiveDockable, Integer> children)
	{
		// Perspectives are not supported
		return null;
	}

	@Override
	public void setLayout(PrimDock aDockable, PrimConfig aLayout, Map<Integer, Dockable> children, PlaceholderStrategy placeholders)
	{
		aDockable.setConfiguration(aLayout);
	}

	@Override
	public void setLayout(PrimDock aDockable, PrimConfig aLayout, PlaceholderStrategy placeholders)
	{
		aDockable.setConfiguration(aLayout);
	}

	@Override
	public void write(PrimConfig aLayout, DataOutputStream aStream) throws IOException
	{
		aLayout.writeBin(aStream);
	}

	@Override
	public void write(PrimConfig layout, XElement element)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public PrimConfig read(DataInputStream aStream, PlaceholderStrategy placeholders) throws IOException
	{
		PrimConfig rLayout;			
	
		rLayout = new PrimConfig();
		rLayout.readBin(aStream);
		return rLayout;
	}

	@Override
	public PrimConfig read(XElement element, PlaceholderStrategy placeholders)
	{
		throw new UnsupportedOperationException();
	}

	@Override
	public void estimateLocations(PrimConfig layout, LocationEstimationMap children)
	{
		; // Nothing to do
	}

	@Override
	public PrimDock layout(PrimConfig layout, Map<Integer, Dockable> children, PlaceholderStrategy placeholders)
	{
		PrimDock aDockable;
		
		aDockable = layout(layout, placeholders);
		return aDockable;
	}

	@Override
	public PrimDock layout(PrimConfig aLayout, PlaceholderStrategy placeholders)
	{
		PrimDock rDockable;
		Class<? extends PrimDock> spawnClass;
		Constructor<? extends PrimDock>spawnConstructor;
		Class<?> parmTypes[] = {Registry.class};
		Object parmValues[] = {refRegistry};
		String spawnName;
		
		spawnName = aLayout.getString(SpawnNameKey, null);
		
		spawnClass = spawnMap.get(spawnName);
		if (spawnClass == null)
			throw new RuntimeException("Factory is not configured properly. Failed to locate associated class for spawn name:" + spawnName);
		
		try
		{
			spawnConstructor = ReflectUtil.getConstructorSafe(spawnClass, parmTypes);
			if (spawnConstructor != null)
				rDockable = spawnConstructor.newInstance(parmValues);
			else
				rDockable = spawnClass.getDeclaredConstructor().newInstance();
		}
		catch (Exception aExp)
		{
			throw new RuntimeException("Failed to instantite class.", aExp);
		}
		
		rDockable.setConfiguration(aLayout);
		return rDockable;
	}

	@Override
	public DefaultDockablePerspective layoutPerspective(PrimConfig layout, Map<Integer, PerspectiveDockable> children)
	{
		// Perspectives are not supported
		return null;
	}

	@Override
	public void layoutPerspective(DefaultDockablePerspective perspective, PrimConfig layout, Map<Integer, PerspectiveDockable> children)
	{
		; // Nothing to do
	}

}
