package guiOneTimePassword;

import java.util.ArrayList;
import java.util.List;

import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.Pane;
import javafx.scene.shape.Line;
import javafx.scene.text.Font;
import javafx.stage.Stage;
import database.Database;
import entityClasses.User;
import guiAddRemoveRoles.ControllerAddRemoveRoles;
import guiAdminHome.ControllerAdminHome;
import guiAdminHome.ViewAdminHome;
import guiOneTimePassword.ViewOneTimePassword;
import guiUserUpdate.ViewUserUpdate;

/*******
 * <p> Title: GUIAdminHomePage Class. </p>
 * 
 * <p> Description: The Java/FX-based Admin Home Page.  This class provides the JavaFX GUI widgets
 * that enable an admin to perform admin functions.  This page contains a number of buttons that
 * have not yet been implemented.  What has been implemented may not work the way the final product
 * requires and there maybe defects in this code.
 * 
 * The class has been written using a singleton design pattern and is the View portion of the 
 * Model, View, Controller pattern.  The pattern is designed that the all accesses to this page and
 * its functions starts by invoking the static method displayAdminHome.  No other method should 
 * attempt to instantiate this class as that is controlled by displayAdminHome.  It ensure that
 * only one instance of class is instantiated and that one is properly configured for each use.  
 * 
 * Please note that this implementation is not appropriate for concurrent systems with multiple
 * users. This Baeldung article provides insight into the issues: 
 *           https://www.baeldung.com/java-singleton</p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2025 </p>
 * 
 * @author Lynn Robert Carter
 * 
 * @version 1.00		2025-08-17 Initial version
 *  
 */

public class ViewOneTimePassword {
	
	/*-*******************************************************************************************

	Attributes
	
	*/
	
	// These are the application values required by the user interface
	
	private static double width = applicationMain.FoundationsMain.WINDOW_WIDTH;
	private static double height = applicationMain.FoundationsMain.WINDOW_HEIGHT;
	
	
	// These are the widget attributes for the GUI. There are * areas for this GUI.
	
	// GUI Area 1: To Be Determined
	protected static Label label_PageTitle = new Label();
	protected static Label label_InitPrompt = new Label();
	
	
	// GUI Area 2: This area is used to provide status of the system.  This basic foundational code
		// does not have much current status information to display.
		protected static Label label_NumberOfPasswords = 
				new Label("Number of Oustanding Invitations: x");
		protected static Label label_NumberOfUsers = new Label("Number of Users: x");
	
	
	// GUI Area 3: 
	protected static Label label_EmailInput = new Label("Write the email of the user needing"
			+ " a one-time password:");
	protected static Label label_InvitationEmailAddress = new Label("Email Address");
	protected static TextField text_InvitationEmailAddress = new TextField();
	protected static Button button_SendPassword = new Button("Send Password");
	protected static Alert alertEmailError = new Alert(AlertType.INFORMATION);
	protected static Alert alertEmailSent = new Alert(AlertType.INFORMATION);
	
	
	// GUI Area x: Return, Logout or Quit
	protected static Button button_Return = new Button("Return");
	protected static Button button_Logout = new Button("Logout");
	protected static Button button_Quit = new Button("Quit");
	
	
	// This is the end of the GUI widgets for this page.
	
	// These attributes are used to configure the page and populate it with this user's information
		private static ViewOneTimePassword theView;		// Used to determine if instantiation of the class
													// is needed

		// Reference for the in-memory database so this package has access
		private static Database theDatabase = applicationMain.FoundationsMain.database;
		
		protected static Stage theStage;			// The Stage that JavaFX has established for us
		private static Pane theRootPane;			// The Pane that holds all the GUI widgets 
		protected static User theUser;				// The current logged in User

		private static Scene theOneTimePasswordScene;		// The shared Scene each invocation populates
	
	
		/*-*******************************************************************************************

		Constructors
		
		*/

		/**********
		 * <p> Method: displayOneTimePassword(Stage ps, User user) </p>
		 * 
		 * <p> Description: This method is the single entry point from outside this package to cause
		 * the OneTimePassword page to be displayed.
		 * 
		 * It first sets up every shared attributes so we don't have to pass parameters.
		 * 
		 * It then checks to see if the page has been setup.  If not, it instantiates the class, 
		 * initializes all the static aspects of the GIUI widgets (e.g., location on the page, font,
		 * size, and any methods to be performed).
		 * 
		 * After the instantiation, the code then populates the elements that change based on the user
		 * and the system's current state.  It then sets the Scene onto the stage, and makes it visible
		 * to the user.
		 * 
		 * @param ps specifies the JavaFX Stage to be used for this GUI and it's methods
		 * 
		 * @param user specifies the User for this GUI and it's methods
		 * 
		 */
		public static void displayOneTimePassword(Stage ps, User user) {
			
			// Establish the references to the GUI and the current user
			theStage = ps;
			theUser = user;
			
			// If not yet established, populate the static aspects of the GUI
			if (theView == null) theView = new ViewOneTimePassword();		// Instantiate singleton if needed
			
			// Populate the dynamic aspects of the GUI with the data from the user and the current
			// state of the system.
			theDatabase.getUserAccountDetails(user.getUserName());		// Fetch this user's data
					
			// Set the title for the window, display the page, and wait for the Admin to do something
			theStage.setTitle("CSE 360 Foundation Code: One-Time Password Page");
			theStage.setScene(theOneTimePasswordScene);						// Set this page onto the stage
			theStage.show();											// Display it to the user
		}
		
