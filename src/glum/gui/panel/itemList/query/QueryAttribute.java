// Copyright (C) 2024 The Johns Hopkins University Applied Physics Laboratory LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package glum.gui.panel.itemList.query;

import java.io.IOException;
import java.util.Comparator;

import javax.swing.JLabel;
import javax.swing.table.*;

import glum.gui.table.SortDir;
import glum.unit.EmptyUnitProvider;
import glum.unit.UnitProvider;
import glum.zio.*;

/**
 * Class that provides for the definition of attributes associated with a specific data field.
 *
 * @author lopeznr1
 */
public class QueryAttribute<G1 extends Enum<?>> implements ZioObj
{
	// Attributes
	public final G1 refKey;
	public final int modelIndex;
	public final Class<?> refClass;

	// State vars
	public Comparator<?> refComparator;
	public UnitProvider refUnitProvider;

	// Config vars
	public String label;
	public boolean isVisible;
	public int alignment;
	public int defaultSize;
	public int minSize;
	public int maxSize;
	public SortDir sortDir;

	// Helper vars
	public TableColumn assocTableColumn;
	public TableCellRenderer renderer;
	public TableCellEditor editor;

	/**
	 * Standard Constructor.
	 *
	 * @param aRefKey
	 * @param aRefClass
	 * @param aModelIndex
	 */
	public QueryAttribute(G1 aRefKey, Class<?> aRefClass, int aModelIndex)
	{
		refKey = aRefKey;
		refClass = aRefClass;
		modelIndex = aModelIndex;

		refComparator = null;
		refUnitProvider = new EmptyUnitProvider();

		label = "";
		isVisible = true;
		alignment = JLabel.LEFT;
		defaultSize = 100;
		maxSize = -1;
		minSize = -1;
		sortDir = SortDir.NotSorted;

		assocTableColumn = null;
		renderer = null;
		editor = null;
	}

	/**
	 * Copy Constructor.
	 *
	 * @param aAttribute
	 */
	public QueryAttribute(QueryAttribute<G1> aAttribute)
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
		renderer = null; // aAttribute.renderer;
		editor = null; // aAttribute.editor;
	}

	/**
	 * Sets this QueryAttribute to match aAttribute.
	 * <p>
	 * Currently only the following config vars are matched: label, isVisible, alignment, defaultSize, sortDir
	 */
	public void setConfig(QueryAttribute<G1> aAttribute)
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
		sortDir = aStream.readEnum(SortDir.values());
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
		aStream.writeEnum(sortDir);
	}

}
