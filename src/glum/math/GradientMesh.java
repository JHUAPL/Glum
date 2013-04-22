package glum.math;

import static java.lang.Math.*;
import java.util.*;

import glum.coord.*;

public class GradientMesh
{
	// Level of detail
	int lod;
	int bitMask;

	// Reference gradients
	double[] grad1D;
	Point2D[] grad2D;
	Point3D[] grad3D;

	/**
	* Constructor
	*/
	public GradientMesh(long aSeed, int aLod)
	{
		Random aRand;

		// lod: Level of detail. The number of reference gradients for
		// each dimension is equal to 2^lod. Thus we support [64, 1048576]
		// reference gradients. Erroneous input will be silently clamped.
		lod = aLod;
		if (lod < 6)
			lod = 6;
		else if (lod > 20)
			lod = 20;

		// Compute the bit mask associated with lod
		bitMask = 0;
		for (int c1 = 0; c1 < lod; c1++)
			bitMask = (bitMask << 1) + 1;

		// Construct our reference items
		aRand = new Random(aSeed);
		constructMesh1D(aRand);
		constructMesh2D(aRand);
//aRand = new Random(3432);
		constructMesh3D(aRand);
	}

	public double getGrad1D(int randVal)
	{
		return grad1D[randVal & bitMask];
	}

	public Point2D getGrad2D(int randVal)
	{
		return grad2D[randVal & bitMask];
	}

	public Point3D getGrad3D(int randVal)
	{
		return grad3D[randVal & bitMask];
	}

	/**
	* constructMesh1D
	*/
	protected void constructMesh1D(Random aRand)
	{
		int numItems;

		numItems = 1 << lod;
		grad1D = new double[numItems];
		for (int c1 = 0; c1 < numItems; c1++)
		{
			if (aRand.nextDouble() < 0.5)
				grad1D[c1] = -1;
			else
				grad1D[c1] = 1;
		}
	}

	/**
	* constructMesh2D
	*/
	protected void constructMesh2D(Random aRand)
	{
		Point2D aPt;
		double theta;		
		int numItems;

		numItems = 1 << lod;
		grad2D = new Point2D[numItems];
		for (int c1 = 0; c1 < numItems; c1++)
		{
			// Compute a random point on the unit circle.
			aPt = new Point2D();

			theta = 2 * PI * aRand.nextDouble();
			aPt.x = cos(theta);
			aPt.y = sin(theta);
			grad2D[c1] = aPt;
/*
			while (true)
			{
				aPt.x = 1.0 - aRand.nextDouble()*2;
				aPt.z = 1.0 - aRand.nextDouble()*2;

				if (aPt.x * aPt.x + aPt.y * aPt.y <= 1.0)
					break;
			}

			len = sqrt((aPt.x * aPt.x) + (aPt.y * aPt.y));
			aPt.x = aPt.x / len;
			aPt.y = aPt.y / len;
			grad3D[c1] = aPt;
*/
		}
	}

	/**
	* constructMesh3D
	*/
	protected void constructMesh3D(Random aRand)
	{
		Point3D aPt;
		double u, v, theta, phi;		
		int numItems;

		numItems = 1 << lod;
		grad3D = new Point3D[numItems];
		for (int c1 = 0; c1 < numItems; c1++)
		{
			// Compute a random point on a sphere. The logic is taken from the site:
			// http://mathworld.wolfram.com/SpherePointPicking.html
			// Note you can not pick 3 points x,y, and z and then normalize them to
			// the unit vector because these points are constrained by the equation:
			// x^2 + y^2 + z^2 = 1
			aPt = new Point3D();

			u = aRand.nextDouble();
			v = aRand.nextDouble();

			theta = 2 * PI * u;
			phi = acos(2*v - 1);

			// Go from phi, theta to x,y,z on the unit sphere
			aPt.x = 1.0*cos(theta)*sin(phi);
			aPt.y = 1.0*sin(theta)*sin(phi);
			aPt.z = 1.0*cos(phi);
			grad3D[c1] = aPt;
/*
			while (true)
			{
				aPt.x = 1.0 - aRand.nextDouble()*2;
				aPt.y = 1.0 - aRand.nextDouble()*2;
				aPt.z = 1.0 - aRand.nextDouble()*2;

				if (aPt.x * aPt.x + aPt.y * aPt.y +  aPt.z * aPt.z  <= 1.0)
					break;
			}

			len = sqrt((aPt.x * aPt.x) + (aPt.y * aPt.y) + (aPt.z * aPt.z));
			aPt.x = aPt.x / len;
			aPt.y = aPt.y / len;
			aPt.z = aPt.z / len;
			grad3D[c1] = aPt;
*/
		}
	}

}
