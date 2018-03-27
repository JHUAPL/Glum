package glum.reflect;

import java.io.File;
import java.lang.reflect.*;
import java.net.URL;
import java.net.URLDecoder;

import javax.lang.model.type.NullType;

import com.google.common.primitives.Primitives;

public class ReflectUtil
{
	/**
	 * Utility method to check if an object of type rightType can be assigned to leftType
	 * <P>
	 * If the leftType is a non primitive then rightType must be null or of a child class.
	 * <P>
	 * If the leftType is a primitive then this method will only return true if the rightType is a primitive and does not
	 * need to be cast so that loss of information will occur. I.E:<BR>
	 * checkAssignability(long, Int) -> true<BR>
	 * checkAssignability(int, Long) -> false
	 */
	static boolean checkAssignability(Class<?> leftType, Class<?> rightType)
	{
		// Evaluate non primitive assignments
		if (leftType.isPrimitive() == false)
		{
			if (rightType == null || rightType == NullType.class)
				return true;
			else
				return leftType.isAssignableFrom(rightType);
		}

		// Evaluate primitive assignments
		if (rightType == null)
			return false;

		leftType = Primitives.wrap(leftType);
		rightType = Primitives.wrap(rightType);

		// Bail if same types
		if (leftType == rightType)
			return true;

		if (leftType == Double.class)
		{
			if (rightType == Float.class)
				return true;
		}
		else if (leftType == Long.class)
		{
			if (rightType == Integer.class || rightType == Short.class || rightType == Character.TYPE || rightType == Byte.class)
				return true;
		}
		else if (leftType == Integer.class)
		{
			if (rightType == Short.class || rightType == Character.TYPE || rightType == Byte.class)
				return true;
		}
		else if (leftType == Short.class)
		{
			if (rightType == Character.TYPE || rightType == Byte.class)
				return true;
		}

		return false;
	}

	/**
	 * Returns the Constructor of aClass with arguments matching parmTypes. On failure it returns null rather than throw
	 * an exception.
	 */
	public static <G1> Constructor<G1> getConstructorSafe(Class<G1> aClass, Class<?>... parmTypes)
	{
		Constructor<G1> aConstructor;

		try
		{
			aConstructor = aClass.getConstructor(parmTypes);
		}
		catch(Exception aExp)
		{
			return null;
		}

		return aConstructor;
	}

	/**
	 * Returns a neatly formatted string corresponding to classArr
	 */
	public static String getStringForClassArray(Class<?>... classArr)
	{
		String retStr;

		retStr = "[";
		for (int c1 = 0; c1 < classArr.length; c1++)
		{
			if (classArr[c1] == null)
				retStr += "null";
			else
				retStr += classArr[c1].getName();

			if (c1 + 1 != classArr.length)
				retStr += ", ";
		}
		retStr += "]";
		return retStr;

	}

	/**
	 * Attempts to return an object of type G1 from the fully qualified path
	 */
	public static <G1> G1 loadObject(String aFullClassPath, Class<G1> retType)
	{
		Class<?> aClass;
		Object aObject;

		// Insanity check
		if (aFullClassPath == null)
			return null;

		aObject = null;
		try
		{
			aClass = Class.forName(aFullClassPath);
			if (retType.isAssignableFrom(aClass) == false)
				return null;

			aObject = aClass.getDeclaredConstructor().newInstance();
			return retType.cast(aObject);
		}
		catch(ClassNotFoundException aExp)
		{
			System.out.println("Failure: " + aFullClassPath + " not found.");
		}
		catch(Exception aExp)
		{
			// Unknown Exception
			aExp.printStackTrace();
		}

		return null;
	}

	/**
	 * Attempts to return an object of type G1 from the fully qualified path. The object will be constructed using the
	 * vars parmTypes and parmValues.
	 */
	public static <G1> G1 loadObject(String aFullClassPath, Class<G1> retType, Class<?>[] parmTypes, Object[] parmValues)
	{
		Class<?> aClass;
		Constructor<?> aConstructor;
		Object aObject;

		// Insanity check
		if (aFullClassPath == null)
			return null;

		aObject = null;
		try
		{
			aClass = Class.forName(aFullClassPath);
			if (retType.isAssignableFrom(aClass) == false)
				return null;

			// Try to obtain the preferred constructor
			aConstructor = aClass.getConstructor(parmTypes);
			if (aConstructor == null)
				return null;

			// Construct the object
			aObject = aConstructor.newInstance(parmValues);
			return retType.cast(aObject);
		}
		catch(ClassNotFoundException aExp)
		{
			System.out.println("Failure: " + aFullClassPath + " not found.");
		}
		catch(Exception aExp)
		{
			// Unknown Exception
			aExp.printStackTrace();
		}

		return null;
	}

	/**
	 * Utility method to determine where refClass is installed
	 */
	public static File getInstalledRootDir(Class<?> refClass)
	{
		String dataPath;
		File rootDir;
		URL aUrl;
		int aIndex;

		// Attempt to determine the default data directory
		aUrl = refClass.getResource(refClass.getSimpleName() + ".class");
		// System.out.println("URL:" + aUrl);
		try
		{
			dataPath = aUrl.toURI().toString();
			dataPath = URLDecoder.decode(dataPath, "UTF-8");
		}
		catch(Exception aExp)
		{
			dataPath = aUrl.getPath();
			try
			{
				dataPath = URLDecoder.decode(dataPath, "UTF-8");
			}
			catch(Exception aExp2)
			{
				;
			}
		}

		// Remove the "file:" protocol specification
		aIndex = dataPath.indexOf("file:");
		if (aIndex != -1)
			dataPath = dataPath.substring(aIndex + 5);
		else
			System.out.println("Warning: Protocol \"file:\" not found. Run from  http???");

		// Remove the jar file component from the path "!*.jar"
		aIndex = dataPath.lastIndexOf("!");
		if (aIndex != -1)
		{
			dataPath = dataPath.substring(0, aIndex);
			rootDir = new File(dataPath);
			rootDir = rootDir.getParentFile();
		}
		else
		{
			System.out.println("Warning: " + refClass.getSimpleName() + " class does not appear to be packaged. Assuming developers environment.");
			rootDir = new File(dataPath);
			rootDir = rootDir.getParentFile().getParentFile().getParentFile();
		}

		return rootDir;
	}

	/**
	 * Utility method that searches for a matching method from the specified class, refClass. If null is specified as any
	 * of the parameter types, then any non primitive object will be considered a match for that parameter.
	 */
	public static Method locateMatchingMethod(Class<?> refClass, String methodName, Class<?>... methodParamTypeArr)
	{
		Method[] methodArr;
		boolean isMatch;
		Class<?> aClass;

		// Search all of the available methods
		methodArr = refClass.getDeclaredMethods();
		for (Method aItem : methodArr)
		{
			// Ensure the name matches
			if (methodName.equals(aItem.getName()) == true)
			{
				Class<?>[] evalParamTypeArr;

				// Ensure the number of arguments matches
				evalParamTypeArr = aItem.getParameterTypes();
				if (evalParamTypeArr.length == methodParamTypeArr.length)
				{
					isMatch = true;
					for (int c1 = 0; c1 < evalParamTypeArr.length; c1++)
					{
						isMatch &= checkAssignability(evalParamTypeArr[c1], methodParamTypeArr[c1]);
					}

					// Found a matching method
					if (isMatch == true)
						return aItem;
				}
			}
		}

		// Try the super classes
		aClass = refClass.getSuperclass();
		if (aClass != null)
			return locateMatchingMethod(aClass, methodName, methodParamTypeArr);

		// No method found
		return null;
	}

}
