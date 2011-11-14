package gov.va.vinci.v3nlp.services.database;

import gov.va.research.v3nlp.repo.DBRepository;
import gov.va.vinci.cm.DocumentInterface;
import gov.va.vinci.v3nlp.model.datasources.DataServiceSource;

import java.sql.SQLException;
import java.util.List;

public interface DatabaseRepositoryService {

	public abstract List<String> getRespostoryNames();

	public abstract List<DocumentInterface> getDocuments(V3nlpDBRepository m, String loggedInUser) throws SQLException;

    public abstract List<V3nlpDBRepository> getRepositories();
}