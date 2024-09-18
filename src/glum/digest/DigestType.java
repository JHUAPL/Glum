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

/**
 * Enum that defines the supported digest types.
 *
 * @author lopeznr1
 */
public enum DigestType
{
	// Weak digest - but very fast
	MD5("MD5"),

	// Fairly strong digest type (with good performance on 32 bit machines)
	SHA256("SHA-256"),

	// Very strong digest type
	SHA512("SHA-512");

	// State vars
	private String algName;

	private DigestType(String aAlgName)
	{
		algName = aAlgName;
	}

	/**
	 * Returns the official digest algorithm name.
	 *
	 * @see http://docs.oracle.com/javase/1.5.0/docs/guide/security/CryptoSpec.html#AppA
	 */
	public String getAlgName()
	{
		return algName;
	}

	/**
	 * Returns the corresponding DigestType.
	 */
	public static DigestType parse(String aStr)
	{
		if (aStr.equalsIgnoreCase("MD5") == true)
			return MD5;
		if (aStr.equalsIgnoreCase("SHA256") == true)
			return SHA256;
		if (aStr.equalsIgnoreCase("SHA512") == true)
			return SHA512;

		return null;
	}
}