package se.sundsvallskommun.nodes.cruds;

import se.sundsvallskommun.nodes.beans.NodeFile;
import se.sundsvallskommun.nodes.beans.NodeFileInfo;
import se.sundsvallskommun.nodes.beans.NodeGeoLocation;
import se.sundsvallskommun.nodes.beans.NodeOwner;
import se.sundsvallskommun.nodes.beans.NodeAttribute;
import se.sundsvallskommun.nodes.beans.NodeAttributeNote;
import se.sundsvallskommun.nodes.beans.NodeTag;
import se.sundsvallskommun.nodes.beans.NodeType;
import se.sundsvallskommun.nodes.beans.NodeTemplateAttribute;
import se.sundsvallskommun.nodes.tools.DynamicAttributes;

import java.io.UnsupportedEncodingException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.rowset.serial.SerialBlob;
import javax.sql.rowset.serial.SerialException;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.sundsvallskommun.nodes.PulRegistryModule;
import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.hierarchy.core.beans.Group;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SystemInterface;
import se.unlogic.hierarchy.core.utils.crud.IntegerBeanIDParser;
import se.unlogic.hierarchy.core.utils.crud.ModularCRUD;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.CRUDDAO;
import se.unlogic.standardutils.numbers.NumberUtils;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationErrorType;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

public class NodeOwnerCRUD extends ModularCRUD<NodeOwner, Integer, User, PulRegistryModule> {
	
	protected Logger log = Logger.getLogger(this.getClass());	
	private SystemInterface systemInterface = null;	
	private static final String REMOVE_FILE_KEY = "remove_file";
	
	public static List<NodeOwner> getAllowedNodes(User user, List<NodeOwner> nodes) {
		
		if ( nodes == null ) nodes = new ArrayList<NodeOwner>();
		
		if ( user == null || user.isAdmin() ) {
			
			Collections.sort(nodes);
			return nodes;
		}
		
		List<NodeOwner> allowedList = new ArrayList<NodeOwner>();
		for ( NodeOwner node : nodes ) {
			
			Collection<Group> groups = user.getGroups();
			if ( groups != null ) {
				for ( Group g : groups ) {
					if ( g.getGroupID().equals( node.getGroupOwnerID() ) ) {
						allowedList.add(node);
					}
				}
			}
		}
		Collections.sort(allowedList);
		return allowedList;
	}

	public NodeOwnerCRUD(CRUDDAO<NodeOwner, Integer> crudDAO, PulRegistryModule callback, SystemInterface systemInterface ) {

		super(IntegerBeanIDParser.getInstance(), crudDAO,
				new AnnotatedRequestPopulator<NodeOwner>(NodeOwner.class), "Node", "node", "/showflatlist",
				callback);
		
		this.systemInterface = systemInterface;
	}

	@Override
	protected void checkAddAccess(User user, HttpServletRequest req, URIParser uriParser)
			throws AccessDeniedException, URINotFoundException, SQLException {

		checkAccess(null,user);
		super.checkAddAccess(user, req, uriParser);
	}
	
	@Override
	protected void checkDeleteAccess(NodeOwner bean, User user, HttpServletRequest req, URIParser uriParser)
			throws AccessDeniedException, URINotFoundException, SQLException {

		checkAccess(bean,user);
		super.checkDeleteAccess(bean, user, req, uriParser);
	}
	
	@Override
	protected void checkListAccess(User user, HttpServletRequest req, URIParser uriParser)
			throws AccessDeniedException, URINotFoundException, SQLException {
		
		checkAccess(null,user);
		super.checkListAccess(user, req, uriParser);
	}
	
	@Override
	protected void appendUpdateFormData(NodeOwner bean, Document doc, Element updateTypeElement, User user,
			HttpServletRequest req, URIParser uriParser) throws Exception {
		
		super.appendUpdateFormData(bean, doc, updateTypeElement, user, req, uriParser);
	}

	public void checkAccess( NodeOwner node , User user ) throws AccessDeniedException {
		
		if (user == null) {
			throw new AccessDeniedException("Access denied. User not logged in");
		}
		
		if ( user.isAdmin() ) return;
		
		if ( node == null || node.getGroupOwnerID() == null ) return;
		
		Collection<Group> groups = user.getGroups();
		if ( groups != null ) { 
			for ( Group g : groups ) {
				if ( g.getGroupID().equals( node.getGroupOwnerID() ) ) {
					return;
				}
			}
		}
		throw new AccessDeniedException("Access denied.");
	}

