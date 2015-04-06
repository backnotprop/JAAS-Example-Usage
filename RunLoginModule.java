
import java.io.IOException;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.security.Principal;
import java.util.Map;


import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * RunLoginModule implements the JAAS LoginModule
 * Authenticates users using a file DB system and query
 * @author Michael Ramos, Christopher Huntington
 *
 */
public class RunLoginModule implements LoginModule {

	// Flag to keep track of successful login.
	private Boolean successfulLogin = false;

	// Variable that keeps track of the principal.
	private Principal RolePrincipal;
	private Principal UserPrincipal;
	
	/*
	 * Subject keeps track of who is currently logged in.
	 */
	private Subject subject;
	
	/*
	 * String username
	 * String password 
	 * Temporary storage for usernames and passwords (before authentication).
	 * After authentication we can clear these variables. 
	 */
	private String username, password, role, eid;
	
	private File file = new File("auth.txt");
	private File users = new File("records.txt");
	
	/*
	 * Other variables that are initialized by the login context. 
	 */
	
	CallbackHandler cbh;
	Map sharedState;
	Map options;
	
	/*
	 * This method is called by the login context automatically.
	 * @see javax.security.auth.spi.LoginModule#initialize(javax.security.auth.Subject, 
	 *         javax.security.auth.callback.CallbackHandler, java.util.Map, java.util.Map)
	 */
	public void initialize(Subject subject, 
			CallbackHandler cbh,
			Map sharedState,
			Map options) {
		
		this.subject = subject;
		this.cbh = cbh;
		this.sharedState = sharedState;
		this.options = options;
		
	}
	
	/*
	 * If a user tries to abort a login then the state is reset. 
	 * @see javax.security.auth.spi.LoginModule#abort()
	 */
	public boolean abort() throws LoginException {
		if (!successfulLogin) {
			
			username = null;
			password = null;
			return false; 
		} else {
			logout(); 
			return true; 
		}
		
	}

	/*
	 * If login is valid, then the commit method is called by the LoginContext object. Here
	 * the logged in user is associated with a "principle". Think of this as a token
	 * that can from now on be used for authorization. 
	 * @see javax.security.auth.spi.LoginModule#commit()
	 */
	public boolean commit() throws LoginException {
		
		if (successfulLogin) {
			Queries q = new Queries();
			// Do search to determine users role
			try {
				eid = q.findId(file, username);
				role = q.defineRole(users, eid);
				
			} catch (IOException e) {
				System.out.println("Role Define error");
			}
			// Principal object stores the logged user's role.
				RolePrincipal = new RolePrincipal(role);
				UserPrincipal = new UserPrincipal(username);
				// subject stores the current logged in user.
				subject.getPrincipals().add(RolePrincipal);
				subject.getPrincipals().add(UserPrincipal);
				return true; 
		}
		
		return false;
	}

	/*
	 * The actual login method that performs the authentication
	 * @see javax.security.auth.spi.LoginModule#login()
	 */
	public boolean login() throws LoginException {
		// We will use two call backs - one for username and the other
		// for password. 
		Callback exampleCallbacks[] = new Callback[2];
		exampleCallbacks[0] = new NameCallback("username: ");
		exampleCallbacks[1] = new PasswordCallback("password: ", false);
		// pass the callbacks to the handler. 
		try {
			cbh.handle(exampleCallbacks);
		} catch (IOException e) {
			 e.printStackTrace();
		} catch (UnsupportedCallbackException e) {
			e.printStackTrace();
		}

		// Now populate username/passwords etc. from the handler
		username = ((NameCallback) exampleCallbacks[0]).getName();
		password = new String (
					((PasswordCallback) exampleCallbacks[1]).getPassword());
		// hash it
		password = md5(password);
		
		// Now perform validation. This part, you can either read from a file or a 
		// database. You can also incorporate secure password  handling here. 
		// As an example, we are going to use hard-coded passwords. 
		System.out.println();
		// this is the string we try to match in login file
		String creds = username + ", " + password;
		String isGood = "NO";
	    FileReader fr;
	    BufferedReader br = null;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
		
			if(!br.equals(null)){
			
				    String line;
				    while((line = br.readLine()) != null){
				    	// match line with creds to get a logged in user
				        if(line.contains(creds)){
				            isGood = "YES";
				            successfulLogin = true;
				        }
				    }	   
			}
			else
				System.out.println("DB NOT FOUND");
			
		br.close();
		}
		catch (FileNotFoundException e) {
				System.out.println("User Database not found" + e);
				e.printStackTrace();
		}
	    catch (IOException e) {
			System.out.println("Error reading file" + e);
			e.printStackTrace();
	    }
		
			
		// if good return true
		if(isGood.equals("YES")){
			return true;
		} else
			return false;
	}
	
	/*
	 * 
	 * @see javax.security.auth.spi.LoginModule#logout()
	 */
	public boolean logout() throws LoginException {
		username = null;
		password = null;		
		subject.getPrincipals().remove(RolePrincipal);
		subject.getPrincipals().remove(UserPrincipal);
		return true;
	}
	
		/*
		 * md5 method takes a string and hashes it
		 * important to remember to make this method private
		 * 
		 */
	    private static String md5(String input) {
         
        String md5 = null;
         
        if(null == input) return null;
         
        try {
             
        //Create MessageDigest object for MD5
        MessageDigest digest = MessageDigest.getInstance("MD5");
         
        //Update input string in message digest
        digest.update(input.getBytes(), 0, input.length());
 
        //Converts message digest value in base 16 (hex) 
        md5 = new BigInteger(1, digest.digest()).toString(16);
 
        } catch (NoSuchAlgorithmException e) {
 
            e.printStackTrace();
        }
        return md5;
    }

}
