package se.sundsvallskommun.nodes.controller;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.sql.Blob;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;

import org.apache.commons.fileupload.FileItem;
import org.apache.tika.Tika;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import rst.pdfbox.layout.elements.Paragraph;
import rst.pdfbox.layout.text.BaseFont;
import se.sundsvallskommun.nodes.PulRegistryModule;
import se.sundsvallskommun.nodes.crud.FormCRUD;
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
import se.sundsvallskommun.nodes.tools.ModuleRequestContext;
import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.hierarchy.core.exceptions.AccessDeniedException;
import se.unlogic.hierarchy.core.interfaces.ForegroundModuleResponse;
import se.unlogic.hierarchy.core.interfaces.SystemInterface;
import se.unlogic.hierarchy.core.utils.HierarchyAnnotatedDAOFactory;
import se.unlogic.hierarchy.core.utils.crud.TransactionRequestFilter;
import se.unlogic.hierarchy.foregroundmodules.rest.RESTMethod;
import se.unlogic.standardutils.dao.AdvancedAnnotatedDAOWrapper;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.LowLevelQuery;
import se.unlogic.standardutils.dao.QueryParameterFactory;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.HTTPUtils;
import se.unlogic.webutils.http.URIParser;
import se.unlogic.webutils.http.enums.ContentDisposition;

public class FormController {

	private AnnotatedDAO<Form> formDAO;
	private AnnotatedDAO<FormType> formTypeDAO;
	private AnnotatedDAO<QuestionnaireStatus> questionnaireStatusDAO;
	private AnnotatedDAO<Question> questionDAO;
	private AnnotatedDAO<QuestionOption> questionOptionDAO;
	private AnnotatedDAO<Questionnaire> questionnaireDAO;
	private AnnotatedDAO<QuestionnaireValue> questionnaireValueDAO;
	private AnnotatedDAO<SelectedValue> selectedValueDAO;
	private FileDAO fileDAO;

	private FormCRUD formCRUD;

	private QueryParameterFactory<FormType, Integer> formTypeIDParamFactory;
	private QueryParameterFactory<QuestionnaireStatus, Integer> questionnaireStatusIDParamFactory;
	private QueryParameterFactory<QuestionnaireValue, Integer> questionnaireValueIDParamFactory;

	private SystemInterface systemInterface;
//	private Logger log = Logger.getLogger(this.getClass());

	private PulRegistryModule callback;

	public FormController() {

	}

	public FormController(PulRegistryModule callback, DataSource dataSource, SystemInterface systemInterface)
			throws Exception {
		this.callback = callback;
		this.systemInterface = systemInterface;
		createDAOs(dataSource);

	}

	protected void createDAOs(DataSource dataSource) throws Exception {

		HierarchyAnnotatedDAOFactory daoFactory = new HierarchyAnnotatedDAOFactory(dataSource,
				systemInterface.getUserHandler(), systemInterface.getGroupHandler());
		this.formDAO = daoFactory.getDAO(Form.class);
		this.formTypeDAO = daoFactory.getDAO(FormType.class);
		this.questionnaireDAO = daoFactory.getDAO(Questionnaire.class);
		this.questionnaireStatusDAO = daoFactory.getDAO(QuestionnaireStatus.class);
		this.questionDAO = daoFactory.getDAO(Question.class);
		this.questionOptionDAO = daoFactory.getDAO(QuestionOption.class);
		this.questionnaireValueDAO = daoFactory.getDAO(QuestionnaireValue.class);
		this.selectedValueDAO = daoFactory.getDAO(SelectedValue.class);
		this.fileDAO = new FileDAO<File>(dataSource, File.class, daoFactory, questionnaireValueDAO);

		AdvancedAnnotatedDAOWrapper<Form, Integer> formDAOWrapper = this.formDAO.getAdvancedWrapper(Integer.class);

		this.formCRUD = new FormCRUD(formDAOWrapper, callback, this, systemInterface);

		this.formTypeIDParamFactory = this.formTypeDAO.getParamFactory("formTypeID", Integer.class);
		this.questionnaireValueIDParamFactory = this.questionnaireValueDAO.getParamFactory("questionnaireValueID",
				Integer.class);

		this.formCRUD.addRequestFilter(new TransactionRequestFilter(dataSource));

		formCRUD.setDaos(formDAO, formTypeDAO, questionnaireStatusDAO, questionDAO, questionOptionDAO, questionnaireDAO,
				questionnaireValueDAO, selectedValueDAO, fileDAO, formTypeIDParamFactory);

	}

