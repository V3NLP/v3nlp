package gov.va.vinci.v3nlp.services.database;

import gov.va.vinci.cm.Document;
import gov.va.vinci.cm.DocumentInterface;
import gov.va.vinci.v3nlp.NlpUtilities;
import gov.va.vinci.v3nlp.model.datasources.DataServiceSource;
import org.apache.commons.validator.GenericValidator;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Mock-up for a database repository service for testing if the concept will work.
 * TODO: If this approach is taken, this code needs cleaned up.
 */
public class StubDatabaseRepositoryServiceImpl implements
        DatabaseRepositoryService {

    public List<V3nlpDBRepository> repositories = new ArrayList<V3nlpDBRepository>();

    private EntityManager entityManager;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public StubDatabaseRepositoryServiceImpl() {
        try {
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            Class.forName("com.mysql.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }


    public String test(V3nlpDBRepository ds, String loggedInUser) {
        List<DocumentInterface> results = new ArrayList<DocumentInterface>();
        Connection con = null;

        try {
            con = getConnection(ds, loggedInUser);
        } catch (SQLException e) {
            return "Could not get connection to database.";
        }

        Statement stmnt = null;
        try {
            stmnt = con.createStatement();
            stmnt.execute(ds.getCountSql());
        } catch (SQLException e) {
            return "Could not do select on data service. (" + e.getMessage() + ")";
        }

        try {
            con.close();
        } catch (Exception e) {

        }

        return "";
    }

    public List<V3nlpDBRepository> getRepositories() {
        return (List<V3nlpDBRepository>) entityManager.createQuery("select repo from gov.va.vinci.v3nlp.services.database.V3nlpDBRepository repo").getResultList();
    }

    protected Connection getConnection(V3nlpDBRepository ds, String loggedInUser) throws SQLException {
        Connection con = null;
        if ("com.microsoft.sqlserver.jdbc.SQLServerDriver".equals(ds.getDriverClassName())) {
            String connectionUrl = "jdbc:sqlserver://vhacdwdbs10;integratedSecurity=false;user=VinciUser;password=VinciUser;" + "database=" + ds.getSchema();
            con = DriverManager.getConnection(connectionUrl);
            con.createStatement().execute("EXECUTE AS LOGIN = '" + loggedInUser.trim().replace("'", "''").toLowerCase() + "';");
        } else if ("com.mysql.jdbc.Driver".equals(ds.getDriverClassName())) {
            con = DriverManager.getConnection(ds.getUrl());
        }

        return con;
    }

    public List<DocumentInterface> getDocuments(V3nlpDBRepository ds, String loggedInUser) {
        List<DocumentInterface> results = new ArrayList<DocumentInterface>();
        Connection con = null;
        try {
            con = getConnection(ds, loggedInUser);
            Statement stmnt = con.createStatement();

            ResultSet rs = stmnt.executeQuery(ds.getSelectSql());
            while (rs.next()) {
                Document d = new Document();
                d.setDocumentId("" + rs.getObject(1));
                d.setDocumentName(d.getDocumentId());
                d.setContent(rs.getString(2));
                results.add(d);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                con.close();
            } catch (Exception ex) {

            }
        }
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
