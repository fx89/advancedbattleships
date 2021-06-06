package com.advancedbattleships.userprofile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import com.advancedbattleships.userprofile.auth.AuthSuccessHandler;

@Configuration
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private AuthSuccessHandler authSuccessHandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
	    http
	    	.csrf().disable()
	    	.formLogin().loginPage("/auth/login")
	    	.and()
	    	.authorizeRequests()
	            .antMatchers("/error**", "/oauth2/authorization/**", "/auth/**", "/pages/**", "/favicon.ico")
	            	.permitAll()
	            .anyRequest()
	            	.authenticated()
	            	.and()
	            	.oauth2Login()
	            	.successHandler(authSuccessHandler)
		;
	}

}
