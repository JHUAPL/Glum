package glum.math;

final public class FastMath
{

		private FastMath(){}


	/** A "close to zero" double epsilon value for use*/
	public static final double DBL_EPSILON = 2.220446049250313E-16d;


	/** A "close to zero" double epsilon value for use*/
	public static final double FLT_EPSILON = 1.1920928955078125E-7d;


	/** A "close to zero" double epsilon value for use*/
	public static final double ZERO_TOLERANCE = 0.0001d;

	public static final double ONE_THIRD = 1.0/3.0;


	/** The value PI as a double. (180 degrees) */
	public static final double PI = Math.PI;


	/** The value 2PI as a double. (360 degrees) */
	public static final double TWO_PI = 2.0 * PI;


	/** The value PI/2 as a double. (90 degrees) */
	public static final double HALF_PI = 0.5 * PI;


	/** The value PI/4 as a double. (45 degrees) */
	public static final double QUARTER_PI = 0.25 * PI;


	/** The value 1/PI as a double. */
	public static final double INV_PI = 1.0 / PI;


	/** The value 1/(2PI) as a double. */
	public static final double INV_TWO_PI = 1.0 / TWO_PI;


	/** A value to multiply a degree value by, to convert it to radians. */
	public static final double DEG_TO_RAD = PI / 180.0;


	/** A value to multiply a radian value by, to convert it to degrees. */
	public static final double RAD_TO_DEG = 180.0 / PI;


	/** A precreated random object for random numbers. */
	//!    public static final Random rand = new Random(System.currentTimeMillis());




    /**
     * Returns true if the number is a power of 2 (2,4,8,16...)
     *
     * A good implementation found on the Java boards. note: a number is a power
     * of two if and only if it is the smallest number with that number of
     * significant bits. Therefore, if you subtract 1, you know that the new
     * number will have fewer bits, so ANDing the original number with anything
     * less than it will give 0.
     *
     * @param number
     *            The number to test.
     * @return True if it is a power of two.
     */
    public static boolean isPowerOfTwo(int number) {
        return (number > 0) && (number & (number - 1)) == 0;
    }

    public static int nearestPowerOfTwo(int number) {
        return (int)Math.pow(2, Math.ceil(Math.log(number) / Math.log(2)));
    }


    /**
     * Linear interpolation from startValue to endValue by the given percent.
     * Basically: ((1 - percent) * startValue) + (percent * endValue)
     *
     * @param percent
     *            Percent value to use.
     * @param startValue
     *            Begining value. 0% of f
     * @param endValue
     *            ending value. 100% of f
     * @return The interpolated value between startValue and endValue.
     */
    public static double LERP(double percent, double startValue, double endValue) {
        if (startValue == endValue) return startValue;
        return ((1 - percent) * startValue) + (percent * endValue);
    }




     /**
     * Returns the arc cosine of an angle given in radians.<br>
     * Special cases:
     * <ul><li>If fValue is smaller than -1, then the result is PI.
     * <li>If the argument is greater than 1, then the result is 0.</ul>
     * @param fValue The angle, in radians.
     * @return fValue's acos
     * @see java.lang.Math#acos(double)
     */
    public static double acos(double fValue) {
        if (-1.0f < fValue) {
            if (fValue < 1.0f)
                return Math.acos(fValue);

            return 0.0f;
        }

        return PI;
    }


     /**
     * Returns the arc sine of an angle given in radians.<br>
     * Special cases:
     * <ul><li>If fValue is smaller than -1, then the result is -HALF_PI.
     * <li>If the argument is greater than 1, then the result is HALF_PI.</ul>
     * @param fValue The angle, in radians.
     * @return fValue's asin
     * @see java.lang.Math#asin(double)
     */
    public static double asin(double fValue) {
        if (-1.0f < fValue) {
            if (fValue < 1.0f)
                return Math.asin(fValue);

            return HALF_PI;
        }

        return -HALF_PI;
    }


