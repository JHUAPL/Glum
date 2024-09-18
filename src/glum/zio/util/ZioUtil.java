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
package glum.zio.util;

import java.awt.*;
import java.io.IOException;
import java.time.*;

import glum.task.Task;
import glum.unit.TimeCountUnit;
import glum.zio.ZinStream;
import glum.zio.ZoutStream;
import glum.zio.stream.FileZinStream;
import glum.zio.stream.FileZoutStream;

/**
 * Collection of utility methods for working with zio streams ({@link ZinStream} and {@link ZoutStream}).
 *
 * @author lopeznr1
 */
public class ZioUtil
{
	/**
	 * Utility method to print the various stats gathered while reading to the specified task.
	 */
	public static void printResults(Task aTask, FileZinStream aStream, Object aSource) throws IOException
	{
		TimeCountUnit aUnit;
		long runTime;
		String checksum;
		String message;

		// Ensure we have a valid task
		if (aTask == null)
			return;

		aUnit = new TimeCountUnit(3);
		checksum = aStream.getCheckSum();
		runTime = aStream.getRunTime();
		message = "[Read] MD5: " + checksum + "   Source: " + aSource + "   Time: " + aUnit.getString(runTime);
		aTask.logRegln(message);
	}

	/**
	 * Utility method to print the various stats gathered while writing to the specified task.
	 */
	public static void printResults(Task aTask, FileZoutStream aStream, Object aSource) throws IOException
	{
		TimeCountUnit aUnit;
		long runTime;
		String checksum;
		String message;

		// Ensure we have a valid task
		if (aTask == null)
			return;

		aUnit = new TimeCountUnit(3);
		checksum = aStream.getCheckSum();
		runTime = aStream.getRunTime();
		message = "[Read] MD5: " + checksum + "   Source: " + aSource + "   Time: " + aUnit.getString(runTime);
		aTask.logRegln(message);
	}

	/**
	 * Utility method to read a compact int. A compact int will take anywhere from 1 byte to 5 bytes of storage. This
	 * method is the inverse of {@link ZioUtil#writeCompactInt}
	 */
	public static int readCompactInt(ZinStream aStream) throws IOException
	{
		int aValue;

		// Read the int
		aValue = aStream.readByte() & 0x00FF;
		if (aValue == 255)
			aValue = aStream.readInt();

		return aValue;
	}

	/**
	 * Utility method to write a compact int. A compact int will take anywhere from 1 byte to 5 bytes of storage. This
	 * method is the inverse of {@link ZioUtil#readCompactInt}
	 */
	public static void writeCompactInt(ZoutStream aStream, int aValue) throws IOException
	{
		byte byteVal;

		// Takes up one byte if in the range of (0, 254)
		byteVal = (byte) 0x00FF;
		if (aValue >= 0 && aValue < 255)
			byteVal = (byte) (0x00FF & aValue);
		aStream.writeByte(byteVal);

		// Takes up 5 bytes otherwise
		if (aValue >= 255)
			aStream.writeInt(aValue);
	}

	/**
	 * Utility method to read a {@link Dimension} from aStream. This method is the inverse of
	 * {@link ZioUtil#writeDimension}
	 */
	public static Dimension readDimension(ZinStream aStream) throws IOException
	{
		Dimension aDimension;
		boolean aBool;

		aDimension = null;
		aBool = aStream.readBool();
		if (aBool == true)
			aDimension = new Dimension(aStream.readInt(), aStream.readInt());

		return aDimension;
	}

	/**
	 * Utility method to write aDimension to aStream. The argument aDimension may be null. This method is the inverse of
	 * {@link ZioUtil#readDimension}
	 */
	public static void writeDimension(ZoutStream aStream, Dimension aDimension) throws IOException
	{
		boolean aBool;

		aBool = (aDimension != null);
		aStream.writeBool(aBool);
		if (aBool == true)
		{
			aStream.writeInt(aDimension.width);
			aStream.writeInt(aDimension.height);
		}
	}

	/**
	 * Utility method to read a {@link Point} from aStream. This method is the inverse of {@link ZioUtil#writePoint}
	 */
	public static Point readPoint(ZinStream aStream) throws IOException
	{
		Point aPoint;
		boolean aBool;

		aPoint = null;
		aBool = aStream.readBool();
		if (aBool == true)
			aPoint = new Point(aStream.readInt(), aStream.readInt());

		return aPoint;
	}

