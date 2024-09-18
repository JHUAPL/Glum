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
package glum.gui.component.model;

import java.util.Collection;

import javax.swing.ComboBoxModel;

import com.google.common.collect.ImmutableList;

import glum.gui.component.GComboBox;

/**
 * Implementation of {@link ComboBoxModel} that is used with {@link GComboBox}.
 *
 * @author lopeznr1
 */
public class GComboBoxModel<G1> extends GListModel<G1> implements ComboBoxModel<G1>
{
	private G1 chosenItem;

	/**
	 * Standard Constructor
	 */
	public GComboBoxModel(Collection<G1> aItemC)
	{
		super(aItemC);

		chosenItem = null;
		if (itemL.size() > 0)
			chosenItem = itemL.get(0);
	}

	@SafeVarargs
	public GComboBoxModel(G1... aItemArr)
	{
		this(ImmutableList.copyOf(aItemArr));
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
		if (itemL.size() > 0)
			chosenItem = itemL.get(0);
	}

	/**
	 * Note aItem must be of the Generified type
	 */
	@Override
	@SuppressWarnings("unchecked")
	public void setSelectedItem(Object aItem)
	{
		chosenItem = (G1) aItem;
	}

	@Override
	public G1 getSelectedItem()
	{
		return chosenItem;
	}

}
