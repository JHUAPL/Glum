package glum.coord;

public class UV extends Point2D
{
	public UV ()
	{
		x = 0;
		y = 0;
	}

	public UV (Point2D pt)
	{
		if (pt != null)
		{
			x = pt.x;
			y = pt.y;
		}
	}

	public UV (double x, double y)
	{
		this.x = x;
		this.y = y;
	}

}
