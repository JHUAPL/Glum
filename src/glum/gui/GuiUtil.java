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
package glum.gui;

import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.util.Collection;

import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeListener;

import glum.gui.icon.IconUtil;
import glum.reflect.Function;

/**
 * Collection of AWT/Swing utilities.
 *
 * @author lopeznr1
 */
public class GuiUtil
{
	/**
	 * Method to examine the labels and returns the size of the largest button.
	 */
	public static Dimension computePreferredJButtonSize(String... aLabelArr)
	{
		Dimension maxDim = null;
		var tmpB = new JButton("");

		// Find the label that requires the largest dimension
		for (String aStr : aLabelArr)
		{
			if (aStr == null)
				aStr = "";

			tmpB.setText(aStr);
			var tmpDim = tmpB.getPreferredSize();
			if (maxDim == null || maxDim.getWidth() < tmpDim.getWidth())
				maxDim = tmpDim;
		}

		return maxDim;
	}

	/**
	 * Creates a JButton with the specified settings
	 */
	public static JButton createJButton(String aTitle, ActionListener aActionListener)
	{
		var tmpB = new JButton(aTitle);
		tmpB.addActionListener(aActionListener);

		return tmpB;
	}

	/**
	 * Creates the JRadioButton with the following attributes.
	 * <p>
	 * TODO: Do not remove this method until DistMaker has been updated...
	 */
	public static JButton createJButton(String aTitle, ActionListener aActionListener, Font aFont)
	{
		var tmpB = new JButton(aTitle);
		tmpB.addActionListener(aActionListener);
		tmpB.setFont(aFont);

		return tmpB;
	}

	public static JButton createJButton(String aTitle, ActionListener aActionListener, Dimension aDimension)
	{
		var tmpB = new JButton(aTitle);
		tmpB.addActionListener(aActionListener);

		// Force a dimension
		if (aDimension != null)
		{
			tmpB.setMinimumSize(aDimension);
			tmpB.setMaximumSize(aDimension);
			tmpB.setPreferredSize(aDimension);
		}

		return tmpB;
	}

	public static JButton createJButton(Icon aIcon, ActionListener aActionListener)
	{
		return createJButton(aIcon, aActionListener, null);
	}

	public static JButton createJButton(Icon aIcon, ActionListener aActionListener, String aToolTip)
	{
		var tmpB = new JButton(aIcon);
		tmpB.addActionListener(aActionListener);

		if (aToolTip != null)
			tmpB.setToolTipText(aToolTip);

		return tmpB;
	}

	/**
	 * Creates the JButton with the specified resource icon.
	 */
	public static JButton createJButtonViaResource(ActionListener aHandler, String aResourcePath)
	{
		return createJButtonViaResource(aHandler, aResourcePath, null);
	}

	public static JButton createJButtonViaResource(ActionListener aHandler, String aResourcePath, String aToolTip)
	{
		var tmpB = new JButton(IconUtil.loadIcon(aResourcePath));
		tmpB.addActionListener(aHandler);

		if (aToolTip != null)
			tmpB.setToolTipText(aToolTip);

		return tmpB;
	}

	/**
	 * Creates a JCheckBox with the specified settings
	 */
	public static JCheckBox createJCheckBox(String aTitle, ActionListener aActionListener)
	{
		var tmpCB = new JCheckBox(aTitle);
		tmpCB.addActionListener(aActionListener);

		return tmpCB;
	}

	/**
	 * Creates a JLabel with the specified settings
	 */
	public static JLabel createJLabel(String aTitle, Font aFont)
	{
		return createJLabel(aTitle, JLabel.LEADING, aFont);
	}

	public static JLabel createJLabel(String aTitle, int aAlignment, Font aFont)
	{
		var tmpL = new JLabel(aTitle, aAlignment);
		if (aFont != null)
			tmpL.setFont(aFont);

		return tmpL;
	}

	/**
	 * Creates the {@link JComboBox} with the following attributes.
	 */
	public static <G1> JComboBox<G1> createComboBox(ActionListener aListener, Collection<G1> aItemC)
	{
		var retBox = new JComboBox<G1>();
		for (G1 aItem : aItemC)
			retBox.addItem(aItem);

		retBox.addActionListener(aListener);

		return retBox;
	}

	/**
	 * Creates the JRadioButton with the following attributes.
	 */
	public static JRadioButton createJRadioButton(ItemListener aListener, String aLabel)
	{
		var retRB = new JRadioButton(aLabel);
		retRB.addItemListener(aListener);

		return retRB;
	}

