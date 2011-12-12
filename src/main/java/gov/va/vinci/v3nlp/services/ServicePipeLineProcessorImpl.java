/**
 * Copyright (C) 2011-
 *
 * All rights reserved.
 */
package gov.va.vinci.v3nlp.services;

import gov.va.vinci.cm.*;
import gov.va.vinci.v3nlp.Utilities;
import gov.va.vinci.v3nlp.model.BatchJobStatus;
import gov.va.vinci.v3nlp.model.CorpusSummary;
import gov.va.vinci.v3nlp.model.ServicePipeLine;
import gov.va.vinci.v3nlp.model.ServicePipeLineComponent;
import gov.va.vinci.v3nlp.registry.NlpComponent;
import gov.va.vinci.v3nlp.registry.RegistryService;
import gov.va.vinci.v3nlp.services.database.DatabaseRepositoryService;
import gov.va.vinci.v3nlp.services.database.V3nlpDBRepository;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.io.File;
import java.util.*;
import java.util.concurrent.*;

@Transactional
public class ServicePipeLineProcessorImpl extends BaseServicePipeLineProcessor {

    private String directoryToStoreResults;

    private static Log logger = LogFactory.getLog(ServicePipeLineProcessorImpl.class);

    private DatabaseRepositoryService databaseRepositoryService;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this. entityManager = entityManager;
    }


    @Override
    public void init() {
        if (!new File(getDirectoryToStoreResults()).exists()
                || !new File(getDirectoryToStoreResults()).isDirectory()) {
            throw new RuntimeException(getDirectoryToStoreResults()
                    + ": Is not a valid directory to store results.");
        }
    }

    @Override
    @Async
    @Transactional(readOnly = false)
    public void processPipeLine(String pipeLineId, ServicePipeLine pipeLine, Corpus corpus, BatchJobStatus jobStatus) {

        Corpus returnCorpus = corpus;
        Date startTime = new Date();
        logger.info("Begin pipeline processing [" + pipeLine.getPipeLineName() + "] Pipeline Processes: " + pipeLine.getNumberOfProcesses() + " for user: '" + pipeLine.getUserToken().trim() + "'");
        String pathOfResults = directoryToStoreResults + Utilities.getUsernameAsDirectory(pipeLine.getUserToken().trim());

        Map serviceMap = new HashMap<String, NlpComponent>();

        for (ServicePipeLineComponent c: pipeLine.getServices())
        {
            if (!GenericValidator.isBlankOrNull(c.getServiceUid())) {
                serviceMap.put(c.getServiceUid(), registryService.getNlpComponent(c.getServiceUid()));
            }
        }

        try {
            List<DocumentInterface> newDocuments = new ArrayList<DocumentInterface>();
            BlockingQueue<Runnable> queue = new ArrayBlockingQueue<Runnable>(corpus.getDocuments().size());

            int processes = pipeLine.getNumberOfProcesses();
            if (processes < 1) {
                processes = 1;
            }

            /** Create a thread pool for this processing. **/
            ThreadPoolExecutor executor = new ThreadPoolExecutor(processes,
                    processes,
                    60,
                    TimeUnit.SECONDS,
                    queue);

            List<Future<DocumentInterface>> futures = new ArrayList<Future<DocumentInterface>>();

            /** Put the documents on the queue for processing. **/
            for (DocumentInterface d : corpus.getDocuments()) {
                Future<DocumentInterface> f = executor.submit(new CallableDocumentServiceProcessor(pipeLine, d, serviceMap));
                futures.add(f);
            }


            /**
             * Determine if the queue is complete.
             * Loop through the futures, and if one is not done yet, wait for the result.
             * Repeat until all are complete.
             ***/
            boolean complete = false;
            while (!complete) {
                Thread.sleep(1000);
                complete = true;
                for (Future<DocumentInterface> future : futures) {
                    if (!future.isDone()) {
                        complete = false;
                    }
                }
            }

            /** Queue complete - get results **/
            for (Future<DocumentInterface> future : futures) {
                newDocuments.add(future.get());
            }

            /** Clean up executor. **/
            executor.purge();
            executor.shutdown();
            executor = null;

            returnCorpus.setDocuments(newDocuments);
            logger.info("End pipeline processing [" + pipeLine.getPipeLineName() + "] in " + (new Date().getTime() - startTime.getTime()) + "ms");

            returnCorpus = removeUnneededAnnotations(pipeLine, returnCorpus);

            // If writing to a database(s), loop through them and call the write method.
        /**    if (pipeLine.getSaveToRepositories() != null) {
                for (V3nlpDBRepository repo: pipeLine.getSaveToRepositories()) {
                    this.databaseRepositoryService.writeCorpus(returnCorpus, repo, Utilities.getUsername(pipeLine.getUserToken().trim()));
                }
            }
            // FIX THIS! Make it an else.    **/

            Utilities.serializeObject(pathOfResults + pipeLineId
                    + ".results", new CorpusSummary(returnCorpus));

            updatePipeLineStatus(jobStatus, "COMPLETE", pathOfResults + pipeLineId
                    + ".results");
        } catch (Exception e) {
            logger.error("Exception:" + e);
            e.printStackTrace();
            Utilities.serializeObject(pathOfResults + pipeLineId + ".err",
                    e);
            updatePipeLineStatus(jobStatus, "ERROR", pathOfResults + pipeLineId + ".err");
        } finally {
            new File(pathOfResults + pipeLineId + ".lck").delete();
        }

        return;
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

    protected static NlpProcessingUnit getService(ThreadPoolExecutor tpe, String bean) {
        return null;
    }
}
