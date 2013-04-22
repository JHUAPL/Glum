package glum.gui.component;

import glum.gui.component.model.GComboBoxModel;

import java.awt.event.ActionListener;
import java.util.*;
import javax.swing.JComboBox;
import javax.swing.ListCellRenderer;

public class GComboBox<G1> extends JComboBox
{
	// State vars
	protected GComboBoxModel<G1> itemModel;
	
	public GComboBox()
	{
		itemModel = new GComboBoxModel<G1>(new LinkedList<G1>());
		setModel(itemModel);
	}
	
	public GComboBox(ActionListener aListener, ListCellRenderer aRenderer)
	{
		this();
		
		addActionListener(aListener);
		setRenderer(aRenderer);
	}
	
	public GComboBox(ActionListener aListener)
	{
		this();
		
		addActionListener(aListener);
	}
	
	public GComboBox(ActionListener aListener, List<G1> aItemList)
	{
		itemModel = new GComboBoxModel<G1>(aItemList);
		setModel(itemModel);

		addActionListener(aListener);
	}
	
	public GComboBox(ActionListener aListener, G1... aItemArr)
	{
		itemModel = new GComboBoxModel<G1>(aItemArr);
		setModel(itemModel);
		
		addActionListener(aListener);
	}

	/**
	 * Returns the list of all items stored in the GComboBox
	 */
	public ArrayList<G1> getAllItems()
	{
		return itemModel.getAllItems();
	}

	/**
	 * Returns the items that is currently selected.
	 */
	public G1 getChosenItem()
	{
		return itemModel.getSelectedItem();
	}
	
	/**
	 * Sets in the currently selected item. This method will not trigger an ActionEvent.
	 * @see JComboBox#setSelectedItem
	 */
	public void setChosenItem(G1 aItem)
	{
		ActionListener[] listenerArr;
		
		listenerArr = getActionListeners();
		for (ActionListener aListener : listenerArr)
			removeActionListener(aListener);
		
		itemModel.setSelectedItem(aItem);

		for (ActionListener aListener : listenerArr)
			addActionListener(aListener);
	}
	
	/**
	 * Note aItem must be of the generified type. This method will not trigger an ActionEvent.
	 */
	@Override @SuppressWarnings("unchecked")
	public void addItem(Object aItem)
	{
		ActionListener[] listenerArr;
		
		listenerArr = getActionListeners();
		for (ActionListener aListener : listenerArr)
			removeActionListener(aListener);
		
		itemModel.addItem((G1)aItem);
		
		for (ActionListener aListener : listenerArr)
			addActionListener(aListener);
	}

	/**
	 * Note aItem must be of the generified type. This method will not trigger an ActionEvent.
	 */
	public void addItems(G1... aItemArr)
	{
		ActionListener[] listenerArr;
		
		listenerArr = getActionListeners();
		for (ActionListener aListener : listenerArr)
			removeActionListener(aListener);
		
		for (G1 aItem : aItemArr)
			itemModel.addItem(aItem);
		
		for (ActionListener aListener : listenerArr)
			addActionListener(aListener);
	}

	/**
	 * Note aItem must be of the generified type. This method will not trigger an ActionEvent.
	 */
	@Override @SuppressWarnings("unchecked")
	public void removeItem(Object aItem)
	{
		ActionListener[] listenerArr;
		
		listenerArr = getActionListeners();
		for (ActionListener aListener : listenerArr)
			removeActionListener(aListener);
		
		itemModel.removeItem((G1)aItem);
		
		for (ActionListener aListener : listenerArr)
			addActionListener(aListener);
	}

	/**
	 * Removes all of the items from the model. This method will not trigger an ActionEvent.
	 */
	@Override
	public void removeAllItems()
	{
		ActionListener[] listenerArr;
		
		listenerArr = getActionListeners();
		for (ActionListener aListener : listenerArr)
			removeActionListener(aListener);
		
		itemModel.removeAllItems();
		
		for (ActionListener aListener : listenerArr)
			addActionListener(aListener);
	}
	
// Note you cannot do the below as internal methods within JComboBox make calls to
// the methods below.
//	@Override
//	public Object getSelectedItem()
//	{
//		throw new RuntimeException("Unsupported operation. Call getChosenItem()");
//	}
//	
//	@Override
//	public void setSelectedItem(Object aObj)
//	{
//		throw new RuntimeException("Unsupported operation. Call setChosenItem()");
//	}
}
