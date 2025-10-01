package guiTools;


/*******
 * <p> Title: ValidationTest Class. </p>
 * 
 * <p> Description: A Java demonstration for semi-automated tests </p>
 * 
 * <p> Copyright: Lynn Robert Carter © 2022 </p>
 * 
 * @author Team 5
 * 
 */
public class ValidationTest {
	
	static int numPassed = 0;	// Counter of the number of passed tests
	static int numFailed = 0;	// Counter of the number of failed tests

	/*
	 * This mainline displays a header to the console, performs a sequence of
	 * test cases, and then displays a footer with a summary of the results
	 */
	public static void main(String[] args) {
		/************** Test cases semi-automation report header **************/
		System.out.println("______________________________________");
		System.out.println("\nTesting Automation");

		/************** Start of the test cases **************/
		
		performUsernameTestCase(1, "1Flask", false);
		
		performUsernameTestCase(2, "Phil", true);
		
		performUsernameTestCase(3, "Crash12.", false);
		
		performUsernameTestCase(4, "pilot.Jimmy", true);
		
		performUsernameTestCase(5, "", false);
		
		/************** End of the test cases **************/
		
		/************** Test cases semi-automation report footer **************/
		System.out.println("____________________________________________________________________________");
		System.out.println();
		System.out.println("Number of tests passed: "+ numPassed);
		System.out.println("Number of tests failed: "+ numFailed);
	}
	
	/*
	 * This method sets up the input value for the test from the input parameters,
	 * displays test execution information, invokes precisely the same recognizer
	 * that the interactive JavaFX mainline uses, interprets the returned value,
	 * and displays the interpreted result.
	 */
	private static void performUsernameTestCase(int testCase, String inputText, boolean expectedPass) {
				
		/************** Display an individual test case header **************/
		System.out.println("____________________________________________________________________________\n\nTest case: " + testCase);
		System.out.println("Input: \"" + inputText + "\"");
		System.out.println("______________");
		System.out.println("\nFinite state machine execution trace:");
		
		/************** Call the recognizer to process the input **************/
		String resultText= usernameValidator(inputText);
		
		/************** Interpret the result and display that interpreted information **************/
		System.out.println();
		
		// If the resulting text is empty, the recognizer accepted the input
		if (resultText != "") {
			 // If the test case expected the test to pass then this is a failure
			if (expectedPass) {
				System.out.println("***Failure*** The username <" + inputText + "> is invalid." + 
						"\nBut it was supposed to be valid, so this is a failure!\n");
				System.out.println("Error message: " + resultText);
				numFailed++;
			}
			// If the test case expected the test to fail then this is a success
			else {			
				System.out.println("***Success*** The username <" + inputText + "> is invalid." + 
						"\nBut it was supposed to be invalid, so this is a pass!\n");
				System.out.println("Error message: " + resultText);
				numPassed++;
			}
		}
		
		// If the resulting text is empty, the recognizer accepted the input
		else {	
			// If the test case expected the test to pass then this is a success
			if (expectedPass) {	
				System.out.println("***Success*** The username <" + inputText + 
						"> is valid, so this is a pass!");
				numPassed++;
			}
			// If the test case expected the test to fail then this is a failure
			else {
				System.out.println("***Failure*** The username <" + inputText + 
						"> was judged as valid" + 
						"\nBut it was supposed to be invalid, so this is a failure!");
				numFailed++;
			}
		}
	}
	
