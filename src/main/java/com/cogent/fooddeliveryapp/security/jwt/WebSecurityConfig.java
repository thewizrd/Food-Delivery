package com.cogent.fooddeliveryapp.security.jwt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * WebSecurityConfig
 *
 * @author bryan
 * @date Feb 23, 2022-4:42:01 PM
 */
@Configuration // marks as Configuration class (containing 1+ @Beans)
@EnableWebSecurity // enables Spring Security environment (via HttpSecurity)
@EnableGlobalMethodSecurity(prePostEnabled = true) // AOP Security (method level authorization)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	private UserDetailsService userDetailsService;
	@Autowired
	private AuthEntryPointJwt authEntryPointJwt;
	
	@Bean // for customization as needed
	public AuthTokenFilter jwtAuthTokenFilter() {
		return new AuthTokenFilter();
	}
	
	@Bean // method level annotation
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService)
			.passwordEncoder(passwordEncoder());
	}
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		/*
		 * Web Security Core
		 * 
		 * Allows us to restrict access to endpoints
		 * Restrict unauthorized access to endpoints
		 * Provide public access to login/register
		 * Provide token validation
		 * CORS:
		 */
		super.configure(http);
	}
}
