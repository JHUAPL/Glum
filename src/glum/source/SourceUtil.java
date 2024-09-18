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
package glum.source;

import java.io.File;
import java.io.IOException;
import java.net.*;

import glum.net.*;
import glum.task.Task;
import glum.unit.ByteUnit;
import glum.unit.NumberUnit;

/**
 * Collection of utility methods for working with {@link Source} objects.
 *
 * @author lopeznr1
 */
public class SourceUtil
{
	/**
	 * Utility method that will download the contents of the {@link Source}.
	 * <p>
	 * The contents will be downloaded from the remote location to local file.
	 *
	 * @param aTask
	 *        {@link Task} used to monitor or abort the download process.
	 * @param aSource
	 *        {@link Source} to be downloaded.
	 * @param aCredential
	 *        User {@link Credential} for the resource. May be null if credentials are not needed.
	 */
	public static void download(Task aTask, Source aSource, Credential aCredential) throws IOException, FetchError
	{
		URL remoteUrl = aSource.getRemoteUrl();
		File localFile = aSource.getLocalFile();
		long diskSize = aSource.getDiskSize();

		// Log the start of the download
		File partFile = getTempFile(aSource);
		if (partFile.exists() == false)
			aTask.logRegln("Downloading file...");
		else
			aTask.logRegln("Resuming download...");

		// Ensure the source has both a local and remote end points
		if (localFile == null)
			throw new IOException("Local file has not been specified.");
		if (remoteUrl == null)
			throw new IOException("Remote URL has not been specified.");

		// Ensure the parent folder exists
		File parentDir = localFile.getParentFile();
		parentDir.mkdirs();

		if (parentDir.exists() == false)
			throw new IOException("Parent folder does not exist! \n\tFolder: " + parentDir);
		if (parentDir.canWrite() == false)
			throw new IOException("Parent folder is read only! \n\tFolder: " + parentDir);

		// Fetch the remote file
		boolean isPass;
		try
		{
			// Delegate the actual download
			String updateMsg = "\tProgress: ";
			isPass = NetUtil.download(aTask, remoteUrl, partFile, aCredential, diskSize, null, updateMsg);
		}
		catch (FetchError aExp)
		{
			aTask.logRegln("File download has failed...");

			Result tmpResult = aExp.getResult();
			switch (tmpResult)
			{
				case BadCredentials:
					aTask.logRegln("\tReason: The site is password protected and bad credentials were provided.\n");
					break;

				case ConnectFailure:
				case UnreachableHost:
				case UnsupportedConnection:
					aTask.logRegln("\tReason: The site appears to be unreachable.\n");
					break;

				case Interrupted:
					aTask.logRegln("\tReason: The retrival of the remote file has been interrupted.\n");
					break;

				case InvalidResource:
					aTask.logRegln("\tReason: The remote file does not appear to be valid..\n");
					break;

				default:
					aTask.logRegln("\tReason: Unknown ---> " + tmpResult + "..\n");
					break;
			}

			// Rethrow the error
			throw aExp;
		}

		// Log the results
		String resultMsg = "\tDownload has been completed.\n";
		if (isPass == false)
		{
			resultMsg = "\tDownload has failed!\n";
			if (aTask.isAborted() == true)
				resultMsg = "\tDownload has been aborted.\n";
			aTask.abort();
		}
		aTask.logRegln(resultMsg);

		// Move the (completed) tmpFile to the proper location
		if (isPass == true)
			partFile.renameTo(localFile);
	}

