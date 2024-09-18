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
package glum.zio.stream;

import java.io.EOFException;
import java.io.IOException;
import java.nio.ByteBuffer;

import glum.zio.ZinStream;

/**
 * Implementation of {@link ZinStream} backed by a byte array.
 *
 * @author lopeznr1
 */
public class ByteArrayZinStream extends BaseZinStream
{
	/** Standard Constructor */
	public ByteArrayZinStream(byte[] aDataArr, boolean computeCheckSum) throws IOException
	{
		super(ByteBuffer.wrap(aDataArr), computeCheckSum);

		// Move the position to the start
		workBuffer.rewind();
	}

	/** Simplified Constructor */
	public ByteArrayZinStream(byte[] aDataArr) throws IOException
	{
		this(aDataArr, false);
	}

	@Override
	public long getAvailable() throws IOException
	{
		if (workBuffer == null)
			return 0;

		return workBuffer.remaining();
	}

	@Override
	public long getPosition() throws IOException
	{
		// There is no virtual position if the stream has been closed
		if (workBuffer == null)
			throw new IOException("Stream has been closed.");

		return workBuffer.position();
	}

	@Override
	protected void refreshWorkBuffer() throws IOException
	{
		// There will never be new fresh data for a ByteArrayZinStream
		throw new EOFException("EOF reached on stream.");
	}

	@Override
	protected void releaseStreamVars() throws IOException
	{
		; // Nothing to do
	}

}
