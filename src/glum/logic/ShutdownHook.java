package glum.logic;

import glum.util.ThreadUtil;

import java.util.List;
import java.util.Set;

import com.google.common.collect.Lists;
import com.google.common.collect.Sets;

/**
 * Shutdown logic that attempts to properly shutdown the refLogicChunkEngine. This logic assumes
 * that any non daemon threads that appear after the creation of this shutdown hook are a direct 
 * result of the LogicChunkEngine. Any threads that are started due to a LogicChunkEngine should
 * be properly stopped whenever the dispose() method is called on the corresponding LogicChunk. 
 * a
 * <LI>This shutdown hook should be installed via Runtime.getRuntime().addShutdownHook().
 * <LI>This shutdown hook should be installed before aLogicChunkEngine.loadLogicChunks() is called.
 * <LI>This class should remain package visible
 */
class ShutdownHook extends Thread
{
	// State vars
	private Set<Thread> ignoreThreadSet;
	private LogicChunkEngine refLogicChunkEngine;
	private String appName;
	private boolean doAbortExit;
	private boolean doQuickExit;
	
	public ShutdownHook(LogicChunkEngine aLogicChunkEngine, String aAppName)
	{
		refLogicChunkEngine = aLogicChunkEngine;
		appName = aAppName;
		doAbortExit = false;
		doQuickExit = false;
		
		// Form the list of currently running non daemon threads that can be ignored
		// Any non daemon threads that appear are assumed to be started via a LogicChunk and thus
		// should be properly stopped whenever the dispose() method is called on the LogicChunk.
		ignoreThreadSet = Sets.newHashSet();
		ignoreThreadSet.addAll(Thread.getAllStackTraces().keySet());
		ignoreThreadSet.add(this);
		
		// Set in the preferred name of this thread
		setName("thread-" + getClass().getSimpleName());
	}

	/**
	 * Configures the shutdown hook to exit immediately by skipping the shutdown of the refLogicChunkEngine.
	 * <P>
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
		List<Thread> origList, currList;
		ThreadGroup aThreadGroup;
		int cntAttempts;
		
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
		origList = Lists.newArrayList(); 
		currList = getBlockList();
		
		cntAttempts = 1;
		while (currList.isEmpty() == false)
		{
			// Dump out the blocking threads, whenever there is a change
			if (origList.size() != currList.size())
			{
				System.out.println("Shutdown logic blocked by " + currList.size() + " threads..");
				for (Thread aThread : currList)
				{
					aThreadGroup = aThread.getThreadGroup();
					if (aThreadGroup != null)
						System.out.println("   Waiting for non daemon thread to die: " + aThread.getName() + "   threadGroup: " + aThreadGroup.getName());
					else
						System.out.println("   Waiting for non daemon thread to die: " + aThread.getName() + "   threadGroup: null");
					
				}
				System.out.println();
				
				origList = currList;
			}
			
			// Give the blocked threads some time to exit
			ThreadUtil.safeSleep(1000);

			// Retrieve the updated list of blocking threads
			currList = getBlockList();

			// Bail if too many failed attempts
			if (cntAttempts >= 7 & currList.isEmpty() == false)
			{
				// Print out the stack trace of all active threads
				System.out.println("Shutdown logic blocked by " + currList.size() + " threads..");
				for (Thread aThread : currList)
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
	 * <BR> Note any thread with the following name pattern will be ignored:
	 * AWT-*, DestroyJavaVM
	 */
	private List<Thread> getBlockList()
	{
		List<Thread> fullList;
		List<Thread> blockList;
		String name;
		
		blockList = Lists.newLinkedList();
		
		fullList = Lists.newLinkedList(Thread.getAllStackTraces().keySet());
		for (Thread aThread : fullList)
		{
			name = aThread.getName();
			
			// Record the non daemon threads that are still alive and not in the ignoreThreadSet or with a known name
			if (aThread.isDaemon() == false && aThread.isAlive() == true && ignoreThreadSet.contains(aThread) == false)
			{
				// Only add the thread if the name is not one of a well defined name such as: AWT-*, DestroyJavaVM
				if ((name.startsWith("AWT-") == true || name.startsWith("DestroyJavaVM") == true) == false)
					blockList.add(aThread);
			}
		}
		
		return blockList;
	}

}
