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
package glum.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

import glum.task.Task;

/**
 * Collection of thread utility methods.
 * <p>
 * The provided utility methods fall under the following classes:
 * <ul>
 * <li>Transforming a stack trace into a string.
 * <li>Scheduling a {@link Runnable} on a separate thread, the AWT, or as a shutdown hook.
 * <li>Sleeping / waiting on a thread.
 * </ul>
 *
 * @author lopeznr1
 */
public class ThreadUtil
{
	/**
	 * Utility method to register the runnable into the JVM's shutdown hook.
	 */
	public static void addShutdownHook(Runnable aRunnable)
	{
		Thread tmpThread = new Thread(aRunnable);
		Runtime.getRuntime().addShutdownHook(tmpThread);
	}

	/**
	 * Utility method to print the stack trace of aExp to a string
	 */
	public static String getStackTrace(Throwable aThrowable)
	{
		StringBuilder tmpSB = new StringBuilder();
		tmpSB.append(aThrowable.getClass().getName() + "\n");
		tmpSB.append("Msg: " + aThrowable.getMessage() + "\n");

		// Print out the stack trace
		for (StackTraceElement aItem : aThrowable.getStackTrace())
			tmpSB.append("      " + aItem.toString() + "\n");

		// Print out any cause
		// TODO

		return tmpSB.toString();
	}

	/**
	 * Utility method to print the stack trace of aExp to a string exactly as {@link Throwable#printStackTrace}
	 */
	public static String getStackTraceClassic(Throwable aThrowable)
	{
		StringWriter tmpSW = new StringWriter();
		PrintWriter tmpPW = new PrintWriter(tmpSW);
		aThrowable.printStackTrace(tmpPW);

		tmpPW.close();
		return tmpSW.toString();
	}

	/**
	 * Utility method to execute aRunnable synchronously on the AWT event dispatching thread with out throwing an
	 * InterruptedException. If the thread is interrupted however the stack trace will be printed. This method will still
	 * throw an InvocationTargetException but encapsulated in a RuntimeException.
	 */
	public static void invokeAndWaitOnAwt(Runnable aRunnable)
	{
		while (true)
		{
			try
			{
				SwingUtilities.invokeAndWait(aRunnable);
				break;
			}
			catch (InterruptedException aExp)
			{
				aExp.printStackTrace();
			}
			catch (InvocationTargetException aExp)
			{
				// This is an unrecoverable exception
				throw new RuntimeException(aExp);
			}
		}
	}

	/**
	 * Utility method to launch a Runnable in a new Thread
	 */
	public static void launchRunnable(Runnable aRunnable, String threadName)
	{
		Thread tmpThread = new Thread(aRunnable, threadName);
		tmpThread.start();
	}

	/**
	 * Utility method to suspend the current thread for numMS milliseconds.
	 *
	 * @param aTask
	 *        This method will return prematurely if aTask is no longer active.
	 */
	public static void safeSleep(long numMS, Task aTask)
	{
		long wakeTime = System.currentTimeMillis() + numMS;
		sleepUntilTime(wakeTime, aTask);
	}

	/**
	 * Utility method to sleep for numMS milliseconds without throwing an {@link InterruptedException}.
	 *
	 * @return True: if the thread was interrupted
	 */
	public static boolean safeSleep(long numMS)
	{
		try
		{
			Thread.sleep(numMS);
		}
		catch (InterruptedException aExp)
		{
			return true;
		}
		return false;
	}

	/**
	 * Utility method to wait for a signal without throwing an {@link InterruptedException}.
	 *
	 * @see Thread#wait()
	 *
	 * @return True: if the thread was interrupted
	 */
	public static boolean safeWait(Object aLock)
	{
		try
		{
			aLock.wait();
		}
		catch (InterruptedException aExp)
		{
			return true;
		}
		return false;
	}

	/**
	 * Utility method to wait for a signal without throwing an {@link InterruptedException}.
	 *
	 * @see Thread#wait(long)
	 *
	 * @return True: if the thread was interrupted
	 */
	public static boolean safeWait(Object aLock, long aMaxTimeMS)
	{
		try
		{
			aLock.wait(aMaxTimeMS);
		}
		catch (InterruptedException aExp)
		{
			return true;
		}
		return false;
	}

	/**
	 * Utility method to suspend the current thread until the system time is at or has passed nextWakeTime
	 *
	 * @return True: if the thread was interrupted
	 */
	public static boolean sleepUntilTime(long nextWakeTime)
	{
		long currTime = System.currentTimeMillis();
		while (currTime < nextWakeTime)
		{
			long sleepTime = nextWakeTime - currTime;

			boolean isInterrupt = ThreadUtil.safeSleep(sleepTime);
			if (isInterrupt == true)
				return true;

			currTime = System.currentTimeMillis();
		}

		return false;
	}

	/**
	 * Utility method to suspend the current thread until the system time is at or has passed nextWakeTime
	 *
	 * @param aTask
	 *        This method will return prematurely if aTask is no longer active.
	 */
	public static void sleepUntilTime(long nextWakeTime, Task aTask)
	{
		long currTime = System.currentTimeMillis();
		while (currTime < nextWakeTime && aTask.isActive() == true)
		{
			long sleepTime = nextWakeTime - currTime;
			ThreadUtil.safeSleep(sleepTime);

			currTime = System.currentTimeMillis();
		}
	}

}
