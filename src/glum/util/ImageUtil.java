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

import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;

public class ImageUtil
{

	/**
	 * Utility method to return the Image associated with the specified resource.
	 */
	public static BufferedImage getImageForResource(String imagePath)
	{
		try
		{
			return ImageIO.read(ClassLoader.getSystemResource(imagePath));
		}
		catch(Exception aExp)
		{
			throw new RuntimeException("Failed to load resource " + imagePath, aExp);
		}
	}

}
