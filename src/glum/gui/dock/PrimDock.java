package glum.gui.dock;

import bibliothek.gui.dock.DockElement;

/**
 * Base class for Dockables which would like to store their configuration via
 * the PrimConfig mechanism. Note if the child class will be loaded with the 
 * PrimDockableFactory, then you should have a constructor with one of the 
 * following properties:.
 *  <LI> 1 arguments: Registry
 *  <LI> 0 arguments: Empty Constructor
 *  <BR>
 *  <BR>It is also important the method getFactoryID() is overridden so that it returns  PrimDockableFactory.ID 
 */
public interface PrimDock extends DockElement
{
	/**
	 * Returns the PrimConfig associated with the Dockable
	 * @return
	 */
	public abstract PrimConfig getConfiguration();

	/**
	 * Configures the Dockable with the aConfig
	 * @return
	 */
	public abstract void setConfiguration(PrimConfig aConfig);

//	@Override
//	public String getFactoryID()
//	{
//		return PrimDockableFactory.ID;
//	}

}
