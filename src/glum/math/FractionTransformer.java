package glum.math;

public enum FractionTransformer
{
	Linear("Linear", "LINEAR")
	{
		@Override
		public double transform(double aFrac)
		{
			return aFrac;
		}
	},

	Cosine("Cosine", "COSINE")
	{
		@Override
		public double transform(double aFrac)
		{
			aFrac = aFrac * Math.PI;
			aFrac = (1 - Math.cos(aFrac)) * 0.5;

			return aFrac;
		}
	},

	Cubic("Cubic", "CUBIC")
	{
		@Override
		public double transform(double aFrac)
		{
			double t, t2;

			t = aFrac;
			t2 = aFrac * aFrac;
			aFrac = 3*t2 - 2*t2*t;

			return aFrac;
		}
	};


	// Vars
	private final String userStr;
	private final String encodeStr;


	/**
	* Constructor
	*/
	FractionTransformer(String aUserStr, String aEncodeStr)
	{
		userStr = aUserStr;
		encodeStr = aEncodeStr;
	}


	/**
	* getReferenceName
	*/
	public String getEncodeString()
	{
		return encodeStr;
	}

	/**
	* transform
	*/
	public abstract double transform(double aFrac);

	/**
	* parse - Returns the associated enum type
	*/
	public static FractionTransformer parse(String aStr)
	{
		if (aStr == null)
			return null;

		for (FractionTransformer aEnum : FractionTransformer.values())
		{
			if (aStr.equals(aEnum.getEncodeString()) == true)
				return aEnum;
		}

		return null;
	}

	@Override
	public String toString()
	{
		return userStr;
	}

}
