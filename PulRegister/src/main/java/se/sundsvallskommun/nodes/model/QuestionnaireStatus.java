/**
 * 
 */
package se.sundsvallskommun.nodes.model;

import java.util.List;

import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

/**
 * @author TimoMikkola
 *
 */
@Table(name = "form_questionnaire_status")
@XMLElement
public class QuestionnaireStatus extends GeneratedElementable {

	@DAOManaged(columnName = "questionnaire_status_id")
	@Key
	@XMLElement(name = "StatusID")
	private Integer questionnaireStatusID;
	
	@DAOManaged(columnName = "status")
	@WebPopulate(required = true)
	@XMLElement(name = "Status")
	private String status;
	
	@DAOManaged
	@OneToMany
	private List<Questionnaire> questionnaires;

	public Integer getID() {
		return questionnaireStatusID;
	}

	public void setID(Integer questionnaireStatusID) {
		this.questionnaireStatusID = questionnaireStatusID;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
	}

	public List<Questionnaire> getQuestionnaires() {
		return questionnaires;
	}

	public void setQuestionnaires(List<Questionnaire> questionnaires) {
		this.questionnaires = questionnaires;
	}
	
	
	
}
