package gov.va.research.inlp.services;

import gov.va.research.inlp.model.datasources.DataServiceSource;
import gov.va.vinci.cm.DocumentInterface;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseRepositoryService {

	public abstract List<String> getRespostoryNames();

	public abstract List<DocumentInterface> getDocuments(DataServiceSource m) throws SQLException;

}