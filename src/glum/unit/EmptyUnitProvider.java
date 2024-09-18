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

import java.io.IOException;

import glum.zio.ZinStream;
import glum.zio.ZoutStream;

/**
 * {@link UnitProvider} that always returns null for the {@link Unit}.
 *
 * @author lopeznr1
 */
public class EmptyUnitProvider implements UnitProvider
{
	@Override
	public void addListener(UnitListener aListener)
	{
		; // Nothing to do
	}

	@Override
	public void delListener(UnitListener aListener)
	{
		; // Nothing to do
	}

	@Override
	public String getDisplayName()
	{
		return "Null Unit";
	}

	@Override
	public Unit getUnit()
	{
		return null;
	}

	@Override
	public void zioRead(ZinStream aStream) throws IOException
	{
		; // Nothing to do
	}

	@Override
	public void zioWrite(ZoutStream aStream) throws IOException
	{
		; // Nothing to do
	}

}
