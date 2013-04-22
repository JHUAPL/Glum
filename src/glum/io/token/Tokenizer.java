package glum.io.token;

import java.util.ArrayList;

public interface Tokenizer
{
	/**
	 * Returns all the tokens that match the pattern from the input
	 */
	public ArrayList<String> getTokens(String inputStr);

}
