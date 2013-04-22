package glum.util;

import glum.task.Task;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;

import javax.swing.SwingUtilities;

public class ThreadUtil
{

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
			catch(InterruptedException aExp)
			{
				aExp.printStackTrace();
			}
			catch(InvocationTargetException aExp)
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
		Thread aThread;

		aThread = new Thread(aRunnable, threadName);
		aThread.start();
	}

	/**
	 * Utility method to suspend the current thread for numMS milliseconds.
	 * 
	 * @param aTask
	 *           This method will return prematurely if aTask is no longer active.
	 */
	public static void safeSleep(long numMS, Task aTask)
	{
		long wakeTime;
		
		wakeTime = System.currentTimeMillis() + numMS;
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
		catch(InterruptedException aExp)
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
		catch(InterruptedException aExp)
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
		catch(InterruptedException aExp)
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
		long currTime, sleepTime;
		boolean isInterrupt;

		currTime = System.currentTimeMillis();
		while (currTime < nextWakeTime)
		{
			sleepTime = nextWakeTime - currTime;

			isInterrupt = ThreadUtil.safeSleep(sleepTime);
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
	 *           This method will return prematurely if aTask is no longer active.
	 */
	public static void sleepUntilTime(long nextWakeTime, Task aTask)
	{
		long currTime, sleepTime;

		currTime = System.currentTimeMillis();
		while (currTime < nextWakeTime && aTask.isActive() == true)
		{
			sleepTime = nextWakeTime - currTime;
			ThreadUtil.safeSleep(sleepTime);

			currTime = System.currentTimeMillis();
		}
	}

	/**
	 * Utility method to print the stack trace of aExp to a string
	 */
	public static String getStackTrace(Throwable aThrowable)
	{
		StringBuilder strBuf;

		strBuf = new StringBuilder();
		strBuf.append(aThrowable.getClass().getName() + "\n");
		strBuf.append("Msg: " + aThrowable.getMessage() + "\n");

		// Print out the stack trace
		for (StackTraceElement aItem : aThrowable.getStackTrace())
			strBuf.append("      " + aItem.toString() + "\n");

		// Print out any cause
		// TODO

		return strBuf.toString();
	}

	/**
	 * Utility method to print the stack trace of aExp to a string exactly as {@link Throwable#printStackTrace}
	 */
	public static String getStackTraceClassic(Throwable aThrowable)
	{
		StringWriter stringWriter;
		PrintWriter printWriter;

		stringWriter = new StringWriter();
		printWriter = new PrintWriter(stringWriter);
		aThrowable.printStackTrace(printWriter);

		printWriter.close();
		return stringWriter.toString();
	}

}
