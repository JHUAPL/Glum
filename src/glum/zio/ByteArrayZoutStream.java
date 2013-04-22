package glum.zio;

import java.io.IOException;
import java.util.Arrays;

public class ByteArrayZoutStream extends BaseZoutStream
{
	// Stream vars
	private byte[] dataArr;
	private int dataPos;

	public ByteArrayZoutStream(int initCap, boolean computeCheckSum) throws IOException
	{
		super(computeCheckSum, false);
		
		// Set up the stream vars
		dataArr = new byte[initCap];
		dataPos = 0;
	}

	public ByteArrayZoutStream(int initCap) throws IOException
	{
		this(initCap, false);
	}

	/**
	 * Returns a byte array of the contents of this stream.
	 */
	public byte[] toByteArray()
	{
		// This is a live stream, so make sure the buffer has been emptied
		if (workBuffer != null)
		{
			emptyWorkBuffer();
			return Arrays.copyOf(dataArr, dataPos);
		}
		
		// Return the final byte array
		return dataArr;
	}

	@Override
	public long getPosition() throws IOException
	{
		// There is no virtual position if the stream has been closed
		if (workBuffer == null)
			throw new IOException("Stream has been closed.");

		return dataPos + workBuffer.position();
	}

	@Override
	protected void emptyWorkBuffer()
	{
		byte[] newArr;
		int numBufBytes;

		// Prepare the buffer for a dump of its contents from the start
		workBuffer.flip();

		// Ensure there is enough space in dataArr
		numBufBytes = workBuffer.remaining();
		if (dataArr.length - dataPos < numBufBytes)
		{
			newArr = new byte[(dataArr.length + numBufBytes) * 2];

			System.arraycopy(dataArr, 0, newArr, 0, dataPos);
			dataArr = newArr;
		}

		// Copy the contents of workBuffer to the stream (dataArr)
		workBuffer.get(dataArr, dataPos, numBufBytes);
		dataPos += numBufBytes;

		// Clear the workBuffer
		clearWorkBuffer();
	}
	
	@Override
	protected void releaseStreamVars() throws IOException
	{
		// Form the final data byte array
		dataArr = Arrays.copyOf(dataArr, dataPos);
		dataPos = -1;
	}

}
