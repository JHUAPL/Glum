package glum.registry;

public interface ResourceListener
{
	/**
	 * Event handler
	 */
	public void resourceChanged(Registry aRegistry, Object aKey);
}
