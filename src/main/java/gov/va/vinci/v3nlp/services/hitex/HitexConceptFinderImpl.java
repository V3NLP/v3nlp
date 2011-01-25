package gov.va.vinci.v3nlp.services.hitex;

import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.creole.SerialAnalyserController;
import gov.va.vinci.v3nlp.services.BaseGateService;
import gov.va.vinci.v3nlp.services.ConceptFinderService;
import hitex.gate.regex.ConceptFinder;

import java.util.Hashtable;

import org.apache.commons.validator.GenericValidator;

public class HitexConceptFinderImpl extends BaseGateService implements ConceptFinderService {

	public gov.va.vinci.cm.Corpus conceptFinder(String regExFilter,
			String sectionFilter, gov.va.vinci.cm.Corpus _corpus) {

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

			corpus = createGateCorpusFromCommonModel(_corpus,
					corpusDocKeyDocument);
			controller.setCorpus(corpus);

			addRegEx(regExFilter, sectionFilter, controller, regexConceptFinder);

			// run the application
			controller.execute();
			results = processGateResults(corpusDocKeyDocument);
			results.setCorpusName(_corpus.getCorpusName());

		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			cleanupPipeLine(controller, corpus);
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

	private void addRegEx(String regExFilter, String sectionizerFilter,
			SerialAnalyserController controller,
			ConceptFinder regexConceptFinder) {

		regexConceptFinder.setRulesXML(regExFilter);

		if (!GenericValidator.isBlankOrNull(sectionizerFilter) && !sectionizerFilter.equals("ANY")) {
			regexConceptFinder.setFilterText(sectionizerFilter);
			regexConceptFinder.setUseSections(true);
		} else {
			regexConceptFinder.setUseSections(false);
		}
		controller.add(regexConceptFinder);
	}

}