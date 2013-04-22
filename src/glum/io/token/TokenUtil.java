package glum.io.token;

public class TokenUtil
{
	/**
	 * Returns the actual string contents sans quotes
	 */
	public static String getRawStr(String aStr)
	{
		// Insanity check
		if (aStr == null)
			return null;

		// Test for the empty string
		if (aStr.length() < 3)
			return aStr;

		// Return the string sans quote
		if (aStr.charAt(0) == '"')
			return aStr.substring(1, aStr.length() - 1);

		// No quote section found
		return aStr;
	}

	/**
	 * Utility method to convert a wild card exression to a regular
	 * expression. Currently only the special chars '?', '*' are supported.
	 * Source: http://www.rgagnon.com/javadetails/java-0515.html
	 */
	public static String convertWildCardToRegEx(String wildcard)
	{
		StringBuffer regex;
		
		regex = new StringBuffer(wildcard.length());
		regex.append('^');
		for (int i = 0, is = wildcard.length(); i < is; i++)
		{
			char c = wildcard.charAt(i);
			switch (c)
			{
				case '*':
				regex.append(".*");
				break;
				
				case '?':
				regex.append(".");
				break;
				
				// escape special regexp-characters
				case '(':
				case ')':
				case '[':
				case ']':
				case '$':
				case '^':
				case '.':
				case '{':
				case '}':
				case '|':
				case '\\':
				regex.append("\\");
				regex.append(c);
				break;
				
				default:
				regex.append(c);
				break;
			}
		}
		
		regex.append('$');
		return (regex.toString());
	}	

}
