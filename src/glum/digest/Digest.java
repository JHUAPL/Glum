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
package glum.digest;

import java.util.Arrays;

/**
 * Immutable object that stores a single digest.
 *
 * @author lopeznr1
 */
public class Digest
{
	private final DigestType digestType;
	private final byte[] digestValueArr;

	public Digest(DigestType aDigestType, byte[] aDigestValueArr)
	{
		digestType = aDigestType;
		digestValueArr = Arrays.copyOf(aDigestValueArr, aDigestValueArr.length);
	}

	public Digest(DigestType aDigestType, String aHexStr)
	{
		digestType = aDigestType;
		digestValueArr = DigestUtils.hexStr2ByteArr(aHexStr);
	}

	/**
	 * Returns a user friendly description (string) of this digest result.
	 * <p>
	 * The result will be DigestType:hexDigestValue
	 */
	public String getDescr()
	{
		return "" + digestType + ":" + getValueAsString();
	}

	/**
	 * Returns the DigestType associated with this Digest.
	 */
	public DigestType getType()
	{
		return digestType;
	}

	/**
	 * Returns the actual digest (as a string) associated with this Digest.
	 */
	public byte[] getValue()
	{
		return Arrays.copyOf(digestValueArr, digestValueArr.length);
	}

	/**
	 * Returns the actual digest (as a string) associated with this Digest.
	 */
	public String getValueAsString()
	{
		return DigestUtils.byteArr2HexStr(digestValueArr);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + ((digestType == null) ? 0 : digestType.hashCode());
		result = prime * result + Arrays.hashCode(digestValueArr);
		return result;
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
		Digest other = (Digest) obj;
		if (digestType != other.digestType)
			return false;
		if (!Arrays.equals(digestValueArr, other.digestValueArr))
			return false;
		return true;
	}

}
