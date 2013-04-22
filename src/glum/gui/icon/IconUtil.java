package glum.gui.icon;

import java.awt.Component;
import javax.swing.ImageIcon;
import javax.swing.JButton;

public class IconUtil
{
	/**
	 * Utility method to load the Icon specified by the resource path.
	 */
	public static ImageIcon loadIcon(String iconPath)
	{
//		URL aURL;
//		
//		aURL = IconUtil.class.getClassLoader().getResource(iconPath);
//		if (aURL == null)
//			throw new RuntimeException("Failed to load icon for path: " + iconPath);
//					
//		return new ImageIcon(aURL);
		return new ImageIcon(ClassLoader.getSystemResource(iconPath));
	}

	/**
	 * Utility method to test if aComp is pressed down.
	 */
	public static boolean isPressed(Component aComp)
	{
		if (aComp instanceof JButton == false)
			return false;
		
		return ((JButton)aComp).getModel().isPressed();
	}

}