	@Override
	public NodeOwner getRequestedBean(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser,
			String getMode) throws SQLException, AccessDeniedException {
		
		NodeOwner node =  super.getRequestedBean(req, res, user, uriParser, getMode);
		
		checkAccess(node,user);
		
		return node;
	}

	@Override
	protected void appendAddFormData(Document doc, Element addTypeElement, User user, HttpServletRequest req,
			URIParser uriParser) throws Exception {
		
		List<String> l = Arrays.asList(uriParser.getAll());
		Collections.reverse(l);
		Integer typeID = Integer.parseInt(l.get(0));

		XMLUtils.append(doc, addTypeElement, "NodeTypes", callback.getNodeTypes(typeID));
	}
	
	@Override
	protected void appendBean(NodeOwner bean, Element targetElement, Document doc, User user) {
		
		try {			
			bean.setFacilityGeoLocations( callback.getGeoData(bean) );		
			bean.setFacilityNodeFiles( callback.getFilesAttached(bean) );
			DynamicAttributes.addMissingAttributes(bean);
		} catch (SQLException e) {
			log.error("Could not append bean.", e);
		}
		
		super.appendBean(bean, targetElement, doc, user);
	};

	protected void appendAllBeans(Document doc, Element listTypeElement, User user, HttpServletRequest req, URIParser uriParser, List<ValidationError> validationError) throws SQLException {

		
		super.appendAllBeans(doc, listTypeElement, user, req, uriParser, validationError);
	}
	
	@Override
	protected List<NodeOwner> getAllBeans(User user) throws SQLException {
		
		if ( user == null ) return null;

		List<NodeOwner> nodes = super.getAllBeans(user);
		if ( nodes != null )
		{
			for( NodeOwner node : nodes)
			{
				List<NodeFile> fList = new ArrayList<NodeFile>(); 
				List<NodeFileInfo> fiList = callback.getFilesAttachedInfo(node);
				if ( fiList != null && fiList.size() > 0 )
				{
					for ( NodeFileInfo fi : fiList )
					{
						NodeFile ff = new NodeFile();
						ff.setDateAdded(fi.getDateAdded());
						ff.setFacilityFileID(fi.getFacilityFileID());
						ff.setFileName(fi.getFileName());
						ff.setFileSize(fi.getFileSize());					
						fList.add(ff);
					}
					node.setFacilityNodeFiles(fList);
				}
			}
		}
		
		return NodeOwnerCRUD.getAllowedNodes(user, nodes);
	}
	

	@Override
	protected HttpServletRequest parseRequest(HttpServletRequest req, User user) throws ValidationException, Exception {

		if (MultipartRequest.isMultipartRequest(req) == false)
			return super.parseRequest(req, user);

		MultipartRequest requestWrapper = null;
		ValidationException validationException = null;

		try {
			requestWrapper = new MultipartRequest(1000, 1024 * 1024 * 100, req);

		} catch (FileSizeLimitExceededException e) {
			validationException = new ValidationException(new ValidationError("FileSizeLimitExceeded"));
		} catch (FileUploadException e) {
			validationException = new ValidationException(new ValidationError("UnableToParseRequest"));
		}

		if (requestWrapper != null)
			return super.parseRequest(requestWrapper, user);
		else
			return super.parseRequest(req, user);
	}

	
	@Override
	protected NodeOwner populateFromAddRequest(HttpServletRequest req, User user, URIParser uriParser)
			throws ValidationException, Exception {

		return super.populateFromAddRequest(req, user, uriParser);
	}

