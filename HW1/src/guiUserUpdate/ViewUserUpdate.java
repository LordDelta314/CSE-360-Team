package guiUserUpdate;

import java.util.Optional;

import database.Database;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import entityClasses.User;

/*******
 * <p> Title: ViewUserUpdate Class. </p>
 * 
 * <p> Description: The Java/FX-based User Update Page.  This page enables the user to update the
 * attributes about the user held by the system.  Currently, this page does not provide a mechanism
 * to change the Username and not all of the functions on this page are implemented.
 * 
 * Currently the following attributes can be updated:
 * 		- First Name
 * 		- Middle Name
 * 		- Last Name
 * 		- Preferred First Name
 * 		- Email Address
 * The page uses dialog boxes for updating these items.</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.01		2025-08-19 Initial version plus new internal documentation
 *  
 */

public class ViewUserUpdate {

	/*-********************************************************************************************

	Attributes

	 */

	// These are the application values required by the user interface
	
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;

	
	// These are the widget attributes for the GUI. There are 3 areas for this GUI.
	
	// Unlike may of the other pages, the GUI on this page is not organized into areas and the user
	// is not able to logout, return, or quit from this page
	
	// These widgets display the purpose of the page and guide the user.
	private static Label label_ApplicationTitle = new Label("Update a User's Account Details");
    private static Label label_Purpose = 
    		new Label(" Use this page to define or update your account information."); 
    
    // These are static output labels and do not change during execution
	private static Label label_Username = new Label("Username:");
	private static Label label_Password = new Label("Password:");
	private static Label label_FirstName = new Label("First Name:");
	private static Label label_MiddleName = new Label("Middle Name:");
	private static Label label_LastName = new Label("Last Name:");
	private static Label label_PreferredFirstName = new Label("Preferred First Name:");
	private static Label label_EmailAddress = new Label("Email Address:");
	
	// These are dynamic labels and they change based on the user and user interactions.
	private static Label label_CurrentUsername = new Label();
	private static Label label_CurrentPassword = new Label();
	private static Label label_CurrentFirstName = new Label();
	private static Label label_CurrentMiddleName = new Label();
	private static Label label_CurrentLastName = new Label();
	private static Label label_CurrentPreferredFirstName = new Label();
	private static Label label_CurrentEmailAddress = new Label();
	
	// These buttons enable the user to edit the various dynamic fields.  The username and the
	// passwords for a user are currently not editable.
	private static Button button_UpdateUsername = new Button("Update Username");
	private static Button button_UpdatePassword = new Button("Update Password");
	private static Button button_UpdateFirstName = new Button("Update First Name");
	private static Button button_UpdateMiddleName = new Button("Update Middle Name");
	private static Button button_UpdateLastName = new Button("Update Last Name");
	private static Button button_UpdatePreferredFirstName = new Button("Update Preferred First Name");
	private static Button button_UpdateEmailAddress = new Button("Update Email Address");

	// This button enables the user to finish working on this page and proceed to the user's home
	// page determined by the user's role at the time of log in.
	private static Button button_ProceedToUserHomePage = new Button("Proceed to the User Home Page");
	
	// This is the end of the GUI widgets for this page.
	
	// These are the set of pop-up dialog boxes that are used to enable the user to change the
	// the values of the various account detail items.
	private static TextInputDialog dialogUpdateFirstName;
	private static TextInputDialog dialogUpdateMiddleName;
	private static TextInputDialog dialogUpdateLastName;
	private static TextInputDialog dialogUpdatePreferredFirstName;
	private static TextInputDialog dialogUpdateEmailAddresss;
	
	// These attributes are used to configure the page and populate it with this user's information
	private static ViewUserUpdate theView;	// Used to determine if instantiation of the class
											// is needed

	// This enables access to the application's database
	private static Database theDatabase = applicationMain.FoundationsMain.database;

	private static Stage theStage;				// The Stage that JavaFX has established for us	
	private static Pane theRootPane;			// The Pane that holds all the GUI widgets
	private static User theUser;				// The current user of the application

	public static Scene theUserUpdateScene = null;	// The Scene each invocation populates

	private static Optional<String> result;		// The result from a pop-up dialog

	protected static Alert invalidPassAlert = new Alert(AlertType.INFORMATION);
	
	private static boolean passValid = true;  // Tracks if a password is valid, default set to true 
	private static String originalPass = "";  // Variable to keep track of the original password before it is changed, for validation reasons
	
