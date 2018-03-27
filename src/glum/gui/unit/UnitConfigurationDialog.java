package glum.gui.unit;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import javax.swing.table.*;

import glum.gui.FocusUtil;
import glum.gui.GuiUtil;
import glum.gui.action.ClickAction;
import glum.gui.panel.CardPanel;
import glum.gui.panel.itemList.*;
import glum.gui.panel.itemList.query.QueryComposer;
import glum.unit.UnitListener;
import glum.unit.UnitProvider;
import net.miginfocom.swing.MigLayout;

public class UnitConfigurationDialog extends JDialog implements ActionListener, ListSelectionListener, UnitListener
{
	// Gui Components
	private ItemListPanel<UnitProvider> itemLP;
	private CardPanel<EditorPanel> editorPanel;
	private JLabel titleL;
	private JButton closeB;

	// State vars
	private StaticItemProcessor<UnitProvider> itemProcessor;

	public UnitConfigurationDialog(JFrame aParentFrame)
	{
		// Make sure we call the parent
		super(aParentFrame);

		// Set the characteristics for this dialog
		setTitle("Edit Unit");

		setDefaultCloseOperation(HIDE_ON_CLOSE);
		setLocationRelativeTo(aParentFrame);
		setModal(false);

		// Build the actual GUI
		buildGuiArea();
		updateGui();
		pack();

		// Set up keyboard short cuts
		FocusUtil.addWindowKeyBinding(getRootPane(), "ESCAPE", new ClickAction(closeB));
		FocusUtil.addWindowKeyBinding(getRootPane(), "ENTER", new ClickAction(closeB));
//		FocusUtil.addFocusKeyBinding(itemLP.getTable(), "ENTER", new ClickAction(closeB));
	}

	/**
	 * Adds an EditorPanel and associateds with aClass
	 */
	public void addEditorPanel(Class<?> aClass, EditorPanel aPanel)
	{
		Dimension aDim;

		// Insanity check
		if (aClass == null || aPanel == null)
			return;

		// Add the editorPanel and associate with the specified class
		editorPanel.addCard("" + aClass, aPanel);
		editorPanel.switchToCard("null");
		updateGui();

		aDim = editorPanel.getMinimumSize();
		for (EditorPanel evalPanel : editorPanel.getAllCards())
		{
			if (evalPanel.getMinimumSize().height > aDim.height)
				aDim.height = evalPanel.getMinimumSize().height;
		}

		// Set the dialog to the best initial size
		setSize(getPreferredSize().width, itemLP.getHeight() + closeB.getHeight() + titleL.getHeight() + aDim.height);
	}

	/**
	 * Adds a UnitProvider to our set of UnitProviders
	 */
	public void addUnitProvider(UnitProvider aUnitProvider)
	{
		List<UnitProvider> itemList;

		// Insanity check
		if (aUnitProvider == null)
			return;

		// Register for state changes on the UnitProvider
		aUnitProvider.addListener(this);

		// Update the table processor
		itemList = itemProcessor.getItems();
		itemList.add(aUnitProvider);
		;
		itemProcessor.setItems(itemList);

		// Update the dialog
		updateTable();
	}

	@Override
	public void actionPerformed(ActionEvent aEvent)
	{
		Object source;

		source = aEvent.getSource();
		if (source == closeB)
		{
			setVisible(false);
		}
	}

	@Override
	public void unitChanged(UnitProvider aProvider, String aKey)
	{
		itemLP.repaint();
	}

	@Override
	public void valueChanged(ListSelectionEvent aEvent)
	{
		Object source;

		source = aEvent.getSource();
		if (source == itemLP)
		{
			if (aEvent.getValueIsAdjusting() == true)
				return;

			updateGui();
		}
	}

