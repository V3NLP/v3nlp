package gov.va.vinci.v3nlp.services.gate;

import gate.Document;
import gate.Factory;
import gate.ProcessingResource;
import gate.creole.SerialAnalyserController;
import gov.va.vinci.cm.Corpus;
import gov.va.vinci.cm.DocumentInterface;
import gov.va.vinci.v3nlp.registry.NlpComponentProvides;
import gov.va.vinci.v3nlp.services.NlpProcessingUnit;

import java.util.Hashtable;
import java.util.List;

public class GenericGateService extends BaseGateService implements NlpProcessingUnit {

	protected ProcessingResource resource;

    public GenericGateService(ProcessingResource resource) {
        this.resource = resource;
    }

    public DocumentInterface process(String config, DocumentInterface document,List<NlpComponentProvides> previousModuleProvided) {
        SerialAnalyserController controller = null;
		gate.Corpus corpus = null;

		gov.va.vinci.cm.Corpus results = new gov.va.vinci.cm.Corpus();

		try {
			controller = (SerialAnalyserController) Factory.createResource(
                    "gate.creole.SerialAnalyserController", Factory
                    .newFeatureMap(), Factory.newFeatureMap(), "V3NLP");
			controller.reInit();

			// Add Resource
			controller.add(resource);

			// Add Corpus
			corpus = createGateCorpusFromCommonModel(document);
			controller.setCorpus(corpus);

			// run the application
			controller.execute();
			return(processGateResults(corpus));
		} catch (Exception e) {
			e.printStackTrace();
			throw new RuntimeException(e);
		} finally {
			cleanupPipeLine(controller, corpus);
		}
    }

    @Override
    public void initialize() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void destroy() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
