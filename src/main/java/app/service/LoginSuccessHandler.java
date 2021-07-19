package app.service;

import java.io.IOException;
import java.util.Collection;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler {

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws ServletException, IOException {

		System.out.println("1111111111111111");

		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		System.out.println("Username: " + userDetails.getUsername());

		Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
		authorities.forEach(auth -> System.out.println(auth.getAuthority()));
		super.onAuthenticationSuccess(request, response, authentication);
	}

}
