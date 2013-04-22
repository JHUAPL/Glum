package glum.registry;

public interface SelectionListener
{
	/**
	 * Notification that the selection was changed for the specified class
	 */
	public void selectionChanged(SelectionManager aManager, Class<?> aClass);

}
