package glum.zio.util;

import glum.zio.ZoutStream;

import java.io.IOException;
import java.io.OutputStream;

/**
 * Package private class to transform a ZoutStream to an OutputStream (view).
 */
class WrapZoutStream extends OutputStream
{
	private ZoutStream refStream;

	protected WrapZoutStream(ZoutStream aStream)
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
	public void write(int b) throws IOException
	{
		refStream.writeByte((byte)(b & 0x00FF));
	}
	
	@Override
	public void write(byte[] b, int off, int len) throws IOException
	{
		refStream.writeFully(b, off, len);
	}

}