package glum.coord;

public class Point2Di
{
	public int	x;
	public int	y;

	public Point2Di () { }

	public Point2Di (Point2Di pt)
	{ if ( pt != null ) { x = pt.x; y = pt.y; } }

	public Point2Di (int x, int y)  { this.x = x; this.y = y; }

	public void set (int x, int y) { this.x = x; this.y = y; }

	public void set (Point2Di pt)
	{ if ( pt != null ) { x = pt.x; y = pt.y; } }

	@Override
	public boolean equals (Object obj)
	{
		return (obj instanceof Point2Di)  &&
			x == ((Point2Di) obj).x  &&
			y == ((Point2Di) obj).y;
	}
}
