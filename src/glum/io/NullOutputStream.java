// Copyright (C) 2024 The Johns Hopkins University Applied Physics Laboratory LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package glum.io;

import java.io.DataOutputStream;
import java.io.IOException;

/**
 * Implementation of {@link DataOutputStream} useful for counting bytes written.
 *
 * @author lopeznr1
 */
public class NullOutputStream extends DataOutputStream
{
	private int byteCount;

	/** Standard Constructor */
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
