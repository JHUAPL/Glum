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

import java.io.*;
import java.nio.channels.FileChannel;

import glum.zio.ZoutStream;

/**
 * Implementation of {@link ZoutStream} backed by a file.
 *
 * @author lopeznr1
 */
public class FileZoutStream extends BaseZoutStream
{
	// Stream vars
	private FileChannel fileCh;

	/** Standard Constructor */
	public FileZoutStream(File aFile, boolean aComputeCheckSum, boolean aIsDirect) throws IOException
	{
		super(aComputeCheckSum, aIsDirect);

		// Set up the stream vars
		fileCh = new FileOutputStream(aFile).getChannel();
	}

	/** Simplified Constructor */
	public FileZoutStream(File aFile) throws IOException
	{
		this(aFile, false, false);
	}

	@Override
	public long getPosition() throws IOException
	{
		// There is no virtual position if the stream has been closed
		if (workBuffer == null)
			throw new IOException("Stream has been closed.");

		return fileCh.position() + workBuffer.position();
	}

	@Override
	protected void emptyWorkBuffer() throws IOException
	{
		// Prepare the buffer for a dump of its contents from the start
		workBuffer.flip();

		// Copy the contents of workBuffer to the stream (fileCh)
		var bytesWritten = 0;
		while (workBuffer.remaining() > 0)
		{
			bytesWritten = fileCh.write(workBuffer);
			if (workBuffer.remaining() > 0)
				System.out.println("Failed to write buffer all at once. bytesToWrite: " + workBuffer.remaining() //
						+ ". Bytes formerly written: " + bytesWritten);
		}

		// Clear the workBuffer
		clearWorkBuffer();
	}

	@Override
	protected void releaseStreamVars() throws IOException
	{
		fileCh.close();
		fileCh = null;
	}

}
