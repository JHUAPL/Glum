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
package glum.net;

import java.io.*;
import java.net.*;
import java.security.DigestInputStream;
import java.security.MessageDigest;
import java.util.Base64;

import glum.digest.Digest;
import glum.digest.DigestUtils;
import glum.io.IoUtil;
import glum.task.ConsoleTask;
import glum.task.Task;
import glum.unit.NumberUnit;

/**
 * Collection of utility methods for working with remote resources.
 *
 * @author lopeznr1
 */
public class NetUtil
{
	/**
	 * Utility method to download the specified file from aSrcUrl to aDestFile. Returns true on success.
	 * <p>
	 * Note the passed in {@link Task}'s progress will be updated from 0% to 100% at file download completion, If the
	 * specified parameter aFileSize is invalid (aFileSize <= 0) or the download turns out to be bigger than the
	 * specified size then there will be no progress update while the file is being downloaded - only at completion.
	 * <p>
	 * This method can be aborted via {@link Task#abort()}.
	 * <p>
	 * <p>
	 * Returns true if the download is successfully completed. Returns false if the provided task is aborted.
	 */
	public static boolean download(Task aTask, URL aSrcUrl, File aDstFile, Credential aCredential, long aFileSize,
			MessageDigest aDigest, String aUpdateMsg) throws FetchError
	{

		NumberUnit perNU = new NumberUnit("", "", 1.0, "0.00 %");

		// Ensure we have a valid aTask
		if (aTask == null)
			aTask = new ConsoleTask();

		// Allocate space for the byte buffer
		byte[] byteArr = new byte[10000];

		// Determine if the file download should be "resumed"
		long cntByteCurr = 0L;
		if (aDstFile.exists() == true)
			cntByteCurr = aDstFile.length();

		// Perform the actual copying
		InputStream inStream = null;
		OutputStream outStream = null;
		URLConnection connection = null;
		try
		{
			// Open the src stream (with a 30 sec connect timeout)
			connection = aSrcUrl.openConnection();
			if (cntByteCurr > 0)
				connection.setRequestProperty("Range", "bytes=" + cntByteCurr + "-");
//			connection.setConnectTimeout(30 * 1000);
//			connection.setReadTimeout(90 * 1000);

			// Open the InputStream
			inStream = NetUtil.getInputStream(connection, aCredential);
			if (aDigest != null)
				inStream = new DigestInputStream(inStream, aDigest);

			// Open the OutputStream
			boolean isAppend = false;
			if (cntByteCurr > 0)
				isAppend = true;
			outStream = new FileOutputStream(aDstFile, isAppend);

			// Copy the bytes from the instream to the outstream
			long cntByteFull = aFileSize;
			int numBytes = 0;
			while (numBytes != -1)
			{
				numBytes = inStream.read(byteArr);
				if (numBytes > 0)
				{
					outStream.write(byteArr, 0, numBytes);
					cntByteCurr += numBytes;
				}

				// Update the progressVal to reflect the download progress. Note however that we do update the
				// progress to 100% since that would change the task to be flagged as inactive and thus cause
				// the download to be aborted prematurely.
				// TODO: In the future Tasks should not be marked as inactive based on progress values
				double progressVal = 0;
				if (cntByteFull > 0)
				{
					progressVal = (cntByteCurr + 0.0) / cntByteFull;
					if (progressVal >= 1.0)
						progressVal = 0.99;
				}

				if (aUpdateMsg != null)
					aTask.logRegUpdate(aUpdateMsg + perNU.getString(progressVal) + "\n");
				aTask.setProgress(progressVal);

				// Bail if the Task has been aborted
				if (aTask.isAborted() == true)
					return false;
			}

			// Mark aTask's progress as complete since the file was downloaded.
			if (aUpdateMsg != null)
				aTask.logRegUpdate(aUpdateMsg + perNU.getString(1.0) + "\n");
			aTask.setProgress(1.0);
		}
		catch (IOException aExp)
		{
			Result tmpResult = getResult(aExp, connection);
			throw new FetchError(aExp, tmpResult);
		}
		finally
		{
			IoUtil.forceClose(inStream);
			IoUtil.forceClose(outStream);
		}

		return true;
	}

	/**
	 * Utility method to download the specified file from aSrcUrl to aDestFile. Returns true on success.
	 * <p>
	 * Note the passed in {@link Task}'s progress will be updated from 0% to 100% at file download completion, If the
	 * specified parameter aFileSize is invalid (aFileSize <= 0) or the download turns out to be bigger than the
	 * specified size then there will be no progress update while the file is being downloaded - only at completion.
	 * <p>
	 * On any error logging will be sent to the provided Task.
	 * <p>
	 * Validation of the download will be performed if a valid {@link Digest} (aTargDigest) is provided.
	 * <p>
	 * This method can be aborted via {@link Task#abort()}.
	 * <p>
	 * Returns true if the download is successfully completed. Returns false if the provided task is aborted.
	 */
	public static boolean download(Task aTask, URL aSrcUrl, File aDstFile, Credential aCredential, long aFileLen,
			Digest aTargDigest)
	{
		// Form the message digest of interest
		MessageDigest tmpMessageDigest = null;
		if (aTargDigest != null)
			tmpMessageDigest = DigestUtils.getDigest(aTargDigest.getType());

		try
		{
			NetUtil.download(aTask, aSrcUrl, aDstFile, aCredential, aFileLen, tmpMessageDigest, null);
			if (aTask.isAborted() == true)
			{
				aTask.logRegln("File download has been aborted...");
				aTask.logRegln("\tSource: " + aSrcUrl);
				aTask.logRegln("\tFile: " + aDstFile + "\n");
//				aTask.logRegln("\tFile: " + dstFile + " Bytes transferred: " + cntByteCurr);
				return false;
			}
		}
		catch (FetchError aExp)
		{
			aTask.logRegln("File download has failed...");
			aTask.logRegln("\tReason: " + aExp.getResult());
			aTask.logRegln("\tSource: " + aSrcUrl);
			aTask.logRegln("\tFile: " + aDstFile + "\n");
			return false;
		}

		// Success if there is no targDigest to validate against
		if (aTargDigest == null)
			return true;

		// Validate that the file was downloaded successfully
		Digest testDigest = new Digest(aTargDigest.getType(), tmpMessageDigest.digest());
		if (aTargDigest.equals(testDigest) == false)
		{
			aTask.logRegln("File download is corrupted...");
			aTask.logRegln("\tFile: " + aDstFile);
			aTask.logRegln("\t\tExpected " + aTargDigest.getDescr());
			aTask.logRegln("\t\tReceived " + testDigest.getDescr() + "\n");
			return false;
		}

		return true;
	}

