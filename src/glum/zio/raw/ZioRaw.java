package glum.zio.raw;

import glum.zio.ZinStream;
import glum.zio.ZoutStream;

import java.io.IOException;

/**
 * Interface to allow for simple serialization of binary objects.
 */
public interface ZioRaw
{
	/**
	 * Deserialization method to read data from the ZinStream
	 */
	public void zioReadRaw(ZinStream aStream) throws IOException;

	/**
	 * Serialization method to write data to the ZoutStream
	 */
	public void zioWriteRaw(ZoutStream aStream) throws IOException;

}
