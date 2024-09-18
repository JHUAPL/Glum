// Copyright (C) 2024 The Johns Hopkins University Applied Physics Laboratory LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package glum.gui.panel;

import java.awt.CardLayout;
import java.awt.Component;
import java.util.*;

import javax.swing.JPanel;

/**
 * Panel that allows you to shuffle between a collection of "card" panels. Each card must be a descendant of JPanel.
 * <p>
 * Unlike {@link CardLayout} this class supports mapping of an object (rather than a string) to a card.
 *
 * @author lopeznr1
 */
public class CardPanel<G1> extends JPanel
{
	// State vars
	private HashMap<Object, G1> keyMap;
	private Map<G1, String> revMap;
	private CardLayout workCardLayout;
	private G1 activeCard;
	private G1 backupCard;

	/** Standard Constructor */
	public CardPanel()
	{
		workCardLayout = new CardLayout();
		setLayout(workCardLayout);

		keyMap = new HashMap<>();
		revMap = new HashMap<>();
		activeCard = null;
		backupCard = null;
	}

	@Override
	public Component add(String aName, Component aComp)
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
			throw new IllegalArgumentException(
					"aCard must be of type Component. Found class: " + aCard.getClass().getName());

		// Associate the card with the specified key
		if (keyMap.get(aKey) == null)
			keyMap.put(aKey, aCard);
		// If the key is associated, then ensure it is matched to aCard
		else if (keyMap.get(aKey) != aCard)
			throw new RuntimeException("Attempting to add new card with an already inserted key: " + aKey);

		// Form a mapping of aCard to a unique string key
		if (revMap.containsKey(aCard) == false)
		{
			String strKey = "" + revMap.size();
			revMap.put(aCard, strKey);
			add((Component) aCard, strKey);
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

	public List<G1> getAllCards()
	{
		var retCardL = new ArrayList<G1>(keyMap.values());
		return retCardL;
	}

	/**
	 * Returns the card associated with the specified key.
	 */
	public G1 getCardForKey(Object aKey)
	{
		return keyMap.get(aKey);
	}

	/**
	 * Method to specify the backup card to use should no card be found that corresponds to the specified key.
	 * <p>
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
	 * <p>
	 * A RuntimeException will be thrown if there is no card associated with aKey and the backupCord has not been
	 * specified.
	 */
	public void switchToCard(Object aKey)
	{
		// Switch to the proper card
		activeCard = keyMap.get(aKey);
		if (activeCard == null)
			activeCard = backupCard;

		// Ensure we have a valid activeCard
		if (activeCard == null)
			throw new RuntimeException("No mapping found when switching to card: " + aKey);

		// Switch the CardLayout to the activeCard
		String strKey = revMap.get(activeCard);
		workCardLayout.show(this, strKey);
	}

}
