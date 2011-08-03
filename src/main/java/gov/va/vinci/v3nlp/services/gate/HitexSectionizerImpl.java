package gov.va.vinci.v3nlp.services.gate;

import gate.Corpus;
import gate.Factory;
import gate.ProcessingResource;
import gate.creole.SerialAnalyserController;
import gov.va.vinci.cm.DocumentInterface;
import gov.va.vinci.v3nlp.registry.NlpComponentProvides;
import gov.va.vinci.v3nlp.services.NlpProcessingUnit;
import hitex.gate.Sectionizer;
import org.apache.commons.validator.GenericValidator;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.List;

public class HitexSectionizerImpl extends BaseGateService implements NlpProcessingUnit {

    protected ProcessingResource sectionizer;

    /**
     * Process method
     * @param config This is an xml configuration for custom section headers. Not needed
     * if using the standard (vi tconfigured) section headers. If passing in a custom
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
     * @param d The Document to processes.
     * @return  Corpus annotated with section_header and section annotations denoting the section_headers
     * and section content.
     */
    @Override
    public DocumentInterface process(String config, DocumentInterface d, List<NlpComponentProvides> previousModuleProvided) {
        SerialAnalyserController controller = null;
        Corpus corpus = null;
        gov.va.vinci.cm.Corpus results = new gov.va.vinci.cm.Corpus();

        try {
            controller = (SerialAnalyserController) Factory.createResource(
                    "gate.creole.SerialAnalyserController", Factory
                    .newFeatureMap(), Factory.newFeatureMap(), "V3NLP");
            controller.reInit();

            addSectionizer(config, controller);

            corpus = createGateCorpusFromCommonModel(d);
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

            org.w3c.dom.Document doc = null;
            try {
                // Create a builder factory
                DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
                factory.setValidating(false);
                // Create the builder and parse the file
                doc = factory.newDocumentBuilder().parse(new InputSource(new StringReader(customSectionizerRules)));
                if (doc==null) {
                    throw new RuntimeException("Custom sectionizer rules parsing returned null.");
                }
                s.setHeadersList(doc);
            }
            catch (Exception e) {
                throw new RuntimeException(e.getMessage());
            }

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