/**
 * Copyright (C) 2011-
 *
 * All rights reserved.
 */
package gov.va.vinci.v3nlp.services.uima;

import gov.va.vinci.cm.Corpus;
import gov.va.vinci.cm.Document;
import gov.va.vinci.nlp.framework.marshallers.commonModel.util.ToCommonModel;
import gov.va.vinci.nlp.framework.marshallers.database.ToDatabase;
import org.apache.uima.aae.client.UimaAsBaseCallbackListener;
import org.apache.uima.cas.CAS;
import org.apache.uima.collection.EntityProcessStatus;
import org.apache.uima.resource.ResourceInitializationException;

import java.util.HashMap;
import java.util.logging.Logger;


public class SqlDatabaseListener extends UimaAsBaseCallbackListener {
    private static Logger log = Logger.getLogger(SqlDatabaseListener.class.getCanonicalName());
    private ToDatabase toDatabase;

    public SqlDatabaseListener() {
    }

    // Initialize the database
    public void initialize(HashMap<String, String> resultsSettingsMap,
                           String pIncludedLabels[], String pExcludedLabels[]) throws ResourceInitializationException {
        String jdbcDriver = resultsSettingsMap.get("jdbcDriver");
        String jdbcVendor = resultsSettingsMap.get("jdbcVendor");
        String hostNameAndPort = resultsSettingsMap.get("hostNameAndPort");
        String location = resultsSettingsMap.get("location");
        String databaseUserName = resultsSettingsMap.get("databaseUserName");
        String databasePassword = resultsSettingsMap.get("databasePassword");
        String databaseName = resultsSettingsMap.get("databaseName");
        String jdbcConnectURLBase = resultsSettingsMap.get("jdbcConnectURLBase"); // ignored
        String jdbcOptions = resultsSettingsMap.get("jdbcOptions"); // ignored
        String sequenceFile = resultsSettingsMap.get("sequenceFile");
        toDatabase = new ToDatabase();
        toDatabase.initialize(jdbcDriver, jdbcVendor, hostNameAndPort, location,
                databaseName, databaseUserName, databasePassword, jdbcConnectURLBase,
                jdbcOptions, sequenceFile, pIncludedLabels, pExcludedLabels);
    }


    @Override
    public void entityProcessComplete(CAS aCas, EntityProcessStatus aStatus) {

        log.info("===== Got CAS BACK, saving to database : " + aCas);
        try {
            toDatabase.process(aCas.getJCas());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void collectionProcessComplete(EntityProcessStatus aStatus) {
        log.info("===== Processing complete.");
    }//collectionProcessComplete method

}
