package glum.gui.panel.itemList.query;

import java.io.IOException;

import javax.swing.JLabel;
import javax.swing.table.*;

import glum.unit.EmptyUnitProvider;
import glum.unit.UnitProvider;
import glum.zio.*;

public class QueryAttribute implements ZioObj
{
	// State vars
	public final int modelIndex;
	public Enum<?> refKey;
	public Class<?> refClass;
	public UnitProvider refUnitProvider;
	
	// Config vars
	public String label;
	public boolean isVisible;
	public int alignment;
	public int defaultSize;
	public int minSize;
	public int maxSize;
	public int sortDir;

	// Helper vars
	public TableColumn assocTableColumn;
	public TableCellRenderer renderer;
	public TableCellEditor editor;

	public QueryAttribute(int aModelIndex)
	{
		modelIndex = aModelIndex;
		refKey = null;
		refClass = String.class;
		label = "";

		isVisible = true;
		alignment = JLabel.LEFT;
		defaultSize = 100;
		maxSize = -1;
		minSize = -1;
		sortDir = 0;
		refUnitProvider = new EmptyUnitProvider();

		assocTableColumn = null;
		renderer = null;
		editor = null;
	}
	
	public QueryAttribute(QueryAttribute aAttribute)
	{
		// Synchronize the attribute before copying the configuration
		aAttribute.synchronizeAttribute();
		
		modelIndex = aAttribute.modelIndex;
		refKey = aAttribute.refKey;
		refClass = aAttribute.refClass;
		label = aAttribute.label;

		isVisible = aAttribute.isVisible;
		alignment = aAttribute.alignment;
		defaultSize = aAttribute.defaultSize;
		maxSize = aAttribute.maxSize;
		minSize = aAttribute.minSize;
		sortDir = aAttribute.sortDir;
		refUnitProvider = aAttribute.refUnitProvider;

		assocTableColumn = null;
		renderer = null; //aAttribute.renderer;
		editor = null; //aAttribute.editor;
	}
	
	/**
	 * Sets this QueryAttribute to match aAttribute.
	 *<P> 
	 * Currently only the following config vars are matched:
	 * label, isVisible, alignment, defaultSize, sortDir
	 */
	public void setConfig(QueryAttribute aAttribute)
	{
		label = aAttribute.label;
		isVisible = aAttribute.isVisible;
		alignment = aAttribute.alignment;
		defaultSize = aAttribute.defaultSize;
		sortDir = aAttribute.sortDir;
	}

	/**
	 * Synchronizes the QueryAttribute to it's associated column
	 */
	public void synchronizeAttribute()
	{
		if (assocTableColumn == null)
			return;
		
		defaultSize = assocTableColumn.getWidth();
//		sortDir = assocTableColumn.
	}

	/**
	 * Synchronizes the associated column to this QueryAttribute
	 */
	public void synchronizeColumn()
	{
		if (assocTableColumn == null)
			return;

System.out.println("Are we ready for this ???");		
		assocTableColumn.setWidth(defaultSize);
//		sortDir = assocTableColumn.
	}

	@Override
	public void zioRead(ZinStream aStream) throws IOException
	{
		aStream.readVersion(0);
		
		label = aStream.readString();
		isVisible = aStream.readBool();
		alignment = aStream.readInt();
		defaultSize = aStream.readInt();
		minSize = aStream.readInt();
		maxSize = aStream.readInt();
		sortDir = aStream.readInt();
	}

	@Override
	public void zioWrite(ZoutStream aStream) throws IOException
	{
		// Synchronize the attribute before serialization
		synchronizeAttribute();
		
		aStream.writeVersion(0);
		
		aStream.writeString(label);
		aStream.writeBool(isVisible);
		aStream.writeInt(alignment);
		aStream.writeInt(defaultSize);
		aStream.writeInt(minSize);
		aStream.writeInt(maxSize);
		aStream.writeInt(sortDir);
	}
	
}
