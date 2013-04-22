package glum.gui.panel.task;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JSplitPane;
import javax.swing.JTextField;

import net.miginfocom.swing.MigLayout;

import glum.gui.GuiUtil;
import glum.gui.panel.GlassPanel;

public class DualTaskPanel extends GlassPanel implements ActionListener
{
	// Gui vars
	private PlainTaskPanel priTask, secTask;
	private JButton abortB, closeB;

	public DualTaskPanel(Component parentFrame, PlainTaskPanel aPriTask, PlainTaskPanel aSecTask, boolean showControlArea)
	{
		super(parentFrame);
		
		priTask = aPriTask;
		secTask = aSecTask;
		
		buildGui(showControlArea);
		updateGui();		
	}

	@Override
	public void actionPerformed(ActionEvent aEvent)
	{
		Object source;
		
		source = aEvent.getSource();
		if (source == abortB)
		{
			priTask.abort();
			secTask.abort();
		}
		else if (source == closeB)
		{
			setVisible(false);
		}
		
		updateGui();
	}
	
	@Override
	public void setVisible(boolean isVisible) 
	{
		updateGui();

		super.setVisible(isVisible);
	}
	
	/**
	 * Forms the GUI
	 */
	private void buildGui(boolean showControlArea)
	{
		JSplitPane mainPane;
		Font smallFont;
		
		mainPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, true, priTask, secTask);
		mainPane.setResizeWeight(0.40);
		
		setLayout(new MigLayout("", "[grow]", "[grow][]"));
		
		// Main area
		add(mainPane, "growx,growy,span 1,wrap");

		// Control area
		abortB = null;
		closeB = null;
		if (showControlArea == true)
		{
			smallFont = (new JTextField()).getFont();
			
			abortB = GuiUtil.createJButton("Abort", this, smallFont);
			add(abortB, "align right,split 2");

			closeB = GuiUtil.createJButton("Close", this, smallFont);
			add(closeB);
		}
	}
	
	/**
	 * Keeps the GUI synchronized
	 */
	private void updateGui()
	{
		boolean isActive;
		
		isActive = priTask.isActive | secTask.isActive();
		if (abortB != null && closeB != null)
		{
			abortB.setEnabled(isActive);
			closeB.setEnabled(!isActive);
		}
	}

}
