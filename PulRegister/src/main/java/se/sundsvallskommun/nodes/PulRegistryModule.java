package se.sundsvallskommun.nodes;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;

import org.apache.pdfbox.exceptions.COSVisitorException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.mysql.jdbc.exceptions.jdbc4.MySQLIntegrityConstraintViolationException;

import rst.pdfbox.layout.elements.Paragraph;
import rst.pdfbox.layout.text.BaseFont;
import rst.pdfbox.layout.text.Indent;
import rst.pdfbox.layout.text.SpaceUnit;
import se.sundsvallskommun.nodes.beans.NodeFile;
import se.sundsvallskommun.nodes.beans.NodeFileInfo;
import se.sundsvallskommun.nodes.beans.NodeOwner;
import se.sundsvallskommun.nodes.beans.NodeAttribute;
import se.sundsvallskommun.nodes.beans.NodeAttributeNote;
import se.sundsvallskommun.nodes.beans.NodeTag;
import se.sundsvallskommun.nodes.beans.NodeType;
import se.sundsvallskommun.nodes.beans.NodeTemplateAttribute;
import se.sundsvallskommun.nodes.cruds.NodeAttributeCRUD;
import se.sundsvallskommun.nodes.cruds.NodeOwnerCRUD;
import se.sundsvallskommun.nodes.cruds.NodeTagCRUD;
import se.sundsvallskommun.nodes.cruds.NodeTypeCRUD;
import se.sundsvallskommun.nodes.rest.NodeRestOperations;
import se.sundsvallskommun.nodes.tools.DynamicAttributes;
import se.sundsvallskommun.nodes.tools.ModuleRequestContext;
import se.unlogic.hierarchy.core.annotations.ModuleSetting;
import se.unlogic.hierarchy.core.annotations.TextFieldSettingDescriptor;
import se.unlogic.hierarchy.core.annotations.WebPublic;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleDescriptor;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SectionInterface;
import se.unlogic.hierarchy.core.utils.CRUDCallback;
import se.unlogic.hierarchy.core.utils.HierarchyAnnotatedDAOFactory;
import se.unlogic.hierarchy.core.utils.crud.TransactionRequestFilter;
import se.unlogic.hierarchy.foregroundmodules.rest.AnnotatedRESTModule;
import se.unlogic.hierarchy.foregroundmodules.rest.RESTMethod;
import se.unlogic.hierarchy.foregroundmodules.rest.URIParam;
import se.unlogic.standardutils.dao.AdvancedAnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.LowLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.db.tableversionhandler.TableVersionHandler;
import se.unlogic.standardutils.db.tableversionhandler.UpgradeResult;
import se.unlogic.standardutils.db.tableversionhandler.XMLDBScriptProvider;
import se.unlogic.standardutils.json.JsonArray;
import se.unlogic.standardutils.json.JsonObject;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.http.enums.ContentDisposition;

public class PulRegistryModule extends AnnotatedRESTModule implements CRUDCallback<User> {

	protected AnnotatedDAO<NodeOwner> nodeDAO;
	protected AnnotatedDAO<NodeType> nodeTypeDAO;
	protected AnnotatedDAO<NodeAttribute> nodeAttributeDAO;
	protected AnnotatedDAO<NodeTag> nodeTagDAO;
	protected AnnotatedDAO<NodeTemplateAttribute> templateAttributeDAO;
	protected AnnotatedDAO<NodeFile> fileDAO;
	protected AnnotatedDAO<NodeFileInfo> fileInfoDAO;
	protected AnnotatedDAO<NodeAttributeNote> noteDAO;

	protected NodeOwnerCRUD nodeCRUD;
	protected NodeTypeCRUD nodeTypeCRUD;
	protected NodeAttributeCRUD nodeAttributeCRUD;
	protected NodeTagCRUD nodeTagCRUD;

