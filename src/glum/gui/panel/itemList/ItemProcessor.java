package glum.gui.panel.itemList;

import java.util.*;

public interface ItemProcessor<G1>
{
	/**
	 * Returns the number of items in this ItemProcessor
	 */
	public int getNumItems();

	/**
	 * Returns a list of all items in this ItemProcessor
	 */
	public Collection<? extends G1> getItems();

	/**
	 * Registers for notification when items are added/removed from this ItemProcessor.
	 */
	public void addItemChangeListener(ItemChangeListener aItemChangeListener);

	/**
	 * Deregisters for notification of item list change events.
	 */
	public void removeItemChangeListener(ItemChangeListener aItemChangeListener);
}
