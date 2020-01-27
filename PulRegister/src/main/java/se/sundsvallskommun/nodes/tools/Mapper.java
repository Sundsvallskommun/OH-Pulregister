package se.sundsvallskommun.nodes.tools;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletRequest;
import javax.sql.rowset.serial.SerialBlob;

import org.apache.commons.fileupload.FileItem;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.google.protobuf.Field;
import com.itextpdf.text.log.SysoCounter;

import se.sundsvallskommun.nodes.crud.FormCRUD;
import se.sundsvallskommun.nodes.dao.FileDAO;
import se.sundsvallskommun.nodes.model.File;
import se.sundsvallskommun.nodes.model.Note;
import se.sundsvallskommun.nodes.model.Question;
import se.sundsvallskommun.nodes.model.QuestionOption;
import se.sundsvallskommun.nodes.model.Questionnaire;
import se.sundsvallskommun.nodes.model.QuestionnaireValue;
import se.sundsvallskommun.nodes.model.SelectedValue;
import se.unlogic.fileuploadutils.MultipartRequest;
import se.unlogic.hierarchy.core.beans.User;
import se.unlogic.standardutils.time.TimeUtils;
import se.unlogic.standardutils.xml.XMLUtils;
import se.unlogic.webutils.http.URIParser;

public class Mapper {

	private FormCRUD formCRUD;
	private boolean hasActionRequired = false;
	private boolean hasNotes = false;
	private FileDAO fileDAO;

	public Mapper(FormCRUD formCRUD) {
		this.formCRUD = formCRUD;
		this.fileDAO = FileDAO.getInstance();
	}

