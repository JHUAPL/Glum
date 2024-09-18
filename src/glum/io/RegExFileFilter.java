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
import java.util.regex.Pattern;

import javax.swing.filechooser.FileFilter;

/**
 * Implementation of {@link FileFilter} used to gather files that match a regular expression.
 *
 * @author lopeznr1
 */
public class RegExFileFilter extends FileFilter implements java.io.FileFilter
{
	// State vars
	private String description;
	private Pattern matchPattern;
	private boolean allowDirs;

	/**
	 * Standard Constructor
	 *
	 * @param aDescription
	 *        Textual description of this filter.
	 * @param aMatchRegEx
	 *        Regular expression used for matching.
	 * @param aAllowDirs
	 *        Set to true if directories should (always) be kept or rejected.
	 */
	public RegExFileFilter(String aDescription, String aMatchRegEx, boolean aAllowDirs)
	{
		description = aDescription;
		matchPattern = Pattern.compile(aMatchRegEx);
		allowDirs = aAllowDirs;
	}

	/** Simplified Constructor */
	public RegExFileFilter(String aDescription, String aMatchRegEx)
	{
		this(aDescription, aMatchRegEx, true);
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
		var fileName = aFile.getName();

		// Test to see if the fileName matches the compiled regex
		if (matchPattern.matcher(fileName).matches() == true)
			return true;

		return false;
	}

	@Override
	public String getDescription()
	{
		return description;
	}

}
