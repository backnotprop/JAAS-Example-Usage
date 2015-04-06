
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Registration 
 * 
 * Handles the registering of a user login account
 * @author work
 *
 */
public class Registration {
	
	/**
	 * The Register method that performs registration
	 * @return
	 * @throws IOException
	 */
	protected boolean register() throws IOException {
		
		// Use queries class for matching method
		Queries q = new Queries();
		
		// Value to return: determines if registration occurred
		boolean successfulReg = false; 
		
		// DBs used for reading/writing user details
		File users = new File("records.txt");	
		File login = new File("auth.txt");	
	
		// local variables
		String fullname = null;
		String eid = null;  // this will only match 4 digits
		String username = null;
		String password = null;
		
		
		/*---------------Collect User Input---------------*/
		
		Scanner sc = new Scanner(System.in);
		
		// local boolean used for testing 
		boolean valid = false;
		// Collect user name 
		while(!valid){
			System.out.println("Enter Employee Name:");
			fullname = sc.nextLine();
			if(fullname.equals(null))
				System.out.println("Name may not be empty");
			else
				valid = true;
		}
		valid = false;
		
		// Collect id
		while(!valid ){
			System.out.println("Enter Employee ID:");
			eid = sc.nextLine();
			// Pattern must match 4 numbers
			Pattern digitPattern = Pattern.compile("\\d\\d\\d\\d");
			if(!digitPattern.matcher(eid).matches() || eid.equals(null))
				System.out.println("ID must only be a 4-digit value");
			else
				valid = true;
		}
		valid = false;
		
		
		// match the name, id pattern in Records DB, then continue
		String creds = fullname +", "+ eid;
		System.out.println(creds);
		if(!q.matchContent(users,creds))
				System.out.println("No record exists for this employee");
		else{
			
			System.out.println("\nEmployee Found\n");
			
			// continue collecting data, then write to file
			
			// collect desired username
			while(!valid){
				System.out.println("Enter Disered Username:");
				username = sc.nextLine();
				if(!q.matchContent(login,username + ",") && username != null)
					valid = true;
				else
					System.out.println("\nUsername already exists\n");
			}
			valid = false;
			
			// collect desired password
			while(!valid){
				System.out.println("Enter Password:");
				password = sc.nextLine();
				if(password.equals(null))
					System.out.println("\nPassword must not be empty\n");
				else{
					valid = true;
					
				//encrypt password
				password = md5(password);
				
				}
			}

			
			/*---------------Write to File ---------------*/

	
			FileWriter fw = new FileWriter(login, true);
			BufferedWriter bw = new BufferedWriter(fw);
			FileReader fr = new FileReader(login);
			BufferedReader br = new BufferedReader(fr);
			  
			// variables used in overwrite
			boolean exists = false;
			boolean doWrite = false;
			String input = "";
			
			// variable to add
			String writeit = fullname + ", " + eid + ", " + username + ", " + password;

			try {
				if(!br.equals(null)){
				
				    String line; 
				    while((line = br.readLine()) != null){
				    			    	
				    	if(line.contains(creds)){
				    		exists = true;	
				        	System.out.println("\nThis user already has a login. Overwrite? \n1. yes\n2. no");
				        	String response = sc.nextLine();
				        	if(response.equals("1")){
				        		// add new line (ignores old)
				        		input += writeit + '\n';
				        		// this grants write to overwrite file
				        		doWrite = true;
				        		
				        	}
				        }
				    	else
				    		// add old stuff
				    		input += line + '\n';    	
				    }	   
				}
				
				if(!exists){
					bw.write(writeit);
					successfulReg = true;		
				}
				else{
					if(doWrite){
						
						// write the overwrite
						String tmpFile = "tmpfile.txt";
		        		BufferedWriter btw = new BufferedWriter(new FileWriter(tmpFile));
						// Once everything is complete, delete old file..
		    		    login.delete();
		    		    // And rename tmp file's name to old file name
		    		    File newFile = new File(tmpFile);
		    		    newFile.renameTo(login);
	
		        		successfulReg = true;
		        	
		        		btw.write(input);
			        	btw.close();
					}	
				}
			}
			catch (FileNotFoundException e) {
				System.out.println("User Database not found" + e);
				e.printStackTrace();
			}
			catch(IOException e){
				System.out.println("Writing to file error "+ e);
				System.out.println();
				successfulReg = false;
			} 
			bw.close();
    		fw.close();
    		
		} // input never processes due to employee not existing
		return successfulReg;	
	} // register

	/**
	 * Create md5 
	 * @param input
	 * @return
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