	@Override
	protected void validateUpdatePopulation(NodeOwner bean, HttpServletRequest req, User user, URIParser uriParser)
			throws ValidationException, SQLException, Exception {
		
		this.checkAccess( bean , user);

		Integer typeID = NumberUtils.toInt(req.getParameter("type"));
		NodeType facilityType = null;

		if (typeID == null || (facilityType = callback.getNodeType(typeID)) == null) {
			throw new ValidationException(new ValidationError("category", ValidationErrorType.RequiredField));
		}

		bean.setFacilityNodeType(facilityType);

		List<NodeGeoLocation> geoLocations = bean.getFacilityGeoLocations();
		if ( geoLocations == null ) geoLocations = new ArrayList<NodeGeoLocation>();
		Map<String, String[]> params = req.getParameterMap();
		List<NodeAttributeNote> notes = new ArrayList<NodeAttributeNote>();
		
		for (Map.Entry<String, String[]> entry : params.entrySet()) {
			
			NodeAttribute attr = new NodeAttribute();
			String key = entry.getKey();
			String[] value = entry.getValue();
			
			if ( value.length == 0  || ( value.length == 1 && value[0].equals("") ) ) continue;
			
			Integer noteTemplateID = getNoteParentTemplateID(bean,key,value);
			Integer noteID = getNoteParentID(bean,key,value);
			
			if ( noteTemplateID != null || noteID != null ){
				
				NodeAttributeNote note = new NodeAttributeNote();
				note.setValue(value[0]);				
				note.setNodeAttributeNoteID(noteID);
				note.setParentTemplateAttributeID(noteTemplateID);
				notes.add(note);
				continue;
			}
			
			handleFileRemoval( bean , key , value );
		
			key = DynamicAttributes.decodeKey(key);

			if (DynamicAttributes.isDynamicAttribute(key)) {
				
				List<String> parts = new ArrayList<String>(Arrays.asList(key.split("\\|")));
				try {
					Integer attrID = Integer.parseInt(parts.get(3));
					if (attrID != null) {
						attr = bean.getAttributeFromID(attrID);
						setAttributeValue(attr, value);
					}
				} catch (NumberFormatException e) {
					
					attr.setAttributeID(null);
					attr.setParentNode(bean);
					
					Integer templateID = Integer.parseInt(parts.get(4));
					NodeTemplateAttribute templateAttrib = callback.getFacilityTemplate(templateID);
					attr.setTemplateID(templateAttrib);
					List<NodeAttribute> attrList = bean.getFacilityNodeAttributes();
					if ( attrList == null ) attrList = new ArrayList<NodeAttribute>();
					attrList.add(attr);
					bean.setFacilityNodeAttributes(attrList);
				}
				handleNewTags(bean, attr);
				handleGeoData(bean, attr , geoLocations);
				handleNewUploadedFiles(bean, req, attr);
				setAttributeValue(attr, value);
				handleGroupOwner(bean, attr, user);				
				attr.setHandled(true);
			}
			
		}
		bean.setFacilityGeoLocations(geoLocations);
		if ( bean.getFacilityNodeAttributes() != null )
		{
			for ( NodeAttribute a : bean.getFacilityNodeAttributes() ){			
				if ( a.getHandled().equals(false)){
					a.setValue(null);
				}
			}
		}
		handleNewNotes(bean, notes);
		
	}
	
	private Integer getNoteParentInternal(NodeOwner bean, String key, String[] value, Boolean getTemplateID ) throws SQLException {
		
		List<String> parts = new ArrayList<String>(Arrays.asList(key.split("\\|")));
		String dkey = "";
		try {
			dkey = DynamicAttributes.decodeKey(parts.get(0));
		} catch (UnsupportedEncodingException e) {}		
		if ( parts.size() == 2 && DynamicAttributes.isDynamicAttribute( dkey ) ){
			
			List<String> dparts = new ArrayList<String>(Arrays.asList(dkey.split("\\|")));
			Integer noteTemplateID = null;
			Integer noteID = null;
			
			try {
				noteID = Integer.parseInt(dparts.get(3));
			}
			catch(NumberFormatException e){}
			try {
				noteTemplateID = Integer.parseInt(dparts.get(4));
			}
			catch(NumberFormatException e){}
							
			return getTemplateID.equals(true) ? noteTemplateID : noteID;
		}
		
		return null;
	}
	
	private Integer getNoteParentID(NodeOwner bean, String key, String[] value) throws SQLException{
		
		return getNoteParentInternal(bean,key,value,false);
	}
	
	private Integer getNoteParentTemplateID(NodeOwner bean, String key, String[] value) throws SQLException{
		
		return getNoteParentInternal(bean,key,value,true);
	}

	protected void setAttributeValue(NodeAttribute attr, String[] value) {
		
		if ( value.length > 1 ) {
			
			String joinedValue = String.join("|", value);
			attr.setValue(joinedValue);
		}
		else {
			
			attr.setValue(value[0]);
		}
	}
	
	protected void handleFileRemoval( NodeOwner bean , String key , String[] value ) throws NumberFormatException, SQLException
	{
		if ( key.equals(REMOVE_FILE_KEY) && value.length > 0 )
		{
			for ( String id : value )
			{
				callback.removeFile( Integer.parseInt(id));
			}
		}
	}

