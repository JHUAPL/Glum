package	glum.coord;

/** Determines if two numbers are close, usually as a way to say
* that they are equal.  Close is defined to mean that their difference
* is less than some small number, which is either supplied by the caller
* or is EPSILON.
* <p>For longitude near the equator, a difference of EPSILON is about
* 3.65 feet (where the earth's circumference is about 21913.3 DM, or
* about 60.87 DM per degree longitude).  For DataMile measurements, it's
* about 0.72 inches.
*/

public class Epsilon
{
	/** The measure of closeness; set to 0.00001. */
	public static final double	EPSILON  = 0.00001;

	public static boolean close (float a, float b)
	{
		float	diff = a - b;
		return diff < EPSILON && diff > -EPSILON;
	}

	public static boolean close (float a, float b, float epsilon)
	{
		float	diff = a - b;
		return diff < epsilon && diff > -epsilon;
	}

	public static boolean close (double a, double b)
	{
		double	diff = a - b;
		return diff < EPSILON && diff > -EPSILON;
	}

	public static boolean close (double a, double b, float epsilon)
	{
		double	diff = a - b;
		return diff < EPSILON && diff > -EPSILON;
	}

	private Epsilon () { }
}