	/**
	 * Forms a {@link Source} relevant to the specified parameters.
	 *
	 * @param aCacheDir
	 *        The folder where content will be cached
	 * @param aBasePath
	 *        The base path of the source
	 * @param aTargPath
	 *        The path to the resource. If basePath is null then this will be interpreted as absolute.
	 * @param aDiskSize
	 *        The disk size in bytes of the resource.
	 *
	 * @throws MalformedURLException
	 */
	public static Source formSource(File aCacheDir, String aBasePath, String aTargPath, long aDiskSize)
			throws MalformedURLException
	{
		if (aBasePath != null)
		{
			if (aBasePath.startsWith("http") == true)
			{
				File localFile = null;
				if (aCacheDir != null)
					localFile = new File(aCacheDir, aTargPath);

				if (aBasePath.endsWith("/") == false)
					aBasePath += "/";

				URL remotePath = new URL(aBasePath + aTargPath);
				return new PlainSource(localFile, remotePath, aDiskSize);
			}

			return new LocalSource(new File(aBasePath, aTargPath));
		}

		return new LocalSource(new File(aTargPath));
	}

	/**
	 * Utility method that returns the file that should be used as the temporal file when it is being fetched. Currently
	 * this just appends the text ".part" to the file.
	 */
	public static File getTempFile(Source aSource)
	{
		File localFile = aSource.getLocalFile();
		if (localFile == null)
			return null;

		File retFile = new File(localFile.getParentFile(), localFile.getName() + ".part");
		return retFile;
	}

	/**
	 * Utility method that returns the base path associated with the specified {@link Source}.
	 * <p>
	 * If both the remote {@link URL} and local {@link File} are specified then the base path will be relative to the
	 * remote {@link URL}.
	 */
	public static String getBasePath(Source aSource)
	{
		URL remoteUrl = aSource.getRemoteUrl();
		if (remoteUrl != null)
		{
			try
			{
				return "" + remoteUrl.toURI().resolve(".");
			}
			catch (URISyntaxException aExp)
			{
				throw new RuntimeException(aExp);
			}
		}

		File localFile = aSource.getLocalFile();
		return localFile.getParent();
	}

	/**
	 * Returns the {@link SourceState} associated with the source.
	 * <p>
	 * Depending on underlying file system changes the returned state may out dated.
	 */
	public static SourceState getState(Source aSource)
	{
		File localFile = aSource.getLocalFile();
		if (localFile != null && localFile.isFile() == true)
			return SourceState.Local;

		File partFile = getTempFile(aSource);
		if (partFile != null && partFile.isFile() == true)
			return SourceState.Partial;

		URL remoteUrl = aSource.getRemoteUrl();
		if (remoteUrl != null)
			return SourceState.Remote;

		return SourceState.Unavailable;
	}

	/**
	 * Utility method that returns a detailed message describing the overall status of the {@link Source}.
	 */
	public static String getStatusMsg(Source aSource)
	{
		URL remoteUrl = aSource.getRemoteUrl();
		File localFile = aSource.getLocalFile();
		long diskSize = aSource.getDiskSize();

		File partFile = getTempFile(aSource);
		double perDone = partFile.length() / (diskSize + 0.0);

		NumberUnit perNU = new NumberUnit("", "", 1.0, "0.00 %");
		ByteUnit tmpBU = new ByteUnit(2);

		// Log the header
		String retMsg = "File has not been fetched.";
		if (remoteUrl == null)
			retMsg = "File is local.";
		else if (localFile.exists() == true)
			retMsg = "File has been downloaded and is local.";
		else if (partFile.exists() == true)
		{
			retMsg = "File has been partially downloaded. [Fetched: " + perNU.getString(perDone) + "]";
			if (diskSize < 0)
				retMsg = "File has been partially downloaded. [Fetched: " + tmpBU.getString(partFile.length()) + "]";
		}
		else
			retMsg = "File is remote. A download will be required.";

		// Log the stats
		retMsg += "\n";
		if (remoteUrl != null)
		{
			retMsg += "\tSRC: " + remoteUrl + "\n";
			retMsg += "\tDST: " + localFile + "\n";
		}
		else
		{
			retMsg += "\tName: " + localFile.getName() + "\n";
			retMsg += "\tPath: " + localFile.getParent() + "\n";
		}
		if (diskSize > 0)
			retMsg += "\tSize: " + tmpBU.getString(diskSize) + "\n";

		return retMsg;
	}

}
