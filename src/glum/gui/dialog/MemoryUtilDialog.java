package glum.gui.dialog;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.DecimalFormat;
import javax.swing.*;
import net.miginfocom.swing.MigLayout;

import glum.gui.FocusUtil;
import glum.gui.GuiUtil;
import glum.gui.action.ClickAction;
import glum.gui.component.GLabel;
import glum.unit.ConstUnitProvider;
import glum.unit.NumberUnit;
import glum.unit.UnitProvider;

public class MemoryUtilDialog extends JDialog implements ActionListener
{
	// Gui components
	private GLabel totalMemL, freeMemL, usedMemL;
	private UnitProvider byteUP;
	private JButton closeB, gcRunB, updateB;

	/**
	 * Constructor
	 */
	public MemoryUtilDialog(JFrame parentFrame)
	{
		super(parentFrame);

		setTitle("JVM Memory Usage");
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setModal(false);

		DecimalFormat numFormat;
		numFormat = new DecimalFormat();
		numFormat.setGroupingUsed(true);
		numFormat.setGroupingSize(3);
		numFormat.setMaximumFractionDigits(0);
		byteUP = new ConstUnitProvider(new NumberUnit("MB", "MB", 1.0 / (1024 * 1024), numFormat));

		// Place the dialog in the center
		buildGuiArea();
		setLocationRelativeTo(parentFrame);

		// Set up keyboard short cuts
		FocusUtil.addWindowKeyBinding(getRootPane(), "ESCAPE", new ClickAction(closeB));
		FocusUtil.addWindowKeyBinding(getRootPane(), "ENTER", new ClickAction(updateB));
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object source;

		source = e.getSource();
		if (source == gcRunB)
		{
			System.gc();
			updateGui();
		}
		else if (source == updateB)
		{
			updateGui();
		}
		else if (source == closeB)
		{
			setVisible(false);
		}
	}

	@Override
	public void setVisible(boolean isVisible)
	{
		updateGui();
		super.setVisible(isVisible);
	}

	/**
	 * Forms the actual dialog GUI
	 */
	private void buildGuiArea()
	{
		JPanel aPanel;
		JLabel tmpL;
		Font tmpFont;

		// Form the panel
		aPanel = new JPanel(new MigLayout("", "[right][left,grow]", "[][][]10[]10"));
		tmpFont = new JTextField().getFont();

		// Info area
		tmpL = new JLabel("Total Memory:");
		totalMemL = new GLabel(byteUP, tmpFont, true);
		aPanel.add(tmpL, "");
		aPanel.add(totalMemL, "growx,wrap");

		tmpL = new JLabel("Free Memory:");
		freeMemL = new GLabel(byteUP, tmpFont, true);
		aPanel.add(tmpL, "");
		aPanel.add(freeMemL, "growx,wrap");

		tmpL = new JLabel("Used Memory:");
		usedMemL = new GLabel(byteUP, tmpFont, true);
		aPanel.add(tmpL, "");
		aPanel.add(usedMemL, "growx,wrap");

		// Control area
		updateB = GuiUtil.createJButton("Update", this);
		gcRunB = GuiUtil.createJButton("Run GC", this);
		closeB = GuiUtil.createJButton("Close", this);
		aPanel.add(updateB, "span 2,split 3");
		aPanel.add(gcRunB, "");
		aPanel.add(closeB, "");

		// Add the main panel into the dialog
		getContentPane().add(aPanel);
		pack();
	}

	/**
	 * Utility method to update the Gui
	 */
	private void updateGui()
	{
		Runtime aRuntime;
		long freeMem, usedMem, totalMem;

		aRuntime = Runtime.getRuntime();

		// Update the memory usage
		freeMem = aRuntime.freeMemory();
		totalMem = aRuntime.totalMemory();
		usedMem = totalMem - freeMem;

		freeMemL.setValue(freeMem);
		totalMemL.setValue(totalMem);
		usedMemL.setValue(usedMem);
	}

}
