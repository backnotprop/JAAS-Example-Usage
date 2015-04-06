
import java.security.Principal;

public class UserPrincipal implements Principal {
	String name;

	
	UserPrincipal(String name) {
		this.name = name;
	}
	

	public String getName() {
		return name;	
	}

}
