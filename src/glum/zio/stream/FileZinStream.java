package glum.zio.stream;

import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileZinStream extends BaseZinStream
{
	// Stream vars
	private FileChannel fileCh;
	private byte[] staleArr;
	
	public FileZinStream(File aFile, boolean computeCheckSum) throws IOException
	{
		super(computeCheckSum, aFile.length());
		
		// Set up the stream vars
		fileCh = new FileInputStream(aFile).getChannel();
		staleArr = new byte[256];
	}
	
	public FileZinStream(File aFile) throws IOException
	{
		this(aFile, false);
	}

	@Override
	public long getAvailable() throws IOException
	{
		if (workBuffer == null)
			return 0;
		
		return fileCh.size() - (fileCh.position() - workBuffer.remaining());
	}
	
	@Override
	public long getPosition() throws IOException
	{
		// There is no virtual position if the stream has been closed
		if (workBuffer == null)
			throw new IOException("Stream has been closed.");
		
		return fileCh.position() - workBuffer.remaining();
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
		numReadBytes = fileCh.read(workBuffer);
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
		fileCh.close();
		
		fileCh = null;
		staleArr = null;
	}	

}
