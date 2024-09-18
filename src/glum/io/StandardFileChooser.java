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
package glum.io;

import java.awt.*;

import javax.swing.JDialog;
import javax.swing.JFileChooser;

/**
 * Enhanced {@link JFileChooser} that allows external code to position the file dialog.
 *
 * @author lopeznr1
 */
public class StandardFileChooser extends JFileChooser
{
	// Gui vars
	private Dialog tDialog;
	private Point tPoint;

	/**
	 * Constructor
	 */
	public StandardFileChooser(String aFilePath)
	{
		super(aFilePath);

		tDialog = null;
		tPoint = null;
	}

	@Override
	protected JDialog createDialog(Component parent) throws HeadlessException
	{
		JDialog dialog = super.createDialog(parent);

		if (tPoint != null)
			dialog.setLocation(tPoint);

		tDialog = dialog;
		return dialog;
	}

	@Override
	public void setLocation(Point aPoint)
	{
		tPoint = aPoint;
		if (tDialog != null)
			tDialog.setLocation(aPoint);
	}

	@Override
	public Point getLocation()
	{
		if (tDialog != null)
			return tDialog.getLocation();

		return super.getLocation();
	}

}
