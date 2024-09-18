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

/**
 * Immutable object which provides access to attributes of a color.
 * <p>
 * The attributes are: hue, saturation, brightness, luminance
 *
 * @author lopeznr1
 */
public class ColorHSBL
{
	// Attributes
	private final Color refColor;
	private final float hue;
	private final float saturation;
	private final float brightness;
	private final float luminance;

	/** Standard Constructor */
	public ColorHSBL(Color aColor)
	{
		refColor = aColor;

		float[] tmpArr = convertColorToHSBL(aColor);
		hue = tmpArr[0];
		saturation = tmpArr[1];
		brightness = tmpArr[2];
		luminance = tmpArr[3];
	}

	/**
	 * Returns the associated {@link Color}.
	 */
	public Color getColor()
	{
		return refColor;
	}

	/**
	 * Returns the hue component.
	 */
	public float getHue()
	{
		return hue;
	}

	/**
	 * Returns the saturation component.
	 */
	public float getSaturation()
	{
		return saturation;
	}

	/**
	 * Returns the brightness component.
	 */
	public float getBrightness()
	{
		return brightness;
	}

	/**
	 * Returns the luminance component.
	 */
	public float getLuminance()
	{
		return luminance;
	}

	/**
	 * Utility method that returns a 4 element array that has the following in the elements
	 * <ul>
	 * <li>hue
	 * <li>saturation
	 * <li>brightness
	 * <li>luminosity
	 * </ul>
	 * The source for this conversion originated from:</br>
	 * https://stackoverflow.com/questions/596216/formula-to-determine-perceived-brightness-of-rgb-color
	 * <p>
	 * Note the above source has changed overtime and the conversion below is not authoritative.
	 */
	public static float[] convertColorToHSBL(Color aColor)
	{
		int rI = aColor.getRed();
		int gI = aColor.getGreen();
		int bI = aColor.getBlue();

		float rF = rI / 255.0f;
		float gF = gI / 255.0f;
		float bF = bI / 255.0f;
		// Formula: broken
//		float lum = (float) Math.sqrt(0.241f * rF + 0.691f * gF + 0.068f * bF);
		// Formula: corrected
//		float lum = (0.299f * rF + 0.587f * gF + 0.114f * bF);
		float lum = (float) Math.sqrt(0.299f * rF * rF + 0.587f * gF * gF + 0.114f * bF * bF);

		float[] hsblArr = new float[4];
		Color.RGBtoHSB(rI, gI, bI, hsblArr);

		hsblArr[3] = lum;
		return hsblArr;
	}

}
