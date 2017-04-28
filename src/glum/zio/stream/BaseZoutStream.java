package glum.zio.stream;

import glum.util.WallTimer;
import glum.zio.ZoutStream;
import glum.zio.util.ZioUtil;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;

public abstract class BaseZoutStream implements ZoutStream
{
	// Work vars
	protected ByteBuffer workBuffer;

	// Stat vars
	private WallTimer wallTimer;
	private MessageDigest digest;
	private String checkSumStr;

	/**
	 * @param computeCheckSum
	 *           True if a checksum (md5sum) is desired to be computed as the stream is written
	 * @param isDirect
	 *           True if a direct buffer is desired. This should only be true if the stream is going to a physical I/O component (disk, network) and the size of
	 *           the final stream will be at least ~50 MB.
	 */
	public BaseZoutStream(boolean computeCheckSum, boolean isDirect) throws IOException
	{
		// Allocate the stat vars
		wallTimer = new WallTimer(true);
		digest = null;
		checkSumStr = null;
		try
		{
			if (computeCheckSum == true)
				digest = MessageDigest.getInstance("MD5");
		}
		catch(NoSuchAlgorithmException aExp)
		{
			throw new IOException("Unreconized Algorithm", aExp);
		}

		// Allocate our work vars
		allocateWorkVars(isDirect);
	}

	/**
	 * Returns the length of time (in milliseconds) this stream has been open
	 */
	public long getRunTime()
	{
		return wallTimer.getTotal();
	}

	@Override
	public void close() throws IOException
	{
		// Bail if we have already been closed
		if (workBuffer == null)
			return;

		// Empty any remaining buffered bytes
		emptyWorkBuffer();

		// Force the checksum to be computed
		getCheckSum();

		// Release the work and digest vars
		workBuffer = null;
		digest = null;

		// Release the stream vars
		releaseStreamVars();
	}

	@Override
	public String getCheckSum() throws IOException
	{
		// If the stream is dead then just return the previously computed checksum
		if (workBuffer == null || digest == null)
			return checkSumStr;

		// We are on a live stream, and need to (re)evaluate the digest
		// Ensure there are no unaccounted bytes in the workBuffer
		emptyWorkBuffer();

		// Transform the digest into a checksum
		checkSumStr = new BigInteger(1, digest.digest()).toString(16);
		checkSumStr = Strings.padStart(checkSumStr, 32, '0');
		return checkSumStr;
	}

	@Override
	public void writeByte(byte aByte) throws IOException
	{
		// Ensure there is enough space in workBuffer
		if (workBuffer.remaining() < 1)
			emptyWorkBuffer();

		workBuffer.put(aByte);
	}

	@Override
	public void writeBool(boolean aBool) throws IOException
	{
		if (aBool == false)
			writeByte((byte)0);
		else
			writeByte((byte)1);
	}

	@Override
	public void writeChar(char aChar) throws IOException
	{
		// Ensure there is enough space in workBuffer
		if (workBuffer.remaining() < 2)
			emptyWorkBuffer();

		workBuffer.putChar(aChar);
	}

	@Override
	public void writeInt(int aInt) throws IOException
	{
		// Ensure there is enough space in workBuffer
		if (workBuffer.remaining() < 4)
			emptyWorkBuffer();

		workBuffer.putInt(aInt);
	}

	@Override
	public void writeLong(long aLong) throws IOException
	{
		// Ensure there is enough space in workBuffer
		if (workBuffer.remaining() < 8)
			emptyWorkBuffer();

		workBuffer.putLong(aLong);
	}

	@Override
	public void writeShort(short aShort) throws IOException
	{
		// Ensure there is enough space in workBuffer
		if (workBuffer.remaining() < 2)
			emptyWorkBuffer();

		workBuffer.putShort(aShort);
	}

	@Override
	public void writeFloat(float aFloat) throws IOException
	{
		// Ensure there is enough space in workBuffer
		if (workBuffer.remaining() < 4)
			emptyWorkBuffer();

		workBuffer.putFloat(aFloat);
	}

	@Override
	public void writeDouble(double aDouble) throws IOException
	{
		// Ensure there is enough space in workBuffer
		if (workBuffer.remaining() < 8)
			emptyWorkBuffer();

		workBuffer.putDouble(aDouble);
	}

