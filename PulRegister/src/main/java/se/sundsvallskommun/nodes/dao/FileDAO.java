package se.sundsvallskommun.nodes.dao;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import javax.sql.DataSource;

import se.sundsvallskommun.nodes.model.File;
import se.sundsvallskommun.nodes.model.QuestionnaireValue;
import se.unlogic.standardutils.dao.AnnotatedDAO;
import se.unlogic.standardutils.dao.AnnotatedDAOFactory;
import se.unlogic.standardutils.dao.LowLevelQuery;

public class FileDAO<T> extends AnnotatedDAO<File> {

	private DataSource dataSource;
	private static FileDAO<?> fileDAO;
	private AnnotatedDAOFactory daoFactory;
	private AnnotatedDAO<QuestionnaireValue> questionnaireValueDAO;
	
	public FileDAO(DataSource dataSource, Class<File> beanClass, AnnotatedDAOFactory daoFactory, AnnotatedDAO<QuestionnaireValue> questionnaireValueDAO) {
		super(dataSource, beanClass, daoFactory);
		this.dataSource = dataSource;
		this.fileDAO = this;
		this.daoFactory = daoFactory;
		this.questionnaireValueDAO = questionnaireValueDAO;
	}
	
	public static FileDAO<?> getInstance() {
		if (fileDAO != null)
			return fileDAO;
		else {
			System.out.println("NO FILEDAO INSTANCE AVAILABLE");
			return null;
		}
	}

	public void deleteFile(int fileId) throws SQLException {
		LowLevelQuery<File> fileQuery = new LowLevelQuery<>();
		fileQuery.setSql("SELECT * FROM form_file WHERE file_id = " + fileId);
		File file  = fileDAO.get(fileQuery);
		fileDAO.delete(file, dataSource.getConnection());
	}

	public Integer getFileIdFromQuestionnaireId(int questionnaireId) throws SQLException {
		LowLevelQuery<QuestionnaireValue> query = new LowLevelQuery<>();
		query.setSql("SELECT * FROM form_questionnaire_value WHERE questionnaire_id = "
				+ questionnaireId + " AND question_id = 53");
		QuestionnaireValue  questionnaireValue = questionnaireValueDAO.get(query);
		if(questionnaireValue != null) {
			return questionnaireValue.getID();	
		}
		else {
			return null;
		}
	}

	

	public File getFile(int fileId) throws SQLException {
		LowLevelQuery<File> fileQuery = new LowLevelQuery<>();
		fileQuery.setSql("SELECT * FROM form_file WHERE file_id = " + fileId);
		return fileDAO.get(fileQuery);
	}

	public void updateFileId(int questionnaireId, int oldFileId) throws SQLException {
		int fileId = this.getFileIdFromQuestionnaireId(questionnaireId);
		LowLevelQuery<File> updateQuery = new LowLevelQuery<>();
		updateQuery.setSql("UPDATE form_file SET file_id = " + fileId + " WHERE file_id = " + oldFileId);;
		fileDAO.update(updateQuery);
	}
	
}
