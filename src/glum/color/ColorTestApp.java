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
package glum.color;

import java.awt.Color;
import java.util.*;

import javax.swing.JFrame;
import javax.swing.JPanel;

import com.google.common.collect.ImmutableList;

import glum.gui.misc.BooleanCellEditor;
import glum.gui.misc.BooleanCellRenderer;
import glum.gui.panel.itemList.*;
import glum.gui.panel.itemList.query.QueryComposer;
import glum.gui.table.ColorHSBLCellRenderer;
import glum.gui.table.PrePendRenderer;
import net.miginfocom.swing.MigLayout;

/**
 * Demo application that demonstrates a number of features:
 * <ul>
 * <li>Showing color attributes via a table
 * <li>Alternative color sorting
 * <li>Multicolmun sorting
 * </ul>
 *
 * @author lopeznr1
 */
public class ColorTestApp
{
	// Constants
	private static final int DefNumGroups = 8;
//	private static final int DefNumItems = 100; // 3100
	private static final int DefNumItems = 3100;

	/**
	 * Main entry point.
	 */
	public static void main(String[] args)
	{
		JFrame frame = new JFrame("TableColorTest");
		frame.setContentPane(formTestPanel(DefNumGroups, DefNumItems));

		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(true);
	}

	private static JPanel formTestPanel(int aNumGroups, int aNumItems)
	{
		ItemListPanel<TestItem, LookUp> tmpILP;

		JPanel retPanel = new JPanel(new MigLayout());

		// Table Content
		QueryComposer<LookUp> tmpComposer = new QueryComposer<>();
		tmpComposer.addAttribute(LookUp.IsVisible, Boolean.class, "Show", 50);
		tmpComposer.addAttribute(LookUp.Index, Integer.class, "ID", "Trk: 99");

		tmpComposer.addAttribute(LookUp.HSBL, Color.class, "HBL", 50);
		tmpComposer.addAttribute(LookUp.HSBL, Color.class, "HLB", 50);
		tmpComposer.addAttribute(LookUp.HSBL, Color.class, "HLS", 50);
		tmpComposer.addAttribute(LookUp.HSBL, Color.class, "HSB", 50);
		tmpComposer.addAttribute(LookUp.HSBL, Color.class, "HSL", 50);
		tmpComposer.addAttribute(LookUp.HSBL, Color.class, "SBH", 50);
		tmpComposer.addAttribute(LookUp.HSBL, Color.class, "SBL", 50);
		tmpComposer.addAttribute(LookUp.HSBL, Color.class, "SLB", 50);

		tmpComposer.addAttribute(LookUp.Message, String.class, "Message", null);
		tmpComposer.addAttribute(LookUp.Red, Integer.class, "Red", 40);
		tmpComposer.addAttribute(LookUp.Green, Integer.class, "Green", 40);
		tmpComposer.addAttribute(LookUp.Blue, Integer.class, "Blue", 40);
		tmpComposer.addAttribute(LookUp.Hue, Integer.class, "Hue:" + aNumGroups, 60);
		tmpComposer.addAttribute(LookUp.Sat, Integer.class, "Sat:" + aNumGroups, 60);
		tmpComposer.addAttribute(LookUp.Lum, Integer.class, "Lum:" + aNumGroups, 60);
		tmpComposer.addAttribute(LookUp.Bri, Integer.class, "Bri:" + aNumGroups, 60);

		tmpComposer.setEditor(LookUp.IsVisible, new BooleanCellEditor());
		tmpComposer.setRenderer(LookUp.IsVisible, new BooleanCellRenderer());
		tmpComposer.setRenderer(LookUp.Index, new PrePendRenderer("Trk: "));
		tmpComposer.setRenderer(LookUp.HSBL, new ColorHSBLCellRenderer(false));

//		KeyType[] sortKeyArr = {KeyType.Hue, KeyType.Saturation, KeyType.Luminance};
		List<KeyAttr> sortKeyL_HBL = ImmutableList.of(KeyAttr.Hue, KeyAttr.Brightness, KeyAttr.Luminance);
		List<KeyAttr> sortKeyL_HLB = ImmutableList.of(KeyAttr.Hue, KeyAttr.Luminance, KeyAttr.Brightness);
		List<KeyAttr> sortKeyL_HLS = ImmutableList.of(KeyAttr.Hue, KeyAttr.Luminance, KeyAttr.Saturation);
		List<KeyAttr> sortKeyL_HSB = ImmutableList.of(KeyAttr.Hue, KeyAttr.Saturation, KeyAttr.Brightness);
		List<KeyAttr> sortKeyL_HSL = ImmutableList.of(KeyAttr.Hue, KeyAttr.Saturation, KeyAttr.Luminance);
		List<KeyAttr> sortKeyL_SBH = ImmutableList.of(KeyAttr.Saturation, KeyAttr.Brightness, KeyAttr.Hue);
		List<KeyAttr> sortKeyL_SBL = ImmutableList.of(KeyAttr.Saturation, KeyAttr.Brightness, KeyAttr.Luminance);
		List<KeyAttr> sortKeyL_SLB = ImmutableList.of(KeyAttr.Saturation, KeyAttr.Luminance, KeyAttr.Brightness);

		ItemHandler<TestItem, LookUp> tmpIH = new TestItemHandler(aNumGroups);
		StaticItemProcessor<TestItem> tmpIP = new StaticItemProcessor<>(formTestItemList(aNumItems));
		tmpILP = new ItemListPanel<>(tmpIH, tmpIP, tmpComposer, true);
		tmpILP.setSortingEnabled(true);
//		tmpILP.setSortComparator(ColorHSBL.class, new ColorStepSortCompartor(numGroups));
		tmpILP.setSortComparator(2, new ColorHSBLCompartor(sortKeyL_HBL, aNumGroups));
		tmpILP.setSortComparator(3, new ColorHSBLCompartor(sortKeyL_HLB, aNumGroups));
		tmpILP.setSortComparator(4, new ColorHSBLCompartor(sortKeyL_HLS, aNumGroups));
		tmpILP.setSortComparator(5, new ColorHSBLCompartor(sortKeyL_HSB, aNumGroups));
		tmpILP.setSortComparator(6, new ColorHSBLCompartor(sortKeyL_HSL, aNumGroups));
		tmpILP.setSortComparator(7, new ColorHSBLCompartor(sortKeyL_SBH, aNumGroups));
		tmpILP.setSortComparator(8, new ColorHSBLCompartor(sortKeyL_SBL, aNumGroups));
		tmpILP.setSortComparator(9, new ColorHSBLCompartor(sortKeyL_SLB, aNumGroups));

//		JTable tmpTable = lidarILP.getTable();
//		tmpTable.setAutoResizeMode(JTable.AUTO_RESIZE_OFF);
//		retPanel.add(new JScrollPane(tmpTable), "growx,growy,pushx,pushy,span,wrap");
		retPanel.add(tmpILP, "growx,growy,pushx,pushy,span,wrap");

		tmpIP.setItems(formTestItemList(aNumItems));

		return retPanel;
	}

