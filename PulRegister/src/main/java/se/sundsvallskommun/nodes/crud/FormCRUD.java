package se.sundsvallskommun.nodes.crud;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileUploadBase.FileSizeLimitExceededException;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.log4j.Logger;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import se.sundsvallskommun.nodes.PulRegistryModule;
import se.sundsvallskommun.nodes.controller.FormController;
import se.sundsvallskommun.nodes.dao.FileDAO;
import se.sundsvallskommun.nodes.model.File;
import se.sundsvallskommun.nodes.model.Form;
import se.sundsvallskommun.nodes.model.FormType;
import se.sundsvallskommun.nodes.model.Question;
import se.sundsvallskommun.nodes.model.QuestionOption;
import se.sundsvallskommun.nodes.model.Questionnaire;
import se.sundsvallskommun.nodes.model.QuestionnaireStatus;
import se.sundsvallskommun.nodes.model.QuestionnaireValue;
import se.sundsvallskommun.nodes.model.SelectedValue;
import se.sundsvallskommun.nodes.tools.Mapper;
import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.hierarchy.core.beans.Group;
import se.unlogic.hierarchy.core.beans.SimpleForegroundModuleResponse;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.exceptions.URINotFoundException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SystemInterface;
import se.unlogic.hierarchy.core.utils.crud.IntegerBeanIDParser;
import se.unlogic.hierarchy.core.utils.crud.ModularCRUD;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.CRUDDAO;
import se.unlogic.standardutils.dao.HighLevelQuery;
import se.unlogic.standardutils.dao.LowLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.validation.ValidationError;
import se.unlogic.standardutils.validation.ValidationException;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.RequestUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.populators.annotated.AnnotatedRequestPopulator;

public class FormCRUD extends ModularCRUD<Form, Integer, User, PulRegistryModule> {

	protected Logger log = Logger.getLogger(this.getClass());
	private SystemInterface systemInterface = null;
	private static final String REMOVE_FILE_KEY = "remove_file";

	private FormController formController;
	private AnnotatedDAO<Form> formDAO;
	private AnnotatedDAO<FormType> formTypeDAO;
	private AnnotatedDAO<QuestionnaireStatus> questionnaireStatusDAO;
	private AnnotatedDAO<Question> questionDAO;
	private AnnotatedDAO<QuestionOption> questionOptionDAO;
	private AnnotatedDAO<Questionnaire> questionnaireDAO;
	private AnnotatedDAO<QuestionnaireValue> questionnaireValueDAO;
	private AnnotatedDAO<SelectedValue> selectedValueDAO;
	private AnnotatedDAO<File> fileDAO;

	private QueryParameterFactory<FormType, Integer> formTypeIDParamFactory;
	private QueryParameterFactory<QuestionnaireStatus, Integer> questionnaireStatusIDParamFactory;
	private QueryParameterFactory<QuestionnaireValue, Integer> questionnaireValueIDParamFactory;

	public FormCRUD(CRUDDAO<Form, Integer> crudDAO, PulRegistryModule callback, FormController formController,
			SystemInterface systemInterface) {

		super(IntegerBeanIDParser.getInstance(), crudDAO, new AnnotatedRequestPopulator<Form>(Form.class), "Form",
				"form", "/formlist", callback);

		this.formController = formController;
		this.systemInterface = systemInterface;

	}

