/**
 * Copyright (C) 2011-
 *
 * All rights reserved.
 */
package gov.va.vinci.v3nlp.services.database;

import gov.va.vinci.cm.*;
import gov.va.vinci.v3nlp.NlpUtilities;
import gov.va.vinci.v3nlp.model.datasources.DataServiceSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.eclipse.jetty.jndi.java.javaNameParser;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.sql.*;
import java.util.*;
import java.util.Date;

/**
 * Mock-up for a database repository service for testing if the concept will work.
 *
 * This should be cleaned up a bit after schema is firmed up. (SQL Statements moved
 * to property files, broader database platform support, etc...
 *
 */
public class StubDatabaseRepositoryServiceImpl implements
        DatabaseRepositoryService {
    private static Log logger = LogFactory.getLog(StubDatabaseRepositoryServiceImpl.class);

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

    public String testForSave(V3nlpDBRepository ds, String loggedInUser) {
        Connection connection = null;
        SingleConnectionDataSource dataSource;

        try {
            try {
                connection = this.getConnection(ds, loggedInUser);
                dataSource = new SingleConnectionDataSource(connection, true);
                dataSource.setAutoCommit(false);
            } catch (Exception e) {
                return "Could not make connection to the database. (" + e.getMessage() + ")";
            }
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);

            // Do selects to make sure the schema is ready to insert into.
            jdbcTemplate.execute("select id, reference_location, version from analyte_reference");
            jdbcTemplate.execute("select id, name, version from annotation");
            jdbcTemplate.execute("select id, annotation_id, analyte_reference_id, version from annotation_analyte_reference");
            jdbcTemplate.execute("select id, annotation_analyte_ref_id, start_offset, end_offset, version from span");
            jdbcTemplate.execute("select id, annotation_id, name, parent_id, pedigree, version from feature");
            jdbcTemplate.execute("select id, name, text_value, feature_id, version from feature_element_text");
            jdbcTemplate.execute("select id, name, numeric_value, feature_id, version from feature_element_numeric");
            jdbcTemplate.execute("select id, name, blob_value, feature_id, version from feature_element_blob");
        } catch (Exception e) {
            return "Validation failed. Schema is not correct. (" + e.getMessage() + ")";
        }

        return "";
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
            stmnt.execute(ds.getCheckSql());
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
            String connectionUrl = ds.getUrl() + ds.getSchema();
            con = DriverManager.getConnection(connectionUrl);
            con.createStatement().execute("EXECUTE AS LOGIN = '" + loggedInUser.trim().replace("'", "''").toLowerCase() + "';");
        } else if ("com.mysql.jdbc.Driver".equals(ds.getDriverClassName())) {
            String connectURL = ds.getUrl().replace("${schema}", ds.getSchema());
            con = DriverManager.getConnection(connectURL);
        }

        return con;
    }

    public List<DocumentInterface> getDocuments(V3nlpDBRepository ds, String loggedInUser) {
        List<DocumentInterface> results = new ArrayList<DocumentInterface>();
        Connection con = null;
        try {
            con = getConnection(ds, loggedInUser);
            Statement statement = con.createStatement();

            ResultSet rs = statement.executeQuery(ds.getSelectSql());
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

    public void writeCorpus(Corpus c, V3nlpDBRepository ds, String loggedInUser) {
        Connection connection = null;
        try {
            connection = this.getConnection(ds, loggedInUser);

            SingleConnectionDataSource dataSource = new SingleConnectionDataSource(connection, true);
            dataSource.setAutoCommit(false);
            JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);


            // FIX THIS! - MYSQL Specific currently.
            jdbcTemplate.execute("START TRANSACTION");
            for (DocumentInterface d : c.getDocuments()) {
                // Step 1 - Write the Analyte Reference
                // FIX THIS - Creating UUID's for objects here, should be done before now, but common model currently
                // uses numbers for id's instead of strings.
                String documentUUID = UUID.randomUUID().toString();
                jdbcTemplate.update("insert into analyte_reference (id, reference_location, version) values (?, ?, ?)", new Object[]{documentUUID, d.getDocumentName(), new java.util.Date()});

                // Step 2 - Annotations, Spans, and Annotation/Analyte References
                for (AnnotationInterface a : d.getAnnotations().getAll()) {
                    String annotationUUID = UUID.randomUUID().toString();
                    jdbcTemplate.update("insert into annotation (id, name, version) values (?, ?, ?)", new Object[]{annotationUUID, a.getOffsetKey(), new java.util.Date()});
                    String aarUID = UUID.randomUUID().toString();
                    jdbcTemplate.update("insert into annotation_analyte_reference (id, annotation_id, analyte_reference_id, version) values (?, ?, ?, ?)", new Object[]{aarUID, annotationUUID, documentUUID, new java.util.Date()});
                    jdbcTemplate.update("insert into span (id, annotation_analyte_ref_id, start_offset, end_offset, version) values (?, ?, ?, ?, ?)", new Object[]{UUID.randomUUID().toString(), aarUID, a.getBeginOffset(), a.getEndOffset(), new java.util.Date()});

                    // Step 3 - Features
                    for (Feature f : a.getFeatures()) {
                        String featureUUID = UUID.randomUUID().toString();
                        String insertFeatureSQL = "insert into feature(id, annotation_id, name, parent_id, pedigree, version) values (?, ?, ?, ?, ?, ?)";
                        jdbcTemplate.update(insertFeatureSQL, new Object[]{featureUUID, annotationUUID, f.getFeatureName(), null, f.getMetaData().getPedigree(), new java.util.Date()});

                        // Step 4 - Feature Elements.
                        for (FeatureElement fe : f.getFeatureElements()) {
                            if (fe.getName() == null && fe.getValue() == null) {
                                continue;
                            }

                            if (fe.getValue() instanceof String) {
                                jdbcTemplate.update("insert into feature_element_text (id, name, text_value, feature_id, version) values (?, ?, ?, ?, ?)",
                                        new Object[]{UUID.randomUUID().toString(), fe.getName(), fe.getValue(), featureUUID, new java.util.Date()});
                            } else if (fe.getValue() instanceof Number) {
                                jdbcTemplate.update("insert into feature_element_numeric (id, name, numeric_value, feature_id, version) values (?, ?, ?, ?, ?)",
                                        new Object[]{UUID.randomUUID().toString(), fe.getName(), fe.getValue(), featureUUID, new java.util.Date()});
                            } else {
                                jdbcTemplate.update("insert into feature_element_blob (id, name, blob_value, feature_id, version) values (?, ?, ?, ?, ?)",
                                        new Object[]{UUID.randomUUID().toString(), fe.getName(), fe.getValue(), featureUUID, new java.util.Date()});
                            }
                        }
                    }

                }
            }

            jdbcTemplate.execute("COMMIT");

        } catch (SQLException e) {
            logger.error(e);
            throw new RuntimeException(e);
        } finally {
            try {
                connection.close();
            } catch (Exception e) {

            }
        }
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
