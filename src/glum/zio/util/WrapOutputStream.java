package glum.zio.util;

import glum.zio.stream.BaseZoutStream;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;

/**
 * Package private class to transform an OutputStream to a ZoutStream (view).
 */
public class WrapOutputStream extends BaseZoutStream
{
	// Stream vars
	private WritableByteChannel refCh;
	private long streamPos;

	protected WrapOutputStream(OutputStream aStream, boolean computeCheckSum, boolean isDirect) throws IOException
	{
		super(computeCheckSum, isDirect);

		// Set up the stream vars
		refCh = Channels.newChannel(aStream);
		streamPos = 0;
	}

	protected WrapOutputStream(OutputStream aStream) throws IOException
	{
		this(aStream, false, false);
	}

	@Override
	public long getPosition() throws IOException
	{
		// There is no virtual position if the stream has been closed
		if (workBuffer == null)
			throw new IOException("Stream has been closed.");

		return streamPos + workBuffer.remaining();
	}

	@Override
	protected void emptyWorkBuffer() throws IOException
	{
		int bytesWritten;

		// Prepare the buffer for a dump of its contents from the start
		workBuffer.flip();

		// Copy the contents of workBuffer to the stream (fileCh)
		bytesWritten = 0;
		while (workBuffer.remaining() > 0)
		{
			bytesWritten = refCh.write(workBuffer);
			if (workBuffer.remaining() > 0)
				System.out.println("Failed to write buffer all at once. bytesToWrite: " + workBuffer.remaining() + ". Bytes formerly written: " + bytesWritten);
		}

		// Clear the workBuffer
		clearWorkBuffer();
	}

	@Override
	protected void releaseStreamVars() throws IOException
	{
		refCh.close();
		
		refCh = null;
		streamPos = -1;
	}

}
