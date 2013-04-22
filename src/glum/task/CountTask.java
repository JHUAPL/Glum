package glum.task;

/**
 * Task that automatically updates the progress based on a count.
 * Once the currCount == fullCount then the Task is Complete.
 */
public class CountTask implements Task
{
	protected Task refTask;
	protected int fullCount;
	protected int currCount;

	public CountTask(Task aRefTask, int aFullCount)
	{
		refTask = aRefTask;
		fullCount = aFullCount;
		currCount = 0;
	}
	
	/**
	 * Increment the currCount (and corresponding) progress to
	 * completion.
	 */
	public void incrementCount()
	{
		currCount++;
		refTask.setProgress(currCount, fullCount);
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
		return (currCount + 0.0) / fullCount;
	}

	@Override
	public void reset()
	{
		currCount = 0;
		refTask.setProgress(currCount, fullCount);
	}

	@Override
	public void setProgress(double aProgress)
	{
		// The only way to increase the progress is to increment the count.
		; // Nothing to do
	}

	@Override
	public void setProgress(int currVal, int maxVal)
	{
		setProgress((currVal + 0.0)/ maxVal);
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
