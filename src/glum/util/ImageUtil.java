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
