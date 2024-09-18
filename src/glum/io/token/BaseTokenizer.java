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
package glum.io.token;

/**
 * Abstract base {@link Tokenizer}.
 *
 * @author lopeznr1
 */
public abstract class BaseTokenizer implements Tokenizer
{
	// Constants
	public static final int MODE_NONE = 0;
	public static final int MODE_ANY_POS = 1;
	public static final int MODE_FIRST_POS = 2;

	// State vars
	protected int commentMode;

	/** Standard Constructor */
	public BaseTokenizer()
	{
		commentMode = MODE_ANY_POS;
	}

	/**
	 * Sets in the comment mode
	 */
	public void setCommentMode(int aMode)
	{
		commentMode = aMode;
	}

	/**
	 * Returns the string with the comments stripped based on the allowable comment style
	 */
	protected String getCleanString(String aInputStr)
	{
		if (commentMode == MODE_NONE)
		{
			return aInputStr;
		}
		else if (commentMode == MODE_FIRST_POS)
		{
			if (aInputStr.indexOf(0) == '#')
				return "";
		}
		else if (commentMode == MODE_ANY_POS)
		{
			int index;

			index = aInputStr.indexOf('#');
			if (index != -1)
				aInputStr = aInputStr.substring(0, index);
		}
		else
		{
			throw new RuntimeException("Error: commentMode:" + commentMode);
		}

		return aInputStr;
	}

}
