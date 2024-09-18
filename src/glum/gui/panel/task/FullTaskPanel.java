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
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import glum.gui.FocusUtil;
import glum.gui.GuiUtil;
import glum.gui.action.ClickAction;
import glum.gui.component.GLabel;
import glum.gui.panel.generic.GenericCodes;
import glum.task.Task;
import glum.unit.*;
import net.miginfocom.swing.MigLayout;

/**
 * GUI component that shows the progress of a "task". The panel itself is a {@link Task}. This panel must be manually
 * closed by the user.
 *
 * @author lopeznr1
 */
public class FullTaskPanel extends BaseTaskPanel implements ActionListener, GenericCodes
{
	// GUI vars
	private JButton abortB, closeB;

	/**
	 * Standard Constructor
	 */
	public FullTaskPanel(Component aParent, boolean hasInfoArea, boolean hasStatusArea)
	{
		super(aParent);

		buildGuiArea(hasInfoArea, hasStatusArea);
		setSize(450, getPreferredSize().height);
		setTitle("Task Progress");

		// Set up keyboard short cuts
		FocusUtil.addAncestorKeyBinding(this, "ESCAPE", new ClickAction(abortB));
		FocusUtil.addAncestorKeyBinding(this, "ENTER", new ClickAction(closeB));

		updateGui();
	}

	/**
	 * Simplified Constructor
	 */
	public FullTaskPanel(Component aParent)
	{
		this(aParent, true, true);
	}

	@Override
	public void actionPerformed(ActionEvent aEvent)
	{
		var source = aEvent.getSource();
		if (source == abortB)
		{
			abort();
			notifyListeners(this, ID_CANCEL, "Abort");
		}
		else if (source == closeB)
		{
			setVisible(false);
			notifyListeners(this, ID_ACCEPT, "Close");
		}

		updateGui();
	}

	/**
	 * Forms the actual GUI
	 */
	protected void buildGuiArea(boolean hasInfoArea, boolean hasStatusArea)
	{
		var colConstraints = "[][]";
		if (hasStatusArea == true)
			colConstraints += "[]";
		if (hasInfoArea == true)
			colConstraints += "[grow][]";
		setLayout(new MigLayout("", "[right][pref!][grow][right][pref!]", colConstraints));
		var tmpFont = (new JTextField()).getFont();

		// Title area
		titleL = new JLabel("Title", JLabel.CENTER);
		add(titleL, "growx,span,wrap");

		// Progress + Timer area
		var percentUP = new ConstUnitProvider(new NumberUnit("%", "%", 100.0, 2));
		var tmpL = new JLabel("Progress:");
		progressL = new GLabel(percentUP, tmpFont, true);
		add(tmpL, "");
		add(progressL, "");

		var timerUP = new ConstUnitProvider(new TimeCountUnit(2));
		tmpL = new JLabel("Time:");
		timerL = new GLabel(timerUP, tmpFont, true);
		add(tmpL, "skip 1");
		add(timerL, "wrap");

		// Status area
		if (hasStatusArea == true)
		{
			tmpL = new JLabel("Status:");
			statusL = new GLabel(tmpFont);
			add(tmpL, "");
			add(statusL, "growx,span,wrap");
		}

		// Info area
		infoTA = null;
		if (hasInfoArea == true)
		{
			infoTA = GuiUtil.createUneditableTextArea(7, 0);
			infoTA.setFont(new Font("Monospaced", Font.PLAIN, tmpFont.getSize() - 1));
			infoTA.setOpaque(true);

			var tmpScrollPane = new JScrollPane(infoTA);
			add(tmpScrollPane, "growx,growy,span,wrap");
		}

		// Control area
		abortB = GuiUtil.createJButton("Abort", this);
		closeB = GuiUtil.createJButton("Close", this);
		add(abortB, "ax right,span,split");
		add(closeB, "");
	}

	@Override
	protected void updateGui()
	{
		// If progress >= 1.0, then we are done
		if (mProgress >= 1.0)
			isActive = false;

		abortB.setEnabled(isActive);
		closeB.setEnabled(!isActive);

		progressL.setValue(mProgress);
		if (statusL != null)
			statusL.setValue(mStatus);
		timerL.setValue(mTimer.getTotal());
	}

}