	public void setDaos(AnnotatedDAO<Form> formDAO, AnnotatedDAO<FormType> formTypeDAO,
			AnnotatedDAO<QuestionnaireStatus> questionnaireStatusDAO, AnnotatedDAO<Question> questionDAO,
			AnnotatedDAO<QuestionOption> questionValueDAO, AnnotatedDAO<Questionnaire> questionnaireDAO,
			AnnotatedDAO<QuestionnaireValue> questionnaireValueDAO, AnnotatedDAO<SelectedValue> selectedValueDAO,
			AnnotatedDAO<File> fileDAO, QueryParameterFactory<FormType, Integer> formTypeIDParamFactory) {

		this.formDAO = formDAO;
		this.formTypeDAO = formTypeDAO;
		this.questionnaireStatusDAO = questionnaireStatusDAO;
		this.questionDAO = questionDAO;
		this.questionnaireValueDAO = questionnaireValueDAO;
		this.questionnaireDAO = questionnaireDAO;
		this.questionOptionDAO = questionValueDAO;
		this.selectedValueDAO = selectedValueDAO;
		this.fileDAO = fileDAO;
		this.formTypeIDParamFactory = formTypeIDParamFactory;
	}

	/**
	 * Returns a list containing forms depending on user restrictions
	 * 
	 * @param user
	 * @param forms
	 * @return
	 */
	public List<Form> getAllowedForms(User user, List<Form> forms) {

		if (forms == null)
			forms = new ArrayList<Form>();

		if (user == null || user.isAdmin()) {
			Collections.sort(forms);
			return forms;

		}

		/**
		 * If user != administrator
		 */
		List<Form> allowedForms = new ArrayList<>();
		for (Form form : forms) {
			Collection<Group> groups = user.getGroups();

			if (groups != null) {
				for (Group group : groups) {
					if (group.getGroupID().equals(form.getGroupID())) {
						allowedForms.add(form);

					}

				}

			}

		}

		Collections.sort(allowedForms);
		return allowedForms;

	}

	@Override
	protected void checkAddAccess(User user, HttpServletRequest req, URIParser uriParser)
			throws AccessDeniedException, URINotFoundException, SQLException {

		checkAccess(null, user);
		super.checkAddAccess(user, req, uriParser);

	}

	@Override
	protected void checkDeleteAccess(Form form, User user, HttpServletRequest req, URIParser uriParser)
			throws AccessDeniedException, URINotFoundException, SQLException {

		checkAccess(form, user);
		super.checkDeleteAccess(form, user, req, uriParser);

	}

	@Override
	protected void checkListAccess(User user, HttpServletRequest req, URIParser uriParser)
			throws AccessDeniedException, URINotFoundException, SQLException {

		checkAccess(null, user);
		super.checkListAccess(user, req, uriParser);

	}

	@Override
	protected Form populateFromAddRequest(HttpServletRequest req, User user, URIParser uriParser)
			throws ValidationException, Exception {

		return super.populateFromAddRequest(req, user, uriParser);

	}

	/**
	 * Checks if user has access to form
	 * 
	 * @param form
	 * @param user
	 * @throws AccessDeniedException
	 */
	public void checkAccess(Form form, User user) throws AccessDeniedException {

		if (user == null) {
			throw new AccessDeniedException("Access denied. User not logged in!");

		}

		if (user.isAdmin()) {
			return;

		}

		if (form == null || form.getGroupID() == null) {
			return;

		}

		Collection<Group> groups = user.getGroups();
		if (groups != null) {
			for (Group group : groups) {
				if (group.getGroupID().equals(form.getGroupID())) {
					return;

				}

			}

		}

		throw new AccessDeniedException("Access denied!");

	}

	@Override
	public Form getRequestedBean(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser,
			String getMode) throws SQLException, AccessDeniedException {

		Form form = super.getRequestedBean(req, res, user, uriParser, getMode);
		checkAccess(form, user);

		return form;

	}

	@Override
	protected HttpServletRequest parseRequest(HttpServletRequest req, User user) throws ValidationException, Exception {

		if (MultipartRequest.isMultipartRequest(req) == false) {
			return super.parseRequest(req, user);

		}

		MultipartRequest requestWrapper = null;
		ValidationException validationException = null;

		try {
			requestWrapper = new MultipartRequest(1000, 1024 * 1024 * 100, req);

		} catch (FileSizeLimitExceededException e) {
			validationException = new ValidationException(new ValidationError("FileSizeLimitExceeded"));
		} catch (FileUploadException e) {
			validationException = new ValidationException(new ValidationError("UnableToParseRequest"));
		}

		if (requestWrapper != null) {
			return super.parseRequest(requestWrapper, user);

		} else {
			return super.parseRequest(req, user);

		}

	}

