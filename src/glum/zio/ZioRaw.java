package glum.zio;

import java.io.IOException;

/**
 * Interface to allow for serialization of (immutable) objects.
 * <P>
 * The method zioWrite() is provided to allow immutable objects to be serialized.
 * <P>
 * The method zioRead() is not provided since that would imply mutability. Any deserialization should be done via the constructor.
 */
public interface ZioRaw
{
	/**
	 * Serialization method to write data to the ZoutStream.
	 */
	public void zioWrite(ZoutStream aStream) throws IOException;

}
