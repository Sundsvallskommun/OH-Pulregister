package se.sundsvallskommun.nodes.cruds;

import se.sundsvallskommun.nodes.beans.NodeAttribute;

import javax.servlet.http.HttpServletRequest;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.sundsvallskommun.nodes.PulRegistryModule;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.utils.crud.IntegerBeanIDParser;
import se.unlogic.hierarchy.core.utils.crud.ModularCRUD;
import se.unlogic.standardutils.dao.CRUDDAO;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

public class NodeAttributeCRUD extends ModularCRUD<NodeAttribute, Integer, User, PulRegistryModule>{

	public NodeAttributeCRUD(CRUDDAO<NodeAttribute, Integer> crudDAO, PulRegistryModule callback) {

		super(IntegerBeanIDParser.getInstance(), crudDAO, new AnnotatedRequestPopulator<NodeAttribute>(NodeAttribute.class), "NodeAttribute", "nodeAtribute", "/", callback);
	}

	

	@Override
	protected void appendAddFormData(Document doc, Element addTypeElement, User user, HttpServletRequest req,
			URIParser uriParser) throws Exception {
		
		
		
	}
	
	
	

}

