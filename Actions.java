
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

/**
 * Actions Class
 * Includes methods that authorized users can do
 * @author Michael Ramos && Chris Huntington
 *
 */
public class Actions {
	
	// private variables for this class
	private File file = new File("records.txt");
	private File login = new File("auth.txt");
	private Scanner sc = new Scanner(System.in);
	private Queries q = new Queries();

	
	/*
	 * getId prompts action user for id
	 * this method is used in other below methods
	 */
	private String getId(){
		// Returned value
		String eid = "none";
		// loop condition
		boolean valid = false;
		while(!valid ){
			// Scanner
			System.out.println("Enter Employee ID:");
			eid = sc.nextLine();
			// Pattern must match 4 numbers
			Pattern digitPattern = Pattern.compile("\\d\\d\\d\\d");
			if(!digitPattern.matcher(eid).matches() || eid.equals(null))
				System.out.println("ID must only be a 4-digit value");
			else
				valid = true;
		}
		return eid;
	}
	
	/*
	 * findData takes in a username of the user doing the action
	 * the username is then checked to make sure they have 
	 * permission.
	 */
	protected String findData(String username){
		// data to be returned
		String info = "none";
		try{
			
			// get id of user we want to query
			String eid = getId();
			// use queries
			String test = q.findId(login, username);
			String pos = q.defineRole(file, test);
			// try defineAll using eid
			// query row based off id
			String line = q.defineAll(file, eid);
			String[] data = line.split(", ");
			
			// Here is our positions test
			if(!data[3].contains("("+test+")") && !pos.equals("CEO"))
				System.out.println("\nEmployee not under your management!\n");
			else
				// checks out with proper permissions
				info = q.defineAll(file, eid);
		}
		catch (FileNotFoundException e) {
			System.out.println("User Database not found" + e);
			e.printStackTrace();
		}
		catch(IOException e){
			System.out.println("Reading file error "+ e);	
		}
		// return queryed data
		return info;
	}
	
	
	/*
	 * myData method takes in a user's username and queries that users 
	 * data.
	 */
	protected String myData(String username){
		
		// data to be returned
		String info = "none";
		String id = "";
		try {
			id = q.findId(login, username);
			info = q.defineAll(file, id);
		} catch (IOException e) {
			System.out.println("Reading file error "+ e);	
		}
		// return personal data
		return info;
	}
	
	/*
	 * changeData takes in username of action user
	 * if that user has proper action authority than method 
	 * completed.
	 */
	protected void changeData(String username){
		try {
			// get id
			String eid = getId();
			// use queries
			String test = q.findId(login, username);
			String pos = q.defineRole(file, test);
			
			// query row based off id
			String line = q.defineAll(file, eid);
			String[] data = line.split(", ");

			// match if id's match than user is trying to edit themselves
			if(eid.equals(test))
				System.out.println("\nNo user may change their own data!\n");
			// the edited user must have the action user labeled as a manager
			else if(!data[3].contains("("+test+")") && !pos.equals("CEO"))
				System.out.println("\nEmployee not under your management!\n");
			else{
				
		    	// access is grant to change data
				System.out.println("Specify New Position: ");
				String newPos = sc.nextLine();
				System.out.println("Specify New Salary: ");
				String newSal = sc.nextLine();
				
				FileReader fr = new FileReader(file);
				BufferedReader br = new BufferedReader(fr);
				  	
				// variable to add/overwrite
				String writeit = data[0] + ", " + data[1] + ", " + newPos + ", " + data[3] + ", " + newSal;
				String input = "";
			
				// read unit user is found
				if(!br.equals(null)){
					
					    String read; 				    
					    while((read = br.readLine()) != null){
					    	// found user condition
					    	if(read.contains(eid + ",")){
					        	System.out.println("\n Confirm New Data? \n 1. yes\n 2. no");
					        	String response = sc.nextLine();
					        	if(response.equals("1")){
					        		// add new line (ignores old)
					        		input += writeit + '\n';
					        	}
					        }
					    	else
					    		// add old stuff
					    		input += read + '\n';
					    }
				}

				// write the overwrite
				String tmpFile = "tmpfile.txt";
	    		BufferedWriter btw = new BufferedWriter(new FileWriter(tmpFile));
				// Once everything is complete, delete old file..
			    file.delete();
			    // And rename tmp file's name to old file name
			    File newFile = new File(tmpFile);
			    newFile.renameTo(file);
	
			    // ** Close Files **
	    		btw.write(input);
	        	btw.close();
	        	br.close();
	        	System.out.println("\nNew salary and position entered\n");
			}
		}
		catch (FileNotFoundException e) {
			System.out.println("User Database not found" + e);
			e.printStackTrace();
		}
		catch(IOException e){
			System.out.println("Writing to file error "+ e);	
		}
	}
	


}
