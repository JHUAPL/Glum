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
package glum.gui.action;

import java.awt.Component;
import java.util.Collection;

/**
 * Interface that provides a mechanism for a component to declare that it will provide action {@link Component}s
 * (typically buttons).
 *
 * @author lopeznr1
 */
public interface ActionComponentProvider
{

	/**
	 * Returns a list of {@link Component}s that should be placed in the action region.
	 */
	public Collection<? extends Component> getActionButtons();

}
