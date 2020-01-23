package se.sundsvallskommun.nodes.model;

import java.util.ArrayList;
import java.util.List;

/**
 * @author TimoMikkola
 */

import se.unlogic.standardutils.annotations.WebPopulate;
import se.unlogic.standardutils.dao.annotations.DAOManaged;
import se.unlogic.standardutils.dao.annotations.Key;
import se.unlogic.standardutils.dao.annotations.ManyToOne;
import se.unlogic.standardutils.dao.annotations.OneToMany;
import se.unlogic.standardutils.dao.annotations.OneToOne;
import se.unlogic.standardutils.dao.annotations.Table;
import se.unlogic.standardutils.xml.GeneratedElementable;
import se.unlogic.standardutils.xml.XMLElement;

@Table(name = "form_questionnaire_value")
@XMLElement
public class QuestionnaireValue extends GeneratedElementable implements Comparable<QuestionnaireValue> {

	@DAOManaged(columnName = "questionnaire_value_id", autoGenerated = true)
	@Key
	@XMLElement(name = "QuestionnaireValueID")
	private Integer questionnaireValueID;
	
	@DAOManaged(columnName = "question_id")
	@XMLElement(name = "Question")
	@ManyToOne(autoGet = true, autoAdd = false, autoUpdate = false)
	private Question question;
	
	@DAOManaged(columnName = "questionnaire_id")
	@ManyToOne(autoGet = true)
	private Questionnaire questionnaire;
	
	@DAOManaged
	@OneToMany(autoGet = true, autoAdd = true, autoUpdate = true)
	private List<SelectedValue> selectedValues;
	
	@DAOManaged	
	@OneToOne(autoGet = true, autoAdd = true, autoUpdate = true)
	private Note note;
	
	@DAOManaged(columnName = "action_required")
	@WebPopulate(required = true)
	@XMLElement(name = "ActionRequired")
	private Boolean actionRequired;
	
	@DAOManaged
	@OneToOne(autoGet = true, autoAdd = true, autoUpdate = true)
	@XMLElement(name = "File")
	private File file;
		
	public Integer getID() {
		return questionnaireValueID;
	}

	public void setID(Integer questionnaireValueID) {
		this.questionnaireValueID = questionnaireValueID;
	}

	public Question getQuestion() {
		return question;
	}

	public void setQuestion(Question question) {
		this.question = question;
	}

	public Questionnaire getQuestionnaire() {
		return questionnaire;
	}

	public void setQuestionnaire(Questionnaire questionnaire) {
		this.questionnaire = questionnaire;
	}

	public Note getNote() {
		return note;
	}

	public void setNote(Note note) {
		this.note = note;
	}

	public List<SelectedValue> getSelectedValues() {
		return selectedValues;
	}

	public void setSelectedValues(List<SelectedValue> selectedValues) {
		this.selectedValues = selectedValues;
	}

	@Override
	public int compareTo(QuestionnaireValue value) {
		// TODO Auto-generated method stub
		return 0;
	}

	public Boolean getActionRequired() {
		return actionRequired;
	}

	public void setActionRequired(Boolean actionRequired) {
		this.actionRequired = actionRequired;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}	
	
	
	
}