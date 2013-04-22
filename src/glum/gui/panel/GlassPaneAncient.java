package glum.gui.panel;

import glum.gui.GuiUtil;

import java.awt.*;
import javax.swing.*;

import net.miginfocom.swing.MigLayout;

public class GlassPaneAncient extends JComponent
{
	// Communicator vars
	protected Component refParent;
	protected JComponent displayPane;

	/**
	 * Constructor
	 */
	public GlassPaneAncient(Component aRefParent, JComponent aDisplayPane)
	{
		// Communicator vars
		refParent = aRefParent;
		displayPane = aDisplayPane;

		// Build the GUI
		setLayout(new MigLayout("", "0[center,grow]0", "0[center,grow]0"));
		add(aDisplayPane, "span 1");

		GlassPaneListener aListener = new GlassPaneListener(this, aDisplayPane);
		addMouseListener(aListener);
		addMouseMotionListener(aListener);
	}

	@Override
	public void setVisible(boolean isVisible)
	{
		RootPaneContainer rootPane;

		// Update the RootPane to use this GlassPane
		rootPane = GuiUtil.getRootPaneContainer(refParent);
		if (isVisible == true)
			rootPane.setGlassPane(this);
//		else if (isVisible == false && rootPane.getGlassPane() == this)
//			rootPane.setGlassPane(null);

		super.setVisible(isVisible);
	}

	@Override
	protected void paintComponent(Graphics g)
	{
		g.setColor(new Color(192, 192, 192, 192));
		g.fillRect(0, 0, getWidth(), getHeight());
	}

}
