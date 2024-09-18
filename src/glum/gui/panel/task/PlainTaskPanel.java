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

import javax.swing.*;

import glum.gui.GuiUtil;
import glum.gui.component.GLabel;
import glum.task.Task;
import glum.unit.*;
import net.miginfocom.swing.MigLayout;

/**
 * GUI component that shows the progress of a "task". The panel itself is a {@link Task}. This panel is typically
 * embedded into other components, thus it has no built in abort or close button.
 *
 * @author lopeznr1
 */
public class PlainTaskPanel extends BaseTaskPanel
{
	/**
	 * Standard Constructor
	 */
	public PlainTaskPanel(Component aParent, boolean hasInfoArea, boolean hasStatusArea)
	{
		super(aParent);

		buildGuiArea(hasInfoArea, hasStatusArea);
		setSize(450, getPreferredSize().height);
		setTitle("Task Progress");

		updateGui();
	}

	/**
	 * Simplified Constructor
	 */
	public PlainTaskPanel(Component aParent)
	{
		this(aParent, true, true);
	}

	/**
	 * Forms the actual GUI
	 */
	protected void buildGuiArea(boolean hasInfoArea, boolean hasStatusArea)
	{
		JLabel tmpL;
		JScrollPane tmpScrollPane;
		UnitProvider percentUP, timerUP;
		String colConstraints;
		Font aFont;

		colConstraints = "[][]";
		if (hasStatusArea == true)
			colConstraints += "[]";
		if (hasInfoArea == true)
			colConstraints += "[grow]";
		setLayout(new MigLayout("", "[right][pref!][grow][right][pref!]", colConstraints));
		aFont = (new JTextField()).getFont();

		// Title area
		titleL = new JLabel("Title", JLabel.CENTER);
		add(titleL, "growx,span,wrap");

		// Progress + Timer area
		percentUP = new ConstUnitProvider(new NumberUnit("%", "%", 100.0, 2));
		tmpL = new JLabel("Progress:");
		progressL = new GLabel(percentUP, aFont, true);
		add(tmpL, "");
		add(progressL, "");

		timerUP = new ConstUnitProvider(new TimeCountUnit(2));
		tmpL = new JLabel("Time:");
		timerL = new GLabel(timerUP, aFont, true);
		add(tmpL, "skip 1");
		add(timerL, "");

		// Status area
		if (hasStatusArea == true)
		{
			tmpL = new JLabel("Status:");
			statusL = new GLabel(aFont);
			add(tmpL, "newline");
			add(statusL, "growx,span");
		}

		// Info area
		infoTA = null;
		if (hasInfoArea == true)
		{
			infoTA = GuiUtil.createUneditableTextArea(7, 0);
infoTA.setFont(new Font(aFont.getName(), Font.PLAIN, aFont.getSize() - 2));
			infoTA.setOpaque(true);

			tmpScrollPane = new JScrollPane(infoTA);
			add(tmpScrollPane, "growx,growy,newline,span");
		}
	}

	@Override
	protected void updateGui()
	{
		// If progress >= 1.0, then we are done
		if (mProgress >= 1.0)
			isActive = false;

		progressL.setValue(mProgress);
		if (statusL != null)
			statusL.setValue(mStatus);
		timerL.setValue(mTimer.getTotal());
	}

}
