// Copyright (C) 2024 The Johns Hopkins University Applied Physics Laboratory LLC
//
// Licensed under the Apache License, Version 2.0 (the "License");
// you may not use this file except in compliance with the License.
// You may obtain a copy of the License at
//
//     http://www.apache.org/licenses/LICENSE-2.0
//
// Unless required by applicable law or agreed to in writing, software
// distributed under the License is distributed on an "AS IS" BASIS,
// WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
// See the License for the specific language governing permissions and
// limitations under the License.
package glum.reflect;

import java.io.File;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.net.URLDecoder;

import javax.lang.model.type.NullType;

import com.google.common.primitives.Primitives;

/**
 * Collection of reflect related utility methods.
 *
 * @author lopeznr1
 */
public class ReflectUtil
{
	/**
	 * Utility method to check if an object of type rightType can be assigned to leftType
	 * <p>
	 * If the leftType is a non primitive then rightType must be null or of a child class.
	 * <p>
	 * If the leftType is a primitive then this method will only return true if the rightType is a primitive and does not
	 * need to be cast so that loss of information will occur. I.E:</br>
	 * checkAssignability(long, Int) -> true</br>
	 * checkAssignability(int, Long) -> false
	 */
	static boolean checkAssignability(Class<?> aLeftType, Class<?> aRightType)
	{
		// Evaluate non primitive assignments
		if (aLeftType.isPrimitive() == false)
		{
			if (aRightType == null || aRightType == NullType.class)
				return true;
			else
				return aLeftType.isAssignableFrom(aRightType);
		}

		// Evaluate primitive assignments
		if (aRightType == null)
			return false;

		aLeftType = Primitives.wrap(aLeftType);
		aRightType = Primitives.wrap(aRightType);

		// Bail if same types
		if (aLeftType == aRightType)
			return true;

		if (aLeftType == Double.class)
		{
			if (aRightType == Float.class)
				return true;
		}
		else if (aLeftType == Long.class)
		{
			if (aRightType == Integer.class || aRightType == Short.class || aRightType == Character.TYPE
					|| aRightType == Byte.class)
				return true;
		}
		else if (aLeftType == Integer.class)
		{
			if (aRightType == Short.class || aRightType == Character.TYPE || aRightType == Byte.class)
				return true;
		}
		else if (aLeftType == Short.class)
		{
			if (aRightType == Character.TYPE || aRightType == Byte.class)
				return true;
		}

		return false;
	}

	/**
	 * Returns the Constructor of aClass with arguments matching parmTypes. On failure it returns null rather than throw
	 * an exception.
	 */
	public static <G1> Constructor<G1> getConstructorSafe(Class<G1> aClass, Class<?>... aParmTypeArr)
	{
		try
		{
			return aClass.getConstructor(aParmTypeArr);
		}
		catch (Exception aExp)
		{
			return null;
		}
	}

	/**
	 * Returns a neatly formatted string corresponding to classArr
	 */
	public static String getStringForClassArray(Class<?>... aClassArr)
	{
		var retStr = "[";
		for (int c1 = 0; c1 < aClassArr.length; c1++)
		{
			if (aClassArr[c1] == null)
				retStr += "null";
			else
				retStr += aClassArr[c1].getName();

			if (c1 + 1 != aClassArr.length)
				retStr += ", ";
		}
		retStr += "]";
		return retStr;

	}

	/**
	 * Utility method to return the root directory of the specified class.
	 */
	public static File getInstalledRootDir(Class<?> aClass)
	{
		File rootDir;

		// Delegate the retrieval of the (schema) root directory
		var dataPath = getPartialRootDir(aClass);

		// Remove the jar file component from the path "!*.jar"
		var tmpIdx = dataPath.lastIndexOf("!");
		if (tmpIdx != -1)
		{
			dataPath = dataPath.substring(0, tmpIdx);
			rootDir = new File(dataPath);
			rootDir = rootDir.getParentFile();
		}
		else
		{
			System.out.println("Warning: " + aClass.getSimpleName()
					+ " class does not appear to be packaged. Assuming developers environment.");
			rootDir = new File(dataPath);
			rootDir = rootDir.getParentFile().getParentFile().getParentFile();
		}

		return rootDir;
	}

