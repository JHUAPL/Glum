package glum.zio;

import glum.util.WallTimer;
import glum.zio.raw.ZioRaw;
import glum.zio.util.ZioUtil;

import java.io.IOException;
import java.math.BigInteger;
import java.nio.ByteBuffer;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import com.google.common.base.Charsets;
import com.google.common.base.Strings;

public abstract class BaseZinStream implements ZinStream
{
	// Work vars
	protected ByteBuffer workBuffer;

	// Stat vars
	private WallTimer wallTimer;
	private MessageDigest digest;
	private String checkSumStr;
	protected int digestPos;

	/**
	 * @param computeCheckSum
	 *           True if a checksum (md5sum) is desired to be computed as the stream is written
	 * @param streamSizeHint
	 *           A hint which indicates the final size of the source stream. This hint will be used to determine if a
	 *           direct buffer should be allocated. If the hint size is greater than 25 MB then a direct buffer will be
	 *           allocated. A value of 0 implies that a direct buffer should not be allocated.
	 */
	public BaseZinStream(boolean computeCheckSum, long streamSizeHint) throws IOException
	{
		// Allocate the stat vars
		wallTimer = new WallTimer(true);
		digest = null;
		checkSumStr = null;
		digestPos = -1;
		if (computeCheckSum == true)
		{
			try
			{
				digest = MessageDigest.getInstance("MD5");
				digestPos = 0;
			}
			catch (NoSuchAlgorithmException aExp)
			{
				throw new IOException("Unreconized Algorithm", aExp);
			}
		}

		// Allocate our work vars
		allocateWorkVars(streamSizeHint);
	}

	/**
	 * @param aWorkBuffer
	 *           This ByteBuffer will be used as the workBuffer. No workBuffer will be allocated for this BaseZinStream.
	 * @param computeCheckSum
	 *           True if a checksum (md5sum) is desired to be computed as the stream is written
	 */
	public BaseZinStream(ByteBuffer aWorkBuffer, boolean computeCheckSum) throws IOException
	{
		// Allocate the checksum digest worker
		digest = null;
		checkSumStr = null;
		if (computeCheckSum == true)
		{
			try
			{
				digest = MessageDigest.getInstance("MD5");
				digestPos = 0;
			}
			catch (NoSuchAlgorithmException aExp)
			{
				throw new IOException("Unreconized Algorithm", aExp);
			}
		}

		// Allocate our work vars
		workBuffer = aWorkBuffer;
		if (workBuffer == null)
			throw new NullPointerException();
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

		// Stop the timer
		wallTimer.stop();
		
		// Force the checksum to be computed
		getCheckSum();

		// Release the work and digest vars
		workBuffer = null;
		digest = null;
		digestPos = -1;

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
		updateDigest();

		// Transform the digest into a checksum
		checkSumStr = new BigInteger(1, digest.digest()).toString(16);
		checkSumStr = Strings.padStart(checkSumStr, 32, '0');
		return checkSumStr;
	}

	@Override
	public byte readByte() throws IOException
	{
		// Ensure there is enough data in workBuffer
		if (workBuffer.remaining() < 1)
			refreshWorkBuffer();

		return workBuffer.get();
	}

	@Override
	public boolean readBool() throws IOException
	{
		return readByte() != 0;
	}

	@Override
	public char readChar() throws IOException
	{
		// Ensure there is enough data in workBuffer
		if (workBuffer.remaining() < 2)
			refreshWorkBuffer();

		return workBuffer.getChar();
	}

	@Override
	public int readInt() throws IOException
	{
		// Ensure there is enough data in workBuffer
		if (workBuffer.remaining() < 4)
			refreshWorkBuffer();

		return workBuffer.getInt();
	}

	@Override
	public long readLong() throws IOException
	{
		// Ensure there is enough data in workBuffer
		if (workBuffer.remaining() < 8)
			refreshWorkBuffer();

		return workBuffer.getLong();
	}

	@Override
	public short readShort() throws IOException
	{
		// Ensure there is enough data in workBuffer
		if (workBuffer.remaining() < 2)
			refreshWorkBuffer();

		return workBuffer.getShort();
	}

	@Override
	public float readFloat() throws IOException
	{
		// Ensure there is enough data in workBuffer
		if (workBuffer.remaining() < 4)
			refreshWorkBuffer();

		return workBuffer.getFloat();
	}

	@Override
	public double readDouble() throws IOException
	{
		// Ensure there is enough data in workBuffer
		if (workBuffer.remaining() < 8)
			refreshWorkBuffer();

		return workBuffer.getDouble();
	}

	@Override
	public String readString() throws IOException
	{
		byte[] data;
		int size;

		size = readShort() & 0x00FFFF;
		if (size == 0x00FFFF)
			return null;
		if (size == 0)
			return "";

		data = new byte[size];
		readFully(data);
		return new String(data, Charsets.UTF_8);
	}

	@Override
	public void readRawStringAndValidate(String absStr) throws IOException
	{
		byte[] absByteArr, readByteArr;

		// Transform the passed in string to US-ASCII
		absByteArr = absStr.getBytes(Charsets.US_ASCII);

		// Grab the next set of bytes from the stream equal to length of absByteArr
		readByteArr = new byte[absStr.length()];
		readFully(readByteArr);

		// Ensure the two arrays are equal
		if (Arrays.equals(absByteArr, readByteArr) == false)
			throw new IOException("Mismatched string. Needed:" + absStr + "  Found:" + new String(readByteArr, Charsets.US_ASCII));
	}

