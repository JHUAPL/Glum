package glum.io;

import glum.zio.*;
import glum.zio.util.ZioUtil;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import javax.swing.JFileChooser;
import javax.swing.plaf.FileChooserUI;

/**
 * Mutable object that contains the details needed to display a load or save GUI component. This object will typically be changed whenever the component is
 * modified.
 */
public class LoaderInfo implements ZioObj
{
	// Gui vars
	private boolean isVisible;
	private Point position;
	private Dimension dimension;

	// Path vars
	private String filePath;

	public LoaderInfo(File aFilePath)
	{
		isVisible = false;
		position = null;
		dimension = null;

		filePath = null;
		if (aFilePath != null)
			filePath = aFilePath.getAbsolutePath();
	}

	public LoaderInfo()
	{
		this(null);
	}

	/**
	 * Loads the current configuration into aFileChooser
	 */
	public void loadConfig(JFileChooser aFileChooser)
	{
		if (position != null)
			aFileChooser.setLocation(position);

		if (dimension != null)
		{
			aFileChooser.setPreferredSize(dimension);
			aFileChooser.setSize(dimension);
		}

		// Bail if there is no filePath
		if (filePath == null)
			return;

		if (aFileChooser.getFileSelectionMode() == JFileChooser.DIRECTORIES_ONLY)
		{
			File tmpFile = new File(filePath);

			// Locate the first folder that exists
//			File workPath = tmpFile.getParentFile();
			File workPath = tmpFile;
			while (workPath != null && workPath.isDirectory() == false)
				workPath = workPath.getParentFile();

			// Set the FileChooser to the first folder that exists
			aFileChooser.setCurrentDirectory(workPath);

			// Set the FileChooser's name area to reflect the absolute path
			if (workPath != null)
			{
				String absPathStr = tmpFile.getAbsolutePath();
				try
				{
					FileChooserUI tmpFCUI = aFileChooser.getUI();
					Class<? extends FileChooserUI> fcClass = tmpFCUI.getClass();
					Method setFileName = fcClass.getMethod("setFileName", String.class);
					setFileName.invoke(tmpFCUI, absPathStr);
				}
				catch(Exception aExp)
				{
					aExp.printStackTrace();
				}
			}
		}
		else
		{
			File tmpPath = new File(filePath);
			if (tmpPath.isFile() == true)
			{
				aFileChooser.setSelectedFile(tmpPath);
			}
			else
			{
				// Set the FileChooser's current directory to the first folder that exists
				while (tmpPath != null && tmpPath.getParentFile().isDirectory() == false)
					tmpPath = tmpPath.getParentFile();

				aFileChooser.setCurrentDirectory(tmpPath);
			}
		}
	}

	/**
	 * Stores the current configuration from aFileChooser
	 */
	public void saveConfig(JFileChooser aFileChooser)
	{
		position = aFileChooser.getLocation();
		dimension = aFileChooser.getSize();
		isVisible = aFileChooser.isVisible();

		filePath = aFileChooser.getCurrentDirectory().getAbsolutePath();
	}

	/**
	 * Sets the filePath of the LoaderInfo.
	 */
	public void setFilePath(String aFilePath)
	{
		filePath = aFilePath;
	}

	@Override
	public void zioRead(ZinStream aStream) throws IOException
	{
		aStream.readVersion(0);

		isVisible = aStream.readBool();

		position = ZioUtil.readPoint(aStream);
		dimension = ZioUtil.readDimension(aStream);

		filePath = aStream.readString();
	}

	@Override
	public void zioWrite(ZoutStream aStream) throws IOException
	{
		aStream.writeVersion(0);

		aStream.writeBool(isVisible);

		ZioUtil.writePoint(aStream, position);
		ZioUtil.writeDimension(aStream, dimension);

		aStream.writeString(filePath);
	}

}
