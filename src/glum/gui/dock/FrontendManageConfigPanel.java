package glum.gui.dock;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Collection;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;
import javax.swing.border.BevelBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

//import echo.gui.LookUp;

import com.google.common.collect.Lists;

import glum.database.QueryItem;
import glum.gui.FocusUtil;
import glum.gui.GuiUtil;
import glum.gui.action.ClickAction;
import glum.gui.panel.GlassPanel;
import glum.gui.panel.itemList.ItemListPanel;
import glum.gui.panel.itemList.StaticItemProcessor;
import glum.gui.panel.itemList.query.QueryComposer;
import glum.gui.panel.itemList.query.QueryItemHandler;

import bibliothek.gui.DockFrontend;

import net.miginfocom.swing.MigLayout;

public class FrontendManageConfigPanel extends GlassPanel implements ActionListener, ListSelectionListener
{
	// GUI vars
	protected ItemListPanel<ConfigItem> listPanel;
	protected JButton deleteB, closeB;

	// State vars
	protected DockFrontend refFrontend;
	protected StaticItemProcessor<ConfigItem> myItemProcessor;

	/**
	 * Constructor
	 * @param aFrontend 
	 */
	public FrontendManageConfigPanel(Component aParent, DockFrontend aFrontend)
	{
		super(aParent);

		// State vars
		refFrontend = aFrontend;

		// Build the actual GUI
		buildGuiArea();
		setPreferredSize(new Dimension(250, 300));//TODO:getPreferredSize().height));
		
		// Set up keyboard short cuts
		FocusUtil.addAncestorKeyBinding(this, "ESCAPE", new ClickAction(closeB));
		FocusUtil.addAncestorKeyBinding(this, "ENTER", new ClickAction(closeB));
		FocusUtil.addFocusKeyBinding(listPanel.getTable(), "ENTER", new ClickAction(closeB));
	}

	@Override
	public void setVisible(boolean aBool)
	{
		resetGui();
		
		super.setVisible(aBool);
	}

	@Override
	public void actionPerformed(ActionEvent e)
	{
		Object source;
		ConfigItem chosenItem;

		chosenItem = listPanel.getSelectedItem();

		source = e.getSource();
		if (source == deleteB)
		{
			refFrontend.delete(chosenItem.getName());
			resetGui();
		}
		else if (source == closeB)
		{
			setVisible(false);
		}
	}

	@Override
	public void valueChanged(ListSelectionEvent aEvent)
	{
		if (aEvent.getValueIsAdjusting() == true)
			return;
		
		updateGui();
	}

	/**
	 * buildGuiArea - Forms the actual dialog GUI
	 */
	private void buildGuiArea()
	{
		JLabel tmpL;
		QueryItemHandler<ConfigItem> aItemHandler;
		QueryComposer<LookUp> aComposer;

		
		setLayout(new MigLayout("", "[grow][][]", "[][grow][]"));
		
		tmpL = new JLabel("Select configuration:", JLabel.CENTER);
		add(tmpL, "growx,span 4,wrap");
		
		// Construct the actual table panel
		aComposer = new QueryComposer<LookUp>();
		aComposer.addAttribute(LookUp.Name, String.class, "Configuration", null);

		myItemProcessor = new StaticItemProcessor<ConfigItem>();
		aItemHandler = new QueryItemHandler<ConfigItem>(aComposer);
		listPanel = new ItemListPanel<ConfigItem>(aItemHandler, myItemProcessor, false, true);
		listPanel.addListSelectionListener(this);
		add(listPanel, "growx,growy,span 4,wrap");
		
		// Control area
		deleteB = GuiUtil.createJButton("Delete", this);
		closeB = GuiUtil.createJButton("Close", this);

		add(deleteB, "skip 1,span 1");
		add(closeB, "span 1");
		
		setBorder(new BevelBorder(BevelBorder.RAISED));
	}

	/**
	 * Utility method to update item list
	 */
	private void resetGui()
	{
		Collection<String> strList;
		Collection<ConfigItem> itemList;

		itemList = Lists.newLinkedList();

		strList = refFrontend.getSettings();
		for (String aStr : strList)
		{
			// Add only non reserved items
			if (aStr.charAt(0) != '.')
				itemList.add(new ConfigItem(aStr));
		}
		
		myItemProcessor.setItems(itemList);
				
		// TODO: Ugly code: Should be able to just call updateGui but can not
		SwingUtilities.invokeLater(new Runnable()
		{
			
			@Override
			public void run()
			{
				updateGui();
			}
		});
	}

	/**
	 * Utility method to update the individual gui components
	 */
	private void updateGui()
	{
		ConfigItem chosenItem;
		boolean isEnabled;

		chosenItem = listPanel.getSelectedItem();

		if (chosenItem != null)
			refFrontend.load(chosenItem.getName());
		
		isEnabled = chosenItem != null;
		deleteB.setEnabled(isEnabled);
		
		// Ensure we have the focus.
//listPanel.requestFocusInWindow();		
		repaint();

		// TODO: Ugly code: Not sure why need to request focus multiple times to ensure we regrab 
		// the focus. This is particularly true when there are multiple DockStations located on different
		// windows
		SwingUtilities.invokeLater(new Runnable()
		{
			
			@Override
			public void run()
			{
				listPanel.getTable().requestFocus();
				
				SwingUtilities.invokeLater(new Runnable()
				{
					
					@Override
					public void run()
					{
						listPanel.getTable().requestFocus();
					}
				});
				
//				listPanel.getTable().getFocusCycleRootAncestor().setFocusCycleRoot(true);		
				listPanel.getTable().requestFocusInWindow();
				repaint();
			}
		});
	}
	
		
	/**
	 * Internal class only used to wrap named (string) settings into
	 * a QueryItem 
	 */
	class ConfigItem implements QueryItem<LookUp>
	{
		private String refName;
		
		public ConfigItem(String aName)
		{
			refName = aName;
		}

		/**
		 * Accessor method
		 */
		public String getName()
		{
			return refName;
		}

		@Override
		public Object getValue(LookUp aLookUp)
		{
			return refName;
		}

		@Override
		public void setValue(LookUp aLookUp, Object aObj)
		{
			throw new UnsupportedOperationException();
		}
		
	}

}
