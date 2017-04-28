package glum.unit;

import glum.zio.ZinStream;
import glum.zio.ZoutStream;

import java.io.IOException;
import java.text.Format;
import java.text.NumberFormat;

public class LatLonUnitProvider extends BaseUnitProvider
{
	// State vars
	private Unit latUnit, lonUnit;

	public LatLonUnitProvider(String aRefName)
	{
		super(aRefName);

		activateStandard(false);
	}

	/**
	 * Updates the active lat,lon unit to be configured in raw format
	 */
	public void activateRaw(int aDecimalPlaces, boolean aIsZeroCentered)
	{
		// Set in the unit
		if (aIsZeroCentered == true)
		{
			latUnit = new ShiftedUnit("", "", 0, aDecimalPlaces);
			lonUnit = new ShiftedUnit("", "", 0, aDecimalPlaces);
		}
		else
		{
			latUnit = new ShiftedUnit("", "", 90, aDecimalPlaces);
			lonUnit = new ShiftedUnit("", "", 180, aDecimalPlaces);
		}

		notifyListeners();
	}

	/**
	 * Updates the active lat,lon unit to be configured in standard format
	 */
	public void activateStandard(boolean aIsSecondsShown)
	{
		latUnit = new LatUnit(aIsSecondsShown);
		lonUnit = new LonUnit(aIsSecondsShown);
		notifyListeners();
	}

	/**
	 * Returns the number of decimal places used in the raw unit.
	 */
	public int getDecimalPlaces()
	{
		Format aFormat;

		aFormat = latUnit.getFormat();
		if (aFormat instanceof NumberFormat)
			return ((NumberFormat)aFormat).getMaximumFractionDigits();

		return 0;
	}

	/**
	 * Returns the active lat unit
	 */
	public Unit getLatUnit()
	{
		return latUnit;
	}

	/**
	 * Returns the active lon unit
	 */
	public Unit getLonUnit()
	{
		return lonUnit;
	}

	/**
	 * Returns whether the raw unit is zero centered. This is true only for properly configured active raw units.
	 */
	public boolean isZeroCentered()
	{
		if (latUnit instanceof LatUnit)
			return false;

		return (latUnit.toModel(0) == 0);
	}

	/**
	 * Returns whether the active lat,lon unit is set to display in the raw format
	 */
	public boolean isRawUnitActive()
	{
		if (latUnit instanceof LatUnit)
			return false;

		return true;
	}

	/**
	 * Returns whether the active lat,lon unit will display seconds. This is true only for properly configured active
	 * standard units.
	 */
	public boolean isSecondsShown()
	{
		if (latUnit instanceof LatUnit)
			return ((LatUnit)latUnit).isSecondsShown();

		return false;
	}

	@Override
	public String getConfigName()
	{
		if (latUnit instanceof LatUnit)
			return "Standard";

		return "Raw";
	}

	@Override
	public Unit getUnit()
	{
		throw new RuntimeException("Please use getLatUnit() or getLonUnit() instead.");
	}

	@Override
	public void zioRead(ZinStream aStream) throws IOException
	{
		int decimalPlaces;
		boolean isRawUnit, isZeroCentered, isSecondsShown;

		// Read the stream's content
		aStream.readVersion(0);

		isRawUnit = aStream.readBool();
		decimalPlaces = aStream.readInt();
		isZeroCentered = aStream.readBool();
		isSecondsShown = aStream.readBool();

		// Install the configuration
		if (isRawUnit == true)
			activateRaw(decimalPlaces, isZeroCentered);
		else
			activateStandard(isSecondsShown);
	}

	@Override
	public void zioWrite(ZoutStream aStream) throws IOException
	{
		int decimalPlaces;
		boolean isRawUnit, isZeroCentered, isSecondsShown;

		// Retrieve the configuration
		isRawUnit = isRawUnitActive();
		decimalPlaces = getDecimalPlaces();
		isZeroCentered = isZeroCentered();
		isSecondsShown = isSecondsShown();

		// Write the stream's contents
		aStream.writeVersion(0);

		aStream.writeBool(isRawUnit);
		aStream.writeInt(decimalPlaces);
		aStream.writeBool(isZeroCentered);
		aStream.writeBool(isSecondsShown);
	}

}