	/**
	 * Return a Foregroundmoduleresponse containing the list to be displayed when
	 * the user logs in
	 * 
	 * @param req
	 * @param res
	 * @param user
	 * @param uriParser
	 * @return
	 * @throws Exception
	 */
	public ForegroundModuleResponse listForms(HttpServletRequest req, HttpServletResponse res, User user,
			URIParser uriParser) throws Exception {

		ForegroundModuleResponse response = formCRUD.list(req, res, user, uriParser, null);

		if (response != null) {
			return response;

		} else {
			System.out.println("It was null!");

		}

		return null;

	}

	/**
	 * Returns a Foregroundmoduleresponse containing the add form
	 * 
	 * @param req
	 * @param res
	 * @param user
	 * @param uriParser
	 * @return
	 * @throws Exception
	 */
	public ForegroundModuleResponse addNewForm(HttpServletRequest req, HttpServletResponse res, User user,
			URIParser uriParser) throws Exception {

		Form form = this.formCRUD.getRequestedBean(req, res, user, uriParser, "ADD");
		this.formCRUD.checkAccess(form, user);

		ForegroundModuleResponse fgResp = this.formCRUD.add(req, res, user, uriParser);

		try {
			if (fgResp != null) {
				Document doc = fgResp.getDocument();
				Element element = (Element) fgResp.getDocument().getFirstChild().getFirstChild().getNextSibling()
						.getNextSibling();
				XMLUtils.appendNewElement(doc, element, "sessionTimeout", user.getSession().getMaxInactiveInterval());
				XMLUtils.appendNewElement(doc, element, "sessionLastAccess", user.getSession().getLastAccessedTime());
			}

		} catch (Exception e) {
		}

		return fgResp;

	}

	/**
	 * Returns a Foregroundmoduleresponse containing the update form
	 * 
	 * @param req
	 * @param res
	 * @param user
	 * @param uriParser
	 * @return
	 * @throws Exception
	 */
	public ForegroundModuleResponse updateExistingForm(HttpServletRequest req, HttpServletResponse res, User user,
			URIParser uriParser) throws Exception {

		Form form = this.formCRUD.getRequestedBean(req, res, user, uriParser, "UPDATE");
		this.formCRUD.checkAccess(form, user);
		ForegroundModuleResponse result = null;

		result = this.formCRUD.update(req, res, user, uriParser);

		try {
			if (result != null) {
				Document doc = result.getDocument();
				Element element = (Element) result.getDocument().getFirstChild().getFirstChild().getNextSibling()
						.getNextSibling();
				XMLUtils.appendNewElement(doc, element, "sessionTimeout", user.getSession().getMaxInactiveInterval());
				XMLUtils.appendNewElement(doc, element, "sessionLastAccess", user.getSession().getLastAccessedTime());
			}
		} catch (Exception e) {
		}

		return result;

	}

