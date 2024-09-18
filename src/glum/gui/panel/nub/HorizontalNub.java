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
package glum.gui.panel.nub;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;

import glum.gui.panel.WaftPanel;
import glum.util.MathUtil;

/**
 * UI component that provides the (top and south) control used to drag glass panels.
 *
 * @author lopeznr1
 */
public class HorizontalNub extends JComponent implements MouseListener, MouseMotionListener
{
	// Constants
	private static final int DEFAULT_CORNER_DIM = 20;

	// Gui vars
	protected JComponent rootComp;
	protected WaftPanel waftPanel;

	// Hook vars
	protected Dimension hookDim;
	protected Point hookLoc;
	protected Point hookPt;

	// State vars
	protected Action action;
	protected boolean isNorth;
	protected int cornerDim;

	// Color vars
	protected Color plainColor;
	protected Color hookColor;
	protected Color armedColor;

	/** Standard Constructor */
	public HorizontalNub(JComponent aRootComp, WaftPanel aWaftPanel, boolean aIsNorth)
	{
		rootComp = aRootComp;
		waftPanel = aWaftPanel;

		hookDim = null;
		hookLoc = null;
		hookPt = null;

		action = Action.None;
		isNorth = aIsNorth;
		cornerDim = DEFAULT_CORNER_DIM;

		plainColor = Color.GRAY;
		hookColor = Color.RED.darker();
		armedColor = Color.RED.darker().darker();

		// Register for events of interest
		addMouseMotionListener(this);
		addMouseListener(this);

		// Set the dimensions of this component
		setMinimumSize(new Dimension(50, 6));
		setPreferredSize(new Dimension(50, 6));
	}

	/**
	 * Sets in whether there will be a sub nub to grab hold that will allow the end user to resize the associated
	 * WaftPanel.
	 */
	public void setResizable(boolean aBool)
	{
		cornerDim = DEFAULT_CORNER_DIM;
		if (aBool == false)
			cornerDim = 0;

		repaint();
	}

	@Override
	public void mouseClicked(MouseEvent aEvent)
	{
		; // Nothing to do
	}

	@Override
	public void mousePressed(MouseEvent aEvent)
	{
		// Push the waftPanel to the top of the (stack) view
		if (rootComp instanceof JLayeredPane)
			((JLayeredPane) rootComp).moveToFront(waftPanel);

		var mousePt = aEvent.getPoint();
		action = computeMode(mousePt);

		hookDim = new Dimension(waftPanel.getSize());
		hookLoc = new Point(waftPanel.getLocation());
		hookPt = SwingUtilities.convertPoint(this, mousePt, rootComp);

		repaint();
	}

	@Override
	public void mouseReleased(MouseEvent aEvent)
	{
		hookDim = null;
		hookLoc = null;
		hookPt = null;

		action = computeMode(aEvent.getPoint());
		repaint();
	}

	@Override
	public void mouseEntered(MouseEvent aEvent)
	{
		;// Nothing to do
	}

	@Override
	public void mouseExited(MouseEvent aEvent)
	{
		// Bail if we are in an active state
		if (hookPt != null)
			return;

		action = Action.None;
		repaint();
	}

	@Override
	public void mouseDragged(MouseEvent aEvent)
	{
		// Bail if we are not in an active state
		if (hookPt == null)
			return;

		// Transform the reference WaftPanel
		tranformWaftPanel(aEvent);
	}

	@Override
	public void mouseMoved(MouseEvent aEvent)
	{
		action = computeMode(aEvent.getPoint());
		repaint();
	}

	@Override
	public void paint(Graphics g)
	{
		var height = getHeight();
		var width = getWidth();

		var g2d = (Graphics2D) g;

		// Left nub
		if (cornerDim > 0)
		{
			var isArmed = action == Action.ResizeNW || action == Action.ResizeSW;
			drawHandle(g2d, 0, 0, cornerDim, height, isArmed);

			// Right nub
			isArmed = action == Action.ResizeNE || action == Action.ResizeSE;
			drawHandle(g2d, width - cornerDim, 0, cornerDim, height, isArmed);
		}

		// Middle nub
		drawHandle(g2d, cornerDim, 0, width - (2 * cornerDim), height, action == Action.Move);
	}

