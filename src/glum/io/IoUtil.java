package glum.io;

import glum.task.*;
import glum.zio.ZinStream;
import glum.zio.ZoutStream;

import java.io.*;
import java.net.*;
import java.nio.channels.Channel;
import java.util.Base64;
import java.util.Map;

import com.google.common.collect.Maps;

public class IoUtil
{
	/**
	 * Creates a URL without throwing an exception. Returns null on failure
	 */
	public static URL createURL(String aUrlStr)
	{
		URL retURL;

		try
		{
			retURL = new URL(aUrlStr);
		}
		catch(MalformedURLException e)
		{
			retURL = null;
		}

		return retURL;
	}

	/**
	 * Prints out the error msg followed by the stack trace.
	 */
	public static void dumpTrace(Exception aExp, String aMsg)
	{
		System.out.println(aMsg);
		aExp.printStackTrace();
	}

	/**
	 * Forces aChannel to be closed (silently)
	 */
	public static void forceClose(Channel aChannel)
	{
		if (aChannel == null)
			return;

		try
		{
			aChannel.close();
		}
		catch(Exception aExp)
		{
			aExp.printStackTrace();
		}
	}

	/**
	 * Forces an InputStream to be closed (silently)
	 */
	public static void forceClose(InputStream aStream)
	{
		if (aStream == null)
			return;

		try
		{
			aStream.close();
		}
		catch(Exception aExp)
		{
			aExp.printStackTrace();
		}
	}

	/**
	 * Forces an OutputStream to be closed (silently)
	 */
	public static void forceClose(OutputStream aStream)
	{
		if (aStream == null)
			return;

		try
		{
			aStream.close();
		}
		catch(Exception aExp)
		{
			aExp.printStackTrace();
		}
	}

	/**
	 * Forces a Reader to be closed (silently)
	 */
	public static void forceClose(Reader aReader)
	{
		if (aReader == null)
			return;

		try
		{
			aReader.close();
		}
		catch(Exception aExp)
		{
			aExp.printStackTrace();
		}
	}

	/**
	 * Forces a Writer to be closed (silently)
	 */
	public static void forceClose(Writer aWriter)
	{
		if (aWriter == null)
			return;

		try
		{
			aWriter.close();
		}
		catch(Exception aExp)
		{
			aExp.printStackTrace();
		}

	}

	/**
	 * Forces a ZinStream to be closed (silently)
	 */
	public static void forceClose(ZinStream aStream)
	{
		if (aStream == null)
			return;

		try
		{
			aStream.close();
		}
		catch(Exception aExp)
		{
			aExp.printStackTrace();
		}
	}

	/**
	 * Forces a ZoutStream to be closed (silently)
	 */
	public static void forceClose(ZoutStream aStream)
	{
		if (aStream == null)
			return;

		try
		{
			aStream.close();
		}
		catch(Exception aExp)
		{
			aExp.printStackTrace();
		}
	}

	/**
	 * Downloads the content at aUrl and saves it to aFile. Before the file is downloaded, the connection is configured
	 * with the specified property map. The download will be aborted if aTask is no longer active.
	 */
	public static boolean copyUrlToFile(Task aTask, URL aUrl, File aFile, Map<String, String> aPropertyMap)
	{
		// Ensure we have a valid aTask
		if (aTask == null)
			aTask = new ConsoleTask();

		// Allocate space for the byte buffer
		byte[] byteArr = new byte[10000];

		// Perform the actual copying
		InputStream inStream = null;
		OutputStream outStream = null;
		try
		{
			// Open the src stream (with a 30 sec connect timeout)
			URLConnection aConnection = aUrl.openConnection();
			aConnection.setConnectTimeout(30 * 1000);
			aConnection.setReadTimeout(90 * 1000);

			// Setup the various properties
			if (aPropertyMap != null)
			{
				for (String aKey : aPropertyMap.keySet())
					aConnection.setRequestProperty(aKey, aPropertyMap.get(aKey));
			}

			inStream = aConnection.getInputStream();
//			inStream = aUrl.openStream();

			// Open the dest stream
			outStream = new FileOutputStream(aFile);

			// Copy the bytes from the instream to the outstream
			int numBytes = 0;
			while (numBytes != -1)
			{
				numBytes = inStream.read(byteArr);
				if (numBytes > 0)
					outStream.write(byteArr, 0, numBytes);

				// Bail if aTask is aborted
				if (aTask.isActive() == false)
				{
					aTask.infoAppendln("Download of file: " + aFile + " has been aborted!");
					return false;
				}
			}
		}
		catch(Exception aExp)
		{
			aTask.infoAppendln("Exception:" + aExp);
			aTask.infoAppendln("   URL:" + aUrl);
			aTask.infoAppendln("   File:" + aFile);
			aExp.printStackTrace();
			return false;
		}
		finally
		{
			forceClose(inStream);
			forceClose(outStream);
		}

		return true;
	}

