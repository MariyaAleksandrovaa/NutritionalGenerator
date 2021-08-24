package app;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordEncoder {

	
	public static void main(String args []) {
		
		System.out.println(System.getProperty("java.version"));
		
		BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
		String rawPassword = "admin1";
		String encodedPassword = encoder.encode(rawPassword);
		
		System.out.println(encodedPassword);
	}
}
