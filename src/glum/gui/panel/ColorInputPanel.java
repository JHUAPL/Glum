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

import java.awt.Color;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.google.common.collect.Range;

import glum.gui.component.GNumberField;
import glum.unit.ConstUnitProvider;
import glum.unit.NumberUnit;
import net.miginfocom.swing.MigLayout;

/**
 * User input component that allows the user to specify the a {@link Color}.
 *
 * @author lopeznr1
 */
public class ColorInputPanel extends GPanel implements ActionListener, ChangeListener
{
	// Gui components
	private ColorPanel colorP;
	private JLabel redL, greenL, blueL, alphaL;
	private JSlider redS, greenS, blueS, alphaS;
	private GNumberField redNF, greenNF, blueNF, alphaNF;

	/** Standard Constructor */
	public ColorInputPanel(boolean aIsHorizontal, boolean aShowTF, boolean aShowAlpha)
	{
		// Build the gui areas
		buildGuiArea(aShowTF, aIsHorizontal, aShowAlpha);

		// Set in the default color
		setColorConfig(Color.BLACK);
	}

	/**
	 * Returns the selected color
	 */
	public Color getColorConfig()
	{
		var redVal = redS.getValue();
		var greenVal = greenS.getValue();
		var blueVal = blueS.getValue();
		var alphaVal = alphaS.getValue();
		return new Color(redVal, greenVal, blueVal, alphaVal);
	}

	/**
	 * Sets in the current selected color.
	 */
	public void setColorConfig(Color aColor)
	{
		// Insanity check
		if (aColor == null)
			return;

		synchronizeGui(aColor);
	}

	@Override
	public void actionPerformed(ActionEvent aEvent)
	{
		// Perform GUI updates
		var source = aEvent.getSource();
		updateGui(source);

		// Notify the listeners
		notifyListeners(this, ID_UPDATE);
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
		alphaL.setEnabled(aBool);
		alphaS.setEnabled(aBool);
		alphaNF.setEnabled(aBool);

		colorP.setEnabled(aBool);
	}

	@Override
	public void stateChanged(ChangeEvent aEvent)
	{
		// Perform GUI updates
		var source = aEvent.getSource();
		updateGui(source);

		// Notify the listeners
		if (source instanceof JSlider)
		{
			// Fire off an event only if not being updated
			var tmpSlider = (JSlider) source;
			if (tmpSlider.getValueIsAdjusting() == false)
				notifyListeners(this, ID_UPDATE);
			else
				notifyListeners(this, ID_UPDATING);
		}
	}

	/**
	 * Forms the actual gui
	 */
	private void buildGuiArea(boolean aIsHorizontal, boolean aShowTF, boolean aShowAlpha)
	{
		var sliderStyle = JSlider.HORIZONTAL;
		if (aIsHorizontal == false)
			sliderStyle = JSlider.VERTICAL;

		var countUP = new ConstUnitProvider(new NumberUnit("", "", 1.0, 0));

		// RGB sliders
		var rgbRange = Range.closed(0.0, 255.0);

		redL = new JLabel("R", JLabel.CENTER);
		redS = new JSlider(sliderStyle, 0, 255, 0);
		redNF = new GNumberField(this, countUP, rgbRange);
		var rPanel = formColorControl(redS, redL, redNF, aIsHorizontal, aShowTF);

		greenL = new JLabel("G", JLabel.CENTER);
		greenS = new JSlider(sliderStyle, 0, 255, 0);
		greenNF = new GNumberField(this, countUP, rgbRange);
		var gPanel = formColorControl(greenS, greenL, greenNF, aIsHorizontal, aShowTF);

		blueL = new JLabel("B", JLabel.CENTER);
		blueS = new JSlider(sliderStyle, 0, 255, 0);
		blueNF = new GNumberField(this, countUP, rgbRange);
		var bPanel = formColorControl(blueS, blueL, blueNF, aIsHorizontal, aShowTF);

		alphaL = new JLabel("A", JLabel.CENTER);
		alphaS = new JSlider(sliderStyle, 0, 255, 0);
		alphaNF = new GNumberField(this, countUP, rgbRange);
		var aPanel = formColorControl(alphaS, alphaL, alphaNF, aIsHorizontal, aShowTF);

		// The color area
		colorP = new ColorPanel(40, 40);
		colorP.setOpaque(true);

		if (aIsHorizontal == true)
		{
			if (aShowAlpha == false)
				setLayout(new MigLayout("", "0[grow,75::][]0", "0[][][]0"));
			else
				setLayout(new MigLayout("", "0[grow,75::][]0", "0[][][][]0"));

			add(rPanel, "growx,wrap");
			add(gPanel, "growx,wrap");
			add(bPanel, "growx,wrap");
			if (aShowAlpha == true)
				add(aPanel, "growx,wrap");
			add(colorP, "cell 1 0,growy,spany");
		}
		else
		{
			if (aShowAlpha == false)
				setLayout(new MigLayout("", "0[][][]0", "0[grow,75::][]0"));
			else
				setLayout(new MigLayout("", "0[][][][]0", "0[grow,75::][]0"));

			add(rPanel, "growy");
			add(gPanel, "growy");
			add(bPanel, "growy");
			if (aShowAlpha == true)
				add(aPanel, "growy");
			add(colorP, "newline,growx,spanx");
		}
	}