	/**
	 * Creates the JRadioButton with the following attributes.
	 * <p>
	 * TODO: Do not remove this method until DistMaker has been updated...
	 */
	public static JRadioButton createJRadioButton(ItemListener aListener, String aLabel, Font aFont)
	{
		var retRB = new JRadioButton(aLabel);
		retRB.addItemListener(aListener);
		retRB.setFont(aFont);

		return retRB;
	}

	/**
	 * Utility method for creating a visual thin divider
	 * <p>
	 * Typically added to MigLayout (or like manager) with: add(aComp, "growx,h 4!,span,wrap");
	 */
	public static JPanel createDivider()
	{
		var retPanel = new JPanel();
		retPanel.setBorder(new BevelBorder(BevelBorder.RAISED));

		return retPanel;
	}

	/**
	 * Creates an uneditable JTextArea with the settings: non-opaque, line wrap enabled and word wrap enabled.
	 */
	public static JTextArea createUneditableTextArea(int rows, int cols)
	{
		var tmpTA = new JTextArea("", rows, cols);
		tmpTA.setEditable(false);
		tmpTA.setOpaque(false);
		tmpTA.setLineWrap(true);
		tmpTA.setWrapStyleWord(true);

		return tmpTA;
	}

	/**
	 * Creates an uneditable JTextPane configured with non-opaque and content type of text/html.
	 */
	public static JTextPane createUneditableTextPane()
	{
		var tmpTP = new JTextPane();
		tmpTP.setEditable(false);
		tmpTP.setOpaque(false);
		tmpTP.setContentType("text/html");

		return tmpTP;
	}

	/**
	 * Creates an uneditable JTextField
	 */
	public static JTextField createUneditableTextField(String aTitle)
	{
		var tmpTF = new JTextField(aTitle);
		tmpTF.setBorder(null);
		tmpTF.setEditable(false);
		tmpTF.setOpaque(false);

		return tmpTF;
	}

	/**
	 * Utility helper method to create a JButton with the specified configuration.
	 *
	 * @param aListener
	 *        A Listener registered with the JButton.
	 * @param aIcon
	 *        The icon associated with the button
	 */
	public static JButton formButton(ActionListener aListener, Icon aIcon)
	{
		var retB = new JButton();
		retB.addActionListener(aListener);
		retB.setIcon(aIcon);
		return retB;
	}

	/**
	 * Utility helper method to create a JButton with the specified configuration.
	 *
	 * @param aListener
	 *        A Listener registered with the JButton.
	 * @param aImage
	 *        The image to be used as an icon.
	 * @param aToolTip
	 *        The tool tip associated with the JButton.
	 */
	public static JButton formButton(ActionListener aListener, BufferedImage aImage, String aToolTip)
	{
		var tmpIcon = new ImageIcon(aImage);

		var retB = new JButton();
		retB.setIcon(tmpIcon);
		retB.addActionListener(aListener);
		retB.setToolTipText(aToolTip);

		return retB;
	}

	/**
	 * Utility helper method to create a JButton with the specified configuration.
	 *
	 * @param aListener
	 *        A Listener registered with the JButton.
	 * @param aTitle
	 *        The text title of the JButton.
	 */
	public static JButton formButton(ActionListener aListener, String aTitle)
	{
		var retB = new JButton();
		retB.addActionListener(aListener);
		retB.setText(aTitle);
		return retB;
	}

	/**
	 * Utility helper method to create a JToggleButton with the specified configuration.
	 *
	 * @param aListener
	 *        A Listener registered with the JButton.
	 * @param aPriIcon
	 *        The icon to be used as the primary (unselected) icon.
	 * @param aSecIcon
	 *        The icon to be used when the secondary (selected) icon.
	 * @param aToolTip
	 *        The tool tip associated with the JToggleButton.
	 */
	public static JToggleButton formToggleButton(ActionListener aListener, Icon aPriIcon, Icon aSecIcon)
	{
		var retTB = new JToggleButton(aPriIcon, false);
		retTB.setSelectedIcon(aSecIcon);
		retTB.addActionListener(aListener);

		return retTB;
	}

	/**
	 * Utility method to link a set of radio buttons together
	 */
	public static ButtonGroup linkRadioButtons(JRadioButton... aButtonArr)
	{
		var retGroup = new ButtonGroup();
		for (var aItem : aButtonArr)
			retGroup.add(aItem);

		return retGroup;
	}

	/**
	 * Utility method to link a set of radio buttons together
	 */
	public static ButtonGroup linkRadioButtons(Collection<JRadioButton> aButtonC)
	{
		var retGroup = new ButtonGroup();
		for (var aItem : aButtonC)
			retGroup.add(aItem);

		return retGroup;
	}

