package gov.va.research.inlp;

import gate.Annotation;
import gate.AnnotationSet;
import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.ProcessingResource;
import gate.creole.ResourceInstantiationException;
import gate.creole.SerialAnalyserController;
import gov.va.research.inlp.gate.SectionizerHeaderFactory;
import gov.va.research.inlp.model.PipeLine;
import gov.va.research.inlp.model.operations.Negation;
import gov.va.vinci.cm.Annotations;
import hitex.gate.Sectionizer;
import hitex.gate.negex.NegEx;
import hitex.gate.regex.ConceptFinder;
import hitex.util.Header;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

public class GateNlpServiceImpl implements NlpService {
	protected ProcessingResource sectionizer;

	@javax.annotation.Resource(name = "defaultTextTokenizer")
	protected ProcessingResource textTokenizer;

	@javax.annotation.Resource(name = "defaultSentenceSplitter")
	protected ProcessingResource sentenceSplitter;

	protected ProcessingResource negEx;

	@javax.annotation.Resource(name = "defaultUmlsConceptFinder")
	protected ProcessingResource defaultUmlsConceptFinder;

	private org.springframework.core.io.Resource sectionizerHeadersUrl;

	private List<String> annotationTypesToReturn = new ArrayList<String>();

	public String getDefaultSectionizerConfiguration() throws Exception {
		Sectionizer s = this.createSectionizer();
		String result = "";

		if (s.getHeadersList() != null) {
			for (Object rule1 : s.getHeadersList()) {
				result += rule1 + "\n";
			}
		} else if (s.getHeadersURL() != null) {
			BufferedReader in = new BufferedReader(new InputStreamReader(s
					.getHeadersURL().openStream()));
			String str = null;
			while ((str = in.readLine()) != null) {
				result += str + "\n";
			}
		}
		return result;
	}

