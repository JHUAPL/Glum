package glum.gui.component;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import javax.swing.JComponent;
import javax.swing.JList;
import javax.swing.SwingUtilities;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import glum.gui.component.model.GListModel;
import glum.gui.panel.generic.GenericCodes;

import com.google.common.collect.Lists;

public class GList<G1> extends JComponent implements GenericCodes, ListSelectionListener
{
	// Gui vars
	private JList<G1> refList;
	private GListModel<G1> refModel;

	// State vars
	private ActionListener refActionListener;
	private ListSelectionListener refListSelectionListener;

	public GList(Object aListener, List<G1> aItemList)
	{
		refActionListener = null;
		refListSelectionListener = null;

		refModel = new GListModel<>(aItemList);
		refList = new JList<>(refModel);
		buildGui(aListener);
	}

//	public GList(Object aListener, G1... aItemArr)
//	{
//		refActionListener = null;
//		refListSelectionListener = null;
//
//		refModel = new GListModel<G1>(aItemArr);
//		refList = new JList<>(refModel);
//		buildGui(aListener);
//	}
//
	/**
	 * Returns the (first) selected item
	 */
	public G1 getSelectedItem()
	{
		int selectedIndex;

		selectedIndex = refList.getSelectedIndex();
		if (selectedIndex == -1)
			return null;

		return refModel.getElementAt(selectedIndex);
	}

	/**
	 * Returns all of the selected items
	 */
	public List<G1> getSelectedItems()
	{
		ArrayList<G1> retList;
		int[] indexArr;

		indexArr = refList.getSelectedIndices();

		retList = Lists.newArrayList();
		for (int aIndex : indexArr)
			retList.add(refModel.getElementAt(aIndex));

		retList.trimToSize();
		return retList;
	}

	/**
	 * Replaces all of the items in the list with aItemList.
	 */
	public void setItems(Collection<G1> aItemList)
	{
		refList.removeListSelectionListener(this);
		refList.setModel(new GListModel<G1>(aItemList));
		refList.addListSelectionListener(this);
	}

	/**
	 * Sets aItem as the selected item.
	 */
	public void setSelectedItem(G1 aItem)
	{
		refList.removeListSelectionListener(this);
		refList.setSelectedIndex(refModel.indexOf(aItem));
		refList.addListSelectionListener(this);
	}

	/**
	 * Sets all of the items in aItemList as selected
	 */
	public void setSelectedItems(List<G1> aItemList)
	{
		int[] idArr;
		int c1;

		// Ensure we are executed only on the proper thread
		if (SwingUtilities.isEventDispatchThread() == false)
			throw new RuntimeException("GList.selectItems() not executed on the AWT event dispatch thread.");

		refList.removeListSelectionListener(this);

		c1 = 0;
		idArr = new int[aItemList.size()];
		for (G1 aItem : aItemList)
		{
			idArr[c1] = refModel.indexOf(aItem);
			c1++;
		}

		refList.setSelectedIndices(idArr);

		refList.addListSelectionListener(this);
	}

	@Override
	public void setEnabled(boolean aBool)
	{
		refList.setEnabled(aBool);
	}

	@Override
	public void valueChanged(ListSelectionEvent aEvent)
	{
		if (refListSelectionListener != null)
			refListSelectionListener.valueChanged(new ListSelectionEvent(this, aEvent.getFirstIndex(), aEvent.getLastIndex(), aEvent.getValueIsAdjusting()));

		if (refActionListener != null)
			refActionListener.actionPerformed(new ActionEvent(this, ID_UPDATE, "update"));
	}

	/**
	 * Helper method used to build the GUI
	 */
	private void buildGui(Object aListener)
	{
		if (aListener instanceof ActionListener)
			refActionListener = (ActionListener)aListener;
		if (aListener instanceof ListSelectionListener)
			refListSelectionListener = (ListSelectionListener)aListener;

		setLayout(new BorderLayout());
		add(refList);
	}

}
