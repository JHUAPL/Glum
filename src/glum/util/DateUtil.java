package glum.util;

import java.util.*;

public class DateUtil
{
	public final static int SECONDS_IN_DAY = 60 * 60 * 24;
	public final static int MINUTES_IN_DAY = 60 * 24;

	/**
	 * Returns the date as a double that best describes the currDate position relative to startDate and endDate.
	 * <P>
	 * Return should be between 0 - 1.
	 */
	public static double computeFractionalPosition(Calendar startDate, Calendar currDate, Calendar endDate)
	{
		double currTimeSpan, totTimeSpan;

		// Insanity checks
		if (startDate == null || currDate == null || endDate == null)
			return 0;

		if (startDate.getTime().after(endDate.getTime()) == true)
			return 0;

		if (currDate.getTime().before(startDate.getTime()) == true)
			return 0;

		if (currDate.getTime().after(endDate.getTime()) == true)
			return 1;

		totTimeSpan = getTotalSeconds(startDate, endDate);
		currTimeSpan = getTotalSeconds(startDate, currDate);

		return currTimeSpan / totTimeSpan;
	}

	/**
	 * Returns the date closest to fracPos relative to sDate and eDate. Note fracPos should range from 0-1
	 */
	public static Calendar computeDate(Calendar sDate, Calendar eDate, double fracPos)
	{
		Calendar currDate;
		long offsetSecs;
		int deltaMins, deltaSecs;

		// Compute the number of seconds to add
		offsetSecs = (long)(getTotalSeconds(sDate, eDate) * fracPos);

		// Compute delta secs and delta min
		deltaMins = (int)(offsetSecs / 60);
		deltaSecs = (int)(offsetSecs - (deltaMins * 60));

		// Construct the new target calendar
		currDate = (Calendar)sDate.clone();
		currDate.add(Calendar.MINUTE, deltaMins);
		currDate.add(Calendar.SECOND, deltaSecs);

		return currDate;
	}

	/**
	 * Returns the total seconds between the start date and end date
	 */
	public static long getTotalSeconds(Calendar sDate, Calendar eDate)
	{
		Calendar tmpDate;
		int sYear, eYear, cYear;
		int sDay, eDay;
		int sHour, eHour;
		int sMin, eMin;
		int sSec, eSec;
		long daysLeftStart, daysTillEnd, totNumFullDays;
		long secsLeftStart, secsTillStart, secsTillEnd, totNumSecs;

		// Insanity check
		if (sDate == null || eDate == null)
			return 0;

		// Compute the various components of the start and end date
		sYear = sDate.get(Calendar.YEAR);
		eYear = eDate.get(Calendar.YEAR);
		sDay = sDate.get(Calendar.DAY_OF_YEAR);
		eDay = eDate.get(Calendar.DAY_OF_YEAR);
		sHour = sDate.get(Calendar.HOUR_OF_DAY);
		eHour = eDate.get(Calendar.HOUR_OF_DAY);
		sMin = sDate.get(Calendar.MINUTE);
		eMin = eDate.get(Calendar.MINUTE);
		sSec = sDate.get(Calendar.SECOND);
		eSec = eDate.get(Calendar.SECOND);

		// Get the days left in the year of the current startDate
		daysLeftStart = sDate.getActualMaximum(Calendar.DAY_OF_YEAR) - sDay;

		// Get the days that have passed in the year of the endDate
		daysTillEnd = eDay - eDate.getActualMinimum(Calendar.DAY_OF_YEAR);

		// Add the days to the total number of days
		if (sYear == eYear)
			totNumFullDays = (eDay - sDay) - 1;
		else
			totNumFullDays = daysLeftStart + daysTillEnd;

		// Add the total number of days from the years in between the start and end date
		tmpDate = new GregorianCalendar(sYear, 0, 1);
		for (cYear = sYear + 1; cYear < eYear; cYear++)
		{
			tmpDate.set(Calendar.YEAR, cYear);
			totNumFullDays += tmpDate.getActualMaximum(Calendar.DAY_OF_YEAR);
		}

		// Get the number of seconds left in the current day of the startDate
		secsLeftStart = SECONDS_IN_DAY - (sHour * 3600 + sMin * 60 + sSec);

		// Get the number of seconds that have passed in the current day of the startDate
		secsTillStart = sHour * 3600 + sMin * 60 + sSec;

		// Get the number of seconds that have passed in the current day of the endDate
		secsTillEnd = eHour * 3600 + eMin * 60 + eSec;

		// Compute the total number of full seconds
		if (sYear == eYear && sDay == eDay)
			totNumSecs = secsTillEnd - secsTillStart;
		else
			totNumSecs = secsLeftStart + secsTillEnd + (totNumFullDays * SECONDS_IN_DAY);

		return totNumSecs;
	}

	/**
	 * Returns aDate if it is bounded by sDate and eDate; else it returns the nearest boundary date.
	 */
	public static Calendar verifyDateBounds(Calendar sDate, Calendar eDate, Calendar aDate)
	{
		Date sTime, eTime, aTime;

		if (sDate == null || eDate == null || aDate == null)
			return aDate;

		sTime = sDate.getTime();
		eTime = eDate.getTime();
		aTime = aDate.getTime();

		if (aTime.compareTo(sTime) < 0)
			return (Calendar)sDate.clone();
		else if (aTime.compareTo(eTime) > 0)
			return (Calendar)eDate.clone();

		return aDate;
	}

}
