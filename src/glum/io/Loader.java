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

import java.awt.Component;
import java.io.*;
import java.net.URL;
import java.util.*;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import com.google.common.collect.ImmutableList;

import glum.io.token.MatchTokenizer;
import glum.io.token.Tokenizer;
import glum.task.Task;

/**
 * Collection of utility method for loading a text file using {@link TokenProcessor}s and and a {@link Tokenizer}.
 *
 * @author lopeznr1
 */
public class Loader
{
	// Constants
	// Default regEx handles the following cases:
	// (1) 1-Liner comments started by the char: #
	// (2) Any double quoted text
	// (3) Any alphanumeric string including the following special symbols: [. / | : ]
	public static final String DEFAULT_REG_EX = "(#.*$)|([-+]?[_a-zA-Z0-9\\.\\^\\-\\/\\|\\:]+)|(\"[^\"\\r\\n]*\")|([a-zA-Z0-9\\.\\^\\-\\/\\|]+)";

	public static void loadAsciiFile(File aFile, TokenProcessor aTokenProcessor)
	{
		loadAsciiFile(aFile, aTokenProcessor, new MatchTokenizer(DEFAULT_REG_EX));
	}

	public static void loadAsciiFile(File aFile, TokenProcessor aTokenProcessor, Tokenizer aTokenizer)
	{
		var tmpTokenProcessorL = ImmutableList.of(aTokenProcessor);
		loadAsciiFile(aFile, tmpTokenProcessorL, aTokenizer);
	}

	public static void loadAsciiFile(File aFile, Collection<TokenProcessor> tpSet)
	{
		loadAsciiFile(aFile, tpSet, new MatchTokenizer(DEFAULT_REG_EX));
	}

	public static void loadAsciiFile(File aFile, Collection<TokenProcessor> tpSet, Tokenizer aTokenizer)
	{
		loadAsciiFile(aFile, tpSet, aTokenizer, null);
	}

	public static void loadAsciiFile(File aFile, Collection<TokenProcessor> tpSet, Tokenizer aTokenizer, Task aTask)
	{
		try
		{
			var tmpUrl = aFile.toURI().toURL();
			loadAsciiFile(tmpUrl, tpSet, aTokenizer, aTask);
		}
		catch (Exception aExp)
		{
			aExp.printStackTrace();
			System.out.println("Resource not processed: " + aFile);
			return;
		}
	}

	public static void loadAsciiFile(URL aUrl, TokenProcessor aTokenProcessor)
	{
		var tmpTokenProcessorL = ImmutableList.of(aTokenProcessor);
		loadAsciiFile(aUrl, tmpTokenProcessorL, new MatchTokenizer(DEFAULT_REG_EX));
	}

	public static void loadAsciiFile(URL aUrl, Collection<TokenProcessor> tpSet)
	{
		loadAsciiFile(aUrl, tpSet, new MatchTokenizer(DEFAULT_REG_EX));
	}

	public static void loadAsciiFile(URL aUrl, Collection<TokenProcessor> tpSet, Tokenizer aTokenizer)
	{
		loadAsciiFile(aUrl, tpSet, aTokenizer, null);
	}

	public static void loadAsciiFile(URL aUrl, Collection<TokenProcessor> tpSet, Tokenizer aTokenizer, Task aTask)
	{
		// Insanity check
		if (aUrl == null)
			return;

		// Process our input
		try (var aStream = aUrl.openStream();)
		{
			loadAsciiFile(aStream, tpSet, aTokenizer, aTask);
		}
		catch (FileNotFoundException aExp)
		{
			System.out.println("Resource not found: " + aUrl);
			return;
		}
		catch (IOException aExp)
		{
			System.out.println("Ioexception occured while loading: " + aUrl);
			return;
		}
	}

	public static void loadAsciiFile(InputStream inStream, TokenProcessor aTokenProcessor) throws IOException
	{
		loadAsciiFile(inStream, aTokenProcessor, new MatchTokenizer(DEFAULT_REG_EX));
	}

	public static void loadAsciiFile(InputStream inStream, TokenProcessor aTokenProcessor, Tokenizer aTokenizer)
			throws IOException
	{
		loadAsciiFile(inStream, aTokenProcessor, aTokenizer, null);
	}

