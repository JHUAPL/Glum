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
package glum.source;

/**
 * Enum that defines the current (availability) state of a {@link Source}.
 *
 * @author lopeznr1
 */
public enum SourceState
{
	/** Defines the source as being located on a local file system. */
	Local,

	/** Defines the source as being located on a remote system. */
	Remote,

	/** Defines the source as being partially fetched to a local file system. */
	Partial,

	/** Defines the source as being unavailable. */
	Unavailable;

}