	/**
	 * Utility method to locate the RootPaneContainer for the specified Component
	 */
	public static RootPaneContainer getRootPaneContainer(Component aComponent)
	{
		// Check to see if the Component is an actual RootPaneContainer
		if (aComponent instanceof RootPaneContainer)
			return (RootPaneContainer) aComponent;

		// Attempt to locate the RootPaneContainer (through our stack)
		var retParent = aComponent.getParent();
		while (retParent != null && (retParent instanceof RootPaneContainer) == false)
			retParent = retParent.getParent();

		// Bail if we failed to find the RootPaneContainer
		if (retParent instanceof RootPaneContainer == false)
			throw new RuntimeException("No valid (grand)parent associated with GlassPane.");

		return (RootPaneContainer) retParent;
	}

	/**
	 * Utility method to locate all of the subcomponents contained in aContainer which are an instance of searchClass
	 */
	public static void locateAllSubComponents(Container aContainer, Collection<Component> itemList,
			Class<?>... searchClassArr)
	{
		for (Component aComponent : aContainer.getComponents())
		{
			for (Class<?> aClass : searchClassArr)
			{
				if (aClass.isInstance(aComponent) == true)
				{
					itemList.add(aComponent);
					break;
				}
			}

			if (aComponent instanceof Container)
				locateAllSubComponents((Container) aComponent, itemList, searchClassArr);
		}
	}

	/**
	 * Utility method to force a Component to act as modal while it is visible
	 * <p>
	 * Source: http://stackoverflow.com/questions/804023/how-do-i-simulate-a-modal-dialog-from-within-an-applet
	 */
	public static void modalWhileVisible(Component aComponent)
	{
		// Bail if not called from the EventDispatchThread
		if (SwingUtilities.isEventDispatchThread() == false)
			throw new RuntimeException("Visibility for modal components must be changed via the Event thread.");

		synchronized (aComponent)
		{
			try
			{
				var theQueue = aComponent.getToolkit().getSystemEventQueue();
				while (aComponent.isVisible())
				{
//System.out.println("About to dispatch event... component.isVisible():" + aComponent.isVisible());
					var event = theQueue.getNextEvent();
					var source = event.getSource();
					if (event instanceof ActiveEvent)
					{
						((ActiveEvent) event).dispatch();
					}
					else if (source instanceof Component)
					{
						((Component) source).dispatchEvent(event);
					}
					else if (source instanceof MenuComponent)
					{
						((MenuComponent) source).dispatchEvent(event);
					}
					else
					{
						System.err.println("Unable to dispatch: " + event);
					}
				}
			}
			catch (InterruptedException ignored)
			{
			}
		}
	}

	/**
	 * Utility to call a specific method (methodName) with specific parameters (aParamArr) on aComp and on all of the
	 * child subcomponents. The method will only be called if the components are an instance of refMatchClass.
	 * <p>
	 * This is useful so that a component and all of its children can be disabled, hidden, etc</br>
	 * Example: GuiUtil.callMethod(myPanel, setEnabled, false);
	 * <p>
	 * Be aware, this is rather expensive, so do not call in time critical applications.
	 */
	public static void callMethod(Component aComp, Class<?> refMatchClass, String aMethodName, Object... aParamArr)
	{
		// Construct the associated type array
		var typeArr = new Class<?>[0];
		if (aParamArr.length > 0)
		{
			// Determine the types of the specified arguments
			typeArr = new Class[aParamArr.length];
			for (int c1 = 0; c1 < typeArr.length; c1++)
			{
				typeArr[c1] = null;
				if (aParamArr[c1] != null)
					typeArr[c1] = aParamArr[c1].getClass();
			}
		}

		// Call the helper version
		callMethodHelper(aComp, refMatchClass, aMethodName, typeArr, aParamArr);
	}

	/**
	 * Helper method to callMethod
	 */
	private static void callMethodHelper(Component aComp, Class<?> refMatchClass, String aMethodName,
			Class<?>[] aTypeArr, Object[] aParamArr)
	{
		// Locate and call the actual method
		if (refMatchClass.isInstance(aComp) == true)
		{
			try
			{
				var tmpFunction = new Function(aComp, aMethodName, aTypeArr);
				tmpFunction.invoke(aParamArr);
			}
			catch (NoSuchMethodException aExp1)
			{
				throw new RuntimeException("Failed to locate valid function. Method:" + aMethodName, aExp1);
			}
			catch (Exception aExp2)
			{
				throw new RuntimeException("Failed to execute function. Method:" + aMethodName, aExp2);
			}
		}

		// Bail if we do not have subcomponents
		if (aComp instanceof Container == false)
			return;

		// Recurse down our children
		var subCompArr = ((Container) aComp).getComponents();
		for (Component aSubComp : subCompArr)
			callMethodHelper(aSubComp, refMatchClass, aMethodName, aTypeArr, aParamArr);
	}