	@Override
	public void writeString(String aStr) throws IOException
	{
		byte[] data;
		int size;

		// Null strings are handled in special fashion
		if (aStr == null)
		{
			writeShort((short)0x00FFFF);
			return;
		}

		// Empty strings are handled in special fashion
		if (aStr.equals("") == true)
		{
			writeShort((short)0);
			return;
		}

		// Transform the string to it's UTF-8 bytes
		data = aStr.getBytes(Charsets.UTF_8);
		size = data.length;

		// Ensure the string size is less than 0x00FFFF
		if (size >= 0x00FFFF)
			throw new RuntimeException("Transformed UTF-8 string is too large! Max size: " + (0x00FFFF - 1) + "  Curr size:" + size);

		// Write out the string
		writeShort((short)(size & 0x00FFFF));
		writeFully(data);
	}

	@Override
	public void writeRawString(String aStr) throws IOException
	{
		byte[] byteArr;

		byteArr = aStr.getBytes(Charsets.US_ASCII);
		writeFully(byteArr);
	}

	@Override
	public void writeFully(byte[] dstArr, int offset, int length) throws IOException
	{
		int bytesWritten, numToWrite;

		bytesWritten = 0;
		while (bytesWritten != length)
		{
			numToWrite = workBuffer.remaining();
			if (bytesWritten + numToWrite > length)
				numToWrite = length - bytesWritten;

			workBuffer.put(dstArr, offset + bytesWritten, numToWrite);
			bytesWritten += numToWrite;

			// Ensure there is enough space in workBuffer
			if (workBuffer.remaining() < 1 && bytesWritten < length)
				emptyWorkBuffer();
		}
	}

	@Override
	public void writeFully(byte[] dstArr) throws IOException
	{
//		writeFully(dstArr, 0, dstArr.length);
		int bytesWritten, numToWrite;
		int length;

		bytesWritten = 0;
		length = dstArr.length;
		while (bytesWritten != length)
		{
			numToWrite = workBuffer.remaining();
			if (bytesWritten + numToWrite > length)
				numToWrite = length - bytesWritten;

			workBuffer.put(dstArr, bytesWritten, numToWrite);
			bytesWritten += numToWrite;

			// Ensure there is enough space in workBuffer
			if (workBuffer.remaining() < 1 && bytesWritten < length)
				emptyWorkBuffer();
		}
	}

	@Override
	public void writeVersion(int aVersion) throws IOException
	{
		ZioUtil.writeCompactInt(this, aVersion);
	}

	/**
	 * Helper method that ensures the digest has been updated with any buffered data. The buffer will be cleared after the digest has been updated.
	 * <P>
	 * The method shall be called exclusively from {@link BaseZoutStream#emptyWorkBuffer()}.
	 */
	protected void clearWorkBuffer()
	{
		// Update the digest (if requested)
		if (digest != null)
		{
			workBuffer.rewind();
			digest.update(workBuffer);
		}

		// Clear the workBuffer
		workBuffer.clear();
	}

	/**
	 * Helper method to empty the workBuffer and copy the contents to the stream. The contents of the workBuffer will be output to the "stream". This method
	 * ensures that workBuffer will always have enough data to support writing
	 */
	protected abstract void emptyWorkBuffer() throws IOException;

	/**
	 * Helper method to release any stream related vars. This method will only be called once, the very first time the method {@link #close()} is called.
	 */
	protected abstract void releaseStreamVars() throws IOException;

	/**
	 * Helper method to allocate our work vars.
	 * 
	 * @throws IOException
	 */
	private void allocateWorkVars(boolean isDirect) throws IOException
	{
		int workCap;

		// Allocate our byte buffer
		if (isDirect == false)
		{
			// 16K, indirect buffer
			workCap = 16 * 1024;
			workBuffer = ByteBuffer.allocate(workCap);
		}
		else
		{
			// 512K, direct buffer
			workCap = 512 * 1024;
			workBuffer = ByteBuffer.allocateDirect(workCap);
		}
//System.out.println("Is direct buffer: " + workBuffer.isDirect() + " bufferCap: " + workCap);		

		// Mark the buffers as empty
		workBuffer.clear();
	}

}
