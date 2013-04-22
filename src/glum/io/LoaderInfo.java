package glum.io;

import glum.zio.ZinStream;
import glum.zio.ZoutStream;
import glum.zio.raw.ZioRaw;
import glum.zio.util.ZioUtil;

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.IOException;

import javax.swing.JFileChooser;

public class LoaderInfo implements ZioRaw
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
		
		// Not utilized as this is the incorrect way to load up the standard JFileChooser 
//		aFileChooser.setVisible(isVisible);
		
		if (filePath != null)
			aFileChooser.setCurrentDirectory(new File(filePath));
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
	

	@Override
	public void zioReadRaw(ZinStream aStream) throws IOException
	{
		aStream.readVersion(0);
		
		isVisible = aStream.readBool();
		
		position = ZioUtil.readPoint(aStream);
		dimension = ZioUtil.readDimension(aStream);
		
		filePath = aStream.readString();
	}

	@Override
	public void zioWriteRaw(ZoutStream aStream) throws IOException
	{
		aStream.writeVersion(0);
		
		aStream.writeBool(isVisible);
		
		ZioUtil.writePoint(aStream, position);
		ZioUtil.writeDimension(aStream, dimension);
		
		aStream.writeString(filePath);
	}

}
