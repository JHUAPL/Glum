package glum.gui.panel;

import glum.gui.GuiUtil;
import java.awt.*;
import javax.swing.*;

public class ShadePane extends JComponent
{
	// Communicator vars
	protected Component targComp;
	protected RootPaneContainer rootPane;

	// State vars
	Color refColor;

	/**
	 * Constructor
	 */
	public ShadePane(Component aComp)
	{
		targComp = aComp;
		rootPane = null;

		refColor = new Color(192, 192, 192, 128);
	}

	public void setColor(Color aColor)
	{
		refColor = aColor;
	}

	@Override
	public void setVisible(boolean isVisible)
	{
		JLayeredPane layeredPane;

		// Initialize the GUI if it has not been initialed
		// Once initialized rootPane != null
		if (rootPane == null)
			initializeGui();

		super.setVisible(isVisible);

		// Bail if this ShadePane is no longer visible
		if (isVisible == false)
			return;

		// Ensure this ShadePane is at the top
		layeredPane = rootPane.getLayeredPane();
		layeredPane.moveToFront(this);
	}

	/**
	 * Initialize and sets up the ShadePane. This method should be called only on the first time it is set visible.
	 */
	protected void initializeGui()
	{
		JLayeredPane layeredPane;
		ComponentTracker aTracker;

		// Ensure this method is not called twice
		if (rootPane != null)
			throw new RuntimeException("ShadePane.initializeGui() has been already called.");

		// Retrieve the associated rootPane and layeredPane
		rootPane = GuiUtil.getRootPaneContainer(targComp);

		// Add the GlassPane into the layeredPane
		layeredPane = rootPane.getLayeredPane();
		layeredPane.setLayer(this, JLayeredPane.PALETTE_LAYER);
		layeredPane.add(this);
		setLocation(targComp.getLocation());
		setSize(targComp.getSize());
		layeredPane.validate();

		// Set up a ComponentTracker to keep this ShadePane linked to the rootPane
		aTracker = new ComponentTracker(this);
		aTracker.setResizedTracker(rootPane.getContentPane());

		// Set up our initial size
		setSize(rootPane.getContentPane().getSize());
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		Point pt1, pt2;

		// Transform the location of the targComp to our screen coordinates
		pt1 = targComp.getLocation();
		pt2 = SwingUtilities.convertPoint(targComp.getParent(), pt1, this);

		g.setColor(refColor);

		g.setClip(null);
		g.fillRect(pt2.x, pt2.y, targComp.getWidth(), targComp.getHeight());
	}

}
