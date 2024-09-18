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
import java.net.URL;

/**
 * Interface that defines the location of a resource.
 * <p>
 * Implementations of this interface should be immutable.
 *
 * @author lopeznr1
 */
public interface Source
{
	/**
	 * Returns the (expected) disk size (in bytes) of the source.
	 * <p>
	 * If the size is unknown then -1 should be returned.
	 */
	public long getDiskSize();

	/**
	 * Returns the file name associated with the source.
	 * <p>
	 * This is just the last element in the path.
	 */
	public String getName();

	/**
	 * Returns the path as a string associated with the source.
	 */
	public String getPath();

	/**
	 * Returns the (local) source.
	 * <p>
	 * May return null.
	 */
	public File getLocalFile();

	/**
	 * Returns the (remote) source.
	 * <p>
	 * May return null.
	 */
	public URL getRemoteUrl();

}
