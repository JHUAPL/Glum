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

import java.io.File;
import java.util.ArrayList;
import java.util.Collection;

import javax.swing.filechooser.FileFilter;

/**
 * Implementation of {@link FileFilter} used to gather files that have a matching file extension.
 *
 * @author lopeznr1
 */
public class SimpleFileFilter extends FileFilter implements java.io.FileFilter
{
	// State vars
	private ArrayList<String> extensionL;
	private String description;
	private boolean allowDirs;

	/**
	 * Standard Constructor
	 *
	 * @param aDescription
	 *        Textual description of this filter.
	 */
	public SimpleFileFilter(String aDescription)
	{
		extensionL = new ArrayList<String>();
		description = aDescription;
		allowDirs = true;
	}

	/**
	 * Convenience Constructor
	 *
	 * @param aDescription
	 *        Textual description of this filter.
	 * @param aExtension
	 *        A file extension to match against.
	 */
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
			extensionL.add(aExtension);
	}

	/**
	 * Adds the collections of file extensions which should be allowed through this filter
	 */
	public void addExtensions(String... aExtensionArr)
	{
		for (String aExtension : aExtensionArr)
			addExtension(aExtension);
	}

	/**
	 * Adds the collections of file extensions which should be allowed through this filter
	 */
	public void addExtensions(Collection<String> aExtensionC)
	{
		for (String aExtension : aExtensionC)
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
		// Allow directories if appropriate
		if (aFile.isDirectory() == true)
			return allowDirs;

		// Retrieve the corresponding file name
		var tmpFileName = aFile.getName();

		// Ensure the file has an extension
		var tmpIdx = tmpFileName.lastIndexOf('.');
		if (tmpIdx == -1)
			return false;

		// See if aFileName's extension matches any in extensionList
		for (String aExt : extensionL)
		{
			if (tmpFileName.length() > aExt.length())
			{
				var tmpStr = tmpFileName.substring(tmpFileName.length() - aExt.length());
				if (aExt.equalsIgnoreCase(tmpStr) == true)
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
