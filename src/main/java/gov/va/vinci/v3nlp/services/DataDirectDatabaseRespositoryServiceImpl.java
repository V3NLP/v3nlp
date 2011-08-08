package gov.va.vinci.v3nlp.services;

import com.ddtek.jdbc.extensions.ExtConnection;
import gov.va.research.v3nlp.repo.DBRepository;
import gov.va.vinci.cm.Annotations;
import gov.va.vinci.cm.Document;
import gov.va.vinci.cm.DocumentInterface;
import gov.va.vinci.cm.FormatInfo;
import gov.va.vinci.v3nlp.NlpUtilities;
import gov.va.vinci.v3nlp.model.datasources.DataServiceSource;
import org.apache.commons.validator.GenericValidator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class DataDirectDatabaseRespositoryServiceImpl implements
        DatabaseRepositoryService {

    public List<DBRepository> repositories;

    public DataDirectDatabaseRespositoryServiceImpl() {
        try {
            Class.forName("com.ddtek.jdbc.sqlserver.SQLServerDriver");
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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

        // Validate the dataservicesource. Throws an illegalarguement exception if there is a problem.
        validateDataServiceSource(ds);

        List<DocumentInterface> reportList = (ds.getNumberOfDocuments() != null && ds
                .getNumberOfDocuments() > 0) ? new ArrayList<DocumentInterface>(
                ds.getNumberOfDocuments())
                : new ArrayList<DocumentInterface>();

        Connection conn = null;

        try {
            // Create the query
            String query = "select " + ds.getIdColumn() + " as uid, " + ds.getTextColumn() +
                    " as text from " + ds.getTable() + " limit " + ds.getNumberOfDocuments();

            // Create the connection.
            conn = DriverManager.getConnection("jdbc:datadirect:sqlserver://" + ds.getDatabaseServer() +
                    ";databaseName=" + ds.getDatabase());

            // Impersonate the user.
            ((ExtConnection) conn).setCurrentUser(ds.getDataServiceUsername());

            // Run the query.
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(query);
            while (rs.next()) {
                reportList.add(new Document("Repository: "
                        + rs.getString("uid"), rs.getString("uid"), null,
                        new ArrayList<FormatInfo>(), rs.getString("text"),
                        new Annotations()));
            }
            stmt.close();

        } finally {
            try {
                conn.close();
            } catch (Exception e) {
            }
        }

        return reportList;
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
