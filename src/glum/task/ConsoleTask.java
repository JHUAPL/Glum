package glum.task;

import com.google.common.base.Strings;

import glum.unit.NumberUnit;
import glum.unit.Unit;

public class ConsoleTask implements Task
{
	private boolean isActive;
	private double progress;
	
	private String dynamicMsgFrag;
	private String dynamicMsgLast;
	private long dynamicMsgRateMs;
	private long oldTimeMs;

	private Unit progressUnit;
	private boolean showProgressInUpdate;
	
	private String tabStr;

	public ConsoleTask(boolean aShowProgressInUpdate)
	{
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

	public ConsoleTask()
	{
		this(false);
	}

	@Override
	public void abort()
	{
		isActive = false;
	}
	
	@Override
	public void infoAppend(String aMsg)
	{
		// Force the last dynamic message to be shown
		if (dynamicMsgLast != null)
			dynamicMsgUpdateForce(dynamicMsgLast, 0);
		dynamicMsgLast = null;
		
		// Update the tab chars
		if (tabStr != null)
			aMsg = aMsg.replace("\t", tabStr);
		
		// Display the new message
		System.out.print(aMsg);
		System.out.flush();

		// Reset the dynamic vars
		dynamicMsgFrag = null;
		dynamicMsgLast = null;
		oldTimeMs = Long.MIN_VALUE;
	}

	@Override
	public void infoAppendln(String aMsg)
	{
		infoAppend(aMsg + '\n');
	}
	
	@Override
	public void infoUpdate(String aMsg)
	{
		long currTimeMs, totalTimeMs;
		
		// Auto add the progress into update messages if necessary
		dynamicMsgLast = aMsg;
		if (showProgressInUpdate == true)
			dynamicMsgLast = "[" + progressUnit.getString(getProgress()) +"%] " + aMsg;

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
	public double getProgress()
	{
		return progress;
	}

	@Override
	public void reset()
	{
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
		dynamicMsgRateMs = aRateMs;
	}

	@Override
	public void setStatus(String aStatus)
	{
		// ConsoleTasks do not support a status line.
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
		// ConsoleTasks do not support a title line.
		; // Nothing to do
	}

	@Override
	public boolean isActive()
	{
		return isActive;
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