	/**
	 * Form the actual GUI
	 */
	private void buildGuiArea()
	{
		QueryComposer<Lookup> aComposer;
		ItemHandler<UnitProvider> itemHandler;
		EditorPanel nullPanel;

		setLayout(new MigLayout("", "[center,grow]", "2[]3[grow][fill,growprio 200][]"));

		// Build the title label
		titleL = new JLabel("Units", JLabel.CENTER);
		add("span,wrap", titleL);

		// UnitProvider table
		aComposer = new QueryComposer<Lookup>();
		aComposer.addAttribute(Lookup.Key, String.class, "Type", null);
		aComposer.addAttribute(Lookup.Value, String.class, "Value", null);

		itemHandler = new UnitProviderHandler(aComposer);
		itemProcessor = new StaticItemProcessor<UnitProvider>();

		itemLP = new ItemListPanel<UnitProvider>(itemHandler, itemProcessor, false, false);
		itemLP.addListSelectionListener(this);
		itemLP.setSortingEnabled(false);
		add("growx,growy,h 70::,span,wrap", itemLP);

		// Form the editor area
		nullPanel = new EditorPanel();
		nullPanel.setPreferredSize(new Dimension(1, 1));

		editorPanel = new CardPanel<EditorPanel>();
		editorPanel.addCard("null", nullPanel);
		add("growx,growy,wrap", editorPanel);

		// Action area
		closeB = GuiUtil.createJButton("Close", this);
		add("align right", closeB);
	}

	/**
	 * updateGui
	 */
	private void updateGui()
	{
		EditorPanel aPanel;
		UnitProvider aUnitProvider;
		Dimension aDim;
		String cardName;

		// Get the selected UnitProvider
		aUnitProvider = itemLP.getSelectedItem();

		// Switch to the appropriate Editor
		cardName = "null";
		if (aUnitProvider != null)
			cardName = "" + aUnitProvider.getClass();
		editorPanel.switchToCard(cardName);

		// Resize the editorPanel to be as compact as the active card
		aPanel = editorPanel.getActiveCard();
		aPanel.setUnitProvider(aUnitProvider);
		aDim = aPanel.getPreferredSize();
//		System.out.println("minHeight: " + aDim.getHeight() + "   hmm: " + aPanel);

		editorPanel.setMaximumSize(new Dimension(5000, aDim.height));
		// Hack to get the editorPanel resize properly. Not sure why invalidate(), validate() do not work
		int aHeight = getHeight();
		Runnable tmpRunnable1 = () -> setSize(getWidth(), aHeight - 1);
		Runnable tmpRunnable2 = () -> setSize(getWidth(), aHeight);
		SwingUtilities.invokeLater(tmpRunnable1);
		SwingUtilities.invokeLater(tmpRunnable2);
//		Runnable tmpRunnable1 = () -> editorPanel.invalidate();
//		Runnable tmpRunnable2 = () -> editorPanel.validate();
//		invalidate();
//		validate();
	}

	/**
	 * updateTable
	 */
	private void updateTable()
	{
		List<UnitProvider> itemList;
		JTableHeader aTableHeader;
		TableColumnModel aTableColumnModel;
		TableColumn aTableColumn;
		int aWidth, tmpWidth;
		JLabel aLabel;

		// Update myTable column[0] width
		aWidth = 10;
		aLabel = new JLabel("");
		itemList = itemProcessor.getItems();
		for (UnitProvider aUnitProvider : itemList)
		{
			aLabel.setText(aUnitProvider.getDisplayName());
			tmpWidth = aLabel.getPreferredSize().width + 5;
			if (aWidth < tmpWidth)
				aWidth = tmpWidth;
		}

		// Set sizing attributes of the column 1
		aTableHeader = itemLP.getTable().getTableHeader();
		aTableColumnModel = aTableHeader.getColumnModel();
		aTableColumn = aTableColumnModel.getColumn(0);
		aTableColumn.setResizable(false);
		aTableColumn.setMinWidth(aWidth);
		aTableColumn.setMaxWidth(aWidth);
		aTableColumn.setPreferredWidth(aWidth);
	}

	/**
	 * Helper classes to aide with setting up the UnitProvider table
	 */
	enum Lookup
	{
		Key,
		Value,
	};

	public class UnitProviderHandler extends BasicItemHandler<UnitProvider>
	{
		public UnitProviderHandler(QueryComposer<?> aComposer)
		{
			super(aComposer);
		}

		@Override
		public Object getColumnValue(UnitProvider aItem, int aColNum)
		{
			Enum<?> refKey;

			// Insanity check
			if (aColNum < 0 && aColNum >= fullAttributeList.size())
				return null;

			refKey = fullAttributeList.get(aColNum).refKey;
			switch ((Lookup)refKey)
			{
				case Key:
					return aItem.getDisplayName();

				case Value:
					return aItem.getConfigName();

				default:
					break;
			}

			return null;
		}

		@Override
		public void setColumnValue(UnitProvider aItem, int colNum, Object aValue)
		{
			throw new RuntimeException("Unsupported Operation.");
		}

	}

}
