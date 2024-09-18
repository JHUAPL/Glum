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
package glum.logic;

import java.util.*;

import glum.util.ThreadUtil;

/**
 * Shutdown logic that attempts to properly shutdown the refLogicChunkEngine. This logic assumes that any non daemon
 * threads that appear after the creation of this shutdown hook are a direct result of the LogicChunkEngine. Any threads
 * that are started due to a LogicChunkEngine should be properly stopped whenever the dispose() method is called on the
 * corresponding LogicChunk.
 * <ul>
 * <li>This shutdown hook should be installed via Runtime.getRuntime().addShutdownHook().
 * <li>This shutdown hook should be installed before aLogicChunkEngine.loadLogicChunks() is called.
 * <li>This class should remain package visible
 * </ul>
 *
 * @author lopeznr1
 */
class ShutdownHook extends Thread
{
	// State vars
	private Set<Thread> ignoreThreadS;
	private LogicChunkEngine refLogicChunkEngine;
	private String appName;
	private boolean doAbortExit;
	private boolean doQuickExit;

	/**
	 * Standard Constructor
	 */
	public ShutdownHook(LogicChunkEngine aLogicChunkEngine, String aAppName)
	{
		refLogicChunkEngine = aLogicChunkEngine;
		appName = aAppName;
		doAbortExit = false;
		doQuickExit = false;

		// Form the list of currently running non daemon threads that can be ignored
		// Any non daemon threads that appear are assumed to be started via a LogicChunk and thus
		// should be properly stopped whenever the dispose() method is called on the LogicChunk.
		ignoreThreadS = new HashSet<>();
		ignoreThreadS.addAll(Thread.getAllStackTraces().keySet());
		ignoreThreadS.add(this);

		// Set in the preferred name of this thread
		setName("thread-" + getClass().getSimpleName());
	}

	/**
	 * Configures the shutdown hook to exit immediately by skipping the shutdown of the refLogicChunkEngine.
	 * <p>
	 * Note the associated LogicChunks dispose method will never be called.
	 */
	public void setAbortExit(boolean aBool)
	{
		doAbortExit = aBool;
	}

	/**
	 * Configures the shutdown hook to exit quickly by not waiting for daemon threads.
	 */
	public void setQuickExit(boolean aBool)
	{
		doQuickExit = aBool;
	}

	@Override
	public void run()
	{
		// Bail if we have been marked to abort
		if (doAbortExit == true)
		{
			System.out.println(appName + " has been aborted!");
			return;
		}

		// Shutdown the LogicChunkEngine
		refLogicChunkEngine.dispose();

		// Bail if we are configured to exit quickly
		if (doQuickExit == true)
			return;

		// Ensure all non daemon threads are dead
		List<Thread> origL = new ArrayList<>();
		var currL = getBlockList();

		var cntAttempts = 1;
		while (currL.isEmpty() == false)
		{
			// Dump out the blocking threads, whenever there is a change
			if (origL.size() != currL.size())
			{
				System.out.println("Shutdown logic blocked by " + currL.size() + " threads..");
				for (Thread aThread : currL)
				{
					var tmpThreadGroup = aThread.getThreadGroup();
					if (tmpThreadGroup != null)
						System.out.println("   Waiting for non daemon thread to die: " + aThread.getName()
								+ "   threadGroup: " + tmpThreadGroup.getName());
					else
						System.out.println(
								"   Waiting for non daemon thread to die: " + aThread.getName() + "   threadGroup: null");

				}
				System.out.println();

				origL = currL;
			}

			// Give the blocked threads some time to exit
			ThreadUtil.safeSleep(1000);

			// Retrieve the updated list of blocking threads
			currL = getBlockList();

			// Bail if too many failed attempts
			if (cntAttempts >= 7 & currL.isEmpty() == false)
			{
				// Print out the stack trace of all active threads
				System.out.println("Shutdown logic blocked by " + currL.size() + " threads..");
				for (Thread aThread : currL)
				{
					System.out.println("   [" + aThread.getName() + "] StackTrace:");
					for (StackTraceElement aItem : aThread.getStackTrace())
						System.out.println("      " + aItem.toString());
				}

				System.out.println("\nAborting " + appName + ". Waited too long...");
				break;
			}

			cntAttempts++;
		}

		// Let the user know that we were properly terminated
		System.out.println(appName + " has been shutdown.");
	}

	/**
	 * Helper method to retrieve the set of non daemon threads that we are waiting on to finish executing
	 * <p>
	 * Note any thread with the following name pattern will be ignored:</br>
	 * AWT-*, DestroyJavaVM
	 */
	private List<Thread> getBlockList()
	{
		var blockL = new ArrayList<Thread>();

		var fullL = new ArrayList<>(Thread.getAllStackTraces().keySet());
		for (Thread aThread : fullL)
		{
			var name = aThread.getName();

			// Record the non daemon threads that are still alive and not in the ignoreThreadSet or with a known name
			if (aThread.isDaemon() == false && aThread.isAlive() == true && ignoreThreadS.contains(aThread) == false)
			{
				// Only add the thread if the name is not one of a well defined name such as: AWT-*, DestroyJavaVM
				if ((name.startsWith("AWT-") == true || name.startsWith("DestroyJavaVM") == true) == false)
					blockL.add(aThread);
			}
		}

		return blockL;
	}

}
