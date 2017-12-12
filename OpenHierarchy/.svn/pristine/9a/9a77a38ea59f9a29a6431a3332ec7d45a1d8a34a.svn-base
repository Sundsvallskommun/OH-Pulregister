package se.unlogic.hierarchy.foregroundmodules.htmloutput;

import java.io.IOException;
import java.net.URLDecoder;
import java.sql.SQLException;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.hierarchy.backgroundmodules.htmloutput.HTMLOutputModule;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.Breadcrumb;
import se.unlogic.hierarchy.core.beans.SimpleBackgroundModuleDescriptor;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.BackgroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.MutableSettingHandler;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.utils.AccessUtils;
import se.unlogic.hierarchy.core.utils.FCKConnector;
import se.unlogic.hierarchy.core.utils.FCKUtils;
import se.unlogic.hierarchy.core.utils.ModuleUtils;
import se.unlogic.hierarchy.foregroundmodules.AnnotatedForegroundModule;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.string.StringUtils;
import se.unlogic.standardutils.validation.PositiveStringIntegerValidator;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.url.URLRewriter;

public class HTMLOutputAdminModule extends AnnotatedForegroundModule {

	@ModuleSetting(allowsNull = true)
	@TextFieldSettingDescriptor(name = "Editor CSS", description = "Path to the desired CSS stylesheet for FCKEditor (relative from the contextpath)", required = false)
	protected String cssPath;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "File store", description = "Directory where uploaded files are stored", required = true)
	protected String fileStore;

	@ModuleSetting
	@TextFieldSettingDescriptor(name="Max upload sizee",description="Maxmium upload size in megabytes allowed in a single post request",required=true,formatValidator=PositiveStringIntegerValidator.class)
	protected Integer diskThreshold = 100;

	@ModuleSetting
	@TextFieldSettingDescriptor(name="RAM threshold",description="Maximum size of files in KB to be buffered in RAM during file uploads. Files exceeding the threshold are written to disk instead.",required=true,formatValidator=PositiveStringIntegerValidator.class)
	protected Integer ramThreshold = 500;

	@ModuleSetting
	@TextFieldSettingDescriptor(name = "Editor height", description = "The height of CKEditor", required = false, formatValidator = PositiveStringIntegerValidator.class)
	protected Integer editorHeight = 200;
	
	private FCKConnector connector;

	@Override
	protected void parseSettings(MutableSettingHandler mutableSettingHandler) throws Exception {

		super.parseSettings(mutableSettingHandler);

		this.connector = new FCKConnector(this.fileStore, this.diskThreshold, this.ramThreshold);
	}

	@Override
	public ForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		return this.settings(req, res, user, uriParser);
	}

	@WebPublic
	public ForegroundModuleResponse settings(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		Integer moduleID;
		Entry<BackgroundModuleDescriptor, HTMLOutputModule> moduleEntry;

		if (uriParser.size() == 3 && (moduleID = NumberUtils.toInt(uriParser.get(2))) != null && (moduleEntry = ModuleUtils.findBackgroundModule(HTMLOutputModule.class, moduleID, true, sectionInterface.getSystemInterface().getRootSection())) != null) {

			if (AccessUtils.checkAccess(user, moduleEntry.getValue())) {

				Document doc = XMLUtils.createDomDocument();
				Element documentElement = doc.createElement("Document");
				doc.appendChild(documentElement);

				Element settingsElement = doc.createElement("Settings");
				documentElement.appendChild(settingsElement);

				documentElement.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
				documentElement.appendChild(this.moduleDescriptor.toXML(doc));

				String currentHTML = null;

				if (req.getMethod().equalsIgnoreCase("post")) {

					String newHTML = req.getParameter("html");

					HTMLOutputModule htmlOutputModule = moduleEntry.getValue();

					if (htmlOutputModule.htmlIsRequired() && StringUtils.isEmpty(newHTML)) {

						settingsElement.appendChild(new ValidationError("html", ValidationErrorType.RequiredField).toXML(doc));

						log.info("User " + user + " failed validation with validation error while updating HTMLOutputModule " + moduleDescriptor);

						currentHTML = "";

					} else {

						log.info("User " + user + " updating HTMLOutputModule " + moduleDescriptor);

						if(!StringUtils.isEmpty(newHTML)) {

							newHTML = FCKUtils.removeAbsoluteFileUrls(newHTML, getAbsoluteFileURL(uriParser));

							newHTML = URLRewriter.removeAbsoluteLinkUrls(newHTML, req);
						}

						htmlOutputModule.setHtml(newHTML);

						SimpleBackgroundModuleDescriptor backgroundModuleDescriptor = (SimpleBackgroundModuleDescriptor)moduleEntry.getKey();
						MutableSettingHandler settingsHandler = backgroundModuleDescriptor.getMutableSettingHandler();
						
						settingsHandler.setSetting("html", newHTML);
						
						Integer priority = NumberUtils.toInt(req.getParameter("priority"));
						
						if (priority != null && !priority.equals(backgroundModuleDescriptor.getPriority())) {
							SectionInterface sectionInterface = systemInterface.getSectionInterface(moduleEntry.getKey().getSectionID());
							
							if (sectionInterface != null) {
								backgroundModuleDescriptor.setPriority(priority);
								sectionInterface.getBackgroundModuleCache().update(backgroundModuleDescriptor);
							}
						}

						backgroundModuleDescriptor.saveSettings(this.systemInterface);

						String redirect = req.getParameter("redirect");

						if(redirect != null && redirect.startsWith("/")){

							res.sendRedirect(redirect);

						}else{

							res.sendRedirect(RequestUtils.getFullContextPathURL(req));
						}

						return null;
					}
				}

				if(currentHTML == null){

					currentHTML = moduleEntry.getValue().getHtml();

					if(!StringUtils.isEmpty(currentHTML)) {

						currentHTML = FCKUtils.setAbsoluteFileUrls(currentHTML, getAbsoluteFileURL(uriParser));

						currentHTML = URLRewriter.setAbsoluteLinkUrls(currentHTML, req);

						XMLUtils.appendNewElement(doc, settingsElement, "html", currentHTML);
					}
				}
				
				XMLUtils.appendNewElement(doc, settingsElement, "Priority", moduleEntry.getKey().getPriority());

				XMLUtils.appendNewCDATAElement(doc, documentElement, "cssPath", cssPath);
				XMLUtils.appendNewCDATAElement(doc, documentElement, "adminCssClass", moduleEntry.getValue().getAdminCssClass());
				XMLUtils.appendNewCDATAElement(doc, documentElement, "editorHeight", editorHeight);
				
				String redirect = req.getParameter("redirect");

				if(redirect != null && redirect.startsWith("/")){

					XMLUtils.appendNewElement(doc, settingsElement, "redirect", redirect);
				}

				return new SimpleForegroundModuleResponse(doc, this.moduleDescriptor.getName(), getDefaultBreadcrumb(), new Breadcrumb(this, moduleEntry.getKey().getName(), "/settings/" + moduleEntry.getKey().getModuleID()));
			}

			throw new AccessDeniedException(this.sectionInterface.getSectionDescriptor());

		}

		throw new URINotFoundException(uriParser);
	}

	public ForegroundModuleDescriptor getModuleDescriptor(){

		return moduleDescriptor;
	}
	
	@WebPublic
	public SimpleForegroundModuleResponse delete(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws SQLException, URINotFoundException, IOException {

		SimpleBackgroundModuleDescriptor simpleModuleDescriptor = null;

		if (uriParser.size() == 3 && NumberUtils.isInt(uriParser.get(2)) && (simpleModuleDescriptor = systemInterface.getCoreDaoFactory().getBackgroundModuleDAO().getModule(Integer.parseInt(uriParser.get(2)))) != null) {

			this.log.info("User " + user + " deleting background module " + simpleModuleDescriptor);

			// Get the sectioninterface of the section containing the module
			SectionInterface sectionInterface = systemInterface.getSectionInterface(simpleModuleDescriptor.getSectionID());

			// Check if the module is cached
			if (sectionInterface != null && sectionInterface.getBackgroundModuleCache().isCached(simpleModuleDescriptor)) {

				// Module is cached unload it
				try {
					sectionInterface.getBackgroundModuleCache().unload(simpleModuleDescriptor);
				} catch (Exception e) {
					this.log.error("Error unloading background module " + simpleModuleDescriptor + " from section " + sectionInterface.getSectionDescriptor() + " while deleting module requested by user " + user);
				}
			}

			// Delete section from database
			systemInterface.getCoreDaoFactory().getBackgroundModuleDAO().delete(simpleModuleDescriptor);

			String redirect = req.getParameter("redirect");
			
			if (!StringUtils.isEmpty(redirect)) {
				res.sendRedirect(URLDecoder.decode(redirect, systemInterface.getEncoding()));
			}

			return null;

		} else {
			throw new URINotFoundException(uriParser);
		}
	}

	@WebPublic
	public SimpleForegroundModuleResponse connector(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws TransformerFactoryConfigurationError, TransformerException, IOException {

		this.connector.processRequest(req, res, uriParser, user, moduleDescriptor);

		return null;
	}

	@WebPublic
	public SimpleForegroundModuleResponse file(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser) throws Exception {

		this.connector.processFileRequest(req, res, user, uriParser, moduleDescriptor, sectionInterface, 2, null);

		return null;
	}

	private String getAbsoluteFileURL(URIParser uriParser) {

		return uriParser.getCurrentURI(true) + "/" + this.moduleDescriptor.getAlias() + "/file";
	}
}