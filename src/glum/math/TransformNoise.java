package glum.math;

public class TransformNoise implements Noise
{
	// State vars
	protected Noise refNoise;
	protected double offsetVal;
	protected double scalarVal;

	/**
	 * Constructor
	 */
	public TransformNoise(Noise aRefNoise, double aOffsetVal, double aScalarVal)
	{
		refNoise = aRefNoise;
		offsetVal = aOffsetVal;
		scalarVal = aScalarVal;
	}

	@Override
	public double getAmplitude()
	{
		return scalarVal * refNoise.getAmplitude();
	}

	@Override
	public double getValue1D(double x)
	{
		return offsetVal + (scalarVal * refNoise.getValue1D(x));
	}

	@Override
	public double getValue2D(double x, double y)
	{
		return offsetVal + (scalarVal * refNoise.getValue2D(x, y));
	}

	@Override
	public double getValue3D(double x, double y, double z)
	{
		return offsetVal + (scalarVal * refNoise.getValue3D(x, y, z));
	}

}