	protected static String usernameValidator(String username) {
		// check if the size of the string is 0. Return if so
		if (username.length() < 1) {
			return "Error: String of length 0";
		}
		
		// local variables for username FSM simulation
				int state = 0;
				String inputline = username;
				int currentCharIndex = 0;
				char currentChar = username.charAt(0);
				boolean running = true;
				int nextState = -1;
				int usernameSize = 0;
				boolean finalState = false;
				boolean validUser = false;
				
				// FSM continues until EOI is reached or if there is a forbidden character that doesn't lead to any state
				while (running) {
					// The switch statement takes the execution to the code for the current state, where
					// that code sees whether or not the current character is valid to transition to a
					// next state
					switch (state) {
					case 0: 
						
						// State 0 has 1 valid transition that is addressed by an if statement.
						
						// The current character is checked against A-Z, a-z, 0-9. If any are matched
						// the FSM goes to state 1
						
						// A-Z, a-z, 0-9 -> State 1
						if ((currentChar >= 'A' && currentChar <= 'Z' ) ||		// Check for A-Z
								(currentChar >= 'a' && currentChar <= 'z' ) ||	// Check for a-z
								(currentChar >= '0' && currentChar <= '9' )) {	// Check for 0-9
							nextState = 1;
							
							// Count the character 
							usernameSize++;
							
							// This only occurs once, so there is no need to check for the size getting
							// too large.
						}
						// If it is none of those characters, the FSM halts
						else 
							running = false;
						
						// The execution of this state is finished
						break;
					
					case 1: 
						// State 1 has two valid transitions, 
						//	1: a A-Z, a-z, 0-9 that transitions back to state 1
						//  2: a period that transitions to state 2 

						
						// A-Z, a-z, 0-9 -> State 1
						if ((currentChar >= 'A' && currentChar <= 'Z' ) ||		// Check for A-Z
								(currentChar >= 'a' && currentChar <= 'z' ) ||	// Check for a-z
								(currentChar >= '0' && currentChar <= '9' )) {	// Check for 0-9
							nextState = 1;
							
							// Count the character
							usernameSize++;
						}
						// . -> State 2
						// include choices of hyphen and underscore as part of the special characters
						else if (currentChar == '.' || currentChar == '-' || currentChar == '_') {							// Check for /
							nextState = 2;
							
							// Count the .
							usernameSize++;
						}				
						// If it is none of those characters, the FSM halts
						else
							running = false;
						
						// The execution of this state is finished
						// If the size is larger than 16, the loop must stop
						if (usernameSize > 16)
							running = false;
						break;			
						
					case 2: 
						// State 2 deals with a character after a period in the name.
						// inclusive with now period, hyphen, and underscore
						
						// A-Z, a-z, 0-9 -> State 1
						if ((currentChar >= 'A' && currentChar <= 'Z' ) ||		// Check for A-Z
								(currentChar >= 'a' && currentChar <= 'z' ) ||	// Check for a-z
								(currentChar >= '0' && currentChar <= '9' )) {	// Check for 0-9
							nextState = 1;
							
							// Count the odd digit
							usernameSize++;
							
						}
						// If it is none of those characters, the FSM halts
						else 
							running = false;

						// The execution of this state is finished
						// If the size is larger than 16, the loop must stop
						if (usernameSize > 16)
							running = false;
						break;			
					}
					
					if (running) {
						// When the processing of a state has finished, the FSM proceeds to the next
						// character in the input and if there is one, it fetches that character and
						// updates the currentChar.  If there is no next character the currentChar is
						// set to a blank.
						currentCharIndex++;
						if (currentCharIndex < inputline.length())
							currentChar = inputline.charAt(currentCharIndex);
						else {
							currentChar = ' ';
							running = false;
						}

						// Move to the next state
						state = nextState;
						
						// Is the new state a final state?  If so, signal this fact.
						if (state == 1) finalState = true;

						// Ensure that one of the cases sets this to a valid value
						nextState = -1;
					}
					// Should the FSM get here, the loop starts again
			
				}
				
				// prints out the state we are in when the FSM is finished running
				System.out.println(state);
				
				switch (state) {
				case 0:
					// State 0 is not a final state, so we can return a very specific error message
					// if the first character is a forbidden character
					return "Error: Forbidden character at state 0";

				case 1:
					// State 1 is a final state.  Check to see if the UserName length is valid.  If so we
					// we must ensure the whole string has been consumed.

					if (usernameSize < 4) {
						// UserName is too small
						return "Error: Username too small";
					}
					else if (usernameSize > 16) {
						// UserName is too long
						return "Error: username too long";
					} 
					// Incorporate 3rd else condition for when the first character of the input is an integer
					else if(Character.isDigit(username.charAt(0))) {
						return "Error: Username starts with integer";
					}
					else if (currentCharIndex < username.length()) {
						// There are characters remaining in the input, so the input is not valid
						return "Error: Characters remaining in the input";
					}
					
					validUser = true;
					return "";
				
				case 2:
					// State 2 is not a final state, so we can return a very specific error message
					// display corresponding error for when the special character conditions are not met
					return "Error: Special character conditions not met";
					
				default:
					// This is for the case where we have a state that is outside of the valid range.
					// This should not happen
					return "Error: Default";
				}
	}
}
