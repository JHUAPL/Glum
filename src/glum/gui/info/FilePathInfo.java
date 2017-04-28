package glum.gui.info;

import glum.zio.*;

import java.io.IOException;

public class FilePathInfo implements ZioObj
{
	// State vars
	protected String filePath;

	public FilePathInfo()
	{
		filePath = null;
	}

	public FilePathInfo(String aFilePath)
	{
		filePath = aFilePath;
	}

	@Override
	public void zioRead(ZinStream aStream) throws IOException
	{
		aStream.readVersion(0);

		filePath = aStream.readString();
	}

	@Override
	public void zioWrite(ZoutStream aStream) throws IOException
	{
		aStream.writeVersion(0);

		aStream.writeString(filePath);
	}

}
