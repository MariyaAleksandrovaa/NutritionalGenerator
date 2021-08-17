package app.service;

import java.io.IOException;


import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;
//import org.springframework.security.web.DefaultRedirectStrategy;
//import org.springframework.security.web.RedirectStrategy;
import org.springframework.stereotype.Component;

@Component
public class LoginSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler /*implements AuthenticationSuccessHandler */{

	
//	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();
		
		String redirectURL = request.getContextPath();
		
		Collection<? extends GrantedAuthority> authorities = userDetails.getAuthorities();
		authorities.forEach(auth -> System.out.print(auth.getAuthority()));

		if(userDetails.hasRole("ADMIN")) {
			response.sendRedirect(redirectURL + "/admin");
			
		}else if(userDetails.hasRole("EDITOR")) {
			response.sendRedirect(redirectURL + "/editor");
			
		}else if(userDetails.hasRole("USER")) {
			response.sendRedirect(redirectURL + "/user");
			
		}
	}

}
