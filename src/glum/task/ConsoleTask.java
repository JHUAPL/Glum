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

import glum.unit.NumberUnit;
import glum.unit.Unit;

/**
 * Implementation of {@link Task} where all logging will be sent to the console.
 *
 * @author lopeznr1
 */
public class ConsoleTask implements Task
{
	// State vars
	private boolean isAborted;
	private boolean isActive;
	private double progress;

	private String dynamicMsgFrag;
	private String dynamicMsgLast;
	private long dynamicMsgRateMs;
	private long oldTimeMs;

	private Unit progressUnit;
	private boolean showProgressInUpdate;

	private String tabStr;

	/** Standard Constructor */
	public ConsoleTask(boolean aShowProgressInUpdate)
	{
		isAborted = false;
		isActive = true;
		progress = 0;

		dynamicMsgFrag = null;
		dynamicMsgLast = null;
		dynamicMsgRateMs = 37;
		oldTimeMs = Long.MIN_VALUE;

		progressUnit = new NumberUnit("", "", 100, 2);
		showProgressInUpdate = aShowProgressInUpdate;

		tabStr = null;
	}

	/** Simplified Constructor */
	public ConsoleTask()
	{
		this(false);
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
		// Force the last dynamic message to be shown
		if (dynamicMsgLast != null)
			dynamicMsgUpdateForce(dynamicMsgLast, 0);
		dynamicMsgLast = null;

		String tmpMsg = String.format(aFmtMsg, aObjArr);

		// Update the tab chars
		if (tabStr != null)
			tmpMsg = tmpMsg.replace("\t", tabStr);

		// Display the new message
		System.out.print(tmpMsg);
		System.out.flush();

		// Reset the dynamic vars
		dynamicMsgFrag = null;
		dynamicMsgLast = null;
		oldTimeMs = Long.MIN_VALUE;
	}

	@Override
	public void logRegln(String aFmtMsg, Object... aObjArr)
	{
		logReg(aFmtMsg + '\n', aObjArr);
	}

	@Override
	public void logRegUpdate(String aFmtMsg, Object... aObjArr)
	{
		long currTimeMs, totalTimeMs;

		String tmpMsg = String.format(aFmtMsg, aObjArr);

		// Auto add the progress into update messages if necessary
		dynamicMsgLast = tmpMsg;
		if (showProgressInUpdate == true)
			dynamicMsgLast = "[" + progressUnit.getString(getProgress()) + "%] " + tmpMsg;

		// Get the current time
		currTimeMs = System.nanoTime() / 1000000;

		// Has enough time elapsed/
		totalTimeMs = currTimeMs - oldTimeMs;
		if (totalTimeMs < dynamicMsgRateMs && totalTimeMs > 0)
			return;

		// Worker method
		dynamicMsgUpdateForce(dynamicMsgLast, currTimeMs);
		dynamicMsgLast = null;
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
	public void setProgress(int aCurrVal, int aMaxVal)
	{
		setProgress((aCurrVal + 0.0) / aMaxVal);
	}

	@Override
	public void setRefreshRateMs(long aRateMs)
	{
		dynamicMsgRateMs = aRateMs;
	}

	@Override
	public void setStatus(String aStatus)
	{
		// ConsoleTasks do not support a status line.
		; // Nothing to do
	}

	@Override
	public void setTabSize(int aNumSpaces)
	{
		tabStr = Strings.repeat(" ", aNumSpaces);
	}

	@Override
	public void setTitle(String aTitle)
	{
		// ConsoleTasks do not support a title line.
		; // Nothing to do
	}

	/**
	 * Helper method that does the actual updating of the previous dynamic text with aMsg.
	 */
	protected void dynamicMsgUpdateForce(String aMsg, long currTimeMs)
	{
		int numTempChars;

		// Erase the old message
		if (dynamicMsgFrag != null)
		{
			numTempChars = dynamicMsgFrag.length();
			for (int c1 = 0; c1 < numTempChars; c1++)
				System.out.print("\b \b");
		}

		// Update the tab chars
		if (tabStr != null)
			aMsg = aMsg.replace("\t", tabStr);

		// Output the new message
		System.out.print(aMsg);
		System.out.flush();

		// Save off the new dynamic message fragment
		dynamicMsgFrag = aMsg;

		// Update our internal time
		oldTimeMs = currTimeMs;
	}

}
