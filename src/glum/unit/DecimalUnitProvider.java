package glum.unit;

import glum.zio.ZinStream;
import glum.zio.ZoutStream;

import java.io.IOException;
import java.text.Format;
import java.text.NumberFormat;
import java.util.List;

import com.google.common.collect.Lists;

public class DecimalUnitProvider extends BaseUnitProvider
{
	// State vars
	private List<Unit> protoUnitList;
	private Unit activeUnit;
	
	public DecimalUnitProvider(String aRefName, Unit aActiveUnit)
	{
		super(aRefName);
		
		protoUnitList = Lists.newLinkedList();
		activeUnit = aActiveUnit;
	}

	/**
	 * Updates the activeUnit with the specified configuration
	 */
	public void activate(Unit protoUnit, int decimalPlaces, boolean forceFullLabel)
	{
		String formatStr;

		// Insanity check
		if (protoUnit == null)
			return;

		// Build the format
		formatStr = "###,###,###,###,###,##0";
		if (decimalPlaces > 0)
		{
			formatStr += ".";
			for (int c1 = 0; c1 < decimalPlaces; c1++)
				formatStr += "0";
		}

		if (protoUnit instanceof HeuristicUnit)
			activeUnit = ((HeuristicUnit)protoUnit).spawnClone(decimalPlaces);
		else if (forceFullLabel == true)
			activeUnit = new NumberUnit(protoUnit.getLabel(true), protoUnit.getLabel(true), protoUnit.toUnit(1), formatStr);
		else
			activeUnit = new NumberUnit(protoUnit.getLabel(true), protoUnit.getLabel(false), protoUnit.toUnit(1), formatStr);
		
		notifyListeners();
	}

	/**
	 * Adds in the specified Unit as a prototype (which can be used to configure
	 * the active unit)
	 */
	public void addProtoUnit(Unit aProtoUnit)
	{
		// Insanity check
		if (aProtoUnit instanceof HeuristicUnit == false && aProtoUnit instanceof NumberUnit == false)
			throw new RuntimeException("ProtoUnit must either be of type HeuristicUnit or NumberUnit");
			
		protoUnitList.add(aProtoUnit);
	}

	/**
	 * Returns the number of decimal places allowed in the current unit
	 */
	public int getDecimalPlaces()
	{
		Format aFormat;

		aFormat = activeUnit.getFormat();
		if (aFormat instanceof NumberFormat)
			return ((NumberFormat)aFormat).getMaximumFractionDigits();

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
		for (Unit aUnit : protoUnitList)
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
		return Lists.newArrayList(protoUnitList);
	}
	
	@Override
	public String getConfigName()
	{
		return activeUnit.getConfigName();
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
		if (protoUnitIdx >=0 && protoUnitIdx < protoUnitList.size())
			protoUnit = protoUnitList.get(protoUnitIdx);
		activate(protoUnit, decimalPlaces, forceFullLabel);
	}

	@Override
	public void zioWrite(ZoutStream aStream) throws IOException
	{
		int protoUnitIdx;
		int decimalPlaces;
		boolean forceFulLabel;
		
		// Retrieve the configuration
		protoUnitIdx = protoUnitList.indexOf(getProtoUnit());
		decimalPlaces = getDecimalPlaces();
		forceFulLabel = getForceFullLabel();

		// Write the stream's contents
		aStream.writeVersion(0);
		
		aStream.writeInt(protoUnitIdx);
		aStream.writeInt(decimalPlaces);
		aStream.writeBool(forceFulLabel);
	}

}
