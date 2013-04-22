package glum.gui.panel.task;

import glum.gui.GuiUtil;
import glum.gui.component.GLabel;
import glum.unit.ConstUnitProvider;
import glum.unit.NumberUnit;
import glum.unit.TimeCountUnit;
import glum.unit.UnitProvider;
import java.awt.Component;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import net.miginfocom.swing.MigLayout;

/**
 * TaskPanel component that shows the progress of a Task. The panel itself is a Task. 
 * This TaskPanel is typically embedded into other Components, thus it has no built in
 * abort or close button.
 */
public class PlainTaskPanel extends BaseTaskPanel
{
	public PlainTaskPanel(Component aParent)
	{
		this(aParent, true, true);
	}

	public PlainTaskPanel(Component aParent, boolean hasInfoArea, boolean hasStatusArea)
	{
		super(aParent);
		
		buildGuiArea(hasInfoArea, hasStatusArea);
		setSize(450, getPreferredSize().height);
		setTitle("Task Progress");
		
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
		
		colConstraints = "[][]";
		if (hasStatusArea == true)
			colConstraints += "[]";
		if (hasInfoArea == true)
			colConstraints += "[grow]";
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
		add(timerL, "");

		// Status area
		if (hasStatusArea == true)
		{
			tmpL = new JLabel("Status:");
			statusL = new GLabel(aFont);
			add(tmpL, "newline");
			add(statusL, "growx,span");
		}
			
		// Info area
		infoTA = null;
		if (hasInfoArea == true)
		{
			infoTA = GuiUtil.createUneditableTextArea(7,  0);
infoTA.setFont(new Font(aFont.getName(), Font.PLAIN, aFont.getSize()-2));
			infoTA.setOpaque(true);
	
			tmpScrollPane = new JScrollPane(infoTA);
			add(tmpScrollPane, "growx,growy,newline,span");
		}
		
		// Border
		setBorder(new BevelBorder(BevelBorder.RAISED));
	}
	
	@Override
	protected void updateGui()
	{
		// If progress >= 1.0, then we are done
		if (mProgress >= 1.0)
			isActive = false;
		
		progressL.setValue(mProgress);
		if (statusL != null)
			statusL.setValue(mStatus);
		timerL.setValue(mTimer.getTotal());
	}

}