     /**
     * Returns the arc tangent of an angle given in radians.<br>
     * @param fValue The angle, in radians.
     * @return fValue's asin
     * @see java.lang.Math#atan(double)
     */
    public static double atan(double fValue) {
        return Math.atan(fValue);
    }


    /**
     * A direct call to Math.atan2.
     * @param fY
     * @param fX
     * @return Math.atan2(fY,fX)
     * @see java.lang.Math#atan2(double, double)
     */
    public static double atan2(double fY, double fX) {
        return Math.atan2(fY, fX);
    }


    /**
     * Rounds a fValue up.  A call to Math.ceil
     * @param fValue The value.
     * @return The fValue rounded up
     * @see java.lang.Math#ceil(double)
     */
    public static double ceil(double fValue) {
        return Math.ceil(fValue);
    }

    /**
     * Fast Trig functions for x86. This forces the trig functiosn to stay
     * within the safe area on the x86 processor (-45 degrees to +45 degrees)
     * The results may be very slightly off from what the Math and StrictMath
     * trig functions give due to rounding in the angle reduction but it will be
     * very very close.
     *
     * note: code from wiki posting on java.net by jeffpk
     */
    public static double reduceSinAngle(double radians) {
        radians %= TWO_PI; // put us in -2PI to +2PI space
        if (Math.abs(radians) > PI) { // put us in -PI to +PI space
            radians = radians - (TWO_PI);
        }
        if (Math.abs(radians) > HALF_PI) {// put us in -PI/2 to +PI/2 space
            radians = PI - radians;
        }


        return radians;
    }


    /**
     * Returns sine of a value.
     *
     * note: code from wiki posting on java.net by jeffpk
     *
     * @param fValue
     *            The value to sine, in radians.
     * @return The sine of fValue.
     * @see java.lang.Math#sin(double)
     */
/*!    public static double sin(double fValue) {
        fValue = reduceSinAngle(fValue); // limits angle to between -PI/2 and +PI/2
        if (Math.abs(fValue)<=Math.PI/4){
           return Math.sin(fValue);
        }

        return Math.cos(Math.PI/2-fValue);
    }
*/

    /**
     * Returns cos of a value.
     *
     * @param fValue
     *            The value to cosine, in radians.
     * @return The cosine of fValue.
     * @see java.lang.Math#cos(double)
     */
/*!    public static double cos(double fValue) {
        return sin(fValue+HALF_PI);
    }
*/

    /**
     * Returns E^fValue
     * @param fValue Value to raise to a power.
     * @return The value E^fValue
     * @see java.lang.Math#exp(double)
     */
/*!    public static double exp(double fValue) {
        return Math.exp(fValue);
    }
*/

    /**
     * Returns Absolute value of a double.
     * @param fValue The value to abs.
     * @return The abs of the value.
     * @see java.lang.Math#abs(double)
     */
    public static double abs(double fValue) {
        if (fValue < 0) return -fValue;
        return fValue;
    }


    /**
     * Returns a number rounded down.
     * @param fValue The value to round
     * @return The given number rounded down
     * @see java.lang.Math#floor(double)
     */
    public static double floor(double fValue) {
        return Math.floor(fValue);
    }


    /**
     * Returns 1/sqrt(fValue)
     * @param fValue The value to process.
     * @return 1/sqrt(fValue)
     * @see java.lang.Math#sqrt(double)
     */
    public static double invSqrt(double fValue) {
        return (1.0f / Math.sqrt(fValue));
    }


    /**
     * Returns the log base E of a value.
     * @param fValue The value to log.
     * @return The log of fValue base E
     * @see java.lang.Math#log(double)
     */
    public static double log(double fValue) {
        return Math.log(fValue);
    }

    /**
     * Returns the logarithm of value with given base, calculated as log(value)/log(base),
     * so that pow(base, return)==value (contributed by vear)
     * @param value The value to log.
     * @param base Base of logarithm.
     * @return The logarithm of value with given base
     */
    public static double log(double value, double base) {
        return (Math.log(value)/Math.log(base));
    }


