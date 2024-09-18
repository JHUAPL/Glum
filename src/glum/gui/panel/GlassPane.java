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
import java.awt.event.*;
import java.io.IOException;
import java.util.LinkedHashSet;

import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

import glum.gui.GuiUtil;
import glum.zio.*;
import net.miginfocom.swing.MigLayout;

/**
 * GUI element useful for holding the provided child UI components. This GUI component will be displayed as a floating
 * "modal" glass pane over it's parent component.
 *
 * @author lopeznr1
 */
public class GlassPane extends JComponent implements ZioObj, ComponentListener
{
	// Communicator vars
	private final Component parentComp;
	protected final WaftPanel childComp;

	private RootPaneContainer rootPane;

	// State vars
	private Color fillColor;
	private CustomFocusTraversalPolicy myFocusPolicy;
	private MigLayout workLayout;

	/** Standard Constructor */
	public GlassPane(Component aRefParent, JComponent aDisplayPane)
	{
		// Communicator vars
		parentComp = aRefParent;
		childComp = new WaftPanel(aDisplayPane, this);

		// Build the GUI
		workLayout = new MigLayout();
		setLayout(workLayout);

//		add("pos 150px 100px", childComp);
		add("", childComp);
		validate();
		revalidate();

		// Register for events of interest
		childComp.addComponentListener(this);

		// Set up the GlassPanelListener handler
		GlassPaneListener aListener = new GlassPaneListener(this, childComp);
		addMouseListener(aListener);
		addMouseMotionListener(aListener);

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

	/**
	 * Method to change the end users ability to resize a panel. This method call is just a suggestion and the GUI
	 * toolkit may totally ignore this call.
	 */
	public void setResizable(boolean aBool)
	{
		childComp.setResizable(aBool);
	}

	@Override
	public void componentResized(ComponentEvent aEvent)
	{
		; // Nothing to do
	}

	@Override
	public void componentMoved(ComponentEvent aEvent)
	{
		if (aEvent.getSource() == childComp)
			workLayout.setComponentConstraints(childComp,
					"pos " + childComp.getLocation().x + " " + childComp.getLocation().y);
	}

	@Override
	public void componentShown(ComponentEvent aEvent)
	{
		; // Nothing to do
	}

	@Override
	public void componentHidden(ComponentEvent aEvent)
	{
		; // Nothing to do
	}

	@Override
	public void setVisible(boolean aIsVisible)
	{
		// Initialize the GUI if it has not been initialed
		// Once initialized rootPane != null
		if (rootPane == null)
			initializeGui();

		super.setVisible(aIsVisible);

		// Activate/Deactive managing the focus
		setFocusCycleRoot(aIsVisible);
		setFocusTraversalPolicyProvider(aIsVisible);

		// Bail if this GlassPane is no longer visible
		if (aIsVisible == false)
			return;

		// Ensure this GlassPane is at the top
		var layeredPane = rootPane.getLayeredPane();
		layeredPane.moveToFront(this);

		// Clear out the old focus (from the underlay component)
		var tmpKFM = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		tmpKFM.clearGlobalFocusOwner();

		// Wait and ensure that the GlassPane is visible, before we request the focus
		SwingUtilities.invokeLater(new Runnable() {
			@Override
			public void run()
			{
				var tmpComponent = myFocusPolicy.getDefaultComponent(null);
				if (tmpComponent != null)
					tmpComponent.requestFocusInWindow();

				repaint();
			}
		});
	}

	@Override
	public void zioRead(ZinStream aStream) throws IOException
	{
		aStream.readVersion(0);
		childComp.zioRead(aStream);
	}

	@Override
	public void zioWrite(ZoutStream aStream) throws IOException
	{
		aStream.writeVersion(0);
		childComp.zioWrite(aStream);
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
		if (aComp == childComp)
			System.out.println("displayPane being " + aStr + "...");
		else if (aComp == rootPane)
			System.out.println("rootPane being " + aStr + "...");
		else
			System.out.println("Undefined object being " + aStr + "...");
	}

	/**
	 * Sets up the GUI for the GlassPane
	 */
	private void initializeGui()
	{
		// Ensure this method is not called twice
		if (rootPane != null)
			throw new RuntimeException("GlassPane.initializeGui() has been already called.");

		// Retrieve the associated rootPane and layeredPane
		rootPane = GuiUtil.getRootPaneContainer(parentComp);

		// Add the GlassPane into the layeredPane
		var layeredPane = rootPane.getLayeredPane();
		layeredPane.setLayer(this, JLayeredPane.PALETTE_LAYER);
		layeredPane.add(this);
		setSize(layeredPane.getSize());
//		layeredPane.validate();
		layeredPane.revalidate();

		// Determine initial location and size of the child component
		var prefDim = childComp.getPrefDim();
		if (prefDim == null)
			prefDim = childComp.getPreferredSize();

		var prefLoc = childComp.getPrefLoc();
		if (prefLoc == null)
			prefLoc = new Point(getWidth() / 2 - prefDim.width / 2, getHeight() / 2 - prefDim.height / 2);

//		System.out.println("prefLoc: " + prefLoc + "   prefDim: " + prefDim);
		childComp.setLocation(prefLoc);
		childComp.setSize(prefDim);
//childComp.revalidate();
//layeredPane.revalidate();
//this.validate();
//this.revalidate();

		// Setup the focus policy to consist of any child components of type
		// AbstractButton, JComboBox, JTextField, JTable
		var compS = new LinkedHashSet<Component>();
		Class<?>[] classArr = { AbstractButton.class, JComboBox.class, JTextField.class, JTable.class };
		GuiUtil.locateAllSubComponents(childComp, compS, classArr);

		myFocusPolicy = new CustomFocusTraversalPolicy();
		for (Component aItem : compS)
			myFocusPolicy.addComponent(aItem);

		setFocusCycleRoot(true);
		setFocusTraversalPolicyProvider(true);
		setFocusTraversalPolicy(myFocusPolicy);

		// Set up a ComponentTracker to keep this GlassPane linked to the
		// appropriate Components of interest
		var tmpTracker = new ComponentTracker(this);
		tmpTracker.setHiddenTracker((Component) rootPane);
		tmpTracker.setResizedTracker(layeredPane);
	}

}

/**
 * Helper class to capture all mouse interactions and redispatch the events only to the subcomponents associated with
 * the container.
 *
 * @author lopeznr1
 */
class GlassPaneListener extends MouseInputAdapter
{
	private Component glassPane;
	private Container contentPane;

