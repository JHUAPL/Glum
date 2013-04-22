package glum.unit;

public interface UnitListener
{
	/**
	 * Event callback for notification of whenever a Unit changes in aManager
	 */
	public void unitChanged(UnitProvider aProvider, String aKey);

}
