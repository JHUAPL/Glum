package glum.math;

import java.util.*;

public class GroupNoise implements Noise
{
	// Collection of individual noise objects
	protected Collection<Noise> myNoiseList;

	/**
	 * Constructor
	 */
	public GroupNoise()
	{
		myNoiseList = new LinkedList<Noise>();
	}

	public GroupNoise(Collection<Noise> aNoiseList)
	{
		myNoiseList = new LinkedList<Noise>(aNoiseList);
	}

	/**
	 * Adds a Noise object to this GroupNoise
	 */
	public void add(Noise aNoise)
	{
		myNoiseList.add(aNoise);
	}

	@Override
	public double getAmplitude()
	{
		double maxAmp, aAmp;

		maxAmp = 0;
		for (Noise aNoise : myNoiseList)
		{
			aAmp = aNoise.getAmplitude();
			if (aAmp > maxAmp)
				maxAmp = aAmp;
		}

		return maxAmp;
	}

	/**
	 * getNoiseList - Returns the list of individual noises
	 */
	public Collection<Noise> getNoiseList()
	{
		List<Noise> aNoiseList;

		aNoiseList = new LinkedList<Noise>(myNoiseList);
		return aNoiseList;
	}

	@Override
	public double getValue1D(double x)
	{
		double total;

		total = 0;
		for (Noise aNoiseGen : myNoiseList)
			total += aNoiseGen.getValue1D(x);

		return total;
	}

	@Override
	public double getValue2D(double x, double y)
	{
		double total;

		total = 0;
		for (Noise aNoiseGen : myNoiseList)
			total += aNoiseGen.getValue2D(x, y);

		return total;
	}

	@Override
	public double getValue3D(double x, double y, double z)
	{
		double total;

		total = 0;
		for (Noise aNoiseGen : myNoiseList)
			total += aNoiseGen.getValue3D(x, y, z);

		return total;
	}

}
