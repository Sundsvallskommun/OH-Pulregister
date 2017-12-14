package se.sundsvallskommun.nodes.beans;

import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "node_attribute_notes")
@XMLElement
public class NodeAttributeNote extends GeneratedElementable {

	
	@Key
	@DAOManaged(autoGenerated=true, columnName="node_attribute_notes_id")
	@XMLElement
	Integer nodeAttributeNoteID;
	
	@DAOManaged(columnName="value")
	@XMLElement
	String value;
	
	@DAOManaged(columnName="node_attribute_parent")
	@ManyToOne	
	NodeAttribute parentAttribute;
	
	
	Integer parentTemplateAttributeID = null;


	public Integer getNodeAttributeNoteID() {
		return nodeAttributeNoteID;
	}


	public void setNodeAttributeNoteID(Integer nodeAttributeNoteID) {
		this.nodeAttributeNoteID = nodeAttributeNoteID;
	}


	public String getValue() {
		return value;
	}


	public void setValue(String value) {
		this.value = value;
	}


	public NodeAttribute getParentAttribute() {
		return parentAttribute;
	}


	public void setParentAttribute(NodeAttribute parentAttribute) {
		this.parentAttribute = parentAttribute;
	}


	public Integer getParentTemplateAttributeID() {
		return parentTemplateAttributeID;
	}


	public void setParentTemplateAttributeID(Integer parentTemplateAttributeID) {
		this.parentTemplateAttributeID = parentTemplateAttributeID;
	}

	
	 
}