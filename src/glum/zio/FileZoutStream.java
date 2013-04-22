package glum.zio;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class FileZoutStream extends BaseZoutStream
{
	// Stream vars
	private FileChannel fileCh;

	public FileZoutStream(File aFile, boolean computeCheckSum, boolean isDirect) throws IOException
	{
		super(computeCheckSum, isDirect);

		// Set up the stream vars
		fileCh = new FileOutputStream(aFile).getChannel();
	}

	public FileZoutStream(File aFile) throws IOException
	{
		this(aFile, false, false);
	}

	@Override
	public long getPosition() throws IOException
	{
		// There is no virtual position if the stream has been closed
		if (workBuffer == null)
			throw new IOException("Stream has been closed.");

		return fileCh.position() + workBuffer.position();
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
			bytesWritten = fileCh.write(workBuffer);
			if (workBuffer.remaining() > 0)
				System.out.println("Failed to write buffer all at once. bytesToWrite: " + workBuffer.remaining() + ". Bytes formerly written: " + bytesWritten);
		}

		// Clear the workBuffer
		clearWorkBuffer();
	}

	@Override
	protected void releaseStreamVars() throws IOException
	{
		fileCh.close();
		fileCh = null;
	}

}
