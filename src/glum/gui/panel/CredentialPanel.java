package glum.gui.panel;

import glum.net.Credential;
import glum.net.NetUtil;
import glum.net.Result;
import glum.gui.FocusUtil;
import glum.gui.GuiUtil;
import glum.gui.action.ClickAction;
import glum.gui.component.GPasswordField;
import glum.gui.component.GTextField;
import glum.gui.panel.GlassPanel;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.BevelBorder;

import net.miginfocom.swing.MigLayout;

public class CredentialPanel extends GlassPanel implements ActionListener
{
	// Constants
	private static final Color warnColor = new Color(128, 0, 0);

	// GUI vars
	protected JLabel titleL;
	protected JButton ignoreB, acceptB;
	protected JPasswordField passTF;
	protected GTextField userTF;
	protected JTextField sourceTF, warnTA;

	// State vars
	protected Credential myCredential;
	protected Result eResult;
	protected Boolean isReset;

	/**
	 * Constructor
	 */
	public CredentialPanel(Component aParent)
	{
		super(aParent);

		// State vars
		myCredential = null;
		eResult = null;
		isReset = true;

		// Build the actual GUI
		buildGuiArea();
		updateGui();

		// Set up some keyboard shortcuts
		FocusUtil.addAncestorKeyBinding(this, "ESCAPE", new ClickAction(ignoreB));
	}

	public Credential getCredential()
	{
		return myCredential;
	}

	public void setCredential(Credential aCredential, String aSourceUri)
	{
		// Construct the default (empty) credential
		if (aCredential == null)
			aCredential = new Credential("", null);

		userTF.removeActionListener(this);
		userTF.setText(aCredential.getUsername());
		userTF.addActionListener(this);

		passTF.removeActionListener(this);
		passTF.setText(aCredential.getPasswordAsString());
		passTF.addActionListener(this);

		sourceTF.setText(aSourceUri);
		sourceTF.setCaretPosition(0);

		// Reset the dialog
		isReset = true;
		EventQueue.invokeLater(new Runnable()
		{
			@Override
			public void run()
			{
				updateGui();
			}
		});
	}

	public void setTitle(String aTitle)
	{
		titleL.setText(aTitle);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object source;

		source = e.getSource();

		if (source == ignoreB)
		{
			// Hide the dialog
			myCredential = null;
			setVisible(false);

			// Notify our listeners
			notifyListeners(this, 0, "Ignore");
		}
		else if (source == acceptB || source == passTF)
		{
			// Set the GUI into the waiting mode
			isReset = null;
			updateGui();

			// Validate the settings
			final Credential aCredential = new Credential(userTF.getText(), passTF.getPassword());
			final String uriRoot = sourceTF.getText();

			new Thread()
			{
				@Override
				public void run()
				{
					final Result aResult;

					// Pause for 0.5 sec to let the user register the change
					try
					{
						Thread.sleep(500);
					}
					catch (InterruptedException aExp)
					{
						aExp.printStackTrace();
					}

					// Test the credentials
					aResult = NetUtil.checkCredentials(uriRoot, aCredential);

					// Update the Gui
					EventQueue.invokeLater(new Runnable()
					{
						@Override
						public void run()
						{
							// Bail if the user decided to quit before our results came
							// back.
							if (isVisible() == false)
								return;

							// If the credentials are valid then save them and bail
							if (aResult == Result.Success)
							{
								myCredential = aCredential;
								setVisible(false);

								// Notify our listeners
								notifyListeners(CredentialPanel.this, 0, "Accept");
							}
							// Try again
							else
							{
								eResult = aResult;
								isReset = false;
								updateGui();
							}
						}
					});
				}
			}.start();
		}
		else
		{
			updateGui();
		}
	}

	/**
	 * Construct the GUI
	 */
	private void buildGuiArea()
	{
		Dimension aDimension;
		JLabel tmpL;
		String aStr;

		// Form the grid bag constraints
		setLayout(new MigLayout("", "[right][grow][][]"));

		// Title Area
		titleL = new JLabel("Product Name", JLabel.CENTER);
		add(titleL, "growx,span 4,wrap");

		// Source area
		tmpL = new JLabel("Source:");
		add(tmpL);

		sourceTF = GuiUtil.createUneditableTextField("http://www.google.edu");
		add(sourceTF, "growx,span 3,wrap");

		// Username area
		tmpL = new JLabel("Username:");
		userTF = new GTextField(this);
		add(tmpL);
		add(userTF, "growx,span 3,wrap");

		// Password area
		tmpL = new JLabel("Password:");
		passTF = new GPasswordField(this);
		add(tmpL);
		add(passTF, "growx,span 3,wrap");

		// Warn area
		aStr = "Please enter the credentials for accessing the data.";
		warnTA = GuiUtil.createUneditableTextField(aStr);
		warnTA.setForeground(warnColor);
		add(warnTA, "growx,span 4,wrap");

		// Action area
		aDimension = GuiUtil.computePreferredJButtonSize("Ignore", "Accept");
		ignoreB = GuiUtil.createJButton("Ignore", this, aDimension);
		acceptB = GuiUtil.createJButton("Accept", this, aDimension);
		add(ignoreB, "skip 2,span 1");
		add(acceptB, "span 1");

		setBorder(new BevelBorder(BevelBorder.RAISED));
	}

	/**
	 * Utility method to update the GUI
	 */
	private void updateGui()
	{
		boolean isEnabled;

		if (isReset == null)
		{
			warnTA.setText("Checking the credentials...");
			isEnabled = false;
		}
		else
		{
			if (userTF.getText().isEmpty() == true && passTF.getPassword().length < 2)
				isReset = true;

			isEnabled = true;
			if (isReset == true)
			{
				warnTA.setText("Please enter the credentials for accessing the data.");
			}
			else
			{
				switch (eResult)
				{
					case BadCredentials:
					warnTA.setText("Credentials are invalid.");
					break;

					case ConnectFailure:
					warnTA.setText("Failed to connect to resource.");
					break;

					case UnreachableHost:
					warnTA.setText("Unreachable host.");
					break;

					default:
					warnTA.setText("Unreconzied error. Error: " + eResult);
					break;
				}
			}
		}

		// Update the UI elements
		userTF.setEnabled(isEnabled);
		passTF.setEnabled(isEnabled);
		acceptB.setEnabled(isEnabled);
	}

}
