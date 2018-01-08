package se.sundsvallskommun.nodes.beans;

import java.io.UnsupportedEncodingException;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

import se.sundsvallskommun.nodes.tools.DynamicAttributes;


@Table(name = "node_template_attributes")
@XMLElement
public class NodeTemplateAttribute extends GeneratedElementable  implements Comparable<NodeTemplateAttribute>   {

	@DAOManaged(columnName = "template_attribute_id", autoGenerated = true)
	@Key
	@XMLElement	
	private Integer templateAttributeID;
	
	@DAOManaged(columnName = "parent_type_id")
	@ManyToOne
	@XMLElement
	private NodeType parentNodeType;
	
	@DAOManaged(columnName = "template_attribute_id")
	@OneToMany	
	private List<NodeAttribute> attributes;
	
	@DAOManaged(columnName = "name")
	@WebPopulate(required = true)
	@XMLElement
	private String name;
	
	@DAOManaged(columnName = "value")
	@WebPopulate(required = true)
	@XMLElement
	private String value;
	
	@DAOManaged(columnName = "value_type")
	@WebPopulate(required = true)
	@XMLElement
	private String type;
	
	@DAOManaged(columnName = "description")
	@XMLElement
	private String description;
	
	@DAOManaged(columnName = "regex")
	@XMLElement
	private String regex;
	
	@DAOManaged(columnName = "required")
	@XMLElement
	private Boolean required;
	
	@XMLElement
	private NodeAttribute currentNodeAttribute;
	
	@XMLElement
	@DAOManaged(columnName = "parent_template_attribute")
	private Integer parentTemplateAttribute;
	
	@XMLElement
	@DAOManaged(columnName = "show_only_when_value_equals")
	private String showOnlyWhenValueEquals;
	
	@XMLElement
	private String parentTemplateID;


	@Override
	public Element toXML(Document doc) {
		
		try {
			return DynamicAttributes.generateTemplateAttributeDetailsXml(
					super.toXML(doc),
					this.getName(),
					this.getType(),
					-1,
					this.getTemplateAttributeID());
		} catch (UnsupportedEncodingException e) {
			return null;
		}
	}

	public NodeType getParentNodeType() {
		return parentNodeType;
	}


	public void setParentNodeType(NodeType parentNodeType) {
		this.parentNodeType = parentNodeType;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	public String getType() {
		return type;
	}


	public void setType(String type) {
		this.type = type;
	}

	public Integer getTemplateAttributeID() {
		return templateAttributeID;
	}

	public void setTemplateAttributeID(Integer templateAttributeID) {
		this.templateAttributeID = templateAttributeID;
	}

	public NodeAttribute getCurrentNodeAttribute() {
		return currentNodeAttribute;
	}

	public void setCurrentNodeAttribute(NodeAttribute currentNodeAttribute) {
		this.currentNodeAttribute = currentNodeAttribute;
	}

	public String getParentTemplateID() {
		return parentTemplateID;
	}

	public void setParentTemplateID(String parentTemplateID) {
		this.parentTemplateID = parentTemplateID;
	}

	public Integer getParentTemplateAttribute() {
		return parentTemplateAttribute;
	}

	public void setParentTemplateAttribute(Integer parentTemplateAttribute) {
		this.parentTemplateAttribute = parentTemplateAttribute;
	}

	@Override
	public int compareTo(NodeTemplateAttribute a) {
			
		int lhs_val = this.getTemplateAttributeID();
		int rhs_val = a.getTemplateAttributeID();
		
		int pre_result = lhs_val > rhs_val ? 1 : lhs_val < rhs_val ? -1 : 0;
		
		if ( this.getParentTemplateAttribute() != null ) {
			lhs_val = this.getParentTemplateAttribute() + 1;
		}
		
		if ( a.getParentTemplateAttribute() != null ){
			rhs_val = a.getParentTemplateAttribute() + 1;
		}
		
		int result = lhs_val > rhs_val ? 1 : lhs_val < rhs_val ? -1 : 0;
		
		if ( pre_result != 0 && result == 0 )
			return pre_result;
		
		return result;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	
}
