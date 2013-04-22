package glum.gui.panel.task;

import glum.gui.FocusUtil;
import glum.gui.GuiUtil;
import glum.gui.action.ClickAction;
import glum.gui.component.GLabel;
import glum.gui.panel.generic.GenericCodes;
import glum.unit.ConstUnitProvider;
import glum.unit.NumberUnit;
import glum.unit.TimeCountUnit;
import glum.unit.UnitProvider;
import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;
import net.miginfocom.swing.MigLayout;

/**
 * TaskPanel component that shows the progress of a Task. The panel itself is a Task. 
 * This TaskPanel must be manually closed by the user.
 */
public class FullTaskPanel extends BaseTaskPanel implements ActionListener, GenericCodes
{
	// GUI vars
	private JButton abortB, closeB;

	public FullTaskPanel(Component aParent)
	{
		this(aParent, true, true);
	}

	public FullTaskPanel(Component aParent, boolean hasInfoArea, boolean hasStatusArea)
	{
		super(aParent);
		
		buildGuiArea(hasInfoArea, hasStatusArea);
		setSize(450, getPreferredSize().height);
		setTitle("Task Progress");
		
		// Set up keyboard short cuts
		FocusUtil.addAncestorKeyBinding(this, "ESCAPE", new ClickAction(abortB));
		FocusUtil.addAncestorKeyBinding(this, "ENTER", new ClickAction(closeB));
		
		updateGui();
	}

	@Override
	public void actionPerformed(ActionEvent aEvent)
	{
		Object source;

		source = aEvent.getSource();
		if (source == abortB)
		{
			abort();
			notifyListeners(this, ID_CANCEL, "Abort");			
		}
		else if (source == closeB)
		{
			setVisible(false);
			notifyListeners(this, ID_ACCEPT, "Close");
		}
		
		updateGui();
	}

	/**
	 * Forms the actual GUI
	 */
	protected void buildGuiArea(boolean hasInfoArea, boolean hasStatusArea)
	{
		JLabel tmpL;
		JScrollPane tmpScrollPane;
		UnitProvider percentUP, timerUP;
		String colConstraints;
		Font aFont;
		Border aBorder;
		
		colConstraints = "[][]";
		if (hasStatusArea == true)
			colConstraints += "[]";
		if (hasInfoArea == true)
			colConstraints += "[grow][]";
		setLayout(new MigLayout("", "[right][pref!][grow][right][pref!]", colConstraints));
		aFont = (new JTextField()).getFont();
		
		// Title area
		titleL = new JLabel("Title", JLabel.CENTER);
		add(titleL, "growx,span,wrap");
		
		// Progress + Timer area
		percentUP = new ConstUnitProvider(new NumberUnit("%", "%", 100.0, 2));  
		tmpL = new JLabel("Progress:");
		progressL = new GLabel(percentUP, aFont, true);
		add(tmpL, "");
		add(progressL, "");
		
		timerUP = new ConstUnitProvider(new TimeCountUnit(2));
		tmpL = new JLabel("Time:");
		timerL = new GLabel(timerUP, aFont, true);
		add(tmpL, "skip 1");
		add(timerL, "wrap");

		// Status area
		if (hasStatusArea == true)
		{
			tmpL = new JLabel("Status:");
			statusL = new GLabel(aFont);
			add(tmpL, "");
			add(statusL, "growx,span,wrap");
		}
			
		// Info area
		infoTA = null;
		if (hasInfoArea == true)
		{
			infoTA = GuiUtil.createUneditableTextArea(7,  0);
			infoTA.setFont(new Font("Monospaced", Font.PLAIN, aFont.getSize()-1));
			infoTA.setOpaque(true);
	
			tmpScrollPane = new JScrollPane(infoTA);
			add(tmpScrollPane, "growx,growy,span,wrap");
		}
		
		// Control area
		abortB = GuiUtil.createJButton("Abort", this, aFont);
		closeB = GuiUtil.createJButton("Close", this, aFont);
		add(abortB, "align right,span,split 2");
		add(closeB, "span 1");

		// Border
		aBorder = new BevelBorder(BevelBorder.RAISED);
		setBorder(aBorder);
	}
	
	@Override
	protected void updateGui()
	{
		// If progress >= 1.0, then we are done
		if (mProgress >= 1.0)
			isActive = false;
		
		abortB.setEnabled(isActive);
		closeB.setEnabled(!isActive);
		
		progressL.setValue(mProgress);
		if (statusL != null)
			statusL.setValue(mStatus);
		timerL.setValue(mTimer.getTotal());
	}

}
