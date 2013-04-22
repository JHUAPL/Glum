package glum.zio;

import glum.zio.raw.ZioRaw;

import java.io.IOException;

import com.google.common.base.Charsets;

/**
 * ZoutStream used to count bytes that would be written.
 */
public class NullZoutStream implements ZoutStream
{
	private int byteCount;
	
	public NullZoutStream()
	{
		byteCount = 0;
	}
	
	/**
	 * Returns the number of bytes that have been written
	 */
	public int getNumBytes()
	{
		return byteCount;
	}

	@Override
	public void close() throws IOException
	{
		throw new IOException("Unsupported operation.");
	}

	@Override
	public String getCheckSum() throws IOException
	{
		throw new IOException("Unsupported operation.");
	}

	@Override
	public long getPosition() throws IOException
	{
		return byteCount;
	}

	@Override
	public void writeByte(byte aByte) throws IOException
	{
		byteCount++;
	}

	@Override
	public void writeBool(boolean aBool) throws IOException
	{
		byteCount++;
	}

	@Override
	public void writeChar(char aChar) throws IOException
	{
		byteCount += 2;
	}

	@Override
	public void writeInt(int aInt) throws IOException
	{
		byteCount += 4;
	}

	@Override
	public void writeLong(long aLong) throws IOException
	{
		byteCount += 8;
	}

	@Override
	public void writeShort(short aShort) throws IOException
	{
		byteCount += 2;
	}

	@Override
	public void writeFloat(float aFloat) throws IOException
	{
		byteCount += 4;
	}

	@Override
	public void writeDouble(double aDouble) throws IOException
	{
		byteCount += 8;
	}

	@Override
	public void writeString(String aStr) throws IOException
	{
		byte[] data;
		int size;
		
		// Null strings are handled in special fashion
		if (aStr == null)
		{
			byteCount += 2;
			return;
		}
		
		// Empty strings are handled in special fashion
		if (aStr.equals("") == true)
		{
			byteCount += 2;
			return;
		}
		
		// Transform the string to it's UTF-8 bytes
		data = aStr.getBytes(Charsets.UTF_8);
		size = data.length;
		
		// Ensure the string size is less than 0x00FFFF
		if (size >= 0x00FFFF)
			throw new RuntimeException("Transformed UTF-8 string is too large! Max size: " + (0x00FFFF - 1) + "  Curr size:" + size);
		
		byteCount += 2 + size;
	}

	@Override
	public void writeRawString(String aStr) throws IOException
	{
		byteCount += aStr.length();
	}

	@Override
	public void writeFully(byte[] dstArr, int offset, int length) throws IOException
	{
		byteCount += length;
	}

	@Override
	public void writeFully(byte[] dstArr) throws IOException
	{
		byteCount += dstArr.length;
	}

	@Override
	public void writeVersion(int aVersion) throws IOException
	{
		if (aVersion < 255)
			byteCount++;
		else
			byteCount += 5;
	}

	@Override
	public void writeZioRaw(ZioRaw aZioRaw) throws IOException
	{
		aZioRaw.zioWriteRaw(this);
	}

}
