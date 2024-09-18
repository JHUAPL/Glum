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

import java.io.PrintStream;

import com.google.common.base.Strings;

import glum.unit.NumberUnit;
import glum.unit.Unit;

/**
 * Implementation of {@link Task} where all logging will be sent to the provided {@link PrintStream}.
 *
 * @author lopeznr1
 */
public class PrintStreamTask implements Task
{
	// Ref vars
	private final PrintStream refStream;

	// State vars
	private boolean isAborted;
	private boolean isActive;
	private double progress;
	private String tabStr;

	private String dynamicMsgLast;
	private Unit progressUnit;
	private boolean showProgressInUpdate;

	/**
	 * Standard Constructor
	 *
	 * @param aStream
	 * @param aShowProgressInUpdate
	 */
	public PrintStreamTask(PrintStream aStream, boolean aShowProgressInUpdate)
	{
		refStream = aStream;

		isAborted = false;
		isActive = true;
		progress = 0;
		tabStr = null;

		dynamicMsgLast = null;
		progressUnit = new NumberUnit("", "", 100, 2);
		showProgressInUpdate = aShowProgressInUpdate;
	}

	/**
	 * Properly close the associated file used for the log
	 */
	public void close()
	{
		refStream.close();
	}

	/**
	 * Configures whether the dynamic messages should have an automatic progress bar readout.
	 */
	public void setShowProgressInUpdate(boolean aShowProgressInUpdate)
	{
		showProgressInUpdate = aShowProgressInUpdate;
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
		if (dynamicMsgLast != null)
			refStream.print(dynamicMsgLast);
		dynamicMsgLast = null;

		String tmpMsg = String.format(aFmtMsg, aObjArr);

		// Update the tab chars
		if (tabStr != null)
			tmpMsg = tmpMsg.replace("\t", tabStr);

		// Display the new message
		refStream.print(tmpMsg);
		refStream.flush();
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

		// Auto add the progress into update messages if necessary
		if (showProgressInUpdate == true)
			dynamicMsgLast = "[" + progressUnit.getString(getProgress()) + "%] " + tmpMsg;
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
		// Refresh rate is nonsensical for a file log
		; // Nothing to do
	}

	@Override
	public void setStatus(String aStatus)
	{
		// PrintStreamTasks do not support a status line.
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
		// PrintStreamTasks do not support a title line.
		; // Nothing to do
	}

}
