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
package glum.gui.panel.task;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JSplitPane;

import glum.gui.GuiUtil;
import glum.gui.panel.GlassPanel;
import net.miginfocom.swing.MigLayout;

/**
 * Panel used to display content from 2 separet {@link PlainTaskPanel}s.
 *
 * @author lopeznr1
 */
public class DualTaskPanel extends GlassPanel implements ActionListener
{
	// Gui vars
	private final PlainTaskPanel priTask, secTask;
	private JButton abortB, closeB;

	/** Standard Constructor */
	public DualTaskPanel(Component parentFrame, PlainTaskPanel aPriTask, PlainTaskPanel aSecTask,
			boolean showControlArea)
	{
		super(parentFrame);

		priTask = aPriTask;
		secTask = aSecTask;

		buildGui(showControlArea);
		updateGui();
	}

	@Override
	public void actionPerformed(ActionEvent aEvent)
	{
		var source = aEvent.getSource();
		if (source == abortB)
		{
			priTask.abort();
			secTask.abort();
		}
		else if (source == closeB)
		{
			setVisible(false);
		}

		updateGui();
	}

	@Override
	public void setVisible(boolean isVisible)
	{
		updateGui();

		super.setVisible(isVisible);
	}

	/**
	 * Forms the GUI
	 */
	private void buildGui(boolean showControlArea)
	{
		var mainPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, priTask, secTask);
		mainPane.setResizeWeight(0.40);

		setLayout(new MigLayout("", "[grow]", "[grow][]"));

		// Main area
		add(mainPane, "growx,growy,span 1,wrap");

		// Control area
		abortB = null;
		closeB = null;
		if (showControlArea == true)
		{
			abortB = GuiUtil.createJButton("Abort", this);
			add(abortB, "align right,split 2");

			closeB = GuiUtil.createJButton("Close", this);
			add(closeB);
		}
	}

	/**
	 * Keeps the GUI synchronized
	 */
	private void updateGui()
	{
		var isActive = priTask.isActive | secTask.isActive();
		if (abortB != null && closeB != null)
		{
			abortB.setEnabled(isActive);
			closeB.setEnabled(!isActive);
		}
	}

}
