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
package glum.gui.panel.generic;

import java.awt.Component;
import java.util.Collection;
import java.util.regex.Pattern;

import com.google.common.collect.ImmutableSet;

/**
 * UI component that provides the user with a simple prompt to allow for simple textual input.
 * <p>
 * Support is provided for the following:
 * <ul>
 * <li>Checking for empty input. This is treated as an error.
 * <li>Checking for input that matches reserved input. This is treated as an error.
 * <li>Checking for input that matches utilized input. This is treated as a warning.
 * </ul>
 *
 * @author lopeznr1
 */
public class TextInputPanel extends BaseTextInputPanel
{
	// State vars
	private ImmutableSet<String> reservedS;
	private ImmutableSet<String> utilizedS;
	private Pattern matchPattern;

	private String issueBadInputMsg;
	private String issueNoInputMsg;
	private String issueReservedMsg;
	private String issueUtilizedMsg;

	/**
	 * Standard Constructor
	 */
	public TextInputPanel(Component aParent, String aTitle, String aNameLabel)
	{
		super(aParent);

		// Set in a more specific title and input label
		titleL.setText(aTitle);
		inputL.setText(aNameLabel);

		reservedS = ImmutableSet.of();
		utilizedS = ImmutableSet.of();
		matchPattern = null;

		issueNoInputMsg = "Specified input is invalid.";
		issueBadInputMsg = "Please enter a valid name.";
		issueReservedMsg = "Name is reserved. Please pick another.";
		issueUtilizedMsg = "Name is in use. Item will be overwritten";

		updateGui();
	}

	/**
	 * Simplified Constructor
	 */
	public TextInputPanel(Component aParent, String aTitle)
	{
		this(aParent, aTitle, "Name:");
	}

	/**
	 * Method that allows the user to customize the warning / error message associated with user input.
	 *
	 * @param aIssueBadInputMsg
	 *        Message used when the user has entered bad input.
	 * @param aIssueNoInputMsg
	 *        Message used when the user has not entered any input.
	 * @param aIssueReservedMsg
	 *        Message used when the user has entered input that is reserved.
	 * @param aIssueUtilizedMsg
	 *        Message used when the user has entered input that is utilized.
	 */
	public void setIssueMessages(String aIssueBadInputMsg, String aIssueNoInputMsg, String aIssueReservedMsg,
			String aIssueUtilizedMsg)
	{
		if (aIssueBadInputMsg != null)
			issueBadInputMsg = aIssueBadInputMsg;
		if (aIssueNoInputMsg != null)
			issueNoInputMsg = aIssueNoInputMsg;
		if (aIssueReservedMsg != null)
			issueReservedMsg = aIssueReservedMsg;
		if (aIssueUtilizedMsg != null)
			issueUtilizedMsg = aIssueUtilizedMsg;

		updateGui();
	}

	/**
	 * Sets in a regular expression for which to check for valid input.
	 * <p>
	 * User specified input must match this regular expression.
	 * <p>
	 * Passing in null will allow any user input to match.
	 */
	public void setMatchRegex(String aMatchRegex)
	{
		matchPattern = null;
		if (aMatchRegex != null)
			matchPattern = Pattern.compile(aMatchRegex);

		updateGui();
	}

	/**
	 * Sets in all of the names that have been reserved.
	 * <p>
	 * Input matching items in this list will not be allowed.
	 */
	public void setReservedNames(Collection<String> aReservedC)
	{
		reservedS = ImmutableSet.copyOf(aReservedC);
		updateGui();
	}

	/**
	 * Sets in all of the names that have been utilized.
	 * <p>
	 * Input matching items in this list will be allowed - but will result in a warning.
	 */
	public void setUtilizedNames(Collection<String> aUtilizedC)
	{
		utilizedS = ImmutableSet.copyOf(aUtilizedC);
		updateGui();
	}

	@Override
	protected void updateGui()
	{
		String inputStr = inputTF.getText();

		// Check for errors / warnings
		String failMsg = null;
		String warnMsg = null;
		if (inputStr.equals("") == true)
			failMsg = issueNoInputMsg;
		else if (matchPattern != null && matchPattern.matcher(inputStr).matches() == false)
			failMsg = issueBadInputMsg;
		else if (reservedS.contains(inputStr) == true)
			failMsg = issueReservedMsg;
		else if (utilizedS.contains(inputStr) == true)
			warnMsg = issueUtilizedMsg;

		String infoMsg = failMsg;
		if (infoMsg == null)
			infoMsg = warnMsg;
		infoL.setText(infoMsg);

		boolean isEnabled = failMsg == null;
		acceptB.setEnabled(isEnabled);
	}

}
