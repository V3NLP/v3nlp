package gov.va.research.inlp.services;

import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.ProcessingResource;
import gate.creole.SerialAnalyserController;
import gov.va.research.inlp.gate.SectionizerHeaderFactory;
import gov.va.research.inlp.model.PipeLine;
import hitex.gate.Sectionizer;
import hitex.gate.regex.ConceptFinder;
import hitex.util.Header;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;

public class HitexGateModulesImpl extends BaseGateService {
	protected ProcessingResource sectionizer;

	@javax.annotation.Resource(name = "defaultTextTokenizer")
	protected ProcessingResource textTokenizer;

	@javax.annotation.Resource(name = "defaultSentenceSplitter")
	protected ProcessingResource sentenceSplitter;

	protected ProcessingResource negEx;

	private org.springframework.core.io.Resource sectionizerHeadersUrl;


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

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * gov.va.research.inlp.NlpService#processPipeLine(gov.va.research.inlp.
	 * model.PipeLine)
	 */
	public gov.va.vinci.cm.Corpus processPipeLine(PipeLine dataToProcess, gov.va.vinci.cm.Corpus _corpus) {
		SerialAnalyserController controller = null;
		Corpus corpus = null;
		Hashtable<String, Document> corpusDocKeyDocument = new Hashtable<String, Document>();
		ConceptFinder regexConceptFinder = createConceptFinder();
		gov.va.vinci.cm.Corpus results = new gov.va.vinci.cm.Corpus();

		try {
			controller = (SerialAnalyserController) Factory.createResource(
					"gate.creole.SerialAnalyserController", Factory
							.newFeatureMap(), Factory.newFeatureMap(), "V3NLP");
			controller.reInit();
			
			if (dataToProcess.hasOperation(gov.va.research.inlp.model.operations.Tokenizer.class)) {
				controller.add(this.textTokenizer);
			}
			
			if (dataToProcess.hasOperation(gov.va.research.inlp.model.operations.SentenceSplitter.class)) {
				controller.add(this.sentenceSplitter);
			}
			
			if (dataToProcess.hasSectionCriteria()) {
				addSectionizer(dataToProcess, controller);
			}

			corpus = createGateCorpusFromCommonModel(_corpus, corpusDocKeyDocument);
			controller.setCorpus(corpus);

			if (!dataToProcess.getRegularExpressionConfiguration().equals("")) {
				addRegEx(dataToProcess, controller, regexConceptFinder);
			}


			

			// run the application
			controller.execute();
			results = processGateResults(corpusDocKeyDocument);
			results.setCorpusName(dataToProcess.getPipeLineName());

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



	/*************** PRIVATE METHODS BELOW HERE ********************************/

	private void cleanupPipeLine(SerialAnalyserController controller,
			Corpus corpus, ConceptFinder regexConceptFinder) {
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
}