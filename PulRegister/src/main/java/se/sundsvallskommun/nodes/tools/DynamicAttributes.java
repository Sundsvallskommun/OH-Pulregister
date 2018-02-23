package se.sundsvallskommun.nodes.tools;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Base64;
import java.util.Collections;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import se.sundsvallskommun.nodes.beans.NodeAttribute;
import se.sundsvallskommun.nodes.beans.NodeOwner;
import se.sundsvallskommun.nodes.beans.NodeTemplateAttribute;

import org.w3c.dom.Node;


public class DynamicAttributes {
	
	static final String DELIMITER = "|";
	static final String PREFIX = "####";
	static final String DETAILS_ELEMENT_NAME = "AttributeDetails";
	
	
	
	
	static private Node getFirstNodeByName( String name , Element startElement ){
		
		NodeList nodeList = startElement.getElementsByTagName(name);
		Node node = null;
		if ( nodeList.getLength() > 0 ) node = nodeList.item(0);
		return node;
	}
	
	static private Node getNodeByXPath( Element element , String path ){
		
		XPathFactory xpathFactory = XPathFactory.newInstance();
		XPath xpath = xpathFactory.newXPath();
		Node node = null;
		try {
			node = (Node) xpath.evaluate(path, element, XPathConstants.NODE);
		} catch (XPathExpressionException e) {
			return null;
		}
		return (Element) node;
	}
	
	static public void addMissingAttributes(NodeOwner bean) {
		
		List<NodeAttribute> attributes = bean.getFacilityNodeAttributes();		
		List<NodeTemplateAttribute> templateAttributes = bean.getFacilityNodeType().getFacilityTemplateAttributes();
		
		if ( attributes == null ) attributes = new ArrayList<NodeAttribute>();
		
		for ( NodeTemplateAttribute tAttrib : templateAttributes ){
			
			Boolean found = false;
			for ( NodeAttribute attrib : attributes ){
				
				if ( attrib.getTemplateID().getTemplateAttributeID().equals( tAttrib.getTemplateAttributeID() ) ) {
					
					found = true;
					break;
				}
			}
			if ( found.equals(true) ) continue;
			// We have a missing attribute
			NodeAttribute newAttribute = new NodeAttribute();
			newAttribute.setTemplateID(tAttrib);
			newAttribute.setParentNode(bean);
			attributes.add(newAttribute);
			
		}
		Collections.sort(attributes);
		bean.setFacilityNodeAttributes(attributes);
		
	}
	
	static public String getTemplateName(NodeAttribute attribute ) {
		
		if ( attribute == null || attribute.getTemplateID() == null || attribute.getName() == null ) return "";
		return attribute.getName();
	}
	
	static public String getDisplayValue(NodeAttribute attribute ) {
		
		if ( attribute == null || attribute.getTemplateID() == null || attribute.getValue() == null ) return "";
		
		if ( !DynamicAttributes.hasChoices( attribute.getType() ) ) {
			
			return attribute.getValue() == null ? "" : attribute.getValue();
		}
		
		String templateValues = attribute.getTemplateID().getValue();
		String attrValues = attribute.getValue();
		
		List<String> template_multi_values = Arrays.asList( templateValues.split("\\|") );
		List<String> multi_values = Arrays.asList( attrValues.split("\\|") );
		
		String displayValue = "";
		Boolean first = true;
		for ( String val : multi_values ) {
			
			if ( !first ) displayValue += ","; 
			int currentValue = 0;
			try {
				currentValue = Integer.parseInt(val);
			}
			catch ( NumberFormatException e ) {
				continue;
			}
			
			if ( currentValue >= 0 && currentValue < template_multi_values.size() ) {
				
				displayValue += template_multi_values.get(currentValue);
			}
			first = false;
		}
		return displayValue;
	}
	
	
	
	static public Boolean isDynamicAttribute( String str )
	{
		return str.startsWith(PREFIX);
	}
	
	static public String decodeKey(String key) throws UnsupportedEncodingException {
		byte[] asBytes = null;
		
		try {
			asBytes = Base64.getDecoder().decode(key);
		    key = new String(asBytes, "utf-8");		    	
		}
		catch ( IllegalArgumentException e){}
		return key;
	}
	
	static private Boolean hasChoices( String typeStr ) {
		
		return ( typeStr.equals("ENUM") || typeStr.equals("MULTISELECT") || typeStr.equals("SINGLESELECT") );
	}
	
