package glum.io;

import java.io.File;
import java.util.regex.Pattern;
import javax.swing.filechooser.FileFilter;

public class RegExFileFilter extends FileFilter implements java.io.FileFilter
{
	// State vars
	private String description;
	private Pattern matchPattern;
	private boolean allowDirs;

	public RegExFileFilter(String aDescription, String aMatchRegEx, boolean aAllowDirs)
	{
		description = aDescription;
		matchPattern = Pattern.compile(aMatchRegEx);
		allowDirs = aAllowDirs;
	}

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
		String fileName;

		// Allow directories if appropriate
		if (aFile.isDirectory() == true)
			return allowDirs;

		// Retrieve the corresponding file name
		fileName = aFile.getName();
		
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
