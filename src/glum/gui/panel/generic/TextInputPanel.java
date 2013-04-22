package glum.gui.panel.generic;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.*;
import javax.swing.border.BevelBorder;
import glum.gui.FocusUtil;
import glum.gui.GuiUtil;
import glum.gui.action.ClickAction;
import glum.gui.component.GTextField;
import glum.gui.panel.GlassPanel;
import net.miginfocom.swing.MigLayout;

public abstract class TextInputPanel extends GlassPanel implements ActionListener, GenericCodes
{
	// Constants
	public static final Color warnColor = new Color(128, 0, 0);

	// GUI vars
	protected JLabel titleL, inputL, infoL;
	protected JButton cancelB, acceptB;
	protected GTextField inputTF;
	
	// State vars
	protected boolean isAccepted;

	/**
	 * Constructor
	 */
	public TextInputPanel(Component aParent)
	{
		super(aParent);
		
		isAccepted = false;

		// Build the actual GUI
		buildGuiArea();
		setPreferredSize(new Dimension(300, getPreferredSize().height));

		// Set up keyboard short cuts
		FocusUtil.addAncestorKeyBinding(this, "ESCAPE", new ClickAction(cancelB));
		FocusUtil.addAncestorKeyBinding(this, "ENTER", new ClickAction(acceptB));
		FocusUtil.addFocusKeyBinding(inputTF, "ENTER", new ClickAction(acceptB));
	}
	
	/**
	 * Returns the input of the user.
	 */
	public String getInput()
	{
		if (isAccepted == false)
			return null;
		
		return inputTF.getText();
	}

	/**
	 * Sets in aStr as the default input
	 */
	public void setInput(String aStr)
	{
		inputTF.setValue(aStr);
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
		else if (source == inputTF)
		{
			updateGui();
		}
	}

	@Override
	public void setVisible(boolean aBool)
	{
		if (aBool == true)
			isAccepted = false;
//			resetGui();

		super.setVisible(aBool);
	}

	/**
	 * Forms the actual dialog GUI
	 */
	protected void buildGuiArea()
	{
		Font aFont;
		String aStr;

		setLayout(new MigLayout("", "[right][grow][][]", "[][][20!][]"));
		aFont = (new JTextField()).getFont();

		// Title Area
		titleL = new JLabel("Title", JLabel.CENTER);
		add(titleL, "growx,span 4,wrap");

		// Source area
		inputL = new JLabel("Symbol:");
		inputTF = new GTextField(this);
		add(inputL);
		add(inputTF, "growx,span 3,wrap");

		// Warn area
		aStr = "Please enter text input.";
		infoL = GuiUtil.createJLabel(aStr, aFont);
		infoL.setForeground(warnColor);
		add(infoL, "growx,span 4,wrap");

		// Control area
		cancelB = GuiUtil.createJButton("Cancel", this, aFont);
		acceptB = GuiUtil.createJButton("Accept", this, aFont);
		add(cancelB, "skip 2,span 1");
		add(acceptB, "span 1");

		setBorder(new BevelBorder(BevelBorder.RAISED));
	}

	/**
	 * Sets the Gui and associated components to the initial state
	 */
	public void resetGui()
	{
		isAccepted = false;
		inputTF.setText("");
		updateGui();
	}
	
	/**
	 * Utility method to update the various GUI components
	 * (most likely infoL, acceptB) based on the current
	 * inputTF.
	 */
	protected abstract void updateGui();

}
