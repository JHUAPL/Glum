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
 * Singleton instance of {@link Task} that defines the "invalid" {@link Task}.
 * <p>
 * All attempts to change the state of this task will be ignored.
 * <p>
 * All queries to this task will reflect that of an inactive / aborted task.
 *
 * @author lopeznr1
 */
public class InvalidTask implements Task
{
	// Constants
	/** The "invalid" {@link Task}. */
	static final InvalidTask Instance = new InvalidTask();

	/** Private Singleton Constructor */
	private InvalidTask()
	{
		; // Nothing to do
	}

	@Override
	public void abort()
	{
		; // Nothing to do
	}

	@Override
	public double getProgress()
	{
		return Double.NaN;
	}

	@Override
	public boolean isAborted()
	{
		return true;
	}

	@Override
	public boolean isActive()
	{
		return false;
	}

	@Override
	public void logReg(String aFmtMsg, Object... aObjArr)
	{
		; // Nothing to do
	}

	@Override
	public void logRegln(String aFmtMsg, Object... aObjArr)
	{
		; // Nothing to do
	}

	@Override
	public void logRegUpdate(String aFmtMsg, Object... aObjArr)
	{
		; // Nothing to do
	}

	@Override
	public void reset()
	{
		; // Nothing to do
	}

	@Override
	public void setProgress(double aProgress)
	{
		; // Nothing to do
	}

	@Override
	public void setProgress(int aCurrVal, int aMaxVal)
	{
		; // Nothing to do
	}

	@Override
	public void setRefreshRateMs(long aRateMs)
	{
		; // Nothing to do
	}

	@Override
	public void setStatus(String aStatus)
	{
		; // Nothing to do
	}

	@Override
	public void setTabSize(int aNumSpaces)
	{
		; // Nothing to do
	}

	@Override
	public void setTitle(String aTitle)
	{
		; // Nothing to do
	}

}