	/**
	 * Removes the selected questionnaire
	 * 
	 * @param req
	 * @param res
	 * @param user
	 * @param uriParser
	 * @return
	 * @throws Exception
	 */
	public ForegroundModuleResponse deleteForm(HttpServletRequest req, HttpServletResponse res, User user,
			URIParser uriParser) throws Exception {

		Form form = this.formCRUD.getRequestedBean(req, res, user, uriParser, "DELETE");

		this.formCRUD.checkAccess(form, user);

		/**
		 * Get the questionnaires saved questionnaire values with the saved selected
		 * values
		 */
		LowLevelQuery<QuestionnaireValue> query = new LowLevelQuery<>();
		query.setSql(
				"SELECT * FROM form_questionnaire_value WHERE questionnaire_id = " + form.getQuestionnaire().getID());

		List<QuestionnaireValue> questionnaireValues = this.questionnaireValueDAO.getAll(query);

		if (questionnaireValues != null) {
			for (QuestionnaireValue qvValue : questionnaireValues) {
				if (qvValue.getSelectedValues() != null) {
					selectedValueDAO.delete(qvValue.getSelectedValues());

				}

				qvValue.setSelectedValues(null);

			}

			this.questionnaireValueDAO.delete(questionnaireValues);
			form.getQuestionnaire().setQuestionnaireValues(null);

		}

		this.questionnaireDAO.delete(form.getQuestionnaire());

		ForegroundModuleResponse result = formCRUD.delete(req, res, user, uriParser);

		return result;

	}

	/**
	 * Exports a CSV file with forms depending on selected option
	 * 
	 * @param req
	 * @param res
	 * @param user
	 * @param uriParser
	 * @throws IOException
	 * @throws AccessDeniedException
	 * @throws SQLException
	 */
	public void exportCSV(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser)
			throws IOException, AccessDeniedException, SQLException {

		String delimiter = ";";
		String lineSeparator = System.getProperty("line.separator");
		List<Form> forms = this.getForms(new ModuleRequestContext(req, res, user, uriParser));

		int value = Integer.parseInt(uriParser.get(2));

		String csvString = "ID" + delimiter + "Namn";

		List<Question> questionList = questionDAO.getAll();
		Collections.sort(questionList);

		/**
		 * Adds the questions and sub questions as headers to the CSV document
		 */
		for (Question question : questionList) {
			csvString += delimiter + question.getName();

			if (!question.getRequired() && question.getParent() == null) {
				csvString += delimiter + "Anteckningar";

			}

		}

		csvString += lineSeparator;

		/**
		 * Checks if value equals case to filter exportCSV.
		 */
		List<Form> formList = new ArrayList<>();

		switch (value) {

		case 0:
			csvString = createCsvString(forms, questionList, csvString, delimiter, lineSeparator);

			break;

		case 1: // If "Ej påbörjad" is selected as export option
			for (Form form : forms) {
				if (form.getQuestionnaire().getQuestionnaireStatus() != null) {
					if (form.getQuestionnaire().getQuestionnaireStatus().getID() == 1) {
						formList.add(form);
					}
				}
			}
			csvString = createCsvString(formList, questionList, csvString, delimiter, lineSeparator);

			break;

		case 2: // If "Påbörjad" is selected as export option
			for (Form form : forms) {
				if (form.getQuestionnaire().getQuestionnaireStatus() != null) {
					if (form.getQuestionnaire().getQuestionnaireStatus().getID() == 2) {
						formList.add(form);
					}
				}
			}
			csvString = createCsvString(formList, questionList, csvString, delimiter, lineSeparator);

			break;

		case 3: // If "Klar" is selected as export option
			for (Form form : forms) {
				if (form.getQuestionnaire().getQuestionnaireStatus() != null) {
					if (form.getQuestionnaire().getQuestionnaireStatus().getID() == 3) {
						formList.add(form);
					}
				}
			}
			csvString = createCsvString(formList, questionList, csvString, delimiter, lineSeparator);

			break;

		case 4: // If "Åtgärd krävs" is selected as export option
			for (Form form : forms) {
				if (form.getQuestionnaire().getHasActionRequired() != null) {
					if (form.getQuestionnaire().getHasActionRequired().equals(true)) {
						formList.add(form);
					}
				}
			}
			csvString = createCsvString(formList, questionList, csvString, delimiter, lineSeparator);

			break;

		case 5: // If "Anteckningar" is selected as export option
			for (Form form : forms) {
				if (form.getQuestionnaire().getHasNotes() != null) {
					if (form.getQuestionnaire().getHasNotes().equals(true)) {
						formList.add(form);
					}
				}
			}
			csvString = createCsvString(formList, questionList, csvString, delimiter, lineSeparator);

			break;

		default:

			csvString = createCsvString(formList, questionList, csvString, delimiter, lineSeparator);

			break;
		}

		InputStream inputStream = new ByteArrayInputStream(csvString.getBytes(StandardCharsets.ISO_8859_1));

		HTTPUtils.sendFile(inputStream, "export.csv", "application/csv", "", req, res, ContentDisposition.INLINE);

	}

