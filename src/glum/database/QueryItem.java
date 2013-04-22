package glum.database;

public abstract interface QueryItem<G1 extends Enum<?>>
{
	/**
	 * Returns the corresponding value associated with aEnum
	 */
	public Object getValue(G1 aEnum);

	/**
	 * Sets in the aObj as the corresponding value to aEnum
	 */
	public void setValue(G1 aEnum, Object aObj);

}
