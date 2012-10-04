/**
 * Copyright (C) 2011-
 *
 * All rights reserved.
 */
package gov.va.vinci.v3nlp.services;


import gov.va.vinci.cm.Corpus;
import gov.va.vinci.flap.cr.SuperReader;
import gov.va.vinci.flap.descriptors.CollectionReaderFactory;
import gov.va.vinci.flap.Client;
import gov.va.vinci.flap.Service;
import gov.va.vinci.v3nlp.Utilities;
import gov.va.vinci.v3nlp.model.BatchJobStatus;
import gov.va.vinci.v3nlp.model.CorpusSummary;
import gov.va.vinci.v3nlp.model.ServicePipeLine;
import gov.va.vinci.v3nlp.model.ServicePipeLineComponent;
import gov.va.vinci.v3nlp.registry.NlpComponent;
import gov.va.vinci.v3nlp.services.database.DatabaseRepositoryService;
import gov.va.vinci.v3nlp.services.uima.CorpusSubReader;
import gov.va.vinci.v3nlp.services.uima.CorpusUimaAsCallbackListener;
import org.apache.uima.collection.CollectionReader;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
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
    public void processPipeLine(String pipeLineId, ServicePipeLine pipeLine, Corpus corpus, BatchJobStatus jobStatus) {
        String pathOfResults = directoryToStoreResults + Utilities.getUsernameAsDirectory(pipeLine.getUserToken().trim());

        Service service = null;

        try {
            // Log UIMA_HOME env variable
            log.info("Environment Variable UIMA_HOME:" + System.getenv("UIMA_HOME"));

            //Create the server object
            service = new Service(flapPropertiesFile);

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

            service.deploy(descriptors, true);

            //Create the listener and generate the client
            CorpusUimaAsCallbackListener callbackListener = new CorpusUimaAsCallbackListener();
            Client myClient = new Client(flapPropertiesFile);

            //Create the CollectionReader & add a Corpus SubReader
            CorpusSubReader subReader = new CorpusSubReader(corpus);
            //SuperReader myReader = new SuperReader();
            SuperReader myReader = (SuperReader)CollectionReaderFactory.
                    generateSubReader(new HashMap<String,String>(),"gov.va.vinci.v3nlp.services.uima.CorpusSubReader");
            myReader.setSubReader(subReader);

            //Execute the pipeline with the collection reader
            myClient.run(myReader, callbackListener);

            Corpus c = callbackListener.getCorpus();

            myClient = null;

            c = removeUnneededAnnotations(pipeLine, c);
            Utilities.serializeObject(pathOfResults + pipeLineId
                    + ".results", new CorpusSummary(c));
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