package glum.io.token;

import java.util.*;
import java.util.regex.*;

import com.google.common.collect.Lists;

public class SplitTokenizer extends BaseTokenizer
{
	private Pattern myPattern;

	public SplitTokenizer(String aRegEx)
	{
		// Compile the pattern
		myPattern = Pattern.compile(aRegEx);
	}

	@Override
	public ArrayList<String> getTokens(String inputStr)
	{
		String[] tokenArr;
		ArrayList<String> retList;

		// Clean up the input string before processing
		inputStr = getCleanString(inputStr);

		tokenArr = myPattern.split(inputStr, -1);

		retList = Lists.newArrayList(tokenArr);
		return retList;
	}

}