	/**
	 * Maps servlet request to a questionnaire
	 * 
	 * @param req
	 * @param questionnaire
	 * @return
	 * @throws SQLException
	 */
	public Questionnaire mapToQuestionnaire(HttpServletRequest req, Questionnaire questionnaire,
			List<Question> questionList) throws SQLException {
		Map<Integer, Question> questionMap = questionList.stream()
				.collect(Collectors.toMap(Question::getID, Question -> Question));

		Map<Integer, QuestionnaireValue> valueMap = new HashMap<>();

		Map<String, String[]> paramMap = req.getParameterMap();
		Iterator<String> it = paramMap.keySet().iterator();

		/**
		 * Chops the parameter to retrieve the type of the question, question id and
		 * attributes of the question. e.g if the question has notes or if it has action
		 * required
		 */
		while (it.hasNext()) {

			String key = it.next();
			String[] params = key.split("-");

//			System.out.println("Param[0]: " + params[0] + ", key: " + key);

			int id = 0;

			boolean actionRequired = false;
			boolean valueFound = false;

			QuestionnaireValue questionnaireValue = null;

			switch (params[0]) {
			case "question":
				// If question has note
				if (key.contains("|note")) {
					if (req.getParameter(key).length() > 0) {
						String s = params[1].split("\\|")[0];
						id = Integer.parseInt(s);

						questionnaireValue = checkIfExist(id, valueMap, valueFound);
						Note note = new Note();
						note.setNote(req.getParameter(key));
						questionnaireValue.setNote(note);

						hasNotes = true;

						questionnaireValue.setQuestion(questionMap.get(id));

						if (!valueFound) {
							valueMap.put(id, questionnaireValue);

						}

						break;

					} else {
						break;

					}

					// If question has action required checked
				} else if (key.contains("actionrequired")) {
					id = Integer.parseInt(params[1]);

					questionnaireValue = checkIfExist(id, valueMap, valueFound);

					hasActionRequired = true;
					actionRequired = true;
					questionnaireValue.setQuestion(questionMap.get(id));

					if (!valueFound) {
						valueMap.put(id, questionnaireValue);

					}

					break;

				} else {
					if (req.getParameter(key).length() > 0) {
						id = Integer.parseInt(params[1]);

						questionnaireValue = checkIfExist(id, valueMap, valueFound);

						questionnaireValue.setSelectedValues(
								new Value().add(paramMap.get(key), questionnaireValue, questionMap.get(id)));

						if (!valueFound) {
							valueMap.put(id, questionnaireValue);

						}

						break;

					}

					break;

				}

				// Saves the status selected towards the questionnaire instead of a selected
				// value
			case "questionstatus":
				try {
					questionnaire.setQuestionnaireStatus(
							formCRUD.getQuestionnaireStatus(Integer.parseInt(req.getParameter(key))));

				} catch (NumberFormatException | SQLException e) {
					e.printStackTrace();

				}

				break;

			case "file":

				String fileAsString = "";
				int fileId = -1;
				File fileInDB = null;
				int questionnaireId = questionnaire.getID();

				// Hämtar ut filen ur paramMap
				for (String s : paramMap.get(key)) {
					fileAsString = s;
				}

				System.out.println("filen som sträng...? " + fileAsString);

				if (!fileAsString.isEmpty() && fileAsString.contains(";")) {
					fileId = Integer.valueOf(fileAsString.split(";")[0]);
					System.out.println(fileId);
					fileInDB = fileDAO.getFile(fileId);
				}

				if (fileAsString.isEmpty()) {
					// om det inte kommer någon fil från formuläret tar vi bort filen om det finns
					// någon i db.
					fileDAO.deleteFile(fileDAO.getOldFileId(questionnaireId));
					
				} else {

					// lägg in ny fil.
					questionnaireValue = checkIfExist(id, valueMap, valueFound);
					if (questionnaireValue != null) {
						if (req instanceof MultipartRequest) {
							MultipartRequest wrapper = (MultipartRequest) req;
							
							
							List<FileItem> fileItems = wrapper.getFiles();
							// File file = questionnaireValue.getFile();
							
							System.out.println("fileitems:     "+fileItems);

							// Wrappern hittar bara de nya filerna därför tappas dom redan tillagda bort.
							File f = null;
							for (FileItem file : fileItems) {
								long sz = file.getSize();
								if (file.getName() != null && sz > 0L) {
									System.out.println("the file is: " + req.getParameterValues(key)[0]);
									Path p = Paths.get(file.getName());
									String filename = p.getFileName().toString();

									if (f == null)
										f = new File();
									f.setFileName(filename);
									f.setFileSize(sz);
									f.setFileData(new SerialBlob(file.get()));
									f.setQuestionnaireValue(questionnaireValue);

									f.setDateAdded(TimeUtils.getCurrentTimestamp());
									System.out.println("the current file is: " + f.getFileName() + " " + f.getFileSize()
											+ " " + f.getDateAdded());
									System.out.println("filedata: " + f.getFileData());
//											System.out.println(f);

								}
							}
							System.out.println(f);
							questionnaireValue.setFile(f);
							questionnaireValue.setQuestion(questionMap.get(53));
						}

						if (!valueFound) {
							valueMap.put(53, questionnaireValue);

						}

					}

				}

				break;

			default:

				break;

			}

			if (questionnaireValue != null) {
				questionnaireValue.setActionRequired(actionRequired);

			}

		}

		for (Integer i : valueMap.keySet()) {
			System.out.println("key is: " + i);
		}

		try {
			// Sets the questionnaire values as a list to the questionnaire
			List<QuestionnaireValue> questionnaireValueList = new ArrayList<>(valueMap.values());
			questionnaire.setQuestionnaireValues(questionnaireValueList);

			// Marks the questionnaire with action required and notes tag
			questionnaire.setHasActionRequired(hasActionRequired);
			questionnaire.setHasNotes(hasNotes);

		} catch (NumberFormatException e) {
			throw new NumberFormatException("Number not correctly formatted: " + e.getMessage());

		}

		return questionnaire;

	}

