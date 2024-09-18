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

import java.awt.Font;
import java.awt.GraphicsEnvironment;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import com.google.common.collect.Range;

import glum.gui.component.GComboBox;
import glum.gui.component.GNumberField;
import glum.unit.*;
import net.miginfocom.swing.MigLayout;

/**
 * User input component that allows the user to specify a {@link Font}.
 *
 * @author lopeznr1
 */
public class FontInputPanel extends GPanel implements ActionListener, ChangeListener
{
	// Gui components
	private GComboBox<String> nameBox;
	private JLabel sizeL;
	private JSlider sizeS;
	private GNumberField sizeNF;
	private JCheckBox boldCB, italicCB;
	private JTextField previewTF;

	/**
	 * Standard Constructor
	 */
	public FontInputPanel()
	{
		// Build the gui area
		buildGuiArea();

		// Set in the default Font
		var defFont = UIManager.getFont("Label.font");
		setFontConfig(defFont);
	}

	/**
	 * Returns the selected Font.
	 */
	public Font getFontConfig()
	{
		var name = nameBox.getChosenItem();

		var size = sizeS.getValue();

		var style = Font.PLAIN;
		if (boldCB.isSelected() == true)
			style |= Font.BOLD;
		if (italicCB.isSelected() == true)
			style |= Font.ITALIC;

		return new Font(name, style, size);
	}

	/**
	 * Sets in the current selected Font.
	 */
	public void setFontConfig(Font aFont)
	{
		synchronizeGui(aFont);
	}

	@Override
	public void actionPerformed(ActionEvent aEvent)
	{
		// Perform GUI updates
		var source = aEvent.getSource();
		updateGui(source);

		// Notify the listeners
		notifyListeners(source, ID_UPDATE);
	}

	@Override
	public void setEnabled(boolean aBool)
	{
		nameBox.setEnabled(aBool);
		boldCB.setEnabled(aBool);
		italicCB.setEnabled(aBool);
		sizeL.setEnabled(aBool);
		sizeS.setEnabled(aBool);
		sizeNF.setEnabled(aBool);

		previewTF.setEnabled(aBool);
	}

	@Override
	public void stateChanged(ChangeEvent aEvent)
	{
		// Perform GUI updates
		var source = aEvent.getSource();
		updateGui(source);

		if (source == sizeS)
		{
			// Notify the listeners
			if (sizeS.getValueIsAdjusting() == false)
				notifyListeners(source, ID_UPDATE);
			else
				notifyListeners(source, ID_UPDATING);
		}
	}

	/**
	 * Forms the actual gui
	 */
	private void buildGuiArea()
	{
		setLayout(new MigLayout("", "0[right][][fill][grow]0", "0[]0"));
		var nameArr = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames();

		// Name area
		var tmpL = new JLabel("Name:");
		nameBox = new GComboBox<>(this, nameArr);
		add("", tmpL);
		add("span 2", nameBox);

		// Preview area
		previewTF = new JTextField("Sample Text");
		previewTF.setHorizontalAlignment(JTextField.CENTER);
		add("growx,growy,pushy,spany,wrap", previewTF);

		// Style area
		tmpL = new JLabel("Style:");
		boldCB = new JCheckBox("Bold", false);
		boldCB.addActionListener(this);
		italicCB = new JCheckBox("Italic", false);
		italicCB.addActionListener(this);
		add("", tmpL);
		add("", boldCB);
		add("wrap", italicCB);

		// Size area
		Range<Double> sizeRange = Range.closed(4.0, 72.0);
		UnitProvider countUP = new ConstUnitProvider(new NumberUnit("", "", 1.0, 0));
		sizeL = new JLabel("Size:", JLabel.CENTER);
		sizeNF = new GNumberField(this, countUP, sizeRange);
		sizeS = new JSlider(JSlider.HORIZONTAL, 4, 72, 12);
		add("", sizeL);
		add("span 2,split,w 24::", sizeNF);
		add("growx", sizeS);
	}

	/**
	 * Syncs the GUI to match aFont.
	 */
	private void synchronizeGui(Font aFont)
	{
		nameBox.setChosenItem(aFont.getName());
		boldCB.setSelected(aFont.isBold());
		italicCB.setSelected(aFont.isItalic());

		// Update the components related to size
		int size = aFont.getSize();
		if (size != sizeNF.getValue())
			sizeNF.setValue(size);

		sizeS.removeChangeListener(this);
		sizeS.setValue(size);
		sizeS.addChangeListener(this);
	}

	/**
	 * Updates the gui to reflect the source that has changed
	 */
	private void updateGui(Object aSource)
	{
		if (aSource == sizeS)
		{
			int size = sizeS.getValue();
			sizeNF.setValue(size);
		}
		else if (aSource == sizeNF)
		{
			int size = sizeNF.getValueAsInt(4);
			sizeS.removeChangeListener(this);
			sizeS.setValue(size);
			sizeS.addChangeListener(this);
		}

		// Update the preview area
		var currFont = getFontConfig();
		previewTF.setFont(currFont);
	}

}