	public String getDefaultNegationConfiguration() throws Exception {
		NegEx defaultNegEx = createNegEx();
		String toReturn = "";

		if (defaultNegEx.getRulesURL() != null) {
			BufferedReader in = new BufferedReader(new InputStreamReader(
					defaultNegEx.getRulesURL().openStream()));
			String str = null;
			while ((str = in.readLine()) != null) {
				toReturn += str + "\n";
			}
		}

		if (defaultNegEx.getRulesList() != null) {
			for (String rule : defaultNegEx.getRulesList()) {
				toReturn += rule + "\n";
			}
		}

		return toReturn;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.va.research.inlp.NlpService#processPipeLine(gov.va.research.inlp.
	 * model.PipeLine)
	 */
	public gov.va.vinci.cm.Corpus processPipeLine(PipeLine dataToProcess) {
		SerialAnalyserController controller = null;
		Corpus corpus = null;
		Hashtable<String, Document> corpusDocKeyDocument = new Hashtable<String, Document>();
		ConceptFinder regexConceptFinder = createConceptFinder();
		gov.va.vinci.cm.Corpus results = new gov.va.vinci.cm.Corpus();

		try {
			controller = (SerialAnalyserController) Factory.createResource(
					"gate.creole.SerialAnalyserController", Factory
							.newFeatureMap(), Factory.newFeatureMap(), "ANNIE");
			controller.reInit();
			controller.add(this.textTokenizer);
			controller.add(this.sentenceSplitter);

			if (dataToProcess.hasSectionCriteria()) {
				addSectionizer(dataToProcess, controller);
			}

			corpus = createCorpus(dataToProcess, corpusDocKeyDocument);
			controller.setCorpus(corpus);

			if (!dataToProcess.getRegularExpressionConfiguration().equals("")) {
				addRegEx(dataToProcess, controller, regexConceptFinder);
			}

			handleNegation(dataToProcess, controller);

			results.setCorpusName(dataToProcess.getPipeLineName());

			// run the application
			controller.execute();
			Enumeration<String> docEnum = corpusDocKeyDocument.keys();

			while (docEnum.hasMoreElements()) {
				String s = docEnum.nextElement();
				results.addDocument(s, corpusDocKeyDocument.get(s).getContent().toString(),
						processDocumentForReturn(corpusDocKeyDocument.get(s)));
			}

			/** Add the format tags that were passed through. **/
			for (int d = 0; d < dataToProcess.getServices().size(); d++) {
				if (dataToProcess.getServices().get(d).getFormatInfo() != null) {
					results.addFormatInfo(dataToProcess.getServices().get(d)
							.getFormatInfo());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			cleanupPipeLine(controller, corpus, regexConceptFinder);
		}

		return results;
	}

	/**
	 * Create a concept finder. Overridden in Spring with Method Injection to
	 * return a new Concept Finder for every request.
	 * 
	 * @return a concept (regex) finder.
	 */
	public ConceptFinder createConceptFinder() {
		return null;
	}

	/**
	 * Create a concept finder. Overridden in Spring with Method Injection to
	 * return a new Negex Finder for every request.
	 * 
	 * @return a concept (regex) finder.
	 */
	public NegEx createNegEx() {
		return null;
	}

	/**
	 * Create a sectionizer. Overridden in Spring with Method Injection to
	 * return a new Sectionizer for every request.
	 * 
	 * @return a Sectionizer.
	 */
	public Sectionizer createSectionizer() {
		return null;
	}

	/**
	 * Get a list of available sections headers for the configured sectionizer.
	 * 
	 * @return A list of string section headers.
	 */
	public List<String> getAvailableSectionHeaders() {

		HashSet<String> map = new HashSet<String>();

		try {
			List<Header> result = SectionizerHeaderFactory
					.getHeaders(sectionizerHeadersUrl.getFile());

			for (Header h : result) {
				StringTokenizer st = new StringTokenizer(h.getCategories(), ",");
				while (st.hasMoreTokens()) {
					map.add(st.nextToken().trim());
				}
			}
			return new ArrayList<String>(map);
		} catch (Exception e) {
			throw new IllegalStateException(e);
		}
	}

	public void setSectionizerHeaders(org.springframework.core.io.Resource url) {
		this.sectionizerHeadersUrl = url;
	}

	/**
	 * The processes document will only return annotations the service has been
	 * configured to return (in order to avoid all of the noise annotations,
	 * such as SpaceToken and Token.)
	 * 
	 * @return the list of annotation types this service will return in the
	 *         result.
	 */
	public List<String> getAnnotationTypesToReturn() {
		return annotationTypesToReturn;
	}

	/**
	 * The processes document will only return annotations the service has been
	 * configured to return (in order to avoid all of the noise annotations,
	 * such as SpaceToken and Token.)
	 * 
	 * @param annotationTypesToReturn
	 *            a list of annotation types to return.
	 */
	public void setAnnotationTypesToReturn(List<String> annotationTypesToReturn) {
		this.annotationTypesToReturn = annotationTypesToReturn;
	}

	/*************** PRIVATE METHODS BELOW HERE ********************************/

	private void cleanupPipeLine(SerialAnalyserController controller,
			Corpus corpus, ConceptFinder regexConceptFinder) {
		defaultUmlsConceptFinder.cleanup();
		textTokenizer.cleanup();
		sentenceSplitter.cleanup();
		regexConceptFinder.cleanup();

		if (corpus != null) {
			for (int i = 0; i < corpus.size(); i++) {
				Document doc2 = (Document) corpus.get(i);
				Factory.deleteResource(doc2);
			}
		}
		corpus.clear();
		controller.cleanup();
	}

	private Annotations processDocumentForReturn(Document d) {
		AnnotationSet annotations = d.getAnnotations();
		Annotations results = new Annotations();
		Iterator<Annotation> i = annotations.iterator();
		while (i.hasNext()) {
			Annotation a = i.next();
			if (annotationTypesToReturn.contains(a.getType())) {
				results.put(NlpUtilities.convertAnnotation(a));
			}
		}
		return results;
	}

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
	private Corpus createCorpus(PipeLine dataToProcess,
			Hashtable<String, Document> docs)
			throws ResourceInstantiationException {
		Corpus corpus;
		corpus = Factory.newCorpus("NlpServiceImpl Corpus");

		Hashtable<String, String> dataCorpuses = dataToProcess
				.getCorpusContent();

		Enumeration<String> e = dataCorpuses.keys();

		// Iterate through documents.
		while (e.hasMoreElements()) {
			String key = (String) e.nextElement();
			Document doc = Factory.newDocument(dataCorpuses.get(key));
			doc.setName(key);
			docs.put(key, doc);
			corpus.add(doc);
		}
		return corpus;
	}


	private void addRegEx(PipeLine dataToProcess,
			SerialAnalyserController controller,
			ConceptFinder regexConceptFinder) {
		regexConceptFinder.setRulesXML(dataToProcess
				.getRegularExpressionConfiguration());

		if (dataToProcess.hasSectionCriteria()) {
			regexConceptFinder.setFilterText(dataToProcess
					.getSectionCriteriaExpression());
			regexConceptFinder.setUseSections(true);
		} else {
			regexConceptFinder.setUseSections(false);
		}
		controller.add(regexConceptFinder);
	}

	private void addSectionizer(PipeLine dataToProcess,
			SerialAnalyserController controller) {
		Sectionizer s = this.createSectionizer();
		if (dataToProcess.getCustomSectionRules() != null) {
			List<String> newRules = new ArrayList<String>();
			for (String line : dataToProcess.getCustomSectionRules().split(
					"\r\n|\r|\n")) {
				if (!org.apache.commons.validator.GenericValidator
						.isBlankOrNull(line)) {
					newRules.add(line);
				}
			}
			s.setHeadersList(newRules);
		}
		controller.add(s);
	}

	private void handleNegation(PipeLine dataToProcess,
			SerialAnalyserController controller) {

		if (dataToProcess.getNegation() != null) {
			NegEx ne = createNegEx();
			Negation negation = dataToProcess.getNegation();

			// Add the custom rules if there are any.
			if (negation.getUseCustomConfiguration()) {
				List<String> ruleList = new ArrayList<String>();

				for (String s : negation.getConfiguration().split("\r|\n")) {
					ruleList.add(s);
				}
				ne.setRulesList(ruleList);
			}
			controller.add(ne);
		}
	}

}
