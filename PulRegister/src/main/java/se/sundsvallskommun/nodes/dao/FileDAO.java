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

	public int getOldFileId(int questionnaireId) throws SQLException {
		LowLevelQuery<QuestionnaireValue> query = new LowLevelQuery<>();
		query.setSql("SELECT * FROM form_questionnaire_value WHERE questionnaire_id = "
				+ questionnaireId + " AND question_id = 53");
		QuestionnaireValue  questionnaireValue = questionnaireValueDAO.get(query);
		return questionnaireValue.getID();
	}

	

	public File getFile(int fileId) throws SQLException {
		
		LowLevelQuery<File> fileQuery = new LowLevelQuery<>();
		fileQuery.setSql("SELECT * FROM form_file WHERE file_id = " + fileId);
		return fileDAO.get(fileQuery);
		

//		File file = new File();
//
//		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/register", "root", "root");
//
//		Statement stmt = con.createStatement();
//
//		ResultSet rs = stmt.executeQuery("SELECT * FROM form_file WHERE file_id = " + fileId);
//
//		if (rs.next()) {
//			file.setFileID(rs.getInt(1));
//			file.setFileData(rs.getBlob(2));
//			file.setFileName(rs.getString(3));
//			file.setDateAdded(rs.getTimestamp(4));
//			file.setFileSize(rs.getLong(5));
//		} else {
//			file = null;
//		}
//
//		rs.close();
//		stmt.close();
//		con.close();
//		return file;
	}

	public void updateFileId(int questionnaireId, int oldFileId) throws SQLException {

		Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/register", "root", "root");

		Statement stmt = con.createStatement();

		ResultSet rs = stmt.executeQuery(
				"SELECT questionnaire_value_id FROM form_questionnaire_value WHERE question_id = 53 AND questionnaire_id = "
						+ questionnaireId);
		if (rs.next()) {
			System.out.println(rs.getInt(1) + " EFTER");

			stmt.execute("UPDATE form_file SET file_id = " + rs.getInt(1) + " WHERE file_id = " + oldFileId);
		}

		rs.close();
		stmt.close();
		con.close();
	}
	
	

}
