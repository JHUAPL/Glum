package glum.zio.stream;

import glum.zio.ZoutStream;

import java.io.IOException;

import com.google.common.base.Charsets;

/**
 * ZoutStream used for debugging. This is useful when trying to determine when and where a specific byte is being written out.
 * <P>
 * When the specified nth byte has been written this object will throw an IOException.
 */
public class DebugZoutStream implements ZoutStream
{
	private int byteCnt;
	private int failByteCnt;

	/**
	 * @param aFailByteCnt
	 *           The nth byte that when written will cause an Exception to be raised.
	 */
	public DebugZoutStream(int aFailByteCnt)
	{
		byteCnt = 0;
		failByteCnt = aFailByteCnt;
	}

	/**
	 * Returns the number of bytes that have been written
	 */
	public int getNumBytes()
	{
		return byteCnt;
	}

	@Override
	public void close() throws IOException
	{
		// Nothing to do
	}

	@Override
	public String getCheckSum() throws IOException
	{
		throw new IOException("Unsupported operation.");
	}

	@Override
	public long getPosition() throws IOException
	{
		return byteCnt;
	}

	@Override
	public void writeByte(byte aByte) throws IOException
	{
		byteCnt++;
		if (byteCnt >= failByteCnt)
			throwBadByteWrittenException();
	}

	@Override
	public void writeBool(boolean aBool) throws IOException
	{
		byteCnt++;
		if (byteCnt >= failByteCnt)
			throwBadByteWrittenException();
	}

	@Override
	public void writeChar(char aChar) throws IOException
	{
		byteCnt += 2;
		if (byteCnt >= failByteCnt)
			throwBadByteWrittenException();
	}

	@Override
	public void writeInt(int aInt) throws IOException
	{
		byteCnt += 4;
		if (byteCnt >= failByteCnt)
			throwBadByteWrittenException();
	}

	@Override
	public void writeLong(long aLong) throws IOException
	{
		byteCnt += 8;
		if (byteCnt >= failByteCnt)
			throwBadByteWrittenException();
	}

	@Override
	public void writeShort(short aShort) throws IOException
	{
		byteCnt += 2;
		if (byteCnt >= failByteCnt)
			throwBadByteWrittenException();
	}

	@Override
	public void writeFloat(float aFloat) throws IOException
	{
		byteCnt += 4;
		if (byteCnt >= failByteCnt)
			throwBadByteWrittenException();
	}

	@Override
	public void writeDouble(double aDouble) throws IOException
	{
		byteCnt += 8;
		if (byteCnt >= failByteCnt)
			throwBadByteWrittenException();
	}

	@Override
	public void writeString(String aStr) throws IOException
	{
		byte[] data;
		int size;

		// Null strings are handled in special fashion
		if (aStr == null)
		{
			byteCnt += 2;
			if (byteCnt >= failByteCnt)
				throwBadByteWrittenException();
			return;
		}

		// Empty strings are handled in special fashion
		if (aStr.equals("") == true)
		{
			byteCnt += 2;
			if (byteCnt >= failByteCnt)
				throwBadByteWrittenException();
			return;
		}

		// Transform the string to it's UTF-8 bytes
		data = aStr.getBytes(Charsets.UTF_8);
		size = data.length;

		// Ensure the string size is less than 0x00FFFF
		if (size >= 0x00FFFF)
			throw new RuntimeException("Transformed UTF-8 string is too large! Max size: " + (0x00FFFF - 1) + "  Curr size:" + size);

		byteCnt += 2 + size;
		if (byteCnt >= failByteCnt)
			throwBadByteWrittenException();
	}

	@Override
	public void writeRawString(String aStr) throws IOException
	{
		byteCnt += aStr.length();
		if (byteCnt >= failByteCnt)
			throwBadByteWrittenException();
	}

	@Override
	public void writeFully(byte[] dstArr, int offset, int length) throws IOException
	{
		byteCnt += length;
		if (byteCnt >= failByteCnt)
			throwBadByteWrittenException();
	}

	@Override
	public void writeFully(byte[] dstArr) throws IOException
	{
		byteCnt += dstArr.length;
		if (byteCnt >= failByteCnt)
			throwBadByteWrittenException();
	}

	@Override
	public void writeVersion(int aVersion) throws IOException
	{
		if (aVersion < 255)
			byteCnt++;
		else
			byteCnt += 5;

		if (byteCnt >= failByteCnt)
			throwBadByteWrittenException();
	}

	/**
	 * Helper method that throws the actual exception.
	 */
	public void throwBadByteWrittenException() throws IOException
	{
		throw new IOException("Bad byte has been written.");
	}

}
