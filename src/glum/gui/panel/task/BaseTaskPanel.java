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
package glum.gui.panel.task;

import java.awt.Component;
import java.awt.Font;

import javax.swing.*;

import glum.gui.component.GLabel;
import glum.gui.panel.GlassPanel;
import glum.task.Task;
import glum.util.WallTimer;

/**
 * Abstract GUI component that provides an implementation of the {@link Task} interface.
 *
 * @author lopeznr1
 */
public abstract class BaseTaskPanel extends GlassPanel implements Task
{
	// State vars
	protected boolean isAborted;
	protected boolean isActive;
	protected String infoMsgFrag;
	protected double mProgress;
	protected String mStatus;
	protected WallTimer mTimer;
	protected long oldTimeMs;
	protected long refreshRateMs;
	protected int maxLC;

	// Gui vars
	protected JLabel titleL;
	protected GLabel progressL, timerL;
	protected GLabel statusL;
	protected JTextArea infoTA;

	/**
	 * Standard Constructor
	 */
	public BaseTaskPanel(Component aParent)
	{
		super(aParent);

		isAborted = false;
		isActive = true;
		infoMsgFrag = null;
		mProgress = 0;
		mStatus = "";
		mTimer = new WallTimer(true);
		oldTimeMs = Long.MIN_VALUE;
		refreshRateMs = 47;
		maxLC = -1;
	}

	/**
	 * Method to set the font of the infoTA
	 */
	public void setFontInfo(Font aFont)
	{
		if (infoTA != null)
			infoTA.setFont(aFont);
	}

	/**
	 * Sets in the maximum number of lines that will be displayed in the info area
	 */
	public void setMaxLineCount(int aMaxLC)
	{
		maxLC = aMaxLC;
	}

	@Override
	public void abort()
	{
		mTimer.stop();
		isAborted = true;
		isActive = false;
		SwingUtilities.invokeLater(() -> updateGui());
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
		logRegUpdateForce(aFmtMsg, aObjArr);

		// Reset the dynamic vars
		infoMsgFrag = null;
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
		// Bail if it is not time to update our UI
		if (isTimeForUpdate() == false)
			return;

		logRegUpdateForce(aFmtMsg, aObjArr);
	}

	@Override
	public double getProgress()
	{
		return mProgress;
	}

	@Override
	public void reset()
	{
		isAborted = false;
		isActive = true;
		infoMsgFrag = null;
		mProgress = 0;
		mStatus = "";
		mTimer.start();
		oldTimeMs = Long.MIN_VALUE;

		// Clear out all the text in the infoTA
		if (infoTA != null)
			infoTA.setText("");

		SwingUtilities.invokeLater(() -> updateGui());
	}

	@Override
	public void setProgress(double aProgress)
	{
		mProgress = aProgress;

		// Bail if it is not time to update our UI
		if (isTimeForUpdate() == false && aProgress < 1.0)
			return;

		SwingUtilities.invokeLater(() -> updateGui());
	}

	@Override
	public void setProgress(int aCurrVal, int aMaxVal)
	{
		setProgress((aCurrVal + 0.0) / aMaxVal);
	}

	@Override
	public void setRefreshRateMs(long aRateMs)
	{
		refreshRateMs = aRateMs;
	}

	@Override
	public void setTabSize(int aNumSpaces)
	{
		if (infoTA != null)
			infoTA.setTabSize(aNumSpaces);
	}

	@Override
	public void setTitle(String aTitle)
	{
		titleL.setText(aTitle);
	}

	@Override
	public void setStatus(String aMsg)
	{
		mStatus = aMsg;

		// Bail if it is not time to update our UI
		if (isTimeForUpdate() == false)
			return;

		SwingUtilities.invokeLater(() -> updateGui());
	}

	/**
	 * Utility method that checks the update rate and returns true if the task UI can be updated. Note this method keeps
	 * track of the timer vars to honor refreshRateMs
	 */
	protected boolean isTimeForUpdate()
	{
		long currTimeMs, totalTimeMs;

		// Get the current time
		currTimeMs = System.nanoTime() / 1000000;

		// Has enough time elapsed?
		totalTimeMs = currTimeMs - oldTimeMs;
		if (totalTimeMs < refreshRateMs && totalTimeMs > 0)
			return false;

		oldTimeMs = currTimeMs;
		return true;
	}

	/**
	 * Utility method that does the actual updating of the previous info text with aMsg
	 */
	protected void logRegUpdateForce(String aFmtMsg, Object... aObjArr)
	{
		int end, start;
		int currLC;

		// Bail if there is no info area
		if (infoTA == null)
			return;

		String tmpMsg = String.format(aFmtMsg, aObjArr);

		// Update the old message
		if (infoMsgFrag != null)
		{
			// Update the info message
			start = end = 0;
			try
			{
				end = infoTA.getLineEndOffset(infoTA.getLineCount() - 1);
				start = end - infoMsgFrag.length();
				infoTA.replaceRange(tmpMsg, start, end);
			}
			catch (Exception aExp)
			{
				System.err.println("infoMsgFrag:" + infoMsgFrag.length() + " start: " + start + " end:" + end);
				throw new RuntimeException(aExp);
			}
		}
		// Just append the message
		else
		{
			infoTA.append(tmpMsg);

			// Trim the buffer if we exceed our maxLC
			if (maxLC > 0)
			{
				currLC = infoTA.getLineCount();
				if (currLC > maxLC)
				{
					start = end = 0;
					try
					{
						start = 0;
						end = infoTA.getLineEndOffset(currLC - maxLC);
						infoTA.replaceRange("", start, end);
					}
					catch (Exception aExp)
					{
						System.err.println("currLC:" + currLC + " maxLC:" + maxLC + " start: " + start + " end:" + end);
						throw new RuntimeException(aExp);
					}
				}
			}
		}

		// Save off the new dynamic message fragment
		infoMsgFrag = tmpMsg;

//		timerL.setValue(mTimer.getTotal());
//		SwingUtilities.invokeLater(new FunctionRunnable(timerL, "updateGui"));

		// Update our internal time
		oldTimeMs = System.nanoTime() / 1000000;
	}

	/**
	 * Utility method to update the GUI
	 */
	protected abstract void updateGui();

}
