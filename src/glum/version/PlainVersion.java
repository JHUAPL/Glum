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
package glum.version;

/**
 * Provides the standard implementation of the {@link Version} interface.
 *
 * @author lopeznr1
 */
public class PlainVersion implements Version
{
	// Constants
	public static PlainVersion AbsMin = new PlainVersion(Integer.MIN_VALUE, Integer.MIN_VALUE, Integer.MIN_VALUE);
	public static PlainVersion AbsMax = new PlainVersion(Integer.MAX_VALUE, Integer.MAX_VALUE, Integer.MAX_VALUE);
	public static PlainVersion Zero = new PlainVersion(0, 0, 0);

	// Attributes
	private final int major;
	private final int minor;
	private final int patch;

	/** Standard Constructor */
	public PlainVersion(int aMajor, int aMinor, int aPatch)
	{
		major = aMajor;
		minor = aMinor;
		patch = aPatch;
	}

	/**
	 * Forms a PlainVersion from the specified string. The version should have have at most 3 integer components
	 * separated by the char: '.'. Any extra components after the first 3 will be ignored.
	 * <p>
	 * If any of the first 3 components are not integers then null will be returned.
	 */
	public static PlainVersion parse(String aStr)
	{
		var tokenArr = aStr.split("\\.");

		int major = 0, minor = 0, patch = 0;
		try
		{
			major = Integer.parseInt(tokenArr[0]);
			if (tokenArr.length >= 2)
				minor = Integer.parseInt(tokenArr[1]);
			if (tokenArr.length >= 3)
				patch = Integer.parseInt(tokenArr[2]);
		}
		catch (NumberFormatException aExp)
		{
			return null;
		}

		return new PlainVersion(major, minor, patch);
	}

	@Override
	public int major()
	{
		return major;
	}

	@Override
	public int minor()
	{
		return minor;
	}

	@Override
	public int patch()
	{
		return patch;
	}

	@Override
	public String toString()
	{
		var retStr = "" + major + "." + minor;
		if (patch != 0)
			retStr += "." + patch;

		return retStr;
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
		PlainVersion other = (PlainVersion) obj;
		if (major != other.major)
			return false;
		if (minor != other.minor)
			return false;
		if (patch != other.patch)
			return false;
		return true;
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + major;
		result = prime * result + minor;
		result = prime * result + patch;
		return result;
	}

}
