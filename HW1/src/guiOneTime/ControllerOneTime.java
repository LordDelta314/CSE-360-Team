package guiOneTime;

import database.Database;
import guiAdminHome.ViewAdminHome;

public class ControllerOneTime {

	/*-*******************************************************************************************

	User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	 */
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	protected static void performInvitation () {
		// Verify that the email address is valid - If not alert the user and return
		String emailAddress = ViewOneTime.text_InvitationEmailAddress.getText();
		if (invalidEmailAddress(emailAddress)) {
			return;
		}
		
		// Check to ensure that we are not sending a second message with a new invitation code to
		// the same email address.  
		/*if (theDatabase.emailaddressHasBeenUsed(emailAddress)) {
			ViewOneTime.alertEmailError.setContentText(
					"An invitation has already been sent to this email address.");
			ViewOneTime.alertEmailError.showAndWait();
			return;
		}*/
		
		// Inform the user that the invitation has been sent and display the invitation code
		//String theSelectedRole = (String) ViewAdminHome.combobox_SelectRole.getValue();
		String oneTimePass = theDatabase.generateOneTimePass(emailAddress);
		String msg = "Code: " + oneTimePass + " was sent to: " + emailAddress;
		System.out.println(msg);
		ViewOneTime.alertEmailSent.setContentText(msg);
		ViewOneTime.alertEmailSent.showAndWait();
		
		System.out.println(oneTimePass);
		
		// Update the Admin Home pages status
		ViewOneTime.text_InvitationEmailAddress.setText("");
	}
	
	protected static boolean invalidEmailAddress(String emailAddress) {
		if (emailAddress.length() == 0) {
			ViewOneTime.alertEmailError.setContentText(
					"Correct the email address and try again.");
			ViewOneTime.alertEmailError.showAndWait();
			return true;
		}
		return false;
	}
	
 	/**********
	 * <p> Method: performLogout() </p>
	 * 
	 * <p> Description: This method logs out the current user and proceeds to the normal login
	 * page where existing users can log in or potential new users with a invitation code can
	 * start the process of setting up an account. </p>
	 * 
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewOneTime.theStage);
	}
	
	
	/**********
	 * <p> Method: performQuit() </p>
	 * 
	 * <p> Description: This method terminates the execution of the program.  It leaves the
	 * database in a state where the normal login page will be displayed when the application is
	 * restarted.</p>
	 * 
	 */	
	protected static void performQuit() {
		System.exit(0);
	}
}