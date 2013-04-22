package glum.gui.dnd;

import java.awt.datatransfer.Transferable;

import javax.swing.JComponent;
import javax.swing.TransferHandler;

/**
 * Generic TransferHandler that supports transferring an arbitrary Transferable. Note that before the DnD transfer
 * mechanism is triggered via the method exportAsDrag(), the developer must set in the Transferable via the method
 * setWorkTransferable().
 */
public class PlainTransferHandler extends TransferHandler
{
	// State vars
	protected Transferable workTransferable;

	public PlainTransferHandler()
	{
		super();

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
