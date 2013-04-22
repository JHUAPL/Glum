package glum.math;

import static java.lang.Math.*;
import java.util.*;

import glum.coord.*;

public class PerlinNoise implements Noise
{
	// Seed key values
	protected GradientMesh myGradientMesh;
	protected FractionTransformer myFracTransformer;
	protected double amp, freq;

	protected int[] p0Table, p1Table, p2Table,  p3Table;
	protected int[] p4Table, p5Table, p6Table,  p7Table;
	protected int[] p8Table, p9Table, p10Table, p11Table;

	/**
	* Constructor
	*/
	public PerlinNoise(long aSeed)
	{
		this(aSeed, null);
	}

	public PerlinNoise(long aSeed, GradientMesh aGradientMesh)
	{
		Vector<Integer> aList;
		Random aRandom;

		// Set up our rand generater
		aRandom = new Random(aSeed);

		// Set up the fraction tranform and function
		myFracTransformer = FractionTransformer.Cubic;

		// If no GradientMesh specified then construct our own reference gradient
		//  mesh with 2^12 reference points.
		myGradientMesh = aGradientMesh;
		if (myGradientMesh == null)
			myGradientMesh = new GradientMesh(aRandom.nextLong(), 12);

		// Construct the words to use in the permutation
		aList = new Vector<Integer>();
		for (int c1 = 0; c1 < 256; c1++)
			aList.add(c1);

		Collections.shuffle(aList, aRandom);
		p0Table = new int[256];
		for (int c1 = 0; c1 < 256; c1++)
			p0Table[c1] = aList.get(c1) << 0;

		Collections.shuffle(aList, aRandom);
		p1Table = new int[256];
		for (int c1 = 0; c1 < 256; c1++)
			p1Table[c1] = aList.get(c1) << 8;

		Collections.shuffle(aList, aRandom);
		p2Table = new int[256];
		for (int c1 = 0; c1 < 256; c1++)
			p2Table[c1] = aList.get(c1) << 16;

		Collections.shuffle(aList, aRandom);
		p4Table = new int[256];
		for (int c1 = 0; c1 < 256; c1++)
			p4Table[c1] = aList.get(c1) << 0;

		Collections.shuffle(aList, aRandom);
		p5Table = new int[256];
		for (int c1 = 0; c1 < 256; c1++)
			p5Table[c1] = aList.get(c1) << 8;

		Collections.shuffle(aList, aRandom);
		p6Table = new int[256];
		for (int c1 = 0; c1 < 256; c1++)
			p6Table[c1] = aList.get(c1) << 16;

		Collections.shuffle(aList, aRandom);
		p8Table = new int[256];
		for (int c1 = 0; c1 < 256; c1++)
			p8Table[c1] = aList.get(c1) << 0;

		Collections.shuffle(aList, aRandom);
		p9Table = new int[256];
		for (int c1 = 0; c1 < 256; c1++)
			p9Table[c1] = aList.get(c1) << 8;

		Collections.shuffle(aList, aRandom);
		p10Table = new int[256];
		for (int c1 = 0; c1 < 256; c1++)
			p10Table[c1] = aList.get(c1) << 16;

/*
int x = 0xF0F0F0F0;
System.out.print("   x[0]:"  + ((x & 0x000000ff) >>  0) );
System.out.print("   x[1]:"  + ((x & 0x0000ff00) >>  8) );
System.out.print("   x[2]:"  + ((x & 0x00ff0000) >> 16) );
System.out.println("  x[3]:" + ((x & 0xff000000) >> 24) );

System.out.println("Hmm...");
System.out.print("   x[0]:"  + ((x >>  0) & 0xff) );
System.out.print("   x[1]:"  + ((x >>  8) & 0xff) );
System.out.print("   x[2]:"  + ((x >> 16) & 0xff) );
System.out.println("  x[3]:" + ((x >> 24) & 0xff) );
System.exit(0);
*/
	}