	@Override
	protected ForegroundModuleResponse beanAdded(Form form, HttpServletRequest req, HttpServletResponse res, User user,
			URIParser uriParser) throws Exception {

		this.checkAccess(form, user);

		return super.beanAdded(form, req, res, user, uriParser);

	}

	@Override
	protected ForegroundModuleResponse beanUpdated(Form form, HttpServletRequest req, HttpServletResponse res,
			User user, URIParser uriParser) throws Exception {

		this.checkAccess(form, user);

		return super.beanUpdated(form, req, res, user, uriParser);

	}

	/**
	 * TODO: WORK IN PROGRESS Returns a ForegroundModule containing all forms
	 * displayed in the list page (Override ForegroundModuleResponse list(... ) in
	 * OpenHierarchy/src/se/unlogic/hierarchy/core/utils/GenericCRUD)
	 * 
	 * @param HttpServletRequest
	 * @param HttpServletResponse
	 * @param User
	 * @param URIParser
	 * @param List<validationError>
	 * 
	 */
	@Override
	public ForegroundModuleResponse list(HttpServletRequest req, HttpServletResponse res, User user,
			URIParser uriParser, List<ValidationError> validationErrors) throws Exception {

		this.checkListAccess(user, req, uriParser);

		log.info("User " + user + " listing " + this.typeLogPluralName);

		Document doc = formController.createDocument(req, uriParser, user);

		Element listTypeElement = doc.createElement("List" + this.typeElementPluralName); // XML ListForms
		doc.getFirstChild().appendChild(listTypeElement);

		Element nodeElement = doc.createElement(this.typeElementPluralName); // XML Forms
		listTypeElement.appendChild(nodeElement);

		if (validationErrors != null) {
			XMLUtils.append(doc, listTypeElement, validationErrors);
			listTypeElement.appendChild(RequestUtils.getRequestParameters(req, doc));

		}

		this.appendListFormData(doc, nodeElement, user, req, uriParser, validationErrors);

		SimpleForegroundModuleResponse moduleResponse = createListModuleResponse(doc, req, user, uriParser);

		moduleResponse.addBreadcrumbsLast(getListBreadcrumbs(req, user, uriParser, validationErrors));

		return moduleResponse;

	}

	/**
	 * Appends Form data to xml document to display on main list page
	 */
	@Override
	protected void appendListFormData(Document doc, Element nodeElement, User user, HttpServletRequest req,
			URIParser uriParser, List<ValidationError> validationError) throws SQLException {
		List<Form> formList = this.getAllowedForms(user, formDAO.getAll()); // Form

		addReqisterTypeAndContactName(formList);
		handleFormGroup(null, formList, user); // Group

		XMLUtils.append(doc, nodeElement, formList); // XML Questionnaire
	}

