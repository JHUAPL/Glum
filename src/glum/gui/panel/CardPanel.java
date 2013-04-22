package glum.gui.panel;

import java.awt.*;
import java.util.*;
import javax.swing.*;

import com.google.common.collect.*;

public class CardPanel<G1> extends JPanel
{
	protected BiMap<String, G1> myMap;
	protected CardLayout myLayout;
	protected G1 activeCard;
	
	public CardPanel()
	{
		super();
		
		myLayout = new CardLayout();
		setLayout(myLayout);
		
		myMap = HashBiMap.create();
		activeCard = null;
	}
	
	@Override
	public Component add(String name, Component comp)
	{
		throw new RuntimeException("Improper method call. Use addCard() instead of add()");
	}
	
	public void addCard(String aTitle, G1 aComponent)
	{
		// aComponent must be of type Component
		if ((aComponent instanceof Component) == false)
			throw new IllegalArgumentException("aComponent must be of type Component. Found class: " + aComponent.getClass().getName());

		// Add the component if no component associated with the key
		if (myMap.get(aTitle) == null)
		{
			myMap.put(aTitle, aComponent);
			add((Component)aComponent, aTitle);
		}
		// If the key is associated, then ensure it is matched to aComponent
		else if (myMap.get(aTitle) != aComponent)
		{
			throw new RuntimeException("Attempting to add new card with an already inserted key: " + aTitle);
		}

		switchToCard(aTitle);
	}
	
	public G1 getActiveCard()
	{
		return activeCard;
	}
	
	public Collection<G1> getAllCards()
	{
		Collection<G1> itemList;
		
		itemList = new ArrayList<G1>(myMap.values());
		return itemList;
	}
	
	public Set<String> getCardNames()
	{
		return new HashSet<String>(myMap.keySet());
	}
	
	public void switchToCard(String aTitle)
	{
		activeCard = myMap.get(aTitle);
		if (activeCard == null)
			throw new RuntimeException("No mapping found when switching to card: " + aTitle);

		myLayout.show(this, aTitle);
	}

	public void switchToCard(G1 aCard)
	{
		if (myMap.values().contains(aCard) == false)
			throw new RuntimeException("No mapping found when switching to card: " + aCard);
		
		activeCard = aCard;
		myLayout.show(this, myMap.inverse().get(aCard));
	}

}
