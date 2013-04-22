package glum.gui.panel;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.event.*;

import net.miginfocom.swing.MigLayout;

import glum.gui.component.GNumberField;
import glum.unit.ConstUnitProvider;
import glum.unit.NumberUnit;
import glum.unit.UnitProvider;

public class ColorInputPanel extends JPanel implements ActionListener, ChangeListener
{
	// Constants
	private static final Font miniFont = new Font("Serif", Font.PLAIN, 10);

	// Gui components
	private ColorPanel colorP;
	private JLabel redL, greenL, blueL;
	private JSlider redS, greenS, blueS;
	private GNumberField redNF, greenNF, blueNF;

	// State vars
	private Collection<ActionListener> myActionListeners;

	/**
	 * Constructor
	 */
	public ColorInputPanel(boolean isHorizontal, boolean showTF)
	{
		super();

		// Init internal vars
		myActionListeners = new LinkedHashSet<ActionListener>();

		// Build the gui areas
		buildGuiArea(isHorizontal, showTF);

		// Set in the default color
		setColor(Color.BLACK);
	}

	/**
	 * addActionListener
	 */
	public synchronized void addActionListener(ActionListener aActionListener)
	{
		// Insanity check
		if (aActionListener == null)
			return;

		myActionListeners.add(aActionListener);
	}

	/**
	 * removeActionListener
	 */
	public synchronized void removeActionListener(ActionListener aActionListener)
	{
		// Insanity check
		if (aActionListener == null)
			return;

		myActionListeners.remove(aActionListener);
	}

	/**
	 * Returns the selected color
	 */
	public Color getColor()
	{
		int redVal, greenVal, blueVal;

		redVal = redS.getValue();
		greenVal = greenS.getValue();
		blueVal = blueS.getValue();
		return new Color(redVal, greenVal, blueVal);
	}

	/**
	 * Sets in the current selected color
	 */
	public void setColor(Color aColor)
	{
		// Insanity check
		if (aColor == null)
			return;

		synchronizeGui(aColor);
	}

	@Override
	public void actionPerformed(ActionEvent aEvent)
	{
		Object source;

		// Perform GUI updates
		source = aEvent.getSource();
		updateGui(source);

		// Notify the listeners
		fireActionEvent(false);
	}

	@Override
	public void setEnabled(boolean aBool)
	{
		redL.setEnabled(aBool);
		redS.setEnabled(aBool);
		redNF.setEnabled(aBool);
		greenL.setEnabled(aBool);
		greenS.setEnabled(aBool);
		greenNF.setEnabled(aBool);
		blueL.setEnabled(aBool);
		blueS.setEnabled(aBool);
		blueNF.setEnabled(aBool);

		colorP.setEnabled(aBool);
	}

	@Override
	public void stateChanged(ChangeEvent aEvent)
	{
		Object source;
		JSlider aSlider;

		// Perform GUI updates
		source = aEvent.getSource();
		updateGui(source);

		// Notify the listeners
		if (source instanceof JSlider)
		{
			// Fire off an event only if not being updated
			aSlider = (JSlider)source;
			if (aSlider.getValueIsAdjusting() == false)
				fireActionEvent(false);
			else
				fireActionEvent(true);
		}
	}

	/**
	 * Forms the actual gui
	 */
	private void buildGuiArea(boolean isHorizontal, boolean showTF)
	{
		JPanel rPanel, gPanel, bPanel;
		UnitProvider countUP;
		int sliderStyle;

		sliderStyle = JSlider.HORIZONTAL;
		if (isHorizontal == false)
			sliderStyle = JSlider.VERTICAL;

		countUP = new ConstUnitProvider(new NumberUnit("", "", 1.0, 0));

		// RGB sliders
		redL = new JLabel("R", JLabel.CENTER);
		redS = new JSlider(sliderStyle, 0, 255, 0);
		redNF = new GNumberField(this, countUP, 0, 255);
		rPanel = formColorControl(redS, redL, redNF, isHorizontal, showTF);

		greenL = new JLabel("G", JLabel.CENTER);
		greenS = new JSlider(sliderStyle, 0, 255, 0);
		greenNF = new GNumberField(this, countUP, 0, 255);
		gPanel = formColorControl(greenS, greenL, greenNF, isHorizontal, showTF);

		blueL = new JLabel("B", JLabel.CENTER);
		blueS = new JSlider(sliderStyle, 0, 255, 0);
		blueNF = new GNumberField(this, countUP, 0, 255);
		bPanel = formColorControl(blueS, blueL, blueNF, isHorizontal, showTF);

		// The color area
		colorP = new ColorPanel(40, 40);

		if (isHorizontal == true)
		{
			setLayout(new MigLayout("", "0[grow,75::][]0", "0[][][]0"));

			add(rPanel, "growx,span 1,wrap");
			add(gPanel, "growx,span 1,wrap");
			add(bPanel, "growx,span 1,wrap");
			add(colorP, "cell 1 0,growy,spanx 1,spany 3");
		}
		else
		{
			setLayout(new MigLayout("", "0[][][]0", "0[grow,75::][]0"));

			add(rPanel, "growy,span 1");
			add(gPanel, "growy,span 1");
			add(bPanel, "growy,span 1,wrap");
			add(colorP, "growx,span 3");
		}
	}

