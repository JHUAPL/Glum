package glum.net;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.ConnectException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.UnknownHostException;

import javax.xml.bind.DatatypeConverter;

public class NetUtil
{
	/**
	 * Utility method for retrieving the response code
	 */
	public static int getResponseCode(HttpURLConnection aConnection)
	{
		try
		{
			return aConnection.getResponseCode();
		}
		catch (IOException aExp)
		{
			return -1;
		}
	}
	
	/**
	 * Utility method to return the input stream associated with a URL
	 */
	public static InputStream getInputStream(URLConnection aConnection, Credential aCredential) throws IOException
	{
		InputStream inStream;
		String authStr;

		// Properly setup the credentials
		if (aCredential != null)
		{
			authStr = aCredential.getUsername() + ":" + aCredential.getPasswordAsString();
			authStr = DatatypeConverter.printBase64Binary(authStr.getBytes());
			aConnection.setRequestProperty("Authorization", "Basic " + authStr);
		}
		
		// Retrieve the InputStream
		aConnection.connect();
		inStream = aConnection.getInputStream();
		
		return inStream;
	}

	/**
	 * Utility method to return the input stream associated with a URL
	 */
	public static InputStream getInputStream(URL aUrl, Credential aCredential) throws IOException
	{
		return getInputStream(aUrl.openConnection(), aCredential);
	}

	/**
	 * Utility method for retrieving a more detailed result associated with an exception that occured on a URLConnection
	 */
	public static Result getResult(Exception aExp, URLConnection aConnection)
	{
		Throwable aCause;

		// See if there was a problem with the HTTP Connection
		if (aConnection instanceof HttpURLConnection)
		{
			int responseCode;

			responseCode = getResponseCode((HttpURLConnection)aConnection);
			switch (responseCode)
			{
				case HttpURLConnection.HTTP_UNAUTHORIZED:
				return Result.BadCredentials;

				case HttpURLConnection.HTTP_UNSUPPORTED_TYPE:
				return Result.UnsupportedConnection;

				case HttpURLConnection.HTTP_NOT_FOUND:
				case HttpURLConnection.HTTP_NO_CONTENT:
				return Result.InvalidResource;

//				case HttpURLConnection.HTTP_UNAVAILABLE:
//				return Result.UnreachableHost;

				default:
				break;
			}
		}

		// Evaluate the Exception
		aCause = aExp;
		while (aCause != null)
		{
			if (aCause instanceof UnknownHostException)
				return Result.UnreachableHost;
			else if (aCause instanceof ConnectException)
				return Result.ConnectFailure;

			aCause = aCause.getCause();
		}

		return Result.Undefined;

	}

	/**
	 * Checks to see whether the supplied aCredential is valid for the specified root URI.
	 * 
	 * @return The result of testing the credentials.
	 */
	public static Result checkCredentials(String uriRoot, Credential aCredential)
	{
		URLConnection aConnection;
		InputStream inStream;
		String username, password;
		String authStr;
		URL srcURL;
//		int fullLen;

		aConnection = null;
		inStream = null;
		try
		{
			srcURL = new URL(uriRoot);

			username = aCredential.getUsername();
			password = aCredential.getPasswordAsString();
			authStr = username + ":" + password;
			authStr = DatatypeConverter.printBase64Binary(authStr.getBytes());
//			authStr = new sun.misc.BASE64Encoder().encode((username + ":" + password).getBytes());
			aConnection = srcURL.openConnection();
			aConnection.setRequestProperty("Authorization", "Basic " + authStr);
			aConnection.connect();

			// Try to open the connection
//			fullLen = aConnection.getContentLength();
			inStream = new BufferedInputStream(aConnection.getInputStream());
			inStream.close();
			return Result.Success;
		}
		catch (Exception aExp)
		{
//			aExp.printStackTrace();
			return getResult(aExp, aConnection);
		}
	}

}
