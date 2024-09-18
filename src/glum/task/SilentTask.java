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
 * Implementation of {@link Task} where all logging is ignored.
 *
 * @author lopeznr1
 */
public class SilentTask implements Task
{
	// State vars
	private boolean isAborted;
	private boolean isActive;
	private double progress;

	/** Standard Constructor */
	public SilentTask()
	{
		isAborted = false;
		isActive = true;
		progress = 0;
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
