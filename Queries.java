

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * Queries class contains all the methods relevant]
 * to querying the file DBs
 * @author Michael Ramos, Christopher Huntington
 *
 */
public class Queries {
	
	/**
	 * defineAll
	 * @param file file to look in
	 * @param userid user to query
	 * @return request users record
	 * @throws IOException
	 */
	protected String defineAll(File file, String userid) throws IOException{
		
		String userData = "None";
		FileReader fr;
	    BufferedReader br = null;
		
	    // read through records to get role
	    try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			if(!br.equals(null)){
			
				    String line;  
				    while((line = br.readLine()) != null){
				        if(line.contains(userid + ",")){
				        	userData = line;
				        }
				    }	   
			}
			br.close();
			fr.close();	
		}
		catch (FileNotFoundException e) {
				System.out.println("User Database not found" + e);
				e.printStackTrace();
		}
		return userData;
	} 
	
	
	/**
	 * The defineRole method that looks up a role of logged in user
	 * @param file
	 * @param match
	 * @return
	 * @throws IOException
	 */
	public String defineRole(File file, String userid) throws IOException{
		
		String role = "none";
		FileReader fr;
	    BufferedReader br = null;
		
	    // read through records to get role
	    try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			if(!br.equals(null)){

				    String line;  
				    while((line = br.readLine()) != null){
				        if(line.contains(userid + ",")){
				        	String[] data = line.split(", ");
				        	role = data[2];
				        }
				    }	   
			}
			br.close();
			fr.close();	
		}
		catch (FileNotFoundException e) {
				System.out.println("User Database not found" + e);
				e.printStackTrace();
		}
		return role;	
	} // defineRole
	
	
	/**
	 * The findId method that finds id from a user name
	 * @param file
	 * @param match
	 * @return
	 * @throws IOException
	 */
	public String findId(File file, String username) throws IOException{
		String eid = "none";
		FileReader fr;
	    BufferedReader br = null;
	    // read through login files
	    try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			
			if(!br.equals(null)){
				    String line;  
				    while((line = br.readLine()) != null){
				        if(line.contains(username+ ",")){
				        	String[] data = line.split(", ");
				        	eid = data[1];
				        }
				    }	   
			}
			br.close();
			fr.close();
			
		}
		catch (FileNotFoundException e) {
				System.out.println("User Database not found" + e);
				e.printStackTrace();
		}
		
	return eid;	
	} // fidId
	
	
	/**
	 * The matchContent method that looks for string in a file
	 * @param file
	 * @param match
	 * @return
	 * @throws IOException
	 */
	public boolean matchContent(File file, String match) throws IOException{
		boolean matched = false;
		FileReader fr;
	    BufferedReader br = null;
		try {
			fr = new FileReader(file);
			br = new BufferedReader(fr);
			if(!br.equals(null)){
			
			    String line;  
			    while((line = br.readLine()) != null){
			        if(line.contains(match)){
			            matched = true; 
			        }
			    }	   
			}	
		}
		catch (FileNotFoundException e) {
				System.out.println("User Database not found" + e);
				e.printStackTrace();
		}
		br.close();
	return matched;	
	} // matchContent

	

}
