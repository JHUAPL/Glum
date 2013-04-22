package glum.io;

import java.awt.*;
import javax.swing.*;

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
