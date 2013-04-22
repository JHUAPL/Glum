package glum.util;

public class WallTimer
{
	private long startTime, stopTime;

	public WallTimer()
	{
		this(false);
	}

	public WallTimer(boolean startNow)
	{
		startTime = 0;
		stopTime = 0;

		if (startNow == true)
			start();
	}

	/**
	 * Returns the start time
	 */
	public long getStartTime()
	{
		return startTime;
	}

	/**
	 * Returns the stop time
	 */
	public long getStopTime()
	{
		return stopTime;
	}

	/**
	 * Starts the timer
	 */
	public void start()
	{
		startTime = System.currentTimeMillis();
		stopTime = 0;
	}

	/**
	 * Stops the timer
	 */
	public void stop()
	{
		stopTime = System.currentTimeMillis();
	}

	/**
	 * Returns the total number of seconds since the timer has been started. If it has been stopped then the returned
	 * value is the total time between start and stop.
	 * 
	 * TODO: Phase this method out and use getTotal()
	 */
	public double getTotalSec()
	{
		return getTotal() / 1000.0;
	}

	/**
	 * Returns the total number of milliseconds since the timer has been started. If it has been stopped then the
	 * returned value is the total time between start and stop.
	 */
	public long getTotal()
	{
		long aTime;

		// Return 0 if we have not been started
		if (startTime == 0)
			return 0;

		// Return the total since stopped
		if (stopTime != 0)
			return stopTime - startTime;

		// If the timer has not been stopped then return
		// the current running time
		aTime = System.currentTimeMillis();
		return aTime - startTime;
	}

}
