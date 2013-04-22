package glum.io;

import java.awt.*;
import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;

import com.google.common.collect.Lists;

import glum.io.token.MatchTokenizer;
import glum.io.token.Tokenizer;
import glum.task.Task;

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
		Collection<TokenProcessor> tpSet;

		tpSet = new LinkedList<TokenProcessor>();
		tpSet.add(aTokenProcessor);
		loadAsciiFile(aFile, tpSet, aTokenizer);
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
		URL aUrl;

		try
		{
			aUrl = aFile.toURI().toURL();
			loadAsciiFile(aUrl, tpSet, aTokenizer, aTask);
		}
		catch (Exception e)
		{
			e.printStackTrace();
			System.out.println("Resource not processed: " + aFile);
			return;
		}
	}

	public static void loadAsciiFile(URL aUrl, TokenProcessor aTokenProcessor)
	{
		Collection<TokenProcessor> tpSet;

		tpSet = new LinkedList<TokenProcessor>();
		tpSet.add(aTokenProcessor);
		loadAsciiFile(aUrl, tpSet, new MatchTokenizer(DEFAULT_REG_EX));
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
		InputStream inStream;

		// Insanity check
		if (aUrl == null)
			return;

		// Process our input
		inStream = null;
		try
		{
			inStream = aUrl.openStream();
			loadAsciiFile(inStream, tpSet, aTokenizer, aTask);
		}
		catch (FileNotFoundException e)
		{
			System.out.println("Resource not found: " + aUrl);
			return;
		}
		catch (IOException e)
		{
			System.out.println("Ioexception occured while loading: " + aUrl);
			return;
		}
		finally
		{
			IoUtil.forceClose(inStream);
		}
	}

	public static void loadAsciiFile(InputStream inStream, TokenProcessor aTokenProcessor) throws IOException
	{
		loadAsciiFile(inStream, aTokenProcessor, new MatchTokenizer(DEFAULT_REG_EX));
	}

	public static void loadAsciiFile(InputStream inStream, TokenProcessor aTokenProcessor, Tokenizer aTokenizer) throws IOException
	{
		loadAsciiFile(inStream, aTokenProcessor, aTokenizer, null);
	}

	public static void loadAsciiFile(InputStream inStream, TokenProcessor aTokenProcessor, Tokenizer aTokenizer, Task aTask) throws IOException
	{
		Collection<TokenProcessor> tpSet;

		tpSet = new LinkedList<TokenProcessor>();
		tpSet.add(aTokenProcessor);
		loadAsciiFile(inStream, tpSet, aTokenizer, aTask);
	}

	public static void loadAsciiFile(InputStream inStream, Collection<TokenProcessor> tpSet) throws IOException
	{
		loadAsciiFile(inStream, tpSet, new MatchTokenizer(DEFAULT_REG_EX), null);
	}

	public static void loadAsciiFile(InputStream inStream, Collection<TokenProcessor> tpSet, Tokenizer aTokenizer, Task aTask) throws IOException
	{
		BufferedReader br;
		String strLine;
		int lineNum;
		ArrayList<String> aList;
		String tokens[], dummyVar[];
		boolean isProcessed;

		// Insanity check
		if (tpSet == null)
			return;

		// Process our input
		br = new BufferedReader( new InputStreamReader(inStream) );
		lineNum = 0;
		dummyVar = new String[1];

		// Read the lines
		while (true)
		{
			// Bail if the associated task is no longer active
			if (aTask != null && aTask.isActive() == false)
				return;
			
			strLine = br.readLine();
			if (strLine == null)
			{
				// Notify the TokenProcessors of job done
				for (TokenProcessor aTP : tpSet)
					aTP.flush();

				// Release the various streams
				br.close();
				inStream.close();
				break;
			}
			lineNum++;

			// Get the tokens out of our string
			tokens = null;
			aList = aTokenizer.getTokens(strLine);
			if (aList.size() > 0)
			{
				// Transform from a list to an array
				tokens = aList.toArray(dummyVar);

				// Process the tokens
				isProcessed = false;
				for (TokenProcessor aTP : tpSet)
				{
					isProcessed = aTP.process(tokens, lineNum);
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
	 *           : A object that stores the configuration from method call to method call.
	 * @param parentComp
	 *           The parent component for the associated FileChooser GUI
	 * @param aTitleStr
	 *           The title of the FileChooser GUI
	 * @param ffList
	 *           A List of FileFilters
	 * @param isSaveDialog
	 *           Whether this FileChooser displays a GUI appropriate for saving a file
	 * @return The selected file or null
	 */
	public static File queryUserForFile(LoaderInfo aLoaderInfo, Component parentComp, String aTitleStr, Collection<FileFilter> ffList, boolean isSaveDialog)
	{
		JFileChooser aFC;
		int aVal;

		// Ensure we have a non null LoaderInfo
		if (aLoaderInfo == null)
			aLoaderInfo = new LoaderInfo();

		// Set up the FileChooser
		aFC = new StandardFileChooser(null);
		aFC.setAcceptAllFileFilterUsed(false);
		aFC.setDialogTitle(aTitleStr);
		aFC.setMultiSelectionEnabled(false);
		aLoaderInfo.loadConfig(aFC);

		// Set in the FileFilters
		for (FileFilter aFileFilter : ffList)
			aFC.addChoosableFileFilter(aFileFilter);

		// Let the user choose a file
		if (isSaveDialog == true)
			aVal = aFC.showSaveDialog(parentComp);
		else
			aVal = aFC.showOpenDialog(parentComp);

		// Store off the current settings
		aLoaderInfo.saveConfig(aFC);

		// Bail if no file chosen
		if (aVal != JFileChooser.APPROVE_OPTION)
			return null;

		// Return the file
		return aFC.getSelectedFile();
	}

	public static File queryUserForFile(LoaderInfo aLoaderInfo, Component parentComp, String aTitleStr, FileFilter aFileFilter, boolean isSaveDialog)
	{
		List<FileFilter> ffList;
		
		ffList = Lists.newArrayList();
		if (aFileFilter != null)
			ffList.add(aFileFilter);
		
		return queryUserForFile(aLoaderInfo, parentComp, aTitleStr, ffList, isSaveDialog);
	}
	
	public static File queryUserForFile(LoaderInfo aLoaderInfo, Component parentComp, String aTitleStr, boolean isSaveDialog)
	{
		return queryUserForFile(aLoaderInfo, parentComp, aTitleStr, (FileFilter)null, isSaveDialog);
	}
	
	/**
	 * Prompts the user to select multiple Files
	 * 
	 * @param aLoaderInfo
	 *           : A object that stores the configuration from method call to method call.
	 * @param parentComp
	 *           The parent component for the associated FileChooser GUI
	 * @param aTitleStr
	 *           The title of the FileChooser GUI
	 * @param ffList
	 *           A List of FileFilters
	 * @param isSaveDialog
	 *           Whether this FileChooser displays a GUI appropriate for saving a file
	 * @return The selected file or null
	 */
	public static List<File> queryUserForFiles(LoaderInfo aLoaderInfo, Component parentComp, String aTitleStr, Collection<FileFilter> ffList, boolean isSaveDialog)
	{
		JFileChooser aFC;
		List<File> retList;
		int aVal;

		// Ensure we have a non null LoaderInfo
		if (aLoaderInfo == null)
			aLoaderInfo = new LoaderInfo();

		// Set up the FileChooser
		aFC = new StandardFileChooser(null);
		aFC.setAcceptAllFileFilterUsed(false);
		aFC.setDialogTitle(aTitleStr);
		aFC.setMultiSelectionEnabled(true);
		aLoaderInfo.loadConfig(aFC);

		// Set in the FileFilters
		for (FileFilter aFileFilter : ffList)
			aFC.addChoosableFileFilter(aFileFilter);

		// Let the user choose a file
		if (isSaveDialog == true)
			aVal = aFC.showSaveDialog(parentComp);
		else
			aVal = aFC.showOpenDialog(parentComp);

		// Store off the current settings
		aLoaderInfo.saveConfig(aFC);

		// Bail if no file chosen
		if (aVal != JFileChooser.APPROVE_OPTION)
			return null;

		// Return a list that is modifiable
		retList = Arrays.asList(aFC.getSelectedFiles());
		retList = Lists.newArrayList(retList);
		return retList;
	}
	
	public static List<File> queryUserForFiles(LoaderInfo aLoaderInfo, Component parentComp, String aTitleStr, FileFilter aFileFilter, boolean isSaveDialog)
	{
		List<FileFilter> ffList;
		
		ffList = Lists.newArrayList();
		if (aFileFilter != null)
			ffList.add(aFileFilter);
		
		return queryUserForFiles(aLoaderInfo, parentComp, aTitleStr, ffList, isSaveDialog);
	}
	
	public static List<File> queryUserForFiles(LoaderInfo aLoaderInfo, Component parentComp, String aTitleStr, boolean isSaveDialog)
	{
		return queryUserForFiles(aLoaderInfo, parentComp, aTitleStr, (FileFilter)null, isSaveDialog);
	}
	
	/**
	 * Prompts the user to select a single folder
	 * 
	 * @param aLoaderInfo
	 *           : A object that stores the configuration from method call to method call.
	 * @param parentComp
	 *           The parent component for the associated FileChooser GUI
	 * @param aTitleStr
	 *           The title of the FileChooser GUI
	 * @param isSaveDialog
	 *           Whether this FileChooser displays a GUI appropriate for saving a file
	 * @return The selected file or null
	 */
	public static File queryUserForPath(LoaderInfo aLoaderInfo, Component parentComp, String aTitleStr, boolean isSaveDialog)
	{
		JFileChooser aFC;
		int aVal;

		// Ensure we have a non null LoaderInfo
		if (aLoaderInfo == null)
			aLoaderInfo = new LoaderInfo();

		// Set up the FileChooser
		aFC = new StandardFileChooser(null);
		aFC.setDialogTitle(aTitleStr);
		aFC.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		aFC.setMultiSelectionEnabled(false);
		
		aLoaderInfo.loadConfig(aFC);

		// Let the user choose a file
		if (isSaveDialog == true)
			aVal = aFC.showSaveDialog(parentComp);
		else
			aVal = aFC.showOpenDialog(parentComp);

		// Store off the current settings
		aLoaderInfo.saveConfig(aFC);

		// Bail if no file chosen
		if (aVal != JFileChooser.APPROVE_OPTION)
			return null;

		// Return the file
		return aFC.getSelectedFile();
	}
	
}
