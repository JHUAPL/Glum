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
package glum.io;

import java.io.*;
import java.net.*;
import java.nio.channels.Channel;
import java.util.*;

import glum.task.ConsoleTask;
import glum.task.Task;
import glum.zio.ZinStream;
import glum.zio.ZoutStream;

/**
 * Collection of various utility methods.
 *
 * @author lopeznr1
 */
public class IoUtil
{
	/**
	 * Creates a URL without throwing an exception. Returns null on failure
	 */
	public static URL createURL(String aUrlStr)
	{
		try
		{
			return new URL(aUrlStr);
		}
		catch (MalformedURLException e)
		{
			return null;
		}
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
		catch (Exception aExp)
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
		catch (Exception aExp)
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
		catch (Exception aExp)
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
		catch (Exception aExp)
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
		catch (Exception aExp)
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
		catch (Exception aExp)
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
		catch (Exception aExp)
		{
			aExp.printStackTrace();
		}
	}

	/**
	 * Utility helper that will locate the next available {@link File} with a name closest to that of aFile.
	 * <p>
	 * The next available {@link File} is defined as a file that does not exist.
	 * <p>
	 * Names will be searched by appending an increasing index (starting from zero) onto the file name until a
	 * {@link File} is located that does not exist (on the local file system).
	 */
	public static File locateNextAvailableFile(File aFile)
	{
		// If the File does not exist then just utilize it as is
		if (aFile.exists() == false)
			return aFile;

		// Extract the path's components of interest
		File basePath = aFile.getParentFile();
		String baseName = aFile.getName();
		String fileExt = "";

		String tmpFileName = aFile.getName();
		int tmpIdx = tmpFileName.lastIndexOf(".");
		if (tmpIdx != -1 && tmpIdx != tmpFileName.length() - 1)
		{
			baseName = tmpFileName.substring(0, tmpIdx);
			fileExt = tmpFileName.substring(tmpIdx);
		}

		int currIdx = 0;
		while (true)
		{
			File tmpFile = new File(basePath, baseName + "." + currIdx + fileExt);
			if (tmpFile.exists() == false)
				return tmpFile;

			currIdx++;
		}
	}

	/**
	 * Downloads the content at aUrl and saves it to aFile. Before the file is downloaded, the connection is configured
	 * with the specified property map. The download will be aborted if aTask is no longer active.
	 */
	public static boolean copyUrlToFile(Task aTask, URL aUrl, File aFile, Map<String, String> aPropertyM)
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
			if (aPropertyM != null)
			{
				for (String aKey : aPropertyM.keySet())
					aConnection.setRequestProperty(aKey, aPropertyM.get(aKey));
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
					aTask.logRegln("Download of file: " + aFile + " has been aborted!");
					return false;
				}
			}
		}
		catch (Exception aExp)
		{
			aTask.logRegln("Exception:" + aExp);
			aTask.logRegln("   URL:" + aUrl);
			aTask.logRegln("   File:" + aFile);
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
		var authStr = aUsername + ":" + aPassword;
		authStr = Base64.getEncoder().encodeToString(authStr.getBytes());

		var plainM = new HashMap<String, String>();
		plainM.put("Authorization", "Basic " + authStr);

		return copyUrlToFile(aTask, aUrl, aFile, plainM);
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
		catch (Exception aExp)
		{
			aExp.printStackTrace();
			return false;
		}

		return copyUrlToFile(tmpUrl, aFile2);
	}

	/**
	 * Method to recursively delete all of the contents located in the specified directory.
	 * <p>
	 * Source: http://stackoverflow.com/questions/3775694/deleting-folder-from-java
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
			throw new IOException(
					"Transformed UTF-8 string is too large! Max size: " + (0x00FFFF - 1) + "  Curr size:" + size);

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
		catch (Exception aExp)
		{
			throw new RuntimeException("UTF-8 Transform error.", aExp);
		}

		if (size >= 0x00FFFF)
			throw new RuntimeException(
					"Transformed UTF-8 string is too large! Max size: " + (0x00FFFF - 1) + "  Curr size:" + size);

		return (short) (2 + data.length);
	}

}
