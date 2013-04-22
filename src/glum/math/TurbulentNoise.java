package glum.math;

public class TurbulentNoise implements Noise
{
	// State vars
	protected Noise refNoise;

	/**
	 * Constructor
	 */
	public TurbulentNoise(Noise aRefNoise)
	{
		refNoise = aRefNoise;
	}

	@Override
	public double getAmplitude()
	{
		return refNoise.getAmplitude();
	}

	@Override
	public double getValue1D(double x)
	{
		return Math.abs(refNoise.getValue1D(x));
	}

	@Override
	public double getValue2D(double x, double y)
	{
		return Math.abs(refNoise.getValue2D(x, y));
	}

	@Override
	public double getValue3D(double x, double y, double z)
	{
		return Math.abs(refNoise.getValue3D(x, y, z));
	}

}
