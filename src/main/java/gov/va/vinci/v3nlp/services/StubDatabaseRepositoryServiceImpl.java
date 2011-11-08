package gov.va.vinci.v3nlp.services;

import gov.va.research.v3nlp.repo.DBRepository;
import gov.va.vinci.cm.Document;
import gov.va.vinci.cm.DocumentInterface;
import gov.va.vinci.v3nlp.NlpUtilities;
import gov.va.vinci.v3nlp.model.datasources.DataServiceSource;
import org.apache.commons.validator.GenericValidator;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Mock-up for a database repository service for testing if the concept will work.
 * TODO: If this approach is taken, this code needs cleaned up.
 */
public class StubDatabaseRepositoryServiceImpl implements
        DatabaseRepositoryService {

    public List<DBRepository> repositories;

    public StubDatabaseRepositoryServiceImpl() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        } catch (ClassNotFoundException e) {
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


    public List<DocumentInterface> getDocuments(DataServiceSource ds, String loggedInUser)
            throws SQLException {

        List<DocumentInterface> results = new ArrayList<DocumentInterface>();
        String connectionUrl = "jdbc:sqlserver://vhacdwdbs10;database=" + ds.getDatabase() + ";integratedSecurity=false;user=VinciUser;password=VinciUser;";

        Connection con = DriverManager.getConnection(connectionUrl);
        Statement stmnt = con.createStatement();

        /** Execute as changes effective user to currently logged in user. **/
        String executeAs = "EXECUTE AS LOGIN = '" + loggedInUser.trim().replace("'", "''").toLowerCase() + "';";
        stmnt.execute(executeAs);

        String selectQuery = "select " + ds.getIdColumn().replace("'", "''") + ", " +
                ds.getTextColumn().replace("'", "''")
                + ", ROW_NUMBER() OVER (ORDER BY " + ds.getIdColumn().replace("'", "''") + ") as row " +
                " from " + ds.getTable().replace("'", "''");


        String limitQuery = "select * from (" + selectQuery + ") a where row <= " + ds.getNumberOfDocuments();
        ResultSet rs = stmnt.executeQuery(limitQuery);
        while (rs.next()) {
            Document d= new Document();
            d.setDocumentId("" + rs.getObject(1));
            d.setDocumentName(d.getDocumentId());
            d.setContent(rs.getString(2));
            results.add(d);
        }

        /** Reverts back to initial connection user. **/
        stmnt.execute("revert;");
        return results;
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
