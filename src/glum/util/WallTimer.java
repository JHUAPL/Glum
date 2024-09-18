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
