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

import java.io.*;

import glum.zio.ZinStream;
import glum.zio.ZoutStream;

/**
 * Utility class that takes a {@link ZinStream} / {@link ZoutStream} and presents various {@link InputStream} /
 * {@link OutputStream} views.
 *
 * @author lopeznr1
 */
public class ZioWrapUtil
{
	/**
	 * Utility method to return the {@link ZinStream} as a {@link DataInputStream} view
	 */
	public static DataInputStream asDataInputStream(ZinStream aStream)
	{
		return new DataInputStream(asInputStream(aStream));
	}

	/**
	 * Utility method to return the {@link ZinStream} as a {@link DataInputStream} view
	 */
	public static DataOutputStream asDataOutputStream(ZoutStream aStream)
	{
		return new DataOutputStream(asOutputStream(aStream));
	}

	/**
	 * Utility method to return the {@link ZinStream} as an {@link InputStream} view
	 */
	public static InputStream asInputStream(ZinStream aStream)
	{
		return new WrapZinStream(aStream);
	}

	/**
	 * Utility method to return the {@link ZoutStream} as an {@link OutputStream} view
	 */
	public static OutputStream asOutputStream(ZoutStream aStream)
	{
		return new WrapZoutStream(aStream);
	}

	/**
	 * Utility method to return {@link InputStream} the as a {@link ZinStream} view
	 */
	public static ZinStream asZinStream(InputStream aStream) throws IOException
	{
		return new WrapInputStream(aStream);
	}

	/**
	 * Utility method to return InputStream the as a ZinStream view
	 */
	public static ZoutStream asZoutStream(OutputStream aStream) throws IOException
	{
		return new WrapOutputStream(aStream);
	}

}
