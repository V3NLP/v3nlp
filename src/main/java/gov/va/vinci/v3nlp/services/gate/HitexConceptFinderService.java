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

/**
 * Hitex concept finder implementation using Regular Expressions.
 *
 */
public class HitexConceptFinderService extends BaseGateService implements NlpProcessingUnit {
    protected ProcessingResource resource;

    public HitexConceptFinderService(ProcessingResource resource) {
        this.resource = resource;
    }

    /**
     * Processing method
     * @param config  The config string should be xml in the following format:
     * <?xml version="1.0" encoding="ISO-8859-1" ?>
     *  <concepts>
     *  <concept>
     *    <def><![CDATA[(?i)pain]]></def>
     *    <capt_group_num>0</capt_group_num>
     *    <type>RegEx</type>
     *    <name>Label</name>
     *    <features>
     *      <feature>
     *        <name>code</name>
     *        <value><![CDATA[]]></value>
     *      </feature>
     *    </features>
     *  </concept>
     *  </concepts>
     *  <sections>
     *      <section>DIAG</section>
     *      <section>ADM</section>
     *  </sections>
     *
     * This would look for the regular expression pain in the corpus. Assuming sectionizing had been done, it is
     * limited to DIAG and ADM sections. You can change <sections> to <sections exlucde='true'> to ignore sections.
     *
     * @param _corpus  The corpus to be processed
     * @return the annotated corpus.
     */
    public gov.va.vinci.cm.Corpus process(String config, Corpus _corpus) {
        SerialAnalyserController controller = null;
        gate.Corpus corpus = null;
        Hashtable<String, Document> corpusDocKeyDocument = new Hashtable<String, Document>();
        ConceptFinder regexConceptFinder = (ConceptFinder) resource;
        gov.va.vinci.cm.Corpus results = new gov.va.vinci.cm.Corpus();

        try {
            controller = (SerialAnalyserController) Factory.createResource(
                    "gate.creole.SerialAnalyserController", Factory
                    .newFeatureMap(), Factory.newFeatureMap(), "V3NLP");
            controller.reInit();

            corpus = createGateCorpusFromCommonModel(_corpus,
                    corpusDocKeyDocument);
            controller.setCorpus(corpus);

            String regExFilter = getRegExFilter(config);

            String sectionFilter = getSectionFilter(config);

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

    private String getSectionFilter(String config) {
        StringBuffer filter = new StringBuffer();

        if (GenericValidator.isBlankOrNull(config)) {
            return null;
        }

        boolean inSections = false;
        boolean foundAtLeastOne = false;
        for (String line : config.split(
                "\r\n|\r|\n")) {
            if (!org.apache.commons.validator.GenericValidator
                    .isBlankOrNull(line)) {

                if (line.trim().equals("<sections>")) {
                    inSections = true;
                    foundAtLeastOne = false;
                    filter.append("(");
                }
                if (line.trim().equals("<sections exclude='true'>")) {
                    inSections  = true;
                    foundAtLeastOne = false;
                    filter.append("!(");
                }

                if (inSections && line.contains("<section>")) {
                    String section = line.substring(line.indexOf("<section>") + 9, line.lastIndexOf("</section>"));
                    filter.append("(category = " + section + ") &&");
                    foundAtLeastOne = true;
                }
                if (line.trim().equals("</sections>")) {
                    if (foundAtLeastOne) {
                        filter = filter.delete(filter.length() -2, filter.length());
                    }
                    filter.append(")\n");
                }
            }
        }

        if (filter.toString().trim().equals("()")) {
            return null;
        }
        return filter.toString();
    }

    private String getRegExFilter(String config) {
        StringBuffer filter = new StringBuffer();
        if (GenericValidator.isBlankOrNull(config)) {
            return "";
        }
        for (String line : config.split(
                "\r\n|\r|\n")) {
            if (!org.apache.commons.validator.GenericValidator
                    .isBlankOrNull(line)) {
                filter.append(line + "\n");
                if (line.trim().equals("</concepts>")) {
                    return filter.toString();
                }
            }
        }

        return filter.toString();
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
