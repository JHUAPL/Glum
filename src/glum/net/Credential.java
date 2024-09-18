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

/**
 * Defines credentials needed to access a resource.
 *
 * @author lopeznr1
 */
public class Credential
{
	// Constants
	public static final Credential NONE = new Credential("", new char[0]);

	// State vars
	protected final String domain;
	protected final String username;
	protected final char[] password;

	/**
	 * Standard Constructor
	 * <p>
	 * Creates a domainless credential.
	 *
	 * @param aUsername
	 * @param aPassword
	 *        the password to utilize. Note: a reference to this memory is retained by design to minimize distribution of
	 *        the password content through the application.
	 */
	public Credential(String aUsername, char[] aPassword)
	{
		domain = "";
		username = aUsername;
		password = aPassword;
	}

	public void dispose()
	{
		for (int c1 = 0; c1 < password.length; c1++)
			password[c1] = 0;
	}

	public String getDomain()
	{
		return domain;
	}

	public String getUsername()
	{
		return username;
	}

	/**
	 * Returns the password character array.
	 *
	 * @return a reference to the internal array held by the instance.
	 */
	public char[] getPassword()
	{
		return password;
	}

	public String getPasswordAsString()
	{
		if (password == null)
			return "";

		// TODO This is a big no no. The password should always be stored as a
		// character array and zeroed out before this object is garbage collected.
		return new String(password);
	}

}
