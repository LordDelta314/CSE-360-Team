package guiOneTimePassword;

import database.Database;
import guiAdminHome.ViewAdminHome;

/*******
 * <p> Title: GUIAdminHomePage Class. </p>
 * 
 * <p> Description: The Java/FX-based Admin Home Page.  This class provides the controller actions
 * basic on the user's use of the JavaFX GUI widgets defined by the View class.
 * 
 * This page contains a number of buttons that have not yet been implemented.  WHen those buttons
 * are pressed, an alert pops up to tell the user that the function associated with the button has
 * not been implemented. Also, be aware that What has been implemented may not work the way the
 * final product requires and there maybe defects in this code.
 * 
 * The class has been written assuming that the View or the Model are the only class methods that
 * can invoke these methods.  This is why each has been declared at "protected".  Do not change any
 * of these methods to public.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-17 Initial version
 *  
 */

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
	 * Title: performInvitation () Method. </p>
	 * 
	 * <p> Description: Protected method to send an email inviting a potential user to establish
	 * an account and a specific role. </p>
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