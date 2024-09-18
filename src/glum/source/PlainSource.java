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
package glum.source;

import java.io.File;
import java.net.URL;

/**
 * Simple (immutable) implementation of {@link Source}.
 * <p>
 * This object supports 2 attributes:
 * <ul>
 * <li>remoteUrl: The remote location where the resource can be retrieved.
 * <li>localFile: The local location where the file can be retrieved (or stored).
 * </ul>
 * Note that either the remoteUrl or localFile can be null but not both. If both are specified then the remoteUrl takes
 * precedence.
 *
 * @author lopeznr1
 */
public class PlainSource implements Source
{
	// Attributes
	private final File localFile;
	private final URL remoteUrl;
	private final long diskSize;

	/**
	 * Standard constructor.
	 * <p>
	 * Either the local or remote resource may be null but not both!
	 */
	public PlainSource(File aLocalFile, URL aRemoteUrl, long aDiskSize)
	{
		localFile = aLocalFile;
		remoteUrl = aRemoteUrl;
		diskSize = aDiskSize;

		// Insanity check
		if (localFile == null && remoteUrl == null)
			throw new NullPointerException("Both aLocalFile and aRemoteUrl may not be null.");
	}

	/**
	 * Simplified Constructor
	 */
	public PlainSource(File aLocalFile, URL aRemoteUrl)
	{
		this(aLocalFile, aRemoteUrl, -1);
	}

	@Override
	public long getDiskSize()
	{
		return diskSize;
	}

	@Override
	public String getName()
	{
		if (remoteUrl != null)
		{
			String tmpPath = remoteUrl.getPath();
			int tmpIdx = tmpPath.lastIndexOf("/");
			return tmpPath.substring(tmpIdx + 1);
		}

		return localFile.getName();
	}

	@Override
	public String getPath()
	{
		if (remoteUrl != null)
			return "" + remoteUrl;

		return localFile.getPath();
	}

	@Override
	public File getLocalFile()
	{
		return localFile;
	}

	@Override
	public URL getRemoteUrl()
	{
		return remoteUrl;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		PlainSource other = (PlainSource) obj;
		if (localFile == null)
		{
			if (other.localFile != null)
				return false;
		}
		else if (!localFile.equals(other.localFile))
			return false;
		if (remoteUrl == null)
		{
			if (other.remoteUrl != null)
				return false;
		}
		else if (!remoteUrl.equals(other.remoteUrl))
			return false;
		return true;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((localFile == null) ? 0 : localFile.hashCode());
		result = prime * result + ((remoteUrl == null) ? 0 : remoteUrl.hashCode());
		return result;
	}

}