	static private Boolean multiValueMatch(String multiStringValue, Integer intValue){
		
		if ( multiStringValue == null || intValue == null ) return false;
		
		List<String> multi_values = Arrays.asList( multiStringValue.split("\\|") );
		
		for ( String val : multi_values )
		{
			if ( val.length() > 0 && Integer.parseInt(val) == intValue ) {
				
				return true;
			}
		}
		return false;
	}
	
	
	static private Element generateInternal(Element element, String name, String type, Integer id,Integer templateID, Boolean generateTemplate ) throws UnsupportedEncodingException {
		
		String nodeValue = java.text.MessageFormat.format("{0}{4}{1}{4}{2}{4}{3}{4}{5}", PREFIX , name , type , id, DELIMITER , templateID );
		String nodeValueB64 = Base64.getEncoder().encodeToString(nodeValue.getBytes("utf-8"));
		
		Element newElement = element.getOwnerDocument().createElement(DETAILS_ELEMENT_NAME);
		newElement.setTextContent(nodeValueB64);
		element.appendChild(newElement);		
		
		Node typeElement = getFirstNodeByName("type",element);
		Node templateValueElement = getFirstNodeByName("value",element);
		Node nameElement = getFirstNodeByName("name",element);		
		
		String valueStr = null;
		
		List<String> multi_values = null;
		
		if ( !generateTemplate ) {
			
			Node valueElement = getNodeByXPath( element , "./value");
			valueStr = ( valueElement != null ? valueElement.getTextContent() : "" );
			
			multi_values = Arrays.asList( valueStr.split("\\|") );
			
			Node descriptionElement = getFirstNodeByName("description",element);
			Node requiredElement = getFirstNodeByName("required",element);
			
			element.appendChild(typeElement.cloneNode(true));
			element.appendChild(nameElement.cloneNode(true));
			
			if ( descriptionElement != null) element.appendChild(descriptionElement.cloneNode(true));
			if ( requiredElement == null) {
				
				requiredElement = element.getOwnerDocument().createElement("required");
				requiredElement.setTextContent("false");
			}
				
			element.appendChild(requiredElement.cloneNode(true));		
		}
		
		String displayValue = "";
		String typeStr = typeElement.getTextContent();
		
		if ( typeElement != null && templateValueElement != null && DynamicAttributes.hasChoices(typeStr) ) {
			
			String value = ( templateValueElement != null ? templateValueElement.getTextContent() : "");
			List<String> choiceList = Arrays.asList( value.split("\\|") );
			
			newElement = element.getOwnerDocument().createElement("choices");
			Node choicesElement = element.appendChild(newElement);
			
			Integer index = 0;
			
			for ( String choice : choiceList) {
				
				Node choiceElement = element.getOwnerDocument().createElement("choice");
				
				choicesElement.appendChild(choiceElement);
				
				newElement = element.getOwnerDocument().createElement("name");
				newElement.setTextContent(choice);
				
				choiceElement.appendChild(newElement);
				
				newElement = element.getOwnerDocument().createElement("value");
				newElement.setTextContent(index.toString());
				
				choiceElement.appendChild(newElement);
				
				if ( !generateTemplate && multi_values != null && multi_values.size() > 0 ) {
					
					if ( multi_values.contains(index.toString())) {
						
						newElement = element.getOwnerDocument().createElement("selected");		
						choiceElement.appendChild(newElement);
					}
							
				}				
				try
				{
					if ( DynamicAttributes.multiValueMatch( valueStr , index ) ){
						
						displayValue += ( displayValue.length() == 0 ? "" : ", " ) + choice;
					}
				}
				catch ( NumberFormatException e) {}
				index++;
			}
		}	
		if ( displayValue.isEmpty() ) displayValue = valueStr;
		if ( displayValue != null && displayValue.isEmpty() == false ){
			
			newElement = element.getOwnerDocument().createElement("displayvalue");
			newElement.setTextContent(displayValue);					
			element.appendChild(newElement);
		}
		return element;
		
	}
	
	static public Element generateAttributeDetailsXml(Element element, String name, String type, Integer id , Integer templateID ) throws UnsupportedEncodingException {
		
		return DynamicAttributes.generateInternal(element, name, type, id, templateID, false );
		
	}
	
	
	static public Element generateTemplateAttributeDetailsXml(Element element, String name, String type, Integer id , Integer templateID ) throws UnsupportedEncodingException {

		return DynamicAttributes.generateInternal(element, name, type, id, templateID , true );
	}

}