	/**
	 * Adds questions to the XML and maps saved values towards the correct question
	 * 
	 * @param questionList
	 * @param questionnaireValueList
	 * @param doc
	 * @param updateTypeElement
	 * @param user
	 * @param req
	 * @param uriParser
	 * @throws Exception
	 */
	public void mapQuestionToUpdateForm(List<Question> questionList, List<QuestionnaireValue> questionnaireValueList,
			Document doc, Element updateTypeElement, User user, HttpServletRequest req, URIParser uriParser)
			throws Exception {

		Collections.sort(questionList);

		System.out.println(questionList.size());

		for (Question question : questionList) {

			boolean actionRequired = false;
			String note = "";

			Element questionElement = doc.createElement("Question");
			updateTypeElement.appendChild(questionElement);

			// Sets the basic info of each question
			questionElement.appendChild(XMLUtils.createElement("ID", question.getID(), doc));
			questionElement.appendChild(XMLUtils.createElement("Name", question.getName(), doc));
			questionElement.appendChild(XMLUtils.append(doc, updateTypeElement, question.getQuestionType()));
			if (question.getDescription() != null) {
				questionElement.appendChild(XMLUtils.createElement("Description", question.getDescription(), doc));
			}

			questionElement.appendChild(XMLUtils.createElement("Required", question.getRequired(), doc));
			questionElement.appendChild(XMLUtils.createElement("Enabled", question.getEnabled(), doc));
			questionElement.appendChild(XMLUtils.createElement("SortPrio", question.getSortPrio(), doc));

			if (question.getParent() != null) {
				questionElement.appendChild(XMLUtils.createElement("Parent", question.getParent(), doc));

			}

			if (question.getOption() != null) {
				questionElement.appendChild(XMLUtils.createElement("Option", question.getOption(), doc));
			}

			// Builds the enum question options and add selected value to the saved value
			if (question.getQuestionType().getQuestionType().equalsIgnoreCase("ENUM")) {
				Element questionOptionsElement = doc.createElement("QuestionOptions");

				if (question.getQuestionOptions() != null) {
					for (QuestionOption questionOption : question.getQuestionOptions()) {
						questionElement.appendChild(questionOptionsElement);
						Element questionOptionElement = doc.createElement("QuestionOption");

						questionOptionsElement.appendChild(questionOptionElement);
						questionOptionElement.appendChild(XMLUtils.createElement("value", questionOption.getID(), doc));
						questionOptionElement
								.appendChild(XMLUtils.createElement("name", questionOption.getOption(), doc));
						if (questionnaireValueList != null) {

							for (QuestionnaireValue qv : questionnaireValueList) {
								if (qv.getSelectedValues() != null) {
									for (SelectedValue sv : qv.getSelectedValues()) {
										try {
											if (questionOption.getID().equals(Integer.parseInt(sv.getValue()))) {

												Element selected = doc.createElement("selected");
												questionOptionElement.appendChild(selected);

											}

										} catch (NumberFormatException e) {
										}

									}

								}

							}
						}

					}

				}

			} else if (question.getQuestionType().getQuestionType().equalsIgnoreCase("STRINGBOX")
					|| question.getQuestionType().getQuestionType().equalsIgnoreCase("STRING")
					|| question.getQuestionType().getQuestionType().equalsIgnoreCase("SPECIALSTRING")
					|| question.getQuestionType().getQuestionType().equalsIgnoreCase("BOOL")) {

				if (questionnaireValueList != null) {

					for (QuestionnaireValue questionnaireValue : questionnaireValueList) {

						if (questionnaireValue.getQuestion() != null
								&& questionnaireValue.getQuestion().getID().equals(question.getID())) {

							if (questionnaireValue.getNote() != null) {
								if (questionnaireValue.getNote().getNote().length() > 0
										|| questionnaireValue.getNote().getNote() != "") {
									note = questionnaireValue.getNote().getNote();

								}

							}

							if (questionnaireValue.getSelectedValues() != null) {
								for (SelectedValue selectedValue : questionnaireValue.getSelectedValues()) {
									if (selectedValue.getValue() != null) {
										if (selectedValue.getValue() != null
												& questionnaireValue.getQuestion().getID().equals(question.getID())) {
											questionElement.appendChild(
													XMLUtils.createElement("value", selectedValue.getValue(), doc));

										}

									}

								}

							}

							if (questionnaireValue.getActionRequired()) {
								actionRequired = questionnaireValue.getActionRequired();

							}

						}

					}

				}

				// TODO: need to implement file handling stuff
			} else if (question.getQuestionType().getQuestionType().equalsIgnoreCase("FILES")) {
				for (QuestionnaireValue questionnaireValue : questionnaireValueList) {
					if (questionnaireValue.getFile() != null) {
						System.out.println(questionnaireValue.getFile().getFileID());
						Element valueElement = doc.createElement("value");
						Element fileName = XMLUtils.createElement("fileName",
								questionnaireValue.getFile().getFileName(), doc);
						valueElement.appendChild(fileName);
						Element fileSize = XMLUtils.createElement("fileSize",
								questionnaireValue.getFile().getFileSize(), doc);
						valueElement.appendChild(fileSize);
						Element fileData = XMLUtils.createElement("fileData",
								questionnaireValue.getFile().getFileData(), doc);
						valueElement.appendChild(fileData);
						Element fileId = XMLUtils.createElement("fileId", questionnaireValue.getFile().getFileID(),
								doc);
						valueElement.appendChild(fileId);

						questionElement.appendChild(valueElement);

					}
				}

			} else {
				Element questionValuesElement = doc.createElement("QuestionOptions");
				questionElement.appendChild(questionValuesElement);
				if (question.getQuestionOptions() != null) {
					for (QuestionOption questionValue : question.getQuestionOptions()) {
						Element questionValueElement = doc.createElement("QuestionOption");

						questionValuesElement.appendChild(questionValueElement);

						questionValueElement.appendChild(XMLUtils.createElement("value", questionValue.getID(), doc));
						questionValueElement
								.appendChild(XMLUtils.createElement("name", questionValue.getOption(), doc));

						if (questionnaireValueList != null) {

							for (QuestionnaireValue questionnaireValue : questionnaireValueList) {
								if (questionnaireValue.getQuestion() != null
										&& questionnaireValue.getQuestion().getID().equals(question.getID())) {
									if (questionnaireValue.getNote() != null) {
										if (questionnaireValue.getNote().getNote().length() > 0
												|| questionnaireValue.getNote().getNote() != "") {
											note = questionnaireValue.getNote().getNote();

										}

									}

									if (questionnaireValue.getSelectedValues() != null) {
										for (SelectedValue selectedValue : questionnaireValue.getSelectedValues()) {
											if (selectedValue.getValue() != null) {
												if (questionValue.getID()
														.equals(Integer.parseInt(selectedValue.getValue()))) {
													Element selected = doc.createElement("selected");
													questionValueElement.appendChild(selected);

												}

											}

										}

									}

									if (questionnaireValue.getActionRequired()) {
										actionRequired = questionnaireValue.getActionRequired();

									}

								}
							}

						}

					}

				}

			}

			questionElement.appendChild(XMLUtils.createElement("note", note, doc));
			questionElement.appendChild(XMLUtils.createElement("ActionRequired", actionRequired, doc));
			questionElement
					.appendChild(XMLUtils.createElement("AttributeDetails", question.getAttributeDetails(), doc));

		}

	}

	/**
	 * Checks if question has already been handled and added to the value map
	 * 
	 * @param id
	 * @param valueMap
	 * @param found
	 * @return
	 */
	private QuestionnaireValue checkIfExist(Integer id, Map<Integer, QuestionnaireValue> valueMap, boolean found) {

		if (valueMap.containsKey(id)) {
			found = true;
			return valueMap.get(id);

		} else {
			return new QuestionnaireValue();

		}

	}

	private class Value {

		private List<SelectedValue> add(String[] params, QuestionnaireValue questionnaireValue, Question question) {
			List<SelectedValue> selectedValues = new ArrayList<>();

			questionnaireValue.setQuestion(question);

			for (String s : params) {
				SelectedValue sv = new SelectedValue();
				sv.setValue(s);
				sv.setQuestionnaireValue(questionnaireValue);

				selectedValues.add(sv);

			}

			return selectedValues;

		}

	}

	

}
