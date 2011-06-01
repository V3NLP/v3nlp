package gov.va.vinci.v3nlp.services;

import gov.va.vinci.cm.*;
import gov.va.vinci.v3nlp.StaticApplicationContext;
import gov.va.vinci.v3nlp.Utilities;
import gov.va.vinci.v3nlp.model.CorpusSummary;
import gov.va.vinci.v3nlp.model.ServicePipeLine;
import gov.va.vinci.v3nlp.model.ServicePipeLineComponent;
import gov.va.vinci.v3nlp.registry.NlpComponent;
import gov.va.vinci.v3nlp.registry.NlpComponentProvides;
import gov.va.vinci.v3nlp.registry.RegistryService;
import org.apache.commons.validator.GenericValidator;
import org.springframework.scheduling.annotation.Async;
import org.springframework.transaction.annotation.Transactional;

import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;


public class ServicePipeLineProcessorImpl implements ServicePipeLineProcessor {

    private RegistryService registryService;

    private String directoryToStoreResults;

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
        String pathOfResults = directoryToStoreResults + Utilities.getUsernameAsDirectory(pipeLine.getUserToken());
        System.out.println("Begin pipeline processing [" + pipeLine.getPipeLineName() + "] at " + new Date());
        try {

            List<DocumentInterface> newDocuments = new ArrayList<DocumentInterface>();
            for (DocumentInterface d : returnCorpus.getDocuments()) {
                for (ServicePipeLineComponent comp : pipeLine.getServices()) {
                    if (comp.getServiceUid() == null) {
                        continue;
                    }
                    System.out.println("\t\t[ " + pipeLine.getPipeLineName() + "~~" + d.getDocumentName() + " ] Component:" + comp.getServiceUid() + " Starting: " + new Date() + " Keep in final result:" + comp.isKeepAnnotationsInFinalResult());
                    NlpProcessingUnit bean = StaticApplicationContext.getApplicationContext().getBean(comp.getServiceUid(), NlpProcessingUnit.class);
                    d = bean.process(comp.getConfiguration(), d);
                }
                newDocuments.add(d);
            }

            returnCorpus.setDocuments(newDocuments);
            System.out.println("End pipeline processing [" + pipeLine.getPipeLineName() + "] at " + new Date());


            returnCorpus = removeUnneededAnnotations(pipeLine, returnCorpus);


            serializeObject(pathOfResults + pipeLineId
                    + ".results", new CorpusSummary(returnCorpus));
        } catch (Exception e) {
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
                        if (f.getMetaData() != null && f.getMetaData().getPedigree() != null) {
                            if (toRemove.contains(f.getMetaData().getPedigree())) {
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
}
