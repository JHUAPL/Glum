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
 * Task designed to encompass 1 child task and indent all output by the specified number of tabs.
 *
 * @author lopeznr1
 */
public class IndentTask implements Task
{
	// Attributes
	private final Task childTask;
	private final String tabStr;

	// State vars
	private boolean isTrailNL;

	/** Standard Constructor */
	public IndentTask(Task aChildTask, int aNumIndent)
	{
		childTask = aChildTask;

		String tmpStr = "";
		for (int c1 = 0; c1 < aNumIndent; c1++)
			tmpStr += "\t";
		tabStr = tmpStr;

		isTrailNL = true;
	}

	@Override
	public void abort()
	{
		childTask.abort();
	}

	@Override
	public double getProgress()
	{
		return childTask.getProgress();
	}

	@Override
	public boolean isAborted()
	{
		return childTask.isAborted();
	}

	@Override
	public boolean isActive()
	{
		return childTask.isActive();
	}

	@Override
	public void logReg(String aFmtMsg, Object... aObjArr)
	{
		String tmpMsg = String.format(aFmtMsg, aObjArr);

		// Indent the first log message on the following conditions:
		// - The previous output had the last char == '\n' (a trailing NL)
		// - If aMsg does NOT start with a newline
		if (isTrailNL == true && tmpMsg.startsWith("\n") == false)
		{
			isTrailNL = false;
			tmpMsg = tabStr + tmpMsg;
		}

		// Keep track of any trailing newline and strip it from aMsg
		if (tmpMsg.endsWith("\n") == true)
		{
			isTrailNL = true;
			tmpMsg = tmpMsg.substring(0, tmpMsg.length() - 1);
		}

		// Indent any text that is after a newline
		tmpMsg = tmpMsg.replaceAll("\n", "\n" + tabStr);

		// Add back in the trailing newline
		if (isTrailNL == true)
			tmpMsg += "\n";

		childTask.logReg(tmpMsg);
	}

	@Override
	public void logRegln(String aFmtMsg, Object... aObjArr)
	{
		logReg(aFmtMsg + "\n", aObjArr);
	}

	@Override
	public void logRegUpdate(String aFmtMsg, Object... aObjArr)
	{
		aFmtMsg = aFmtMsg.replaceAll("\n", "\n" + tabStr);
		childTask.logRegUpdate(aFmtMsg, aObjArr);
	}

	@Override
	public void reset()
	{
		childTask.reset();
	}

	@Override
	public void setProgress(double aProgress)
	{
		childTask.setProgress(aProgress);
	}

	@Override
	public void setProgress(int aCurrVal, int aMaxVal)
	{
		childTask.setProgress(aCurrVal, aMaxVal);
	}

	@Override
	public void setRefreshRateMs(long aRateMs)
	{
		childTask.setRefreshRateMs(aRateMs);
	}

	@Override
	public void setStatus(String aStatus)
	{
		childTask.setStatus(aStatus);
	}

	@Override
	public void setTabSize(int aNumSpaces)
	{
		childTask.setTabSize(aNumSpaces);
	}

	@Override
	public void setTitle(String aTitle)
	{
		childTask.setTitle(aTitle);
	}

}
