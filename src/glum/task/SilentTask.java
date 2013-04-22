package glum.task;

public class SilentTask implements Task
{
	private boolean isActive;
	private double progress;

	public SilentTask()
	{
		isActive = true;
		progress = 0;
	}

	@Override
	public void abort()
	{
		isActive = false;
	}

	@Override
	public void infoAppend(String aMsg)
	{
		; // Nothing to do
	}

	@Override
	public void infoAppendln(String aMsg)
	{
		; // Nothing to do
	}

	@Override
	public void infoUpdate(String aMsg)
	{
		; // Nothing to do
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
		; // Nothing to do
	}

	@Override
	public void setStatus(String aStatus)
	{
		; // Nothing to do
	}

	@Override
	public void setTabSize(int numSpaces)
	{
		; // Nothing to do
	}

	@Override
	public void setTitle(String aTitle)
	{
		; // Nothing to do
	}

	@Override
	public boolean isActive()
	{
		return isActive;
	}

}
