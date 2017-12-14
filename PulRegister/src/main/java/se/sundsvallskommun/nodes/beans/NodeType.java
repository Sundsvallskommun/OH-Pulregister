package se.sundsvallskommun.nodes.beans;

import java.lang.reflect.Field;
import java.util.List;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.reflection.ReflectionUtils;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;


@Table(name = "node_types")
@XMLElement
public class NodeType extends GeneratedElementable {
	
	public static Field FACILITY_NODE_RELATION = ReflectionUtils.getField(NodeOwner.class, "node_id");

	@DAOManaged
	@Key
	@XMLElement
	private Integer type_id;
	
	
	@DAOManaged
	@OneToMany
	@XMLElement
	private List<NodeOwner> nodes;
	
	@DAOManaged
	@WebPopulate(required = true, maxLength = 255)
	@XMLElement
	private String name;
	
	@DAOManaged(columnName="type_description")
	@XMLElement
	private String typeDescription;
		
	@DAOManaged(columnName="parent_type")	
	@ManyToOne(autoGet=true)
	@XMLElement(name="ParentNodeType")
	private NodeType parentType;
	
	@DAOManaged
	@OneToMany	
	private List<NodeType> childTypes;
	
	@DAOManaged
	@OneToMany(autoGet=true)
	@XMLElement(name="NodeTemplateAttributes")	
	private List<NodeTemplateAttribute> nodeTemplateAttributes;	
	
	private Boolean skipTemplatesInXML = false;
	
	
	@Override
	public Element toXML(Document doc) {
		
		if ( skipTemplatesInXML ) {
			List<NodeTemplateAttribute> tAttribs = nodeTemplateAttributes;
			nodeTemplateAttributes = null;
			Element result = super.toXML(doc);
			nodeTemplateAttributes = tAttribs;
			return result;
		}
		return super.toXML(doc);
	}
	
	@Override
	public String toString() {

		return name + " (ID: " + type_id + ")";
	}

	public Integer getTypeID() {

		return type_id;
	}

	public void setTypeID(Integer type_id) {

		this.type_id = type_id;
	}

	public String getName() {

		return name;
	}

	public void setName(String name) {

		this.name = name;
	}

	public List<NodeTemplateAttribute> getFacilityTemplateAttributes() {
		return nodeTemplateAttributes;
	}

	public void setFacilityTemplateAttributes(List<NodeTemplateAttribute> nodeTemplateAttributes) {
		this.nodeTemplateAttributes = nodeTemplateAttributes;
	}

	public List<NodeType> getChildTypes() {
		return childTypes;
	}

	public void setChildTypes(List<NodeType> childTypes) {
		this.childTypes = childTypes;
	}

	public String getTypeDescription() {
		return typeDescription;
	}

	public void setTypeDescription(String typeDescription) {
		this.typeDescription = typeDescription;
	}

	public NodeType getRootType()
	{
		NodeType curType = this;
		NodeType parentType = this.parentType;
		while ( parentType != null )
		{
			curType = parentType;
			parentType = curType.parentType;
		}
		return curType;
	}

	public Boolean getSkipTemplatesInXML() {
		return skipTemplatesInXML;
	}

	public void setSkipTemplatesInXML(Boolean skipTemplatesInXML) {
		this.skipTemplatesInXML = skipTemplatesInXML;
	}

	
}
