package gov.va.vinci.v3nlp.services;

import gov.va.vinci.cm.DocumentInterface;
import gov.va.vinci.v3nlp.registry.NlpComponent;
import gov.va.vinci.v3nlp.registry.NlpComponentProvides;

import java.util.List;


public interface NlpProcessingUnit {

    public abstract DocumentInterface process(String config, DocumentInterface _document, List<NlpComponent> previousModuleProvided);

    public abstract void initialize();

    public abstract void destroy();
}