	/*-********************************************************************************************

	Constructors
	
	 */


	/**********
	 * <p> Method: displayUserUpdate(Stage ps, User user, boolean isFirstAdmin, boolean updatePass) </p>
	 * 
	 * <p> Description: This method is the single entry point from outside this package to cause
	 * the UserUpdate page to be displayed.
	 * 
	 * It first sets up very shared attributes so we don't have to pass parameters.
	 * 
	 * It then checks to see if the page has been setup.  If not, it instantiates the class, 
	 * initializes all the static aspects of the GUI widgets (e.g., location on the page, font,
	 * size, and any methods to be performed).
	 * 
	 * After the instantiation, the code then populates the elements that change based on the user
	 * and the system's current state.  It then sets the Scene onto the stage, and makes it visible
	 * to the user.
	 * 
	 * @param ps specifies the JavaFX Stage to be used for this GUI and it's methods
	 * 
	 * @param user specifies the User whose roles will be updated
	 *
	 * @param isFirstAdmin specifies if the user is the first user of the program
	 *
	 */
	public static void displayUserUpdate(Stage ps, User user, boolean isFirstAdmin) {
		
		// Establish the references to the GUI and the current user
		theUser = user;
		theStage = ps;

		originalPass = theUser.getPassword(); // copy of the original password before changes
		
		// If not yet established, populate the static aspects of the GUI by creating the 
		// singleton instance of this class
		if (theView == null) theView = new ViewUserUpdate();
		
		// Set the widget values that change from use of page to another use of the page.
		String s = "";

		if(isFirstAdmin) {
			button_UpdatePassword.setDisable(true); // disable the first instance of the update pass button (on the first admin) since they just created their VALID pass
		}
		else {
			button_UpdatePassword.setDisable(false); // on every other instance set the update password button to be available for use 
		}
		
		// Set the dynamic aspects of the window based on the user logged in and the current state
		// of the various account elements.
		s = theUser.getUserName();
		System.out.println("*** Fetching account data for user: " + s);
    	if (s == null || s.length() < 1)label_CurrentUsername.setText("<none>");
    	else label_CurrentUsername.setText(s);
		
		s = theUser.getPassword();
    	if (s == null || s.length() < 1 || s.length() == 6) { // if the length of the password is equal to 6 (the one time) we will nullify the entry and set the password to invalid
    		label_CurrentPassword.setText("<none>");
    		passValid = false;
    	}
    	else {
    		label_CurrentPassword.setText(s); // otherwise the password is valid
    		passValid = true;
    	}
    	
		s = theUser.getFirstName();
    	if (s == null || s.length() < 1)label_CurrentFirstName.setText("<none>");
    	else label_CurrentFirstName.setText(s);
       
        s = theUser.getMiddleName();
    	if (s == null || s.length() < 1)label_CurrentMiddleName.setText("<none>");
    	else label_CurrentMiddleName.setText(s);
        
        s = theUser.getLastName();
    	if (s == null || s.length() < 1)label_CurrentLastName.setText("<none>");
    	else label_CurrentLastName.setText(s);
        
		s = theUser.getPreferredFirstName();
    	if (s == null || s.length() < 1)label_CurrentPreferredFirstName.setText("<none>");
    	else label_CurrentPreferredFirstName.setText(s);
        
		s = theUser.getEmailAddress();
    	if (s == null || s.length() < 1)label_CurrentEmailAddress.setText("<none>");
    	else label_CurrentEmailAddress.setText(s);

		// Set the title for the window, display the page, and wait for the Admin to do something
    	theStage.setTitle("CSE 360 Foundation Code: Update User Account Details");
        theStage.setScene(theUserUpdateScene);
		theStage.show();

		// On the click of the proceed button
		button_ProceedToUserHomePage.setOnAction((event) -> 
    	{
    		System.out.println("Button clicked!");
    		
    	if(isFirstAdmin) { // if we are the firstAdmin, redirect to login page
    		
    		ViewUserLogin.displayUserLogin(theStage);
    		return;
    	}
    		
    	if(passValid) {
    		if(originalPass.equals(theDatabase.getCurrentPassword())) { // If there are no changes in the password, then proceed to corresponding home page
    			ControllerUserUpdate.goToUserHomePage(theStage, theUser);
    			return;
    		}
    		ViewUserLogin.displayUserLogin(theStage); // moves user to the login page to login with new credentials
    	}
    	else {
    		ViewUserUpdate.invalidPassAlert.showAndWait(); // If an invalid password, then show an alert
    	}
    	
    	});
	}

	
	/**********
	 * <p> Method: ViewUserUpdate() </p>
	 * 
	 * <p> Description: This method initializes all the elements of the graphical user interface.
	 * This method determines the location, size, font, color, and change and event handlers for
	 * each GUI object.</p>
	 * 
	 * This is a singleton and is only performed once.  Subsequent uses fill in the changeable
	 * fields using the displayUserUpdate method.</p>
	 * 
	 */
	
