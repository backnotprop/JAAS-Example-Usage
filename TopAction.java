
import java.security.Principal;
import java.util.Iterator;
import java.util.Scanner;
import java.util.Set;

import javax.security.auth.Subject;

/**
 * TopAction class handles manager and exec actions
 * @author Michael Ramos, Christopher Huntington
 *
 */
public class TopAction  {
	Scanner sc = new Scanner(System.in);
	Actions action = new Actions();

	/**
	 * runAsTop is the action handler method in this class
	 * @param loggeduser
	 */
	protected void runAsTop(Subject loggeduser){
		// get principles (role and username)
		String role, username = "";
		Set principals = loggeduser.getPrincipals();
		Iterator i = principals.iterator();
		while (i.hasNext()) {
			role = ((Principal)i.next()).getName();
			username = ((Principal)i.next()).getName();
		}
		// menu/inner program loop
		boolean doRun = true;
		while(doRun){
			
			// action menu
			System.out.println("\nUser Functions:");
			System.out.println("***********************");
			System.out.println("1. Query My Personal Data");
			System.out.println("2. Query User Data");
			System.out.println("3. Change Salary and/or Position of employee");
			System.out.println("4. Logout");
			String choice = sc.nextLine();

			// personal data check
			if(choice.equals("1")){
				String data = action.myData(username);
				System.out.println("\nMy Personal Data:");
				System.out.println(data);
				System.out.println();
			}
			// employee data check
			else if(choice.equals("2")){
				String data = action.findData(username);
				System.out.println("\nEmployee Data:");
				System.out.println(data);
				System.out.println();
			}
			// change employee data
			else if (choice.equals("3")){
				action.changeData(username);
			}
			// exit condition
			else if(choice.equals("4"))
				doRun = false;
		}
	return;
	}
}

