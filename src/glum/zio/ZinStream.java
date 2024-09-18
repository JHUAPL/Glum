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
package glum.zio;

import java.io.IOException;

/**
 * Interface that defines methods used to deserialize data using the glum.zio framework.
 *
 * @author lopeznr1
 */
public interface ZinStream extends AutoCloseable
{
	/**
	 * Releases any resources associated with the ZioInStream
	 */
	@Override
	public void close() throws IOException;

	/**
	 * Returns the number of bytes still available in the input stream
	 */
	public long getAvailable() throws IOException;

	/**
	 * Returns the checksum (as a string) of the stream.
	 * <p>
	 * Note, if the stream is still open, then the returned value will be the checksum evaluated as of the last byte
	 * grabbed from this stream (with no buffering effects - closing the stream immediately will not result in a
	 * different value).
	 */
	public String getCheckSum() throws IOException;

	/**
	 * Returns the virtual position of this stream
	 *
	 * @throws IOException
	 *         If the stream has been closed.
	 */
	public long getPosition() throws IOException;

	/**
	 * Returns the next byte
	 */
	public byte readByte() throws IOException;

	/**
	 * Returns the next byte (cast to a boolean)
	 */
	public boolean readBool() throws IOException;

	/**
	 * Returns the next char
	 */
	public char readChar() throws IOException;

	/**
	 * Returns an enum from the stream.
	 */
	public <G1 extends Enum<?>> G1 readEnum(G1[] aValueArr) throws IOException;

	/**
	 * Returns the next int
	 */
	public int readInt() throws IOException;

	/**
	 * Returns the next long
	 */
	public long readLong() throws IOException;

	/**
	 * Returns the next short
	 */
	public short readShort() throws IOException;

	/**
	 * Returns the next float
	 */
	public float readFloat() throws IOException;

	/**
	 * Returns the next double
	 */
	public double readDouble() throws IOException;

	/**
	 * Reads an 8-bit UTF-8 string from aStream. First 2 bytes specify length of string.
	 */
	public String readString() throws IOException;

	/**
	 * Method to read in a raw string and validate that it matches the passed in absStr. This method is useful to ensure
	 * that file headers and the like are are valid. Note the inverse function is {@link ZoutStream#writeRawString}. The
	 * string will be interpreted as a US-ASCII string.
	 */
	public void readRawStringAndValidate(String absStr) throws IOException;

	/**
	 * Returns the contents of dstArr, starting from offset to length fully.
	 *
	 * @throws IOException
	 *         Will be thrown if not enough data in the stream to fulfill request
	 */
	public void readFully(byte[] dstArr, int offset, int length) throws IOException;

	/**
	 * Reads in the contents of dstArr fully.
	 *
	 * @throws IOException
	 *         Will be thrown if not enough data in the stream to fulfill request
	 */
	public void readFully(byte[] dstArr) throws IOException;

	/**
	 * Method to read the recorded version, and validate that it matches aValidVer. If there is a mismatch, then an
	 * IOException will be thrown. The recorded version should have been written with the inverse method
	 * {@link ZoutStream#writeVersion}.
	 */
	public int readVersion(int aValidVer) throws IOException;

	/**
	 * Method to read the recorded version, and validate that it matches one of the values in validArr. If there is a
	 * mismatch, then an IOException will be thrown. The recorded version should have been written with the inverse
	 * method {@link ZoutStream#writeVersion}.
	 *
	 * @return The version that was read in.
	 */
	public int readVersion(int... validArr) throws IOException;

	/**
	 * Method to read the recorded version. Any value will due. The previously recorded version should have been written
	 * with the inverse method {@link ZoutStream#writeVersion}.
	 *
	 * @return The version that was read in.
	 */
	public int readVersionAny() throws IOException;

	/**
	 * Method to skip numBytes.
	 *
	 * @throws IOException
	 *         Will be thrown if not enough data in the stream to fulfill request
	 */
	public void skipBytes(int numBytes) throws IOException;

}