	@Override
	public ForegroundModuleResponse add(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser)
			throws Exception {

		try {
			ValidationException validationException = null;

			try {
				req = parseRequest(req, user);

				this.checkAddAccess(user, req, uriParser);

				/**
				 * True if the add button is pressed on the add form
				 */
				if (req.getMethod().equalsIgnoreCase("POST")) {

					Form form = this.populateFromAddRequest(req, user, uriParser);
					System.out.println("FormName: " + form.getName());

					Map<String, String[]> paramMap = req.getParameterMap();
					Iterator<String> it = paramMap.keySet().iterator();

					while (it.hasNext()) {
						String key = it.next();
						for (String s : paramMap.get(key)) {
							System.out.println("Key: " + key + ", Value: " + s);
							if (key.contains("question-53")) {
								System.out.println("----------------------------------------------------------"
										+ paramMap.get(key).getClass());
							}
						}

					}

					try {
						form.setName(req.getParameter("name"));
						form.setFormType(this.getFormType(1));

						form.setGroupID(Integer.parseInt(req.getParameter("selectedFormGroup")));

						/**
						 * Uses the Mapper class to create a questionnaire and map information towards a
						 * questionnaire
						 */
						form.setQuestionnaire(
								new Mapper(this).mapToQuestionnaire(req, new Questionnaire(), this.getQuestionList()));

					} catch (NumberFormatException e) {
						throw new NumberFormatException("Number not parsable " + e.getMessage());

					}

					this.validateAddPopulation(form, req, user, uriParser);

					log.info("User " + user + " adding " + this.typeLogName + " " + form);

					this.addBean(form, req, user, uriParser);

					System.out.println("ID: " + form.getID() + " FormName: " + form.getName());

					return this.beanAdded(form, req, res, user, uriParser);

				}

			} catch (ValidationException e) {

				validationException = e;
			}

			return showAddForm(req, res, user, uriParser, validationException);

		} finally {
			releaseRequest(req, user);

		}
	}

	/**
	 * Returns a foregroundmodule containing a document with questions used to
	 * create a new form.
	 */
	@Override
	public ForegroundModuleResponse showAddForm(HttpServletRequest req, HttpServletResponse res, User user,
			URIParser uriParser, ValidationException validationException) throws Exception {

		log.info("User " + user + " requested add " + this.typeLogName + " form");

		Document doc = formController.createDocument(req, uriParser, user);

		Element addTypeElement = doc.createElement("Add" + typeElementName); // XML AddForm
		doc.getFirstChild().appendChild(addTypeElement);

		Element nodeElement = doc.createElement(this.typeElementPluralName); // XML Forms
		addTypeElement.appendChild(nodeElement);

		if (validationException != null) {
			addTypeElement.appendChild(validationException.toXML(doc));
			addTypeElement.appendChild(RequestUtils.getRequestParameters(req, doc));
		}

		this.appendAddFormData(doc, nodeElement, user, req, uriParser);

		SimpleForegroundModuleResponse moduleResponse = createAddFormModuleResponse(doc, req, user, uriParser);

		moduleResponse.addBreadcrumbsLast(this.getAddBreadcrumbs(req, user, uriParser));

		return moduleResponse;
	}

	/**
	 * Add questions to the xml document
	 */
	@Override
	protected void appendAddFormData(Document doc, Element addTypeElement, User user, HttpServletRequest req,
			URIParser uriParser) throws Exception {
		List<Question> questionList = questionDAO.getAll();

		/**
		 * Set ID to AttributeDetail for use in front end rendering
		 */
		for (Question q : questionList) {
			q.setAttributeDetails("question-" + String.valueOf(q.getID()));
			q.setActionRequired(false);

			if (q.getName().equalsIgnoreCase("Status")) {
				List<QuestionOption> statusList = new ArrayList<>();
				for (QuestionnaireStatus status : questionnaireStatusDAO.getAll()) {
					QuestionOption value = new QuestionOption();
					value.setID(status.getID());
					value.setOption(status.getStatus());

					statusList.add(value);

				}

				q.setQuestionOptions(statusList);
				q.setAttributeDetails("questionstatus");
			}

		}

		handleQuestionGroup(questionList, user);

		Collections.sort(questionList);

		XMLUtils.append(doc, addTypeElement, questionList);

		Element element = (Element) doc.getFirstChild().getFirstChild().getNextSibling().getNextSibling();
		XMLUtils.appendNewElement(doc, element, "FormType", this.getFormType(1).getName());

	}