	/**
	 * For each form, add saved data to the csv, if no data is saved, adds a
	 * delimiter to create a blank field
	 */
	private String createCsvString(List<Form> formList, List<Question> questionList, String csvString, String delimiter,
			String lineSeparator) throws SQLException {

		for (Form form : formList) {

			csvString += form.getID() + delimiter + form.getName() + delimiter;

			LowLevelQuery<QuestionnaireValue> query = new LowLevelQuery<>();
			query.setSql("SELECT * FROM form_questionnaire_value WHERE questionnaire_id = "
					+ form.getQuestionnaire().getID());

			List<QuestionnaireValue> questionnaireValues = questionnaireValueDAO.getAll(query);

			if (questionnaireValues != null) {
				for (Question question : questionList) {
					String mainCSVString = "";
					String noteCSVString = "";
					boolean found = false;

					if (question.getName().equalsIgnoreCase("Personuppgiftsansvarig")) {
						csvString += formCRUD.getGroupName(form.getGroupID()) + delimiter;
						found = true;
						continue;

					} else if (question.getName().equalsIgnoreCase("Status")) {
						csvString += formCRUD.getStatusName(form.getQuestionnaire().getQuestionnaireStatus().getID())
								+ delimiter;
						found = true;
						continue;

					}

					for (QuestionnaireValue qv : questionnaireValues) {
						if (qv.getQuestion() != null) {
							if (question.getID().equals(qv.getQuestion().getID())) {
								if (qv.getSelectedValues() != null) {
									int counter = 0;
									for (SelectedValue selectedValue : qv.getSelectedValues()) {
										if (counter > 0) {
											mainCSVString += "," + selectedValue.getValue();

										} else {
											mainCSVString += selectedValue.getValue();
											counter++;

										}

									}

									found = true;

									mainCSVString += delimiter;

								}

								if (qv.getNote() != null) {
									if (qv.getNote().getNote().length() > 0) {
										noteCSVString += qv.getNote().getNote() + delimiter;

									} else {
										noteCSVString += delimiter;

									}

								} else if (qv.getNote() == null && question.getParent() == null) {
									noteCSVString += delimiter;

								}

							}

						}

					}

					/**
					 * If question is not found in saved questionnaire values, add a delimiter
					 */
					if (!found && question.getParent() == null) {
						mainCSVString += delimiter;
						noteCSVString += delimiter;

					} else if (!found && question.getParent() != null) {
						mainCSVString += delimiter;

					}

					/**
					 * Building the CSV string for the current form
					 */
					csvString += mainCSVString;
					csvString += noteCSVString;

				}

			}

			csvString += lineSeparator;

		}

		return csvString;

	}

	private List<Form> getForms(ModuleRequestContext context) throws AccessDeniedException, SQLException {
		return formCRUD.getAllowedForms(context.getUser(), formDAO.getAll());
	}

