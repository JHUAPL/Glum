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
package glum.task;

/**
 * Interface that provides support to monitor a "task". The following features are provided:
 * <ul>
 * <li>Checking the state of the task.
 * <li>Querying + updating of progress.
 * <li>Logging of regular messages associated with the task.
 * <li>Configuration of refresh rates.
 * </ul>
 *
 * @author lopeznr1
 */
public interface Task
{
	// Constants
	/** The "invalid" {@link Task}. */
	public static final Task Invalid = InvalidTask.Instance;

	/**
	 * Method to allow the Task to be properly aborted. After this method call, the method call isActive will return
	 * false.
	 */
	public void abort();

	/**
	 * Returns the percent of progress that has been completed. 0.0: Not started 1.0: Complete
	 */
	public double getProgress();

	/**
	 * Returns whether this task has been aborted.
	 */
	public boolean isAborted();

	/**
	 * Returns whether this task is still active.
	 */
	public boolean isActive();

	/**
	 * Appends a formatted string to the regular buffer. The appended text can not be updated.
	 */
	public void logReg(String aFmtMsg, Object... aObjArr);

	/**
	 * Appends a formatted string to the regular buffer. The appended text can not be updated. A new line will
	 * automatically be added to the buffer after the appended text.
	 */
	public void logRegln(String aFmtMsg, Object... aObjArr);

	/**
	 * Updates the previous regular message with a formatted string. The text can be updated as long as the method
	 * logReg() and logRegln() are not called.
	 */
	public void logRegUpdate(String aFmtMsg, Object... aObjArr);

	/**
	 * Method to reset a Task to its initial state.
	 */
	public void reset();

	/**
	 * Sets in the percent of progress that has been completed. 0.0: Not started 1.0: Complete
	 */
	public void setProgress(double aProgress);

	/**
	 * Sets it the progress of a task so that its completion value is a ratio of aCurrVal to aMaxVal.
	 */
	public void setProgress(int aCurrVal, int aMaxVal);

	/**
	 * Sets in the maximum rate (in milliseconds) the UI will be refreshed at.
	 */
	public void setRefreshRateMs(long aRateMs);

	/**
	 * Method that sets the single line status of the task
	 */
	public void setStatus(String aStatus);

	/**
	 * Sets in the number of spaces the tab character will be converted to
	 */
	public void setTabSize(int aNumSpaces);

	/**
	 * Method that sets the title of the task
	 */
	public void setTitle(String aTitle);

}
