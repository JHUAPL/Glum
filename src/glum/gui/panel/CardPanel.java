package glum.gui.panel;

import java.awt.*;
import java.util.*;

import javax.swing.*;

import com.google.common.collect.*;

/**
 * Panel that allows you to shuffle between a collection of "card" panels. Each card must be a descendant of JPanel.
 * <P>
 * Unlike CardLayout this class supports mapping of an object (rather than a string) to a card.
 */
public class CardPanel<G1> extends JPanel
{
	protected BiMap<Object, G1> keyMap;
	protected Map<G1, String> revMap;
	protected CardLayout myLayout;
	protected G1 activeCard;
	protected G1 backupCard;

	public CardPanel()
	{
		super();

		myLayout = new CardLayout();
		setLayout(myLayout);

		keyMap = HashBiMap.create();
		revMap = new HashMap<>();
		activeCard = null;
		backupCard = null;
	}

	@Override
	public Component add(String name, Component comp)
	{
		throw new RuntimeException("Improper method call. Use addCard() instead of add()");
	}

	/**
	 * Adds and associates the key, aKey with the specified card aCard.
	 */

	public void addCard(Object aKey, G1 aCard)
	{
		// aCard must be of type Component
		if ((aCard instanceof Component) == false)
			throw new IllegalArgumentException("aCard must be of type Component. Found class: " + aCard.getClass().getName());

		// Add the card if no card associated with the key
		if (keyMap.get(aKey) == null)
		{
			// Form the 2-way association of aKey and aComponent
			keyMap.put(aKey, aCard);

			// Form the 1-way mapping of aComponent to a (unique) strKey
			String strKey = "" + revMap.size();
			revMap.put(aCard, strKey);

			add((Component)aCard, strKey);
		}
		// If the key is associated, then ensure it is matched to aCard
		else if (keyMap.get(aKey) != aCard)
		{
			throw new RuntimeException("Attempting to add new card with an already inserted key: " + aKey);
		}

		switchToCard(aKey);
	}

	/**
	 * Returns the card that is currently active.
	 */
	public G1 getActiveCard()
	{
		return activeCard;
	}

	public Collection<G1> getAllCards()
	{
		Collection<G1> itemList;

		itemList = new ArrayList<G1>(keyMap.values());
		return itemList;
	}

	/**
	 * Method to specify the backup card to use should no card be found that corresponds to the specified key.
	 * <P>
	 * This method will throw a RuntimeException if a card has not been associated with the specified key.
	 */
	public void setBackupCard(Object aKey)
	{
		backupCard = keyMap.get(aKey);
		if (backupCard == null)
			throw new RuntimeException("No mapping found for backupCard. Key: " + aKey);
	}

	/**
	 * Causes this CardPanel to show the card associated with the specified key.
	 * <P>
	 * A RuntimeException will be thrown if there is no card associated with aKey and the backupCord has not been specified.
	 */
	public void switchToCard(Object aKey)
	{
		activeCard = keyMap.get(aKey);
		if (activeCard == null)
			activeCard = backupCard;

		// Ensure we have a valid activeCard
		if (activeCard == null)
			throw new RuntimeException("No mapping found when switching to card: " + aKey);

		switchToInstalledCard(activeCard);
	}

	/**
	 * Causes this CardPanel to show the specified card. Throws a RuntimeException if the card was not previously added via addCard().
	 */
	public void switchToInstalledCard(G1 aCard)
	{
		if (keyMap.values().contains(aCard) == false)
			throw new RuntimeException("No mapping found when switching to card: " + aCard);

		// Switch to the proper card
		activeCard = aCard;

		String strKey = revMap.get(aCard);
		myLayout.show(this, strKey);
	}

}