	@Override
	public ForegroundModuleResponse update(HttpServletRequest req, HttpServletResponse res, User user,
			URIParser uriParser) throws Exception {
		try {
			ValidationException validationException = null;

			try {
				req = parseRequest(req, user);

			} catch (ValidationException parseException) {

				// Try to get bean anyway so we can show the update form
				Form form;

				try {
					form = this.getRequestedBean(req, res, user, uriParser, UPDATE);

					if (form != null) {

						return showUpdateForm(form, req, res, user, uriParser, parseException);
					}

				} catch (Exception getBeanException) {
				}

				return list(req, res, user, uriParser, parseException.getErrors());
			}

			Form form = this.getRequestedBean(req, res, user, uriParser, UPDATE);
			int oldFileId = FileDAO.getInstance().getOldFileId(form.getQuestionnaire().getID());

			try {
				this.checkUpdateAccess(form, user, req, uriParser);

				if (req.getMethod().equalsIgnoreCase("POST")) {

					boolean complete = false;

					form = this.populateFromUpdateRequest(form, req, user, uriParser);

					System.out.println("FormName: " + form.getName());

					Map<String, String[]> paramMap = req.getParameterMap();
					Iterator<String> it = paramMap.keySet().iterator();

					while (it.hasNext()) {
						String key = it.next();
						for (String s : paramMap.get(key)) {
							System.out.println("Key: " + key + ", Value: " + s);

						}

					}

					form.setGroupID(Integer.parseInt(req.getParameter("selectedFormGroup")));

					form.setQuestionnaire(
							new Mapper(this).mapToQuestionnaire(req, form.getQuestionnaire(), this.getQuestionList()));

					this.validateUpdatePopulation(form, req, user, uriParser);

					log.info("User " + user + " updating " + typeLogName + " " + form);

					this.updateBean(form, req, user, uriParser);

					FileDAO.getInstance().updateFileId(form.getQuestionnaire().getID(), oldFileId);

					return this.beanUpdated(form, req, res, user, uriParser);

				}

			} catch (ValidationException e) {

				validationException = e;
			}

			return showUpdateForm(form, req, res, user, uriParser, validationException);

		} finally {

			releaseRequest(req, user);
		}
	}

	@Override
	public ForegroundModuleResponse showUpdateForm(Form form, HttpServletRequest req, HttpServletResponse res,
			User user, URIParser uriParser, ValidationException validationException) throws Exception {

		log.info("User " + user + " requested update " + this.typeLogName + " form for " + form);

		Document doc = formController.createDocument(req, uriParser, user);
		Element updateTypeElement = doc.createElement("Update" + typeElementName);
		doc.getFirstChild().appendChild(updateTypeElement);

		Element nodeElement = doc.createElement(this.typeElementPluralName);
		updateTypeElement.appendChild(nodeElement);

		XMLUtils.append(doc, nodeElement, form);

		if (validationException != null) {
			updateTypeElement.appendChild(validationException.toXML(doc));
			updateTypeElement.appendChild(RequestUtils.getRequestParameters(req, doc));
		}

		this.appendUpdateFormData(form, doc, nodeElement, user, req, uriParser);

		SimpleForegroundModuleResponse moduleResponse = createUpdateFormModuleResponse(form, doc, req, user, uriParser);

		moduleResponse.addBreadcrumbsLast(getUpdateBreadcrumbs(form, req, user, uriParser));

		return moduleResponse;

	}

