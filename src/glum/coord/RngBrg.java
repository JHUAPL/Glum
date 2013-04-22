package glum.coord;

public class RngBrg
{
	public double rng;
	public double brg;

	public RngBrg() { }

	public RngBrg(RngBrg pt)
	{
		if ( pt != null )
		{
			rng = pt.rng;
			brg = pt.brg;
		}
	}

	public RngBrg (double rng, double brg)
	{
		this.rng = rng;
		this.brg = brg;
	}

	public void set (double rng, double brg)
	{
		this.rng = rng;
		this.brg = brg;
	}

	public void set (RngBrg pt)
	{
		if ( pt != null )
		{
			rng = pt.rng;
			brg = pt.brg;
		}
	}

	@Override
	public boolean equals (Object obj)
	{
		return (obj instanceof RngBrg)  &&
			Epsilon.close (rng, ((RngBrg) obj).rng)  &&
			Epsilon.close (brg, ((RngBrg) obj).brg);
	}
}
