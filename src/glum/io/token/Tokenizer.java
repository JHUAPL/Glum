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
package glum.io.token;

import java.util.ArrayList;

/**
 * Interface that provide a mechanism for transforming an input string into an array of (string) tokens.
 *
 * @author lopeznr1
 */
public interface Tokenizer
{
	/**
	 * Returns all the tokens that match the pattern from the input
	 */
	public ArrayList<String> getTokens(String aInputStr);

}
