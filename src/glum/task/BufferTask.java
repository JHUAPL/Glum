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

import com.google.common.base.Strings;

/**
 * Implementation of {@link Task} where all logging is captured to a buffer.
 *
 * @author lopeznr1
 */
public class BufferTask implements Task
{
	// State vars
	private boolean isAborted;
	private boolean isActive;
	private double progress;
	private String tabStr;

	private String dynamicMsgLast;
	private StringBuffer workSB;

	/** Standard Constructor */
	public BufferTask()
	{
		isAborted = false;
		isActive = true;
		progress = 0;
		tabStr = null;

		dynamicMsgLast = null;
		workSB = new StringBuffer();
	}

	/**
	 * Clears out the buffer. The buffer will return an empty string after this call.
	 */
	public void clearBuffer()
	{
		dynamicMsgLast = null;
		workSB = new StringBuffer();
	}

	/**
	 * Return the (full) text sent to this task's buffer.
	 */
	public String getBuffer()
	{
		String tmpMsg = dynamicMsgLast;
		if (tmpMsg == null)
			return workSB.toString();

		return workSB.toString() + tmpMsg;
	}

	@Override
	public void abort()
	{
		isAborted = true;
		isActive = false;
	}

	@Override
	public double getProgress()
	{
		return progress;
	}

	@Override
	public boolean isAborted()
	{
		return isAborted;
	}

	@Override
	public boolean isActive()
	{
		return isActive;
	}

	@Override
	public void logReg(String aFmtMsg, Object... aObjArr)
	{
		// Force the last dynamic message to output
		String dynMsg = dynamicMsgLast;
		if (dynMsg != null)
		{
			if (tabStr != null)
				dynMsg = dynMsg.replace("\t", tabStr);
			workSB.append(dynMsg);
		}
		dynamicMsgLast = null;

		String tmpMsg = String.format(aFmtMsg, aObjArr);

		// Update the tab chars
		if (tabStr != null)
			tmpMsg = tmpMsg.replace("\t", tabStr);

		workSB.append(tmpMsg);
	}

	@Override
	public void logRegln(String aFmtMsg, Object... aObjArr)
	{
		logReg(aFmtMsg + '\n', aObjArr);
	}

	@Override
	public void logRegUpdate(String aFmtMsg, Object... aObjArr)
	{
		// Update the dynamic message
		String tmpMsg = String.format(aFmtMsg, aObjArr);
		dynamicMsgLast = tmpMsg;
	}

	@Override
	public void reset()
	{
		isAborted = false;
		isActive = true;
		progress = 0;
	}

	@Override
	public void setProgress(double aProgress)
	{
		progress = aProgress;
	}

	@Override
	public void setProgress(int currVal, int maxVal)
	{
		setProgress((currVal + 0.0) / maxVal);
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
	public void setTabSize(int numSpaces)
	{
		tabStr = Strings.repeat(" ", numSpaces);
	}

	@Override
	public void setTitle(String aTitle)
	{
		; // Nothing to do
	}

}