    /**
     * Returns a number raised to an exponent power.  fBase^fExponent
     * @param fBase The base value (IE 2)
     * @param fExponent The exponent value (IE 3)
     * @return base raised to exponent (IE 8)
     * @see java.lang.Math#pow(double, double)
     */
/*!    public static double pow(double fBase, double fExponent) {
        return Math.pow(fBase, fExponent);
    }
*/

    /**
     * Returns the value squared.  fValue ^ 2
     * @param fValue The vaule to square.
     * @return The square of the given value.
     */
    public static double sqr(double fValue) {
        return fValue * fValue;
    }


    /**
     * Returns the square root of a given value.
     * @param fValue The value to sqrt.
     * @return The square root of the given value.
     * @see java.lang.Math#sqrt(double)
     */
    public static double sqrt(double fValue) {
        return Math.sqrt(fValue);
    }


    /**
     * Returns the tangent of a value.  If USE_FAST_TRIG is enabled, an approximate value
     * is returned.  Otherwise, a direct value is used.
     * @param fValue The value to tangent, in radians.
     * @return The tangent of fValue.
     * @see java.lang.Math#tan(double)
     */
    public static double tan(double fValue) {
        return Math.tan(fValue);
    }







    /**
     * Returns the integral value of a given value.
     * @param fValue The value to round.
     * @return The square root of the given value.
     * @see java.lang.Math#round(double)
     */
    public static double round(double fValue) {
        return Math.round(fValue);
    }


//compute sine
/*public static double sin(double x)
{
	double aAns;

	if (x < -3.14159265)
   	 x += 6.28318531;
	else if (x >  3.14159265)
   	 x -= 6.28318531;

	if (x < 0)
	{
		aAns = 1.27323954 * x + .405284735 * x * x;

		if (aAns < 0)
			aAns = .225 * (aAns *-aAns - aAns) + aAns;
		else
			aAns = .225 * (aAns * aAns - aAns) + aAns;
	}
	else
	{
		aAns = 1.27323954 * x - 0.405284735 * x * x;

		if (aAns < 0)
			aAns = .225 * (aAns *-aAns - aAns) + aAns;
		else
			aAns = .225 * (aAns * aAns - aAns) + aAns;
	}

	return aAns;
}


public static double cos(double x)
{
	return sin(x + 1.57079632);
}
*/

//compute cosine: sin(x + PI/2) = cos(x)
/*public static double cos(double x)
{
	double aAns;

	x += 1.57079632;
	if (x >  3.14159265)
		x -= 6.28318531;

	if (x < 0)
	{
		aAns = 1.27323954 * x + 0.405284735 * x * x;

		if (aAns < 0)
			aAns = .225 * (aAns *-aAns - aAns) + aAns;
		else
			aAns = .225 * (aAns * aAns - aAns) + aAns;
	}
	else
	{
		aAns = 1.27323954 * x - 0.405284735 * x * x;

		if (aAns < 0)
			aAns = .225 * (aAns *-aAns - aAns) + aAns;
		else
			aAns = .225 * (aAns * aAns - aAns) + aAns;
	}

	return aAns;
}
*/




public static final double invFact2 = 1.0/2.0;
public static final double invFact3 = 1.0/6.0;
public static final double invFact4 = 1.0/24.0;
public static final double invFact5 = 1.0/120.0;
public static final double invFact6 = 1.0/720.0;
public static final double invFact7 = 1.0/5040.0;
public static final double invFact8 = 1.0/40320.0;
public static final double invFact9 = 1.0/362880.0;
public static final double invFact11 = 1.0/39916800.0;
public static final double invFact13 = 1.0/6227020800.0;
public static final double invFact15 = 1.0/1307674368000.0;

public static final double invFact2n = -1.0/2.0;
public static final double invFact3n = -1.0/6.0;
public static final double invFact4n = -1.0/24.0;
public static final double invFact5n = -1.0/120.0;
public static final double invFact6n = -1.0/720.0;
public static final double invFact7n = -1.0/5040.0;
public static final double invFact8n = -1.0/40320.0;
public static final double invFact9n = -1.0/362880.0;
public static final double invFact11n = -1.0/39916800.0;


/*public static double cos(double x)
{
	double x2, x4, x6;

	while (x < -Math.PI)
   	 x += TWO_PI;

	while (x > Math.PI)
   	 x -= TWO_PI;

	x2 = x * x;
	x4 = x2 * x2;
	x6 = x4 * x2;

	return 1 - invFact2*x2 + invFact4*x4 - invFact6*x6;
}*/
public static double cos(double x)
{
	return sin(x + HALF_PI);
}


public static double sin(double x)
{
//	double x2, x3, x5, x7;
//	double x2, x3, x4;
	double x2, x3;

	while (x < -Math.PI)
   	 x += TWO_PI;

	while (x > Math.PI)
   	 x -= TWO_PI;

//	x2 = x * x;
//	x3 = x2 * x;
//	x5 = x3 * x2;
//	x7 = x5 * x2;
//	return x - invFact3*x3 + invFact5*x5 - invFact7*x7;


//	x2 = x * x;
//	x3 = x2 * x;
//	x4 = x3 * x;
//	return x + x3*(-invFact3 + invFact5*x2 - invFact7*x4)

//	x2 = x * x;
//	x3 = x2 * x;
//	return x + x3*(-invFact3 + x2*(invFact5 - invFact7*x2));

//	x2 = x * x;
//	x3 = x2 * x;
//	return x + x3*(-invFact3 + x2*(invFact5 + x2*(-invFact7 + x2*(invFact9 - x2*invFact11))));

	x2 = x * x;
	x3 = x2 * x;
	return x + x3*(-invFact3 + x2*(invFact5 + x2*(-invFact7
	         + x2*(invFact9 + x2*(-invFact11 + x2*invFact13)))));
}
//public static double sin(double x)
//{
//	return cos(x - 1.57079632679);  //! This does not work
//}




/*//compute sine
public static double sin(double x)
{
	double aAns;

	if (x < -3.14159265)
   	 x += 6.28318531;
	else if (x >  3.14159265)
   	 x -= 6.28318531;

	if (x < 0)
		aAns = 1.27323954 * x + 0.405284735 * x * x;
	else
		aAns = 1.27323954 * x - 0.405284735 * x * x;

	return aAns;
}


//compute cosine: sin(x + PI/2) = cos(x)
public static double cos(double x)
{
	double aAns;

	x += 1.57079632;
	if (x >  3.14159265)
		x -= 6.28318531;

	if (x < 0)
		aAns = 1.27323954 * x + 0.405284735 * x * x;
	else
		aAns = 1.27323954 * x - 0.405284735 * x * x;

	return aAns;
}
*/















	/**
	* pow - Method to replace Math.pow().
	* THis method should be faster but results in a coarser solution.
	* Grabed from: http://martin.ankerl.com/2007/10/04/optimized-pow-approximation-for-java-and-c-c/
	*/
	public static double pow(final double a, final double b)
	{
		final int x = (int) (Double.doubleToLongBits(a) >> 32);
		final int y = (int) (b * (x - 1072632447) + 1072632447);
		return Double.longBitsToDouble(((long) y) << 32);
	}



	/**
	* exp - Method to replace Math.exp().
	* THis method should be faster but results in a coarser solution.
	* Grabed from: http://martin.ankerl.com/2007/10/04/optimized-pow-approximation-for-java-and-c-c/
	*/
	public static double exp(double val)
	{
		final long tmp = (long) (1512775 * val + (1072693248 - 60801));
		return Double.longBitsToDouble(tmp << 32);
	}



	public static double min(double aVal, double bVal)
	{
//		return Math.min(aVal, bVal);
		if (aVal < bVal)
			return aVal;

		return bVal;
	}


}