	public GlassPaneListener(Component aGlassPane, Container aContentPane)
	{
		glassPane = aGlassPane;
		contentPane = aContentPane;
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		redispatchMouseEvent(e, false);
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		redispatchMouseEvent(e, false);
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		redispatchMouseEvent(e, false);
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		redispatchMouseEvent(e, false);
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		redispatchMouseEvent(e, false);
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		redispatchMouseEvent(e, false);
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		redispatchMouseEvent(e, true);
	}

	// A more finished version of this method would
	// handle mouse-dragged events specially.
	private void redispatchMouseEvent(MouseEvent aEvent, boolean aIsRepaint)
	{
		var glassPanePoint = aEvent.getPoint();
		var container = contentPane;
		var containerPoint = SwingUtilities.convertPoint(glassPane, glassPanePoint, contentPane);

		// The mouse event is probably over the content pane.
		// Find out exactly which component it's over.
		var component = SwingUtilities.getDeepestComponentAt(container, containerPoint.x, containerPoint.y);
		if (component != null)
		{
			// Forward events over the to the component
			dispatchEvent(component, aEvent);
		}

		// Update the glass pane if requested.
		if (aIsRepaint)
			glassPane.repaint();
	}

	private void dispatchEvent(Component aComponent, MouseEvent aEvent)
	{
		if (aComponent == null || aEvent == null)
			return;

		var tmpPt = SwingUtilities.convertPoint(glassPane, aEvent.getPoint(), aComponent);
		if (tmpPt == null)
			return;

		aComponent.dispatchEvent(new MouseEvent(aComponent, aEvent.getID(), aEvent.getWhen(), aEvent.getModifiersEx(),
				tmpPt.x, tmpPt.y, aEvent.getClickCount(), aEvent.isPopupTrigger()));
	}

}
