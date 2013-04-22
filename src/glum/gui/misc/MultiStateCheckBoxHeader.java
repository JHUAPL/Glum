package glum.gui.misc;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.table.*;

public class MultiStateCheckBoxHeader extends MultiStateCheckBox implements TableCellRenderer, MouseListener, MouseMotionListener
{
	// State vars
	protected JTableHeader refHeader;
	protected int column;

	/**
	 * Constructor
	 */
	public MultiStateCheckBoxHeader(JTable aTable, boolean is3StateCycle)
	{
		super("", is3StateCycle);

		// Register for mouse events on the table header
		refHeader = aTable.getTableHeader();
		if (refHeader != null)
		{
			refHeader.addMouseListener(this);
			refHeader.addMouseMotionListener(this);
		}
		else
		{
			System.out.println("Failed to register a mouse listener onto the table header.");
		}
	}

	/**
	 * Returns the column associated with the mouse event
	 */
	public int getAssociatedColumn(MouseEvent aEvent)
	{
		JTableHeader aHeader;
		JTable aTable;
		TableColumnModel aColumnModel;
		int viewCol, refCol;

		if (aEvent.getSource() instanceof JTableHeader == false)
			return -1;

		aHeader = (JTableHeader)aEvent.getSource();
		aTable = aHeader.getTable();
		aColumnModel = aTable.getColumnModel();
		viewCol = aColumnModel.getColumnIndexAtX(aEvent.getX());
		refCol = aTable.convertColumnIndexToModel(viewCol);

		return viewCol;
	}

	@Override
	public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int aColumn)
	{

		if (table != null)
		{
			JTableHeader header = table.getTableHeader();
			if (header != null)
			{
				setForeground(header.getForeground());
				setBackground(header.getBackground());
				setFont(header.getFont());

				// Perhaps we should deregister our listener and register in case we get a new header
				;
			}
		}

		column = aColumn;
		setText((value == null) ? "" : value.toString());
		setBorder(UIManager.getBorder("TableHeader.cellBorder"));

		return this;
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		// Go back to the deactivated state
		model.setPressed(false);
		((Component)e.getSource()).repaint();
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		// Bail if this mouse event does not correspond to our column
		if (getAssociatedColumn(e) != column)
			return;

		// Always activate on mouse press
		model.setPressed(true);
		((Component)e.getSource()).repaint();
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		// Bail if this mouse event does not correspond to our column
		if (getAssociatedColumn(e) != column)
			return;

		// Advance to the next state (if armed)
		if (model.isArmed() == true)
			model.advanceToNextState();
		else
			model.setArmed(true);

		// Always deactivate on mouse release
		model.setPressed(false);
		((Component)e.getSource()).repaint();
	}

	@Override
	public void mouseEntered(MouseEvent e)
	{
		// Bail if this mouse event does not correspond to our column
		if (getAssociatedColumn(e) != column)
			return;

		// Activate our model
		model.setArmed(true);
		((Component)e.getSource()).repaint();
	}

	@Override
	public void mouseExited(MouseEvent e)
	{
		model.setArmed(false);
		((Component)e.getSource()).repaint();
	}

	@Override
	public void mouseMoved(MouseEvent e)
	{
		// Deactivate if not in our column
		if (getAssociatedColumn(e) != column)
		{
			model.setArmed(false);
			model.setPressed(false);
		}
		// Activate if we are in our column
		else
		{
			model.setArmed(true);
		}

		((Component)e.getSource()).repaint();
	}

	@Override
	public void mouseDragged(MouseEvent e)
	{
		// Always deactivate whenever dragging starts to occur
		model.setArmed(false);
		model.setPressed(false);
		((Component)e.getSource()).repaint();
	}

	@Override
	public void setState(MultiState aState)
	{
		super.setState(aState);

		if (refHeader != null)
			refHeader.repaint();
	}

}