	/**
	 * Helper method to update the mode based on the mouse position
	 */
	private Action computeMode(Point mousePt)
	{
		var mouseX = mousePt.x;
		var mouseY = mousePt.y;

		if (mouseX < 0 || mouseX >= getWidth())
			return Action.None;
		if (mouseY < 0 || mouseY >= getHeight())
			return Action.None;

		if (isNorth == true)
		{
			if (mouseX < cornerDim)
				return Action.ResizeNW;
			else if (mouseX >= getWidth() - cornerDim)
				return Action.ResizeNE;
		}
		else
		{
			if (mouseX < cornerDim)
				return Action.ResizeSW;
			else if (mouseX >= getWidth() - cornerDim)
				return Action.ResizeSE;
		}

		return Action.Move;
	}

	/**
	 * Renders the control handle in the proper state
	 */
	protected void drawHandle(Graphics2D g2d, int x, int y, int width, int height, boolean isArmed)
	{
		var tmpColor = plainColor;
		if (isArmed == true)
		{
			tmpColor = armedColor;
			if (hookPt != null)
				tmpColor = hookColor;
		}

		g2d.setColor(tmpColor);
		g2d.fill3DRect(x, y, width, height, true);
	}

	/**
	 * Helper method to transform the associated WaftPanel based on the drag event and the current action.
	 */
	protected void tranformWaftPanel(MouseEvent aEvent)
	{
		var maxX = rootComp.getWidth();
		var maxY = rootComp.getHeight();

		var minDim = waftPanel.getMinimumSize();

		// Retrieve the mousePt in the rootComp coordinate system and force it to be in the bounds of the rootComp.
		var mousePt = aEvent.getPoint();
		mousePt = SwingUtilities.convertPoint(this, mousePt, rootComp);
		mousePt.x = MathUtil.boundRange(0, maxX, mousePt.x);
		mousePt.y = MathUtil.boundRange(0, maxY, mousePt.y);

		// Compute the total change in the mouse movement from the hooked point
		var diffX = -(mousePt.x - hookPt.x);
		var diffY = -(mousePt.y - hookPt.y);

		// Retrieve the northwest and southeast points in the root coordinate system
		var nwPt = new Point(0, 0);
		nwPt = SwingUtilities.convertPoint(waftPanel, nwPt, rootComp);
		var sePt = new Point(waftPanel.getWidth(), waftPanel.getHeight());
		sePt = SwingUtilities.convertPoint(waftPanel, sePt, rootComp);

		// Determine the target transformations based on the action
		Point targLoc;
		Dimension targDim;
		if (action == Action.ResizeNW)
		{
			targLoc = new Point(hookLoc.x - diffX, hookLoc.y - diffY);
			targDim = new Dimension(hookDim.width + diffX, hookDim.height + diffY);

			MathUtil.forceConstraints(targLoc, targDim, 0, 0, sePt.x, sePt.y, minDim, false);
		}
		else if (action == Action.ResizeNE)
		{
			targLoc = new Point(hookLoc.x, hookLoc.y - diffY);
			targDim = new Dimension(hookDim.width - diffX, hookDim.height + diffY);

			MathUtil.forceConstraints(targLoc, targDim, nwPt.x, 0, maxX, sePt.y, minDim, false);
		}
		else if (action == Action.ResizeSW)
		{
			targLoc = new Point(hookLoc.x - diffX, hookLoc.y);
			targDim = new Dimension(hookDim.width + diffX, hookDim.height - diffY);

			MathUtil.forceConstraints(targLoc, targDim, 0, nwPt.y, sePt.x, maxY, minDim, false);
		}
		else if (action == Action.ResizeSE)
		{
			targLoc = new Point(hookLoc.x, hookLoc.y);
			targDim = new Dimension(hookDim.width - diffX, hookDim.height - diffY);

			MathUtil.forceConstraints(targLoc, targDim, nwPt.x, nwPt.y, maxX, maxY, minDim, false);
		}
		else if (action == Action.Move)
		{
			targDim = new Dimension(hookDim);
			targLoc = new Point(hookLoc.x - diffX, hookLoc.y - diffY);

			targLoc.x = MathUtil.boundRange(0, maxX - targDim.width, targLoc.x);
			targLoc.y = MathUtil.boundRange(0, maxY - targDim.height, targLoc.y);
		}
		else
		{
			throw new RuntimeException("Unsupported action: " + action);
		}

		// Update the waftPanel with the target transformations
		waftPanel.setLocation(targLoc);
		waftPanel.setPreferredSize(targDim);
		waftPanel.setSize(targDim);

//		rootComp.validate();
		rootComp.revalidate();
	}

}
