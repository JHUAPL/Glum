package glum.zio.util;

import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import glum.zio.BaseZinStream;

/**
 * Package private class to transform an InputStream to a ZinStream (view).
 */
class WrapInputStream extends BaseZinStream
{
	// Stream vars
	private ReadableByteChannel refCh;
	private byte[] staleArr;
	private long streamPos;

	protected WrapInputStream(InputStream aStream, boolean computeCheckSum) throws IOException
	{
		super(computeCheckSum, 0);

		// Set up the stream vars
		refCh = Channels.newChannel(aStream);
		staleArr = new byte[256];
		streamPos = 0;
	}

	protected WrapInputStream(InputStream aStream) throws IOException
	{
		this(aStream, false);
	}

	@Override
	public long getAvailable() throws IOException
	{
		throw new IOException("Unsupported operation");
	}

	@Override
	public long getPosition() throws IOException
	{
		// There is no virtual position if the stream has been closed
		if (workBuffer == null)
			throw new IOException("Stream has been closed.");

		return streamPos - workBuffer.remaining();
	}

	@Override
	protected void refreshWorkBuffer() throws IOException
	{
		int numReadBytes, numStaleBytes;

		// Ensure the digest has been updated before refreshing the buffer
		updateDigest();

		// Copies the remaining data from workBuffer to a byte (stale) array
		numStaleBytes = workBuffer.remaining();
		if (numStaleBytes > 0)
			workBuffer.get(staleArr, 0, numStaleBytes);

		// Clear the buffer and copy the (stale) bytes from the byte array to the start of workBuffer
		workBuffer.clear();
		if (numStaleBytes > 0)
			workBuffer.put(staleArr, 0, numStaleBytes);

		// Fill the remaining workBuffer with data from the "stream"
		numReadBytes = refCh.read(workBuffer);
		if (numReadBytes == 0)
			System.out.println("Failed to read any buffer bytes!!! Bytes formerly read: " + numReadBytes);
		if (numReadBytes == -1)
			throw new EOFException("EOF reached on stream.");

		// Mark the buffer as fully prepared and ready for processing
		workBuffer.flip();

		// Mark the current digestPos to the start of the workBuffer
		digestPos = 0;
	}

	@Override
	protected void releaseStreamVars() throws IOException
	{
		refCh.close();

		refCh = null;
		staleArr = null;
		streamPos = -1;
	}

}
