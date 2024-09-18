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
import java.util.*;

import glum.zio.ZinStream;
import glum.zio.ZoutStream;

/**
 * Implementation of {@link UnitProvider} that provides a unit compatible for processing chronological values.
 *
 * @author lopeznr1
 */
public class DateUnitProvider extends BaseUnitProvider
{
	// Config vars
	private boolean isCustom;
	private TimeZone cfgTimeZone;
	private String cfgProtoName;
	private String cfgCustomPattern;

	// State vars
	private Map<String, DateUnit> protoM;
	private DateUnit activeUnit;

	/** Standard Constructor */
	public DateUnitProvider(String aRefName, DateUnit aActiveUnit)
	{
		super(aRefName);

		// Config vars
		isCustom = false;
		cfgTimeZone = aActiveUnit.getFormat().getTimeZone();
		cfgProtoName = null;
		cfgCustomPattern = aActiveUnit.getFormat().toPattern();

		// State vars
		protoM = new HashMap<>();
		activeUnit = null;

		// Activate the default (prototype) unit
		addProtoUnit(aActiveUnit);
		activateProto(cfgTimeZone, cfgProtoName);
//		activateCustom(cfgTimeZone, cfgPattern);
	}

	/**
	 * Adds in the specified Unit as a prototype (which can be used to configure the active unit)
	 */
	public void addProtoUnit(DateUnit aUnit)
	{
		// First proto unit will be used as the default proto unit
		if (cfgProtoName == null)
			cfgProtoName = aUnit.getConfigName();

		protoM.put(aUnit.getConfigName(), aUnit);
	}

	/**
	 * Updates the activeUnit with the specified custom configuration
	 */
	public void activateCustom(TimeZone aTimeZone, String aPattern)
	{
		cfgTimeZone = aTimeZone;
		cfgCustomPattern = aPattern;
		isCustom = true;

		activeUnit = buildDateUnit("Custom", aPattern, cfgTimeZone);
		notifyListeners();
	}

	/**
	 * Updates the activeUnit with the specified proto configuration
	 */
	public void activateProto(TimeZone aTimeZone, String aProtoName)
	{
		DateUnit protoUnit;

		// Ensure this proto unit is installed
		protoUnit = protoM.get(aProtoName);
		if (protoUnit == null)
			throw new RuntimeException("Specified name is not installed as a prototype! aProtoName: " + aProtoName);

		cfgTimeZone = aTimeZone;
		cfgProtoName = aProtoName;
		isCustom = false;

		activeUnit = buildDateUnit(cfgProtoName, protoUnit.getFormat().toPattern(), cfgTimeZone);
		notifyListeners();
	}

	/**
	 * Returns whether the current active unit is a custom unit
	 */
	public boolean getIsCustom()
	{
		return isCustom;
	}

	/**
	 * Returns the previously specified custom pattern
	 */
	public String getCustomPattern()
	{
		return cfgCustomPattern;
	}

	/**
	 * Returns the previously specified proto unit
	 */
	public DateUnit getProtoUnit()
	{
		return protoM.get(cfgProtoName);
	}

	/**
	 * Returns all of the valid proto names
	 */
	public List<String> getProtoNameList()
	{
		return new ArrayList<>(protoM.keySet());
	}

	@Override
	public DateUnit getUnit()
	{
		return activeUnit;
	}

	@Override
	public void zioRead(ZinStream aStream) throws IOException
	{
		// Read the configuration
		aStream.readVersion(0);

		isCustom = aStream.readBool();
		cfgTimeZone = TimeZone.getTimeZone(aStream.readString());
		cfgProtoName = aStream.readString();
		cfgCustomPattern = aStream.readString();

		// Activate the unit
		if (isCustom == true)
			activateCustom(cfgTimeZone, cfgCustomPattern);
		else
			activateProto(cfgTimeZone, cfgProtoName);
	}

	@Override
	public void zioWrite(ZoutStream aStream) throws IOException
	{
		// Write the configuration
		aStream.writeVersion(0);

		aStream.writeBool(isCustom);
		aStream.writeString(cfgTimeZone.getID());
		aStream.writeString(cfgProtoName);
		aStream.writeString(cfgCustomPattern);
	}

	/**
	 * Helper method to construct the Unit. Subclass this method if you wish to use an alternative DateUnit.
	 */
	protected DateUnit buildDateUnit(String aConfigName, String aPattern, TimeZone aTimeZone)
	{
		return new DateUnit(aConfigName, aPattern, aTimeZone);
	}

}
