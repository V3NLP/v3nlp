package gov.va.vinci.v3nlp.services;

import gov.va.vinci.cm.DocumentInterface;
import gov.va.vinci.v3nlp.registry.NlpComponent;

import java.util.List;


/**
 * Processing unit interface. All Hitex/Metamap/Gate NLP modules
 * extend this to allow them to be easily plugged into v3nlp.
 */
public interface NlpProcessingUnit {

    /**
     * Process a single document.
     * @param config  a module specific config. This can be xml, a string, etc..
     *                the user interface will send this in, and the processing unit
     *                has the responsibility to know what format the config should be
     *                in, and how to process it.
     * @param _document   the document to process.
     * @param previousModuleProvided  a list of the previous components
     *                                from the last nlp processing unit in
     *                                the pipeline.
     * @return the document with the new annotations added.
     */
    public abstract DocumentInterface process(String config, DocumentInterface _document, List<NlpComponent> previousModuleProvided);

    public abstract void initialize();

    public abstract void destroy();
}
