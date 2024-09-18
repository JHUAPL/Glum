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

/**
 * Interface that provides a mechanism to allow the processing of an array of (string) tokens.
 *
 * @author lopeznr1
 */
public interface TokenProcessor
{
	/**
	 * Lets the processor know that it will not be called anymore. This method is only called after all data has been
	 * read in from the corresponding stream. Note this method will never get called if the reading was aborted or an IO
	 * exception occurred.
	 */
	public void flush();

	/**
	 * Returns true if able to handle the tokens
	 */
	public boolean process(String[] aTokenArr, int aLineNum);

}
