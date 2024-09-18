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
package glum.zio.util;

import java.io.IOException;
import java.io.InputStream;

import glum.zio.ZinStream;

/**
 * Package private class to transform a {@link ZinStream} to an {@link InputStream} (view).
 *
 * @author lopeznr1
 */
class WrapZinStream extends InputStream
{
	// Attributes
	private final ZinStream refStream;

	/** Standard Constructor */
	protected WrapZinStream(ZinStream aStream)
	{
		refStream = aStream;
	}

	@Override
	public void close() throws IOException
	{
		super.close();
		refStream.close();
	}

	@Override
	public int read() throws IOException
	{
		if (refStream.getAvailable() == 0)
			return -1;

		return 0x00FF & refStream.readByte();
	}

	@Override
	public int read(byte[] b, int off, int len) throws IOException
	{
		long availBytes;

		availBytes = refStream.getAvailable();
		if (availBytes == 0)
			return -1;

		if (len > availBytes)
			len = (int) availBytes;

		refStream.readFully(b, off, len);
		return len;
	}

	@Override
	public long skip(long len) throws IOException
	{
		long availBytes;

		availBytes = refStream.getAvailable();
		if (availBytes == 0)
			return -1;

		if (len > availBytes)
			len = availBytes;

		refStream.skipBytes((int) len);
		return len;
	}
}