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

import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;

import javax.swing.border.BevelBorder;

import glum.gui.GuiUtil;
import glum.zio.*;

/**
 * Panel that is typically used to display a model panel that pops up over a top level component.
 *
 * @author lopeznr1
 */
public abstract class GlassPanel extends GPanel implements ZioObj
{
	// State vars
	private Component myGlassPane;

	/** Standard Constructor */
	public GlassPanel(Component aRefParent)
	{
		this(aRefParent, PaneType.GlassPanel);
	}

	/** Complex Constructor */
	public GlassPanel(Component aRefParent, PaneType aType)
	{
		if (aType == PaneType.GlassPanel)
			myGlassPane = new GlassPane(aRefParent, this);
		else if (aType == PaneType.GlassPanelAncient)
			myGlassPane = new GlassPaneAncient(aRefParent, this);
		else
			myGlassPane = new StandardPane(aRefParent, this);

		setBorder(new BevelBorder(BevelBorder.RAISED));
	}

	/**
	 * Call this method to change the end-user's ability to resize this component. Note this call is just a suggestion
	 * and may be totally ignored by the GUI toolkit.
	 */
	public void setResizable(boolean aBool)
	{
		if (myGlassPane instanceof GlassPane)
			((GlassPane)myGlassPane).setResizable(false);
	}

	/**
	 * Call this method if you want the GlassPanel to block until the panel has
	 * been hidden.
	 */
	public void setVisibleAsModal()
	{
		setVisible(true);

		if (myGlassPane instanceof GlassPane)
			GuiUtil.modalWhileVisible(myGlassPane);
		else
			GuiUtil.modalWhileVisible(getParent());
	}

	@Override
	public void setSize(Dimension aDim)
	{
		if (myGlassPane instanceof GlassPane)
			((GlassPane)myGlassPane).childComp.setSize(aDim);
		else
			super.setSize(aDim);
	}

	@Override
	public void setSize(int width, int height)
	{
		setSize(new Dimension(width, height));
	}

	@Override
	public void setVisible(boolean isVisible)
	{
//		// Ensure this panel is visible
//		if (isVisible() == false)
//		super.setVisible(isVisible);

		myGlassPane.setVisible(isVisible);
	}

	@Override
	public void repaint()
	{
		if (myGlassPane == null)
			return;

		if (myGlassPane.isVisible() == true)
			myGlassPane.repaint();
	}

	@Override
	public void zioRead(ZinStream aStream) throws IOException
	{
		aStream.readVersion(0);

		if (myGlassPane instanceof ZioObj)
			((ZioObj)myGlassPane).zioRead(aStream);
	}

	@Override
	public void zioWrite(ZoutStream aStream) throws IOException
	{
		aStream.writeVersion(0);

		if (myGlassPane instanceof ZioObj)
			((ZioObj)myGlassPane).zioWrite(aStream);
	}

}
