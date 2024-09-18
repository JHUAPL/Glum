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
package glum.app;

/**
 * Immutable class that provides information related to the build.
 *
 * @author lopeznr1
 */
public class AppInfo
{
	/**
	 * Defines the version of the Glum library.
	 * <p>
	 * Note we do not make the Version directly visible and final so other classes will not utilized a cached version
	 * when built via Ant.
	 */
	private static String Version = "2.0.0";

	/**
	 * Returns the version of the application
	 */
	public static String getVersion()
	{
		return Version;
	}

}
