package glum.unit;

import glum.zio.ZinStream;
import glum.zio.ZoutStream;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

public class DateUnitProvider extends BaseUnitProvider
{
	// Config vars
	private boolean isCustom;
	private TimeZone cfgTimeZone;
	private String cfgProtoName;
	private String cfgCustomPattern;

	// State vars
	private Map<String, DateUnit> protoMap;
	private DateUnit activeUnit;

	public DateUnitProvider(String aRefName, DateUnit aActiveUnit)
	{
		super(aRefName);

		// Config vars
		isCustom = false;
		cfgTimeZone = aActiveUnit.getFormat().getTimeZone();
		cfgProtoName = null;
		cfgCustomPattern = aActiveUnit.getFormat().toPattern();

		// State vars
		protoMap = Maps.newHashMap();
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

		protoMap.put(aUnit.getConfigName(), aUnit);
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
		protoUnit = protoMap.get(aProtoName);
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
		return protoMap.get(cfgProtoName);
	}

	/**
	 * Returns all of the valid proto names
	 */
	public List<String> getProtoNameList()
	{
		return Lists.newArrayList(protoMap.keySet());
	}

	@Override
	public String getConfigName()
	{
		return activeUnit.getConfigName();
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
