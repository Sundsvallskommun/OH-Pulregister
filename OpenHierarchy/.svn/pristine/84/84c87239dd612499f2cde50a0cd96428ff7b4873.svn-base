package se.unlogic.hierarchy.backgroundmodules.clonemodule;

import java.net.URLDecoder;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.unlogic.hierarchy.core.annotations.InstanceManagerDependency;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.webutils.http.URIParser;

public class CloneForegroundModule extends AnnotatedForegroundModule {
	
	@InstanceManagerDependency(required = true)
	protected CloneBackgroundModule cloneBackgroundModule;

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Throwable {

		cloneBackgroundModule.createInstance(user);
		
		String redirectURI = req.getParameter("uri");
		
		if (!StringUtils.isEmpty(redirectURI)) {
			res.sendRedirect(URLDecoder.decode(redirectURI, systemInterface.getEncoding()));
		}
		
		return null;
	}
}