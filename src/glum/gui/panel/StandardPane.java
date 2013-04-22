package glum.gui.panel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Graphics;

import javax.swing.JComponent;
import javax.swing.RootPaneContainer;
import glum.gui.GuiUtil;
import glum.gui.panel.CustomFocusTraversalPolicy;
import net.miginfocom.swing.MigLayout;

public class StandardPane extends JComponent
{
	// Communicator vars
	protected Component refParent;
	protected JComponent displayPane;

	protected RootPaneContainer rootPane;

	// State vars
	protected Color fillColor;
	protected CustomFocusTraversalPolicy myFocusPolicy;

	/**
	 * Constructor
	 */
	public StandardPane(Component aRefParent, JComponent aDisplayPane)
	{
		// Communicator vars
		refParent = aRefParent;
		displayPane = aDisplayPane;

		// Build the GUI
		setLayout(new MigLayout("", "0[center,fill]0", "0[center,fill]0"));
		add(aDisplayPane, "span 1");

		rootPane = null;
		fillColor = new Color(96, 96, 96, 96);
	}

	/**
	 * Set in an alternative shade color.
	 */
	public void setFillColor(Color aColor)
	{
		fillColor = aColor;
	}

	@Override
	public void setVisible(boolean isVisible)
	{
		// Initialize the GUI if it has not been initialed
		// Once initialized rootPane != null
		if (rootPane == null)
			initializeGui();

		((Component)rootPane).setVisible(isVisible);
	}

	/**
	 * Sets up the GlassPane
	 */
	protected void initializeGui()
	{
		ComponentTracker aTracker;

		// Ensure this method is not called twice
		if (rootPane != null)
			throw new RuntimeException("GlassPane.initializeGui() has been already called.");

		// Retrieve the associated rootPane and layeredPane
		if (refParent == null)
			rootPane = GuiUtil.getRootPaneContainer(displayPane);
		else
			rootPane = GuiUtil.getRootPaneContainer(refParent);

		// Set up a ComponentTracker to keep this ShadePane linked to the rootPane
		aTracker = new ComponentTracker(this);
		aTracker.setResizedTracker((Component)rootPane);
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		g.setColor(fillColor);
		g.fillRect(0, 0, getWidth(), getHeight());
	}

	/**
	 * Utility method to help with debugging
	 */
	protected void debugMsg(Component aComp, String aStr)
	{
		if (aComp == displayPane)
			System.out.println("displayPane being " + aStr + "...");
		else if (aComp == rootPane)
			System.out.println("rootPane being " + aStr + "...");
		else
			System.out.println("Undefined object being " + aStr + "...");
	}

}
