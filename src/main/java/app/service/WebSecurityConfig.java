package app.service;



import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

//	@Autowired
//	private DataSource dataSource;

//	@Autowired
//	public WebSecurityConfig(LoginSuccessHandler loginSuccessHandler) {
//		successHandler = loginSuccessHandler;
//	}

	
//	public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/prueba").setViewName("prueba");
//    }
	
	
	@Bean
	public UserDetailsService userDetailsServiceFunt() {
		return new CustomUserDetailsService();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

//	@Bean
//	public DaoAuthenticationProvider authenticationProvider() {
//		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
//
//		authProvider.setUserDetailsService(userDetailsService());
//		authProvider.setPasswordEncoder(passwordEncoder());
//
//		return authProvider;
//	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//		auth.authenticationProvider(authenticationProvider());
		auth.userDetailsService(userDetailsServiceFunt()).passwordEncoder(passwordEncoder());
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
		.antMatchers("/admin/**").hasAnyAuthority("ADMIN")
//		.hasRole("ADMIN")
		
		.antMatchers("/editor/**").hasAnyAuthority("EDITOR")
//		.hasRole("EDITOR")
		
		.antMatchers("/user/**")
// 		.hasRole("USER")
 		.hasAnyAuthority("USER")
		
		.antMatchers("/fonts/**",
                "/css/**",
                "/images/**",
                "/js/**").permitAll()
		.anyRequest().authenticated()
		.and()
		.formLogin()
		.loginPage("/prueba")
		.usernameParameter("username")
		.successHandler(successHandler).failureUrl("/login?error=true")
		.permitAll()
		.and()
		.logout().permitAll();

	}

	@Autowired
	private LoginSuccessHandler successHandler;

}
