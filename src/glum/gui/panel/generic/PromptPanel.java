package glum.gui.panel.generic;

import glum.gui.FocusUtil;
import glum.gui.GuiUtil;
import glum.gui.action.ClickAction;
import glum.gui.panel.GlassPanel;

import java.awt.Component;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;
import javax.swing.border.Border;

import net.miginfocom.swing.MigLayout;

public class PromptPanel extends GlassPanel implements ActionListener, GenericCodes
{
	// GUI vars
	protected JLabel titleL;
	protected JTextArea infoTA;
	protected JButton cancelB, acceptB;
	
	// State vars
	protected boolean isAccepted;

	public PromptPanel(Component aParent, String aTitle, int sizeX, int sizeY)
	{
		super(aParent);
		
		isAccepted = false;

		buildGuiArea();
		setSize(sizeX, sizeY);
		setTitle(aTitle);
		
		// Set up keyboard short cuts
		FocusUtil.addAncestorKeyBinding(this, "ESCAPE", new ClickAction(cancelB));
		FocusUtil.addAncestorKeyBinding(this, "ENTER", new ClickAction(acceptB));
	}

	public PromptPanel(Component aParent)
	{
		this(aParent, "Untitled", 275, 350);
	}
	
	public PromptPanel(Component aParent, String aTitle)
	{
		this(aParent, aTitle, 275, 350);
	}
	
	/**
	 * Returns true if the prompt was accepted
	 */
	public boolean isAccepted()
	{
		return isAccepted;
	}
	
	/**
	 * Sets the message of the PromptPanel
	 */
	public void setInfo(String aStr)
	{
		infoTA.setText(aStr);
	}

	/**
	 * Sets the title of this PromptPanel
	 */
	public void setTitle(String aTitle)
	{
		titleL.setText(aTitle);
	}

	@Override
	public void actionPerformed(ActionEvent aEvent)
	{
		Object source;

		source = aEvent.getSource();
		if (source == cancelB)
		{
			isAccepted = false;
			setVisible(false);
			notifyListeners(this, ID_CANCEL, "Cancel");
		}
		else if (source == acceptB)
		{
			isAccepted = true;
			setVisible(false);
			notifyListeners(this, ID_ACCEPT, "Accept");
		}
	}
	
	@Override
	public void setVisible(boolean isVisible)
	{
		// Reset the panel
		if (isVisible == true)
			isAccepted = false;
		
		super.setVisible(isVisible);
	}

	/**
	 * Forms the actual GUI
	 */
	protected void buildGuiArea()
	{
		JScrollPane tmpScrollPane;
		Font aFont;
		Border aBorder;
		
		setLayout(new MigLayout("", "[right][grow][][]", "[][grow][]"));
		aFont = (new JTextField()).getFont();
		
		// Title Area
		titleL = new JLabel("Title", JLabel.CENTER);
		add(titleL, "growx,span,wrap");
		
		// Info area
		infoTA = new JTextArea("No status", 3, 0);
		infoTA.setEditable(false);
//		infoTA.setOpaque(false);
		infoTA.setLineWrap(true);
		infoTA.setWrapStyleWord(true);

		tmpScrollPane = new JScrollPane(infoTA);
//		tmpScrollPane.setBorder(null);
		add(tmpScrollPane, "growx,growy,span,wrap");
		
		// Control area
		cancelB = GuiUtil.createJButton("Cancel", this, aFont);
		acceptB = GuiUtil.createJButton("Accept", this, aFont);
		add(cancelB, "skip 2");
		add(acceptB, "");

		// Border
		aBorder = new BevelBorder(BevelBorder.RAISED);
		setBorder(aBorder);
	}

}
