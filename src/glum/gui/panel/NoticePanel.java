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

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Map;

import javax.swing.*;
import javax.swing.border.LineBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import com.google.common.collect.ImmutableMap;

import glum.gui.FocusUtil;
import glum.gui.GuiUtil;
import glum.gui.action.ClickAction;
import glum.gui.component.model.GListModel;
import net.miginfocom.swing.MigLayout;

/**
 * {@link GlassPanel} used to provide the user with a listing of notices.
 * <p>
 * The title and list of notices can be customized.
 *
 * @author lopeznr1
 */
public class NoticePanel extends GlassPanel implements ActionListener, ListSelectionListener
{
	// GUI vars
	private JLabel titleL;
	private JList<String> itemJL;
	private JTextArea infoTA;
	private JButton closeB;

	// State vars
	private ImmutableMap<String, String> notificationM;

	/** Standard Constructor */
	public NoticePanel(Component aParent, String aTitle, int sizeX, int sizeY)
	{
		super(aParent);

		notificationM = ImmutableMap.of();

		buildGuiArea();
		setSize(sizeX, sizeY);
		setTitle(aTitle);

		// Set up keyboard short cuts
		FocusUtil.addAncestorKeyBinding(this, "ESCAPE", new ClickAction(closeB));
		FocusUtil.addAncestorKeyBinding(this, "ENTER", new ClickAction(closeB));
	}

	/** Simplified Constructor */
	public NoticePanel(Component aParent, String aTitle)
	{
		this(aParent, aTitle, 275, 350);
	}

	/** Simplified Constructor */
	public NoticePanel(Component aParent)
	{
		this(aParent, "Untitled", 275, 350);
	}

	/**
	 * Sets the notifications to be displayed.
	 */
	public void setNotifications(Map<String, String> aNotificationM)
	{
		notificationM = ImmutableMap.copyOf(aNotificationM);

		// Update the item JList
		itemJL.removeListSelectionListener(this);
		var tmpModel = new GListModel<>(aNotificationM.keySet());
		itemJL.setModel(tmpModel);
		itemJL.addListSelectionListener(this);

		String tmpItem = null;
		if (notificationM.size() > 0)
			tmpItem = notificationM.keySet().iterator().next();
		itemJL.setSelectedValue(tmpItem, true);

		updateGui();
	}

	/**
	 * Sets the font of the info area.
	 */
	public void setFontInfo(Font aFont)
	{
		infoTA.setFont(aFont);
	}

	/**
	 * Sets the tab size associated with the info area.
	 */
	public void setTabSize(int aSize)
	{
		infoTA.setTabSize(aSize);
	}

	/**
	 * Sets the title of this panel.
	 */
	public void setTitle(String aTitle)
	{
		titleL.setText(aTitle);
	}

	@Override
	public void actionPerformed(ActionEvent aEvent)
	{
		var source = aEvent.getSource();
		if (source == closeB)
		{
			setVisible(false);
			notifyListeners(this, ID_CANCEL);
		}

		updateGui();
	}

	@Override
	public void valueChanged(ListSelectionEvent aEvent)
	{
		updateGui();
	}

	/**
	 * Forms the actual GUI
	 */
	private void buildGuiArea()
	{
		setLayout(new MigLayout("", "[]", "[]"));

		// Title Area
		titleL = new JLabel("Title", JLabel.CENTER);
		add(titleL, "growx,span,wrap");
		add(GuiUtil.createDivider(), "growx,h 4!,span,wrap");

		// Notification area
		itemJL = new JList<>();
		itemJL.setBorder(new LineBorder(Color.BLACK));
		itemJL.addListSelectionListener(this);

		infoTA = new JTextArea("No status", 3, 0);
		infoTA.setEditable(false);
//		infoTA.setOpaque(false);
		infoTA.setLineWrap(true);
		infoTA.setTabSize(3);
		infoTA.setWrapStyleWord(true);

		add(new JScrollPane(itemJL), "w pref+3::,growy,span,split");
		add(new JScrollPane(infoTA), "growx,growy,pushx,pushy,span,wrap");

		// Action area
		closeB = GuiUtil.createJButton("Close", this);
		add(closeB, "ax right,span,split");
	}

	/**
	 * Helper method that keeps various UI components synchronized.
	 */
	private void updateGui()
	{
		var tmpKey = itemJL.getSelectedValue();
		var tmpVal = notificationM.get(tmpKey);

		infoTA.setText(tmpVal);
		infoTA.setCaretPosition(0);
	}
}