	/**
	 * Helper method that forms the list of TestItems.
	 */
	private static List<TestItem> formTestItemList(int aNumItems)
	{
		List<TestItem> retL = new ArrayList<>();

		Random tmpRandom = new Random(100);
		for (int c1 = 0; c1 < aNumItems; c1++)
		{
			int r = tmpRandom.nextInt(256);
			int g = tmpRandom.nextInt(256);
			int b = tmpRandom.nextInt(256);
			Color tmpColor = new Color(r, g, b);
			String tmpMsg = String.format("Color: %3d %3d %3d", r, g, b);
			retL.add(new TestItem(c1, tmpColor, tmpMsg));
		}

		return retL;
	}

	enum LookUp
	{
		IsVisible,

		Index,

		Color,

		HSBL,

		Message,

		Red,

		Green,

		Blue,

		Hue,

		Sat,

		Lum,

		Bri
	}

	static class TestItem
	{
		// Attributes
		private final int index;
		private final Color color;
		private final String message;

		// Cache vars
		private final ColorHSBL cHSBL;

		// State vars
		private boolean isVisible;

		public TestItem(int aIndex, Color aColor, String aMessage)
		{
			index = aIndex;
			color = aColor;
			message = aMessage;

			cHSBL = new ColorHSBL(color);

			isVisible = true;
		}
	}

	static class TestItemHandler implements ItemHandler<TestItem, LookUp>
	{
		// Attributes
		private final int numGroups;

		/** Standard Constructor */
		public TestItemHandler(int aNumGroups)
		{
			numGroups = aNumGroups;
		}

		@Override
		public Object getValue(TestItem aItem, LookUp aEnum)
		{
			switch (aEnum)
			{
				case IsVisible:
					return aItem.isVisible;
				case Index:
					return aItem.index;
				case Color:
					return aItem.color;
				case HSBL:
					return aItem.cHSBL;
				case Message:
					return aItem.message;
				case Red:
					return aItem.color.getRed();
				case Green:
					return aItem.color.getGreen();
				case Blue:
					return aItem.color.getBlue();
				case Hue:
					return (int) (aItem.cHSBL.getHue() * numGroups);
				case Sat:
					return (int) (aItem.cHSBL.getSaturation() * numGroups);
				case Bri:
					return (int) (aItem.cHSBL.getBrightness() * numGroups);
				case Lum:
					return (int) (aItem.cHSBL.getLuminance() * numGroups);
				default:
					break;
			}

			throw new UnsupportedOperationException("Column is not supported. Enum: " + aEnum);
		}

		@Override
		public void setValue(TestItem aItem, LookUp aEnum, Object aValue)
		{
			if (aEnum == LookUp.IsVisible)
				aItem.isVisible = (Boolean) aValue;
			else
				throw new UnsupportedOperationException("Column is not supported. Enum: " + aEnum);
		}

	}

}
