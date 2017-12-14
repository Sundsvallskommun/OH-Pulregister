package se.sundsvallskommun.nodes.beans;

import java.io.Serializable;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "node_geo_location")
@XMLElement(name="geo",skipChildParentElement=true)
public class NodeGeoLocation extends GeneratedElementable implements Serializable {
	
	private static final long serialVersionUID = -6188427465428775655L;

	@DAOManaged(columnName = "geo_id")
	@Key
	@XMLElement	
	private Integer geoID;
	
	@XMLElement
	@DAOManaged(columnName = "GeoToText(geo_data)")	
	private String geoData;
	
	@DAOManaged(columnName = "parent_node")
	@ManyToOne
	private NodeOwner parentNode;
	
	@DAOManaged(columnName = "parent_node_attribute")
	private Integer parentAttributeID;
	
	
	@DAOManaged(columnName = "parent_node")	
	@XMLElement
	private Integer parentNodeID;
	
	
	private NodeAttribute parentAttribute;

	public Integer getGeoID() {
		return geoID;
	}

	public void setGeoID(Integer geoID) {
		this.geoID = geoID;
	}

	public String getGeoData() {
		return geoData;
	}

	public void setGeoData(String geoData) {
		this.geoData = geoData;
	}

	public NodeOwner getParentNode() {
		return parentNode;
	}

	public void setParentNode(NodeOwner parentNode) {
		this.parentNode = parentNode;
	}

	public Integer getParentAttributeID() {
		return parentAttributeID;
	}

	public void setParentAttributeID(Integer parentAttribute) {
		this.parentAttributeID = parentAttribute;
	}

	public NodeAttribute getParentAttribute() {
		return parentAttribute;
	}

	public void setParentAttribute(NodeAttribute parentAttribute) {
		this.parentAttribute = parentAttribute;
	}
	
}