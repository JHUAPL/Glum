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
package glum.unit;

import glum.zio.ZioObj;

/**
 * Interface that provides a mechanism for accessing a {@link Unit} and getting notification when that {@link Unit} has
 * changed.
 *
 * @author lopeznr1
 */
public interface UnitProvider extends ZioObj
{
	/**
	 * Adds a Listener for Unit changes
	 */
	public void addListener(UnitListener aListener);

	/**
	 * Removes a Listener for Unit changes
	 */
	public void delListener(UnitListener aListener);

	/**
	 * Returns the official name name associated with the Unit
	 */
	public String getDisplayName();

	/**
	 * Returns the {@link Unit} associated with this provider
	 */
	public Unit getUnit();

}
