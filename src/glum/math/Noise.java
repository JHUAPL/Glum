package glum.math;

public interface Noise
{
	/**
	 * getAmplitude - Returns the maximum offset produced by this noise
	 */
	public double getAmplitude();

	/**
	 * getValue1D - Returns a continous random value in the range of [0-1] in 1D space. It is repeatable for the same x
	 * values.
	 */
	public double getValue1D(double x);

	/**
	 * getValue2D - Returns a continous random value in the range of [0-1] in 2D space. It is repeatable for the same x,y
	 * values.
	 */
	public double getValue2D(double x, double y);

	/**
	 * getValue3D - Returns a continous random value in the range of [0-1] in 3D space. It is repeatable for the same
	 * x,y,z values.
	 */
	public double getValue3D(double x, double y, double z);

}