	@Override
	protected void validateAddPopulation(NodeOwner bean, HttpServletRequest req, User user, URIParser uriParser)
			throws ValidationException, SQLException, Exception {
		
		this.checkAccess( bean , user);

		Integer typeID = NumberUtils.toInt(req.getParameter("type"));
		NodeType facilityType = null;

		if (typeID == null || (facilityType = callback.getNodeType(typeID)) == null) {
			throw new ValidationException(new ValidationError("category", ValidationErrorType.RequiredField));
		}

		bean.setFacilityNodeType(facilityType);

		List<NodeAttribute> attrList = new ArrayList<NodeAttribute>();
		Map<String, String[]> params = req.getParameterMap();
		List<NodeGeoLocation> geoLocations = new ArrayList<NodeGeoLocation>();
		List<NodeAttributeNote> notes = new ArrayList<NodeAttributeNote>();

		for (Map.Entry<String, String[]> entry : params.entrySet()) {
			String key = entry.getKey();
			String[] value = entry.getValue();
			
			if ( value.length == 0  || ( value.length == 1 && value[0].equals("") ) ) continue;
			
			
			Integer noteTemplateID = getNoteParentTemplateID(bean,key,value);
			
			if ( noteTemplateID != null ){
				
				NodeAttributeNote note = new NodeAttributeNote();
				note.setValue(value[0]);				
				note.setParentTemplateAttributeID(noteTemplateID);
				notes.add(note);
				continue;
			}

			NodeAttribute attr = new NodeAttribute();

			key = DynamicAttributes.decodeKey(key);

			if (DynamicAttributes.isDynamicAttribute(key)) {
				List<String> parts = new ArrayList<String>(Arrays.asList(key.split("\\|")));
				Integer counter = 0;
				for (String p : parts) {
					counter++;					
					if (counter == 5) {
						Integer templateID = Integer.parseInt(p);
						NodeTemplateAttribute templateAttrib = callback.getFacilityTemplate(templateID);
						attr.setTemplateID(templateAttrib);
					}
				}
				attr.setParentNode(bean);
				setAttributeValue(attr, value);

				handleNewTags(bean, attr);
				handleGeoData(bean, attr, geoLocations);
				handleNewUploadedFiles(bean, req, attr);
				handleGroupOwner(bean, attr, user);
				attrList.add(attr);
			}
		}
		bean.setFacilityGeoLocations(geoLocations);
		bean.setFacilityNodeAttributes(attrList);		
		handleNewNotes(bean, notes);
			
	}


	public void handleNewNotes(NodeOwner bean, List<NodeAttributeNote> notes) throws SQLException {
		List<NodeAttribute> attributes = bean.getFacilityNodeAttributes();
		for ( NodeAttributeNote note : notes ) {
			
			Boolean found = false;
			for ( NodeAttribute attr : attributes ){
				if ( attr.getTemplateID() != null && attr.getTemplateID().getTemplateAttributeID().equals(note.getParentTemplateAttributeID())){
					
					note.setParentAttribute(attr);
					attr.setNote(note);
					found = true;
					break;
				}
			}
			if ( !found ){
					
				NodeAttribute attr = new NodeAttribute();
				
				NodeTemplateAttribute templateAttrib = callback.getFacilityTemplate(note.getParentTemplateAttributeID());
				attr.setTemplateID(templateAttrib);
				attr.setNote(note);
				attr.setValue("");
				attr.setParentNode(bean);	
				
				note.setParentAttribute(attr);
				attributes.add(attr);
			}
		}
	}

	public void handleNewUploadedFiles(NodeOwner bean, HttpServletRequest req, NodeAttribute attr)
			throws SerialException, SQLException {
		
		if (req instanceof MultipartRequest && (attr.getType().equals("FILES") || attr.getType().equals("FILE"))) {
			MultipartRequest reqWrapper = (MultipartRequest) req;
			if (reqWrapper.getFileCount() > 0) {
				bean.setFacilityNodeFiles( callback.getFilesAttached(bean) );
				List<FileItem> files = reqWrapper.getFiles();
				List<NodeFile> fileList = bean.getFacilityNodeFiles();
				if ( fileList == null ) fileList = new ArrayList<NodeFile>();
				for (FileItem file : files) {
					long sz = file.getSize();
					if (file.getName() != null && sz > 0L) {
						NodeFile f = null;
						Boolean fileExists = false;
						Path p = Paths.get(file.getName());
						String filename = p.getFileName().toString();
						for ( NodeFile existingFile : fileList )
						{
							if ( existingFile.getFileName().equals( filename ) )
							{
								fileExists = true;
								f = existingFile;
								break;
							}
						}
						if ( f == null ) f = new NodeFile();
						f.setFileName(filename);
						f.setFileSize(sz);
						f.setFileData(new SerialBlob(file.get()));

						f.setDateAdded(TimeUtils.getCurrentTimestamp());
						f.setParentNode(bean);
						// TODO: We have to store the attributes first to get
						// the ID from db
						f.setParentNodeAttribute(attr.getAttributeID());
						if ( fileExists == false )
						{
							fileList.add(f);
						}
					}
				}
				bean.setFacilityNodeFiles(fileList);
				reqWrapper.deleteFiles();
			}
		}
	}

