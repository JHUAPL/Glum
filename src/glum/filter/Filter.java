package glum.filter;

import glum.zio.ZioObj;

public interface Filter<G1> extends ZioObj
{
	/**
	 * Method that returns true if aItem passes this filter
	 */
	public boolean isValid(G1 aItem);

}
