
import java.io.IOException;
import java.util.Iterator;
import java.util.Set;
import java.security.Principal;
import java.util.Scanner;
import javax.security.auth.Subject;
import javax.security.auth.login.*;

/**
 * Driver Class. Runs the entire program
 * 
 * Program implement s JAAS Login Authentication
 * Users can query file databases as they would in a 
 * professional company enviornment. It runs like
 * an admin HR system
 * @author Michael Ramos, Christopher Huntington
 *
 */
public class Driver {
	
	// JAAS includes basic methods used to authenticate subjects
    static private LoginContext lc; 	

    /*
     * MAIN
     */
	public static void main(String[] args) {

		
		/* Create a call back handler. This call back handler will be populated with
                 * different callbacks by the various login modules. For example, 
                 * if a login module implements a username/password style login, it populates this object
                 * with NameCallback (to get username) and PasswordCallback (which gets password).
		 */
		CallBack cbe = new CallBack(); 
		
		/* Create a new login context. 
		 * @param Policy Name : We defined a policy in the file JAASPolicy.txt 
		 *                      and it is called "JAASExample"
		 * @param Call Back Handler
		 */
		try {
			lc = new LoginContext("JAASExample", cbe);
		}
		catch (LoginException e) {
			System.err.println("Login exception."); 
		}
		
		/*
		 * 
		 * Registration context
		 * 
		 */
		Registration rc = new Registration();
		
		/*
		 * UI and program starts
		 */
		System.out.println("\n*************************************************");
		System.out.println("Employee Login and Authentication System\n");
		System.out.println("Authors: Michael Ramos, Chris Hunington");
		System.out.println("\n*************************************************");
		
		// program run loop
		Scanner sc = new Scanner(System.in);
		boolean doRun = true;
		String choiceIn = null;
		while (doRun){

			// initial menu
			System.out.println("\nPlease select from the following:");
			System.out.println("1. Login");
			System.out.println("2. Register Your Id");
			System.out.println("3. Exit Program");
			choiceIn = sc.nextLine();
		
			// register the user
			if(choiceIn.equals("2") || choiceIn.equals("Register Your Id") || choiceIn.equals("register")){
				boolean create = false;
				try {
					create = rc.register();
				} catch (IOException e) {
					e.printStackTrace();
				}

				if(create)
					System.out.println("\nNew user registered!\r\n");
				else
					System.out.println("\nError creating user\r\n");
					
			}
			// login (Implements JAAS)
			else if (choiceIn.equals("1") || choiceIn.equals("Login") || choiceIn.equals("login")){
				try{
					
					// run login and authenticate
					lc.login();
			
					// Get all the principles associated with subject
					// for this we have 2: role and username
					Subject loggedUser = lc.getSubject();
					Set principals = lc.getSubject().getPrincipals();
					Iterator i = principals.iterator();
					
					// Implement logged-in actions
					while (i.hasNext()) {
						// Subject should have 2 principles
						String role = ((Principal)i.next()).getName();
						String username = ((Principal)i.next()).getName();
						
						System.out.println("\n***********************************");
						System.out.println("Logged-In As: "+username);
						System.out.println("Your Role: " + role + "\n");
						System.out.println("***********************************");
						
						// associates may query their own data
						// action class is userAction
						if (role.equals("Associate") || role.equals("Junior Associate")) {
							UserAction act = new UserAction();
							act.runAsUser(loggedUser);
						}
						
						// supervisors can query data or change salary and position of their own employees
						// CEOs will have more privilege actions that under-management will be denied
						// action class ran is TopAction
						if (role.equals("Manager") || role.equals("VP")  || role.equals("CEO")) {
							TopAction act = new TopAction();
							act.runAsTop(loggedUser);
						}
						
					}
					// logout the user and erase principles
					lc.logout();
					System.out.println("\nLOGGED OUT");
					System.out.println("*************************************************\n");
					
					
					
				} 
				catch (LoginException e) {
					System.out.println("Username/password incorrect! " + e);
				}
				catch (SecurityException e) {
					System.out.println(" " + e);
				}
			}
			else if(choiceIn.equals("3") || choiceIn.equals("exit") || choiceIn.equals("Exit")){
				// exit loop condition
				// program will proceed to logout
				doRun = false;
				sc.close();
			}
			else{
				System.out.println("Invalid input, please enter 1, 2, or 3");
			}
			
		} 
		
		// shutdown
		Runtime.getRuntime().exit(1);
		
    }
    
}
	