	/**
	 * Attempts to return an object of type G1 from the fully qualified path
	 */
	public static <G1> G1 loadObject(String aFullClassPath, Class<G1> aRetType)
	{
		// Insanity check
		if (aFullClassPath == null)
			return null;

		try
		{
			var tmpClass = Class.forName(aFullClassPath);
			if (aRetType.isAssignableFrom(tmpClass) == false)
				return null;

			var tmpObject = tmpClass.getDeclaredConstructor().newInstance();
			return aRetType.cast(tmpObject);
		}
		catch (ClassNotFoundException aExp)
		{
			System.out.println("Failure: " + aFullClassPath + " not found.");
		}
		catch (Exception aExp)
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
	public static <G1> G1 loadObject(String aFullClassPath, Class<G1> aRetType, Class<?>[] aParmTypeArr,
			Object[] aParmValueArr)
	{
		// Insanity check
		if (aFullClassPath == null)
			return null;

		try
		{
			var tmpClass = Class.forName(aFullClassPath);
			if (aRetType.isAssignableFrom(tmpClass) == false)
				return null;

			// Try to obtain the preferred constructor
			var tmpConstructor = tmpClass.getConstructor(aParmTypeArr);
			if (tmpConstructor == null)
				return null;

			// Construct the object
			var tmpObject = tmpConstructor.newInstance(aParmValueArr);
			return aRetType.cast(tmpObject);
		}
		catch (ClassNotFoundException aExp)
		{
			System.out.println("Failure: " + aFullClassPath + " not found.");
		}
		catch (Exception aExp)
		{
			// Unknown Exception
			aExp.printStackTrace();
		}

		return null;
	}

	/**
	 * Utility method that searches for a matching method from the specified class, refClass. If null is specified as any
	 * of the parameter types, then any non primitive object will be considered a match for that parameter.
	 */
	public static Method locateMatchingMethod(Class<?> aClass, String aMethodName, Class<?>... aMethodParamTypeArr)
	{
		// Search all of the available methods
		var methodArr = aClass.getDeclaredMethods();
		for (Method aItem : methodArr)
		{
			// Ensure the name matches
			if (aMethodName.equals(aItem.getName()) == true)
			{
				// Ensure the number of arguments matches
				var evalParamTypeArr = aItem.getParameterTypes();
				if (evalParamTypeArr.length == aMethodParamTypeArr.length)
				{
					var isMatch = true;
					for (int c1 = 0; c1 < evalParamTypeArr.length; c1++)
						isMatch &= checkAssignability(evalParamTypeArr[c1], aMethodParamTypeArr[c1]);

					// Found a matching method
					if (isMatch == true)
						return aItem;
				}
			}
		}

		// Try the super classes
		var tmpClass = aClass.getSuperclass();
		if (tmpClass != null)
			return locateMatchingMethod(tmpClass, aMethodName, aMethodParamTypeArr);

		// No method found
		return null;
	}

	/**
	 * Utility method to determine if the JVM is running from a developers environment.
	 * <p>
	 * Pass the class in of the calling code.
	 */
	public static boolean isDeveloperEnvironment(Class<?> aClass)
	{
		// Delegate the retrieval of the (schema) root directory
		var dataPath = getPartialRootDir(aClass);

		// If there is a jar ("!*.jar") component then we are packaged up
		var tmpIdx = dataPath.lastIndexOf("!");
		if (tmpIdx != -1)
			return false;

		return true;
	}

	/**
	 * Utility helper method that returns a string representing the the (schema) root directory of the specified class.
	 * <p>
	 * If the software is packaged into a jar this string will contain the reference to the jar component.
	 */
	private static String getPartialRootDir(Class<?> aClass)
	{
		String dataPath;

		// Attempt to determine the default data directory
		var tmpUrl = aClass.getResource(aClass.getSimpleName() + ".class");
		// System.out.println("URL:" + aUrl);
		try
		{
			dataPath = tmpUrl.toURI().toString();
			dataPath = URLDecoder.decode(dataPath, "UTF-8");
		}
		catch (Exception aExp)
		{
			dataPath = tmpUrl.getPath();
			try
			{
				dataPath = URLDecoder.decode(dataPath, "UTF-8");
			}
			catch (Exception aExp2)
			{
				;
			}
		}

		// Remove the "file:" protocol specification
		var tmpIndex = dataPath.indexOf("file:");
		if (tmpIndex != -1)
			dataPath = dataPath.substring(tmpIndex + 5);
		else
			System.out.println("Warning: Protocol \"file:\" not found. Run from http???");

		return dataPath;
	}

}
