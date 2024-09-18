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
package glum.zio;

import java.io.IOException;

/**
 * Interface to allow for serialization of (mutable) objects.
 *
 * @author lopeznr1
 */
public interface ZioObj
{
	/**
	 * Deserialization method to read data from the {@link ZinStream}.
	 */
	public void zioRead(ZinStream aStream) throws IOException;

	/**
	 * Serialization method to write data to the {@link ZoutStream}.
	 */
	public void zioWrite(ZoutStream aStream) throws IOException;

}