	/**
	* setPersistence - Set the amplitude and frequency associated
	* with the noise function.
	*/
	public void setPersistence(double aAmp, double aFreq)
	{
		amp = aAmp;
		freq = aFreq;
	}


	@Override
	public double getAmplitude()
	{
		return amp;
	}




	@Override
	public double getValue1D(double x)
	{
		return getInterpolatedValue1D(x * freq) * amp;
	}


	public double getInterpolatedValue1D(double x)
	{
		int iX;
		double fracX;
		double left, right;

		iX = (int)floor(x);
		fracX = x - iX;
		fracX = myFracTransformer.transform(fracX);

		left = getSmoothValue1D(x, iX);
		right = getSmoothValue1D(x, iX + 1);

		return interpolate(left, right, fracX);
	}


	public double getSmoothValue1D(double x, int qX)
	{
		int vx;
//		int w1, w2, w3, w4;
		int w1, w2, w3;		
		double gradPt;
		double value;

		vx = qX;
		w1 = (vx >>  0) & 0xFF;
		w2 = (vx >>  8) & 0xFF;
		w3 = (vx >> 16) & 0xFF;
//		w4 = (vx >> 24) & 0xFF;
		w2 = w2 + w1;
		w3 = w3 + w2;
//		w4 = w4 + w3;
		vx = (p0Table[w1 & 0xff] ) | (p1Table[w2 & 0xff] ) | (p2Table[w3 & 0xff] );

		// Return a value from -1 to 1
		gradPt = myGradientMesh.getGrad1D(vx);
		value = gradPt * (x - qX);
		return value;
	}










	@Override
	public double getValue2D(double x, double y)
	{
		return getInterpolatedValue2D(x * freq, y * freq) * amp;
	}


	public double getInterpolatedValue2D(double x, double y)
	{
		int iX, iY;
		double fracX, fracY;
		double tL, tR, bL, bR, topRow, botRow;

		iX = (int)floor(x);
		fracX = x - iX;
		fracX = myFracTransformer.transform(fracX);

		iY = (int)floor(y);
		fracY = y - iY;
		fracY = myFracTransformer.transform(fracY);

		tL = getSmoothValue2D(x, y, iX,     iY);
		tR = getSmoothValue2D(x, y, iX + 1, iY);
		bL = getSmoothValue2D(x, y, iX,     iY + 1);
		bR = getSmoothValue2D(x, y, iX + 1, iY + 1);

		topRow = interpolate(tL, tR, fracX);
		botRow = interpolate(bL, bR, fracX);

		return interpolate(topRow, botRow, fracY);
	}


	public double getSmoothValue2D(double x, double y, int qX, int qY)
	{
		int vx, vy;
//		int w1, w2, w3, w4;
		int w1, w2, w3;		
		Point2D gradPt;
		double value;

		vx = qX;
		w1 = (vx >>  0) & 0xFF;
		w2 = (vx >>  8) & 0xFF;
		w3 = (vx >> 16) & 0xFF;
//		w4 = (vx >> 24) & 0xFF;
		w2 = w2 + w1;
		w3 = w3 + w2;
//		w4 = w4 + w3;
		vx = (p0Table[w1 & 0xff] ) | (p1Table[w2 & 0xff] ) | (p2Table[w3 & 0xff] );

		vy = vx + qY;
		w1 = (vy >>  0) & 0xFF;
		w2 = (vy >>  8) & 0xFF;
		w3 = (vy >> 16) & 0xFF;
//		w4 = (vy >> 24) & 0xFF;
		w2 = w2 + w1;
		w3 = w3 + w2;
//		w4 = w4 + w3;
		vy = (p4Table[w1 & 0xff] ) | (p5Table[w2 & 0xff] ) | (p6Table[w3 & 0xff] );

		// Return a value from -1 to 1
		gradPt = myGradientMesh.getGrad2D(vy);
		value = (gradPt.x * (x - qX)) + (gradPt.y * (y - qY));
		return value;
	}













