package glum.io;

import java.io.DataOutputStream;
import java.io.IOException;

public class NullOutputStream extends DataOutputStream
{
	protected int byteCount;

	/**
	 * OutputStream used to count bytes that are to be written.
	 */
	public NullOutputStream()
	{
		super(null);

		// Redirect the OutputStream set in the constructor from null to this. This is
		// needed since we can't pass "this" into the default constructor. Low level
		// IO calls are routed into the NullOutputStream and bytes are just counted.
		out = this;

		byteCount = 0;
	}

	/**
	 * Returns the number of bytes that have been counted.
	 */
	public int getNumBytes()
	{
		return byteCount;
	}

	@Override
	public void write(int b) throws IOException
	{
		byteCount++;
	}

	@Override
	public void write(byte[] aArr) throws IOException
	{
		byteCount += aArr.length;
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException
	{
		byteCount += len;
	}

	@Override
	public void flush() throws IOException
	{
		; // Nothing to do
	}

	@Override
	public void close() throws IOException
	{
		throw new RuntimeException("Unsupported operation.");
	}

}
