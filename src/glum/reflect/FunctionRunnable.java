package glum.reflect;

/**
 * Class that wraps the provided function in a Runnable
 */
public class FunctionRunnable implements Runnable
{
	protected Function refFunc;
	protected Object[] argArr;
	
	public FunctionRunnable(Object aObj, String methodName, Object... aArgArr)
	{
		Class<?>[] typeArr;
		
		argArr = aArgArr;
		
		typeArr = new Class[0];
		if (argArr.length > 0)
		{
			// Determine the types of the specified arguments
			typeArr = new Class[argArr.length];
			for (int c1 = 0; c1 < typeArr.length; c1++)
			{
				typeArr[c1] = null;
				if (argArr[c1] != null)
					typeArr[c1] = argArr[c1].getClass();
			}
		}
		
		try
		{
			if (aObj instanceof Class)
				refFunc = new Function((Class<?>)aObj, methodName, typeArr);
			else
				refFunc = new Function(aObj, methodName, typeArr);
		}
		catch (Exception aExp)
		{
			String aMsg;
			
			aMsg = "Failed to locate valid function. Method:" + methodName;
			aMsg += "\n   Class:" + aObj.getClass();
			throw new RuntimeException(aMsg, aExp);
		}
	}
	
	@Override
	public void run()
	{
		try
		{
			refFunc.invoke(argArr);
		}
		catch (Exception aExp)
		{
			Throwable aCause;
			
			// Retrieve the root cause
			aCause = aExp;
			while (aCause.getCause() != null)
				aCause = aCause.getCause();
			
			throw new RuntimeException("Failed to invoke method.", aCause);
		}
	}
	
}
