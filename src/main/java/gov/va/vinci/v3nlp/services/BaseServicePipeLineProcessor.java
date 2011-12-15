/**
 * Copyright (C) 2011-
 *
 * All rights reserved.
 */
package gov.va.vinci.v3nlp.services;

import gov.va.vinci.cm.*;
import gov.va.vinci.v3nlp.model.BatchJobStatus;
import gov.va.vinci.v3nlp.model.ServicePipeLine;
import gov.va.vinci.v3nlp.model.ServicePipeLineComponent;
import gov.va.vinci.v3nlp.registry.NlpComponent;
import gov.va.vinci.v3nlp.registry.RegistryService;
import org.apache.commons.validator.GenericValidator;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;


@Transactional
public abstract class BaseServicePipeLineProcessor implements ServicePipeLineProcessor {
    protected EntityManager entityManager;
    protected RegistryService registryService;

    @PersistenceContext
    public void setEntityManager(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    @Transactional(readOnly = false)
    protected void updatePipeLineStatus(BatchJobStatus jobStatus, String status, String pathToResults) {
        jobStatus.setStatus(status);
        jobStatus.setResultPath(pathToResults);
        entityManager.merge(jobStatus);
    }

    public void setRegistryService(RegistryService registryService) {
        this.registryService = registryService;
    }

    /**
     * Given the services in the pipeline, remove all unneeded annotations where the
     * service component "keepAnnotationInFinalResult" is false.
     *
     * <strong>Note: This looks at feature.metadata.pedigree, a pipe delimited list
     * of pedigrees that created this feature. A feature is ONLY removed if all
     * pedigrees on a particular feature are selected to be removed.</strong>
     *
     * @param pipeLine
     * @param returnCorpus
     * @return
     */
    protected Corpus removeUnneededAnnotations(ServicePipeLine pipeLine, Corpus returnCorpus) {
        List<String> toRemove = new ArrayList<String>();

        /** Get a list of annotation types to remove. **/
        for (ServicePipeLineComponent comp : pipeLine.getServices()) {
            if (GenericValidator.isBlankOrNull(comp.getServiceUid())) {
                continue;
            }
            if (!comp.isKeepAnnotationsInFinalResult()) {
                NlpComponent loadedComp = registryService.getNlpComponent(comp.getServiceUid());
                toRemove.add(loadedComp.getPedigree());
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

                        String fName = f.getMetaData().getPedigree();

                        if (fName != null) {  // Has a pedigree, see if it needs removed.
                            String[] pedigrees = fName.split(Pattern.quote("|"));
                            boolean doRemove= true;

                            // Check the split pedigree list. Only remove this feature
                            // if ALL pedigrees are to be removed.
                            for (String p: pedigrees) {
                                if (!toRemove.contains(p)) {
                                     doRemove = false;
                                }
                            }
                            if (doRemove) {
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

}
