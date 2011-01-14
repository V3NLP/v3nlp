package gov.va.research.inlp.services;

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

import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

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

			results.addDocument(s, corpusDocKeyDocument.get(s).getContent().toString(),
					processDocumentForReturn(corpusDocKeyDocument.get(s)));
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
			results.put(convertGateAnnotation(a, d.getContent().getContent(a.getStartNode().getOffset(), a.getEndNode().getOffset()).toString()));		
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
	
	private Annotation convertGateAnnotation(gate.Annotation gateAnnotation,
			String content) {
		Annotation resultAnnotation = new Annotation(gateAnnotation.getId());
		
		resultAnnotation.setBeginOffset(gateAnnotation.getStartNode()
				.getOffset().intValue());
		resultAnnotation.setEndOffset(gateAnnotation.getEndNode().getOffset()
				.intValue());
		Feature feature = new Feature((String) gateAnnotation.getFeatures()
				.get("type"), (String) gateAnnotation.getFeatures().get("name"));
		feature.getFeatureElements().add(new FeatureElement("type", gateAnnotation.getType()));
		feature.getMetaData().setCreatedDate(new Date());
		if (gateAnnotation.getFeatures().get("type") != null) {
			feature.getMetaData().setPedigree((String) gateAnnotation.getFeatures().get("type"));
		} else {
			feature.getMetaData().setPedigree(gateAnnotation.getType());	
		}
		

		// Copy Features
		for (Object o : gateAnnotation.getFeatures().keySet()) {
			Object j = gateAnnotation.getFeatures().get(o);
			if (j instanceof String) {
				feature.getFeatureElements().add(
						new FeatureElement((String) o, (String) j));
			} else if (j instanceof List) {
				// TODO Implement
			} else if (j instanceof Set) {
				// TODO Implement
			} else if (j == null) {
				
			} else {
				System.out.println(j);
				throw new RuntimeException("Unknown Feature type.");
			}
		}
		
		// TODO set theNumWhiteSpaceThatFollows if available.
		resultAnnotation.setOriginalContent(content);
		resultAnnotation.getFeatures().add(feature);
		return resultAnnotation;
	}

}
