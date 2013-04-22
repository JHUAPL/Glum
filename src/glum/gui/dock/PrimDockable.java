package glum.gui.dock;

import bibliothek.gui.dock.DefaultDockable;

/**
 * Base class for Dockables which would like to store their configuration via
 * the PrimConfig mechanism. Note if the child class will be loaded with the 
 * PrimDockableFactory, then you should have a constructor with one of the 
 * following properties:.
 *  <LI> 1 arguments: Registry
 *  <LI> 0 arguments: Empty Constructor
 */
public abstract class PrimDockable extends DefaultDockable implements PrimDock
{
	@Override
	public abstract PrimConfig getConfiguration();

	@Override
	public abstract void setConfiguration(PrimConfig aConfig);

	@Override
	public String getFactoryID()
	{
		return PrimDockFactory.ID;
	}

}
