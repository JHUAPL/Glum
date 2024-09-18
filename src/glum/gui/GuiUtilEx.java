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
package glum.gui;

import java.awt.*;

/**
 * Collection of additional AWT/Swing based utility methods.
 *
 * @author lopeznr1
 */
public class GuiUtilEx
{
	/**
	 * Utility method that returns the default dimension for the main window.
	 * <p>
	 * The returned {@link Dimension} will have a minimum size of [aMinX,aMinY] or [80%,80%] of the screen resolution
	 * (which ever is smaller).
	 */
	public static Dimension getDimensionDefaultMain(int aMinX, int aMinY)
	{
		var screenDim = Toolkit.getDefaultToolkit().getScreenSize();
		int winW = Math.min(aMinX, (int) (screenDim.getWidth() * 0.80));
		int winH = Math.min(aMinY, (int) (screenDim.getHeight() * 0.80));
		return new Dimension(winW, winH);
	}

	/**
	 * Utility method that returns the default dimension for the main window.
	 * <p>
	 * The returned {@link Dimension} will have a minimum size of [950, 750] or 80% of the screen resolution.
	 */
	public static Dimension getDimensionDefaultMain()
	{
		// Delegate
		return getDimensionDefaultMain(950, 750);
	}

	/**
	 * Utility method that returns a new Dimension where the size has been scaled by the specified scalars.
	 *
	 * @param aDimension
	 *        The original dimension
	 * @param aScalerX
	 *        Percent to scale the dimensions width.
	 * @param aScalerY
	 *        Percent to scale the dimensions height.
	 */
	public static Dimension getDimensionScaled(Dimension aDimension, double aScalerX, double aScalerY)
	{
		int winW = Math.min(950, (int) (aDimension.getWidth() * aScalerX));
		int winH = Math.min(750, (int) (aDimension.getHeight() * aScalerY));
		return new Dimension(winW, winH);
	}

	/**
	 * Utility method that returns the (first) parent that matches the specified Class. If there is no such component
	 * then null will be returned.
	 */
	public static Component getParent(Component aComp, Class<?> aClass)
	{
		// Search through all the (grand)parents
		var parent = aComp.getParent();
		while (parent != null)
		{
			// Bail once we have a matching class
			if (parent.getClass() == aClass)
				return parent;

			// Next parent
			parent = parent.getParent();
		}

		return null;
	}

}
