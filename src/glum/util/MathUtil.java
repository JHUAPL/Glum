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
package glum.util;

import java.awt.Dimension;
import java.awt.Point;

public class MathUtil
{
	/**
	 * Utility method that returns aVal to be clamped within the range of minVal or maxVal.
	 */
	public static double boundRange(double minVal, double maxVal, double aVal)
	{
		if (aVal < minVal)
			return minVal;

		if (aVal > maxVal)
			return maxVal;

		return aVal;
	}

	/**
	 * Utility method that returns aVal to be clamped within the range of minVal or maxVal.
	 */
	public static long boundRange(long minVal, long maxVal, long aVal)
	{
		if (aVal < minVal)
			return minVal;

		if (aVal > maxVal)
			return maxVal;

		return aVal;
	}

	/**
	 * Utility method that returns aVal to be clamped within the range of minVal or maxVal.
	 */
	public static int boundRange(int minVal, int maxVal, int aVal)
	{
		if (aVal < minVal)
			return minVal;

		if (aVal > maxVal)
			return maxVal;

		return aVal;
	}

	/**
	 * Utility method to ensure that a window defined by targLoc and targDim will be fully in the area defined by the
	 * bound of (x0,y0 - x1,y1). The targLoc and targDim will be modified to fit the constraints. Note the window will be
	 * made no smaller than minDim. If it is impossible to honor the constraints then no modifications will be made.
	 * 
	 * @param tryKeepDim
	 *           True, if preservation of dimension is more important than preservation of location.
	 */
	public static void forceConstraints(Point targLoc, Dimension targDim, int x0, int y0, int x1, int y1, Dimension minDim, boolean tryKeepDim)
	{
		int boundW, boundH;
		int eX, eY;

		// Bail if the constraints are nonsensical
		boundW = x1 - x0;
		boundH = y1 - y0;
		if (minDim.width > boundW || minDim.height > boundH)
			return;

		// Move the panel towards the northwest corner if any part are overhanging the southeast region
		if (tryKeepDim == true)
		{
			// Ensure the southeast location is fully contained in the specified bounds
			if (targLoc.x + targDim.width > x1)
				targLoc.x = x1 - targDim.width;

			if (targLoc.y + targDim.height > y1)
				targLoc.y = y1 - targDim.height;
		}

		// Ensure the northwest location is fully contained in the specified bounds
		if (targLoc.x < x0)
		{
			targDim.width -= x0 - targLoc.x;
			targLoc.x = x0;
		}

		if (targLoc.y < y0)
		{
			targDim.height -= y0 - targLoc.y;
			targLoc.y = y0;
		}

		// Ensure the targDim is no bigger than the bounded area
		if (targDim.width > boundW)
		{
			targDim.width = boundW;
		}

		if (targDim.height > boundH)
		{
			targDim.height = boundH;
		}

		// Ensure the minimum dimension is honored
		if (targDim.width < minDim.width)
		{
			eX = minDim.width - targDim.width;
			targDim.width = minDim.width;

			if (targLoc.x > x0)
				targLoc.x -= eX;
		}

		if (targDim.height < minDim.height)
		{
			eY = minDim.height - targDim.height;
			targDim.height = minDim.height;

			if (targLoc.y > y0)
				targLoc.y -= eY;
		}

	}

}
