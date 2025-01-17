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

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.io.IOException;

import javax.swing.JComponent;

import glum.gui.panel.nub.HorizontalNub;
import glum.util.MathUtil;
import glum.zio.*;
import glum.zio.util.ZioUtil;
import net.miginfocom.swing.MigLayout;

/**
 * Panel which provides decorative controls to allow a panel to be moved or resized. This is useful for wrapping a
 * component that will be thrown into a JLayered pane of sorts. Note it is assumed that WaftPanel is contained in a
 * LayoutManager of type MigLayout.
 * <p>
 * Perhaps, this class should be rewritten so that it draws the move, resize borders, and manually listens to them
 * rather than delegating to the Nub classes. The current implementation is slightly inefficient, but probably more
 * flexible as it allows other custom Nubs to be swapped in.
 *
 * @author lopeznr1
 */
public class WaftPanel extends JComponent implements ZioObj, ComponentListener
{
	// Gui vars
	protected Component childComp;
	protected JComponent parentComp;
	protected HorizontalNub nMoveComp, sMoveComp;

	// State vars
	protected Dimension prefDim;
	protected Point prefLoc;
	protected double aspectRatio;

	/** Standard Constructor */
	public WaftPanel(Component aChildComp, JComponent aParentComp, boolean aTopNubOnly)
	{
		childComp = aChildComp;
		parentComp = aParentComp;

		prefDim = null;
		prefLoc = null;

		// Embed the childComp into this WaftPanel and surround with the various nub handles
		setLayout(new MigLayout("", "0[grow,fill]0[]0", "0[]0[grow,fill]0[]0"));

		nMoveComp = new HorizontalNub(parentComp, this, true);
		sMoveComp = new HorizontalNub(parentComp, this, false);
		add(nMoveComp, "wrap");
		add(aChildComp, "growx,growy,wrap");
		if (aTopNubOnly == false)
			add(sMoveComp, "wrap");

		// Register to listen to components events associated with the parent
		parentComp.addComponentListener(this);
	}

	/** Simplified Constructor */
	public WaftPanel(Component aChildComp, JComponent aParentComp)
	{
		this(aChildComp, aParentComp, false);
	}

	/**
	 * Change whether the user can move the WaftPanel. Note this will automatically disabling the resizing of the
	 * WaftPanel.
	 */
	public void setMovable(boolean aBool)
	{
		nMoveComp.setVisible(false);
		sMoveComp.setVisible(false);
	}

	/**
	 * Changes whether the associated nubs will provide resize controls.
	 */
	public void setResizable(boolean aBool)
	{
		nMoveComp.setResizable(aBool);
		sMoveComp.setResizable(aBool);
	}

	/**
	 * Returns the current targeted and locked dimension
	 */
	public Dimension getPrefDim()
	{
		if (prefDim == null)
			return null;

		return new Dimension(prefDim);
	}

	/**
	 * Returns the current targeted and locked location
	 */
	public Point getPrefLoc()
	{
		if (prefLoc == null)
			return null;

		return new Point(prefLoc);
	}

	@Override
	public void setLocation(Point aLoc)
	{
		// Update the preferred location
		prefLoc = new Point(aLoc);

		setLocationAndLock(aLoc);
	}

	@Override
	public void setLocation(int x, int y)
	{
		setLocation(new Point(x, y));
	}

	@Override
	public void setSize(Dimension aDim)
	{
		// Update the preferred location
		prefDim = new Dimension(aDim);

		super.setSize(aDim);
	}

	@Override
	public void componentResized(ComponentEvent aEvent)
	{
		// Respond only to resizes of our parent
		if (aEvent.getComponent() != parentComp)
			return;

		// Record the current WaftPanels as preferences if they have not been recorded
		if (prefDim == null)
			prefDim = getSize();

		if (prefLoc == null)
			prefLoc = getLocation();

		// Retrieve the targDim, targLoc, and the minDim
		var minDim = getMinimumSize();
		var targDim = new Dimension(prefDim);
		var targLoc = new Point(prefLoc);

		// Ensure the targDim is no larger than parentDim
		var parentDim = parentComp.getSize();
		targDim.width = MathUtil.boundRange(0, parentDim.width, targDim.width);
		targDim.height = MathUtil.boundRange(0, parentDim.height, targDim.height);

		// Attempt to force ourself into the constraint of the parentComp
		MathUtil.forceConstraints(targLoc, targDim, 0, 0, parentComp.getWidth(), parentComp.getHeight(),
				new Dimension(0, 0), true);

		// Ensure the minimum constraints are not violated
		targDim.width = MathUtil.boundRange(minDim.width, targDim.width, targDim.width);
		targDim.height = MathUtil.boundRange(minDim.height, targDim.height, targDim.height);

		// Update the waftPanel with the target transformations
		setLocationAndLock(targLoc);
		setPreferredSize(targDim);
		super.setSize(targDim);

		parentComp.revalidate();
	}

	@Override
	public void componentMoved(ComponentEvent e)
	{
		; // Nothing to do
	}

	@Override
	public void componentShown(ComponentEvent e)
	{
		; // Nothing to do
	}

	@Override
	public void componentHidden(ComponentEvent e)
	{
		; // Nothing to do
	}

	@Override
	public void zioRead(ZinStream aStream) throws IOException
	{
		aStream.readVersion(0);

		prefDim = ZioUtil.readDimension(aStream);
		prefLoc = ZioUtil.readPoint(aStream);

		if (prefLoc != null && prefDim != null)
		{
			setLocation(prefLoc);
			setPreferredSize(prefDim);
		}
	}

	@Override
	public void zioWrite(ZoutStream aStream) throws IOException
	{
		aStream.writeVersion(0);

		ZioUtil.writeDimension(aStream, prefDim);
		ZioUtil.writePoint(aStream, prefLoc);
	}

	/**
	 * Helper method to set the location of this WaftPanel relative to its parent.
	 * <p>
	 * Adjustments are made to our constraints associated with the layout manager. This is needed so that when the layout
	 * manager is (re)validated any movement or resize changes made will stick. Note we assume that we are placed in a
	 * MigLayout manager.
	 */
	private void setLocationAndLock(Point aLoc)
	{
		// Adjust our constraints associated in the layout manager. This is needed so that when the layout manager is
		// (re)validated any movement or resize changes made will stick.
		// Note we assume that we are placed in a MigLayout manager.
		var rootLayout = (MigLayout) parentComp.getLayout();
		if (rootLayout == null)
		{
			super.setLocation(aLoc.x, aLoc.y);
			return;
		}

		rootLayout.setComponentConstraints(this, "pos " + aLoc.x + "px " + aLoc.y + "px");
		rootLayout.invalidateLayout(null);
	}

}
