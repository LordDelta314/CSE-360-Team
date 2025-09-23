package guiOneTimePassword;

import database.Database;
import guiUserLogin.ViewUserLogin;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.control.ComboBox;

public class ControllerOneTImePassword {
	
	/*-********************************************************************************************

	User Interface Actions for this page
	
	This controller is not a class that gets instantiated.  Rather, it is a collection of protected
	static methods that can be called by the View (which is a singleton instantiated object) and 
	the Model is often just a stub, or will be a singleton instantiated object.
	
	 */

	// Reference for the in-memory database so this package has access
	private static Database theDatabase = applicationMain.FoundationsMain.database;		

	
	/**********
	 * <p> Method: doSelectUser() </p>
	 * 
	 * <p> Description: This method uses the ComboBox widget, fetches which item in the ComboBox
	 * was selected (a user in this case), and establishes that user and the current user, setting
	 * easily accessible values without needing to do a query. </p>
	 * 
	 */
	protected static void doSelectUser() {
		ViewOneTimePassword.theSelectedUser = 
				(String) ViewOneTimePassword.combobox_SelectUser.getValue();
		theDatabase.getUserAccountDetails(ViewOneTimePassword.theSelectedUser);
		setPassword();
	}
	
	
	/**********
	 * <p> Method: repaintTheWindow() </p>
	 * 
	 * <p> Description: This method determines the current state of the window and then establishes
	 * the appropriate list of widgets in the Pane to show the proper set of current values. </p>
	 * 
	 */
	protected static void repaintTheWindow() {
		// Clear what had been displayed
		ViewOneTimePassword.theRootPane.getChildren().clear();
		
		// Defermine which of the two views to show to the user
		if (ViewOneTimePassword.theSelectedUser.compareTo("<Select a User>") == 0) {
			// Only show the request to select a user to be updated and the ComboBox
			System.out.print("Select User");
			ViewOneTimePassword.theRootPane.getChildren().addAll(
					ViewOneTimePassword.label_PageTitle, ViewOneTimePassword.label_UserDetails, 
					ViewOneTimePassword.button_UpdateThisUser, ViewOneTimePassword.line_Separator1,
					ViewOneTimePassword.label_SelectUser, ViewOneTimePassword.combobox_SelectUser, 
					ViewOneTimePassword.line_Separator4, ViewOneTimePassword.button_Return,
					ViewOneTimePassword.button_Logout, ViewOneTimePassword.button_Quit);
		}
		else {
			// Show all the fields as there is a selected user (as opposed to the prompt)
			System.out.print("The other one");
			ViewOneTimePassword.theRootPane.getChildren().addAll(
					ViewOneTimePassword.label_PageTitle, ViewOneTimePassword.label_UserDetails,
					ViewOneTimePassword.button_UpdateThisUser, ViewOneTimePassword.line_Separator1,
					ViewOneTimePassword.label_SelectUser,
					ViewOneTimePassword.combobox_SelectUser, 
					ViewOneTimePassword.text_Password,
					ViewOneTimePassword.line_Separator4, 
					ViewOneTimePassword.button_Return,
					ViewOneTimePassword.button_Logout,
					ViewOneTimePassword.button_Quit);
		}
		
		// Add the list of widgets to the stage and show it
		
		// Set the title for the window
		ViewOneTimePassword.theStage.setTitle("CSE 360 Foundation Code: Admin Opertaions Page");
		ViewOneTimePassword.theStage.setScene(ViewOneTimePassword.theOneTimePasswordScene);
		ViewOneTimePassword.theStage.show();
	}
	
	protected static void setPassword() {
		System.out.println("*** Entering setPassword");
		
	}
	
	
	
	/**********
	 * <p> Method: performReturn() </p>
	 * 
	 * <p> Description: This method returns the user (who must be an Admin as only admins are the
	 * only users who have access to this page) to the Admin Home page. </p>
	 * 
	 */
	protected static void performReturn() {
		guiAdminHome.ViewAdminHome.displayAdminHome(ViewOneTimePassword.theStage,
				ViewOneTimePassword.theUser);
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
		guiUserLogin.ViewUserLogin.displayUserLogin(ViewOneTimePassword.theStage);
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