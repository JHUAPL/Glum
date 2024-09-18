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
package glum.gui.misc;

import java.awt.*;
import javax.swing.*;

public class MultiStateCheckBoxTest
{
	public static void main(String args[]) throws Exception
	{
		JFrame frame;
		JCheckBox multi1CB, multi2CB;
		JCheckBox standardCB;

		frame = new JFrame("MultiStateCheckBoxTest");
		frame.getContentPane().setLayout(new GridLayout(0, 1, 5, 5));

		standardCB = new JCheckBox("Standard checkbox");
		standardCB.setMnemonic('S');
		frame.getContentPane().add(standardCB);

		multi1CB = new MultiStateCheckBox("Multistate-1 checkbox", false);
		multi1CB.setMnemonic('1');
		frame.getContentPane().add(multi1CB);

		multi2CB = new MultiStateCheckBox("Multistate-2 checkbox", true);
		multi2CB.setMnemonic('2');
		frame.getContentPane().add(multi2CB);

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

}
