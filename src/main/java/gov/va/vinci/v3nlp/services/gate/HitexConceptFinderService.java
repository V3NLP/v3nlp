package gov.va.vinci.v3nlp.services.gate;

import gate.Document;
import gate.Factory;
import gate.ProcessingResource;
import gate.creole.SerialAnalyserController;
import gov.va.vinci.cm.Corpus;
import gov.va.vinci.v3nlp.services.NlpProcessingUnit;
import hitex.gate.regex.ConceptFinder;
import org.apache.commons.validator.GenericValidator;

import java.util.Hashtable;

public class HitexConceptFinderService extends BaseGateService implements NlpProcessingUnit{
    protected ProcessingResource resource;

    public HitexConceptFinderService(ProcessingResource resource) {
        this.resource = resource;
    }

    public gov.va.vinci.cm.Corpus process(String config, Corpus _corpus) {
        SerialAnalyserController controller = null;
        gate.Corpus corpus = null;
        Hashtable<String, Document> corpusDocKeyDocument = new Hashtable<String, Document>();
        ConceptFinder regexConceptFinder = (ConceptFinder)resource;
        gov.va.vinci.cm.Corpus results = new gov.va.vinci.cm.Corpus();

        try {
            controller = (SerialAnalyserController) Factory.createResource(
                    "gate.creole.SerialAnalyserController", Factory
                    .newFeatureMap(), Factory.newFeatureMap(), "V3NLP");
            controller.reInit();

            corpus = createGateCorpusFromCommonModel(_corpus,
                    corpusDocKeyDocument);
            controller.setCorpus(corpus);

            String regExFilter = config;
			String sectionFilter = "";      // TODO Get from configuration.

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

    @Override
    public void initialize() {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void destroy() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
