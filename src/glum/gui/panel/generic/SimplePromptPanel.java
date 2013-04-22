package glum.gui.panel.generic;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.*;

import glum.gui.*;
import glum.gui.action.ClickAction;
import glum.gui.panel.GlassPanel;
import glum.gui.panel.generic.GenericCodes;
import net.miginfocom.swing.MigLayout;

public class SimplePromptPanel extends GlassPanel implements ActionListener, GenericCodes
{
	// GUI vars
	protected JLabel titleL, messageL;
	protected JButton cancelB, acceptB;

	// State vars
	protected boolean isAccepted;

	public SimplePromptPanel(Component aParent)
	{
		super(aParent);

		isAccepted = false;

		// Build the actual GUI
		buildGuiArea();
		setPreferredSize(new Dimension(300, getPreferredSize().height));

		// Set up keyboard short cuts
		FocusUtil.addAncestorKeyBinding(this, "ESCAPE", new ClickAction(cancelB));
		FocusUtil.addAncestorKeyBinding(this, "ENTER", new ClickAction(acceptB));
	}

	/**
	 * Returns true if the user accepted the prompt
	 */
	public boolean isAccepted()
	{
		return isAccepted;
	}

	/**
	 * Sets in the title of this PromptPanel
	 */
	public void setTitle(String aStr)
	{
		titleL.setText(aStr);
	}

	/**
	 * Sets in the informational message of this PromptPanel
	 */
	public void setMessage(String aStr)
	{
		messageL.setText(aStr);
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
	public void setVisible(boolean aBool)
	{
		isAccepted = false;
		super.setVisible(aBool);
	}

	/**
	 * Forms the actual GUI
	 */
	protected void buildGuiArea()
	{
		Font aFont;

		setLayout(new MigLayout("", "[right][grow][][]", "[][][20!][]"));
		aFont = (new JTextField()).getFont();

		// Title Area
		titleL = new JLabel("Title", JLabel.CENTER);
		add(titleL, "growx,span,wrap");

		// Message area
		messageL = GuiUtil.createJLabel("Message", aFont);
		add(messageL, "growx,span,wrap");

		// Control area
		cancelB = GuiUtil.createJButton("Cancel", this, aFont);
		acceptB = GuiUtil.createJButton("Accept", this, aFont);
		add(cancelB, "skip 2");
		add(acceptB, "");

		setBorder(new BevelBorder(BevelBorder.RAISED));
	}

}
