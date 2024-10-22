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
 * Interface that defines methods used to serialize data using the glum.zio framework.
 *
 * @author lopeznr1
 */
public interface ZoutStream extends AutoCloseable
{
	/**
	 * Releases any resources associated with the stream
	 */
	@Override
	public void close() throws IOException;

	/**
	 * Returns the checksum (as a string) of the stream.
	 * <p>
	 * Note, if the stream is still open, then the returned value will be the checksum evaluated as of the last byte sent
	 * to this stream (with no buffering effects - closing the stream immediately will not result in a different value).
	 */
	public String getCheckSum() throws IOException;

	/**
	 * Returns the virtual position of this stream.
	 *
	 * @throws IOException
	 *         If the stream has been closed.
	 */
	public long getPosition() throws IOException;

	/**
	 * Outputs the next byte
	 */
	public void writeByte(byte aByte) throws IOException;

	/**
	 * Outputs the next byte (cast from a boolean)
	 */
	public void writeBool(boolean aBool) throws IOException;

	/**
	 * Outputs the next char
	 */
	public void writeChar(char aChar) throws IOException;

	/**
	 * Outputs an enum to the stream.
	 */
	public <G1 extends Enum<?>> void writeEnum(G1 aEnum) throws IOException;

	/**
	 * Outputs the next int
	 */
	public void writeInt(int aInt) throws IOException;

	/**
	 * Outputs the next long
	 */
	public void writeLong(long aLong) throws IOException;

	/**
	 * Outputs the next short
	 */
	public void writeShort(short aShort) throws IOException;

	/**
	 * Outputs the next float
	 */
	public void writeFloat(float aFloat) throws IOException;

	/**
	 * Outputs the next double
	 */
	public void writeDouble(double aDouble) throws IOException;

	/**
	 * Writes an 8-bit UTF string to aStream. First 2 bytes specify length of string.
	 */
	public void writeString(String aStr) throws IOException;

	/**
	 * Utility method to write out a raw string. Note the inverse function is {@link ZinStream#readRawStringAndValidate}.
	 * The string will be interpreted as a US-ASCII string.
	 */
	public void writeRawString(String aStr) throws IOException;

	/**
	 * Writes the contents of dstArr, starting from offset to length fully.
	 *
	 * @throws IOException
	 *         Will be thrown if not enough space in the stream to fulfill request
	 */
	public void writeFully(byte[] dstArr, int offset, int length) throws IOException;

	/**
	 * Writes the contents of dstArr fully.
	 *
	 * @throws IOException
	 *         Will be thrown if not enough space in the stream to fulfill request
	 */
	public void writeFully(byte[] dstArr) throws IOException;

	/**
	 * Method to write the version to the stream. To properly read the version, use the inverse function
	 * {@link ZinStream#readVersion}.
	 */
	public void writeVersion(int aVersion) throws IOException;

}
