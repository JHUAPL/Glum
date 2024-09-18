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
import java.text.Format;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import glum.zio.ZinStream;
import glum.zio.ZoutStream;

/**
 * Implementation of {@link UnitProvider} that provides a unit compatible for processing numeric values.
 *
 * @author lopeznr1
 */
public class DecimalUnitProvider extends BaseUnitProvider
{
	// State vars
	private List<Unit> protoUnitL;
	private Unit activeUnit;

	/** Standard Constructor */
	public DecimalUnitProvider(String aRefName, Unit aActiveUnit)
	{
		super(aRefName);

		protoUnitL = new ArrayList<>();
		activeUnit = aActiveUnit;
	}

	/**
	 * Updates the activeUnit with the specified configuration
	 */
	public void activate(Unit aProtoUnit, int aDecimalPlaces, boolean aForceFullLabel)
	{
		String formatStr;

		// Insanity check
		if (aProtoUnit == null)
			return;

		// Build the format
		formatStr = "###,###,###,###,###,##0";
		if (aDecimalPlaces > 0)
		{
			formatStr += ".";
			for (int c1 = 0; c1 < aDecimalPlaces; c1++)
				formatStr += "0";
		}

		if (aProtoUnit instanceof HeuristicUnit)
			activeUnit = ((HeuristicUnit) aProtoUnit).spawnClone(aDecimalPlaces);
		else if (aForceFullLabel == true)
			activeUnit = new NumberUnit(aProtoUnit.getLabel(true), aProtoUnit.getLabel(true), aProtoUnit.toUnit(1),
					formatStr);
		else
			activeUnit = new NumberUnit(aProtoUnit.getLabel(true), aProtoUnit.getLabel(false), aProtoUnit.toUnit(1),
					formatStr);

		notifyListeners();
	}

	/**
	 * Adds in the specified Unit as a prototype (which can be used to configure the active unit)
	 */
	public void addProtoUnit(Unit aProtoUnit)
	{
		// Insanity check
		if (aProtoUnit instanceof HeuristicUnit == false && aProtoUnit instanceof NumberUnit == false)
			throw new RuntimeException("ProtoUnit must either be of type HeuristicUnit or NumberUnit");

		protoUnitL.add(aProtoUnit);
	}

	/**
	 * Returns the number of decimal places allowed in the current unit
	 */
	public int getDecimalPlaces()
	{
		Format aFormat;

		aFormat = activeUnit.getFormat();
		if (aFormat instanceof NumberFormat)
			return ((NumberFormat) aFormat).getMaximumFractionDigits();

		return 0;
	}

	/**
	 * Returns whether the unit is forced to show only the detailed label
	 */
	public boolean getForceFullLabel()
	{
		String fullLabel, shortLabel;

		if (activeUnit instanceof HeuristicUnit)
			return false;

		fullLabel = activeUnit.getLabel(true);
		shortLabel = activeUnit.getLabel(false);
		return fullLabel.equals(shortLabel);
	}

	/**
	 * Returns the prototype unit corresponding to the current active unit
	 */
	public Unit getProtoUnit()
	{
		String activeLabel, protoLabel;

		activeLabel = activeUnit.getLabel(true);
		for (Unit aUnit : protoUnitL)
		{
			protoLabel = aUnit.getLabel(true);
			if (protoLabel.equals(activeLabel) == true)
				return aUnit;
		}

		return null;
	}

	/**
	 * Returns all of the possible prototype units
	 */
	public List<Unit> getProtoUnitList()
	{
		return new ArrayList<>(protoUnitL);
	}

	@Override
	public Unit getUnit()
	{
		return activeUnit;
	}

	@Override
	public void zioRead(ZinStream aStream) throws IOException
	{
		Unit protoUnit;
		int protoUnitIdx;
		int decimalPlaces;
		boolean forceFullLabel;

		// Read the stream's content
		aStream.readVersion(0);

		protoUnitIdx = aStream.readInt();
		decimalPlaces = aStream.readInt();
		forceFullLabel = aStream.readBool();

		// Install the configuration
		protoUnit = null;
		if (protoUnitIdx >= 0 && protoUnitIdx < protoUnitL.size())
			protoUnit = protoUnitL.get(protoUnitIdx);
		activate(protoUnit, decimalPlaces, forceFullLabel);
	}

	@Override
	public void zioWrite(ZoutStream aStream) throws IOException
	{
		int protoUnitIdx;
		int decimalPlaces;
		boolean forceFulLabel;

		// Retrieve the configuration
		protoUnitIdx = protoUnitL.indexOf(getProtoUnit());
		decimalPlaces = getDecimalPlaces();
		forceFulLabel = getForceFullLabel();

		// Write the stream's contents
		aStream.writeVersion(0);

		aStream.writeInt(protoUnitIdx);
		aStream.writeInt(decimalPlaces);
		aStream.writeBool(forceFulLabel);
	}

}
