package glum.io;

import java.util.*;

public class WarningTP implements TokenProcessor
{
	private Map<String,String> warningSet;

	/**
	* Constructor
	*/
	public WarningTP()
	{
		warningSet = new LinkedHashMap<String,String>();
	}

	/**
	* add - Adds a new warning to the set of warnings; Note if aMsg is null
	* then aInstr will just be ignored
	*/
	public void add(String aInstr, String aMsg)
	{
		// Insanity check
		if (aInstr == null)
			return;

		warningSet.put(aInstr, aMsg);
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
		if (tokens == null)
			return false;

		if (warningSet.containsKey(tokens[0]) == true)
		{
			String aMsg;

			// Display the aMsg if associated with aInstr
			aMsg = warningSet.get(tokens[0]);
			if (aMsg instanceof String)
				System.out.println("[" + lineNum + "] " + aMsg);

			return true;
		}

		return false;
	}

}