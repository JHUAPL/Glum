package glum.unit;

import glum.zio.ZioObj;

public interface UnitProvider extends ZioObj
{
	/**
	 * Adds a Listener for Unit changes
	 */
	public void addListener(UnitListener aListener);
	
	/**
	 * Removes a Listener for Unit changes
	 */
	public void removeListener(UnitListener aListener);

	/**
	 * Returns the name of the current configuration
	 */
	public String getConfigName();

	/**
	 * Returns the official name name associated with the Unit
	 */
	public String getDisplayName();

	/**
	 * Returns the Unit associated with this provider
	 */
	public Unit getUnit();

}
