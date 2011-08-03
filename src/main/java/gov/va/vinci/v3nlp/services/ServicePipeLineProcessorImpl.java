package gov.va.vinci.v3nlp.services;

import gov.va.vinci.cm.*;
import gov.va.vinci.v3nlp.Utilities;
import gov.va.vinci.v3nlp.model.CorpusSummary;
import gov.va.vinci.v3nlp.model.ServicePipeLine;
import gov.va.vinci.v3nlp.model.ServicePipeLineComponent;
import gov.va.vinci.v3nlp.registry.NlpComponent;
import gov.va.vinci.v3nlp.registry.NlpComponentProvides;
import gov.va.vinci.v3nlp.registry.RegistryService;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.validator.GenericValidator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;


public class ServicePipeLineProcessorImpl implements ServicePipeLineProcessor {

    private RegistryService registryService;

    private String directoryToStoreResults;

    private static Log logger = LogFactory.getLog(ServicePipeLineProcessorImpl.class);

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
    @Transactional(readOnly = true)
    public void processPipeLine(String pipeLineId, ServicePipeLine pipeLine, Corpus corpus) {
	
        Corpus returnCorpus = corpus;
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
            logger.info("End pipeline processing [" + pipeLine.getPipeLineName() + "]");

            returnCorpus = removeUnneededAnnotations(pipeLine, returnCorpus);

            serializeObject(pathOfResults + pipeLineId
                    + ".results", new CorpusSummary(returnCorpus));
        } catch (Exception e) {
            logger.error("Exception:" + e);
            e.printStackTrace();
            serializeObject(pathOfResults + pipeLineId + ".err",
                    e);
        } finally {
            new File(pathOfResults + pipeLineId + ".lck").delete();
        }

        return;
    }

    /**
     * Given the services in the pipeline, remove all unneeded annotations where the
     * service component "keepAnnotationInFinalResult" is false.
     *
     * @param pipeLine
     * @param returnCorpus
     * @return
     */
    private Corpus removeUnneededAnnotations(ServicePipeLine pipeLine, Corpus returnCorpus) {
        List<String> toRemove = new ArrayList<String>();

        /** Get a list of annotation types to remove. **/
        for (ServicePipeLineComponent comp : pipeLine.getServices()) {
            if (GenericValidator.isBlankOrNull(comp.getServiceUid())) {
                continue;
            }
            if (!comp.isKeepAnnotationsInFinalResult()) {
                NlpComponent loadedComp = registryService.getNlpComponent(comp.getServiceUid());
                for (NlpComponentProvides provided : loadedComp.getProvides()) {
                    toRemove.add(provided.getName());
                }
            }
        }

        /** Go through all the documents and remove those that are not needed. **/
        for (DocumentInterface d : returnCorpus.getDocuments()) {
            List<AnnotationInterface> anns = d.getAnnotations().getAll();
            boolean keep = false;
            for (int i = anns.size() - 1; i >= 0; i--) {
                keep = false;
                Annotation a = (Annotation) anns.get(i);
                List<Feature> toBeRemoved = new ArrayList<Feature>();
                if (a.getFeatures() != null) {
                    for (Feature f : a.getFeatures()) {
                        if (f.getFeatureName() != null) {
                            if (toRemove.contains(f.getFeatureName())) {
                                toBeRemoved.add(f);
                            }
                        }
                    }
                    a.getFeatures().removeAll(toBeRemoved);
                    if (a.getFeatures().size() == 0) {
                        anns.remove(a);
                    }

                }
            }
        }
        return returnCorpus;
    }


    private void serializeObject(String path, Object e) {
        OutputStream os;
        try {
            os = new FileOutputStream(path);
            ObjectOutput oo = new ObjectOutputStream(os);
            oo.writeObject(e);
            oo.close();
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    @Override
    public String getDirectoryToStoreResults() {
        return directoryToStoreResults;
    }

    @Override
    public void setDirectoryToStoreResults(String directoryToStoreResults) {
        this.directoryToStoreResults = directoryToStoreResults;
    }


    @Override
    public void setRegistryService(RegistryService registryService) {
        this.registryService = registryService;
    }


    protected static NlpProcessingUnit getService(ThreadPoolExecutor tpe, String bean) {
        return null;
    }
}
