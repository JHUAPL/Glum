package glum.zio.util;

import glum.zio.ZinStream;

import java.io.IOException;
import java.io.InputStream;

/**
 * Package private class to transform a ZinStream to an InputStream (view).
 */
class WrapZinStream extends InputStream
{
	private ZinStream refStream;

	protected WrapZinStream(ZinStream aStream)
	{
		refStream = aStream;
	}

	@Override
	public void close() throws IOException
	{
		super.close();
		refStream.close();
	}

	@Override
	public int read() throws IOException
	{
		if (refStream.getAvailable() == 0)
			return -1;

		return 0x00FF & refStream.readByte();
	}
	
	@Override
	public int read(byte[] b, int off, int len) throws IOException
	{
		long availBytes; 
		
		availBytes = refStream.getAvailable(); 
		if (availBytes == 0)
			return -1;
		
		if (len > availBytes)
			len = (int)availBytes;

		refStream.readFully(b, off, len);
		return len;
	}
	
	@Override
	public long skip(long len) throws IOException
	{
		long availBytes; 
		
		availBytes = refStream.getAvailable(); 
		if (availBytes == 0)
			return -1;
		
		if (len > availBytes)
			len = availBytes;
		
		refStream.skipBytes((int)len);
		return len;
	}
}