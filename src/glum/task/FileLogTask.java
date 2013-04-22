package glum.task;

import glum.unit.NumberUnit;
import glum.unit.Unit;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;

import com.google.common.base.Strings;

public class FileLogTask implements Task
{
	private boolean isActive;
	private double progress;
	private String tabStr;

	private String dynamicMsgLast;
	private Unit progressUnit;
	private boolean showProgressInUpdate;

	private PrintStream printStream;

	public FileLogTask(File aFile, boolean isAppend, boolean aShowProgressInUpdate)
	{
		isActive = true;
		progress = 0;
		tabStr = null;

		dynamicMsgLast = null;
		progressUnit = new NumberUnit("", "", 100, 2);
		showProgressInUpdate = aShowProgressInUpdate;

		// Open the file stream for writing
		try
		{
			printStream = new PrintStream(new FileOutputStream(aFile, isAppend));
		}
		catch (FileNotFoundException aExp)
		{
			throw new RuntimeException(aExp);
		}
		
	}

	public FileLogTask(File aFile, boolean isAppend)
	{
		this(aFile, isAppend, false);
	}

	/**
	 * Properly close the associated file used for the log
	 */
	public void close()
	{
		printStream.close();
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
		isActive = false;
	}

	@Override
	public void infoAppend(String aMsg)
	{
		// Force the last dynamic message to output
		if (dynamicMsgLast != null)
			printStream.print(dynamicMsgLast);
		dynamicMsgLast = null;

		// Update the tab chars
		if (tabStr != null)
			aMsg = aMsg.replace("\t", tabStr);

		// Display the new message
		printStream.print(aMsg);
		printStream.flush();
	}

	@Override
	public void infoAppendln(String aMsg)
	{
		infoAppend(aMsg + '\n');
	}

	@Override
	public void infoUpdate(String aMsg)
	{
		// Update the dynamic message
		dynamicMsgLast = aMsg;

		// Auto add the progress into update messages if necessary
		if (showProgressInUpdate == true)
			dynamicMsgLast = "[" + progressUnit.getString(getProgress()) + "%] " + aMsg;
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
		// Refresh rate is nonsensical for a file log
		; // Nothing to do
	}

	@Override
	public void setStatus(String aStatus)
	{
		// FileLogTasks do not support a status line.
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
		// FileLogTasks do not support a title line.
		; // Nothing to do
	}

	@Override
	public boolean isActive()
	{
		return isActive;
	}

}
