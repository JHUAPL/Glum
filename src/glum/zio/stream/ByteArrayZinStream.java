package glum.zio.stream;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;

public class ByteArrayZinStream extends BaseZinStream
{
	public ByteArrayZinStream(byte[] aDataArr, boolean computeCheckSum) throws IOException
	{
		super(ByteBuffer.wrap(aDataArr), computeCheckSum);
		
		// Move the position to the start
		workBuffer.rewind();
	}

	public ByteArrayZinStream(byte[] aDataArr) throws IOException
	{
		this(aDataArr, false);
	}

	@Override
	public long getAvailable() throws IOException
	{
		if (workBuffer == null)
			return 0;
		
		return workBuffer.remaining();
	}

	@Override
	public long getPosition() throws IOException
	{
		// There is no virtual position if the stream has been closed
		if (workBuffer == null)
			throw new IOException("Stream has been closed.");
		
		return workBuffer.position();
	}

	@Override
	protected void refreshWorkBuffer() throws IOException
	{
		// There will never be new fresh data for a ByteArrayZinStream
		throw new EOFException("EOF reached on stream.");
	}
	
	@Override
	protected void releaseStreamVars() throws IOException
	{
		; // Nothing to do
	}

}