	@Override
	public void readFully(byte[] dstArr, int offset, int length) throws IOException
	{
		int bytesRead, numToRead;

		bytesRead = 0;
		while (bytesRead != length)
		{
			numToRead = workBuffer.remaining();
			if (bytesRead + numToRead > length)
				numToRead = length - bytesRead;

			workBuffer.get(dstArr, offset + bytesRead, numToRead);
			bytesRead += numToRead;

			// Ensure there is enough data in workBuffer
			if (workBuffer.remaining() < 1 && bytesRead < length)
				refreshWorkBuffer();
		}
	}

	@Override
	public void readFully(byte[] dstArr) throws IOException
	{
		int bytesRead, numToRead;
		int length;

		bytesRead = 0;
		length = dstArr.length;
		while (bytesRead != length)
		{
			numToRead = workBuffer.remaining();
			if (bytesRead + numToRead > length)
				numToRead = length - bytesRead;

			workBuffer.get(dstArr, bytesRead, numToRead);
			bytesRead += numToRead;

			// Ensure there is enough data in workBuffer
			if (workBuffer.remaining() < 1 && bytesRead < length)
				refreshWorkBuffer();
		}
	}

	@Override
	public int readVersion(int aValidVer) throws IOException
	{
		int readVersion;

		// Read the version
		readVersion = readByte() & 0x00FF;
		if (readVersion == 255)
			readVersion = readInt();

		// Ensure the version is one of the valid versions
		if (readVersion == aValidVer)
			return readVersion;

		// Failure, let the user know of the version that was read vs what is valid
		throw new IOException("Unreconized version... Read: " + readVersion + " Expected: " + aValidVer);
	}

	@Override
	public int readVersion(int... validArr) throws IOException
	{
		int readVersion;

		// Read the version
		readVersion = ZioUtil.readCompactInt(this);

		// Ensure the version is one of the valid versions
		for (int aVersion : validArr)
		{
			if (aVersion == readVersion)
				return aVersion;
		}

		// Failure, let the user know of the version that was read vs what is valid
		if (validArr.length == 1)
			throw new IOException("Unreconized version... Read: " + readVersion + " Expected: " + validArr[0]);

		throw new IOException("Unreconized version... Read: " + readVersion + " Expected one of the following: " + Arrays.toString(validArr));
	}

	@Override
	public void readZioRaw(ZioRaw aZioRaw) throws IOException
	{
		aZioRaw.zioReadRaw(this);
	}

	@Override
	public void skipBytes(int numBytes) throws IOException
	{
		int bytesSkipped, numToSkip;

		bytesSkipped = 0;
		while (bytesSkipped != numBytes)
		{
			numToSkip = workBuffer.remaining();
			if (bytesSkipped + numToSkip > numBytes)
				numToSkip = numBytes - bytesSkipped;

			workBuffer.position(workBuffer.position() + numToSkip);
			bytesSkipped += numToSkip;

			// Ensure there is enough data in workBuffer (to skip)
			if (workBuffer.remaining() < 1 && bytesSkipped < numBytes)
				refreshWorkBuffer();
		}
	}

	/**
	 * Helper method to refresh the workBuffer with new data from the stream. This method ensures that workBuffer will
	 * always have enough data to support reading.
	 * <P>
	 * If there is no more data on the stream then this method should throw an IOException
	 */
	protected abstract void refreshWorkBuffer() throws IOException;

	/**
	 * Helper method to release any stream related vars. This method will only be called once, the very first time the
	 * method {@link #close()} is called.
	 */
	protected abstract void releaseStreamVars() throws IOException;

	/**
	 * Helper method that ensures the digest has been updated with any data that has been "read" thus far. The definition
	 * of "read" is any data returned from the stream via one of the read methods. The digest shall not be updated with
	 * any buffered data - only data that has been read from the stream.
	 */
	protected void updateDigest() throws IOException
	{
		ByteBuffer tmpBuffer;

		// Bail if the there is no digest
		if (digest == null)
			return;

		// Retrieve a duplicate of the workBuffer (to preserve its configuration)
		tmpBuffer = workBuffer.duplicate();

		// Evaluate the digest from the digestPos to the limit (workBuffer's current position)
		tmpBuffer.flip();
		tmpBuffer.position(digestPos);
		digest.update(tmpBuffer);

		// Update the digest position
		digestPos = tmpBuffer.limit();
	}

	/**
	 * Helper method to allocate our work vars.
	 */
	private void allocateWorkVars(long streamSizeHint) throws IOException
	{
		int workCap;
		boolean isDirect;

		// Determine if we should use a direct buffer for our workBuffer (stream > 25 MB)
		isDirect = false;
		if (streamSizeHint > 25 * 1024 * 1024)
			isDirect = true;

		// Allocate our byte buffer
		if (isDirect == false)
		{
			// [1K, 16K], indirect buffer
			workCap = (int)streamSizeHint;
			if (workCap < 1024)
				workCap = 1024;
			else if (workCap > 16 * 1024)
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

		// Mark the contents in workBuffer as completely empty
		workBuffer.limit(0);
	}

}
