package glum.filter;

import glum.zio.raw.ZioRaw;

public interface Filter<G1> extends ZioRaw
{
	/**
	 * Method that returns true if aItem passes this filter
	 */
	public boolean isValid(G1 aItem);

}
