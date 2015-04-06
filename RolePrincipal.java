
import java.security.Principal;

/**
 * Principles class
 * 
 * @author Michael Ramos && Chris Hunington
 *
 */
public class RolePrincipal implements Principal {
	String name;

	
	RolePrincipal(String name) {
		this.name = name;
	}
	
	public String getName() {
		return name;	
	}

}
