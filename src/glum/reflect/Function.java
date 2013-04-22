package glum.reflect;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * Class to provide easy access to function and allow them to be manipulated as objects.
 * <P>
 * Source: http://stackoverflow.com/questions/1073358/function-pointers-in-java<BR>
 * Source: http://tutorials.jenkov.com/java-reflection/private-fields-and-methods.html<BR>
 * Source: http://java.sun.com/developer/technicalArticles/ALT/Reflection/
 */
public class Function
{
	private Object refObject;
	private Method refMethod;

	public Function(Method m)
	{
		refObject = null;
		refMethod = m;
	}

	public Function(Object aObject, Method aMethod)
	{
		refObject = aObject;
		refMethod = aMethod;
	}

	public Function(Class<?> aClass, String methodName, Class<?>... methodParameterTypes) throws NoSuchMethodException
	{
		refObject = null;

		// Locate the appropriate method
		refMethod = ReflectUtil.locateMatchingMethod(aClass, methodName, methodParameterTypes);
		if (refMethod == null)
			throw new NoSuchMethodException("Failed to locate named: " + methodName + " with parameters: "
					+ ReflectUtil.getStringForClassArray(methodParameterTypes));

		refMethod.setAccessible(true);
	}

	public Function(Object aObj, String methodName, Class<?>... methodParameterTypes) throws NoSuchMethodException
	{
		refObject = aObj;

		// Locate the appropriate method
		refMethod = ReflectUtil.locateMatchingMethod(aObj.getClass(), methodName, methodParameterTypes);
		if (refMethod == null)
			throw new NoSuchMethodException("Failed to locate named: " + methodName + " with parameters: "
					+ ReflectUtil.getStringForClassArray(methodParameterTypes));

		refMethod.setAccessible(true);
	}

	/**
	 * Method used to called the function associated with this object. The parameters are passed in via argsArr
	 */
	public Object invoke(Object... argsArr) throws IllegalAccessException, InvocationTargetException
	{
		return refMethod.invoke(refObject, argsArr);
	}

};
