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
import java.io.OutputStream;

import glum.zio.ZoutStream;

/**
 * Package private class to transform a {@link ZoutStream} to an {@link OutputStream} (view).
 *
 * @author lopeznr1
 */
class WrapZoutStream extends OutputStream
{
	// Attributes
	private final ZoutStream refStream;

	/** Standard Constructor */
	protected WrapZoutStream(ZoutStream aStream)
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
	public void write(int b) throws IOException
	{
		refStream.writeByte((byte) (b & 0x00FF));
	}

	@Override
	public void write(byte[] b, int off, int len) throws IOException
	{
		refStream.writeFully(b, off, len);
	}

}