package glum.zio.util;

import glum.zio.ZinStream;
import glum.zio.ZoutStream;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * Utility class that takes a ZinStream/ZoutStream and presents various InputStream/OutputStream views.
 */
public class ZioWrapUtil
{
	/**
	 * Utility method to return the ZinStream as a DataInputStream view
	 */
	public static DataInputStream asDataInputStream(ZinStream aStream)
	{
		return new DataInputStream(asInputStream(aStream));
	}

	/**
	 * Utility method to return the ZinStream as a DataInputStream view
	 */
	public static DataOutputStream asDataOutputStream(ZoutStream aStream)
	{
		return new DataOutputStream(asOutputStream(aStream));
	}

	/**
	 * Utility method to return the ZinStream as an InputStream view
	 */
	public static InputStream asInputStream(ZinStream aStream)
	{
		return new WrapZinStream(aStream);
	}

	/**
	 * Utility method to return the ZoutStream as an OutputStream view
	 */
	public static OutputStream asOutputStream(ZoutStream aStream)
	{
		return new WrapZoutStream(aStream);
	}
	
	/**
	 * Utility method to return InputStream the as a ZinStream view
	 */
	public static ZinStream asZinStream(InputStream aStream) throws IOException
	{
		return new WrapInputStream(aStream);
	}

	/**
	 * Utility method to return InputStream the as a ZinStream view
	 */
	public static ZoutStream asZoutStream(OutputStream aStream) throws IOException
	{
		return new WrapOutputStream(aStream);
	}

}