	public static void loadAsciiFile(InputStream inStream, TokenProcessor aTokenProcessor, Tokenizer aTokenizer,
			Task aTask) throws IOException
	{
		var tmpTokenProcessorL = ImmutableList.of(aTokenProcessor);
		loadAsciiFile(inStream, tmpTokenProcessorL, aTokenizer, aTask);
	}

	public static void loadAsciiFile(InputStream inStream, Collection<TokenProcessor> tpSet) throws IOException
	{
		loadAsciiFile(inStream, tpSet, new MatchTokenizer(DEFAULT_REG_EX), null);
	}

	public static void loadAsciiFile(InputStream inStream, Collection<TokenProcessor> tpSet, Tokenizer aTokenizer,
			Task aTask) throws IOException
	{
		// Insanity check
		if (tpSet == null)
			return;

		// Process our input
		var tmpBR = new BufferedReader(new InputStreamReader(inStream));
		var lineNum = 0;
		var dummyVar = new String[1];

		// Read the lines
		while (true)
		{
			// Bail if the associated task is no longer active
			if (aTask != null && aTask.isActive() == false)
				return;

			var strLine = tmpBR.readLine();
			if (strLine == null)
			{
				// Notify the TokenProcessors of job done
				for (TokenProcessor aTP : tpSet)
					aTP.flush();

				// Release the various streams
				tmpBR.close();
				inStream.close();
				break;
			}
			lineNum++;

			// Get the tokens out of our string
			var tokenL = aTokenizer.getTokens(strLine);
			if (tokenL.size() > 0)
			{
				// Transform from a list to an array
				var tokenArr = tokenL.toArray(dummyVar);

				// Process the tokens
				var isProcessed = false;
				for (TokenProcessor aTP : tpSet)
				{
					isProcessed = aTP.process(tokenArr, lineNum);
					if (isProcessed == true)
						break;
				}

				// Print out error message
				if (isProcessed == false)
				{
					System.out.println("Unreconized line [" + lineNum + "]: \n" + "\t" + strLine);
				}
			}
		}
	}

	/**
	 * Prompts the user to select a single File
	 *
	 * @param aLoaderInfo
	 *        : A object that stores the configuration from method call to method call.
	 * @param aParentComp
	 *        The parent component for the associated FileChooser GUI
	 * @param aTitleStr
	 *        The title of the FileChooser GUI
	 * @param aFileFilterC
	 *        A List of FileFilters
	 * @param aIsSaveDialog
	 *        Whether this FileChooser displays a GUI appropriate for saving a file
	 * @return The selected file or null
	 */
	public static File queryUserForFile(LoaderInfo aLoaderInfo, Component aParentComp, String aTitleStr,
			Collection<FileFilter> aFileFilterC, boolean aIsSaveDialog)
	{
		// Ensure we have a non null LoaderInfo
		if (aLoaderInfo == null)
			aLoaderInfo = new LoaderInfo();

		// Set up the FileChooser
		var tmpFC = new StandardFileChooser(null);
		tmpFC.setAcceptAllFileFilterUsed(false);
		tmpFC.setDialogTitle(aTitleStr);
		tmpFC.setMultiSelectionEnabled(false);
		aLoaderInfo.loadConfig(tmpFC);

		// Set in the FileFilters
		for (FileFilter aFileFilter : aFileFilterC)
			tmpFC.addChoosableFileFilter(aFileFilter);

		// Let the user choose a file
		int tmpVal;
		if (aIsSaveDialog == true)
			tmpVal = tmpFC.showSaveDialog(aParentComp);
		else
			tmpVal = tmpFC.showOpenDialog(aParentComp);

		// Store off the current settings
		aLoaderInfo.saveConfig(tmpFC);

		// Bail if no file chosen
		if (tmpVal != JFileChooser.APPROVE_OPTION)
			return null;

		// Return the file
		return tmpFC.getSelectedFile();
	}

	public static File queryUserForFile(LoaderInfo aLoaderInfo, Component aParentComp, String aTitleStr,
			FileFilter aFileFilter, boolean aIsSaveDialog)
	{
		var tmpFileFilterL = new ArrayList<FileFilter>();
		if (aFileFilter != null)
			tmpFileFilterL.add(aFileFilter);

		return queryUserForFile(aLoaderInfo, aParentComp, aTitleStr, tmpFileFilterL, aIsSaveDialog);
	}

	public static File queryUserForFile(LoaderInfo aLoaderInfo, Component aParentComp, String aTitleStr,
			boolean aIsSaveDialog)
	{
		return queryUserForFile(aLoaderInfo, aParentComp, aTitleStr, (FileFilter) null, aIsSaveDialog);
	}

