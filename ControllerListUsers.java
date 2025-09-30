package guiListUsers;

import javafx.stage.Stage;

/**
 * Controller for the List Users page.
 */
public class ControllerListUsers {

	/**
	 * Return to Admin Home.
	 */
	protected static void performReturn(Stage ps) {
		guiAdminHome.ViewAdminHome.displayAdminHome(ps, guiListUsers.ViewListUsers.theUser);
	}
}

