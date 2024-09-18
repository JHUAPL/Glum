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
package glum.filter;

import glum.zio.ZioObj;

/**
 * Interface that provides a mechanism for filtering items.
 *
 * @author lopeznr1
 *
 * @param <G1>
 */
public interface Filter<G1> extends ZioObj
{
	/**
	 * Method that returns true if aItem passes this filter
	 */
	public boolean isValid(G1 aItem);

}