	/**
	 * builds a JSlider with a label
	 */
	private JPanel formColorControl(JSlider aS, JLabel aL, GNumberField aNF, boolean aIsHorizontal, boolean aShowTF)
	{
		var retPanel = new JPanel();
		if (aIsHorizontal == true)
		{
			retPanel.setLayout(new BoxLayout(retPanel, BoxLayout.X_AXIS));
			aL.setAlignmentY(0.5f);
			aS.setAlignmentY(0.5f);
			aNF.setAlignmentY(0.5f);
		}
		else
		{
			retPanel.setLayout(new BoxLayout(retPanel, BoxLayout.Y_AXIS));
			aL.setAlignmentX(0.5f);
			aS.setAlignmentX(0.5f);
			aNF.setAlignmentX(0.5f);
		}

		aS.addChangeListener(this);

		aNF.setHorizontalAlignment(JTextField.CENTER);
		aNF.setColumns(3);
		aNF.setValue(0);
		aNF.setMinimumSize(aNF.getPreferredSize());
		aNF.setMaximumSize(aNF.getPreferredSize());

		retPanel.add(aL);
		retPanel.add(aS);

		if (aIsHorizontal == true)
			retPanel.add(Box.createHorizontalStrut(2));
		else
			retPanel.add(Box.createVerticalStrut(2));

		if (aShowTF == true)
			retPanel.add(aNF);

		return retPanel;
	}

	/**
	 * Syncs the GUI to match aColor
	 */
	private void synchronizeGui(Color aColor)
	{
		// Get the rgb values
		var redVal = aColor.getRed();
		var greenVal = aColor.getGreen();
		var blueVal = aColor.getBlue();
		var alphaVal = aColor.getAlpha();

		// Stop listening to events while updating
		redS.removeChangeListener(this);
		greenS.removeChangeListener(this);
		blueS.removeChangeListener(this);
		alphaS.removeChangeListener(this);

		// Update the gui components
		if (redVal != redNF.getValue())
			redNF.setValue(redVal);
		if (greenVal != greenNF.getValue())
			greenNF.setValue(greenVal);
		if (blueVal != blueNF.getValue())
			blueNF.setValue(blueVal);
		if (alphaVal != alphaNF.getValue())
			alphaNF.setValue(alphaVal);
		redS.setValue(redVal);
		greenS.setValue(greenVal);
		blueS.setValue(blueVal);
		alphaS.setValue(alphaVal);
		colorP.setColor(new Color(redVal, greenVal, blueVal, 255));

		// Proceed with listening to events
		redS.addChangeListener(this);
		greenS.addChangeListener(this);
		blueS.addChangeListener(this);
		alphaS.addChangeListener(this);
	}

	/**
	 * Updates the gui to reflect the source that has changed
	 */
	private void updateGui(Object aSource)
	{
		// Determine what values to retrieve based on the source
		int redVal, greenVal, blueVal, alphaVal;
		if (aSource instanceof GNumberField)
		{
			redVal = redNF.getValueAsInt(0);
			greenVal = greenNF.getValueAsInt(0);
			blueVal = blueNF.getValueAsInt(0);
			alphaVal = alphaNF.getValueAsInt(0);
		}
		else
		{
			// Get the slider values
			redVal = redS.getValue();
			greenVal = greenS.getValue();
			blueVal = blueS.getValue();
			alphaVal = alphaS.getValue();
		}

		// Update the appropriate component
		if (aSource == redS)
		{
			redNF.setValue(redVal);
		}
		else if (aSource == greenS)
		{
			greenNF.setValue(greenVal);
		}
		else if (aSource == blueS)
		{
			blueNF.setValue(blueVal);
		}
		else if (aSource == alphaS)
		{
			alphaNF.setValue(alphaVal);
		}
		else if (aSource == redNF)
		{
			redS.removeChangeListener(this);
			redS.setValue(redVal);
			redS.addChangeListener(this);
		}
		else if (aSource == greenNF)
		{
			greenS.removeChangeListener(this);
			greenS.setValue(greenVal);
			greenS.addChangeListener(this);
		}
		else if (aSource == blueNF)
		{
			blueS.removeChangeListener(this);
			blueS.setValue(blueVal);
			blueS.addChangeListener(this);
		}
		else if (aSource == alphaNF)
		{
			alphaS.removeChangeListener(this);
			alphaS.setValue(alphaVal);
			alphaS.addChangeListener(this);
		}

		// Update the preview color
		colorP.setColor(new Color(redVal, greenVal, blueVal, 255));
	}

}
