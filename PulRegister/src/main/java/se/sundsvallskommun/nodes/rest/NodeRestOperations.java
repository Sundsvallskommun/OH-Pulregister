package se.sundsvallskommun.nodes.rest;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Collections;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;

import org.apache.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.XML;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

import se.sundsvallskommun.nodes.PulRegistryModule;
import se.sundsvallskommun.nodes.beans.NodeGeoLocation;
import se.sundsvallskommun.nodes.beans.NodeOwner;
import se.sundsvallskommun.nodes.beans.NodeTemplateAttribute;
import se.sundsvallskommun.nodes.beans.NodeType;
import se.sundsvallskommun.nodes.tools.DynamicAttributes;
import se.sundsvallskommun.nodes.tools.ModuleRequestContext;
import se.sundsvallskommun.nodes.tools.XmlToString;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.standardutils.json.JsonArray;
import se.unlogic.standardutils.json.JsonObject;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPUtils;

public class NodeRestOperations {
	
	private PulRegistryModule facilitiesModule;
	protected Logger log = Logger.getLogger(this.getClass());
	Document doc = null;

	public NodeRestOperations(PulRegistryModule facilitiesModule) {

		this.facilitiesModule = facilitiesModule;
		DocumentBuilderFactory docFactory = DocumentBuilderFactory.newInstance();
		DocumentBuilder docBuilder = null;
		try {
			docBuilder = docFactory.newDocumentBuilder();
		} catch (ParserConfigurationException e) {}
		
		doc = docBuilder.newDocument();
	}
	
	public String nodeToJson()
			throws ParserConfigurationException, TransformerException, IOException {
		
		JSONObject xmlJSONObj = XML.toJSONObject(XmlToString.convert(doc));
		JSONObject rootObject = xmlJSONObj.getJSONObject("root");
		doc.removeChild(doc.getFirstChild());		
		return rootObject.toString(4);
	}

	private void returnError( Integer code , String description , ModuleRequestContext context , Exception e  )
	{
		StringBuilder stringBuilder = new StringBuilder();
		JsonArray parametersJsonArray = new JsonArray();
				
		JsonObject jo = new JsonObject();
		jo.putField("error", code.toString() );
		jo.putField("description", description);
		parametersJsonArray.addNode(jo);
		parametersJsonArray.toJson(stringBuilder);
		
		try {
			context.getRes().setStatus(code);
			HTTPUtils.sendReponse(stringBuilder.toString(), "application/json", "UTF-8", context.getRes() );
		} catch (IOException ioException) {
			
			log.error("Could not send response.", ioException);			
		}
		if ( e != null) log.error("Exception: ", e);
	}
	
	public void getNodes( ModuleRequestContext context )
	{
		List<NodeOwner> nodes = null;
		
		try {
			nodes = facilitiesModule.getFacilityNodes(context);
			
			if ( nodes == null )
			{
				this.returnError(404,"Object not found.",context,null);
				return;
			}
			
			for ( NodeOwner n : nodes ) n.setSkipTemplatesInXML(true);
			
			Element rootElement = doc.createElement("root");			
			
			for(NodeOwner xmlable : nodes){
				
				setNodeUrls(context, xmlable);
				DynamicAttributes.addMissingAttributes(xmlable);
				Node xmlNode = xmlable.toXML(doc);
				doc.renameNode(xmlNode, null, "data");
				rootElement.appendChild(xmlNode);
			}
			doc.appendChild(rootElement);
			
		 	String jsonStr;
			jsonStr = nodeToJson();
			HTTPUtils.sendReponse(jsonStr.toString(), "application/json", "UTF-8", context.getRes() );
			
		} catch (ParserConfigurationException | TransformerException | IOException e) {
			this.returnError(400,"Internal server error.",context,e);
		}
		catch (AccessDeniedException e) {
			this.returnError(401,"Access denied.",context,e);
		} 
		catch (SQLException e) {
			this.returnError(404,"Object not found.",context,e);
		}
		finally {
			if ( nodes != null && nodes.size() > 0 ) {
				
				for ( NodeOwner n : nodes ) n.setSkipTemplatesInXML(false);
			}
		}
		
	}

	public void getNode( ModuleRequestContext context , Integer facilityIndex )
	{
		NodeOwner node = null;
		try {

			node = facilitiesModule.getFacilityNode(facilityIndex,context);
			if ( node == null )
			{
				this.returnError(404,"Object not found.",context,null);
				return;
			}
			DynamicAttributes.addMissingAttributes(node);
			
			setNodeUrls(context, node);
			
			node.setSkipTemplatesInXML(true);
			
			Element rootElement = doc.createElement("root");			
			
			Node xmlNode = node.toXML(doc);
			doc.renameNode(xmlNode, null, "data");
			rootElement.appendChild(xmlNode);
	
			doc.appendChild(rootElement);
			
		 	String jsonStr = nodeToJson();
			HTTPUtils.sendReponse(jsonStr.toString(), "application/json", "UTF-8", context.getRes() );
		
		}
		catch (AccessDeniedException e) {
			this.returnError(401,"Access denied.",context,e);
		} 
		catch (SQLException e) {
			this.returnError(404,"Object not found.",context,e);
		}
		catch (IOException|ParserConfigurationException|JSONException|TransformerException e) {
			this.returnError(400,"Internal server error.",context,e);
		}
		finally {
			if ( node != null ) node.setSkipTemplatesInXML(false);
		}
	}

