package glum.io;

import glum.registry.ConfigMap;

public class ConfigMapTP implements TokenProcessor
{
	// State vars
	ConfigMap refConfigMap;

	public ConfigMapTP(ConfigMap aConfigMap)
	{
		refConfigMap = aConfigMap;
	}

	@Override
	public void flush()
	{
		; // Nothing to do
	}

	@Override
	public boolean process(String[] tokens, int lineNum)
	{
		// Insanity check
		if (tokens == null || tokens.length <= 1)
			return false;

		if (tokens.length == 2)
		{
			refConfigMap.put(tokens[0], tokens[1]);
		}
		else
		{
			for (int c1 = 1; c1 < tokens.length; c1++)
				refConfigMap.addItem(tokens[0], tokens[c1]);
		}

		return true;
	}

}
