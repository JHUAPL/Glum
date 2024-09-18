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

import java.awt.Dimension;
import java.awt.Point;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Method;

import javax.swing.JFileChooser;
import javax.swing.plaf.FileChooserUI;

import glum.zio.*;
import glum.zio.util.ZioUtil;

/**
 * Object that stores the configuration associated with a {@link JFileChooser}.
 * <p>
 * The following state is stored:
 * <ul>
 * <li>Position of dialog
 * <li>Size of dialog
 * <li>File path
 * </ul>
 *
 * @author lopeznr1
 */
public class LoaderInfo implements ZioObj
{
	// State vars
	private File path;

	// Gui vars
	private boolean isVisible;
	private Point position;
	private Dimension dimension;

	/** Standard Constructor */
	public LoaderInfo(File aPath)
	{
		path = aPath;

		isVisible = false;
		position = null;
		dimension = null;
	}

	/** Simplified Constructor */
	public LoaderInfo()
	{
		this(null);
	}

	// Accessor methods
	// @formatter:off
	/** Gets the file path. */
	public File getPath() { return path; }

	/** Sets the file path. */
	public void setPath(File aPath) { path = aPath; }
	// @formatter:on

	/**
	 * Loads the current configuration into aFileChooser
	 */
	public void loadConfig(JFileChooser aFileChooser)
	{
		if (position != null)
			aFileChooser.setLocation(position);

		if (dimension != null)
		{
			aFileChooser.setPreferredSize(dimension);
			aFileChooser.setSize(dimension);
		}

		// Bail if there is no filePath
		if (path == null)
			return;

		if (aFileChooser.getFileSelectionMode() == JFileChooser.DIRECTORIES_ONLY)
		{
			// Locate the first folder that exists
			File tmpPath = path;
			while (tmpPath != null && tmpPath.isDirectory() == false)
				tmpPath = tmpPath.getParentFile();

			// Set the FileChooser to the first folder that exists
			aFileChooser.setCurrentDirectory(tmpPath);

			// Set the FileChooser's name area to reflect the absolute path
			if (tmpPath != null)
			{
				String absPathStr = path.getAbsolutePath();
				try
				{
					FileChooserUI tmpFCUI = aFileChooser.getUI();
					Class<? extends FileChooserUI> fcClass = tmpFCUI.getClass();
					Method setFileName = fcClass.getMethod("setFileName", String.class);
					setFileName.invoke(tmpFCUI, absPathStr);
				}
				catch (Exception aExp)
				{
					aExp.printStackTrace();
				}
			}
		}
		else
		{
			if (path.isFile() == true)
			{
				aFileChooser.setSelectedFile(path);
			}
			else
			{
				// Set the FileChooser's current directory to the first folder that exists
				File tmpPath = path;
				while (tmpPath != null && tmpPath.getParentFile() != null && tmpPath.getParentFile().isDirectory() == false)
					tmpPath = tmpPath.getParentFile();

				aFileChooser.setCurrentDirectory(tmpPath);
			}
		}
	}

	/**
	 * Stores the current configuration from aFileChooser
	 */
	public void saveConfig(JFileChooser aFileChooser)
	{
		position = aFileChooser.getLocation();
		dimension = aFileChooser.getSize();
		isVisible = aFileChooser.isVisible();

		path = aFileChooser.getCurrentDirectory();
	}

	@Override
	public void zioRead(ZinStream aStream) throws IOException
	{
		aStream.readVersion(0);

		isVisible = aStream.readBool();
		position = ZioUtil.readPoint(aStream);
		dimension = ZioUtil.readDimension(aStream);

		String pathStr = aStream.readString();
		path = null;
		if (pathStr != null)
			path = new File(pathStr);
	}

	@Override
	public void zioWrite(ZoutStream aStream) throws IOException
	{
		aStream.writeVersion(0);

		aStream.writeBool(isVisible);
		ZioUtil.writePoint(aStream, position);
		ZioUtil.writeDimension(aStream, dimension);

		String pathStr = null;
		if (path != null)
			pathStr = path.getAbsolutePath();
		aStream.writeString(pathStr);
	}

}
