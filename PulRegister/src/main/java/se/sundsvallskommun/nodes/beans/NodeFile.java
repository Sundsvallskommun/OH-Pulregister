package se.sundsvallskommun.nodes.beans;

import java.io.Serializable;
import java.sql.Blob;
import java.sql.Timestamp;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "node_files")
@XMLElement
public class NodeFile extends GeneratedElementable implements Serializable {
	
	private static final long serialVersionUID = -6188427465428775655L;

	@DAOManaged(columnName = "facility_file_id", autoGenerated = true)
	@Key
	@XMLElement
	private Integer nodeFileID;
	
	@DAOManaged(columnName = "parent_node_attribute")
	private Integer parentNodeAttribute;

	@DAOManaged(columnName = "data",dontUpdateIfNull=true)
	private Blob fileData;
	
	
	@DAOManaged(columnName = "name")
	@XMLElement
	private String fileName;
	
	
	@DAOManaged(columnName = "filesize")
	@XMLElement
	private Long fileSize;
	
	@DAOManaged(columnName = "date_added")
	@XMLElement
	private Timestamp dateAdded;	

	@DAOManaged(columnName = "parent_node_id")
	@ManyToOne
	private NodeOwner parentNode;	

	
	public Integer getFacilityFileID() {
		return nodeFileID;
	}

	public void setFacilityFileID(Integer nodeFileID) {
		this.nodeFileID = nodeFileID;
	}

	public Blob getFileData() {
		return fileData;
	}

	public void setFileData(Blob fileData) {
		this.fileData = fileData;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	
	public NodeOwner getParentNode() {
		return parentNode;
	}

	public void setParentNode(NodeOwner parentNode) {
		this.parentNode = parentNode;
	}
	
	public Long getFileSize() {
		return fileSize;
	}

	public void setFileSize(Long fileSize) {
		this.fileSize = fileSize;
	}

	public Timestamp getDateAdded() {
		return dateAdded;
	}

	public void setDateAdded(Timestamp dateAdded) {
		this.dateAdded = dateAdded;
	}

	public Integer getParentNodeAttribute() {
		return parentNodeAttribute;
	}

	public void setParentNodeAttribute(Integer parentNodeAttribute) {
		this.parentNodeAttribute = parentNodeAttribute;
	}

	
	
}