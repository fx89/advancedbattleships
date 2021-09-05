package com.advancedbattleships.security.service;

import static com.advancedbattleships.common.lang.Suppliers.nullSafeSupplier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.stereotype.Service;

import com.advancedbattleships.security.dataservice.SecurityDataService;
import com.advancedbattleships.security.dataservice.model.Authority;
import com.advancedbattleships.security.dataservice.model.Group;
import com.advancedbattleships.security.dataservice.model.LoginSource;
import com.advancedbattleships.security.dataservice.model.User;
import com.advancedbattleships.security.dataservice.model.UserLoginSource;
import com.advancedbattleships.security.exception.UnknownLoginProviderException;
import com.advancedbattleships.security.service.utility.NicknameGenerator;
import com.advancedbattleships.security.service.utility.UserPingTracker;
import com.advancedbattleships.system.SystemService;
import com.advancedbattleships.utilityservices.TaskManagerService;
import com.advancedbattleships.utilityservices.UniqueTokenProviderService;

@Service
public class SecurityService {

	private static String USR_GRP_NAME = "USERS";

	@Autowired
	private UniqueTokenProviderService uniqueTokenProvider;
	
	@Autowired
	private SecurityDataService securityDataService;

	@Autowired
	private SystemService system;

	@Autowired
	TaskManagerService taskManager;

	private UserPingTracker userPingTracker = new UserPingTracker();

	@PostConstruct
	public void init() {

		userPingTracker.withMaxInactivitySecs(
				system.getDataService().getIntParameter("SECURITY.SESSION_CHECK_SECS")
			);

		userPingTracker.withExpectedUserCount(
				system.getDataService().getIntParameter("SECURITY.EXPECTED_ONLINE_USER_COUNT")
			);

		taskManager.startRecurrentTask(
				"sessionCheck",
				system.getDataService().getIntParameter("SECURITY.SESSION_CHECK_SECS"),
				() -> sessionCheck()
			);
	}

	public User getCurrentUser() {
		return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
	}

	public boolean isUserAuthenticated() {
		return
			nullSafeSupplier(
				() -> ! SecurityContextHolder.getContext().getAuthentication().getPrincipal().equals("anonymousUser")
				,false
			);
	}

	/**
	 * Registers a ping from the current user, to keep the session alive
	 */
	public void keepAlive() {
		if (isUserAuthenticated()) {
			System.out.println("ping"); // TODO: remove
			userPingTracker.pingUser(getCurrentUser().getUniqueToken());
		}
	}

	/**
	 * Sets the "logged in" flag to false for all users who are not active
	 */
	public void sessionCheck() {
		System.out.println("sessionCheck"); // TODO: remove

		Set<String> inactiveUserTokens = userPingTracker.computeInactiveUserTokens();

		if (!inactiveUserTokens.isEmpty()) {
			securityDataService.setOnlineFlagForUsers(
					inactiveUserTokens,
					false
				);

			userPingTracker.clearInactiveUserTokens(inactiveUserTokens);
		}

		// TODO: Eventually run a full cleanup once every hour,
		// to set the online flag to false for any user that's not in the online users list

		// TODO: Try to figure out a way to do this even if there will be many instances of the
		// user service (maybe move the functionality into a micro-service and make sure it has only one instance)
	}

	/**
	 * Called after the user was logged in
	 */
	public void postLoginOperations() {
		User currentUser = getCurrentUser();
		currentUser.setOnline(true);
		securityDataService.saveUser(currentUser);
	}

	/**
	 * Called from the AuthSuccessHandler to put the user details into the security context
	 */
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
		User ret = null;

		UserLoginSource userLoginSource
			= securityDataService.getUserLoginSourceByLoginSourceAndLoginToken(loginSource, loginToken);

		if (userLoginSource == null) {
			userLoginSource = securityDataService.createUserLoginSource(
					uniqueTokenProvider.provide(),
					userName, pictureUrl, primaryEmail, loginSource, loginToken,
					NicknameGenerator.generateNickName(),
					true
			);
		}

		ret = userLoginSource.getUser();

		if (ret.getGroups() == null || ret.getGroups().stream().noneMatch(g -> g.getName().equals(USR_GRP_NAME) )) {
			ret = securityDataService.mapUserToGroup(userLoginSource.getUser(), securityDataService.findGroupByName(USR_GRP_NAME));
		}

		return ret;
	}

	private String extractLoginTokenFromUserPropertiesByLoginSource(LoginSource loginSource, Map<String, Object> userProperties) {
		if (loginSource == LoginSource.GOOGLE) {
			return (String) userProperties.get("email");
		}

		throw new UnknownLoginProviderException(
				"Unable to handle login provider [" + loginSource + "]"
			);
	}

	public User setCurrentUserNickName(String nickName) {
		User ret = securityDataService.findUserByUniqueToken(this.getCurrentUser().getUniqueToken());
		ret.setNickName(nickName);
		ret = securityDataService.saveUser(ret);
		getCurrentUser().setNickName(nickName);
		return ret;
	}

	public Set<User> getUsers(Set<String> userUniqueTokens) {
		return securityDataService.findUsersByUniqueToken(userUniqueTokens);
	}

	public User findUserByNickName(String nickName) {
		return securityDataService.findUserByNickName(nickName);
	}
}
