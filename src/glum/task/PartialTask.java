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
 * Task designed to be a partial of a larger {@link Task}. Utilization of this task is focused on the smaller partial
 * task at hand.
 *
 * @author lopeznr1
 */
public class PartialTask implements Task
{
	// Ref vars
	private final Task refTask;

	// State vars
	private double progressOffset;
	private double progressTotalFragment;
	private double internalProgress;

	/** Standard Constructor */
	public PartialTask(Task aRefTask, double aProgressOffset, double aProgressTotalFragment)
	{
		refTask = aRefTask;
		progressOffset = aProgressOffset;
		progressTotalFragment = aProgressTotalFragment;
		internalProgress = 0;
	}

	@Override
	public void abort()
	{
		refTask.abort();
	}

	@Override
	public double getProgress()
	{
		return progressOffset + (progressTotalFragment * internalProgress);
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
		setProgress(0);
	}

	@Override
	public void setProgress(double aProgress)
	{
		internalProgress = aProgress;

		// Update the refTask with the appropriate task value
		refTask.setProgress(this.getProgress());
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