	protected QueryParameterFactory<NodeType, Integer> facilityTypeIDParamFactory;
	protected QueryParameterFactory<NodeType, NodeType> facilityTypeParentIDParamFactory;
	protected QueryParameterFactory<NodeTemplateAttribute, Integer> facilityTemplateIDParamFactory;
	protected QueryParameterFactory<NodeTemplateAttribute, NodeType> facilityTemplateFromParentTypeParamFactory;
	protected QueryParameterFactory<NodeFile, NodeOwner> nodeFileIDParamFactory;

	protected QueryParameterFactory<NodeTag, String> facilityTagNameParamFactory;
	protected QueryParameterFactory<NodeFile, Integer> facilitySingleFileIDParamFactory;
	protected QueryParameterFactory<NodeFileInfo, Integer> facilitySingleFileInfoIDParamFactory;

	protected NodeRestOperations nodeRestOperations;
	
//	@ModuleSetting
//	@TextFieldSettingDescriptor(description="Path till var dokument sparas.", name = "document filepath")
//	protected String newFilePath = "/usr/share/tomcat/sites/register.sundsvall.se/filestore/";

	public PulRegistryModule() {

	}

	@Override
	protected void createDAOs(DataSource dataSource) throws Exception {

		UpgradeResult upgradeResult = TableVersionHandler.upgradeDBTables(dataSource, PulRegistryModule.class.getName(),
				new XMLDBScriptProvider(PulRegistryModule.class.getResourceAsStream("dbscripts/DB script.xml")));

		if (upgradeResult.isUpgrade()) {

			log.info(upgradeResult.toString());
		}

		HierarchyAnnotatedDAOFactory daoFactory = new HierarchyAnnotatedDAOFactory(dataSource,
				systemInterface.getUserHandler(), systemInterface.getGroupHandler());
		this.nodeDAO = daoFactory.getDAO(NodeOwner.class);
		this.nodeTypeDAO = daoFactory.getDAO(NodeType.class);
		this.nodeAttributeDAO = daoFactory.getDAO(NodeAttribute.class);
		this.templateAttributeDAO = daoFactory.getDAO(NodeTemplateAttribute.class);
		this.nodeTagDAO = daoFactory.getDAO(NodeTag.class);
		this.fileDAO = daoFactory.getDAO(NodeFile.class);
		this.fileInfoDAO = daoFactory.getDAO(NodeFileInfo.class);
		this.noteDAO = daoFactory.getDAO(NodeAttributeNote.class);

		AdvancedAnnotatedDAOWrapper<NodeOwner, Integer> facilityNodeDaoWrapper = this.nodeDAO
				.getAdvancedWrapper(Integer.class);
		AdvancedAnnotatedDAOWrapper<NodeType, Integer> NodeTypeDaoWrapper = this.nodeTypeDAO
				.getAdvancedWrapper(Integer.class);
		AdvancedAnnotatedDAOWrapper<NodeAttribute, Integer> facilityNodeAttributeDaoWrapper = this.nodeAttributeDAO
				.getAdvancedWrapper(Integer.class);
		AdvancedAnnotatedDAOWrapper<NodeTag, Integer> facilityNodeTagDaoWrapper = this.nodeTagDAO
				.getAdvancedWrapper(Integer.class);

		facilityNodeDaoWrapper.getGetQuery().addRelations(NodeOwner.TAGS_RELATION);
		facilityNodeDaoWrapper.getGetAllQuery().addRelations(NodeOwner.TAGS_RELATION);
		facilityNodeDaoWrapper.getAddQuery().addRelations(NodeOwner.TAGS_RELATION);
		facilityNodeDaoWrapper.getUpdateQuery().addRelations(NodeOwner.TAGS_RELATION);

		this.nodeCRUD = new NodeOwnerCRUD(facilityNodeDaoWrapper, this, systemInterface);
		this.nodeTypeCRUD = new NodeTypeCRUD(NodeTypeDaoWrapper, this);
		this.nodeAttributeCRUD = new NodeAttributeCRUD(facilityNodeAttributeDaoWrapper, this);
		this.nodeTagCRUD = new NodeTagCRUD(facilityNodeTagDaoWrapper, this);

		this.facilityTypeIDParamFactory = this.nodeTypeDAO.getParamFactory("type_id", Integer.class);
		this.facilityTypeParentIDParamFactory = this.nodeTypeDAO.getParamFactory("parentType", NodeType.class);
		this.facilityTemplateIDParamFactory = this.templateAttributeDAO.getParamFactory("templateAttributeID",
				Integer.class);
		this.facilityTemplateFromParentTypeParamFactory = this.templateAttributeDAO.getParamFactory("parentNodeType",
				NodeType.class);
		this.nodeFileIDParamFactory = this.fileDAO.getParamFactory("parentNode", NodeOwner.class);
		this.facilitySingleFileIDParamFactory = this.fileDAO.getParamFactory("nodeFileID", Integer.class);
		this.facilitySingleFileInfoIDParamFactory = this.fileInfoDAO.getParamFactory("nodeFileID", Integer.class);
		this.facilityTagNameParamFactory = this.nodeTagDAO.getParamFactory("tagName", String.class);

		this.nodeCRUD.addRequestFilter(new TransactionRequestFilter(dataSource));

	};

