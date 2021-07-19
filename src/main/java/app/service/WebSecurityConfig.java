package app.service;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

	// Necesario para evitar que la seguridad se aplique a los resources
	// Como los css, imagenes y javascripts
	private String[] resources = new String[] { "/include/**", "/css/**", "/icons/**", "/img/**", "/js/**",
			"/layer/**" };
	@Autowired
	private DataSource dataSource;

	@Bean
	public UserDetailsService userDetailsService() {
		return new CustomUserDetailsService();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public DaoAuthenticationProvider authenticationProvider() {
		DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

		authProvider.setUserDetailsService(userDetailsService());
		authProvider.setPasswordEncoder(passwordEncoder());

		return authProvider;
	}

	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authenticationProvider());
	}

	/*
	 * permitAll(): cualquiera puede acceder a esas URLs todo lo que empiece por
	 * admin tiene que tener rol de ADMIN cualquier URL que no sea de las anteriores
	 * tiene que estar autenticada
	 * 
	 * and: quiere decir que queremos configurar otra parte de la seguridad
	 * 
	 * loginPage(): que nos lleve a la url login y que cualquiera puede acceser a
	 * ella
	 * 
	 * defaultSuccessUrl() si el usuario se loguea correctamente que lo lleve a menu
	 * failureUrl() si no se loguea correctamente que lo devuelva login con error
	 * 
	 * usernameParameter() el parametro para el usuario va a ser ...
	 * passwordParameter() el parametro para la contraseña va a ser ...
	 */
//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests()
////	        .antMatchers(resources).permitAll()  
////	        .antMatchers("/","/index").permitAll()
//				.antMatchers("/menu").hasAnyAuthority("ADMIN")
////	        .antMatchers("/user*").access("hasRole('USER')")
////                .anyRequest().authenticated()
//				.and().formLogin().loginPage("/login").permitAll()
////                .defaultSuccessUrl("/menu").AU
////                .failureUrl("/login?error=true")
//				.usernameParameter("username").passwordParameter("password").and().logout()
//				.logoutSuccessUrl("/login?logout").permitAll();
//	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {

		http.authorizeRequests().antMatchers("/list_users").authenticated().anyRequest().permitAll()
//		.hasAnyAuthority("ADMIN")

//		.hasAnyRole("ADMIN").anyRequest().authenticated()
//		.authenticated()
//		.anyRequest()
//		.permitAll()
				.and().formLogin().loginPage("/login").usernameParameter("username").defaultSuccessUrl("/list_users")
				.permitAll()

//				.successHandler(successHandler)
//				.permitAll()
//		.defaultSuccessUrl("/list_users")
//		.permitAll()
				.and().logout().logoutSuccessUrl("/").permitAll();
	}

	@Autowired
	public UserDetailsService userDetailsService;

	@Autowired
	public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Autowired
	private LoginSuccessHandler successHandler;

//	@Override
//	protected void configure(HttpSecurity http) throws Exception {
//		http.authorizeRequests().antMatchers("/list_users").authenticated().anyRequest().permitAll().and().formLogin()
//				.usernameParameter("email").defaultSuccessUrl("/list_users").permitAll().and().logout()
//				.logoutSuccessUrl("/").permitAll();
//	}

}
