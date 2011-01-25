package gov.va.vinci.v3nlp.services;

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
import gov.va.vinci.cm.Annotations;
import gov.va.vinci.cm.DocumentInterface;
import gov.va.vinci.cm.Feature;
import gov.va.vinci.cm.FeatureElement;
import gov.va.vinci.v3nlp.NlpUtilities;

import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;

public class BaseGateService {

	/**
	 * Create the corpus given the pipeline and documents.
	 * 
	 * @param dataToProcess
	 *            the pipeline being processes
	 * @param docs
	 *            the string/document hashtable of corpus items
	 * @return a corpus
	 * @throws ResourceInstantiationException
	 */
	@SuppressWarnings("unchecked")
	protected Corpus createGateCorpusFromCommonModel(
			gov.va.vinci.cm.Corpus dataToProcess,
			Hashtable<String, gate.Document> docs)
			throws ResourceInstantiationException {
		Corpus corpus;
		corpus = Factory.newCorpus("V3NLP Corpus From Commond Model");

		for (DocumentInterface d: dataToProcess.getDocuments()) {
			String key = (String) d.getDocumentName();
			gate.Document doc = Factory.newDocument(d.getContent());
			doc.setName(key);
			
			for (AnnotationInterface a: d.getAnnotations().getAll()) {
				try {
					addAnnotations(doc, a);
				} catch (InvalidOffsetException e) {
					e.printStackTrace();
					throw new RuntimeException(e);
				}
			}

			docs.put(key, doc);
			corpus.add(doc);
		}
		return corpus;
	}
	
	protected gov.va.vinci.cm.Corpus processGateResults(
			Hashtable<String, Document> corpusDocKeyDocument) throws InvalidOffsetException {
		gov.va.vinci.cm.Corpus results = new gov.va.vinci.cm.Corpus();
		Enumeration<String> docEnum = corpusDocKeyDocument.keys();

		while (docEnum.hasMoreElements()) {
			String s = docEnum.nextElement();
	        gov.va.vinci.cm.Document d  = new gov.va.vinci.cm.Document();
	        d.setDocumentName(s);
	        d.setContent(corpusDocKeyDocument.get(s).getContent().toString());
	        d.setDocumentId(s);
	        d.setAnnotations(processDocumentForReturn(corpusDocKeyDocument.get(s)));
	        results.addDocument(d);
		}
		return results;
	}

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

	private Annotations processDocumentForReturn(Document d) throws InvalidOffsetException {
		AnnotationSet annotations = d.getAnnotations();
		Annotations results = new Annotations();
		Iterator<gate.Annotation> i = annotations.iterator();
		while (i.hasNext()) {
			gate.Annotation a = i.next();
			//System.out.println(a.getType());
			results.put(NlpUtilities.convertAnnotation(a, d.getContent().getContent(a.getStartNode().getOffset(), a.getEndNode().getOffset()).toString()));		
		}
		return results;
	}

	private void addAnnotations(gate.Document doc, AnnotationInterface a) throws InvalidOffsetException {

		if (doc == null) {
			String message = "Invalid document: 'null'.";
			throw new RuntimeException(message);
		}

		AnnotationSet newAnnotations = doc.getAnnotations();

		for (Feature f : ((Annotation)a).getFeatures()) {
			FeatureMap features = gate.Factory.newFeatureMap();
			
			for (FeatureElement fe: f.getFeatureElements()) {
				// Copy Features over. 
				features.put(fe.getName(), fe.getValue());
			}
 			
			// Add the annotation for this particular feature. 
			newAnnotations.add(new Long(a.getBeginOffset()), new Long(a
					.getEndOffset()), f.getMetaData().getPedigree(), features);

		}

	}
}
