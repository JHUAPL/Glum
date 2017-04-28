package glum.filter;

import glum.zio.ZinStream;
import glum.zio.ZoutStream;

import java.io.IOException;

/**
 * A Filter which does not filter anything. Thus the method isValid() always returns true.
 */
public class NullFilter<G1> implements Filter<G1>
{

	@Override
	public boolean isValid(G1 aItem)
	{
		return true;
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
