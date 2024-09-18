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
package glum.gui.info;

import java.awt.Component;
import java.awt.Dimension;
import java.io.IOException;

import glum.zio.*;

/**
 * Immutable object used to store the position/dimension/visibility state of a {@link Component}.
 * <p>
 * This is typically used capture the state of a dialog or window.
 *
 * @author lopeznr1
 */
public class WindowCfg implements ZioRaw
{
	// Attributes
	private final boolean isShown;
	private final int posX;
	private final int posY;
	private final int dimX;
	private final int dimY;

	/** Standard Constructor */
	public WindowCfg(boolean aIsShown, int aPosX, int aPosY, int aDimX, int aDimY)
	{
		isShown = aIsShown;

		posX = aPosX;
		posY = aPosY;

		dimX = aDimX;
		dimY = aDimY;
	}

	/** Serialization Constructor */
	public WindowCfg(ZinStream aStream) throws IOException
	{
		aStream.readVersion(0);

		isShown = aStream.readBool();

		posX = aStream.readInt();
		posY = aStream.readInt();

		dimX = aStream.readInt();
		dimY = aStream.readInt();
	}

	/**
	 * UI based constructor
	 *
	 * The WindowInfo will be setup to the current state of the provided {@link Component}.
	 */
	public WindowCfg(Component aComponent)
	{
		isShown = aComponent.isVisible();

		posX = aComponent.getLocation().x;
		posY = aComponent.getLocation().y;

		dimX = aComponent.getSize().width;
		dimY = aComponent.getSize().height;
	}

	/**
	 * Syncs the specified {@link Component} to match the state of this {@link WindowCfg}.
	 */
	public void configure(Component aComponent)
	{
		aComponent.setLocation(posX, posY);

		aComponent.setPreferredSize(new Dimension(dimX, dimY));
		aComponent.setSize(dimX, dimY);
	}

	/**
	 * Returns a {@link WindowCfg} which matches this WindowInfo but with the isShown flag set to the requested setting.
	 */
	public WindowCfg withIsShown(boolean aIsShown)
	{
		// Bail if nothing changes
		if (isShown == aIsShown)
			return this;

		return new WindowCfg(aIsShown, posX, posY, dimX, dimY);
	}

	// @formatter:off
	// Accessor methods
	public boolean isShown() { return isShown; }
	public int dimX() { return dimX; }
	public int dimY() { return dimY; }
	public int posX() { return posX; }
	public int posY() { return posY; }
	// @formatter:on

	@Override
	public void zioWrite(ZoutStream aStream) throws IOException
	{
		aStream.writeVersion(0);

		aStream.writeBool(isShown);

		aStream.writeInt(posX);
		aStream.writeInt(posY);

		aStream.writeInt(dimX);
		aStream.writeInt(dimY);
	}

	@Override
	public int hashCode()
	{
		final int prime = 31;
		int result = 1;
		result = prime * result + dimX;
		result = prime * result + dimY;
		result = prime * result + (isShown ? 1231 : 1237);
		result = prime * result + posX;
		result = prime * result + posY;
		return result;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		WindowCfg other = (WindowCfg) obj;
		if (dimX != other.dimX)
			return false;
		if (dimY != other.dimY)
			return false;
		if (isShown != other.isShown)
			return false;
		if (posX != other.posX)
			return false;
		if (posY != other.posY)
			return false;
		return true;
	}

}