	/**
	 * builds a JSlider with a label
	 */
	private JPanel formColorControl(JSlider aS, JLabel aL, GNumberField aNF, boolean isHorizontal, boolean showTF)
	{
		JPanel aPanel;

		aPanel = new JPanel();
		if (isHorizontal == true)
		{
			aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.X_AXIS));
			aL.setAlignmentY(0.5f);
			aS.setAlignmentY(0.5f);
			aNF.setAlignmentY(0.5f);
		}
		else
		{
			aPanel.setLayout(new BoxLayout(aPanel, BoxLayout.Y_AXIS));
			aL.setAlignmentX(0.5f);
			aS.setAlignmentX(0.5f);
			aNF.setAlignmentX(0.5f);
		}

		aS.addChangeListener(this);

		aNF.setHorizontalAlignment(JTextField.CENTER);
		aNF.setColumns(3);
		aNF.setValue(0);
		aNF.setFont(miniFont);
		aNF.setMinimumSize(aNF.getPreferredSize());
		aNF.setMaximumSize(aNF.getPreferredSize());

		aPanel.add(aL);
		aPanel.add(aS);

		if (isHorizontal == true)
			aPanel.add(Box.createHorizontalStrut(2));
		else
			aPanel.add(Box.createVerticalStrut(2));

		if (showTF == true)
			aPanel.add(aNF);

		return aPanel;
	}

	/**
	 * Notifies all listeners of color change
	 */
	private void fireActionEvent(boolean isChanging)
	{
		Collection<ActionListener> currListeners;
		ActionEvent aEvent;

		// Get a copy of the current set of listeners
		synchronized(this)
		{
			currListeners = new LinkedHashSet<ActionListener>(myActionListeners);
		}

		// Construct the event
		if (isChanging == false)
			aEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Color changed.");
		else
			aEvent = new ActionEvent(this, ActionEvent.ACTION_PERFORMED, "Color changing.");

		// Notify our listeners
		for (ActionListener aListener : currListeners)
		{
			aListener.actionPerformed(aEvent);
		}
	}

	/**
	 * Syncs the GUI to match aColor
	 */
	private void synchronizeGui(Color aColor)
	{
		int redVal, greenVal, blueVal;

		// Get the rgb values
		redVal = aColor.getRed();
		greenVal = aColor.getGreen();
		blueVal = aColor.getBlue();

		// Stop listening to events while updating
		redS.removeChangeListener(this);
		greenS.removeChangeListener(this);
		blueS.removeChangeListener(this);

		// Update the gui components
		if (redVal != redNF.getValue())
			redNF.setValue(redVal);
		if (greenVal != greenNF.getValue())
			greenNF.setValue(greenVal);
		if (blueVal != blueNF.getValue())
			blueNF.setValue(blueVal);
		redS.setValue(redVal);
		greenS.setValue(greenVal);
		blueS.setValue(blueVal);
		colorP.setColor(new Color(redVal, greenVal, blueVal));

		// Proceed with listening to events
		redS.addChangeListener(this);
		greenS.addChangeListener(this);
		blueS.addChangeListener(this);
	}

	/**
	 * Updates the gui to reflect the source that has changed
	 */
	private void updateGui(Object source)
	{
		int redVal, greenVal, blueVal;

		// Determine what values to retrieve based on the source
		if (source instanceof GNumberField)
		{
			redVal = redNF.getValueAsInt(0);
			greenVal = greenNF.getValueAsInt(0);
			blueVal = blueNF.getValueAsInt(0);
		}
		else
		{
			// Get the slider values
			redVal = redS.getValue();
			greenVal = greenS.getValue();
			blueVal = blueS.getValue();
		}

		// Update the appropriate component
		if (source == redS)
		{
			redNF.setValue(redVal);
		}
		else if (source == greenS)
		{
			greenNF.setValue(greenVal);
		}
		else if (source == blueS)
		{
			blueNF.setValue(blueVal);
		}
		else if (source == redNF)
		{
			redS.removeChangeListener(this);
			redS.setValue(redVal);
			redS.addChangeListener(this);
		}
		else if (source == greenNF)
		{
			greenS.removeChangeListener(this);
			greenS.setValue(greenVal);
			greenS.addChangeListener(this);
		}
		else if (source == blueNF)
		{
			blueS.removeChangeListener(this);
			blueS.setValue(blueVal);
			blueS.addChangeListener(this);
		}

		// Update the preview color
		colorP.setColor(new Color(redVal, greenVal, blueVal));
	}

}
