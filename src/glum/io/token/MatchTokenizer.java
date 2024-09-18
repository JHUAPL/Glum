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

import java.util.ArrayList;
import java.util.regex.Pattern;

/**
 * Implementation of {@link BaseTokenizer} that transforms an input string into tokens by matching against a regular
 * expression.
 *
 * @author lopeznr1
 */
public class MatchTokenizer extends BaseTokenizer
{
	// State vars
	private Pattern myPattern;
	private boolean autoStripQuotes;

	/**
	 * Standard Constructor
	 *
	 * @param aRegEx
	 *        The regular expression used to tokenize input strings.
	 */
	public MatchTokenizer(String aRegEx)
	{
		// Compile the pattern
		myPattern = Pattern.compile(aRegEx);

		autoStripQuotes = true;
	}

	/**
	 * Sets the mode of whether double quotes will be automatically stripped
	 */
	public void setAutoStripQuotes(boolean aBool)
	{
		autoStripQuotes = aBool;
	}

	@Override
	public ArrayList<String> getTokens(String aInputStr)
	{
		// Clean up the input string before processing
		aInputStr = getCleanString(aInputStr);

		var tmpMatcher = myPattern.matcher(aInputStr);
		if (tmpMatcher == null)
			return null;

		var retTokenL = new ArrayList<String>();
		while (tmpMatcher.find() == true)
		{
			var tmpMatch = tmpMatcher.group();

			// Strip the (double) quotes if requested
			if (autoStripQuotes == true)
				tmpMatch = TokenUtil.getRawStr(tmpMatch);

			retTokenL.add(tmpMatch);
		}

		return retTokenL;
	}

}
