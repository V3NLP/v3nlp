package gov.va.vinci.v3nlp.services;

import gov.va.vinci.cm.Corpus;
import gov.va.vinci.v3nlp.model.BatchJobStatus;
import gov.va.vinci.v3nlp.model.ServicePipeLine;
import gov.va.vinci.v3nlp.registry.RegistryService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

public interface ServicePipeLineProcessor {
    void init();

    /**
     * Process the pipeline. Results or exceptions are serialized in the users directory. (pipeline.usertoken)
     *
     * @param pipeLineId the id of this job.
     * @param pipeLine   the pipeline definition to process.
     * @param corpus     the corpus to process.
     */
    @Async
    @Transactional(readOnly = true)
    void processPipeLine(String pipeLineId, ServicePipeLine pipeLine, Corpus corpus, BatchJobStatus jobStatus);

    String getDirectoryToStoreResults();

    void setDirectoryToStoreResults(String directoryToStoreResults);

    void setRegistryService(RegistryService registryService);


}
