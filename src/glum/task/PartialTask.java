package glum.task;

public class PartialTask implements Task
{
	protected Task refTask;
	protected double progressOffset;
	protected double progressTotalFragment;
	protected double internalProgress;

	public PartialTask(Task aRefTask, double aProgressOffset, double aProgressTotalFragment)
	{
		refTask = aRefTask;
		progressOffset = aProgressOffset;
		progressTotalFragment = aProgressTotalFragment;
		internalProgress = 0;
	}

	@Override
	public void abort()
	{
		refTask.abort();
	}

	@Override
	public void infoAppend(String aMsg)
	{
		refTask.infoAppend(aMsg);
	}

	@Override
	public void infoAppendln(String aMsg)
	{
		refTask.infoAppendln(aMsg);
	}

	@Override
	public void infoUpdate(String aMsg)
	{
		refTask.infoUpdate(aMsg);
	}

	@Override
	public double getProgress()
	{
		return progressOffset + (progressTotalFragment * internalProgress);
	}

	@Override
	public void reset()
	{
		setProgress(0);
	}

	@Override
	public void setProgress(double aProgress)
	{
		internalProgress = aProgress;

		// Update the refTask with the appropriate task value
		refTask.setProgress(this.getProgress());
	}

	@Override
	public void setProgress(int currVal, int maxVal)
	{
		setProgress((currVal + 0.0) / maxVal);
	}

	@Override
	public void setRefreshRateMs(long aRateMs)
	{
		refTask.setRefreshRateMs(aRateMs);
	}

	@Override
	public void setStatus(String aStatus)
	{
		refTask.setStatus(aStatus);
	}

	@Override
	public void setTabSize(int numSpaces)
	{
		refTask.setTabSize(numSpaces);
	}

	@Override
	public void setTitle(String aTitle)
	{
		refTask.setTitle(aTitle);
	}

	@Override
	public boolean isActive()
	{
		return refTask.isActive();
	}

}
