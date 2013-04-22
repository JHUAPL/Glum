package glum.gui.component.model;

import java.util.Collection;
import javax.swing.ComboBoxModel;

import com.google.common.collect.Lists;

public class GComboBoxModel<G1> extends GListModel<G1> implements ComboBoxModel
{
	protected G1 chosenItem;

	public GComboBoxModel(G1... aItemArr)
	{
		this(Lists.newArrayList(aItemArr));
	}
	
	public GComboBoxModel(Collection<G1> aItemList)
	{
		super(aItemList);
		
		chosenItem = null;
		if (itemList.size() > 0)
			chosenItem = itemList.get(0);
	}

	@Override
	public void addItem(G1 aItem)
	{
		if (chosenItem == null)
			chosenItem = aItem;
		
		super.addItem(aItem);
	}
	
	@Override
	public void removeItem(G1 aItem)
	{
		super.removeItem(aItem);
		
		chosenItem = null;
		if (itemList.size() > 0)
			chosenItem = itemList.get(0);
	}
	
	/**
	 * Note aItem must be of the Generified type
	 */
	@Override @SuppressWarnings("unchecked")
	public void setSelectedItem(Object aItem)
	{
		chosenItem = (G1)aItem;
	}

	@Override
	public G1 getSelectedItem()
	{
		return chosenItem;
	}

}
