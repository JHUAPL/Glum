package glum.gui.panel.task;

import java.awt.Component;
import java.awt.Font;

import javax.swing.*;

import glum.gui.component.GLabel;
import glum.gui.panel.GlassPanel;
import glum.task.Task;
import glum.util.WallTimer;

/**
 * Abstract TaskPanel that handles all of the state vars used to maintain the Task interface.
 */
public abstract class BaseTaskPanel extends GlassPanel implements Task
{
	// State vars
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

	public BaseTaskPanel(Component aParent)
	{
		super(aParent);

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
		isActive = false;
		Runnable tmpRunnable = () -> updateGui();
		SwingUtilities.invokeLater(tmpRunnable);
	}

	@Override
	public void infoAppend(String aMsg)
	{
		infoUpdateForce(aMsg);

		// Reset the dynamic vars
		infoMsgFrag = null;
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
		// Bail if it is not time to update our UI
		if (isTimeForUpdate() == false)
			return;

		infoUpdateForce(aMsg);
	}

	@Override
	public double getProgress()
	{
		return mProgress;
	}

	@Override
	public void reset()
	{
		isActive = true;
		infoMsgFrag = null;
		mProgress = 0;
		mStatus = "";
		mTimer.start();
		oldTimeMs = Long.MIN_VALUE;

		// Clear out all the text in the infoTA
		if (infoTA != null)
			infoTA.setText("");

		Runnable tmpRunnable = () -> updateGui();
		SwingUtilities.invokeLater(tmpRunnable);
	}

	@Override
	public void setProgress(double aProgress)
	{
		mProgress = aProgress;

		// Bail if it is not time to update our UI
		if (isTimeForUpdate() == false && aProgress < 1.0)
			return;

		Runnable tmpRunnable = () -> updateGui();
		SwingUtilities.invokeLater(tmpRunnable);
	}

	@Override
	public void setProgress(int currVal, int maxVal)
	{
		setProgress((currVal + 0.0) / maxVal);
	}

	@Override
	public void setRefreshRateMs(long aRateMs)
	{
		refreshRateMs = aRateMs;
	}

	@Override
	public void setTabSize(int numSpaces)
	{
		if (infoTA != null)
			infoTA.setTabSize(numSpaces);
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

		Runnable tmpRunnable = () -> updateGui();
		SwingUtilities.invokeLater(tmpRunnable);
	}

	@Override
	public boolean isActive()
	{
		return isActive;
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
	protected void infoUpdateForce(String aMsg)
	{
		int end, start;
		int currLC;

		// Bail if there is no info area
		if (infoTA == null)
			return;

		// Update the old message
		if (infoMsgFrag != null)
		{
			// Update the info message
			start = end = 0;
			try
			{
				end = infoTA.getLineEndOffset(infoTA.getLineCount() - 1);
				start = end - infoMsgFrag.length();
				infoTA.replaceRange(aMsg, start, end);
			}
			catch(Exception aExp)
			{
				System.out.println("infoMsgFrag:" + infoMsgFrag.length() + " start: " + start + " end:" + end);
				throw new RuntimeException(aExp);
			}
		}
		// Just append the message
		else
		{
			infoTA.append(aMsg);

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
					catch(Exception aExp)
					{
						System.out.println("currLC:" + currLC + " maxLC:" + maxLC + " start: " + start + " end:" + end);
						throw new RuntimeException(aExp);
					}
				}
			}
		}

		// Save off the new dynamic message fragment
		infoMsgFrag = aMsg;

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
