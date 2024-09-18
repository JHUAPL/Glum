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
package glum.net;

/**
 * Custom {@link Exception} that is thrown whenever a download fails.
 *
 * @author lopeznr1
 *
 */
public class FetchError extends Exception
{
	// Attributes
	private final Result refResult;

	/**
	 * Standard Constructor
	 */
	public FetchError(Throwable aCause, Result aResult)
	{
		super(aCause);

		refResult = aResult;
	}

	/**
	 * Returns the {@link Result}
	 *
	 * @return
	 */
	public Result getResult()
	{
		return refResult;
	}

}
