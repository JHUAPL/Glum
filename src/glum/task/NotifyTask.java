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
 * Implementation of {@link Task} where notification of progress updates is made via a single {@link TaskListener}.
 * <p>
 * Notifications will be sent out once no more frequently than once every 47 milliseconds (~21 updates per second). The
 * frequency of notifications can be configured via {@link #setRefreshRateMS(long)}
 *
 * @author lopeznr1
 */
public class NotifyTask implements Task
{
	// Ref vars
	private final Task refTask;
	private final TaskListener refListener;

	// State vars
	private boolean isInit;
	private long begTime;
	private long endTime;

	private long refreshRateMs;

	/** Standard Constructor. */
	public NotifyTask(Task aTask, TaskListener aListener)
	{
		refTask = aTask;
		refListener = aListener;

		isInit = true;
		begTime = 0L;
		endTime = 0L;

		refreshRateMs = 47;
	}

	/** Simplified Constructor. */
	public NotifyTask(TaskListener aListener)
	{
		this(new SilentTask(), aListener);
	}

	/**
	 * Forces an update to this task.
	 * <p>
	 * If the task is active then the end time will be updated.
	 * <p>
	 * The {@link TaskListener} will be notified.
	 */
	public void forceUpdate()
	{
		if (isActive() == true)
			endTime = System.currentTimeMillis();

		refListener.taskUpdate(this);
	}

	/**
	 * Return true if the task has reached completion or has been aborted.
	 */
	public boolean isDone()
	{
		if (isAborted() == true)
			return true;

		if (getProgress() >= 1.0)
			return true;

		return false;
	}

	/**
	 * Return true if the task has not left the initial state.
	 * <p>
	 * A task should be considered in the initial state if the progress has not moved past 0% and / or if it has not been
	 * manually transitioned out.
	 */
	public boolean isInit()
	{
		return isInit;
	}

	/**
	 * Moves the task from the init state to the in-progress state
	 */
	public void markInitDone()
	{
		begTime = System.currentTimeMillis();
		isInit = false;
	}

	/**
	 * Returns the reference {@link Task}.
	 */
	public Task getRefTask()
	{
		return refTask;
	}

	/**
	 * Returns the rate (in milliseconds) at which notifications will be sent out.
	 */
	public long getRefreshRateMS()
	{
		return refreshRateMs;
	}

	/**
	 * Returns the (system) time when this Task was reset or initialization was completed.
	 */
	public long getTimeBeg()
	{
		return begTime;
	}

	/**
	 * Returns the (system) time when this Task was last updated.
	 */
	public long getTimeEnd()
	{
		return endTime;
	}

	@Override
	public void abort()
	{
		if (isActive() == true)
			endTime = System.currentTimeMillis();

		refTask.abort();

		isInit = false;
		refListener.taskUpdate(this);
	}

	@Override
	public double getProgress()
	{
		return refTask.getProgress();
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
		refTask.reset();

		isInit = true;
		begTime = System.currentTimeMillis();
		endTime = 0L;
	}

	@Override
	public void setProgress(double aProgress)
	{
		refTask.setProgress(aProgress);
		isInit = false;

		// Update UI at refreshRateMS
		long currTime = System.currentTimeMillis();
		if (currTime < endTime + refreshRateMs)
			return;

		// Update
		if (isActive() == true)
			endTime = System.currentTimeMillis();

		refListener.taskUpdate(this);
	}

	@Override
	public void setProgress(int aCurrVal, int aMaxVal)
	{
		refTask.setProgress(aCurrVal, aMaxVal);
		isInit = false;

		// Update UI at refreshRateMS
		long currTime = System.currentTimeMillis();
		if (currTime < endTime + refreshRateMs)
			return;

		// Update
		if (isActive() == true)
			endTime = System.currentTimeMillis();

		refListener.taskUpdate(this);
	}

	@Override
	public void setRefreshRateMs(long aRateMs)
	{
		refreshRateMs = aRateMs;
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
