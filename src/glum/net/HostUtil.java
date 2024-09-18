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

import java.io.InputStream;
import java.io.InputStreamReader;

import com.google.common.base.Charsets;
import com.google.common.io.CharStreams;

/**
 * Collection of utility methods to support determining the system's host name.
 *
 * @author lopeznr1
 */
public class HostUtil
{
	// Constants
	/**
	 * Regex for host name. Source:
	 * https://stackoverflow.com/questions/106179/regular-expression-to-match-dns-hostname-or-ip-address
	 */
	private static final String ValidHostnameRegex = "^(([a-zA-Z0-9]|[a-zA-Z0-9][a-zA-Z0-9\\-]*[a-zA-Z0-9])\\.)*([A-Za-z0-9]|[A-Za-z0-9][A-Za-z0-9\\-]*[A-Za-z0-9])$";

	// Cache vars (static)
	private static String cHostName = null;

	/**
	 * Returns the system host name as specified via the following:
	 * <ul>
	 * <li>First try the relevant system environment variable.
	 * <li>Query the system via the hostname command
	 * </ul>
	 * Returns null on failure.
	 * <p>
	 * Source: https://stackoverflow.com/questions/7348711/recommended-way-to-get-hostname-in-java
	 *
	 * @param aAllowCache
	 *        If set to true, then the cached version will be utilized (if available) otherwise a system call may be
	 *        required.
	 */
	public static String getHostName(boolean aAllowCache)
	{
		String hostName = cHostName;
		if (aAllowCache == true && hostName != null)
			return hostName;

		String osName = System.getProperty("os.name").toLowerCase();
		if (osName.contains("win"))
		{
			hostName = System.getenv("COMPUTERNAME");
			if (hostName == null)
				hostName = execReadToString("hostname");
		}
		else if (osName.contains("nix") || osName.contains("nux") || osName.contains("mac os x"))
		{
			hostName = System.getenv("HOSTNAME");
			if (hostName == null)
				hostName = execReadToString("hostname");
			if (hostName == null)
				hostName = execReadToString("cat /etc/hostname");
		}

		// Remove extra whitespace
		if (hostName != null)
			hostName = hostName.trim();

		// Transform empty string to null
		if (hostName == null || hostName.isEmpty() == true)
			hostName = null;

		// Update the cache
		cHostName = hostName;

		return hostName;
	}

	/**
	 * Returns the system host name as specified via the following:
	 * <ul>
	 * <li>First try the relevant system environment variable.
	 * <li>Query the system via the hostname command
	 * </ul>
	 * Returns null on failure.
	 * <p>
	 * Note that this method will return cached values.
	 */
	public static String getHostName()
	{
		// Delegate
		return getHostName(true);
	}

	/**
	 * Utility method that returns true if the specified host name is valid.
	 */
	public static boolean isValidHostName(String aName)
	{
		if (aName == null)
			return false;

		return aName.matches(ValidHostnameRegex);
	}

	/**
	 * Return the string corresponding to the executed command.
	 * <p>
	 * On success returns the corresponding string with whitespace stripped.
	 * <p>
	 * Any failure will result in null.
	 */
	private static String execReadToString(String execCommand)
	{
		try
		{
			Process tmpProcess = Runtime.getRuntime().exec(execCommand);
			try (InputStream aStream = tmpProcess.getInputStream())
			{
				String result = CharStreams.toString(new InputStreamReader(aStream, Charsets.UTF_8));
				return result.trim();
			}
			catch (Exception aExp)
			{
				return null;
			}
		}
		catch (Exception aExp)
		{
			return null;
		}
	}

}
