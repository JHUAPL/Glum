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
package glum.version;

/**
 * Interface which provides access to version components (major, minor, patch).
 * <p>
 * Each component is modeled as an integer and it is assumed that higher values correspond to more developed software.
 * <p>
 * Reference: https://semver.org/
 * <p>
 * Implementors of this interface should be immutable.
 *
 * @author lopeznr1
 */
public interface Version
{
	// Constants
	public static Version AbsMin = PlainVersion.AbsMin;
	public static Version AbsMax = PlainVersion.AbsMax;
	public static Version Zero = PlainVersion.Zero;

	/**
	 * Returns the major version component.
	 */
	public int major();

	/**
	 * Returns the minor version component.
	 */
	public int minor();

	/**
	 * Returns the patch version component.
	 */
	public int patch();

}
