package glum.unit;

import glum.zio.ZinStream;
import glum.zio.ZoutStream;

import java.io.IOException;

public class EmptyUnitProvider implements UnitProvider
{
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
		return "None";
	}

	@Override
	public String getDisplayName()
	{
		return "Null Unit";
	}

	@Override
	public Unit getUnit()
	{
		return null;
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
