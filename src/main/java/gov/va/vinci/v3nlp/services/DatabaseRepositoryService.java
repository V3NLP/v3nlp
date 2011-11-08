package gov.va.vinci.v3nlp.services;

import gov.va.vinci.cm.DocumentInterface;
import gov.va.vinci.v3nlp.model.datasources.DataServiceSource;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseRepositoryService {

	public abstract List<String> getRespostoryNames();

	public abstract List<DocumentInterface> getDocuments(DataServiceSource m, String loggedInUser) throws SQLException;

}