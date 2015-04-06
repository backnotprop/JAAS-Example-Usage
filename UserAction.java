
import java.security.Principal;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import javax.security.auth.Subject;

/**
 * UserAction class handles basic user actions
 * @author work
 *
 */
public class UserAction  {

	private Scanner sc = new Scanner(System.in);
	private Actions action = new Actions();
	protected Queries q = new Queries();

	/*
	 * runAsUser method invokes all user actions 
	 */
	protected void runAsUser(Subject loggeduser){
		// get user principles
		String role, username = "";
		Set principals = loggeduser.getPrincipals();
		Iterator i = principals.iterator();
		while (i.hasNext()) {
			role = ((Principal)i.next()).getName();
			username = ((Principal)i.next()).getName();
		}
			// program loop
			boolean doRun = true;
			while(doRun){
				
				// action menu
				System.out.println("\nUser Functions:");
				System.out.println("***********************");
				System.out.println("1. Query My Personal Data");
				System.out.println("2. Logout");
				String choice = sc.nextLine();

				// query personal data
				if(choice.equals("1")){
					
					String data = action.myData(username);
					System.out.println("\nMy Personal Data:");
					System.out.println(data);
					System.out.println();
				}
				// exit condition
				else if (choice.equals("2")){
					doRun = false;
				}
			
			}
		return;
	}
}