	/**
	 * Utility method to recursively change the enable state of all Components contained by the specified Container.
	 *
	 * @param aContainer
	 *        The Container of interest.
	 * @param aBool
	 *        Boolean used to define the enable state.
	 */
	public static void setEnabled(Container aContainer, boolean aBool)
	{
		for (Component aComp : aContainer.getComponents())
		{
			aComp.setEnabled(aBool);
			if (aComp instanceof Container)
				setEnabled((Container) aComp, aBool);
		}
	}

	/**
	 * Utility method to set the enable state on all of the specified components.
	 */
	public static void setEnabled(boolean aBool, Component... aComponentArr)
	{
		for (Component aComp : aComponentArr)
			aComp.setEnabled(aBool);
	}

	/**
	 * Utility method to set the enable state on all of the specified components.
	 */
	public static void setEnabled(boolean aBool, Collection<? extends Component> aComponentC)
	{
		for (Component aComp : aComponentC)
			aComp.setEnabled(aBool);
	}

	/**
	 * Utility method that will call doClick() on the selected RadioButton
	 */
	public static void doClickSelectedButton(JRadioButton... buttonArr)
	{
		for (int c1 = 0; c1 < buttonArr.length; c1++)
		{
			if (buttonArr[c1].isSelected() == true)
				buttonArr[c1].doClick();
		}
	}

	/**
	 * Utility method that returns a bitmask composed of the selection state of the specified buttons.
	 * <p>
	 * The array is transformed such that the least significant bit is assumed to have index position 0 with the higher
	 * bits corresponding to a larger (array) index.
	 * <p>
	 * A maximum of 32 buttons is support. Passing in more than 32 buttons will result in an exception.
	 */
	public static int getSelectionStateAsBitMask(AbstractButton... aButtonArr)
	{
		if (aButtonArr.length > 32)
			throw new RuntimeException("Improper API call. Max of 32 buttons supported. Passed: " + aButtonArr.length);

		int retBitMask = 0;
		for (int c1 = 0; c1 < aButtonArr.length; c1++)
		{
			if (aButtonArr[c1].isSelected() == true)
				retBitMask |= 1 << c1;
		}

		return retBitMask;
	}

	/**
	 * Utility method that updates the selection state of the provided buttons to reflect the specified bitmask.
	 * <p>
	 * The array is transformed such that the least significant bit is assumed to have index position 0 with the higher
	 * bits corresponding to a larger (array) index.
	 * <p>
	 * A maximum of 32 buttons is support. Passing in more than 32 buttons will result in an exception.
	 */
	public static void setSelectionStateFromBitMask(int aBitMask, AbstractButton... aButtonArr)
	{
		if (aButtonArr.length > 32)
			throw new RuntimeException("Improper API call. Max of 32 buttons supported. Passed: " + aButtonArr.length);

		for (int c1 = 0; c1 < aButtonArr.length; c1++)
		{
			var tmpBool = false;
			if (((0x01 << c1) & aBitMask) != 0)
				tmpBool = true;

			aButtonArr[c1].setSelected(tmpBool);
		}
	}

	/**
	 * Utility method to update a JSlider without triggering notifications to its registered listeners.
	 */
	public static void updateSlider(JSlider aSlider, int aVal)
	{
		var tmpArr = aSlider.getChangeListeners();

		for (ChangeListener aListener : tmpArr)
			aSlider.removeChangeListener(aListener);

		aSlider.setValue(aVal);

		for (ChangeListener aListener : tmpArr)
			aSlider.addChangeListener(aListener);
	}

	/**
	 * Utility method that checks to ensure the current thread is running on the ATW thread. If it is NOT then the
	 * specified Runnable will be posted so that it is called on the AWT thread. If it is running on the AWT thread then
	 * nothing will happen and this method will return false.
	 * <p>
	 * Typically this utility method is called at the start of a function to ensure it is on the AWT thread, and if not
	 * then schedule the function onto the AWT thread. Thus it is strongly advisable that if this method returns true the
	 * caller should immediately exit.
	 * <p>
	 * <B>Typical usage within a method:</B>
	 *
	 * <PRE>
	 * public void actionPerformed(aEvent)
	 * {
	 *    // Ensure this method is run on the AWT thread
	 *    Runnable tmpRunnable = ()-> actionPerformed(aEvent);
	 *    if (redispatchOnAwtIfNeeded(this, "actionPerformed", aEvent) = true)
	 *       return;
	 *
	 *    // Do normal work ...
	 * }
	 * </PRE>
	 */
	public static boolean redispatchOnAwtIfNeeded(Runnable aRunnable)
	{
		// Do nothing if this is the AWT thread
		if (SwingUtilities.isEventDispatchThread() == true)
			return false;

		SwingUtilities.invokeLater(aRunnable);
		return true;
	}

}
