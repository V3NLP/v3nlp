package gov.va.vinci.v3nlp.services.hitex;

import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.ProcessingResource;
import gate.creole.SerialAnalyserController;
import gov.va.vinci.v3nlp.services.BaseGateService;
import gov.va.vinci.v3nlp.services.TokenizerService;

import java.util.Hashtable;

public class HitexTokenizerImpl extends BaseGateService implements TokenizerService {
	@javax.annotation.Resource(name = "defaultTextTokenizer")
	protected ProcessingResource tokenizer;

	public gov.va.vinci.cm.Corpus tokenize(gov.va.vinci.cm.Corpus _corpus) {
		SerialAnalyserController controller = null;
		Corpus corpus = null;
		Hashtable<String, Document> corpusDocKeyDocument = new Hashtable<String, Document>();

		gov.va.vinci.cm.Corpus results = new gov.va.vinci.cm.Corpus();

		try {
			// Instantiate a gate controller
			controller = (SerialAnalyserController) Factory.createResource(
					"gate.creole.SerialAnalyserController", Factory
							.newFeatureMap(), Factory.newFeatureMap(), "V3NLP");
			controller.reInit();
			
			// Add Tokenizer
			controller.add(this.tokenizer);
			
			// Add Corpus
			corpus = createGateCorpusFromCommonModel(_corpus, corpusDocKeyDocument);
			controller.setCorpus(corpus);
			
			// run the application
			controller.execute();
			
			// Process gate results to common model.
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
}