		/**********
		 * <p> Method: GUIAdminHomePage() </p>
		 * 
		 * <p> Description: This method initializes all the elements of the graphical user interface.
		 * This method determines the location, size, font, color, and change and event handlers for
		 * each GUI object.
		 * 
		 * This is a singleton and is only performed once.  Subsequent uses fill in the changeable
		 * fields using the displayAdminHome method.</p>
		 * 
		 */
		private ViewOneTimePassword() {

			// Create the Pane for the list of widgets and the Scene for the window
			theRootPane = new Pane();
			theOneTimePasswordScene = new Scene(theRootPane, width, height);
		
			// Populate the window with the title and other common widgets and set their static state
			
			// GUI Area 1
			label_PageTitle.setText("One-Time Password Page");
			setupLabelUI(label_PageTitle, "Arial", 28, width, Pos.CENTER, 0, 5);

			label_InitPrompt.setText("Which user needs a temporary password? ");
			setupLabelUI(label_InitPrompt, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 55);
			
			// GUI Area 2
			setupLabelUI(label_NumberOfPasswords, "Arial", 20, 200, Pos.BASELINE_LEFT, 20, 105);
			label_NumberOfPasswords.setText("Number of outstanding unused passwords: " + 
					theDatabase.getNumberOfInvitations());
		
			setupLabelUI(label_NumberOfUsers, "Arial", 20, 200, Pos.BASELINE_LEFT, 20, 135);
			label_NumberOfUsers.setText("Number of users: " + 
					theDatabase.getNumberOfUsers());
			
			
			//GUI Area 3
			setupLabelUI(label_EmailInput, "Arial", 20, width, Pos.BASELINE_LEFT, 20, 175);
			
			setupLabelUI(label_InvitationEmailAddress, "Arial", 16, width, Pos.BASELINE_LEFT,
			20, 210);
		
			setupTextUI(text_InvitationEmailAddress, "Arial", 16, 360, Pos.BASELINE_LEFT,
			130, 205, true);
		
			alertEmailSent.setTitle("Password");
			alertEmailSent.setHeaderText("Password was sent");

			setupButtonUI(button_SendPassword, "Dialog", 16, 150, Pos.CENTER, 630, 205);
			button_SendPassword.setOnAction((event) -> {ControllerOneTimePassword.performPasswordSend(); });
			
			// GUI Area x
			setupButtonUI(button_Return, "Dialog", 18, 210, Pos.CENTER, 20, 540);
			button_Return.setOnAction((event) -> {ControllerOneTimePassword.performReturn(); });
			
			setupButtonUI(button_Logout, "Dialog", 18, 250, Pos.CENTER, 250, 540);
			button_Logout.setOnAction((event) -> {ControllerOneTimePassword.performLogout(); });
	    
			setupButtonUI(button_Quit, "Dialog", 18, 250, Pos.CENTER, 520, 540);
			button_Quit.setOnAction((event) -> {ControllerOneTimePassword.performQuit(); });
			

			// This is the end of the GUI initialization code
			
			// Place all of the widget items into the Root Pane's list of children
			theRootPane.getChildren().addAll(
					label_PageTitle, label_InitPrompt,
					label_EmailInput,
					label_NumberOfPasswords,
					label_NumberOfUsers,
					label_InvitationEmailAddress,
					text_InvitationEmailAddress,
					button_SendPassword,
					button_Return,
					button_Logout,
		    		button_Quit
			);
					
					
			// With theRootPane set up with the common widgets, it is up to displayAdminHome to show
			// that Pane to the user after the dynamic elements of the widgets have been updated.
		}
		
		
	/*-*******************************************************************************************

	Helper methods used to minimizes the number of lines of code needed above
	
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
	private void setupLabelUI(Label l, String ff, double f, double w, Pos p, double x, double y){
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
	private void setupButtonUI(Button b, String ff, double f, double w, Pos p, double x, double y){
		b.setFont(Font.font(ff, f));
		b.setMinWidth(w);
		b.setAlignment(p);
		b.setLayoutX(x);
		b.setLayoutY(y);		
	}

	
	/**********
	 * Private local method to initialize the standard fields for a text input field
	 * 
	 * @param b		The TextField object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the Button
	 * @param p		The alignment (e.g. left, centered, or right)
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 * @param e		Is this TextField user editable?
	 */
	private void setupTextUI(TextField t, String ff, double f, double w, Pos p, double x, double y, boolean e){
		t.setFont(Font.font(ff, f));
		t.setMinWidth(w);
		t.setMaxWidth(w);
		t.setAlignment(p);
		t.setLayoutX(x);
		t.setLayoutY(y);		
		t.setEditable(e);
	}	

	
	/**********
	 * Private local method to initialize the standard fields for a ComboBox
	 * 
	 * @param c		The ComboBox object to be initialized
	 * @param ff	The font to be used
	 * @param f		The size of the font to be used
	 * @param w		The width of the ComboBox
	 * @param x		The location from the left edge (x axis)
	 * @param y		The location from the top (y axis)
	 */
	private void setupComboBoxUI(ComboBox <String> c, String ff, double f, double w, double x, double y){
		c.setStyle("-fx-font: " + f + " " + ff + ";");
		c.setMinWidth(w);
		c.setLayoutX(x);
		c.setLayoutY(y);
	}
}
	
	
