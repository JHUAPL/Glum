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
import java.nio.channels.Channels;
import java.nio.channels.WritableByteChannel;

import glum.zio.ZoutStream;
import glum.zio.stream.BaseZoutStream;

/**
 * Package private class to transform an {@link OutputStream} to a {@link ZoutStream} (view).
 *
 * @author lopeznr1
 */
class WrapOutputStream extends BaseZoutStream
{
	// Stream vars
	private WritableByteChannel refCh;
	private long streamPos;

	/** Standard Constructor */
	protected WrapOutputStream(OutputStream aStream, boolean aComputeCheckSum, boolean aIsDirect) throws IOException
	{
		super(aComputeCheckSum, aIsDirect);

		// Set up the stream vars
		refCh = Channels.newChannel(aStream);
		streamPos = 0;
	}

	/** Simplified Constructor */
	protected WrapOutputStream(OutputStream aStream) throws IOException
	{
		this(aStream, false, false);
	}

	@Override
	public long getPosition() throws IOException
	{
		// There is no virtual position if the stream has been closed
		if (workBuffer == null)
			throw new IOException("Stream has been closed.");

		return streamPos + workBuffer.remaining();
	}

	@Override
	protected void emptyWorkBuffer() throws IOException
	{
		int bytesWritten;

		// Prepare the buffer for a dump of its contents from the start
		workBuffer.flip();

		// Copy the contents of workBuffer to the stream (fileCh)
		bytesWritten = 0;
		while (workBuffer.remaining() > 0)
		{
			bytesWritten = refCh.write(workBuffer);
			if (workBuffer.remaining() > 0)
				System.out.println("Failed to write buffer all at once. bytesToWrite: " + workBuffer.remaining()
						+ ". Bytes formerly written: " + bytesWritten);
		}

		// Clear the workBuffer
		clearWorkBuffer();
	}

	@Override
	protected void releaseStreamVars() throws IOException
	{
		refCh.close();

		refCh = null;
		streamPos = -1;
	}

}
