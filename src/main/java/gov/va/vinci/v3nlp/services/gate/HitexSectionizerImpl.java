package gov.va.vinci.v3nlp.services.gate;

import gate.Corpus;
import gate.Document;
import gate.Factory;
import gate.ProcessingResource;
import gate.creole.SerialAnalyserController;
import gov.va.vinci.v3nlp.services.NlpProcessingUnit;
import hitex.gate.Sectionizer;
import org.apache.commons.validator.GenericValidator;

import java.util.ArrayList;
import java.util.Hashtable;
import java.util.List;

public class HitexSectionizerImpl extends BaseGateService implements NlpProcessingUnit {

    protected ProcessingResource sectionizer;

    /**
     * Process method
     * @param config This is an xml configuration for custom section headers. Not needed
     * if using the standard (configured) section headers. If passing in a custom
     * configuration, it should be in the format:
     * <?xml version="1.0" encoding="ISO-8859-1" ?>
     * <!--  This file contains header definitions for Sectionizer NLP component -->
     * <headers>
 	 *  <header categories="OTHER" captGroupNum="0" >
	 *  	<![CDATA[(?i)\(BILATERAL\)\s{1,3}CAROTID\s{0,3}(:|;)]]>
	 *  </header>
	 *  <header categories="OTHER" captGroupNum="0" >
	 * 	    <![CDATA[(?i)\(BILATERAL\)\s{1,3}FEMORAL\s{0,3}(:|;)]]>
	 *  </header>
	 *  <header categories="OTHER" captGroupNum="0" >
	 * 	    <![CDATA[(?i)ABD\s{0,3}(:|;)]]>
	 *  </header>
     * </headers>
     *
     * @param _corpus The corpus to processes.
     * @return  Corpus annotated with section_header and section annotations denoting the section_headers
     * and section content.
     */
    @Override
    public gov.va.vinci.cm.Corpus process(String config, gov.va.vinci.cm.Corpus _corpus) {
        SerialAnalyserController controller = null;
        Corpus corpus = null;
        Hashtable<String, Document> corpusDocKeyDocument = new Hashtable<String, Document>();
        gov.va.vinci.cm.Corpus results = new gov.va.vinci.cm.Corpus();

        try {
            controller = (SerialAnalyserController) Factory.createResource(
                    "gate.creole.SerialAnalyserController", Factory
                    .newFeatureMap(), Factory.newFeatureMap(), "V3NLP");
            controller.reInit();

            addSectionizer(config, controller);

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

    private void addSectionizer(String customSectionizerRules,
                                SerialAnalyserController controller) {
        Sectionizer s = this.createSectionizer();
        if (!GenericValidator.isBlankOrNull(customSectionizerRules)) {
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


    @Override
    public void initialize() {
        // No-op in this implementation.
    }

    @Override
    public void destroy() {
        // No-op in this implementation.
    }
}