	/**
	 * Utility method that converts an IOException to an understandable message
	 */
	public static String getErrorCodeMessage(URL aUpdateUrl, URLConnection aConnection, IOException aExp)
	{
		// Form a user friendly exception
		String errMsg;
		errMsg = "The update site, " + aUpdateUrl + ", is not available.\n\t";

		Result result;
		result = NetUtil.getResult(aExp, aConnection);
		switch (result)
		{
			case BadCredentials:
				errMsg += "The update site is password protected and bad credentials were provided.\n";
				break;

			case ConnectFailure:
			case UnreachableHost:
			case UnsupportedConnection:
				errMsg += "The update site appears to be unreachable.\n";
				break;

			case Interrupted:
				errMsg += "The retrival of the remote file has been interrupted.\n";
				break;

			case InvalidResource:
				errMsg += "The remote file does not appear to be valid.\n";
				break;

			default:
				errMsg += "An undefined error occurred while retrieving the remote file.\n";
				break;
		}

		// Log the URL which we failed on
		URL fetchUrl;
		fetchUrl = aConnection.getURL();
		errMsg += "\tURL: " + fetchUrl + "\n";

		return errMsg;
	}

	/**
	 * Utility method for retrieving the response code
	 */
	public static int getResponseCode(HttpURLConnection aConnection)
	{
		try
		{
			return aConnection.getResponseCode();
		}
		catch (IOException aExp)
		{
			return -1;
		}
	}

	/**
	 * Utility method to return the input stream associated with a URL
	 */
	public static InputStream getInputStream(URLConnection aConnection, Credential aCredential) throws IOException
	{
		// Properly setup the credentials
		if (aCredential != null)
		{
			String username = aCredential.getUsername();
			String password = aCredential.getPasswordAsString();
			String authStr = username + ":" + password;
			authStr = Base64.getEncoder().encodeToString(authStr.getBytes());
			aConnection.setRequestProperty("Authorization", "Basic " + authStr);
		}

		// Retrieve the InputStream
		aConnection.connect();
		InputStream inStream = aConnection.getInputStream();

		return inStream;
	}

	/**
	 * Utility method to return the input stream associated with a URL
	 */
	public static InputStream getInputStream(URL aUrl, Credential aCredential) throws IOException
	{
		return getInputStream(aUrl.openConnection(), aCredential);
	}

	/**
	 * Utility method for retrieving a more detailed result associated with an exception that occured on a URLConnection
	 */
	public static Result getResult(Exception aExp, URLConnection aConnection)
	{
		// See if there was a problem with the HTTP Connection
		if (aConnection instanceof HttpURLConnection)
		{
			int responseCode = getResponseCode((HttpURLConnection) aConnection);
			switch (responseCode)
			{
				case HttpURLConnection.HTTP_UNAUTHORIZED:
					return Result.BadCredentials;

				case HttpURLConnection.HTTP_UNSUPPORTED_TYPE:
					return Result.UnsupportedConnection;

				case HttpURLConnection.HTTP_NOT_FOUND:
				case HttpURLConnection.HTTP_NO_CONTENT:
					return Result.InvalidResource;

//				case HttpURLConnection.HTTP_UNAVAILABLE:
//					return Result.UnreachableHost;

				default:
					break;
			}
		}

		// Evaluate the Exception
		Throwable tmpCause = aExp;
		while (tmpCause != null)
		{
			if (tmpCause instanceof UnknownHostException)
				return Result.UnreachableHost;
			else if (tmpCause instanceof ConnectException)
				return Result.ConnectFailure;

			tmpCause = tmpCause.getCause();
		}

		return Result.Undefined;

	}

	/**
	 * Checks to see whether the supplied aCredential is valid for the specified root URI.
	 *
	 * @return The result of testing the credentials.
	 */
	public static Result checkCredentials(String uriRoot, Credential aCredential)
	{
		URLConnection aConnection = null;
		InputStream inStream = null;
		try
		{
			URL srcURL = new URL(uriRoot);

			String username = aCredential.getUsername();
			String password = aCredential.getPasswordAsString();
			String authStr = username + ":" + password;
			authStr = Base64.getEncoder().encodeToString(authStr.getBytes());
			aConnection = srcURL.openConnection();
			aConnection.setRequestProperty("Authorization", "Basic " + authStr);
			aConnection.connect();

			// Try to open the connection
//			fullLen = aConnection.getContentLength();
			inStream = new BufferedInputStream(aConnection.getInputStream());
			inStream.close();
			return Result.Success;
		}
		catch (Exception aExp)
		{
//			aExp.printStackTrace();
			return getResult(aExp, aConnection);
		}
	}

}
