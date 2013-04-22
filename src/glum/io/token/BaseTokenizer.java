package glum.io.token;

public abstract class BaseTokenizer implements Tokenizer
{
	// Constants
	public static final int MODE_NONE = 0;
	public static final int MODE_ANY_POS = 1;
	public static final int MODE_FIRST_POS = 2;

	// State vars
	protected int commentMode;

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
	protected String getCleanString(String inputStr)
	{
		if (commentMode == MODE_NONE)
		{
			return inputStr;
		}
		else if (commentMode == MODE_FIRST_POS)
		{
			 if (inputStr.indexOf(0) == '#')
				 return "";
		}
		else if (commentMode == MODE_ANY_POS)
		{
			int index;

			index = inputStr.indexOf('#');
			if (index != -1)
				inputStr = inputStr.substring(0, index);
		}
		else
		{
			throw new RuntimeException("Error: commentMode:" + commentMode);
		}
		
		return inputStr;
	}

}
