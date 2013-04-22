package glum.coord;

public class Point2D
{
	public double x;
	public double y;

	public Point2D () { }

	public Point2D (Point2D pt)
	{ if ( pt != null ) { x = pt.x; y = pt.y; } }

	public Point2D (double x, double y)
	{ this.x = x; this.y = y; }

	public void set (double x, double y)
	{ this.x = x; this.y = y; }

	public void set (Point2D pt)
	{ if ( pt != null ) { x = pt.x; y = pt.y; } }

	public double distance (Point2D aPt)
	{
		if (aPt == null)
			return 0;

		return Math.sqrt((aPt.x - x)*(aPt.x - x) + (aPt.y - y)*(aPt.y - y));
	}

	@Override
	public boolean equals (Object obj)
	{
		return (obj instanceof Point2D)  &&
			Epsilon.close (x, ((Point2D) obj).x)  &&
			Epsilon.close (y, ((Point2D) obj).y);
	}
}
