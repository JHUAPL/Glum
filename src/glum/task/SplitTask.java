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

import com.google.common.collect.ImmutableList;

/**
 * Task designed to encompass many "sub" child tasks and forward actions to each child task.
 * <p>
 * On any query method call such as {@link #getProgress()}, the first child task specified in the constructor will be
 * utilized as the task to fulfill the query.
 *
 * @author lopeznr1
 */
public class SplitTask implements Task
{
	// Attributes
	private final ImmutableList<Task> childTaskL;

	/**
	 * Standard Constructor
	 *
	 * @param aTaskArr
	 *        The list of child tasks for which this SplitTask will automatically forward the method calls to.
	 */
	public SplitTask(Task... aTaskArr)
	{
		childTaskL = ImmutableList.copyOf(aTaskArr);
	}

	@Override
	public void abort()
	{
		for (Task aTask : childTaskL)
			aTask.abort();
	}

	@Override
	public double getProgress()
	{
		return childTaskL.get(0).getProgress();
	}

	@Override
	public boolean isAborted()
	{
		return childTaskL.get(0).isAborted();
	}

	@Override
	public boolean isActive()
	{
		return childTaskL.get(0).isActive();
	}

	@Override
	public void logReg(String aFmtMsg, Object... aObjArr)
	{
		for (Task aTask : childTaskL)
			aTask.logReg(aFmtMsg, aObjArr);
	}

	@Override
	public void logRegln(String aFmtMsg, Object... aObjArr)
	{
		for (Task aTask : childTaskL)
			aTask.logRegln(aFmtMsg, aObjArr);
	}

	@Override
	public void logRegUpdate(String aFmtMsg, Object... aObjArr)
	{
		for (Task aTask : childTaskL)
			aTask.logRegUpdate(aFmtMsg, aObjArr);
	}

	@Override
	public void reset()
	{
		for (Task aTask : childTaskL)
			aTask.reset();
	}

	@Override
	public void setProgress(double aProgress)
	{
		for (Task aTask : childTaskL)
			aTask.setProgress(aProgress);
	}

	@Override
	public void setProgress(int aCurrVal, int aMaxVal)
	{
		for (Task aTask : childTaskL)
			aTask.setProgress(aCurrVal, aMaxVal);
	}

	@Override
	public void setRefreshRateMs(long aRateMs)
	{
		for (Task aTask : childTaskL)
			aTask.setRefreshRateMs(aRateMs);
	}

	@Override
	public void setStatus(String aStatus)
	{
		for (Task aTask : childTaskL)
			aTask.setStatus(aStatus);
	}

	@Override
	public void setTabSize(int aNumSpaces)
	{
		for (Task aTask : childTaskL)
			aTask.setTabSize(aNumSpaces);
	}

	@Override
	public void setTitle(String aTitle)
	{
		for (Task aTask : childTaskL)
			aTask.setTitle(aTitle);
	}

}