	@Override
	public void init(ForegroundModuleDescriptor moduleDescriptor, SectionInterface sectionInterface,
			DataSource dataSource) throws Exception {

		nodeRestOperations = new NodeRestOperations(this);
		super.init(moduleDescriptor, sectionInterface, dataSource);
	}

	@RESTMethod(alias = "rest/api/1/registry/{registryIndex}", method = "get")
	public void getFacilityREST(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser,
			@URIParam(name = "registryIndex") Integer facilityIndex) throws Throwable {

		this.nodeRestOperations.getNode(new ModuleRequestContext(req, res, user, uriParser), facilityIndex);
	}

	@RESTMethod(alias = "rest/api/1/registry", method = "get")
	public void getFacilitiesREST(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser)
			throws Throwable {

		this.nodeRestOperations.getNodes(new ModuleRequestContext(req, res, user, uriParser));
	}

	@RESTMethod(alias = "rest/api/1/registryType/{typeIndex}/templateAttribute", method = "get")
	public void getTemplateAttributesREST(HttpServletRequest req, HttpServletResponse res, User user,
			URIParser uriParser, @URIParam(name = "typeIndex") Integer typeIndex) throws Throwable {

		this.nodeRestOperations.getTemplateAttributes(new ModuleRequestContext(req, res, user, uriParser), typeIndex);
	}

	@RESTMethod(alias = "rest/api/1/registryType/{typeIndex}/templateAttribute/{index}", method = "get")
	public void getTemplateAttributesREST(HttpServletRequest req, HttpServletResponse res, User user,
			URIParser uriParser, @URIParam(name = "typeIndex") Integer typeIndex,
			@URIParam(name = "index") Integer index) throws Throwable {

		this.nodeRestOperations.getTemplateAttribute(new ModuleRequestContext(req, res, user, uriParser), typeIndex,
				index);
	}

	@RESTMethod(alias = "rest/api/1/registryType", method = "get")
	public void getRegistryTypeREST(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser)
			throws Throwable {

		this.nodeRestOperations.getRegistryTypes(new ModuleRequestContext(req, res, user, uriParser));
	}

	@RESTMethod(alias = "rest/api/1/registryType/{index}", method = "get")
	public void getRegistryTypeREST(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser,
			@URIParam(name = "index") Integer index) throws Throwable {

		this.nodeRestOperations.getRegistryType(new ModuleRequestContext(req, res, user, uriParser), index);
	}

	@Override
	public ForegroundModuleResponse processRequest(HttpServletRequest req, HttpServletResponse res, User user,
			URIParser uriParser) throws Throwable {

		return super.processRequest(req, res, user, uriParser);
	}

