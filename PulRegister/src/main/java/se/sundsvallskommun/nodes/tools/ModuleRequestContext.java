package se.sundsvallskommun.nodes.tools;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.webutils.http.URIParser;

public class ModuleRequestContext
{
	private HttpServletRequest req;
	private HttpServletResponse res; 
	private User user;
	private URIParser uriParser;
	
	public ModuleRequestContext(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) {
	
		this.req = req;
		this.res = res; 
		this.user = user;
		this.uriParser = uriParser;
	}

	public HttpServletRequest getReq() {
		return req;
	}

	public void setReq(HttpServletRequest req) {
		this.req = req;
	}

	public HttpServletResponse getRes() {
		return res;
	}

	public void setRes(HttpServletResponse res) {
		this.res = res;
	}

	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	public URIParser getUriParser() {
		return uriParser;
	}

	public void setUriParser(URIParser uriParser) {
		this.uriParser = uriParser;
	}
	
};

