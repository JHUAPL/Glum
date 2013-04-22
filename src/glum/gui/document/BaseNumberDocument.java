package glum.gui.document;

import javax.swing.JTextField;

public abstract class BaseNumberDocument extends BaseDocument
{
	// State vars
	protected double minVal, maxVal;
	protected boolean formalizeDoc;
	protected int numAvailCols;

	public BaseNumberDocument(JTextField aOwner, double aMinVal, double aMaxVal)
	{
		super(aOwner);
		
		setMinMaxValue(aMinVal, aMaxVal);
		formalizeDoc = false;
		numAvailCols = -1;
	}
	
	/**
	 * Updates the new range of valid numbers.
	 */
	public void setMinMaxValue(double aMinVal, double aMaxVal)
	{
		minVal = aMinVal;
		maxVal = aMaxVal;
		
		// Insanity check
		if (minVal >= maxVal)
			throw new RuntimeException("Illogical range. Range: [" + minVal + "," + maxVal + "]");
	}
	
	@Override
	public void formalizeInput()
	{
		String currStr;

		// Insanity check
		if (ownerTF == null)
			return;

		// Is formalization required
		if (formalizeDoc == false)
			return;

		// Disassociate ourselves from event handling
		ownerTF.removeActionListener(this);
		ownerTF.removeFocusListener(this);

		currStr = ownerTF.getText();
		ownerTF.setText(currStr);

		// Reassociate ourselves from event handling
		ownerTF.addActionListener(this);
		ownerTF.addFocusListener(this);
	}

	

}