	public void handleNewTags(NodeOwner bean, NodeAttribute attr) throws SQLException {
		
		if (attr.getType().equals("TAGS")) {
			AnnotatedDAO<NodeTag> dao = callback.getFacilityNodeTagDAO();
			List<String> tags = Arrays.asList(attr.getValue().split(","));
			List<NodeTag> newTags = new ArrayList<NodeTag>();
			for (String tag : tags) {
				if (tag.isEmpty())
					continue;
				NodeTag nodeTag = callback.getTagByName(tag);
				if (nodeTag == null) {
					nodeTag = new NodeTag();
					nodeTag.setTagName(tag);
					dao.add(nodeTag);
				}
				newTags.add(nodeTag);				
			}
			bean.setFacilityNodeTags(newTags);
		}
	}
	
	
	@Override
	protected NodeOwner populateFromUpdateRequest(NodeOwner bean, HttpServletRequest req, User user,
			URIParser uriParser) throws ValidationException, Exception {
		
		this.checkAccess( bean , user);
		
		NodeOwner node = super.populateFromUpdateRequest(bean, req, user, uriParser);
		node.setFacilityGeoLocations( callback.getGeoData(node) );
		return node;
	}
	
	
	public void handleGroupOwner(NodeOwner bean, NodeAttribute attr, User user ){
		
		if ( attr.getTemplateID().getTemplateAttributeID().equals(2) )
		{
			Collection<Group> groups = this.systemInterface.getGroupHandler().getGroups(false);
			
			if ( groups == null ) return;
			
			for ( Group g : groups ) {
				if ( g.getName().equals( DynamicAttributes.getDisplayValue(attr) ) ) {
					bean.setGroupOwnerID(g.getGroupID());
					break;
				}
			}
		}
	}
	
	public void handleGeoData(NodeOwner bean, NodeAttribute attr, List<NodeGeoLocation> geoLocations ) throws SQLException, UnsupportedEncodingException {
		
		if ( attr.getType().equals("GEO-AREA") || attr.getType().equals("GEO-POINT") )
		{
			String attrValB64 = attr.getValue();
			String attrVal = DynamicAttributes.decodeKey(attrValB64);
			
			if ( attrVal.length() < 1 ) return;
			
			NodeGeoLocation geo = new NodeGeoLocation();
			
			for( NodeGeoLocation i : geoLocations )
			{
				if ( i.getParentAttributeID().equals( attr.getAttributeID() ) )
				{
					geo = i;
					break;
				}
			}
			
			geo.setGeoData(attrVal);
			geo.setParentNode(bean);
			geo.setParentAttributeID(attr.getAttributeID());
			geo.setParentAttribute(attr);
			geoLocations.add(geo);
		}
	}
	
	@Override
	protected ForegroundModuleResponse beanAdded(NodeOwner bean, HttpServletRequest req, HttpServletResponse res,
			User user, URIParser uriParser) throws Exception {
		
		this.checkAccess( bean , user);
		
		for ( NodeGeoLocation geo : bean.getFacilityGeoLocations() )
		{
			geo.setParentAttributeID(geo.getParentAttribute().getAttributeID());
			geo.setParentNode(bean);
			callback.addGeoData(geo);
		}
		
		return super.beanAdded(bean, req, res, user, uriParser);
	}
	
	@Override
	protected ForegroundModuleResponse beanUpdated(NodeOwner bean, HttpServletRequest req, HttpServletResponse res,
			User user, URIParser uriParser) throws Exception {
		
		this.checkAccess( bean , user);
		
		for ( NodeGeoLocation geo : bean.getFacilityGeoLocations() )
		{
			geo.setParentAttributeID(geo.getParentAttribute().getAttributeID());
			geo.setParentNode(bean);
			callback.updateGeoData(geo);
		}
		return super.beanUpdated(bean, req, res, user, uriParser);
	}	

}
