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

import java.util.ArrayList;
import java.util.List;

/**
 * Base implementation of {@link UnitProvider} class that provides the functionality of listener registration and
 * notification.
 * 
 * @author lopeznr1
 */
public abstract class BaseUnitProvider implements UnitProvider
{
	private List<UnitListener> listenerL;
	private String refName;

	public BaseUnitProvider(String aRefName)
	{
		listenerL = new ArrayList<>();
		refName = aRefName;
	}

	@Override
	public String getDisplayName()
	{
		return refName;
	}

	@Override
	public void addListener(UnitListener aListener)
	{
		listenerL.add(aListener);
	}

	@Override
	public void delListener(UnitListener aListener)
	{
		listenerL.remove(aListener);
	}

	/**
	 * Utility method to notify our listeners
	 */
	protected void notifyListeners()
	{
		for (UnitListener aListener : listenerL)
			aListener.unitChanged(this, refName);
	}

}
