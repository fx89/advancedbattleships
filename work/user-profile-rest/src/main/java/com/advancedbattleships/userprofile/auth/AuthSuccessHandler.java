package com.advancedbattleships.userprofile.auth;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Service;

import com.advancedbattleships.security.service.SecurityService;

@Service
public class AuthSuccessHandler implements AuthenticationSuccessHandler {

	@Autowired
	SecurityService securityService;
	
	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException
	{
		// Switch to the internal authentication (current user details + granted authorities)
		Authentication internalAuthentication = securityService.resolveInternalAuthentication(authentication);
		SecurityContextHolder.getContext().setAuthentication(internalAuthentication);

		// Redirect to the end-point which will decide where to redirect based on the client type (web, android, etc)
		response.sendRedirect("/auth/handling"); // TODO: change
	}

}
