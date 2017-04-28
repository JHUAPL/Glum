package glum.gui.info;

import glum.zio.*;
import glum.zio.util.ZioUtil;

import java.awt.*;
import java.io.*;

public class WindowInfo implements ZioObj
{
	// Raw vars
	protected Point position;
	protected Dimension size;
	protected boolean isVisible;

	/**
	 * Constructor
	 */
	public WindowInfo()
	{
		position = null;
		size = null;
		isVisible = false;
	}

	public WindowInfo(Component aComponent)
	{
		this();

		if (aComponent == null)
			return;

		position = aComponent.getLocation();
		size = aComponent.getSize();
		isVisible = aComponent.isVisible();
	}

	/**
	 * configure - Syncs aComponent with parmaters of this WindowInfo
	 */
	public void configure(Component aComponent)
	{
		if (position != null)
			aComponent.setLocation(position);
		
		if (size != null)
		{
			aComponent.setPreferredSize(size);
			aComponent.setSize(size);
		}
	}

	@Override
	public void zioRead(ZinStream aStream) throws IOException
	{
		aStream.readVersion(0);

		isVisible = aStream.readBool();

		position = ZioUtil.readPoint(aStream);

		size = ZioUtil.readDimension(aStream);
	}

	@Override
	public void zioWrite(ZoutStream aStream) throws IOException
	{
		aStream.writeVersion(0);

		aStream.writeBool(isVisible);

		ZioUtil.writePoint(aStream, position);

		ZioUtil.writeDimension(aStream, size);
	}

}
