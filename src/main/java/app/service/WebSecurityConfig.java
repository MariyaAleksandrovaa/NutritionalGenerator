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
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter implements WebMvcConfigurer  {

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
    private static final String[] CLASSPATH_RESOURCE_LOCATIONS = {
            "classpath:/META-INF/resources/", "classpath:/resources/",
            "classpath:/static/", "classpath:/public/" };

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/**")
            .addResourceLocations(CLASSPATH_RESOURCE_LOCATIONS);
    }

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests()
		.antMatchers("/admin/**").hasAnyAuthority("ADMIN")
		
		.antMatchers("/editor/**").hasAnyAuthority("EDITOR")
		
		.antMatchers("/user/**").hasAnyAuthority("USER")
		
		.antMatchers("/fonts/**",
                "/css/**",
                "/images/**",
                "/js/**",
                "/src/**").permitAll()
		// .anyRequest().authenticated()
		.and()
		.formLogin()
		.loginPage("/prueba")
		.usernameParameter("username")
		.successHandler(successHandler).failureUrl("/loginError")
		.permitAll()
		.and()
		.logout().logoutSuccessUrl("/").permitAll();

	}

	@Autowired
	private LoginSuccessHandler successHandler;

}
