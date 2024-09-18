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
package glum.gui.misc;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

public class MultiStateModel implements ButtonModel
{
	protected final ButtonModel refModel;
	protected final HashMap<Object, MultiState> nextStateMap;
	protected MultiState myState;

	public MultiStateModel(ButtonModel aRefModel, HashMap<Object, MultiState> aNextStateMap)
	{
		refModel = aRefModel;
		nextStateMap = aNextStateMap;

		myState = MultiState.None;
	}

	public void advanceToNextState()
	{
		MultiState aState;

		aState = nextStateMap.get(myState);
		if (aState == null)
			aState = MultiState.None;

		setState(aState);
	}

	public MultiState getState()
	{
		return myState;
	}

	public void setState(MultiState state)
	{
		this.myState = state;
	}

	@Override
	public boolean isSelected()
	{
		if (myState != MultiState.None)
			return true;

		return false;
	}

	/**
	 * All these methods simply delegate to the "refModel" model that is being decorated.
	 */
	@Override
	public boolean isArmed()
	{
		return refModel.isArmed();
	}

	@Override
	public boolean isEnabled()
	{
		return refModel.isEnabled();
	}

	@Override
	public boolean isPressed()
	{
		return refModel.isPressed();
	}

	@Override
	public boolean isRollover()
	{
		return refModel.isRollover();
	}

	@Override
	public void setArmed(boolean b)
	{
		refModel.setArmed(b);
	}

	@Override
	public void setEnabled(boolean b)
	{
		refModel.setEnabled(b);
	}

	@Override
	public void setSelected(boolean b)
	{
		refModel.setSelected(b);
	}

	@Override
	public void setPressed(boolean b)
	{
		refModel.setPressed(b);
	}

	@Override
	public void setRollover(boolean b)
	{
		refModel.setRollover(b);
	}

	@Override
	public void setMnemonic(int key)
	{
		refModel.setMnemonic(key);
	}

	@Override
	public int getMnemonic()
	{
		return refModel.getMnemonic();
	}

	@Override
	public void setActionCommand(String s)
	{
		refModel.setActionCommand(s);
	}

	@Override
	public String getActionCommand()
	{
		return refModel.getActionCommand();
	}

	@Override
	public void setGroup(ButtonGroup group)
	{
		refModel.setGroup(group);
	}

	@Override
	public void addActionListener(ActionListener l)
	{
		refModel.addActionListener(l);
	}

	@Override
	public void removeActionListener(ActionListener l)
	{
		refModel.removeActionListener(l);
	}

	@Override
	public void addItemListener(ItemListener l)
	{
		refModel.addItemListener(l);
	}

	@Override
	public void removeItemListener(ItemListener l)
	{
		refModel.removeItemListener(l);
	}

	@Override
	public void addChangeListener(ChangeListener l)
	{
		refModel.addChangeListener(l);
	}

	@Override
	public void removeChangeListener(ChangeListener l)
	{
		refModel.removeChangeListener(l);
	}

	@Override
	public Object[] getSelectedObjects()
	{
		return refModel.getSelectedObjects();
	}

}
