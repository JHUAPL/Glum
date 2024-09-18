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

import glum.zio.ZinStream;

/**
 * Implementation of {@link ZinStream} backed by a file.
 *
 * @author lopeznr1
 */
public class FileZinStream extends BaseZinStream
{
	// Stream vars
	private FileChannel fileCh;
	private byte[] staleArr;

	/** Standard Constructor */
	public FileZinStream(File aFile, boolean aComputeCheckSum) throws IOException
	{
		super(aComputeCheckSum, aFile.length());

		// Set up the stream vars
		fileCh = new FileInputStream(aFile).getChannel();
		staleArr = new byte[256];
	}

	/** Simplified Constructor */
	public FileZinStream(File aFile) throws IOException
	{
		this(aFile, false);
	}

	@Override
	public long getAvailable() throws IOException
	{
		if (workBuffer == null)
			return 0;

		return fileCh.size() - (fileCh.position() - workBuffer.remaining());
	}

	@Override
	public long getPosition() throws IOException
	{
		// There is no virtual position if the stream has been closed
		if (workBuffer == null)
			throw new IOException("Stream has been closed.");

		return fileCh.position() - workBuffer.remaining();
	}

	@Override
	protected void refreshWorkBuffer() throws IOException
	{
		// Ensure the digest has been updated before refreshing the buffer
		updateDigest();

		// Copies the remaining data from workBuffer to a byte (stale) array
		var numStaleBytes = workBuffer.remaining();
		if (numStaleBytes > 0)
			workBuffer.get(staleArr, 0, numStaleBytes);

		// Clear the buffer and copy the (stale) bytes from the byte array to the start of workBuffer
		workBuffer.clear();
		if (numStaleBytes > 0)
			workBuffer.put(staleArr, 0, numStaleBytes);

		// Fill the remaining workBuffer with data from the "stream"
		var numReadBytes = fileCh.read(workBuffer);
		if (numReadBytes == 0)
			System.out.println("Failed to read any buffer bytes!!! Bytes formerly read: " + numReadBytes);
		if (numReadBytes == -1)
			throw new EOFException("EOF reached on stream.");

		// Mark the buffer as fully prepared and ready for processing
		workBuffer.flip();

		// Mark the current digestPos to the start of the workBuffer
		digestPos = 0;
	}

	@Override
	protected void releaseStreamVars() throws IOException
	{
		fileCh.close();

		fileCh = null;
		staleArr = null;
	}

}