	/**
	 * Prompts the user to select multiple Files
	 *
	 * @param aLoaderInfo
	 *        : A object that stores the configuration from method call to method call.
	 * @param aParentComp
	 *        The parent component for the associated FileChooser GUI
	 * @param aTitleStr
	 *        The title of the FileChooser GUI
	 * @param aFileFilterC
	 *        A List of FileFilters
	 * @param aIsSaveDialog
	 *        Whether this FileChooser displays a GUI appropriate for saving a file
	 * @return The selected file or null
	 */
	public static List<File> queryUserForFiles(LoaderInfo aLoaderInfo, Component aParentComp, String aTitleStr,
			Collection<FileFilter> aFileFilterC, boolean aIsSaveDialog)
	{
		// Ensure we have a non null LoaderInfo
		if (aLoaderInfo == null)
			aLoaderInfo = new LoaderInfo();

		// Set up the FileChooser
		var tmpFC = new StandardFileChooser(null);
		tmpFC.setAcceptAllFileFilterUsed(false);
		tmpFC.setDialogTitle(aTitleStr);
		tmpFC.setMultiSelectionEnabled(true);
		aLoaderInfo.loadConfig(tmpFC);

		// Set in the FileFilters
		for (FileFilter aFileFilter : aFileFilterC)
			tmpFC.addChoosableFileFilter(aFileFilter);

		// Let the user choose a file
		int tmpVal;
		if (aIsSaveDialog == true)
			tmpVal = tmpFC.showSaveDialog(aParentComp);
		else
			tmpVal = tmpFC.showOpenDialog(aParentComp);

		// Store off the current settings
		aLoaderInfo.saveConfig(tmpFC);

		// Bail if no file chosen
		if (tmpVal != JFileChooser.APPROVE_OPTION)
			return null;

		// Return a list that is modifiable
		var retFileL = Arrays.asList(tmpFC.getSelectedFiles());
		retFileL = new ArrayList<>(retFileL);
		return retFileL;
	}

	public static List<File> queryUserForFiles(LoaderInfo aLoaderInfo, Component aParentComp, String aTitleStr,
			FileFilter aFileFilter, boolean isSaveDialog)
	{
		var tmpFileFilterL = new ArrayList<FileFilter>();
		if (aFileFilter != null)
			tmpFileFilterL.add(aFileFilter);

		return queryUserForFiles(aLoaderInfo, aParentComp, aTitleStr, tmpFileFilterL, isSaveDialog);
	}

	public static List<File> queryUserForFiles(LoaderInfo aLoaderInfo, Component parentComp, String aTitleStr,
			boolean aIsSaveDialog)
	{
		return queryUserForFiles(aLoaderInfo, parentComp, aTitleStr, (FileFilter) null, aIsSaveDialog);
	}

	/**
	 * Prompts the user to select a single folder
	 *
	 * @param aLoaderInfo
	 *        : A object that stores the configuration from method call to method call.
	 * @param aParentComp
	 *        The parent component for the associated FileChooser GUI
	 * @param aTitleStr
	 *        The title of the FileChooser GUI
	 * @param aIsSaveDialog
	 *        Whether this FileChooser displays a GUI appropriate for saving a file
	 * @return The selected file or null
	 */
	public static File queryUserForPath(LoaderInfo aLoaderInfo, Component aParentComp, String aTitleStr,
			boolean aIsSaveDialog)
	{
		// Ensure we have a non null LoaderInfo
		if (aLoaderInfo == null)
			aLoaderInfo = new LoaderInfo();

		// Set up the FileChooser
		var tmpFC = new StandardFileChooser(null);
		tmpFC.setDialogTitle(aTitleStr);
		tmpFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		tmpFC.setMultiSelectionEnabled(false);

		aLoaderInfo.loadConfig(tmpFC);

		// Let the user choose a file
		int tmpVal;
		if (aIsSaveDialog == true)
			tmpVal = tmpFC.showSaveDialog(aParentComp);
		else
			tmpVal = tmpFC.showOpenDialog(aParentComp);

		// Store off the current settings
		aLoaderInfo.saveConfig(tmpFC);

		// Bail if no file chosen
		if (tmpVal != JFileChooser.APPROVE_OPTION)
			return null;

		// Return the file
		return tmpFC.getSelectedFile();
	}

}