	public void setNodeUrls(ModuleRequestContext context, NodeOwner node) {
		
		node.setEditUrl(context.getUriParser().getFullContextPath()+ "/pulregister/nodes/updatefacility/" + node.getID() );
	}	
	
	public void getTemplateAttributes(ModuleRequestContext context, Integer typeIndex) throws IOException {
		
		NodeType nodeType = null;
		List<NodeTemplateAttribute> templateAttributes = null;
		
		try {
			nodeType = this.facilitiesModule.getNodeType(typeIndex);
			templateAttributes = nodeType.getFacilityTemplateAttributes();
			Collections.sort(templateAttributes);
			
			if ( templateAttributes == null )
			{
				this.returnError(404,"Object not found.",context,null);
				return;
			}
			
			Element rootElement = doc.createElement("root");			
			
			for(NodeTemplateAttribute xmlable : templateAttributes){
				
				Node xmlNode = xmlable.toXML(doc);
				doc.renameNode(xmlNode, null, "data");
				rootElement.appendChild(xmlNode);
			}
			doc.appendChild(rootElement);
			
		 	String jsonStr;
			jsonStr = nodeToJson();
			HTTPUtils.sendReponse(jsonStr.toString(), "application/json", "UTF-8", context.getRes() );
			
		} catch (ParserConfigurationException | TransformerException | IOException e) {
			this.returnError(400,"Internal server error.",context,e);
		}
		catch (SQLException e) {
			this.returnError(404,"Object not found.",context,e);
		}
	}
	
	private Boolean objectExists( Object object,ModuleRequestContext context){
		
		if ( object == null ) {
			
			this.returnError(404,"Object not found.",context,null);
			return false;
		}
		return true;
	}
	
	public void getTemplateAttribute(ModuleRequestContext context, Integer typeIndex, Integer index) throws IOException {
		
		NodeType nodeType = null;
		NodeTemplateAttribute templateAttribute = null;
		List<NodeTemplateAttribute> facilityTemplateAttributes = null;
		try {

			nodeType = this.facilitiesModule.getNodeType(typeIndex);
			
			if ( !objectExists(nodeType,context) ) return;
			
			facilityTemplateAttributes = nodeType.getFacilityTemplateAttributes();
			Collections.sort(facilityTemplateAttributes);
			
			if ( !objectExists(facilityTemplateAttributes,context) ) return;
			
			for ( NodeTemplateAttribute tAttr : facilityTemplateAttributes ){
				
				if ( tAttr.getTemplateAttributeID().equals(index) ) {
					
					templateAttribute = tAttr;
					break;
				}
			}		
			if ( !objectExists(templateAttribute,context) ) return;			
			
			Element rootElement = doc.createElement("root");			
			
			Node xmlNode = templateAttribute.toXML(doc);
			doc.renameNode(xmlNode, null, "data");
			rootElement.appendChild(xmlNode);
	
			doc.appendChild(rootElement);
			
		 	String jsonStr = nodeToJson();
			HTTPUtils.sendReponse(jsonStr.toString(), "application/json", "UTF-8", context.getRes() );
		
		}
		catch (SQLException e) {
			this.returnError(404,"Object not found.",context,e);
		}
		catch (IOException|ParserConfigurationException|JSONException|TransformerException e) {
			this.returnError(400,"Internal server error.",context,e);
		}		
	}

	public void getRegistryType(ModuleRequestContext context, Integer index) throws IOException {
		
		try {
			NodeType nodeType = this.facilitiesModule.getNodeType(index);
			
			nodeType.setSkipTemplatesInXML(true);

			Element rootElement = doc.createElement("root");			
			
			Node xmlNode = nodeType.toXML(doc);
			doc.renameNode(xmlNode, null, "data");
			rootElement.appendChild(xmlNode);
	
			doc.appendChild(rootElement);
			
			nodeType.setSkipTemplatesInXML(false);
			
		 	String jsonStr;
			jsonStr = nodeToJson();
			HTTPUtils.sendReponse(jsonStr.toString(), "application/json", "UTF-8", context.getRes() );
			
		} 
		catch (SQLException e) {
			this.returnError(404,"Object not found.",context,e);
		}
		catch (IOException|ParserConfigurationException|JSONException|TransformerException e) {
			this.returnError(400,"Internal server error.",context,e);
		}	
	}
	
	public void getRegistryTypes(ModuleRequestContext context) throws IOException {
		
		List<NodeType> nodeTypes = null;
		
		try {
			
			nodeTypes = this.facilitiesModule.getNodeTypes();
			
			if ( nodeTypes == null )
			{
				this.returnError(404,"Object not found.",context,null);
				return;
			}
			
			Element rootElement = doc.createElement("root");			
			
			for(NodeType xmlable : nodeTypes){
				
				xmlable.setSkipTemplatesInXML(true);
				Node xmlNode = xmlable.toXML(doc);
				doc.renameNode(xmlNode, null, "data");
				rootElement.appendChild(xmlNode);
				xmlable.setSkipTemplatesInXML(false);
			}
			doc.appendChild(rootElement);
			
		 	String jsonStr;
			jsonStr = nodeToJson();
			HTTPUtils.sendReponse(jsonStr.toString(), "application/json", "UTF-8", context.getRes() );
			
		} catch (ParserConfigurationException | TransformerException | IOException e) {
			this.returnError(400,"Internal server error.",context,e);
		}
		catch (SQLException e) {
			this.returnError(404,"Object not found.",context,e);
		}
	}
	

};