	/**
	 * Adds the saved information to the XML document
	 */
	@Override
	protected void appendUpdateFormData(Form form, Document doc, Element updateTypeElement, User user,
			HttpServletRequest req, URIParser uriParser) throws Exception {

		LowLevelQuery<QuestionnaireValue> query = new LowLevelQuery<>();
		query.setSql(
				"SELECT * FROM form_questionnaire_value WHERE questionnaire_id = " + form.getQuestionnaire().getID());

		List<QuestionnaireValue> questionnaireValueList = questionnaireValueDAO.getAll(query);

		List<Question> questionList = new ArrayList<>();

		for (Question question : this.getQuestionList()) {
			question.setAttributeDetails("question-" + question.getID());

			try {

				if (question.getName().equalsIgnoreCase("Personuppgiftsansvarig")) {
					handleSingleQuestionGroup(question, user);
					question.setAttributeDetails("selectedFormGroup");
					System.out.println("Handled questiongroup");

				} else if (question.getName().equalsIgnoreCase("Status")) {
					List<QuestionOption> statusList = new ArrayList<>();
					for (QuestionnaireStatus status : questionnaireStatusDAO.getAll()) {
						QuestionOption value = new QuestionOption();
						value.setID(status.getID());
						value.setOption(status.getStatus());

						statusList.add(value);

						System.out.println("handled status");

					}

					question.setAttributeDetails("questionstatus");
					question.setQuestionOptions(statusList);

				}

			} catch (NullPointerException qvexception) {

			}

			questionList.add(question);

		}

		new Mapper(this).mapQuestionToUpdateForm(questionList, questionnaireValueList, doc, updateTypeElement, user,
				req, uriParser);

	}

	/**
	 * Handles group mapping between simple-groups table and the actual form e.g
	 * Sets the group name on the form on view. If groupID doesn't exist, the name
	 * will be set to an empty string
	 * 
	 * @param form     (a single form, pass null if a list is used)
	 * @param formList (a list of forms, pass null if a single form is used)
	 * @param user
	 * @return
	 */
	private void handleFormGroup(Form form, List<Form> formList, User user) {
		Map<Integer, String> groupMap = this.systemInterface.getGroupHandler().getGroups(false).stream()
				.collect(Collectors.toMap(Group::getGroupID, Group::getName));

		try {
			if (form != null && formList == null) {
				if (groupMap.containsKey(form.getGroupID())) {
					form.setGroupName(groupMap.get(form.getGroupID()));

				} else {
					form.setGroupName("");

				}

			} else if (form == null && !formList.isEmpty()) {
				for (Form f : formList) {
					if (groupMap.containsKey(f.getGroupID())) {
						f.setGroupName(groupMap.get(f.getGroupID()));

					} else {
						f.setGroupName("");

					}

				}

			}

		} catch (ClassCastException | NullPointerException e) {
			return;

		}

	}

	/**
	 * If the question has question type == ENUM-GROUP add groups as question
	 * values.
	 * 
	 * @param doc
	 * @param questionList
	 * @param user
	 * @throws SQLException
	 */
	private void handleQuestionGroup(List<Question> questionList, User user) throws SQLException {
		List<QuestionOption> qvList = new ArrayList<>();

		for (Question q : questionList) {
			if (q.getQuestionType().getQuestionType().equalsIgnoreCase("ENUM")
					&& q.getName().equalsIgnoreCase("Personuppgiftsansvarig")) {
				try {
					for (Group g : this.systemInterface.getGroupHandler().getGroups(false)) {
						if (g.getGroupID() != 1) {
							QuestionOption qv = new QuestionOption();
							qv.setID(g.getGroupID());
							qv.setOption(g.getName());

							qvList.add(qv);

						}

					}

					q.setQuestionOptions(qvList);

				} catch (ClassCastException | NullPointerException e) {
					return;

				}

				q.setAttributeDetails("selectedFormGroup");

			}

		}

	}

	/**
	 * Add groups as question value to a specific question
	 * 
	 * @param question
	 * @param user
	 * @throws SQLException
	 */
	private void handleSingleQuestionGroup(Question question, User user) throws SQLException {
		List<QuestionOption> qvList = new ArrayList<>();

		try {
			for (Group g : this.systemInterface.getGroupHandler().getGroups(false)) {
				if (g.getGroupID() != 1) {
					QuestionOption qv = new QuestionOption();
					qv.setID(g.getGroupID()); // XML row <GroupID>
					qv.setOption(g.getName()); // XML row <GroupName>

					qvList.add(qv);

				}

			}

			question.setQuestionOptions(qvList);

		} catch (ClassCastException | NullPointerException e) {
			return;

		}

	}

