package se.unlogic.hierarchy.filtermodules.login;

import java.io.IOException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.StringTokenizer;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerException;

import se.unlogic.hierarchy.core.annotations.CheckboxSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.EnumDropDownSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.beans.MutableUser;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.enums.EventTarget;
import se.unlogic.hierarchy.core.interfaces.FilterChain;
import se.unlogic.hierarchy.filtermodules.AnnotatedFilterModule;
import se.unlogic.hierarchy.foregroundmodules.login.LoginEvent;
import se.unlogic.log4jutils.levels.LogLevel;
import se.unlogic.standardutils.base64.Base64;
import se.unlogic.webutils.http.URIParser;

public class BasicAuthenticationLoginModule extends AnnotatedFilterModule {

	@ModuleSetting
	@CheckboxSettingDescriptor(name="Add user to session", description="Controls if the user object should be added to the session or if the login should only be valid for the current request.")
	protected boolean useSession;

	@ModuleSetting
	@CheckboxSettingDescriptor(name="Update last login",description="Controls if the last login field of the user should be updated")
	protected boolean updateLastLogin;

	@ModuleSetting
	@EnumDropDownSettingDescriptor(name="Login log level", description="The log level used to log logins via this module", required=true)
	private LogLevel loginLogLevel = LogLevel.INFO;	
	
	@ModuleSetting
	@TextFieldSettingDescriptor(name="Realm", description="The name of this realm", required = true)
	private String realm = "Protected";
	
	@Override
	public void processFilterRequest(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser, FilterChain filterChain) throws IOException, TransformerException, SQLException {

		String authHeader = req.getHeader("Authorization");

		if (authHeader != null) {

			StringTokenizer st = new StringTokenizer(authHeader);

			if (st.hasMoreTokens()) {

				String basic = st.nextToken();

				if (basic.equalsIgnoreCase("Basic")) {

					String decodedCredentials = new String(Base64.decode(st.nextToken()), "UTF-8");

					int index = decodedCredentials.indexOf(":");

					if (index != -1) {

						String username = decodedCredentials.substring(0, index).trim();
						String password = decodedCredentials.substring(index + 1).trim();

						User requestedUser = systemInterface.getUserHandler().getUserByUsernamePassword(username, password, true, true);
						
						if (requestedUser == null) {
							
							log.warn("Failed login attempt using username " + username + " from address " + req.getRemoteHost());
							
							unauthorized(res, "Bad credentials");
							return;

						}else if(user != null && user.equals(requestedUser)){
							
							filterChain.doFilter(req, res, user, uriParser);
							return;
							
						}else{

							if(updateLastLogin){

								setLastLogin(requestedUser);
							}

							if(useSession){

								req.getSession(true).setAttribute("user", requestedUser);
							}							
							
							log.log(loginLogLevel.getLevel(), "User " + requestedUser + " logged in from address " + req.getRemoteHost());

							systemInterface.getEventHandler().sendEvent(User.class, new LoginEvent(requestedUser, req.getSession()), EventTarget.ALL);
							
							filterChain.doFilter(req, res, requestedUser, uriParser);
							return;
						}


					} else {
						unauthorized(res, "Invalid authentication token");
						return;
					}
				}
			}

		}
		
		unauthorized(res, "Unauthorized");
	}

	protected void unauthorized(HttpServletResponse response, String message) throws IOException {

		response.setHeader("WWW-Authenticate", "Basic realm=\"" + realm + "\"");
		response.sendError(401, message);
	}

	protected void setLastLogin(User user) throws SQLException {

		user.setCurrentLogin(new Timestamp(System.currentTimeMillis()));

		if(user instanceof MutableUser){

			MutableUser mutableUser = (MutableUser) user;

			Timestamp lastLogin = user.getLastLogin();

			mutableUser.setLastLogin(user.getCurrentLogin());

			try {
				systemInterface.getUserHandler().updateUser(mutableUser, false, false, false);

			} catch (Exception e) {

				log.error("Unable to update last login for user " + user,e);
			}

			mutableUser.setLastLogin(lastLogin);
		}
	}
}
