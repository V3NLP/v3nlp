/**
 * Copyright (C) 2011-
 *
 * All rights reserved.
 */
package gov.va.vinci.v3nlp.services;


import gov.va.vinci.cm.Corpus;
import gov.va.vinci.flap.Client;
import gov.va.vinci.flap.Service;
import gov.va.vinci.flap.cr.SuperReader;
import gov.va.vinci.flap.descriptors.CollectionReaderFactory;
import gov.va.vinci.v3nlp.Utilities;
import gov.va.vinci.v3nlp.model.BatchJobStatus;
import gov.va.vinci.v3nlp.model.CorpusSummary;
import gov.va.vinci.v3nlp.model.ServicePipeLine;
import gov.va.vinci.v3nlp.model.ServicePipeLineComponent;
import gov.va.vinci.v3nlp.registry.NlpComponent;
import gov.va.vinci.v3nlp.services.database.DatabaseRepositoryService;
import gov.va.vinci.v3nlp.services.uima.CorpusSubReader;
import gov.va.vinci.v3nlp.services.uima.CorpusUimaAsCallbackListener;
import gov.va.vinci.v3nlp.services.uima.SqlDatabaseListener;
import org.apache.uima.aae.client.UimaAsBaseCallbackListener;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.logging.Logger;

@Transactional
public class UimaServicePipeLineProcessorImpl extends BaseServicePipeLineProcessor {
    private String flapPropertiesFile;
    private String directoryToStoreResults;
    private static Logger log = Logger.getLogger(UimaServicePipeLineProcessorImpl.class.getCanonicalName());
    private String corpusSuperReaderDescriptorPath;
    private DatabaseRepositoryService databaseRepositoryService;

    @Override
    public void init() {

    }

    @Override
    public void processPipeLine(String pipeLineId, ServicePipeLine pipeLine,
                                Corpus corpus, BatchJobStatus jobStatus) {
        Service service = null;
        HashMap<String, String> resultsSettingsMap = getResultsSettingsMapFromList(pipeLine.getResultsSettings());

        String pathOfResults = directoryToStoreResults + Utilities.getUsernameAsDirectory(pipeLine.getUserToken().trim());
        if ((resultsSettingsMap.get("type")).equalsIgnoreCase("Directory")) {
            if (resultsSettingsMap.get("selectedDirectory") != null) {
                pathOfResults = resultsSettingsMap.get("selectedDirectory") +
                        Utilities.getUsernameAsDirectory(pipeLine.getUserToken().trim());
            }
        }

        try {
            // Set up the FLAP Server
            SetUpFlapServerWithAnnotatorsAndServiceDescriptors(pipeLine);

            // Create the listener based on the result settings
            UimaAsBaseCallbackListener callbackListener;
            if ((resultsSettingsMap.get("type")).equalsIgnoreCase("Directory")) {
                log.info("Setting up results to be saved into a directory.");
                callbackListener = new CorpusUimaAsCallbackListener();
            } else if ((resultsSettingsMap.get("type")).equalsIgnoreCase("Database")) {
                log.info("Setting up results to be saved into a database.");
                String pIncludedLabels[] = null;
                String pExcludedLabels[] = null;
                // String pExcludedLabels[] = (String[]) getAnnotationsToRemove(pipeLine).toArray();

                callbackListener = new SqlDatabaseListener();
                ((SqlDatabaseListener) callbackListener).initialize(resultsSettingsMap, pIncludedLabels, pExcludedLabels);
            } else {
                throw new RuntimeException("Unknown results settings");
            }

            // Generate the client
            Client myClient = new Client(flapPropertiesFile);


            //Create the CollectionReader & add a Corpus SubReader
            CorpusSubReader subReader = new CorpusSubReader(corpus);
            SuperReader myReader = (SuperReader) CollectionReaderFactory.
                    generateSubReader(new HashMap<String, String>(), "gov.va.vinci.v3nlp.services.uima.CorpusSubReader");
            myReader.setSubReader(subReader);

            //Execute the pipeline with the collection reader
            myClient.run(myReader, callbackListener);

            // If it is a file, get the corpus and remove the unneeded annotations
            if ((resultsSettingsMap.get("type")).equalsIgnoreCase("Directory")) {
                Corpus c = ((CorpusUimaAsCallbackListener) callbackListener).getCorpus();
                myClient = null;
                c = removeUnneededAnnotations(pipeLine, c);
                log.info("Saving the pipeline to: " +
                        pathOfResults + pipeLineId + ".results");
                Utilities.serializeObject(pathOfResults + pipeLineId
                        + ".results", new CorpusSummary(c));
            }

            updatePipeLineStatus(jobStatus, "COMPLETE", pathOfResults + pipeLineId
                    + ".results");
        } catch (Exception e) {
            e.printStackTrace();
            Utilities.serializeObject(pathOfResults + pipeLineId + ".err",
                    e);
            updatePipeLineStatus(jobStatus, "ERROR", pathOfResults + pipeLineId + ".err");
        } finally {
            new File(pathOfResults + pipeLineId + ".lck").delete();
        }
    }

    private void SetUpFlapServerWithAnnotatorsAndServiceDescriptors(ServicePipeLine pipeLine) throws Exception {
        Service flapServer;
        
        // Log UIMA_HOME env variable
        log.info("Environment Variable UIMA_HOME:" + System.getenv("UIMA_HOME"));

        //Create the server object
        flapServer = new Service(flapPropertiesFile);

        //Initialize the server object with the list of annotators
        ArrayList<String> descriptors = new ArrayList<String>();


        // Add the service descriptors.
        for (ServicePipeLineComponent comp : pipeLine.getServices()) {
            if (comp.getServiceUid() == null) { // Ignore any UI / non-server side components.
                continue;
            }

            NlpComponent nlpComp = registryService.getNlpComponent(comp.getServiceUid());
            log.info("Adding descriptor:" + nlpComp.getImplementationClass());
            descriptors.add(nlpComp.getImplementationClass());
        }

        flapServer.deploy(descriptors, true);
    }

    private HashMap<String, String> getResultsSettingsMapFromList(List<String> resultSettings) {
        HashMap<String, String> settingsMap = new HashMap<String, String>();
        for (String setting : resultSettings) {
            int pos = setting.indexOf("|");
            String key = setting.substring(0, pos);
            String value = setting.substring(pos + 1);
            if (value != null && value.equals("")) {
                value = null;
            }
            settingsMap.put(key, value);
        }
        return settingsMap;
    }

    public void setFlapPropertiesFile(String fpf) {
        this.flapPropertiesFile = fpf;
    }

    public void setCorpusSuperReaderDescriptorPath(String path) {
        corpusSuperReaderDescriptorPath = path;
    }

    @Override
    public String getDirectoryToStoreResults() {
        return directoryToStoreResults;
    }

    @Override
    public void setDirectoryToStoreResults(String directoryToStoreResults) {
        this.directoryToStoreResults = directoryToStoreResults;
    }

    public void setDatabaseRepositoryService(DatabaseRepositoryService databaseRepositoryService) {
        this.databaseRepositoryService = databaseRepositoryService;
    }
}