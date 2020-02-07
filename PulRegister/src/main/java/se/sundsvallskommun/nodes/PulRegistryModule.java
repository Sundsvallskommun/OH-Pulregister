package se.sundsvallskommun.nodes;

import java.io.IOException;
import java.sql.SQLException;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.sundsvallskommun.nodes.controller.FormController;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.utils.CRUDCallback;
import se.unlogic.hierarchy.foregroundmodules.rest.AnnotatedRESTModule;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;

public class PulRegistryModule extends AnnotatedRESTModule implements CRUDCallback<User> {

	private FormController formController;
	
	public PulRegistryModule() {

	}

	/**
	 * Used to automatically create an instance of form controller class
	 */
	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, PulRegistryModule.class.getName(),
				new XMLDBScriptProvider(PulRegistryModule.class.getResourceAsStream("dbscripts/DB script.xml")));

		if (upgradeResult.isUpgrade()) {

			log.info(upgradeResult.toString());
		}

		this.formController = new FormController(this, dataSource, systemInterface);
	}

	/**
	 * Displays list of forms on the landingpage
	 * @WebPublic(toLowerCase = true) sets the method name as end-point in lower case
	 * @param req
	 * @param res
	 * @param user
	 * @param uriParser
	 * @return
	 * @throws Exception
	 */
	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse formList(HttpServletRequest req, HttpServletResponse res, User user,
			URIParser uriParser) throws Exception {

		return formController.listForms(req, res, user, uriParser);
		
	}

	/**
	 * Add a new registry
	 * @WebPublic(toLowerCase = true) sets the method name as end-point in lower case
	 * @param req
	 * @param res
	 * @param user
	 * @param uriParser
	 * @return
	 * @throws Exception
	 */
	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse addForm(HttpServletRequest req, HttpServletResponse res, User user,
			URIParser uriParser) throws Exception {

		return formController.addNewForm(req, res, user, uriParser);
		
	}

	/**
	 * Update an existing registry
	 * @WebPublic(toLowerCase = true) sets the method name as end-point in lower case
	 * @param req
	 * @param res
	 * @param user
	 * @param uriParser
	 * @return
	 * @throws Exception
	 */
	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse updateForm(HttpServletRequest req, HttpServletResponse res, User user,
			URIParser uriParser) throws Exception {
		System.out.println(uriParser);
		return formController.updateExistingForm(req, res, user, uriParser);
		
	}

	/**
	 * Removes a form from the database
	 * @WebPublic(toLowerCase = true) sets the method name as end-point in lower case
	 * @param req
	 * @param res
	 * @param user
	 * @param uriParser
	 * @return
	 * @throws Exception
	 */
	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse removeForm(HttpServletRequest req, HttpServletResponse res, User user,
			URIParser uriParser) throws Exception {

		return formController.deleteForm(req, res, user, uriParser);
		
	}

//	private Document createDocumentElement(HttpServletRequest req, User user, URIParser uriParser) {
//
//		Document doc = XMLUtils.createDomDocument();
//		Element document = doc.createElement("document");
//		doc.appendChild(document);
//		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
//		document.appendChild(moduleDescriptor.toXML(doc));
//		if (user != null)
//			document.appendChild(user.toXML(doc));
//		return doc;
//	}

	@Override
	public Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {

		Document doc = XMLUtils.createDomDocument();

		Element documentElement = doc.createElement("document");
		documentElement.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		documentElement.appendChild(this.moduleDescriptor.toXML(doc));

		doc.appendChild(documentElement);

		return doc;

	}

	@Override
	public String getTitlePrefix() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Loops through questions and questionnaire values to add to csv-file
	 * Used when export to csv button is pressed on list page
	 * @WebPublic(toLowerCase = true) sets the method name as end-point in lower case
	 * @param req
	 * @param res
	 * @param user
	 * @param uriParser
	 * @return
	 * @throws IOException
	 * @throws AccessDeniedException
	 * @throws SQLException
	 */
	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse exportCsv(HttpServletRequest req, HttpServletResponse res, User user,
			URIParser uriParser) throws IOException, AccessDeniedException, SQLException {
		
		formController.exportCSV(req, res, user, uriParser);
		
		return null;
		
	}
		
	/**
	 * Export an empty csv file with only headers
	 * @WebPublic(toLowerCase = true) sets the method name as end-point in lower case
	 * @param req
	 * @param res
	 * @param user
	 * @param uriParser
	 * @return
	 * @throws IOException
	 * @throws AccessDeniedException
	 * @throws SQLException
	 */
	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse exportemptyCsv(HttpServletRequest req, HttpServletResponse res, User user,
			URIParser uriParser) throws IOException, AccessDeniedException, SQLException {

		formController.exportEmptyCSV(req, res, user, uriParser);

		return null;
		
	}
	
	/**
	 * Export selected form to pdf
	 * @WebPublic(toLowerCase = true) sets the method name as end-point in lower case
	 * @param req
	 * @param res
	 * @param user
	 * @param uriParser
	 * @return
	 * @throws IOException
	 * @throws AccessDeniedException
	 * @throws SQLException
	 * @throws COSVisitorException
	 */
	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse exportPdf(HttpServletRequest req, HttpServletResponse res, User user,
			URIParser uriParser) throws IOException, AccessDeniedException, SQLException {
		
		formController.exportPDF(req, res, user, uriParser);

		return null;

	}
	
	/**
	 * Export saved file
	 * @WebPublic(toLowerCase = true) sets the method name as end-point in lower case
	 * @param req
	 * @param res
	 * @param user
	 * @param uriParser
	 * @return
	 * @throws IOException
	 * @throws AccessDeniedException
	 * @throws SQLException
	 */
	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse downloadFile(HttpServletRequest req, HttpServletResponse res, User user,
			URIParser uriParser) throws IOException, AccessDeniedException, SQLException {

		formController.downloadFile(req, res, user, uriParser);

		return null;
		
	}

}
