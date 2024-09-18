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
 * Provides the main entry point.
 * <p>
 * Currently this will just print the library name and the version.
 */
public class AppGlum
{
	/**
	 * Main entry point that will print out the version of Glum to stdout.
	 */
	public static void main(String[] aArgArr)
	{
		System.out.println("Glum Library. Version: " + AppInfo.getVersion());
		System.exit(0);
	}

}
