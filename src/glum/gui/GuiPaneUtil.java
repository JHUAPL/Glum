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

import java.awt.Component;

import javax.swing.RootPaneContainer;

import glum.gui.panel.generic.MessagePanel;
import glum.util.ThreadUtil;

/**
 * Collection of AWT/Swing utilities for showing various message panes.
 *
 * @author lopeznr1
 */
public class GuiPaneUtil
{
	/**
	 * Utility method to display an alert messages.
	 * <p>
	 * Alert panel will have a nominal size of 750, 300.
	 */
	public static void showAlertMessage(Component aParent, String aTitle, String aInfoMsg)
	{
		// Delegate
		showFailMessage(aParent, aTitle, aInfoMsg, null, 750, 300);
	}

	/**
	 * Utility method to display failure messages.
	 * <p>
	 * If a stack trace is specified (not null) then the {@link Throwable} will be displayed below aInfoMsg.
	 */
	public static void showFailMessage(Component aParent, String aTitle, String aInfoMsg, Throwable aExp, int aMaxW,
			int aMaxH)
	{
		// Compute the panel size
		int compW = aMaxW;
		int compH = aMaxH;
		RootPaneContainer tmpRPC = GuiUtil.getRootPaneContainer(aParent);
		if (tmpRPC instanceof Component)
		{
			Component tmpComp = (Component) tmpRPC;
			compW = (int) (tmpComp.getWidth() * 0.80);
			compH = (int) (tmpComp.getHeight() * 0.80);
		}
		if (compW > aMaxW)
			compW = aMaxW;
		if (compH > aMaxH)
			compH = aMaxH;

		// Setup the info message
		String infoMsg = "";
		if (aInfoMsg != null)
			infoMsg = aInfoMsg;

		if (aExp != null)
			infoMsg += "\n" + ThreadUtil.getStackTraceClassic(aExp);

		MessagePanel tmpPanel = new MessagePanel(aParent, aTitle, compW, compH);
		tmpPanel.setInfo(infoMsg, 0);
		tmpPanel.setTabSize(2);
		tmpPanel.setVisibleAsModal();
	}

	/**
	 * Utility method to display failure messages.
	 * <p>
	 * The stack trace of the {@link Throwable} will be displayed below aInfoMsg.
	 * <p>
	 * Error panel will have a nominal size of 750, 300.
	 */
	public static void showFailMessage(Component aParent, String aTitle, String aInfoMsg, Throwable aExp)
	{
		// Delegate
		showFailMessage(aParent, aTitle, aInfoMsg, aExp, 750, 300);
	}

}
