package glum.gui.panel;

import glum.gui.GuiUtil;
import glum.gui.panel.CustomFocusTraversalPolicy;
import glum.zio.ZinStream;
import glum.zio.ZoutStream;
import glum.zio.raw.ZioRaw;

import java.awt.*;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Set;
import javax.swing.*;
import javax.swing.event.MouseInputAdapter;

import net.miginfocom.swing.MigLayout;

import com.google.common.collect.Sets;

public class GlassPane extends JComponent implements ZioRaw, ComponentListener
{
	// Communicator vars
	protected Component parentComp;
	protected WaftPanel childComp;

	protected RootPaneContainer rootPane;
	
	// State vars
	protected Color fillColor;
	protected CustomFocusTraversalPolicy myFocusPolicy;
	protected MigLayout refLayout;

	public GlassPane(Component aRefParent, JComponent aDisplayPane)
	{
		// Communicator vars
		parentComp = aRefParent;
		childComp = new WaftPanel(aDisplayPane, this);

		// Build the GUI
		refLayout = new MigLayout();
		setLayout(refLayout);

//		add("pos 150px 100px", childComp);
		add("", childComp);
		this.validate();
		this.revalidate();
		
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
	 * Method to change the end users ability to resize a panel. This method call is just a suggestion
	 * and the GUI toolkit may totally ignore this call.
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
	public void componentMoved(ComponentEvent e)
	{
		if (e.getSource() == childComp)
		refLayout.setComponentConstraints(childComp, "pos " + childComp.getLocation().x + " " + childComp.getLocation().y);
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
	public void setVisible(boolean isVisible)
	{
		JLayeredPane layeredPane;
		KeyboardFocusManager aManager;

		// Initialize the GUI if it has not been initialed
		// Once initialized rootPane != null
		if (rootPane == null)
			initializeGui();
		
		super.setVisible(isVisible);
		
		// Activate/Deactive managing the focus
		setFocusCycleRoot(isVisible);
		setFocusTraversalPolicyProvider(isVisible);
		
		// Bail if this GlassPane is no longer visible
		if (isVisible == false)
			return;

		// Ensure this GlassPane is at the top
		layeredPane = rootPane.getLayeredPane();
		layeredPane.moveToFront(this);

		// Clear out the old focus (from the underlay component)
		aManager = KeyboardFocusManager.getCurrentKeyboardFocusManager();
		aManager.clearGlobalFocusOwner();
		
		// Wait and ensure that the GlassPane is visible, before we request the focus
		SwingUtilities.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				Component aComponent;
				
				aComponent = myFocusPolicy.getDefaultComponent(null);
				if (aComponent != null)
					aComponent.requestFocusInWindow();
				
				repaint();
			}
		});
	}	
	
	@Override
	public void zioReadRaw(ZinStream aStream) throws IOException
	{
		childComp.zioReadRaw(aStream);
	}
	
	@Override
	public void zioWriteRaw(ZoutStream aStream) throws IOException
	{
		childComp.zioWriteRaw(aStream);
	}
	
	/**
	 * Sets up the GUI for the GlassPane
	 */
	protected void initializeGui()
	{
		Set<Component> compSet;
		JLayeredPane layeredPane;
		ComponentTracker aTracker;
		Dimension prefDim;
		Point prefLoc;

		// Ensure this method is not called twice
		if (rootPane != null)
			throw new RuntimeException("GlassPane.initializeGui() has been already called.");

		// Retrieve the associated rootPane and layeredPane
		rootPane = GuiUtil.getRootPaneContainer(parentComp);

		// Add the GlassPane into the layeredPane
		layeredPane = rootPane.getLayeredPane();
		layeredPane.setLayer(this, JLayeredPane.PALETTE_LAYER);
		layeredPane.add(this);
		setSize(layeredPane.getSize());
//		layeredPane.validate();
		layeredPane.revalidate();		

		// Determine initial location and size of the child component
		prefDim = childComp.getPrefDim();
		if (prefDim == null)
			prefDim = childComp.getPreferredSize();
		
		prefLoc = childComp.getPrefLoc();
		if (prefLoc == null)
			prefLoc = new Point(getWidth()/2 - prefDim.width/2, getHeight()/2 - prefDim.height/2);

//		System.out.println("prefLoc: " + prefLoc + "   prefDim: " + prefDim);
		childComp.setLocation(prefLoc);
		childComp.setSize(prefDim);
//childComp.revalidate();		
//layeredPane.revalidate();
//this.validate();
//this.revalidate();
		
		// Setup the focus policy to consist of any child components of type
		// AbstractButton, JComboBox, JTextField, JTable
		compSet = Sets.newLinkedHashSet();
		Class<?>[] classArr = {AbstractButton.class, JComboBox.class, JTextField.class, JTable.class};
		GuiUtil.locateAllSubComponents(childComp, compSet, classArr);

		myFocusPolicy = new CustomFocusTraversalPolicy();
		for (Component aItem : compSet)
			myFocusPolicy.addComponent(aItem);

		setFocusCycleRoot(true);
		setFocusTraversalPolicyProvider(true);
		setFocusTraversalPolicy(myFocusPolicy);

		// Set up a ComponentTracker to keep this GlassPane linked to the
		// appropriate Components of interest
		aTracker = new ComponentTracker(this);
		aTracker.setHiddenTracker((Component)rootPane);
		aTracker.setResizedTracker(layeredPane);
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

}


/**
 * Utility class to capture all mouse interactions and redispatch the events only 
 * to the subcomponents associated with the container.
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

	//A more finished version of this method would
	//handle mouse-dragged events specially.
	private void redispatchMouseEvent(MouseEvent e, boolean repaint)
	{

		Point glassPanePoint = e.getPoint();
		Container container = contentPane;
		Point containerPoint = SwingUtilities.convertPoint(glassPane, glassPanePoint, contentPane);

		//The mouse event is probably over the content pane.
		//Find out exactly which component it's over.
		Component component;

		component = SwingUtilities.getDeepestComponentAt(container, containerPoint.x, containerPoint.y);
		if (component != null)
		{
			//Forward events over the to the component
			dispatchEvent(component, e);
		}

		// Update the glass pane if requested.
		if (repaint)
			glassPane.repaint();
	}

	private void dispatchEvent(Component aComponent, MouseEvent aEvent)
	{
		Point aPt;

		if (aComponent == null || aEvent == null)
			return;

		aPt = SwingUtilities.convertPoint(glassPane, aEvent.getPoint(), aComponent);
		if (aPt == null)
			return;

		aComponent.dispatchEvent(new MouseEvent(aComponent, aEvent.getID(), aEvent.getWhen(), aEvent.getModifiers(),
			aPt.x, aPt.y, aEvent.getClickCount(), aEvent.isPopupTrigger()));
	}

}