	private ViewUserUpdate() {

		// Create the Pane for the list of widgets and the Scene for the window
		theRootPane = new Pane();
		theUserUpdateScene = new Scene(theRootPane, width, height);

		// Initialize the pop-up dialogs to an empty text filed.
		dialogUpdateFirstName = new TextInputDialog("");
		dialogUpdateMiddleName = new TextInputDialog("");
		dialogUpdateLastName = new TextInputDialog("");
		dialogUpdatePreferredFirstName = new TextInputDialog("");
		dialogUpdateEmailAddresss = new TextInputDialog("");

		dialogUpdatePassword = new TextInputDialog("");
		
		// Establish the label for each of the dialogs.
		dialogUpdatePassword.setTitle("Update Password");
		dialogUpdatePassword.setHeaderText("Pick a new valid password, if changed then you need to relogin.");
		
		dialogUpdateFirstName.setTitle("Update First Name");
		dialogUpdateFirstName.setHeaderText("Update your First Name");
		
		dialogUpdateMiddleName.setTitle("Update Middle Name");
		dialogUpdateMiddleName.setHeaderText("Update your Middle Name");
		
		dialogUpdateLastName.setTitle("Update Last Name");
		dialogUpdateLastName.setHeaderText("Update your Last Name");
		
		dialogUpdatePreferredFirstName.setTitle("Update Preferred First Name");
		dialogUpdatePreferredFirstName.setHeaderText("Update your Preferred First Name");
		
		dialogUpdateEmailAddresss.setTitle("Update Email Address");
		dialogUpdateEmailAddresss.setHeaderText("Update your Email Address");

		// Label theScene with the name of the startup screen, centered at the top of the pane
		setupLabelUI(label_ApplicationTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

        // Label to display the welcome message for the first theUser
        setupLabelUI(label_Purpose, "Arial", 20, width, Pos.CENTER, 0, 50);
        
        // Display the titles, values, and update buttons for the various admin account attributes.
        // If the attributes is null or empty, display "<none>".

		invalidPassAlert.setTitle("Please enter a valid password");
		invalidPassAlert.setHeaderText("You must fill in all necessary information for a password (upper, lower, special, number, min length of 8)");
		invalidPassAlert.setContentText("Correct this issue and try again please.");
		
        // USername
        setupLabelUI(label_Username, "Arial", 18, 190, Pos.BASELINE_RIGHT, 5, 100);
        setupLabelUI(label_CurrentUsername, "Arial", 18, 260, Pos.BASELINE_LEFT, 200, 100);
        setupButtonUI(button_UpdateUsername, "Dialog", 18, 275, Pos.CENTER, 500, 93);
       
        // password
        setupLabelUI(label_Password, "Arial", 18, 190, Pos.BASELINE_RIGHT, 5, 150);
        setupLabelUI(label_CurrentPassword, "Arial", 18, 260, Pos.BASELINE_LEFT, 200, 150);
        setupButtonUI(button_UpdatePassword, "Dialog", 18, 275, Pos.CENTER, 500, 143);
		button_UpdatePassword.setOnAction((event) -> { // Event handler for the update button 
        result = dialogUpdatePassword.showAndWait();
        passValid = true;
    	result.ifPresent(name -> {
    		// local variables for password FSM
    		System.out.println(originalPass);
    		if (name.equals("")) {
    			ViewUserUpdate.label_CurrentPassword.setText("<none>");
    			passValid = false;
    			ViewUserUpdate.invalidPassAlert.showAndWait();
    			return;
    		}
    		
    		String passwordRecognizerInput = name;
    		int currCharPassIndex = 0;
    		char currPassChar = name.charAt(0);
    		boolean foundUpper = false;
    		boolean foundLower = false;
    		boolean foundNumber = false;
    		boolean foundSpecialChar = false;
    		boolean foundLongEnough = false;
    		boolean runningPass = true;
    		boolean notInvalidChar = true;
    		
    		// FSM continues until EOI is reached or otherwise there is a forbidden character and the password is invalid
    		while(runningPass) {
    			if (currPassChar >= 'A' && currPassChar <= 'Z') {
    				System.out.println("Upper case letter found");
    				foundUpper = true;
    			} else if (currPassChar >= 'a' && currPassChar <= 'z') {
    				System.out.println("Lower case letter found");
    				foundLower = true;
    			} else if (currPassChar >= '0' && currPassChar <= '9') {
    				System.out.println("Digit found");
    				foundNumber = true;
    			} else if ("~`!@#$%^&*()_-+={}[]|\\:;\"'<>,.?/".indexOf(currPassChar) >= 0) {
    				System.out.println("Special character found");
    				foundSpecialChar = true;
    			} else {
    				notInvalidChar = false;
    			}
    			// Added another condition making it so that the length doesn't exceed 32 characters
    			if (currCharPassIndex > 31) {
    				System.out.println("Now more than 32 characters have been found. Too long of a password!");
    				foundLongEnough = false;
    			}
    			else if (currCharPassIndex >= 7) {
    				System.out.println("At least 8 characters found");
    				foundLongEnough = true;
    			}
    			
    			
    			// Go to the next character if there is one
    			currCharPassIndex++;
    			if (currCharPassIndex >= name.length())
    				runningPass = false;
    			else
    				currPassChar = name.charAt(currCharPassIndex);
    		}
    		
    		// if all password conditions are met, the password is valid, otherwise we give a corresponding error
    		if(foundUpper && foundLower && foundNumber && foundSpecialChar && foundLongEnough && notInvalidChar) {
    			passValid = true;
    		}
    		else {
    			ViewUserUpdate.label_CurrentPassword.setText("<none>");
    			ViewUserUpdate.invalidPassAlert.showAndWait();
    			passValid = false;
    			return;
    		}
    		
    		if(passValid) { // update the database with the updated password, set corresponding view changes
    			theDatabase.updatePassword(name, theUser.getUserName());
        		String newPass = theDatabase.getCurrentPassword();
        		System.out.println(newPass);
        		theUser.setPassword(newPass);
        		label_CurrentPassword.setText(newPass);
    		}
    	});
     	});
		
        // First Name
        setupLabelUI(label_FirstName, "Arial", 18, 190, Pos.BASELINE_RIGHT, 5, 200);
        setupLabelUI(label_CurrentFirstName, "Arial", 18, 260, Pos.BASELINE_LEFT, 200, 200);
        setupButtonUI(button_UpdateFirstName, "Dialog", 18, 275, Pos.CENTER, 500, 193);
        button_UpdateFirstName.setOnAction((event) -> {result = dialogUpdateFirstName.showAndWait();
        	result.ifPresent(name -> theDatabase.updateFirstName(theUser.getUserName(), result.get()));
        	theDatabase.getUserAccountDetails(theUser.getUserName());
         	String newName = theDatabase.getCurrentFirstName();
           	theUser.setFirstName(newName);
        	if (newName == null || newName.length() < 1)label_CurrentFirstName.setText("<none>");
        	else label_CurrentFirstName.setText(newName);
         	});
               
        // Middle Name
        setupLabelUI(label_MiddleName, "Arial", 18, 190, Pos.BASELINE_RIGHT, 5, 250);
        setupLabelUI(label_CurrentMiddleName, "Arial", 18, 260, Pos.BASELINE_LEFT, 200, 250);
        setupButtonUI(button_UpdateMiddleName, "Dialog", 18, 275, Pos.CENTER, 500, 243);
        button_UpdateMiddleName.setOnAction((event) -> {result = dialogUpdateMiddleName.showAndWait();
    		result.ifPresent(name -> theDatabase.updateMiddleName(theUser.getUserName(), result.get()));
    		theDatabase.getUserAccountDetails(theUser.getUserName());
    		String newName = theDatabase.getCurrentMiddleName();
           	theUser.setMiddleName(newName);
        	if (newName == null || newName.length() < 1)label_CurrentMiddleName.setText("<none>");
        	else label_CurrentMiddleName.setText(newName);
    		});
        
        // Last Name
        setupLabelUI(label_LastName, "Arial", 18, 190, Pos.BASELINE_RIGHT, 5, 300);
        setupLabelUI(label_CurrentLastName, "Arial", 18, 260, Pos.BASELINE_LEFT, 200, 300);
        setupButtonUI(button_UpdateLastName, "Dialog", 18, 275, Pos.CENTER, 500, 293);
        button_UpdateLastName.setOnAction((event) -> {result = dialogUpdateLastName.showAndWait();
    		result.ifPresent(name -> theDatabase.updateLastName(theUser.getUserName(), result.get()));
    		theDatabase.getUserAccountDetails(theUser.getUserName());
    		String newName = theDatabase.getCurrentLastName();
           	theUser.setLastName(newName);
      	if (newName == null || newName.length() < 1)label_CurrentLastName.setText("<none>");
        	else label_CurrentLastName.setText(newName);
    		});
        
        // Preferred First Name
        setupLabelUI(label_PreferredFirstName, "Arial", 18, 190, Pos.BASELINE_RIGHT, 
        		5, 350);
        setupLabelUI(label_CurrentPreferredFirstName, "Arial", 18, 260, Pos.BASELINE_LEFT, 
        		200, 350);
        setupButtonUI(button_UpdatePreferredFirstName, "Dialog", 18, 275, Pos.CENTER, 500, 343);
        button_UpdatePreferredFirstName.setOnAction((event) -> 
        	{result = dialogUpdatePreferredFirstName.showAndWait();
    		result.ifPresent(name -> 
    		theDatabase.updatePreferredFirstName(theUser.getUserName(), result.get()));
    		theDatabase.getUserAccountDetails(theUser.getUserName());
    		String newName = theDatabase.getCurrentPreferredFirstName();
           	theUser.setPreferredFirstName(newName);
         	if (newName == null || newName.length() < 1)label_CurrentPreferredFirstName.setText("<none>");
        	else label_CurrentPreferredFirstName.setText(newName);
     		});
        
        // Email Address
        setupLabelUI(label_EmailAddress, "Arial", 18, 190, Pos.BASELINE_RIGHT, 5, 400);
        setupLabelUI(label_CurrentEmailAddress, "Arial", 18, 260, Pos.BASELINE_LEFT, 200, 400);
        setupButtonUI(button_UpdateEmailAddress, "Dialog", 18, 275, Pos.CENTER, 500, 393);
        button_UpdateEmailAddress.setOnAction((event) -> {result = dialogUpdateEmailAddresss.showAndWait();
    		result.ifPresent(name -> theDatabase.updateEmailAddress(theUser.getUserName(), result.get()));
    		theDatabase.getUserAccountDetails(theUser.getUserName());
    		String newEmail = theDatabase.getCurrentEmailAddress();
           	theUser.setEmailAddress(newEmail);
        	if (newEmail == null || newEmail.length() < 1)label_CurrentEmailAddress.setText("<none>");
        	else label_CurrentEmailAddress.setText(newEmail);
 			});
        
        // Set up the button to proceed to this user's home page
        setupButtonUI(button_ProceedToUserHomePage, "Dialog", 18, 300, 
        		Pos.CENTER, width/2-150, 450);
    	
        // Populate the Pane's list of children widgets
        theRootPane.getChildren().addAll(
        		label_ApplicationTitle, label_Purpose, label_Username,
        		label_CurrentUsername, 
        		label_Password, label_CurrentPassword, 
        		button_UpdatePassword, 
        		label_FirstName, label_CurrentFirstName, button_UpdateFirstName,
        		label_MiddleName, label_CurrentMiddleName, button_UpdateMiddleName,
        		label_LastName, label_CurrentLastName, button_UpdateLastName,
        		label_PreferredFirstName, label_CurrentPreferredFirstName,
        		button_UpdatePreferredFirstName, button_UpdateEmailAddress,
        		label_EmailAddress, label_CurrentEmailAddress, 
        		button_ProceedToUserHomePage);
	}
	
	
	/*-********************************************************************************************

	Helper methods to reduce code length

	 */
	
	/**********
	 * Private local method to initialize the standard fields for a label
	 * 
	 * @param l		The Label object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the Button
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	private static void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y){
		l.setFont(Font.font(ff, f));
		l.setMinWidth(w);
		l.setAlignment(p);
		l.setLayoutX(x);
		l.setLayoutY(y);		
	}
	
	
	/**********
	 * Private local method to initialize the standard fields for a button
	 * 
	 * @param b		The Button object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the Button
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	private static void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y){
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);		
	}
}
