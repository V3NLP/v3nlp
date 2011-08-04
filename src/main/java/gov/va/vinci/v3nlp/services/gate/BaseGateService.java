package gov.va.vinci.v3nlp.services.gate;

import gate.AnnotationSet;
import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.FeatureMap;
import gate.creole.ResourceInstantiationException;
import gate.creole.SerialAnalyserController;
import gate.util.InvalidOffsetException;
import gov.va.vinci.cm.Annotation;
import gov.va.vinci.cm.AnnotationInterface;
import gov.va.vinci.cm.DocumentInterface;
import gov.va.vinci.cm.Feature;
import gov.va.vinci.cm.FeatureElement;
import gov.va.vinci.v3nlp.NlpUtilities;
import gov.va.vinci.v3nlp.registry.NlpComponentProvides;

import java.util.*;

public class BaseGateService {

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
        Corpus corpus;
        corpus = Factory.newCorpus("V3NLP Corpus From Common Model");

        String key = (String) d.getDocumentName();
        if (key == null) {
            key = d.getDocumentId();
        }
        gate.Document doc = Factory.newDocument(d.getContent());
        doc.setName(key);

        for (AnnotationInterface a : d.getAnnotations().getAll()) {
            try {
                addAnnotations(doc, a);
            } catch (InvalidOffsetException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
        }
        corpus.add(doc);
        return corpus;
    }

    protected DocumentInterface processGateResults(gate.Corpus corpus, gov.va.vinci.cm.Document d, Integer offset) throws InvalidOffsetException {
        gov.va.vinci.cm.Corpus results = new gov.va.vinci.cm.Corpus();
        List<String> docEnum = corpus.getDocumentNames();
        Document gateDoc = (Document) corpus.iterator().next();

        if (docEnum.size() != 1) {
            throw new RuntimeException("Processing module returned " + docEnum.size() + ". It should have returned one document.");
        }

        if (d == null) {
            d= new gov.va.vinci.cm.Document();
            d.setContent(gateDoc.getContent().toString());
        }

        d.getAnnotations().getAll().addAll(processDocumentForReturn(gateDoc, offset));
        return d;
    }

    protected DocumentInterface processGateResults(gate.Corpus corpus) throws InvalidOffsetException {
        return processGateResults(corpus, null, 0);
    }

    /**
     * Cleans up potentially dangling resources in the controller/corpus.
     * @param controller
     * @param corpus
     */
    protected void cleanupPipeLine(SerialAnalyserController controller,
                                   Corpus corpus) {
        if (corpus != null) {
            for (int i = 0; i < corpus.size(); i++) {
                Document doc2 = (Document) corpus.get(i);
                Factory.deleteResource(doc2);
            }
        }
        corpus.clear();
        controller.cleanup();
    }

    /**
     * This returns a list of common model annotations from a gate document.
     * @param gateDoc the gate document to get annotations from.
     * @param offset Because we only send pieces of a larger document, this offset is the spot in the larger document
     *      where this piece resides. It is added to the gate annotation to get offsets relative to the larger document.
     * @return
     * @throws InvalidOffsetException
     */
    private List<Annotation> processDocumentForReturn(Document gateDoc, Integer offset) throws InvalidOffsetException {

        AnnotationSet annotations = gateDoc.getAnnotations();
        List<Annotation> results = new ArrayList<Annotation>();
        Iterator<gate.Annotation> i = annotations.iterator();
        while (i.hasNext()) {
            gate.Annotation a = i.next();
            results.add(NlpUtilities.convertAnnotation(a,
                    gateDoc.getContent().getContent(a.getStartNode().getOffset(), a.getEndNode().getOffset()).toString(),
                    offset));
        }
        return results;
    }

    private void addAnnotations(gate.Document doc, AnnotationInterface a) throws InvalidOffsetException {
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

            // Add the annotation for this particular feature.
            newAnnotations.add(new Long(a.getBeginOffset()), new Long(a
                    .getEndOffset()), f.getFeatureName(), features);
        }

    }

    protected List<Annotation> getProcessList(DocumentInterface originalDocument, List<NlpComponentProvides> previousModuleProvided) {
        List<Annotation> toProcess = new ArrayList<Annotation>();
        if (previousModuleProvided == null || previousModuleProvided.size() < 1) {
            Annotation a = new Annotation();
            a.setBeginOffset(0);
            a.setEndOffset(originalDocument.getContent().length());
            a.setContent(originalDocument.getContent());
            toProcess.add(a);
        } else {
            for (NlpComponentProvides p : previousModuleProvided) {
                toProcess.addAll(((gov.va.vinci.cm.Document) originalDocument).findAnnotationsWithFeatureName(p.getName()));
            }
        }
        return toProcess;
    }
}
