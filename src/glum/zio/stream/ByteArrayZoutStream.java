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

import java.io.IOException;
import java.util.Arrays;

import glum.zio.ZoutStream;

/**
 * Implementation of {@link ZoutStream} backed by a byte array.
 *
 * @author lopeznr1
 */
public class ByteArrayZoutStream extends BaseZoutStream
{
	// Stream vars
	private byte[] dataArr;
	private int dataPos;

	/** Standard Constructor */
	public ByteArrayZoutStream(int initCap, boolean computeCheckSum) throws IOException
	{
		super(computeCheckSum, false);

		// Set up the stream vars
		dataArr = new byte[initCap];
		dataPos = 0;
	}

	/** Simplified Constructor */
	public ByteArrayZoutStream(int initCap) throws IOException
	{
		this(initCap, false);
	}

	/**
	 * Returns a byte array of the contents of this stream.
	 */
	public byte[] toByteArray()
	{
		// This is a live stream, so make sure the buffer has been emptied
		if (workBuffer != null)
		{
			emptyWorkBuffer();
			return Arrays.copyOf(dataArr, dataPos);
		}

		// Return the final byte array
		return dataArr;
	}

	@Override
	public long getPosition() throws IOException
	{
		// There is no virtual position if the stream has been closed
		if (workBuffer == null)
			throw new IOException("Stream has been closed.");

		return dataPos + workBuffer.position();
	}

	@Override
	protected void emptyWorkBuffer()
	{
		byte[] newArr;
		int numBufBytes;

		// Prepare the buffer for a dump of its contents from the start
		workBuffer.flip();

		// Ensure there is enough space in dataArr
		numBufBytes = workBuffer.remaining();
		if (dataArr.length - dataPos < numBufBytes)
		{
			newArr = new byte[(dataArr.length + numBufBytes) * 2];

			System.arraycopy(dataArr, 0, newArr, 0, dataPos);
			dataArr = newArr;
		}

		// Copy the contents of workBuffer to the stream (dataArr)
		workBuffer.get(dataArr, dataPos, numBufBytes);
		dataPos += numBufBytes;

		// Clear the workBuffer
		clearWorkBuffer();
	}

	@Override
	protected void releaseStreamVars() throws IOException
	{
		// Form the final data byte array
		dataArr = Arrays.copyOf(dataArr, dataPos);
		dataPos = -1;
	}

}
