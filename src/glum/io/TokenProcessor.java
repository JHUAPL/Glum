package glum.io;

public interface TokenProcessor
{
	/**
	 * Lets the processor know that it will not be called anymore. This method is
	 * only called after all data has been read in from the corresponding stream.
	 * Note this method will never get called if the reading was aborted or an IO
	 * exception occured.
	 */
	public void flush();

	/**
	 * Returns true if able to handle the tokens
	 */
	public boolean process(String[] tokens, int lineNum);

}