	@Override
	public double getValue3D(double x, double y, double z)
	{
		return getInterpolatedValue3D(x * freq, y * freq, z * freq) * amp;
	}


	public double getInterpolatedValue3D(double x, double y, double z)
	{
		int iX, iY, iZ;
		double fracX, fracY, fracZ;
		double tL, tR, bL, bR, topRow, botRow;
		double planeZ1, planeZ2;

		iX = (int)floor(x);
		fracX = x - iX;
		fracX = myFracTransformer.transform(fracX);

		iY = (int)floor(y);
		fracY = y - iY;
		fracY = myFracTransformer.transform(fracY);

		iZ = (int)floor(z);
		fracZ = z - iZ;
		fracZ = myFracTransformer.transform(fracZ);

		tL = getSmoothValue3D(x, y, z, iX,     iY,     iZ);
		tR = getSmoothValue3D(x, y, z, iX + 1, iY,     iZ);
		bL = getSmoothValue3D(x, y, z, iX,     iY + 1, iZ);
		bR = getSmoothValue3D(x, y, z, iX + 1, iY + 1, iZ);
		topRow = interpolate(tL, tR, fracX);
		botRow = interpolate(bL, bR, fracX);
		planeZ1 = interpolate(topRow, botRow, fracY);

		tL = getSmoothValue3D(x, y, z, iX,     iY,     iZ + 1);
		tR = getSmoothValue3D(x, y, z, iX + 1, iY,     iZ + 1);
		bL = getSmoothValue3D(x, y, z, iX,     iY + 1, iZ + 1);
		bR = getSmoothValue3D(x, y, z, iX + 1, iY + 1, iZ + 1);
		topRow = interpolate(tL, tR, fracX);
		botRow = interpolate(bL, bR, fracX);
		planeZ2 = interpolate(topRow, botRow, fracY);

		return interpolate(planeZ1, planeZ2, fracZ);
	}


	public double getSmoothValue3D(double x, double y, double z, int qX, int qY, int qZ)
	{
		int vx, vy, vz;
//		int w1, w2, w3, w4;
		int w1, w2, w3;
		Point3D gradPt;
		double value;

		vx = qX;
		w1 = (vx >>  0) & 0xFF;
		w2 = (vx >>  8) & 0xFF;
		w3 = (vx >> 16) & 0xFF;
//		w4 = (vx >> 24) & 0xFF;
		w2 = w2 + w1;
		w3 = w3 + w2;
//		w4 = w4 + w3;
		vx = (p0Table[w1 & 0xff] ) | (p1Table[w2 & 0xff] ) | (p2Table[w3 & 0xff] );

		vy = vx + qY;
		w1 = (vy >>  0) & 0xFF;
		w2 = (vy >>  8) & 0xFF;
		w3 = (vy >> 16) & 0xFF;
//		w4 = (vy >> 24) & 0xFF;
		w2 = w2 + w1;
		w3 = w3 + w2;
//		w4 = w4 + w3;
		vy = (p4Table[w1 & 0xff] ) | (p5Table[w2 & 0xff] ) | (p6Table[w3 & 0xff] );

		vz = vy + qZ;
		w1 = (vz >>  0) & 0xFF;
		w2 = (vz >>  8) & 0xFF;
		w3 = (vz >> 16) & 0xFF;
//		w4 = (vz >> 24) & 0xFF;
		w2 = w2 + w1;
		w3 = w3 + w2;
//		w4 = w4 + w3;
		vz = (p8Table[w1 & 0xff] ) | (p9Table[w2 & 0xff] ) | (p10Table[w3 & 0xff] );

		// Return a value from -1 to 1
		gradPt = myGradientMesh.getGrad3D(vz);
		value = (gradPt.x * (x - qX)) + (gradPt.y * (y - qY)) + (gradPt.z * (z - qZ));
		return value;
	}


	/**
	* interpolate - Returns a value between v1 and v2 wrt to frac.
	*/
	protected double interpolate(double v1, double v2, double frac)
	{
		return v1 + frac*(v2 - v1);
	}

}