	@Override
	public SimpleForegroundModuleResponse defaultMethod(HttpServletRequest req, HttpServletResponse res, User user,
			URIParser uriParser) throws Exception {
		Document doc = createDocumentElement(req, user, uriParser);
		return new SimpleForegroundModuleResponse(doc, this.moduleDescriptor.getName(), this.getDefaultBreadcrumb());
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse showtags(HttpServletRequest req, HttpServletResponse res, User user,
			URIParser uriParser) throws Exception {

		ForegroundModuleResponse fgRes = this.nodeTagCRUD.list(req, res, user, uriParser, null);
		return fgRes;
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse serviceobjekt(HttpServletRequest req, HttpServletResponse res, User user,
			URIParser uriParser) throws Exception {

		return nodeCRUD.list(req, res, user, uriParser, null);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse showFlatList(HttpServletRequest req, HttpServletResponse res, User user,
			URIParser uriParser) throws Exception {

		ForegroundModuleResponse resp = nodeCRUD.list(req, res, user, uriParser, null);
		return resp;
	}

	//Add a registry
	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse addFacility(HttpServletRequest req, HttpServletResponse res, User user,
			URIParser uriParser) throws Exception {

		NodeOwner bean = this.nodeCRUD.getRequestedBean(req, res, user, uriParser, "ADD");
		this.nodeCRUD.checkAccess(bean, user);

		ForegroundModuleResponse fgResp = this.nodeCRUD.add(req, res, user, uriParser);
		
		try {
			if(fgResp != null) {
				Document doc = fgResp.getDocument();
				Element element = (Element) fgResp.getDocument().getFirstChild().getFirstChild().getNextSibling().getNextSibling();
				XMLUtils.appendNewElement(doc, element, "sessionTimeout", user.getSession().getMaxInactiveInterval());
				XMLUtils.appendNewElement(doc, element, "sessionLastAccess", user.getSession().getLastAccessedTime());
			}
		} catch(Exception e) {}
		
		return fgResp;
	}

	//Update an existing registry
	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse updateFacility(HttpServletRequest req, HttpServletResponse res, User user,
			URIParser uriParser) throws Exception {

		NodeOwner bean = this.nodeCRUD.getRequestedBean(req, res, user, uriParser, "UPDATE");
		this.nodeCRUD.checkAccess(bean, user);
		ForegroundModuleResponse result = null;

		result = this.nodeCRUD.update(req, res, user, uriParser);
		
		try {
			if(result != null) {
				Document doc = result.getDocument();
				Element element = (Element) result.getDocument().getFirstChild().getFirstChild().getNextSibling().getNextSibling();
				XMLUtils.appendNewElement(doc, element, "sessionTimeout", user.getSession().getMaxInactiveInterval());
				XMLUtils.appendNewElement(doc, element, "sessionLastAccess", user.getSession().getLastAccessedTime());
			}
		} catch(Exception e) {}
			
		return result;
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse updateFacilityType(HttpServletRequest req, HttpServletResponse res, User user,
			URIParser uriParser) throws Exception {

		this.nodeCRUD.checkAccess(null, user);

		return this.nodeTypeCRUD.update(req, res, user, uriParser);
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse removeFacility(HttpServletRequest req, HttpServletResponse res, User user,
			URIParser uriParser) throws Exception {

		NodeOwner bean = this.nodeCRUD.getRequestedBean(req, res, user, uriParser, "DELETE");

		this.nodeCRUD.checkAccess(bean, user);

		List<NodeAttribute> attributes = bean.getFacilityNodeAttributes();
		if (attributes != null) {
			this.nodeAttributeDAO.delete(attributes);
			bean.setFacilityNodeAttributes(null);
		}

		List<NodeFileInfo> files = this.getFilesAttachedInfo(bean);
		if (files != null) {
			this.fileInfoDAO.delete(files);
			bean.setFacilityNodeFiles(null);
		}

		ForegroundModuleResponse result = nodeCRUD.delete(req, res, user, uriParser);

		return result;
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse viewFacility(HttpServletRequest req, HttpServletResponse res, User user,
			URIParser uriParser) throws Exception {

		this.nodeCRUD.checkAccess(this.nodeCRUD.getRequestedBean(req, res, user, uriParser, "SHOW"), user);

		return this.nodeCRUD.show(req, res, user, uriParser);
	}

	List<NodeType> getNodeTypes(Integer byType, List<NodeType> nodeTypes) throws SQLException {

		List<NodeType> result = new ArrayList<NodeType>();
		for (NodeType nodeType : nodeTypes) {
			if (nodeType.getRootType().getTypeID().equals(byType)) {
				List<NodeType> childs = this.getNodeTypeChilds(nodeType);
				if (childs == null || childs.isEmpty())
					result.add(nodeType);
			}
		}
		return result;
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse chooseFacilityType(HttpServletRequest req, HttpServletResponse res, User user,
			URIParser uriParser) throws Exception {

		this.nodeCRUD.checkAccess(null, user);

		if (req.getMethod() == "POST") {
			String NodeType = req.getParameter("NodeTypes");
			redirectToMethod(req, res, "/addfacility/" + NodeType);
			return null;
		}

		Document doc = createDocumentElement(req, user, uriParser);
		Element docElement = doc.getDocumentElement();
		Element newElement = doc.createElement("ChooseNodeType");
		docElement.appendChild(newElement);

		List<NodeType> nodeTypes = this.getNodeTypes();
		Integer typeID = 1;
		List<String> l = Arrays.asList(uriParser.getAll());
		Collections.reverse(l);

		try {
			typeID = Integer.parseInt(l.get(0));
		} catch (NumberFormatException e) {
		} catch (NullPointerException e) {
		}

		List<NodeType> facilityTypes = getNodeTypes(typeID, nodeTypes);

		XMLUtils.append(doc, newElement, "NodeTypes", facilityTypes);

		return new SimpleForegroundModuleResponse(doc, this.moduleDescriptor.getName(), this.getDefaultBreadcrumb());
	}

	private Document createDocumentElement(HttpServletRequest req, User user, URIParser uriParser) {

		Document doc = XMLUtils.createDomDocument();
		Element document = doc.createElement("document");
		doc.appendChild(document);
		document.appendChild(RequestUtils.getRequestInfoAsXML(doc, req, uriParser));
		document.appendChild(moduleDescriptor.toXML(doc));
		if (user != null)
			document.appendChild(user.toXML(doc));
		return doc;
	}

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

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse exportCsv(HttpServletRequest req, HttpServletResponse res, User user,
			URIParser uriParser) throws IOException, AccessDeniedException, SQLException {

		String delimiter = ";";
		String lineSeparator = System.getProperty("line.separator");
		List<NodeOwner> nodes = this.getFacilityNodes(new ModuleRequestContext(req, res, user, uriParser));
		String csvString = "Namn";
		List<NodeTemplateAttribute> facilityTemplates = this.getFacilityTemplates(this.getNodeType(1));

		for (NodeTemplateAttribute tAttrib : facilityTemplates) {
			if (tAttrib.getTemplateAttributeID() < 101 || tAttrib.getTemplateAttributeID() > 199)
				csvString += delimiter + tAttrib.getName();
		}

		csvString += lineSeparator;

		List<NodeOwner> allowedNodes = NodeOwnerCRUD.getAllowedNodes(user, nodes);

		for (NodeOwner node : allowedNodes) {

			csvString += String.format("%s", node.getTitle());

			DynamicAttributes.addMissingAttributes(node);
			List<NodeAttribute> facilityNodeAttributes = node.getFacilityNodeAttributes();
			Collections.sort(facilityNodeAttributes);
			for (NodeAttribute attr : facilityNodeAttributes) {
				if (attr.getTemplateID().getTemplateAttributeID() < 101
						|| attr.getTemplateID().getTemplateAttributeID() > 199) {
					csvString += delimiter;

					String value = DynamicAttributes.getDisplayValue(attr);
					value = value.replaceAll("\\r\\n|\\r|\\n", " ");
					if (value.contains(delimiter)) {
						csvString += String.format("\"%s\"", value);
					} else {
						csvString += String.format("%s", value);
					}
				}
			}
			csvString += lineSeparator;
		}

		InputStream inputStream = new ByteArrayInputStream(csvString.getBytes(StandardCharsets.ISO_8859_1));

		HTTPUtils.sendFile(inputStream, "export.csv", "application/csv", "", req, res, ContentDisposition.INLINE);

		return null;
	}

	// Export an empty csv file with only headers
	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse exportemptyCsv(HttpServletRequest req, HttpServletResponse res, User user,
			URIParser uriParser) throws IOException, AccessDeniedException, SQLException {

		String delimiter = ";";
		String lineSeparator = System.getProperty("line.separator");
		String csvString = "Namn";
		List<NodeTemplateAttribute> facilityTemplates = this.getFacilityTemplates(this.getNodeType(1));
		Collections.sort(facilityTemplates);
		for (NodeTemplateAttribute tAttrib : facilityTemplates) {
			if (tAttrib.getTemplateAttributeID() < 101 || tAttrib.getTemplateAttributeID() > 199)
				csvString += delimiter + tAttrib.getName();
		}

		csvString += lineSeparator;

		InputStream inputStream = new ByteArrayInputStream(csvString.getBytes(StandardCharsets.ISO_8859_1));

		HTTPUtils.sendFile(inputStream, "mall.csv", "application/csv", "", req, res, ContentDisposition.INLINE);

		return null;
	}

	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse getTagsJson(HttpServletRequest req, HttpServletResponse res, User user,
			URIParser uriParser) throws AccessDeniedException, SQLException, IOException {

		this.nodeCRUD.checkAccess(null, user);

		StringBuilder stringBuilder = new StringBuilder();

		List<NodeTag> tags = this.nodeTagDAO.getAll();
		JsonArray parametersJsonArray = new JsonArray();

		if (tags != null) {
			for (NodeTag tag : tags) {
				JsonObject jo = new JsonObject();
				jo.putField("value", tag.getFacilityTagID());
				jo.putField("text", tag.getTagName());
				parametersJsonArray.addNode(jo);
			}
		}
		parametersJsonArray.toJson(stringBuilder);
		HTTPUtils.sendReponse(stringBuilder.toString(), "application/json", res);

		return null;
	}

	public Element getTagsXml(Document destinationDoc, HttpServletRequest req, HttpServletResponse res, User user,
			URIParser uriParser) throws Exception {

		ForegroundModuleResponse fgRes = this.nodeTagCRUD.list(req, res, user, uriParser, null);
		Document doc = fgRes.getDocument();

		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		Node node = (Node) xpath.evaluate("/document/ListNodeTags/NodeTags", doc, XPathConstants.NODE);
		Node firstDocImportedNode = destinationDoc.importNode(node, true);
		return (Element) firstDocImportedNode;
	}

	public NodeType getNodeType(Integer id) throws SQLException {

		HighLevelQuery<NodeType> query = new HighLevelQuery<NodeType>();
		query.addParameter(facilityTypeIDParamFactory.getParameter(id));
		NodeType result = nodeTypeDAO.get(query);
		return result;
	}

	public List<NodeType> getNodeTypeChilds(NodeType parentID) throws SQLException {

		HighLevelQuery<NodeType> query = new HighLevelQuery<NodeType>();
		query.addParameter(facilityTypeParentIDParamFactory.getParameter(parentID));
		List<NodeType> result = nodeTypeDAO.getAll(query);
		return result;
	}

	public List<NodeType> getNodeTypes(Integer id) throws SQLException {

		HighLevelQuery<NodeType> query = new HighLevelQuery<NodeType>();
		query.addParameter(facilityTypeIDParamFactory.getParameter(id));
		List<NodeType> result = nodeTypeDAO.getAll(query);
		return result;
	}

	public List<NodeType> getNodeTypes() throws SQLException {

		HighLevelQuery<NodeType> query = new HighLevelQuery<NodeType>();
		List<NodeType> result = nodeTypeDAO.getAll(query);
		return result;
	}

	public NodeTemplateAttribute getFacilityTemplate(Integer id) throws SQLException {

		HighLevelQuery<NodeTemplateAttribute> query = new HighLevelQuery<NodeTemplateAttribute>();
		query.addParameter(facilityTemplateIDParamFactory.getParameter(id));
		NodeTemplateAttribute result = templateAttributeDAO.get(query);
		return result;
	}

	public List<NodeTemplateAttribute> getFacilityTemplates(NodeType parentType) throws SQLException {

		HighLevelQuery<NodeTemplateAttribute> query = new HighLevelQuery<NodeTemplateAttribute>();
		query.addParameter(facilityTemplateFromParentTypeParamFactory.getParameter(parentType));
		List<NodeTemplateAttribute> result = templateAttributeDAO.getAll(query);
		Collections.sort(result);
		return result;
	}

	public NodeTag getTagByName(String name) throws SQLException {

		HighLevelQuery<NodeTag> query = new HighLevelQuery<NodeTag>();
		query.addParameter(facilityTagNameParamFactory.getParameter(name));
		NodeTag result = nodeTagDAO.get(query);
		return result;
	}

	public AnnotatedDAO<NodeOwner> getFacilityNodeDAO() {
		return nodeDAO;
	}

	public void setFacilityNodeDAO(AnnotatedDAO<NodeOwner> facilityNodeDAO) {
		this.nodeDAO = facilityNodeDAO;
	}

	public AnnotatedDAO<NodeType> getNodeTypeDAO() {
		return nodeTypeDAO;
	}

	public void setNodeTypeDAO(AnnotatedDAO<NodeType> NodeTypeDAO) {
		this.nodeTypeDAO = NodeTypeDAO;
	}

	public AnnotatedDAO<NodeAttribute> getFacilityNodeAttributeDAO() {
		return nodeAttributeDAO;
	}

	public void setFacilityNodeAttributeDAO(AnnotatedDAO<NodeAttribute> facilityNodeAttributeDAO) {
		this.nodeAttributeDAO = facilityNodeAttributeDAO;
	}

	public AnnotatedDAO<NodeTag> getFacilityNodeTagDAO() {
		return nodeTagDAO;
	}

	public void setFacilityNodeTagDAO(AnnotatedDAO<NodeTag> facilityNodeTagDAO) {
		this.nodeTagDAO = facilityNodeTagDAO;
	}

	public AnnotatedDAO<NodeTemplateAttribute> getFacilityTemplateAttributeDAO() {
		return templateAttributeDAO;
	}

	public void setFacilityTemplateAttributeDAO(AnnotatedDAO<NodeTemplateAttribute> facilityTemplateAttributeDAO) {
		this.templateAttributeDAO = facilityTemplateAttributeDAO;
	}

	public void removeFile(Integer id) throws SQLException {
		NodeFileInfo f = this.getFileInfo(id);
		if (f != null)
			fileInfoDAO.delete(f);
	}

	public NodeFile getFile(Integer id) throws SQLException {

		HighLevelQuery<NodeFile> query = new HighLevelQuery<NodeFile>();
		query.addParameter(facilitySingleFileIDParamFactory.getParameter(id));
		NodeFile result = fileDAO.get(query);
		return result;
	}

	public NodeFileInfo getFileInfo(Integer id) throws SQLException {

		HighLevelQuery<NodeFileInfo> query = new HighLevelQuery<NodeFileInfo>();
		query.addParameter(facilitySingleFileInfoIDParamFactory.getParameter(id));
		NodeFileInfo result = fileInfoDAO.get(query);
		return result;
	}

	public List<NodeFile> getFilesAttached(NodeOwner node) throws SQLException {

		HighLevelQuery<NodeFile> query = new HighLevelQuery<NodeFile>();
		query.addParameter(nodeFileIDParamFactory.getParameter(node));
		List<NodeFile> result = fileDAO.getAll(query);
		return result;
	}

	public NodeOwner getFacilityNode(Integer id, ModuleRequestContext context)
			throws AccessDeniedException, SQLException {
		NodeOwner bean = nodeCRUD.getBean(id, "GET", context.getReq());

		this.nodeCRUD.checkAccess(bean, context.getUser());

		return bean;
	}

	public List<NodeOwner> getFacilityNodes(ModuleRequestContext context) throws AccessDeniedException, SQLException {
		return NodeOwnerCRUD.getAllowedNodes(context.getUser(), nodeDAO.getAll());
	}

	public List<NodeFileInfo> getFilesAttachedInfo(NodeOwner node) throws SQLException {
		LowLevelQuery<NodeFileInfo> query = new LowLevelQuery<NodeFileInfo>(
				"SELECT facility_file_id,name,date_added,filesize from " + fileDAO.getTableName()
						+ " WHERE parent_node_id = ?");
		query.addParameter(node.getNode_id());
		return fileInfoDAO.getAll(query);
	}
	
	// Export PDF file
	@WebPublic(toLowerCase = true)
	public ForegroundModuleResponse exportPdf(HttpServletRequest req, HttpServletResponse res, User user,
			URIParser uriParser) throws IOException, AccessDeniedException, SQLException, COSVisitorException {

		// Creating PDF document object
		rst.pdfbox.layout.elements.Document doc = new rst.pdfbox.layout.elements.Document(40, 60, 40, 60);

		Paragraph paragraph1 = new Paragraph();
		String facilityId = uriParser.get(2); 
		int facID = Integer.valueOf(facilityId);
		String title = null;

		List<NodeOwner> nodes = this.getFacilityNodes(new ModuleRequestContext(req, res, user, uriParser));

		List<NodeOwner> allowedNodes = NodeOwnerCRUD.getAllowedNodes(user, nodes);

		for (NodeOwner node : allowedNodes) {
			// only get the current row
			if (node.getNode_id() == facID) {

				DynamicAttributes.addMissingAttributes(node);
				List<NodeAttribute> facilityNodeAttributes = node.getFacilityNodeAttributes();
				Collections.sort(facilityNodeAttributes);
				// title of register
				title = node.getTitle();
				paragraph1.addMarkup("\\\\\\\\\\\\*__" + node.getTitle() + "\\\\\\\\\\\\*__\n", 16, BaseFont.Times);
				doc.add(paragraph1);

				for (NodeAttribute attr : facilityNodeAttributes) {

					// each row that is not a subquestion (<templateAttributeID100) and not empty
					if (attr.getTemplateID().getTemplateAttributeID() < 100 && attr.getAttributeID() != null) {

						// Check descriptive text
						String desc = null;
						String value = null;
						if (attr.getTemplateID().getDescription() == null
								|| attr.getTemplateID().getDescription() == "")
							desc = "Saknar beskrivande text";
						else
							desc = attr.getTemplateID().getDescription();

						// heading
						Paragraph paragraph2 = new Paragraph();
						paragraph2.addMarkup("\\\\\\\\\\\\*" + DynamicAttributes.getTemplateName(attr) + "\\\\\\\\\\\\*"
								+ "\n_" + desc + "_\n", 11, BaseFont.Times);
						doc.add(paragraph2);

						Paragraph paragraph3 = new Paragraph();
						// individual rows
						if (DynamicAttributes.getDisplayValue(attr).substring(0).equals("true")) {
							if (attr.getValue() == "" || attr.getValue() == null)
								value = "";
							paragraph3.add(new Indent(20, SpaceUnit.pt));
							paragraph3.addMarkup("Ja" + "\n", 11, BaseFont.Times);

							doc.add(paragraph3);
						} else if (DynamicAttributes.getDisplayValue(attr).substring(0).equals("false")) {
							paragraph3.add(new Indent(20, SpaceUnit.pt));
							paragraph3.addMarkup("Nej" + "\n", 11, BaseFont.Times);
							doc.add(paragraph3);
						} else if (DynamicAttributes.getDisplayValue(attr).substring(0).equals("")) {
							paragraph3.add(new Indent(20, SpaceUnit.pt));
							paragraph3.addMarkup("Uppgift saknas" + "\n", 11, BaseFont.Times);
							doc.add(paragraph3);
						} else {
							paragraph3.add(new Indent(20, SpaceUnit.pt));
							paragraph3.addMarkup(DynamicAttributes.getDisplayValue(attr) + "\n", 11, BaseFont.Times);
							doc.add(paragraph3);
						}

					}
				}
			}
		}
		
		ByteArrayOutputStream out = new ByteArrayOutputStream();

		doc.save(out);

		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		HTTPUtils.sendFile(in, title + ".pdf", "application/pdf", "", req, res, ContentDisposition.ATTACHMENT);
		
		return null;

	}

}