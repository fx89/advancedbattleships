package com.advancedbattleships.security.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import com.advancedbattleships.security.dataservice.SecurityDataService;
import com.advancedbattleships.security.dataservice.model.Authority;
import com.advancedbattleships.security.dataservice.model.Group;
import com.advancedbattleships.security.dataservice.model.LoginSource;
import com.advancedbattleships.security.dataservice.model.User;
import com.advancedbattleships.security.dataservice.model.UserLoginSource;
import com.advancedbattleships.security.exception.UnknownLoginProviderException;

@Service
public class SecurityService {

	@Autowired
	SecurityDataService securityDataService;
	
	public Authentication resolveInternalAuthentication(Authentication externalAuthentication) {
		
		if (externalAuthentication.getPrincipal() instanceof OidcUser) {
			return resolveInternalAuthenticationFromOidcUser((OidcUser) externalAuthentication.getPrincipal());			
		}

		throw new UnsupportedOperationException(
			"Logins of type [" + externalAuthentication.getPrincipal().getClass() + "] not yet implemented"
		);
	}

	private Authentication resolveInternalAuthenticationFromOidcUser(OidcUser oidcUser) {
		// Extract authentication details
		LoginSource loginSource = identifyLoginSource(oidcUser.getAttributes().get("iss").toString());
		String name = (String) oidcUser.getAttributes().get("name");
		String email = (String) oidcUser.getAttributes().get("email");
		String pictureUrl = (String) oidcUser.getAttributes().get("picture");
		String loginToken = extractLoginTokenFromUserPropertiesByLoginSource(loginSource, oidcUser.getAttributes());

		// Resolve the user
		User user = resolveUserByLoginSourceAndLoginToken(name, pictureUrl, email, loginSource, loginToken);

		// Get a list of distinct authorities granted to the current user
		Iterable<Group> userGroups = user.getGroups();
		List<SimpleGrantedAuthority> distinctAuthorities
			= userGroups == null ? new ArrayList<>() :
				StreamSupport.stream(userGroups.spliterator(), false)
					.map(group -> group.getAuthorities())
					.flatMap(itAuth -> itAuth == null ? new ArrayList<Authority>().stream() : StreamSupport.stream(itAuth.spliterator(), false))
					.map(auth -> new SimpleGrantedAuthority(auth.getName()))
					.collect(Collectors.toList())
		;

		// Create the internal authentication object, which will replace the external one.
		// This will contain the internal user and all of its granted authorities.
		UsernamePasswordAuthenticationToken internalAuthentication
			= new UsernamePasswordAuthenticationToken(user, null, distinctAuthorities);
		
		// Finally, return the constructed internal authentication
		return internalAuthentication;
	}

	private LoginSource identifyLoginSource(String sourceIdToken) {
		if (sourceIdToken != null) {
			if (sourceIdToken.toUpperCase().contains("GOOGLE")) {
				return LoginSource.GOOGLE;
			}
		}

		throw new UnknownLoginProviderException(
			"Unable to handle login provider [" + (sourceIdToken == null ? "null" : sourceIdToken) + "]"
		);
	}

	private User resolveUserByLoginSourceAndLoginToken(
			String userName, String pictureUrl, String primaryEmail, LoginSource loginSource, String loginToken
	) {
		UserLoginSource userLoginSource
			= securityDataService.getUserLoginSourceByLoginSourceAndLoginToken(loginSource, loginToken);

		if (userLoginSource == null) {
			userLoginSource = securityDataService.createUserLoginSource(
					userName, pictureUrl, primaryEmail, loginSource, loginToken
			);
		}

		return userLoginSource.getUser();
	}

	private String extractLoginTokenFromUserPropertiesByLoginSource(LoginSource loginSource, Map<String, Object> userProperties) {
		if (loginSource == LoginSource.GOOGLE) {
			return (String) userProperties.get("email");
		}

		throw new UnknownLoginProviderException(
				"Unable to handle login provider [" + loginSource + "]"
			);
	}
}