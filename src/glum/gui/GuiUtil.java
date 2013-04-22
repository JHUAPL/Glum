package glum.gui;

import glum.gui.icon.IconUtil;
import glum.reflect.Function;
import glum.reflect.FunctionRunnable;

import java.awt.*;
import java.awt.event.ActionListener;
import java.util.Collection;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import javax.swing.event.ChangeListener;

public class GuiUtil
{
	/**
	 * Method to examine the labels and returns the size of the largest button.
	 */
	public static Dimension computePreferredJButtonSize(String... labels)
	{
		Dimension tmpDim, maxDim;
		JButton tmpB;

		maxDim = null;
		tmpB = new JButton("");

		// Find the label that requires the largest dimension
		for (String aStr : labels)
		{
			if (aStr == null)
				aStr = "";

			tmpB.setText(aStr);
			tmpDim = tmpB.getPreferredSize();

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
		JButton tmpB;

		tmpB = new JButton(aTitle);
		tmpB.addActionListener(aActionListener);
		return tmpB;
	}

	public static JButton createJButton(String aTitle, ActionListener aActionListener, Font aFont)
	{
		JButton tmpB;

		tmpB = new JButton(aTitle);
		tmpB.addActionListener(aActionListener);
		if (aFont != null)
			tmpB.setFont(aFont);

		return tmpB;
	}

	public static JButton createJButton(String aTitle, ActionListener aActionListener, Dimension aDimension)
	{
		JButton tmpB;

		tmpB = new JButton(aTitle);
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
		JButton tmpB;

		tmpB = new JButton(aIcon);
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
		JButton tmpB;

		tmpB = new JButton(IconUtil.loadIcon(aResourcePath));
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
		return createJCheckBox(aTitle, aActionListener, null);
	}

	public static JCheckBox createJCheckBox(String aTitle, ActionListener aActionListener, Font aFont)
	{
		JCheckBox tmpCB;

		tmpCB = new JCheckBox(aTitle);
		tmpCB.addActionListener(aActionListener);
		if (aFont != null)
			tmpCB.setFont(aFont);

		return tmpCB;
	}

	/**
	 * Creates a JComboBox with the specified settings
	 */
	public static JComboBox createJComboBox(ActionListener aListener, Font aFont, Object... itemArr)
	{
		JComboBox tmpBox;

		tmpBox = new JComboBox();
		for (Object aItem : itemArr)
			tmpBox.addItem(aItem);

		if (aFont != null)
			tmpBox.setFont(aFont);

		tmpBox.addActionListener(aListener);
		return tmpBox;
	}

//	/**
//	* Creates a JComboBox with the specified settings
//	*/
//	public static JComboBox createJComboBox(ActionListener aListener, Object... itemArr)
//	{
//		return createJComboBox(aListener, null, itemArr);
//	}

	/**
	 * Creates a JLabel with the specified settings
	 */
	public static JLabel createJLabel(String aTitle, Font aFont)
	{
		return createJLabel(aTitle, JLabel.LEADING, aFont);
	}

	public static JLabel createJLabel(String aTitle, int aAlignment, Font aFont)
	{
		JLabel tmpL;

		tmpL = new JLabel(aTitle, aAlignment);

		if (aFont != null)
			tmpL.setFont(aFont);

		return tmpL;
	}

	/**
	 * Creates the JRadioButton with the following attributes.
	 */
	public static JRadioButton createJRadioButton(String aLabel, ActionListener aListener)
	{
		return createJRadioButton(aLabel, aListener, null);
	}

	public static JRadioButton createJRadioButton(String aLabel, ActionListener aListener, Font aFont)
	{
		JRadioButton tmpRB;

		tmpRB = new JRadioButton(aLabel);
		tmpRB.addActionListener(aListener);
		if (aFont != null)
			tmpRB.setFont(aFont);

		return tmpRB;
	}

	/**
	 * Utility method for creating a visual thin divider
	 * 
	 * Typically added to MigLayout (or like manager) with: add(aComp, "growx,h 4!,span,wrap");
	 */
	public static JPanel createDivider()
	{
		JPanel tmpPanel;

		tmpPanel = new JPanel();
		tmpPanel.setBorder(new BevelBorder(BevelBorder.RAISED));

		return tmpPanel;
	}

	/**
	 * Creates an uneditable JTextArea with no border, non-opaque, line wrap enabled and word wrap enabled.
	 */
	public static JTextArea createUneditableTextArea(int rows, int cols)
	{
		JTextArea tmpTA;

		tmpTA = new JTextArea("", rows, cols);
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
		JTextPane tmpTP;

		tmpTP = new JTextPane();
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
		JTextField tmpTF;

		tmpTF = new JTextField(aTitle);
		tmpTF.setBorder(null);
		tmpTF.setEditable(false);
		tmpTF.setOpaque(false);

		return tmpTF;
	}

	/**
	 * Utility method to link a set of radio buttons together
	 */
	public static void linkRadioButtons(JRadioButton... buttonArr)
	{
		ButtonGroup tmpGroup;

		tmpGroup = new ButtonGroup();
		for (JRadioButton aItem : buttonArr)
			tmpGroup.add(aItem);
	}

	/**
	 * Reads a boolean from a string with out throwing a exception
	 */
	public static boolean readBoolean(String aStr, boolean aVal)
	{
		if (aStr == null)
			return aVal;

		// Special case for 1 char strings
		if (aStr.length() == 1)
		{
			char aChar;

			aChar = aStr.charAt(0);
			if (aChar == 'T' || aChar == 't' || aChar == '1')
				return true;

			return false;
		}

		try
		{
			return Boolean.valueOf(aStr).booleanValue();
		}
		catch(Exception e)
		{
			return aVal;
		}
	}

	/**
	 * Reads a double from a string with out throwing a exception. Note aStr can have an number of separators: comma
	 * chars
	 */
	public static double readDouble(String aStr, double aVal)
	{
		try
		{
			aStr = aStr.replace(",", "");
			return Double.parseDouble(aStr);
		}
		catch(Exception e)
		{
			return aVal;
		}
	}

	/**
	 * Reads a float from a string with out throwing a exception. Note aStr can have an number of separators: comma chars
	 */
	public static float readFloat(String aStr, float aVal)
	{
		try
		{
			aStr = aStr.replace(",", "");
			return Float.parseFloat(aStr);
		}
		catch(Exception e)
		{
			return aVal;
		}
	}

	/**
	 * Reads an int from a string without throwing a exception Note aStr can have an number of separators: comma chars
	 */
	public static int readInt(String aStr, int aVal)
	{
		try
		{
			aStr = aStr.replace(",", "");
			return Integer.parseInt(aStr);
		}
		catch(Exception e)
		{
			return aVal;
		}
	}

	/**
	 * Reads a long from a string without throwing a exception Note aStr can have an number of separators: comma chars
	 */
	public static long readLong(String aStr, long aVal)
	{
		try
		{
			aStr = aStr.replace(",", "");
			return Long.parseLong(aStr);
		}
		catch(Exception e)
		{
			return aVal;
		}
	}

	/**
	 * Reads an int (forced to fit within a range) from a string with out throwing a exception
	 */
	public static int readRangeInt(String aStr, int minVal, int maxVal, int aVal)
	{
		int aInt;

		try
		{
			aInt = Integer.parseInt(aStr);
			if (aInt < minVal)
				aInt = minVal;
			else if (aInt > maxVal)
				aInt = maxVal;

			return aInt;
		}
		catch(Exception e)
		{
			return aVal;
		}
	}

	/**
	 * Utility method to locate the RootPaneContainer for the specified Component
	 */
	public static RootPaneContainer getRootPaneContainer(Component aComponent)
	{
		Container aParent;

		// Check to see if the Component is an actual RootPaneContainer
		if (aComponent instanceof RootPaneContainer)
			return (RootPaneContainer)aComponent;

		// Attempt to locate the RootPaneContainer (through our stack)
		aParent = aComponent.getParent();
		while (aParent != null && (aParent instanceof RootPaneContainer) == false)
			aParent = aParent.getParent();

		// Bail if we failed to find the RootPaneContainer
		if (aParent instanceof RootPaneContainer == false)
			throw new RuntimeException("No valid (grand)parent associated with GlassPane.");

		return (RootPaneContainer)aParent;
	}

	/**
	 * Utility method to locate all of the subcomponents contained in aContainer which are an instance of searchClass
	 */
	public static void locateAllSubComponents(Container aContainer, Collection<Component> itemList, Class<?>... searchClassArr)
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
				locateAllSubComponents((Container)aComponent, itemList, searchClassArr);
		}
	}

	/**
	 * Utility method to force a Component to act as modal while it is visible Source:
	 * http://stackoverflow.com/questions/804023/how-do-i-simulate-a-modal-dialog-from-within-an-applet
	 */
	public static void modalWhileVisible(Component aComponent)
	{
		// Bail if not called from the EventDispatchThread
		if (SwingUtilities.isEventDispatchThread() == false)
			throw new RuntimeException("Visibility for modal components must be changed via the Event thread.");

		synchronized(aComponent)
		{
			try
			{
				EventQueue theQueue = aComponent.getToolkit().getSystemEventQueue();
				while (aComponent.isVisible())
				{
//System.out.println("About to dispatch event... component.isVisible():" + aComponent.isVisible());
					AWTEvent event = theQueue.getNextEvent();
					Object source = event.getSource();
					if (event instanceof ActiveEvent)
					{
						((ActiveEvent)event).dispatch();
					}
					else if (source instanceof Component)
					{
						((Component)source).dispatchEvent(event);
					}
					else if (source instanceof MenuComponent)
					{
						((MenuComponent)source).dispatchEvent(event);
					}
					else
					{
						System.err.println("Unable to dispatch: " + event);
					}
				}
			}
			catch(InterruptedException ignored)
			{
			}
		}
	}

	/**
	 * Utility to call a specific method (methodName) with specific parameters (aParamArr) on aComp and on all of the
	 * child subcomponents. The method will only be called if the components are an instance of refMatchClass.
	 * <P>
	 * This is useful so that a component and all of its children can be disabled, hidden, etc<BR>
	 * Example: GuiUtil.callMethod(myPanel, setEnabled, false);
	 * <P>
	 * Be aware, this is rather expensive, so do not call in time critical applications.
	 */
	public static void callMethod(Component aComp, Class<?> refMatchClass, String aMethodName, Object... aParamArr)
	{
		Class<?>[] typeArr;

		// Construct the associated type array
		typeArr = new Class[0];
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
	private static void callMethodHelper(Component aComp, Class<?> refMatchClass, String aMethodName, Class<?>[] aTypeArr, Object[] aParamArr)
	{
		Component[] subCompArr;
		Function aFunction;

		// Locate and call the actual method
		if (refMatchClass.isInstance(aComp) == true)
		{
			try
			{
				aFunction = new Function(aComp, aMethodName, aTypeArr);
				aFunction.invoke(aParamArr);
			}
			catch(NoSuchMethodException aExp1)
			{
				throw new RuntimeException("Failed to locate valid function. Method:" + aMethodName, aExp1);
			}
			catch(Exception aExp2)
			{
				throw new RuntimeException("Failed to execute function. Method:" + aMethodName, aExp2);
			}
		}

		// Bail if we do not have subcomponents
		if (aComp instanceof Container == false)
			return;

		// Recurse down our children
		subCompArr = ((Container)aComp).getComponents();
		for (Component aSubComp : subCompArr)
			callMethodHelper(aSubComp, refMatchClass, aMethodName, aTypeArr, aParamArr);
	}

	/**
	 * Utility method to set all subcomponents to the specified enabled mode.
	 */
	// TODO: Phase this method out, replace with callMethod()
	public static void setEnabled(Component aComp, boolean aBool)
	{
		Component[] subCompArr;

		aComp.setEnabled(aBool);
		if (aComp instanceof Container == false)
			return;

		subCompArr = ((Container)aComp).getComponents();
		for (Component aSubComp : subCompArr)
			GuiUtil.setEnabled(aSubComp, aBool);
	}

	/**
	 * Utility method to set the enabled switch on all of the specified components.
	 */
	public static void setEnabled(boolean aBool, Component... componentArr)
	{
		for (Component aComp : componentArr)
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
	 * Utility method that takes up to 8 buttons and converts all of their selection states to a single byte.
	 */
	public static byte getSelectionStateAsByte(AbstractButton... buttonArr)
	{
		byte retByte;

		if (buttonArr.length > 8)
			throw new RuntimeException("Improper API call. Max of 8 buttons supported. Passed: " + buttonArr.length);

		retByte = 0;
		for (int c1 = 0; c1 < buttonArr.length; c1++)
		{
			if (buttonArr[c1].isSelected() == true)
				retByte |= 1 << c1;
		}

		return retByte;
	}

	/**
	 * Utility method that takes up to 8 buttons and configures the selection state of the buttons to match the bit
	 * pattern of aByte.
	 */
	public static void setSelectionState(byte aByte, AbstractButton... buttonArr)
	{
		boolean aBool;

		if (buttonArr.length > 8)
			throw new RuntimeException("Improper API call. Max of 8 buttons supported. Passed: " + buttonArr.length);

		for (int c1 = 0; c1 < buttonArr.length; c1++)
		{
			aBool = false;
			if (((0x01 << c1) & aByte) != 0)
				aBool = true;

			buttonArr[c1].setSelected(aBool);
		}
	}

	/**
	 * Utility method to update a JSlider without triggering notifications to its registered listeners.
	 */
	public static void updateSlider(JSlider aSlider, int aVal)
	{
		ChangeListener[] tmpArr;

		tmpArr = aSlider.getChangeListeners();

		for (ChangeListener aListener : tmpArr)
			aSlider.removeChangeListener(aListener);

		aSlider.setValue(aVal);

		for (ChangeListener aListener : tmpArr)
			aSlider.addChangeListener(aListener);
	}

	/**
	 * Utility method that checks to ensure the current thread is running on the ATW thread. If it is NOT then the
	 * specified function will be posted so that it is called on the AWT thread. If it is running on the AWT thread then
	 * nothing will happen and this method will return false.
	 * <P>
	 * Typically this utility method is called at the start of a function to ensure it is on the AWT thread, and if not
	 * then schedule the function onto the AWT thread. Thus it is strongly advisable that if this method returns true the
	 * caller should immediately exit.
	 * <P>
	 * <B>Typical usage within a method:</B>
	 * 
	 * <PRE>
	 * public void actionPerformed(aEvent)
	 * {
	 *    // Ensure this method is run on the AWT thread
	 *    if (redispatchOnAwtIfNeeded(this, "actionPerformed", aEvent) = true)
	 *       return;
	 *       
	 *    // Do normal work ...
	 * }
	 * </PRE>
	 */
	public static boolean redispatchOnAwtIfNeeded(Object aObj, String methodName, Object... aArgArr)
	{
		FunctionRunnable aFunctionRunnable;

		// Do nothing if this is the AWT thread
		if (SwingUtilities.isEventDispatchThread() == true)
			return false;

		aFunctionRunnable = new FunctionRunnable(aObj, methodName, aArgArr);
		SwingUtilities.invokeLater(aFunctionRunnable);
		return true;
	}

}
