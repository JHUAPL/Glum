package glum.task;

public interface Task
{
	/**
	 * Method to allow the Task to be properly aborted. After this method
	 * call, the method call isActive will return false.
	 */
	public void abort();
	
	/**
	 * Appends aMsg to the info buffer. The text aMsg can not be updated.
	 */
	public void infoAppend(String aMsg);

	/**
	 * Appends aMsg to the info buffer. The text aMsg can not be updated.
	 * A new line will automatically be added to the buffer after aMsg.
	 */
	public void infoAppendln(String aMsg);

	/**
	 * Updates the previous update message with the text aMsg. The text
	 * aMsg can be updated as long as the method infoAppend is not called.
	 */
	public void infoUpdate(String aMsg);

	/**
	 * Returns the percent of progress that has been completed.
	 * 0.0: Not started
	 * 1.0: Complete
	 */
	public double getProgress();
	
	/**
	 * Method to reset a Task to its initial state.
	 */
	public void reset();

	/**
	* Sets in the percent of progress that has been completed.
	* 0.0: Not started
	* 1.0: Complete
	*/
	public void setProgress(double aProgress);
	
	/**
	 * Sets it the progress of a task so that its completion
	 * value is a ratio of currVal to maxVal.
	 */
	public void setProgress(int currVal, int maxVal);

	/**
	 * Sets in the maximum rate the UI will be refreshed at.
	 */
	public void setRefreshRateMs(long aRateMs);

	/**
	 * Method that sets the single line status of the task 
	 */
	public void setStatus(String aStatus);

	/**
	 * Sets in the number of spaces the tab character will be converted to
	 */
	public void setTabSize(int numSpaces);

	/**
	 * Method that sets the title of the task 
	 */
	public void setTitle(String aTitle);

	/**
	* Returns whether this task is still active
	*/
	public boolean isActive();

}
