package glum.unit;

import java.text.*;

public interface Unit
{
	/**
	 * Returns the formal name associated with the unit
	 */
	public String getConfigName();

	/**
	 * Returns Format object associated with the unit.
	 */
	public Format getFormat();

	/**
	 * Returns the label associated with the unit.
	 */
	public String getLabel(boolean isDetailed);
	
	/**
	 * Returns string representation of aVal w.r.t this unit without the
	 * associated label.
	 */
	public String getString(Object aVal);

	/**
	 * Returns string representation of aVal w.r.t this unit with the associated
	 * (detailed if isDetailed == true) label.
	 */
	public String getString(Object aVal, boolean isDetailed);

	/**
	 * Returns the model value which corresponds to the specified input string.
	 * The input string should be in this unit. If no value can be parsed then
	 * eVal is returned.
	 */
	public double parseString(String aStr, double eVal);

	/**
	 * Assume aVal is in units of this and returns it as model units.
	 */
	public double toModel(double aVal);

	/**
	 * Returns aVal in terms of this unit. Note aVal is assumed to be in model
	 * units.
	 */
	public double toUnit(double aVal);

}
