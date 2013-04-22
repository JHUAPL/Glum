package glum.coord;

/**
 * Contains a collection of utility methods to perform linear algebra using the objects from this package.
 */
public class GeoUtil
{
	/**
	 * realSqr returns aNum*aNum
	 */
	public static double realSqr(double aNum)
	{
		return aNum * aNum;
	}

	/**
	 * computeDotProduct - Returns the dot product of vector1 and vector2
	 */
	public static double computeDotProduct(Point3D vector1, Point3D vector2)
	{
		return vector1.x * vector2.x + vector1.y * vector2.y + vector1.z * vector2.z;
	}

	/**
	 * computeDistance - Returns the distance between pt1 and pt2
	 */
	public static double computeDistance(Point3D pt1, Point3D pt2)
	{
		return Math.sqrt(realSqr(pt1.x - pt2.x) + realSqr(pt1.y - pt2.y) + realSqr(pt1.z - pt2.z));
	}

	/**
	 * computeDistanceSquare - Returns the squared distance between pt1 and pt2
	 */
	public static double computeDistanceSquare(Point3D pt1, Point3D pt2)
	{
		return realSqr(pt1.x - pt2.x) + realSqr(pt1.y - pt2.y) + realSqr(pt1.z - pt2.z);
	}

	/**
	 * computeLength - Returns the magnitude of aVector
	 */
	public static double computeLength(Point3D aVector)
	{
		return Math.sqrt(realSqr(aVector.x) + realSqr(aVector.y) + realSqr(aVector.z));
	}

	/**
	 * computeNormal - Returns the R.H.R normal defined by the 3 points
	 */
	public static void computeNormal(Point3D pt1, Point3D pt2, Point3D pt3, Point3D aNormal)
	{
		Point3D vector1, vector2;

		vector1 = new Point3D();
		vector2 = new Point3D();
		computeVector(pt1, pt3, vector1);
		computeVector(pt3, pt2, vector2);

		// ! Not sure why I have to negate all the values; Need to refer to linear alg.
//!		aNormal.x = vector1.y*vector2.z - vector1.z*vector2.y;
//!		aNormal.y = vector1.z*vector2.x - vector1.x*vector2.z;
//!		aNormal.z = vector1.x*vector2.y - vector1.y*vector2.x;
		aNormal.x = -(vector1.y * vector2.z - vector1.z * vector2.y);
		aNormal.y = -(vector1.z * vector2.x - vector1.x * vector2.z);
		aNormal.z = -(vector1.x * vector2.y - vector1.y * vector2.x);

		// Normalize the vector
		normalizeVector(aNormal);
	}

	/**
	 * computeVector - Returns the vector defined by the 2 points
	 */
	public static void computeVector(Point3D pt1, Point3D pt2, Point3D aVector)
	{
		aVector.x = pt2.x - pt1.x;
		aVector.y = pt2.y - pt1.y;
		aVector.z = pt2.z - pt1.z;
	}

	/**
	 * normalizeVector - Normalizes aVector so that its length is 1
	 */
	public static void normalizeVector(Point3D aVector)
	{
		double length;

		length = computeLength(aVector);

		// Normalize the vector
		aVector.x = aVector.x / length;
		aVector.y = aVector.y / length;
		aVector.z = aVector.z / length;
	}

}
