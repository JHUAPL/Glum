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

import glum.registry.ConfigMap;

/**
 * Implementation of {@link TokenProcessor} used to populate a {@link ConfigMap}.
 *
 * @author lopeznr1
 */
public class ConfigMapTP implements TokenProcessor
{
	// State vars
	private ConfigMap refConfigMap;

	/** Standard Constructor */
	public ConfigMapTP(ConfigMap aConfigMap)
	{
		refConfigMap = aConfigMap;
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
		if (aTokenArr == null || aTokenArr.length <= 1)
			return false;

		if (aTokenArr.length == 2)
		{
			refConfigMap.put(aTokenArr[0], aTokenArr[1]);
		}
		else
		{
			for (int c1 = 1; c1 < aTokenArr.length; c1++)
				refConfigMap.addItem(aTokenArr[0], aTokenArr[c1]);
		}

		return true;
	}

}
