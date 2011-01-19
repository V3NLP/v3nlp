package gov.va.research.inlp.services.hitex;

import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.ProcessingResource;
import gate.creole.SerialAnalyserController;
import gov.va.research.inlp.gate.SectionizerHeaderFactory;
import gov.va.research.inlp.model.BaseNlpModule;
import gov.va.research.inlp.services.BaseGateService;
import gov.va.research.inlp.services.SectionizerService;
import hitex.gate.Sectionizer;
import hitex.util.Header;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.List;
import java.util.StringTokenizer;

import org.apache.commons.validator.GenericValidator;

public class HitexSectionizerImpl extends BaseGateService implements SectionizerService {

	protected ProcessingResource sectionizer;

	private org.springframework.core.io.Resource sectionizerHeadersUrl;

	/* (non-Javadoc)
	 * @see gov.va.research.inlp.services.SectionizerService#getDefaultSectionizerConfiguration()
	 */
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

	/* (non-Javadoc)
	 * @see gov.va.research.inlp.services.SectionizerService#processPipeLine(java.lang.String, java.lang.String, gov.va.vinci.cm.Corpus)
	 */
	public gov.va.vinci.cm.Corpus sectionize( String customSectionizerRules,  gov.va.vinci.cm.Corpus _corpus) {
		SerialAnalyserController controller = null;
		Corpus corpus = null;
		Hashtable<String, Document> corpusDocKeyDocument = new Hashtable<String, Document>();
		gov.va.vinci.cm.Corpus results = new gov.va.vinci.cm.Corpus();

		try {
			controller = (SerialAnalyserController) Factory.createResource(
					"gate.creole.SerialAnalyserController", Factory
							.newFeatureMap(), Factory.newFeatureMap(), "V3NLP");
			controller.reInit();
			
			addSectionizer(customSectionizerRules, controller);
			
			corpus = createGateCorpusFromCommonModel(_corpus, corpusDocKeyDocument);
			controller.setCorpus(corpus);

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
	 * Create a sectionizer. Overridden in Spring with Method Injection to
	 * return a new Sectionizer for every request.
	 * 
	 * @return a Sectionizer.
	 */
	public Sectionizer createSectionizer() {
		return null;
	}

	/* (non-Javadoc)
	 * @see gov.va.research.inlp.services.SectionizerService#getAvailableSectionHeaders()
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


	private void addSectionizer(String customSectionizerRules,
			SerialAnalyserController controller) {
		Sectionizer s = this.createSectionizer();
		if (customSectionizerRules != null) {
			List<String> newRules = new ArrayList<String>();
			for (String line : customSectionizerRules.split(
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