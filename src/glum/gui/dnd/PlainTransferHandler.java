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
package glum.gui.dnd;

import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

/**
 * Generic {@link TransferHandler} that supports transferring an arbitrary {@link Transferable}. Note that before the
 * DnD transfer mechanism is triggered via the method exportAsDrag(), the developer must set in the {@link Transferable}
 * via the method {@link #setWorkTransferable(Transferable)}.
 *
 * @author lopeznr1
 */
public class PlainTransferHandler extends TransferHandler
{
	// State vars
	protected Transferable workTransferable;

	/** Standard Constructor */
	public PlainTransferHandler()
	{
		workTransferable = null;
	}

	/**
	 * Method to set in the current "active" Trasferable for which this TrasferHandler will process.
	 */
	public void setWorkTransferable(Transferable aTransferable)
	{
		workTransferable = aTransferable;
	}

	@Override
	public int getSourceActions(JComponent aComp)
	{
		return COPY_OR_MOVE;
	}

	@Override
	public Transferable createTransferable(JComponent aComp)
	{
		// Just return the active Transferable
		return workTransferable;
	}

	@Override
	public void exportDone(JComponent c, Transferable t, int action)
	{
		workTransferable = null;
	}

}
