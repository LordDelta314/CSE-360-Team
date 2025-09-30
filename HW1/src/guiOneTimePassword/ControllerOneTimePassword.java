package guiOneTimePassword;

import database.Database;
import guiAdminHome.ViewAdminHome;

public class ControllerOneTimePassword {
	
	/*-*******************************************************************************************

	User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	*/

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;
	
	
	/**********
	 * <p> 
	 * 
	 * Title: performPasswordSend () Method. </p>
	 * 
	 * <p> Description: Protected method to send an password to a user to allow them to
	 * login to their account. </p>
	 */
	protected static void performPasswordSend () {
		// Verify that the email address is valid - If not alert the user and return
		String emailAddress = ViewOneTimePassword.text_InvitationEmailAddress.getText();
		if (invalidEmailAddress(emailAddress)) {
			return;
		} 
	
		// Inform the user that the password has been sent and display the password
		String Password = theDatabase.generatePassword(emailAddress);
		String msg = "Password: " + Password + 
				" was sent to: " + emailAddress;
		System.out.println(msg);
		ViewOneTimePassword.alertEmailSent.setContentText(msg);
		ViewOneTimePassword.alertEmailSent.showAndWait();
		theDatabase.updatePassword(emailAddress, Password);
		
	}
	
	
	/**********
	 * <p> 
	 * 
	 * Title: invalidEmailAddress () Method. </p>
	 * 
	 * <p> Description: Protected method that is intended to check an email address before it is
	 * used to reduce errors.  The code currently only checks to see that the email address is not
	 * empty.  In the future, a syntactic check must be performed and maybe there is a way to check
	 * if a properly email address is active.</p>
	 * 
	 * @param emailAddress	This String holds what is expected to be an email address
	 */
	protected static boolean invalidEmailAddress(String emailAddress) {
		if (emailAddress.length() == 0) {
			ViewOneTimePassword.alertEmailError.setContentText(
					"Correct the email address and try again.");
			ViewOneTimePassword.alertEmailError.showAndWait();
			return true;
		}
		return false;
	}
	
	
	
	
	/**********
	 * <p>
	 * 
	 * Title: performReturn () Method. </p>
	 * 
	 * <p> Description: Protected method that returns this user to AdminHome. </p>
	 */
	protected static void performReturn() {
		guiAdminHome.ViewAdminHome.displayAdminHome(ViewOneTimePassword.theStage, 
				ViewOneTimePassword.theUser);
	}
	
	
	/**********
	 * <p> 
	 * 
	 * Title: performLogout () Method. </p>
	 * 
	 * <p> Description: Protected method that logs this user out of the system and returns to the
	 * login page for future use.</p>
	 */
	protected static void performLogout() {
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewOneTimePassword.theStage);
	}
	
	/**********
	 * <p> 
	 * 
	 * Title: performQuit () Method. </p>
	 * 
	 * <p> Description: Protected method that gracefully terminates the execution of the program.
	 * </p>
	 */
	protected static void performQuit() {
		System.exit(0);
	}
	
	
	
	
	
	
	
	

}
