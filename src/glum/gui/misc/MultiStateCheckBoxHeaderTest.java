package glum.gui.misc;

import java.awt.event.*;
import java.util.*;
import javax.swing.*;
import javax.swing.table.*;

public class MultiStateCheckBoxHeaderTest extends JFrame implements ActionListener
{
	public MultiStateCheckBoxHeaderTest()
	{
		super("MultiStateCheckBoxHeaderTest");

		buildGui();

	}

	/**
	 * Test application entry point
	 */
	public static void main(String[] argv)
	{
		MultiStateCheckBoxHeaderTest mainClass;

		mainClass = new MultiStateCheckBoxHeaderTest();

		mainClass.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainClass.setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object source;
		String text;

		source = e.getSource();
		text = "";
		if (source instanceof AbstractButton)
			text = ((AbstractButton)source).getText();

		System.out.println("HeaderListener::actionPerformed() " + text + ", state:" + ((MultiStateCheckBoxHeader)source).getState());
	}

	/**
	 * Utility method to build the main GUI
	 */
	private void buildGui()
	{
		JTable table;
		DefaultTableModel dataModel;
		JScrollPane scrollpane;
		MultiStateCheckBoxHeader aHeader;

		// Create sample content for the JTable, don't care
		String[][] data = new String[7][5];
		String[] headers = new String[5];
		for (int col = 0; col < data[0].length; col++)
		{
			headers[col] = "- " + col + " -";
			for (int row = 0; row < data.length; row++)
				data[row][col] = "(" + row + "," + col + ")";
		}
		dataModel = new DefaultTableModel(data, headers);
		table = new JTable(dataModel);

		// Create a HeaderCellRenderer for each column
		Enumeration<TableColumn> enumeration = table.getColumnModel().getColumns();
		while (enumeration.hasMoreElements())
		{
			TableColumn aColumn = enumeration.nextElement();
			aHeader = new MultiStateCheckBoxHeader(table, true);
			aHeader.addActionListener(this);
			aColumn.setHeaderRenderer(aHeader);
		}

		scrollpane = new JScrollPane(table);
		getContentPane().add(scrollpane);

		pack();
	}

}
