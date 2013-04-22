package glum.io;

import java.io.File;
import java.util.*;
import javax.swing.filechooser.FileFilter;

public class SimpleFileFilter extends FileFilter implements java.io.FileFilter
{
	// State vars
	private Collection<String> extensionList;
	private String description;
	private boolean allowDirs;

	public SimpleFileFilter(String aDescription)
	{
		allowDirs = true;
		description = aDescription;
		extensionList = new LinkedList<String>();
	}

	public SimpleFileFilter(String aDescription, String aExtension)
	{
		this(aDescription);
		addExtension(aExtension);
	}

	/**
	 * Adds a file extension which should be allowed through this filter
	 */
	public void addExtension(String aExtension)
	{
		if (aExtension != null)
			extensionList.add(aExtension);
	}
	
	/**
	 * Adds the collections of file extensions which should be allowed through this filter
	 */
	public void addExtensions(String... extArr)
	{
		for (String aExtension : extArr)
			addExtension(aExtension);
	}

	/**
	 * Sets whether directories should be allowed
	 */
	public void setAllowDirectories(boolean aBool)
	{
		allowDirs = aBool;
	}

	@Override
	public boolean accept(File aFile)
	{
		String aStr, aFileName;
		int aIndex;

		// Allow directories if appropriate
		if (aFile.isDirectory() == true)
			return allowDirs;

		// Retrieve the corresponding file name
		aFileName = aFile.getName();

		// Ensure the file has an extension
		aIndex = aFileName.lastIndexOf('.');
		if (aIndex == -1)
			return false;

		// See if aFileName's extension matches any in extensionList
		for (String aExt : extensionList)
		{
			if (aFileName.length() > aExt.length())
			{
				aStr = aFileName.substring(aFileName.length() - aExt.length());
				if (aExt.equalsIgnoreCase(aStr) == true)
					return true;
			}
		}

		return false;
	}

	@Override
	public String getDescription()
	{
		return description;
	}

}
