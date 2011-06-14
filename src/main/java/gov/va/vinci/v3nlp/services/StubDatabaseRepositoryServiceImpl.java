package gov.va.vinci.v3nlp.services;

import gov.va.research.v3nlp.repo.DBRepository;
import gov.va.vinci.cm.DocumentInterface;
import gov.va.vinci.v3nlp.NlpUtilities;
import gov.va.vinci.v3nlp.model.datasources.DataServiceSource;
import org.apache.commons.validator.GenericValidator;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class StubDatabaseRepositoryServiceImpl implements
        DatabaseRepositoryService {

    public List<DBRepository> repositories;

    public StubDatabaseRepositoryServiceImpl() {

    }

    public List<DBRepository> getRepositories() {
        return repositories;
    }

    public void setRepositories(List<DBRepository> repositories) {
        this.repositories = repositories;
    }


    /*
      * (non-Javadoc)
      *
      * @see gov.va.vinci.v3nlp.DatabaseRepositoryService#getRespostoryNames()
      */
    public List<String> getRespostoryNames() {
        List<String> results = new ArrayList<String>();

        for (DBRepository d : repositories) {
            results.add(d.getName());
        }
        return results;
    }



    public List<DocumentInterface> getDocuments(DataServiceSource ds)
            throws SQLException {

        return null;
    }

    private void validateDataServiceSource(DataServiceSource ds) {
        if (GenericValidator.isBlankOrNull(ds.getDatabase()) || !NlpUtilities.isValidNameNumbersAndCharacters(ds.getDatabase())) {
            throw new IllegalArgumentException("Database name is required, and must be only characters, numbers, and underscores. (DB Name provided='" + ds.getDatabase() + "'");
        }
        if (GenericValidator.isBlankOrNull(ds.getDatabaseServer())) {
            throw new IllegalArgumentException("Database server is required, and can be in the format <server>:<port>.");
        }
        if (GenericValidator.isBlankOrNull(ds.getDataServiceUsername())) {
            throw new IllegalArgumentException("Database serverice username is required.");
        }
        if (GenericValidator.isBlankOrNull(ds.getTable()) || !NlpUtilities.isValidNameNumbersAndCharacters(ds.getTable())) {
             throw new IllegalArgumentException("Database table is required, and must be only characters, numbers, and underscores.");
        }
        if (GenericValidator.isBlankOrNull(ds.getTextColumn()) || !NlpUtilities.isValidNameNumbersAndCharacters(ds.getTextColumn())) {
             throw new IllegalArgumentException("Text column is required, and must be only characters, numbers, and underscores.");
        }
        if (GenericValidator.isBlankOrNull(ds.getIdColumn()) || !NlpUtilities.isValidNameNumbersAndCharacters(ds.getIdColumn())) {
             throw new IllegalArgumentException("Text column is required, and must be only characters, numbers, and underscores.");
        }
    }
}
