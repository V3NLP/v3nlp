/**
 * Copyright (C) 2011-
 *
 * All rights reserved.
 */
package gov.va.vinci.v3nlp.services.gate;

import gate.*;
import gate.Corpus;
import gate.Document;
import gate.creole.ResourceInstantiationException;
import gate.creole.SerialAnalyserController;
import gate.util.InvalidOffsetException;
import gov.va.vinci.cm.Annotation;
import gov.va.vinci.cm.*;
import gov.va.vinci.v3nlp.NlpUtilities;
import gov.va.vinci.v3nlp.services.BaseNlpProcessingUnit;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Logger;

public class BaseGateService extends BaseNlpProcessingUnit {
    private static Logger log = Logger.getLogger(BaseGateService.class.getCanonicalName());
    /**
     * Create the corpus given a common model document.
     *
     * @param d the document to create a gate corpus for.
     * @return a corpus
     * @throws ResourceInstantiationException
     */
    @SuppressWarnings("unchecked")
    protected Corpus createGateCorpusFromCommonModel(
            DocumentInterface d)
            throws ResourceInstantiationException {
        Corpus corpus = Factory.newCorpus("V3NLP Corpus From Common Model");

        String key = (String) d.getDocumentName();
        if (key == null) {
            key = d.getDocumentId();
        }
        gate.Document doc = Factory.newDocument(d.getContent());
        doc.setName(key);

        for (AnnotationInterface a : d.getAnnotations().getAll()) {
            try {
                addCommonModelAnnotationToGateDoc(doc, a);
            } catch (InvalidOffsetException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        corpus.add(doc);
        return corpus;
    }

    protected DocumentInterface processGateResults(gate.Corpus corpus, gov.va.vinci.cm.Document d, Integer offset, String pedigree) throws InvalidOffsetException {
        gov.va.vinci.cm.Corpus results = new gov.va.vinci.cm.Corpus();
        List<String> docEnum = corpus.getDocumentNames();
        Document gateDoc = (Document) corpus.iterator().next();

        if (docEnum.size() != 1) {
            throw new RuntimeException("Processing module returned " + docEnum.size() + ". It should have returned one document.");
        }

        if (d == null) {
            d = new gov.va.vinci.cm.Document();
            d.setContent(gateDoc.getContent().toString());
        }

        List<Annotation> resultsToAdd =  processDocumentForReturn(gateDoc, offset, pedigree);

        mergeAnnotations(d, resultsToAdd);
        return d;
    }

    protected void mergeAnnotations(gov.va.vinci.cm.Document d, List<Annotation> resultsToAdd) {
        for (Annotation toAdd : resultsToAdd){
            boolean found = false;
            for (AnnotationInterface alreadyInDocument: d.getAnnotations().getAll()) {
                if (alreadyInDocument.getBeginOffset() == toAdd.getBeginOffset() &&
                    alreadyInDocument.getEndOffset() == toAdd.getEndOffset()) {
                    ((Annotation)alreadyInDocument).getFeatures().addAll(toAdd.getFeatures());
                    found = true;
                }
            }
            if (!found) {
                d.getAnnotations().getAll().add(toAdd);
            }
        }
    }

    protected DocumentInterface processGateResults(gate.Corpus corpus, String pedigree) throws InvalidOffsetException {
        return processGateResults(corpus, null, 0, pedigree);
    }

    /**
     * Cleans up potentially dangling resources in the controller/corpus.
     *
     * @param controller
     * @param corpus
     */
    protected void cleanupPipeLine(SerialAnalyserController controller,
                                   Corpus corpus) {

        try {
            if (corpus != null) {
                for (int i = 0; i < corpus.size(); i++) {
                    Document doc2 = (Document) corpus.get(i);
                    Factory.deleteResource(doc2);
                }

                corpus.clear();
            }
            controller.cleanup();
        } catch (Exception e) {
            log.info("Got exception: " + e);
        }

    }

    /**
     * This returns a list of common model annotations from a gate document.
     *
     * @param gateDoc the gate document to get annotations from.
     * @param offset  Because we only send pieces of a larger document, this offset is the spot in the larger document
     *                where this piece resides. It is added to the gate annotation to get offsets relative to the larger document.
     * @return
     * @throws InvalidOffsetException
     */
    protected List<Annotation> processDocumentForReturn(Document gateDoc, Integer offset, String pedigree) throws InvalidOffsetException {

        AnnotationSet annotations = gateDoc.getAnnotations();
        List<Annotation> results = new ArrayList<Annotation>();
        Iterator<gate.Annotation> i = annotations.iterator();
        while (i.hasNext()) {
            gate.Annotation a = i.next();
            // This feature is set on existing annotations converted from common model.
            if (!a.getFeatures().containsKey("V3NLP-CONVERTED-FROM-COMMON-MODEL")) {
                results.add(NlpUtilities.convertAnnotation(a,
                        gateDoc.getContent().getContent(a.getStartNode().getOffset(), a.getEndNode().getOffset()).toString(),
                        offset, pedigree));
            }
        }
        return results;
    }

    private void addCommonModelAnnotationToGateDoc(gate.Document doc, AnnotationInterface a) throws InvalidOffsetException {
        if (doc == null) {
            String message = "Invalid document: 'null'.";
            throw new RuntimeException(message);
        }

        AnnotationSet newAnnotations = doc.getAnnotations();

        for (Feature f : ((Annotation) a).getFeatures()) {
            FeatureMap features = gate.Factory.newFeatureMap();

            for (FeatureElement fe : f.getFeatureElements()) {
                // Copy Features over.
                features.put(fe.getName(), fe.getValue());
            }

            features.put("V3NLP-CONVERTED-FROM-COMMON-MODEL", "true");
            // Add the annotation for this particular feature.
            newAnnotations.add(new Long(a.getBeginOffset()), new Long(a
                    .getEndOffset()), f.getFeatureName(), features);
        }

    }
}
