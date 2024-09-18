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
package glum.gui.icon;

import java.awt.Component;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 * Collection of utility methods for dealing with icons.
 * 
 * @author lopeznr1
 */
public class IconUtil
{
	/**
	 * Utility method to load an icon from the specified resource.
	 */
	public static ImageIcon loadIcon(String aIconPath)
	{
		return new ImageIcon(ClassLoader.getSystemResource(aIconPath));
	}

	/**
	 * Utility method to load an icon from the specified resource.
	 */
	public static ImageIcon loadIcon(URL aURL)
	{
		return new ImageIcon(aURL);
	}

	/**
	 * Utility method to test if aComp is pressed down.
	 */
	public static boolean isPressed(Component aComp)
	{
		if (aComp instanceof JButton == false)
			return false;

		return ((JButton) aComp).getModel().isPressed();
	}

}