	/**
	 * Utility method to write aPoint to aStream. The argument aPoint may be null. This method is the inverse of
	 * {@link ZioUtil#readPoint}
	 */
	public static void writePoint(ZoutStream aStream, Point aPoint) throws IOException
	{
		boolean aBool;

		aBool = (aPoint != null);
		aStream.writeBool(aBool);
		if (aBool == true)
		{
			aStream.writeInt(aPoint.x);
			aStream.writeInt(aPoint.y);
		}
	}

	/**
	 * Utility method to read a {@link Color} from aStream. This method is the inverse of {@link ZioUtil#writeColor}
	 */
	public static Color readColor(ZinStream aStream) throws IOException
	{
		byte type;
		int r, g, b;

		type = aStream.readByte();
		if (type == 0)
			return null;

		r = aStream.readByte() & 0x00FF;
		g = aStream.readByte() & 0x00FF;
		b = aStream.readByte() & 0x00FF;
		return new Color(r, g, b);
	}

	/**
	 * Utility method to write aColor to aStream. The argument aColor may be null. This method is the inverse of
	 * {@link ZioUtil#readColor}
	 */
	public static void writeColor(ZoutStream aStream, Color aColor) throws IOException
	{
		byte byteVal;

		if (aColor == null)
		{
			aStream.writeByte((byte) 0);
			return;
		}

		aStream.writeByte((byte) 1);

		byteVal = (byte) (0x00FF & aColor.getRed());
		aStream.writeByte(byteVal);

		byteVal = (byte) (0x00FF & aColor.getGreen());
		aStream.writeByte(byteVal);

		byteVal = (byte) (0x00FF & aColor.getBlue());
		aStream.writeByte(byteVal);
	}

	/**
	 * Utility method to read a {@link LocalDate} from the specified stream.
	 */
	public static LocalDate readDate(ZinStream aStream) throws IOException
	{
		int ver = aStream.readVersion(0, 1);
		if (ver == 0)
			return null;

		int year = aStream.readShort();
		int dayOfYear = aStream.readShort();
		return LocalDate.ofYearDay(year, dayOfYear);
	}

	/**
	 * Utility method to write a {@link LocalDate} to the specified stream.
	 */
	public static void writeDate(ZoutStream aStream, LocalDate aDate) throws IOException
	{
		if (aDate == null)
		{
			aStream.writeVersion(0);
			return;
		}

		short year = (short) aDate.getYear();
		short dayOfYear = (short) aDate.getDayOfYear();

		aStream.writeVersion(1);
		aStream.writeShort(year);
		aStream.writeShort(dayOfYear);
	}

	/**
	 * Utility method to read a {@link LocalTime} from the specified stream.
	 */
	public static LocalTime readTime(ZinStream aStream) throws IOException
	{
		int ver = aStream.readVersion(0, 1);
		if (ver == 0)
			return null;

		int hour = aStream.readByte();
		int min = aStream.readByte();
		int sec = aStream.readByte();
		return LocalTime.of(hour, min, sec);
	}

	/**
	 * Utility method to write a {@link LocalTime} to the specified stream.
	 */
	public static void writeTime(ZoutStream aStream, LocalTime aTime) throws IOException
	{
		if (aTime == null)
		{
			aStream.writeVersion(0);
			return;
		}

		byte hour = (byte) aTime.getHour();
		byte min = (byte) aTime.getMinute();
		byte sec = (byte) aTime.getSecond();

		aStream.writeVersion(1);
		aStream.writeByte(hour);
		aStream.writeByte(min);
		aStream.writeByte(sec);
	}

	/**
	 * Utility method to read a {@link LocalDateTime}from the specified stream.
	 */
	public static LocalDateTime readDateTime(ZinStream aStream) throws IOException
	{
		int ver = aStream.readVersion(0, 1);
		if (ver == 0)
			return null;

		LocalDate tmpDate = readDate(aStream);
		LocalTime tmpTime = readTime(aStream);
		return LocalDateTime.of(tmpDate, tmpTime);
	}

	/**
	 * Utility method to write a {@link LocalDateTime} to the specified stream.
	 */
	public static void writeDateTime(ZoutStream aStream, LocalDateTime aDateTime) throws IOException
	{
		if (aDateTime == null)
		{
			aStream.writeVersion(0);
			return;
		}

		aStream.writeVersion(1);
		writeDate(aStream, aDateTime.toLocalDate());
		writeTime(aStream, aDateTime.toLocalTime());
	}

}
