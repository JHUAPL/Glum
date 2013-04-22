package glum.task;

import java.util.List;

import com.google.common.collect.Lists;

/**
 * Task designed to encompass many "sub" child tasks and forward actions to each child task.
 * <P>
 * On any query method call such as {@link #getProgress()}, the first child task specified in the constructor will be
 * utilized as the task to fulfill the query.
 */
public class SplitTask implements Task
{
	private List<Task> childTaskList;

	/**
	 * @param aTaskArr
	 *           The list of child tasks for which this SplitTask will automatically forward the method calls to.
	 */
	public SplitTask(Task... aTaskArr)
	{
		childTaskList = Lists.newLinkedList();
		for (Task aTask : aTaskArr)
			childTaskList.add(aTask);
	}

	@Override
	public void abort()
	{
		for (Task aTask : childTaskList)
			aTask.abort();
	}

	@Override
	public void infoAppend(String aMsg)
	{
		for (Task aTask : childTaskList)
			aTask.infoAppend(aMsg);
	}

	@Override
	public void infoAppendln(String aMsg)
	{
		for (Task aTask : childTaskList)
			aTask.infoAppendln(aMsg);
	}

	@Override
	public void infoUpdate(String aMsg)
	{
		for (Task aTask : childTaskList)
			aTask.infoUpdate(aMsg);
	}

	@Override
	public double getProgress()
	{
		return childTaskList.get(0).getProgress();
	}

	@Override
	public void reset()
	{
		for (Task aTask : childTaskList)
			aTask.reset();
	}

	@Override
	public void setProgress(double aProgress)
	{
		for (Task aTask : childTaskList)
			aTask.setProgress(aProgress);
	}

	@Override
	public void setProgress(int currVal, int maxVal)
	{
		for (Task aTask : childTaskList)
			aTask.setProgress(currVal, maxVal);
	}

	@Override
	public void setRefreshRateMs(long aRateMs)
	{
		for (Task aTask : childTaskList)
			aTask.setRefreshRateMs(aRateMs);
	}

	@Override
	public void setStatus(String aStatus)
	{
		for (Task aTask : childTaskList)
			aTask.setStatus(aStatus);
	}

	@Override
	public void setTabSize(int numSpaces)
	{
		for (Task aTask : childTaskList)
			aTask.setTabSize(numSpaces);
	}

	@Override
	public void setTitle(String aTitle)
	{
		for (Task aTask : childTaskList)
			aTask.setTitle(aTitle);
	}

	@Override
	public boolean isActive()
	{
		return childTaskList.get(0).isActive();
	}

}