	/**
	 * Returns the group name
	 * 
	 * @param groupID
	 * @return
	 */
	public String getGroupName(Integer groupID) {
		Map<Integer, Group> groupMap = this.systemInterface.getGroupHandler().getGroups(false).stream()
				.collect(Collectors.toMap(Group::getGroupID, Group -> Group));

		return groupMap.get(groupID).getName();

	}

	/**
	 * Returns a status name based on the status ID
	 * 
	 * @param statusID
	 * @return
	 * @throws SQLException
	 */
	public String getStatusName(Integer statusID) throws SQLException {
		Map<Integer, QuestionnaireStatus> statusMap = questionnaireStatusDAO.getAll().stream()
				.collect(Collectors.toMap(QuestionnaireStatus::getID, QuestionnaireStatus -> QuestionnaireStatus));

		return statusMap.get(statusID).getStatus();

	}

	/**
	 * Returns a form type based on the form type ID
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public FormType getFormType(Integer id) throws SQLException {
		HighLevelQuery<FormType> query = new HighLevelQuery<>();
		query.addParameter(formTypeIDParamFactory.getParameter(id));
		FormType result = formTypeDAO.get(query);
		return result;

	}

	/**
	 * Returns a Questionnaire status based on the questionnaire status ID
	 * 
	 * @param id
	 * @return
	 * @throws SQLException
	 */
	public QuestionnaireStatus getQuestionnaireStatus(Integer id) throws SQLException {
		LowLevelQuery<QuestionnaireStatus> query = new LowLevelQuery<>();
		query.setSql("SELECT * FROM form_questionnaire_status WHERE questionnaire_status_id = " + id);

		QuestionnaireStatus status = questionnaireStatusDAO.get(query);

		return status;

	}

	/**
	 * Returns a list with all questions
	 * 
	 * @return
	 * @throws SQLException
	 */
	private List<Question> getQuestionList() throws SQLException {
		List<Question> questionList = questionDAO.getAll();

		return questionList;

	}



	/**
	 * Add register type and contact name to each form on the list page
	 * 
	 * @param formList
	 * @throws SQLException
	 */
	private void addReqisterTypeAndContactName(List<Form> formList) throws SQLException {

		for (Form form : formList) {
			// Query for the saved contact name
			LowLevelQuery<QuestionnaireValue> contactQuery = new LowLevelQuery<>();
			contactQuery.setSql("SELECT * FROM form_questionnaire_value WHERE questionnaire_id = "
					+ form.getQuestionnaire().getID() + " AND question_id = " + 7);

			// Query for the saved register type
			LowLevelQuery<QuestionnaireValue> registerQuery = new LowLevelQuery<>();
			registerQuery.setSql("SELECT * FROM form_questionnaire_value WHERE questionnaire_id = "
					+ form.getQuestionnaire().getID() + " AND question_id = " + 8);

			QuestionnaireValue contactValue = questionnaireValueDAO.get(contactQuery);
			QuestionnaireValue registerValue = questionnaireValueDAO.get(registerQuery);

			/**
			 * If contactValue is not null, sets the saved contact name to the form, else
			 * set it to "Ej angivet"
			 */
			if (contactValue != null) {
				if (contactValue.getSelectedValues() != null) {
					form.getQuestionnaire().setContactName(contactValue.getSelectedValues().get(0).getValue());
				}

			} else {
				form.getQuestionnaire().setContactName("Ej angivet");

			}

			/**
			 * If registerValue is not null, if null catch and set the saved register type
			 * to the form, else set it to "Ej angivet"
			 */
			if (registerValue != null) {
				if (registerValue.getSelectedValues() != null) {
					form.getQuestionnaire().setRegisterType(registerValue.getSelectedValues().get(0).getValue());
				}
			} else {
				form.getQuestionnaire().setRegisterType("Ej angivet");

			}

		}

	}

}
