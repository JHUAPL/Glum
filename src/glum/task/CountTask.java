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
 * Task that automatically updates the progress based on a count. Once the currCount == fullCount then the Task is
 * considered complete.
 *
 * @author lopeznr1
 */
public class CountTask implements Task
{
	// Ref vars
	private final Task refTask;

	// State vars
	private int fullCount;
	private int currCount;

	/** Standard Constructor */
	public CountTask(Task aRefTask, int aFullCount)
	{
		refTask = aRefTask;
		fullCount = aFullCount;
		currCount = 0;
	}

	/**
	 * Increment the currCount (and corresponding) progress to completion.
	 */
	public void incrementCount()
	{
		currCount++;
		refTask.setProgress(currCount, fullCount);
	}

	@Override
	public void abort()
	{
		refTask.abort();
	}

	@Override
	public double getProgress()
	{
		return (currCount + 0.0) / fullCount;
	}

	@Override
	public boolean isAborted()
	{
		return refTask.isAborted();
	}

	@Override
	public boolean isActive()
	{
		return refTask.isActive();
	}

	@Override
	public void logReg(String aFmtMsg, Object... aObjArr)
	{
		refTask.logReg(aFmtMsg, aObjArr);
	}

	@Override
	public void logRegln(String aFmtMsg, Object... aObjArr)
	{
		refTask.logRegln(aFmtMsg, aObjArr);
	}

	@Override
	public void logRegUpdate(String aFmtMsg, Object... aObjArr)
	{
		refTask.logRegUpdate(aFmtMsg, aObjArr);
	}

	@Override
	public void reset()
	{
		currCount = 0;
		refTask.setProgress(currCount, fullCount);
	}

	@Override
	public void setProgress(double aProgress)
	{
		// The only way to increase the progress is to increment the count.
		; // Nothing to do
	}

	@Override
	public void setProgress(int aCurrVal, int aMaxVal)
	{
		setProgress((aCurrVal + 0.0) / aMaxVal);
	}

	@Override
	public void setRefreshRateMs(long aRateMs)
	{
		refTask.setRefreshRateMs(aRateMs);
	}

	@Override
	public void setStatus(String aStatus)
	{
		refTask.setStatus(aStatus);
	}

	@Override
	public void setTabSize(int aNumSpaces)
	{
		refTask.setTabSize(aNumSpaces);
	}

	@Override
	public void setTitle(String aTitle)
	{
		refTask.setTitle(aTitle);
	}

}
