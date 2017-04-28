package glum.unit;

import glum.zio.ZinStream;
import glum.zio.ZoutStream;

import java.io.IOException;

/**
 * UnitProvider that always returns the same Unit.
 */
public class ConstUnitProvider implements UnitProvider
{
	// State vars
	private Unit activeUnit;

	public ConstUnitProvider(Unit aUnit)
	{
		activeUnit = aUnit;
	}

	@Override
	public void addListener(UnitListener aListener)
	{
		; // Nothing to do
	}

	@Override
	public void removeListener(UnitListener aListener)
	{
		; // Nothing to do
	}

	@Override
	public String getConfigName()
	{
		return "Const";
	}

	@Override
	public String getDisplayName()
	{
		return "Const";
	}

	@Override
	public Unit getUnit()
	{
		return activeUnit;
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
