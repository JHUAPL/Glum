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

import javax.swing.*;

public class FocusUtil
{
	/**
	* Adds a keyboard shortcut to bind aKeyStroke for the specified action.
	* This keyboard shortcut will be executed whenever a child component of aComp has the focus.
	*/
	public static void addAncestorKeyBinding(JComponent aComp, String aKeyStroke, Action aAction)
	{
		addAncestorKeyBinding(aComp, convertStringToKeyStroke(aKeyStroke), aAction);
	}
	
	public static void addAncestorKeyBinding(JComponent aComp, KeyStroke aKeyStroke, Action aAction)
	{
		aComp.getInputMap(JComponent.WHEN_ANCESTOR_OF_FOCUSED_COMPONENT).put(aKeyStroke, aAction);
		aComp.getActionMap().put(aAction, aAction);
	}
	
	/**
	* Adds a keyboard shortcut to bind aKeyStroke for the specified action.
	* This keyboard shortcut will be executed aComp has the focus.
	*/
	public static void addFocusKeyBinding(JComponent aComp, String aKeyStroke, Action aAction)
	{
		addFocusKeyBinding(aComp, convertStringToKeyStroke(aKeyStroke), aAction);
	}
	
	public static void addFocusKeyBinding(JComponent aComp, KeyStroke aKeyStroke, Action aAction)
	{
		aComp.getInputMap(JComponent.WHEN_FOCUSED).put(aKeyStroke, aAction);
		aComp.getActionMap().put(aAction, aAction);
	}
	
	/**
	* Adds a keyboard shortcut to bind aKeyStroke for the specified action.
	* This keyboard shortcut will be executed whenever the parent Window has the focus.
	*/
	public static void addWindowKeyBinding(JComponent aComp, String aKeyStroke, Action aAction)
	{
		addWindowKeyBinding(aComp, convertStringToKeyStroke(aKeyStroke), aAction);
	}
	
	public static void addWindowKeyBinding(JComponent aComp, KeyStroke aKeyStroke, Action aAction)
	{
		aComp.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW).put(aKeyStroke, aAction);
		aComp.getActionMap().put(aAction, aAction);
	}
	
	/**
	* Converts a String to a valid KeyStroke.
	*/
	public static KeyStroke convertStringToKeyStroke(String aStr)
	{
		KeyStroke aKeyStroke;
		
		aKeyStroke = KeyStroke.getKeyStroke(aStr);
		if (aKeyStroke == null)
			throw new RuntimeException("Failed to convert: [" + aStr + "] to a keystroke.");
		
		return aKeyStroke;
	}
	
}
