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

import java.io.IOException;

import glum.zio.ZinStream;
import glum.zio.ZoutStream;

/**
 * A {@link Filter} which does not filter anything. Thus the method isValid() always returns true.
 *
 * @author lopeznr1
 *
 * @param <G1>
 */
public class NullFilter<G1> implements Filter<G1>
{

	@Override
	public boolean isValid(G1 aItem)
	{
		return true;
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
