package glum.zio.util;

import glum.task.Task;
import glum.unit.TimeCountUnit;
import glum.zio.FileZinStream;
import glum.zio.FileZoutStream;
import glum.zio.ZinStream;
import glum.zio.ZoutStream;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.io.IOException;

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
		aTask.infoAppendln(message);
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
		aTask.infoAppendln(message);
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
		byteVal = (byte)0x00FF;
		if (aValue >= 0 && aValue < 255)
			byteVal = (byte)(0x00FF & aValue);
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
			aStream.writeByte((byte)0);
			return;
		}

		aStream.writeByte((byte)1);

		byteVal = (byte)(0x00FF & aColor.getRed());
		aStream.writeByte(byteVal);

		byteVal = (byte)(0x00FF & aColor.getGreen());
		aStream.writeByte(byteVal);

		byteVal = (byte)(0x00FF & aColor.getBlue());
		aStream.writeByte(byteVal);
	}

}