	/**
	 * Downloads the content at aUrl and saves it to aFile. Before the file is downloaded, the connection is configured
	 * with the specified property map.
	 */
	public static boolean copyUrlToFile(URL aUrl, File aFile, Map<String, String> aPropertyMap)
	{
		return copyUrlToFile(null, aUrl, aFile, aPropertyMap);
	}

	/**
	 * Downloads the content at aUrl and saves it to aFile
	 */
	public static boolean copyUrlToFile(URL aUrl, File aFile)
	{
		return copyUrlToFile(aUrl, aFile, null);
	}

	/**
	 * Downloads the content at aUrl and saves it to aFile. Before the file is downloaded, the connection is configured
	 * for basic authorization using the provided credentials (aUsername and aPassword).
	 */
	public static boolean copyUrlToFile(Task aTask, URL aUrl, File aFile, String aUsername, String aPassword)
	{
		String authStr = aUsername + ":" + aPassword;
		authStr = Base64.getEncoder().encodeToString(authStr.getBytes());

		Map<String, String> plainMap = Maps.newHashMap();
		plainMap.put("Authorization", "Basic " + authStr);

		return copyUrlToFile(aTask, aUrl, aFile, plainMap);
	}

	/**
	 * Downloads the content at aUrl and saves it to aFile. Before the file is downloaded, the connection is configured
	 * for basic authorization using the provided credentials (aUsername and aPassword).
	 */
	public static boolean copyUrlToFile(URL aUrl, File aFile, String aUsername, String aPassword)
	{
		return copyUrlToFile(null, aUrl, aFile, aUsername, aPassword);
	}

	/**
	 * Copies the content at aFile1 and saves it to aFile2
	 */
	public static boolean copyFileToFile(File aFile1, File aFile2)
	{
		URL tmpUrl;
		try
		{
			tmpUrl = aFile1.toURI().toURL();
		}
		catch(Exception aExp)
		{
			System.out.println("Exception:" + aExp);
			aExp.printStackTrace();
			return false;
		}

		return copyUrlToFile(tmpUrl, aFile2);
	}

	/**
	 * Method to recursively delete all of the contents located in the specified directory.
	 * <P>
	 * Source: http://stackoverflow.com/questions/3775694/deleting-folder-from-java
	 * <P>
	 */
	public static boolean deleteDirectory(File directory)
	{
		if (directory.exists())
		{
			File[] files = directory.listFiles();
			if (null != files)
			{
				for (int i = 0; i < files.length; i++)
				{
					if (files[i].isDirectory())
					{
						deleteDirectory(files[i]);
					}
					else
					{
						files[i].delete();
					}
				}
			}
		}
		return (directory.delete());
	}

	/**
	 * Reads an 8-bit string from aStream. First 2 bytes specify length of string.
	 */
	public static String readString(DataInputStream aStream) throws IOException
	{
		int size = aStream.readShort() & 0x00FFFF;
		if (size == 0x00FFFF)
			return null;
		if (size == 0)
			return "";

		byte[] data = new byte[size];
		aStream.readFully(data);
		return new String(data, "UTF-8");
	}

	/**
	 * Writes an 8-bit string to aStream. First 2 bytes specify length of string.
	 */
	public static void writeString(DataOutputStream aStream, String aStr) throws IOException
	{
		// Null strings are handled in special fashion
		if (aStr == null)
		{
			aStream.writeShort(0x00FFFF);
			return;
		}

		// Empty strings are handled in special fashion
		if (aStr.equals("") == true)
		{
			aStream.writeShort(0);
			return;
		}

		byte[] data = aStr.getBytes("UTF-8");
		int size = data.length;

		// Ensure the string size is less than 0x00FFFF
		if (size >= 0x00FFFF)
			throw new RuntimeException("Transformed UTF-8 string is too large! Max size: " + (0x00FFFF - 1) + "  Curr size:" + size);

		// Write out the string
		aStream.writeShort(size & 0x00FFFF);
		aStream.write(data);
	}

	/**
	 * Calculates the raw space needed to store an 8-bit string.
	 */
	public static short sizeOnDisk(String aStr)
	{
		if (aStr == null || aStr.equals("") == true)
			return 2;

		byte[] data;
		int size;
		try
		{
			data = aStr.getBytes("UTF-8");
			size = data.length;
		}
		catch(Exception aExp)
		{
			throw new RuntimeException("UTF-8 Transform error.", aExp);
		}

		if (size >= 0x00FFFF)
			throw new RuntimeException("Transformed UTF-8 string is too large! Max size: " + (0x00FFFF - 1) + "  Curr size:" + size);

		return (short)(2 + data.length);
	}

}
