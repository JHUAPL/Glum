package glum.io.token;

import java.util.*;
import java.util.regex.*;

public class MatchTokenizer extends BaseTokenizer
{
	private Pattern myPattern;
	private boolean autoStripQuotes;

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
	public ArrayList<String> getTokens(String inputStr)
	{
		Matcher aMatcher;
		ArrayList<String> aList;
		String aMatch;

		// Clean up the input string before processing
		inputStr = getCleanString(inputStr);

		aMatcher = myPattern.matcher(inputStr);
		if (aMatcher == null)
			return null;

		aList = new ArrayList<String>();
		while (aMatcher.find() == true)
		{
			aMatch = aMatcher.group();

			// Strip the (double) quotes if requested
			if (autoStripQuotes == true)
				aMatch = TokenUtil.getRawStr(aMatch);

			aList.add(aMatch);
		}

		return aList;
	}

}
