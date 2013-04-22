package glum.gui.dock;

import java.util.Set;

import com.google.common.collect.Sets;

import bibliothek.gui.DockStation;
import bibliothek.gui.dock.DockElement;
import bibliothek.gui.dock.layout.DockSituationIgnore;
import bibliothek.gui.dock.perspective.PerspectiveElement;
import bibliothek.gui.dock.perspective.PerspectiveStation;

public class PlainDockSituationIgnore implements DockSituationIgnore
{
	private Set<Class<?>> ignoreClassSet;
	
	public PlainDockSituationIgnore()
	{
		ignoreClassSet = Sets.newLinkedHashSet();
	}
	
	/**
	 * Method to register a new type of Dockable/Station to ignore
	 */
	public void addIgnoreClass(Class<?> aClass)
	{
		ignoreClassSet.add(aClass);
	}
	
	@Override
	public boolean ignoreElement(PerspectiveElement aElement)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean ignoreElement(DockElement aElement)
	{
		// Check to see if aElement is of one of the types in ignoreClassSet
		for (Class<?> aClass : ignoreClassSet)
		{
			if (aClass.isAssignableFrom(aElement.getClass()) == true)
				return true;
		}
		
		return false;
	}
	
	@Override
	public boolean ignoreChildren(PerspectiveStation station)
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public boolean ignoreChildren(DockStation station)
	{
		// TODO Auto-generated method stub
		return false;
	}

}
