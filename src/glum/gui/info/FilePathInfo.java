package glum.gui.info;

import glum.zio.ZinStream;
import glum.zio.ZoutStream;
import glum.zio.raw.ZioRaw;

import java.io.IOException;

public class FilePathInfo implements ZioRaw
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
	public void zioReadRaw(ZinStream aStream) throws IOException
	{
		aStream.readVersion(0);

		filePath = aStream.readString();
	}

	@Override
	public void zioWriteRaw(ZoutStream aStream) throws IOException
	{
		aStream.writeVersion(0);

		aStream.writeString(filePath);
	}

}