	/**
	 * Export an CSV file containing only question names
	 * 
	 * @param req
	 * @param res
	 * @param user
	 * @param uriParser
	 * @throws IOException
	 * @throws AccessDeniedException
	 * @throws SQLException
	 */
	public void exportEmptyCSV(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser)
			throws IOException, AccessDeniedException, SQLException {

		String delimiter = ";";
		String lineSeparator = System.getProperty("line.separator");
		String csvString = "ID" + delimiter + "Namn";

		/**
		 * Prints only questions
		 * 
		 */
		List<Question> questionList = questionDAO.getAll();
		Collections.sort(questionList);

		for (Question question : questionList) {
			csvString += delimiter + question.getName();

			/**
			 * If question is !required, add column for notes
			 */
			if (!question.getRequired()) {
				csvString += delimiter + "Anteckningar";

			}

		}

		csvString += lineSeparator;

		InputStream inputStream = new ByteArrayInputStream(csvString.getBytes(StandardCharsets.ISO_8859_1));

		HTTPUtils.sendFile(inputStream, "mall.csv", "application/csv", "", req, res, ContentDisposition.INLINE);

	}

	/**
	 * Exports a PDF file containing the selected questionnaire
	 * 
	 * @param req
	 * @param res
	 * @param user
	 * @param uriParser
	 * @throws IOException
	 * @throws AccessDeniedException
	 * @throws SQLException
	 * @throws COSVisitorException
	 */
	public void exportPDF(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser)
			throws IOException, AccessDeniedException, SQLException {

		// Creating PDF document object
		rst.pdfbox.layout.elements.Document doc = new rst.pdfbox.layout.elements.Document(40, 60, 40, 60);

		Paragraph paragraph1 = new Paragraph();
		int formID = Integer.parseInt(uriParser.get(2));
		String title = null;

		List<Question> questionList = questionDAO.getAll();
		Collections.sort(questionList);

		/**
		 * Query to receive the selected form
		 */
		LowLevelQuery<Form> formQuery = new LowLevelQuery<>();
		formQuery.setSql("SELECT * FROM form WHERE form_id = " + formID);
		Form form = formDAO.get(formQuery);

		this.formCRUD.checkAccess(form, user);

		/**
		 * Query to receive the forms questionnaire values
		 */
		LowLevelQuery<QuestionnaireValue> questionnaireValueQuery = new LowLevelQuery<>();
		questionnaireValueQuery.setSql(
				"SELECT * FROM form_questionnaire_value WHERE questionnaire_id = " + form.getQuestionnaire().getID());

		List<QuestionnaireValue> questionnaireValues = questionnaireValueDAO.getAll(questionnaireValueQuery);

		Map<Integer, QuestionOption> questionOptionMap = questionOptionDAO.getAll().stream()
				.collect(Collectors.toMap(QuestionOption::getID, QuestionOption -> QuestionOption));

		if (questionnaireValues != null) {

			title = form.getTitle();
			paragraph1.addMarkup("\\\\\\\\\\\\*__" + form.getTitle() + "\\\\\\\\\\\\*__\n", 16, BaseFont.Times);

			doc.add(paragraph1);

			for (Question question : questionList) {

				Paragraph questionParagraph = new Paragraph();
				Paragraph noteParagraph = new Paragraph();

				boolean found = false;
				String desc = question.getDescription();

				if (question.getName().equalsIgnoreCase("Personuppgiftsansvarig")) {
					questionParagraph.addMarkup(
							"\\\\\\\\\\\\*" + question.getName() + "\\\\\\\\\\\\*\n_" + desc + "_\n", 11,
							BaseFont.Times);

					questionParagraph.addMarkup(formCRUD.getGroupName(form.getGroupID()) + "\n", 11, BaseFont.Times);

					found = true;

				} else if (question.getName().equalsIgnoreCase("Status")) {
					questionParagraph.addMarkup("\n" + "\\\\\\\\\\\\*" + question.getName() + "\\\\\\\\\\\\*\n_", 11,
							BaseFont.Times);

					questionParagraph.addMarkup(
							formCRUD.getStatusName(form.getQuestionnaire().getQuestionnaireStatus().getID()) + "\n", 11,
							BaseFont.Times);

					found = true;

				}

				for (QuestionnaireValue questionnaireValue : questionnaireValues) {
					if (questionnaireValue.getQuestion() != null) {
						if (question.getID().equals(questionnaireValue.getQuestion().getID())) {
							if (questionnaireValue.getSelectedValues() != null) {
								questionParagraph.addMarkup(
										"\n" + "\\\\\\\\\\\\*" + question.getName() + "\\\\\\\\\\\\*\n_" + desc + "_\n",
										11, BaseFont.Times);
								for (SelectedValue selectedValue : questionnaireValue.getSelectedValues()) {
									if (question.getQuestionType().getQuestionType().equalsIgnoreCase("MULTISELECT")
											|| question.getQuestionType().getQuestionType()
													.equalsIgnoreCase("SINGLESELECT")) {

										String value = questionOptionMap.get(Integer.parseInt(selectedValue.getValue()))
												.getOption();
										questionParagraph.addMarkup(value + "\n", 11, BaseFont.Times);

									} else {
										if (selectedValue.getValue().equalsIgnoreCase("true")) {
											questionParagraph.addMarkup("Ja" + "\n", 11, BaseFont.Times);

										} else if (selectedValue.getValue().equalsIgnoreCase("false")) {
											questionParagraph.addMarkup("Nej" + "\n", 11, BaseFont.Times);

										} else {
											questionParagraph.addMarkup(selectedValue.getValue() + "\n", 11,
													BaseFont.Times);

										}
									}

								}

								found = true;

							} else if (questionnaireValue.getSelectedValues() == null
									&& questionnaireValue.getNote() != null) {
								noteParagraph.addMarkup("\n" + "\\\\\\\\\\\\*" + "Anteckningar till "
										+ question.getName() + "\\\\\\\\\\\\*\n_", 11, BaseFont.Times);

								if (questionnaireValue.getNote().getNote().length() > 0) {
									noteParagraph.addMarkup(questionnaireValue.getNote() + "\n", 11, BaseFont.Times);

								} else {
									noteParagraph.addMarkup(" " + "\n", 11, BaseFont.Times);

								}

							}

						}

					}

				}

				if (!found) {
					questionParagraph.addMarkup("\n" + "\\\\\\\\\\\\*" + question.getName() + "\\\\\\\\\\\\*\n_", 11,
							BaseFont.Times);
					questionParagraph.addMarkup(" " + "\n", 11, BaseFont.Times);

				}

				doc.add(questionParagraph);
				doc.add(noteParagraph);

			}

		}

		ByteArrayOutputStream out = new ByteArrayOutputStream();

		doc.save(out);
		
		ByteArrayInputStream in = new ByteArrayInputStream(out.toByteArray());
		HTTPUtils.sendFile(in, title + ".pdf", "application/pdf", "", req, res, ContentDisposition.ATTACHMENT);

	}

	public Document createDocument(HttpServletRequest req, URIParser uriParser, User user) {
		return callback.createDocument(req, uriParser, user);

	}

	/**
	 * Export saved file
	 * 
	 * @param req
	 * @param res
	 * @param user
	 * @param uriParser
	 * @throws IOException
	 * @throws AccessDeniedException
	 * @throws SQLException
	 */
	public void downloadFile(HttpServletRequest req, HttpServletResponse res, User user, URIParser uriParser)
			throws IOException, AccessDeniedException, SQLException {

		int fileId = Integer.parseInt(uriParser.get(2));
		File blobFile = this.fileDAO.getFile(fileId);

		InputStream is = blobFile.getFileData().getBinaryStream();

		Tika tika = new Tika();
		
		HTTPUtils.sendFile(is, blobFile.getFileName(), tika.detect(is), "", req, res, ContentDisposition.INLINE);

	}

}
