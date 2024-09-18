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
package glum.gui.memory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;

import javax.swing.*;

import glum.gui.FocusUtil;
import glum.gui.GuiUtil;
import glum.gui.action.ClickAction;
import glum.gui.component.GLabel;
import glum.unit.*;
import net.miginfocom.swing.MigLayout;

/**
 * UI componet that provides a view of the applications memory state.
 *
 * @author lopeznr1
 */
public class MemoryUtilDialog extends JDialog implements ActionListener
{
	// Gui components
	private GLabel totalMemL, freeMemL, usedMemL;
	private UnitProvider byteUP;
	private JButton closeB, gcRunB, updateB;

	/**
	 * Standard Constructor
	 */
	public MemoryUtilDialog(JFrame parentFrame)
	{
		super(parentFrame);

		setTitle("JVM Memory Usage");
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setModal(false);

		var numFormat = new DecimalFormat();
		numFormat.setGroupingUsed(true);
		numFormat.setGroupingSize(3);
		numFormat.setMaximumFractionDigits(0);
		byteUP = new ConstUnitProvider(new NumberUnit("MB", "MB", 1.0 / (1024 * 1024), numFormat));

		// Place the dialog in the center
		buildGuiArea();
		setLocationRelativeTo(parentFrame);

		// Set up keyboard short cuts
		FocusUtil.addWindowKeyBinding(getRootPane(), "ESCAPE", new ClickAction(closeB));
		FocusUtil.addWindowKeyBinding(getRootPane(), "ENTER", new ClickAction(updateB));
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		var source = e.getSource();
		if (source == gcRunB)
		{
			System.gc();
			updateGui();
		}
		else if (source == updateB)
		{
			updateGui();
		}
		else if (source == closeB)
		{
			setVisible(false);
		}
	}

	@Override
	public void setVisible(boolean isVisible)
	{
		updateGui();
		super.setVisible(isVisible);
	}

	/**
	 * Forms the actual dialog GUI
	 */
	private void buildGuiArea()
	{
		// Form the panel
		var tmpPanel = new JPanel(new MigLayout("", "[right][left,grow]", "[][][]10[]10"));
		var tmpFont = new JTextField().getFont();

		// Info area
		var tmpL = new JLabel("Total Memory:");
		totalMemL = new GLabel(byteUP, tmpFont, true);
		tmpPanel.add(tmpL, "");
		tmpPanel.add(totalMemL, "growx,wrap");

		tmpL = new JLabel("Free Memory:");
		freeMemL = new GLabel(byteUP, tmpFont, true);
		tmpPanel.add(tmpL, "");
		tmpPanel.add(freeMemL, "growx,wrap");

		tmpL = new JLabel("Used Memory:");
		usedMemL = new GLabel(byteUP, tmpFont, true);
		tmpPanel.add(tmpL, "");
		tmpPanel.add(usedMemL, "growx,wrap");

		// Control area
		updateB = GuiUtil.createJButton("Update", this);
		gcRunB = GuiUtil.createJButton("Run GC", this);
		closeB = GuiUtil.createJButton("Close", this);
		tmpPanel.add(updateB, "span 2,split 3");
		tmpPanel.add(gcRunB, "");
		tmpPanel.add(closeB, "");

		// Add the main panel into the dialog
		getContentPane().add(tmpPanel);
		pack();
	}

	/**
	 * Utility method to update the Gui
	 */
	private void updateGui()
	{
		var tmpRuntime = Runtime.getRuntime();

		// Update the memory usage
		var freeMem = tmpRuntime.freeMemory();
		var totalMem = tmpRuntime.totalMemory();
		var usedMem = totalMem - freeMem;

		freeMemL.setValue(freeMem);
		totalMemL.setValue(totalMem);
		usedMemL.setValue(usedMem);
	}

}
