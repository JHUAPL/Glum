package glum.io.token;

/**
 * Collection of utility method to aid with tokenization of input strings.
 *
 * @author lopeznr1
 */
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
	 * Utility method to convert a wild card exression to a regular expression. Currently only the special chars '?', '*'
	 * are supported. Source: http://www.rgagnon.com/javadetails/java-0515.html
	 */
	public static String convertWildCardToRegEx(String aWildcard)
	{
		var regexSB = new StringBuffer(aWildcard.length());
		regexSB.append('^');
		for (int i = 0, is = aWildcard.length(); i < is; i++)
		{
			char c = aWildcard.charAt(i);
			switch (c)
			{
				case '*':
					regexSB.append(".*");
					break;

				case '?':
					regexSB.append(".");
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
					regexSB.append("\\");
					regexSB.append(c);
					break;

				default:
					regexSB.append(c);
					break;
			}
		}

		regexSB.append('$');
		return (regexSB.toString());
	}

}
