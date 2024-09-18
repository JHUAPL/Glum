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
package glum.net;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;

import com.google.common.io.ByteStreams;

import glum.task.Task;
import glum.util.ThreadUtil;

/**
 * Collection of utility methods for working with {@link URL}s.
 *
 * @author lopeznr1
 */
public class UrlUtil
{
	/**
	 * utility method that returns a the textual content corresponding to the resource at the specified URL.
	 * <p>
	 * Returns null on failure, and logs the exception to aTask.
	 */
	public static String readText(Task aTask, URL aUrl)
	{
		try (InputStream aStream = aUrl.openStream())
		{
			// Utilize Java 9 Stream read facility
//			return new String(aStream.readAllBytes(), StandardCharsets.UTF_8);
			byte[] tmpByteArr = ByteStreams.toByteArray(aStream);
			return new String(tmpByteArr, StandardCharsets.UTF_8);
		}
		catch (IOException aExp)
		{
			aTask.logRegln(ThreadUtil.getStackTrace(aExp));
		}

		return null;
	}

	/**
	 * Transforms a string representation of url to a {@link URL}.
	 * <p>
	 * On any error a wrapped {@link RuntimeException} will be thrown.
	 */
	public static URL toUrl(String aUrlStr)
	{
		try
		{
			return new URL(aUrlStr);
		}
		catch (Exception aExp)
		{
			throw new RuntimeException(aExp);
		}
	}

	/**
	 * Transforms a {@link URI} to a {@link URL}.
	 * <p>
	 * On any error a wrapped {@link RuntimeException} will be thrown.
	 */
	public static URL toUrl(URI aURI)
	{
		try
		{
			return aURI.toURL();
		}
		catch (Exception aExp)
		{
			throw new RuntimeException(aExp);
		}
	}

	/**
	 * Transforms and resolves a list of strings into a URL.
	 * <p>
	 * The first string should be absolute and reference some top level folder. All intermediate strings will be
	 * interpreted as relative folders. Resolving is done via {@link URI#resolve(String)}
	 * <p>
	 * Minimal error checking is done so it is the responsibility of the caller to ensure input is valid. Wrapped
	 * {@link RuntimeException}s will be thrown.
	 */
	public static URL resolve(String... aStrArr)
	{
		String tmpStr = aStrArr[0];
		if (tmpStr.endsWith("/") == false)
			tmpStr += "/";
		URI tmpURI = URI.create(tmpStr);

		for (int c1 = 0; c1 < aStrArr.length - 1; c1++)
		{
			tmpStr = aStrArr[c1];
			if (tmpStr.endsWith("/") == false)
				tmpStr = tmpStr += '/';

			tmpURI = tmpURI.resolve(tmpStr);
		}

		int lastIdx = aStrArr.length - 1;
		if (lastIdx > 0)
			tmpURI = tmpURI.resolve(aStrArr[lastIdx]);

		// Delegate
		return toUrl(tmpURI);
	}

}
