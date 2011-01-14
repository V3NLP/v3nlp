package gov.va.research.inlp.services;

import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.ProcessingResource;
import gate.creole.SerialAnalyserController;
import gov.va.research.inlp.model.PipeLine;

import java.util.Hashtable;

public class HitexPOSTaggerImpl extends BaseGateService implements POSTaggerService {
	protected ProcessingResource sectionizer;

	@javax.annotation.Resource(name = "hitexPOSTagger")
	protected ProcessingResource posTagger;

	public gov.va.vinci.cm.Corpus posTagging(gov.va.vinci.cm.Corpus _corpus) {
		SerialAnalyserController controller = null;
		Corpus corpus = null;
		Hashtable<String, Document> corpusDocKeyDocument = new Hashtable<String, Document>();

		gov.va.vinci.cm.Corpus results = new gov.va.vinci.cm.Corpus();

		try {
			controller = (SerialAnalyserController) Factory.createResource(
					"gate.creole.SerialAnalyserController", Factory
							.newFeatureMap(), Factory.newFeatureMap(), "V3NLP");
			controller.reInit();
			
			// Add POS Tagger
			controller.add(this.posTagger);
			
			// Add Corpus
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

}
