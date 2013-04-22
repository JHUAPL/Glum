package glum.coord;

/** A class for representing any 3-Dimensional vector, which could be
* a position, a velocity, or a rotation.  No information about units
* is assumed or implied.
*/

public class Point3D
{
	public double	x;
	public double	y;
	public double	z;

	public Point3D () { }

	public Point3D (Point3D pt)
	{ if ( pt != null ) { x = pt.x; y = pt.y; z = pt.z; } }

	public	Point3D (double x, double y, double z)
	{ this.x = x; this.y = y; this.z = z; }

	public void set (double x, double y, double z)
	{ this.x = x; this.y = y; this.z = z; }

	public void set (Point3D pt)
	{ if ( pt != null ) { x = pt.x; y = pt.y; z = pt.z; } }

	@Override
	public boolean equals (Object obj)
	{
		return (obj instanceof Point3D)  &&
			Epsilon.close (x, ((Point3D) obj).x)  &&
			Epsilon.close (y, ((Point3D) obj).y)  &&
			Epsilon.close (z, ((Point3D) obj).z);
	}

	@Override
	public String toString()
	{
		return new String("(" + x + ", " + y + ", " + z + ")");
	}
}
