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
 * Local (immutable) implementation of {@link Source}.
 * <p>
 * This object supports only 1 attributes:
 * <ul>
 * <li>localFile: The local location where the file can be retrieved (or stored).
 * </ul>
 * Note that localFile can not be null.
 *
 * @author lopeznr1
 */
public class LocalSource implements Source
{
	// Attributes
	private final File localFile;
	private final boolean isAuthoritative;

	/** Standard constructor. */
	public LocalSource(File aLocalFile, boolean aIsAuthoritative)
	{
		localFile = aLocalFile;
		isAuthoritative = aIsAuthoritative;

		// Insanity check
		if (localFile == null)
			throw new NullPointerException("Parameter aLocalFile must not be null.");
	}

	/** Simplified constructor. */
	public LocalSource(File aLocalFile)
	{
		this(aLocalFile, true);
	}

	@Override
	public long getDiskSize()
	{
		// If flagged as authoritative then trust the (local) file system and use
		// the use the size stored there otherwise no way to know if the value is
		// authoritative.
		if (isAuthoritative == true)
			return localFile.length();

		return -1;
	}

	@Override
	public String getName()
	{
		return localFile.getName();
	}

	@Override
	public String getPath()
	{
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
		return null;
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
		LocalSource other = (LocalSource) obj;
		if (localFile == null)
		{
			if (other.localFile != null)
				return false;
		}
		else if (!localFile.equals(other.localFile))
			return false;
		return true;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((localFile == null) ? 0 : localFile.hashCode());
		return result;
	}

}
