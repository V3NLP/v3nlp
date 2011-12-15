/**
 * Copyright (C) 2011-
 *
 * All rights reserved.
 */
package gov.va.vinci.v3nlp.model;

import gov.va.vinci.cm.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class CorpusSummary implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    private Corpus corpus;

    private List<String> featureNames = new ArrayList<String>();

    private List<DocumentFeatureNameCount> documentFeatureNameCounts = new ArrayList<DocumentFeatureNameCount>();

    private List<Feature> changedAnnotations = new ArrayList<Feature>();


    /**
     * Full arg constructor for corpus args, needed to copy a corpus in.
     *
     * @param c The corpus to create this corpus summary from.
     */
    public CorpusSummary(Corpus c) {
        this.setCorpus(c);
        updateSummary();
    }

    public CorpusSummary() {
    }

    public void setCorpus(Corpus c) {
        this.corpus = c;
    }

    public void updateSummary() {
        featureNames.clear();
        documentFeatureNameCounts.clear();

        for (DocumentInterface doc : corpus.getDocuments()) {
            HashMap<String, Long> tempResults = new HashMap<String, Long>();
            for (AnnotationInterface a : doc.getAnnotations().getAll()) {
                if (a instanceof Annotation) {
                    for (Feature f : ((Annotation) a).getFeatures()) {
                        // See if this is a manual annotation.
                        if (f.getMetaData()!= null && f.getMetaData().getPedigree() != null &&
                            f.getMetaData().getPedigree().startsWith("Manual:")) {
                            this.getChangedAnnotations().add(f);
                        }

                        if (f.getFeatureName() == null) {
                            continue;
                        }

                        String tempFeatureName = f.getFeatureName();
                        if (!featureNames.contains(tempFeatureName)) {
                            featureNames.add(tempFeatureName);
                        }

                        if (tempResults.containsKey(tempFeatureName)) {

                            tempResults.put(tempFeatureName, ((Long) tempResults.get(tempFeatureName)).longValue() + 1L);
                        } else {
                            tempResults.put(tempFeatureName, 1L);
                        }
                    }
                }

            } // End For Annotations

            for (String key : tempResults.keySet()) {
                this.documentFeatureNameCounts.add(new DocumentFeatureNameCount(doc.getDocumentId(), key, tempResults.get(key)));
            }
        } // End for each document
    }

    public List<String> getFeatureNames() {
        return featureNames;
    }

    public void setFeatureNames(List<String> featureNames) {
        this.featureNames = featureNames;
    }

    public List<DocumentFeatureNameCount> getDocumentFeatureNameCounts() {
        return documentFeatureNameCounts;
    }

    public void setDocumentFeatureNameCounts(List<DocumentFeatureNameCount> documentFeatureNameCounts) {
        this.documentFeatureNameCounts = documentFeatureNameCounts;
    }

    public Corpus getCorpus() {
        return corpus;
    }

    public List<Feature> getChangedAnnotations() {
        return changedAnnotations;
    }

    public void setChangedAnnotations(List<Feature> changedAnnotations) {
        this.changedAnnotations = changedAnnotations;
    }
}

