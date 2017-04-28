package glum.zio;

import java.io.IOException;

/**
 * Interface to allow for serialization of (mutable) objects.
 */
public interface ZioObj
{
	/**
	 * Deserialization method to read data from the ZinStream.
	 */
	public void zioRead(ZinStream aStream) throws IOException;

	/**
	 * Serialization method to write data to the ZoutStream.
	 */
	public void zioWrite(ZoutStream aStream) throws IOException;

}
