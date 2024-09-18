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

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Implementation of {@link TokenProcessor} used to log (to stdout) warnings for tokens where the first token matches a
 * specific value.
 *
 * @author lopeznr1
 */
public class WarningTP implements TokenProcessor
{
	// State vars
	private Map<String, String> warningM;

	/** Standard Constructor */
	public WarningTP()
	{
		warningM = new LinkedHashMap<String, String>();
	}

	/**
	 * Adds a new warning to the set of warnings. If during the processing of tokens the first token equals aInstr then
	 * the warning message will be logged to stdout.
	 * <p>
	 * Note if aMsg is null then aInstr will just be ignored
	 */
	public void add(String aInstr, String aMsg)
	{
		// Insanity check
		if (aInstr == null)
			return;

		warningM.put(aInstr, aMsg);
	}

	@Override
	public void flush()
	{
		; // Nothing to do
	}

	@Override
	public boolean process(String[] aTokenArr, int aLineNum)
	{
		// Insanity check
		if (aTokenArr == null)
			return false;

		if (warningM.containsKey(aTokenArr[0]) == true)
		{
			// Display the aMsg if associated with aInstr
			var tmpMsg = warningM.get(aTokenArr[0]);
			if (tmpMsg instanceof String)
				System.out.println("[" + aLineNum + "] " + tmpMsg);

			return true;
		}

		return false;
	}

}