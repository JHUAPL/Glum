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
import java.util.HashMap;

import javax.swing.*;
import javax.swing.plaf.ActionMapUIResource;

public class MultiStateCheckBox extends JCheckBox implements MouseListener
{
	// State vars
	protected MultiStateModel model;
	protected HashMap<Object, MultiState> nextStateMap;

	/**
	 * Standard Constructor
	 */
	public MultiStateCheckBox(String text, boolean is3StateCycle)
	{
		super(text);

		// Set up our replacement icon
		super.setIcon(new MultiStateIcon());

		// Construct the cycle order of the MultiState checkbox
		nextStateMap = new HashMap<Object, MultiState>();
		nextStateMap.put(false, MultiState.Checked);
		nextStateMap.put(true, MultiState.None);
		if (is3StateCycle == false)
		{
			nextStateMap.put(MultiState.Checked, MultiState.None);
			nextStateMap.put(MultiState.None, MultiState.Checked);
			nextStateMap.put(MultiState.Mixed, MultiState.None);
		}
		else
		{
			nextStateMap.put(MultiState.None, MultiState.Checked);
			nextStateMap.put(MultiState.Checked, MultiState.Mixed);
			nextStateMap.put(MultiState.Mixed, MultiState.None);
//			nextStateMap.put(MultiState.Mixed, MultiState.Crossed);
		}

		// Register for mouse events
		super.addMouseListener(this);

		// Reset the keyboard action map
		rebuildKeyboardMap();

		// set the model to the adapted model
		model = new MultiStateModel(getModel(), nextStateMap);
		setModel(model);
		setState(MultiState.None);
	}

	public MultiStateCheckBox(String text, HashMap<Object, MultiState> aNextStateMap)
	{
		super(text, false);

		// Insanity check
		if (aNextStateMap == null)
			throw new NullPointerException();

		// Save off the custom cycle order of the MultiState checkbox
		nextStateMap = aNextStateMap;
	}

	public MultiStateCheckBox(String text)
	{
		this(text, false);
	}

	public MultiStateCheckBox()
	{
		this(null);
	}

	@Override
	public void doClick()
	{
		var tmpEvent = new MouseEvent(this, MouseEvent.MOUSE_PRESSED, 0, 0, 0, 0, 0, false);
		handleMouseEvent(tmpEvent);
		model.advanceToNextState();
	}

	/** No one may add mouse listeners, not even Swing! */
	@Override
	public void addMouseListener(MouseListener l)
	{
	}

	/** No one may set a new icon */
	@Override
	public void setIcon(Icon icon)
	{
	}

	// @formatter:off
	/**
	* MouseListener interface methods
	*/
	@Override public void mouseClicked(MouseEvent e) { handleMouseEvent(e); }
	@Override public void mouseEntered(MouseEvent e) { handleMouseEvent(e); }
	@Override public void mouseExited(MouseEvent e) { handleMouseEvent(e); }
	@Override public void mousePressed(MouseEvent e) { handleMouseEvent(e); }
	@Override public void mouseReleased(MouseEvent e) { handleMouseEvent(e); }
	// @formatter:on

	/**
	 * Return the current state of the Checkbox
	 */
	public MultiState getState()
	{
		return model.getState();
	}

	@Override
	public void setSelected(boolean selected)
	{
		if (selected)
			setState(MultiState.Checked);
		else
			setState(MultiState.None);
	}

	/**
	 * setState
	 */
	public void setState(MultiState state)
	{
		model.setState(state);

		repaint();
	}

	/**
	 * Utility method for handling various MouseEvents
	 */
	protected void handleMouseEvent(MouseEvent e)
	{
		var tmpID = e.getID();
		if (tmpID == MouseEvent.MOUSE_ENTERED)
		{
			model.setArmed(true);
		}
		else if (tmpID == MouseEvent.MOUSE_EXITED)
		{
			model.setArmed(false);
		}
		else if (tmpID == MouseEvent.MOUSE_RELEASED)
		{
			if (model.isArmed() == true)
				model.advanceToNextState();

			model.setPressed(false);
		}
		else if (tmpID == MouseEvent.MOUSE_PRESSED)
		{
			grabFocus();
			model.setPressed(true);
		}
	}

	/**
	 * Utility method to set up a custom keyboard map
	 */
	protected void rebuildKeyboardMap()
	{
		var map = new ActionMapUIResource();
		map.put("pressed", new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				model.setPressed(true);
			}
		});

		map.put("released", new AbstractAction()
		{
			@Override
			public void actionPerformed(ActionEvent e)
			{
				model.setPressed(false);
				model.advanceToNextState();
			}
		});

		SwingUtilities.replaceUIActionMap(this, map);
	}

}
