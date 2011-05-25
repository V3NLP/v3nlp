package gov.va.vinci.v3nlp.services;

import gov.va.vinci.cm.Corpus;
import gov.va.vinci.v3nlp.model.ServicePipeLine;
import gov.va.vinci.v3nlp.registry.RegistryService;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;


public interface ServicePipeLineProcessor {
    void init();

    @Async
    @Transactional(readOnly = true)
    void processPipeLine(String pipeLineId, ServicePipeLine pipeLine, Corpus corpus);

    String getDirectoryToStoreResults();

    void setDirectoryToStoreResults(String directoryToStoreResults);

    void setRegistryService(RegistryService registryService);
}
