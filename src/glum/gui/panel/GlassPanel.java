package glum.gui.panel;

import glum.gui.GuiUtil;
import glum.zio.*;

import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;

public abstract class GlassPanel extends GPanel implements ZioObj
{
	// State vars
	protected Component myGlassPane;

	public GlassPanel(Component aRefParent)
	{
		this(aRefParent, PaneType.GlassPanel);
	}

	public GlassPanel(Component aRefParent, PaneType aType)
	{
		super();

		if (aType == PaneType.GlassPanel)
			myGlassPane = new GlassPane(aRefParent, this);
		else if (aType == PaneType.GlassPanelAncient)
			myGlassPane = new GlassPaneAncient(aRefParent, this);
		else
			myGlassPane = new StandardPane(aRefParent, this);
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
